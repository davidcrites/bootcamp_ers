package com.capital.one.daos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.postgresql.util.PSQLException;

import com.capital.one.datamodelbeans.Reimbursement;
import com.capital.one.datamodelbeans.UserRoles;
import com.capital.one.datamodelbeans.Users;


public class EmployeeDAOImpl implements EmployeeDAO{

	/***
	 * This method will be used for validating employee login and determining which screen to go to next, EmployeeMenu or FiManagerMenu
	 * @param username - this is provided by the user for authentication (combo with email needs to be unique)
	 * @param email - this is provided by the user for authentication (combo with username needs to be unique)
	 * @param password - this is provided by the user for authentication
	 * @return - If the Employee is validated, we will populate a User object and return it, else we will give error message and return null
	 */
	
	Logger log = Logger.getLogger("EmployeeDAOImpl");
	public Users loginEmployee(String username, String email, String password) {
		// TODO Auto-generated method stub
		//*******
		// Fill prepared statement with username and password
				PreparedStatement preparedStmt = null;
				Statement stmt = null;
				Connection conn = null;
				ResultSet preparedResultSet;
				boolean authenticated = false;
				
				
				try {
					conn = DAOUtilities.getConnection();
					String sql = "SELECT\n" + 
							"	crypt(?,ers_password)=ers_password\n" + 
							"    	AS acessed\n" + 
							"    FROM ers_users\n" + 
							"    WHERE (ers_username= ? AND user_email=?);";
					

					// Setup PreparedStatement
					preparedStmt = conn.prepareStatement(sql);
					
					// Add parameters into PreparedStatement
					preparedStmt.setString(1, password);
					preparedStmt.setString(2, username);
					preparedStmt.setString(3, email);
					
					preparedResultSet = preparedStmt.executeQuery();
					try {
						preparedResultSet.next();
						authenticated = preparedResultSet.getBoolean("acessed");
					}
					catch (PSQLException psql){
						log.error("There was no user found with that name and email!");
					}

				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (preparedStmt != null)
							preparedStmt.close();
						if (conn != null)
							conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				if (authenticated == false) {
					log.info("Username or Password was incorrect.");
					// will exit if block and return null
				}else {
					Users authenticatedUser = new Users();
					log.info("You successfully authenticated!");
					try {
						conn = DAOUtilities.getConnection();
						stmt = conn.createStatement();

						String sql = ("SELECT ers_users_id, user_first_name, user_last_name, user_role_id FROM ers_users\n" +
									  "WHERE (ers_username = '" + username +"' AND user_email = '" + email + "');");
	
						ResultSet rs = stmt.executeQuery(sql);
	
						while (rs.next()) {
							log.debug("result set has data - populate authenticatedUser");
							authenticatedUser.setErsUsersId(rs.getInt("ers_users_id"));
							authenticatedUser.setErsUsername(username);
							authenticatedUser.setErsPassword(password);
							authenticatedUser.setUserEmail(email);
							authenticatedUser.setUserFirstName(rs.getString("user_first_name"));
							authenticatedUser.setUserLastName(rs.getString("user_last_name"));
							authenticatedUser.setRole(rs.getInt("user_role_id"));
						}
					}
					catch (SQLException sqle) {
						log.error("SQL Exception thrown");
						sqle.printStackTrace();
					}
					finally {
						try {
							if (stmt != null)
								stmt.close();
							if (conn != null)
								conn.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					log.info("Finished getting an authenticated user...returning him");
					return authenticatedUser;
				}
				
		return null;
	}

	/***
	 * This method will be used to populate a sql query with the Reimbursement object properties and insert the row into the database
	 * @param reimbRecord - the Reimbursement Screen will have populated a Reimbursement object from user input and passed the object into this method
	 * @throws Exception - if record is not saved to database
	 */
	public void submitReimbursement(Reimbursement reimbRecord) throws Exception {
		// TODO Auto-generated method stub
		//Fill prepared statement with values for ers_reimbursement
		PreparedStatement preparedStmt = null;
		Connection conn = null;
		int success = 0;
		
		try {
			conn = DAOUtilities.getConnection();
			Timestamp timestamp = Timestamp.valueOf(reimbRecord.getReimbSubmitted());
			String sql = "INSERT INTO ers_reimbursement (reimb_amount, reimb_submitted, reimb_description, reimb_author, reimb_status_id,reimb_type_id)\n" + 
					"	VALUES (?,'" + timestamp + "',?,?,?,?)";
			// Setup PreparedStatement
			log.debug("My sql to insert my reimbursement is being run now: " + sql);
			preparedStmt = conn.prepareStatement(sql);
			
			// Add parameters into PreparedStatement
			preparedStmt.setDouble(1, reimbRecord.getReimbursementAmount());
			preparedStmt.setString(2, reimbRecord.getReimbDescription());
			preparedStmt.setInt(3, reimbRecord.getReimbAuthor());
			preparedStmt.setInt(4, reimbRecord.getReimbStatusId());
			preparedStmt.setInt(5, reimbRecord.getReimbTypeId());
			
			success = preparedStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStmt != null)
					preparedStmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (success == 0) {
			// then update didn't occur, throw an exception
			log.error("Insert was unsuccessful");
			throw new Exception("Insert reimbursement failed: " + reimbRecord);
			
		}else {
			log.info("Insert of reimbursement should have been successful");
		}
		
		
		
	}

	/***
	 * This method will allow an Employee to view all the tickets for a particular employee, although a Fi Manager may use this also to view tickets on one empl.
	 * @param employeeId - this is the user ID of the employee 
	 * @param boolResolved - if Resolved = true, returns all past tickets (Denied/Approved), else if Resolved=false will return all Pending tickets
	 * @param ...Types - optionally the method will take an array of Types, and the Tickets returned will be filtered by those types
	 * @return List<Reimbursement> - returns a list of reimbursements
	 */
	public List<Reimbursement> viewEmployeeTickets(int employeeId, boolean boolResolved, int ...Types) {
		// TODO Auto-generated method stub
		List<Reimbursement> reimbList = new ArrayList<Reimbursement>();
		
		Connection conn = null;
		Statement stmt = null;
		String sqlBase = "SELECT * FROM ers_reimbursement a\n";
		String sqlComplete = "";

		try {
			conn = DAOUtilities.getConnection();

			stmt = conn.createStatement();
			
			if (Types.length == 0) { //they didn't pass through any Types to filter by, so don't filter by them
				if (boolResolved) { //they only want resolved tickets so filter by employeeId and status_id>1
					sqlComplete =  (sqlBase + "WHERE (a.reimb_author = " + employeeId + ") AND (a.reimb_status_id > 1)");
				}else { 			    //boolResolved is false, so they only want Pending tickets, so filter by status=1
					sqlComplete = (sqlBase + "WHERE (a.reimb_author = " + employeeId + ") AND (a.reimb_status_id = 1)");
				}
			} else {
				if (boolResolved) { //they only want resolved tickets so filter by employeeId and status_id>1
					sqlComplete =  (sqlBase + "WHERE (a.reimb_author = " + employeeId + ") AND (a.reimb_status_id > 1)");
				}else { 			    //boolResolved is false, so they only want Pending tickets, so filter by status=1
					sqlComplete = (sqlBase + "WHERE (a.reimb_author = " + employeeId + ") AND (a.reimb_status_id = 1)");
				}
				switch (Types.length) {  //they also want certain types, so group types having those types
				case 1:
					// They want to filter on one type, build the sql...just one, so no need to Order By
					sqlComplete = (sqlComplete + "AND (a.reimb_type_id =" + Types[0] + ");");
					break;
				case 2:
					// They want to filter on two types, build the sql
					sqlComplete = (sqlComplete + "\nAND ((a.reimb_type_id = " + Types[0] + ") OR (a.reimb_type_id = " + Types[1] + ")) ORDER BY a.reimb_type_id;");
					break;
				case 3:
					// They want to filter on three types, build the sql
					sqlComplete = (sqlComplete + "\nAND ((a.reimb_type_id = " + Types[0] + ")\n" +
								   "OR (a.reimb_type_id = " + Types[1] + ") OR (a.reimb_type_id = " + Types[2] + ")) ORDER BY a.reimb_type_id;");
					break;
				case 4:
					// They want to filter on all four types, build the sql (same as everything, but we will order by type)
					sqlComplete = (sqlComplete + "\nORDER BY a.reimb_type_id;");
					break;
					default:
						System.out.println("Shouldn't get here...too many types passed in to the viewEmployeeTickets method. Defaulting to all tickets.");
						//Build sql for all types
						sqlComplete = (sqlComplete + "\nORDER BY a.reimb_type_id;");
						break;
				}
			}
			log.debug("sqlComplete from viewEmployeeTickets is : " + sqlComplete);
			

			ResultSet rs = stmt.executeQuery(sqlComplete);
			
//			SELECT a.reimb_id "Reimbursement ID", a.reimb_amount "Reimbursement Amount",
//			a.reimb_submitted "Submit Date", a.reimb_resolved "Approval Date", a.reimb_description "Description",
//			c.ers_username "Author", d.ers_username "Approver", e.reimb_status "Status", f.reimb_type "Type"

			while (rs.next()) {
				Reimbursement r = new Reimbursement();
				if(rs.getInt("reimb_author")!=0) {
					r.setAuthor(rs.getInt("reimb_author"));
				}
				if(rs.getInt("reimb_resolver")!=0) {
					r.setResolver(rs.getInt("reimb_resolver"));
				}
				r.setReimbusementId(rs.getInt("reimb_id"));
				r.setReimbursementAmount(rs.getDouble("reimb_amount"));
				r.setReimbSubmitted((rs.getTimestamp("reimb_submitted").toLocalDateTime()));
				if (rs.getTimestamp("reimb_resolved") != null) {
					r.setReimbResolved(rs.getTimestamp("reimb_resolved").toLocalDateTime());
				}
				r.setReimbDescription(rs.getString("reimb_description"));
				//TODO: skipping receipt for now - leaving Null
				r.setReimbAuthor(rs.getInt("reimb_author"));
				r.setReimbResolver(rs.getInt("reimb_resolver"));
				r.setReimbStatusId(rs.getInt("reimb_status_id"));
				r.setReimbTypeId(rs.getInt("reimb_type_id"));
			
				
				log.debug(r);
				
				reimbList.add(r);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return reimbList;
	}
	/***
	 * getUser used to fully populate a User Object, given the user id
	 * @param userID
	 * @return
	 */
	public Users getUser(int userID) {

		Statement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		
		Users referencedUser = new Users();
		
		try {
			conn = DAOUtilities.getConnection();
			stmt = conn.createStatement();

			String sql = ("SELECT ers_username, user_email, user_first_name, user_last_name, user_role_id FROM ers_users\n" +
						  "WHERE ers_users_id = " + userID +";");
			
			log.debug("sql below:");
			log.debug(sql);

			rs = stmt.executeQuery(sql);
			
			log.debug("result set below:");
			log.debug(rs);

			rs.next();
			log.debug("result set has data - populate User");
			referencedUser.setErsUsersId(userID);
			referencedUser.setErsUsername(rs.getString("ers_username"));
			referencedUser.setErsPassword("password"); //inserting dummy value
			referencedUser.setUserEmail(rs.getString("user_email"));
			referencedUser.setUserFirstName(rs.getString("user_first_name"));
			referencedUser.setUserLastName(rs.getString("user_last_name"));
			referencedUser.setRole(rs.getInt("user_role_id"));
			
		}
		catch (SQLException sqle) {
			log.error("SQL Exception thrown");
			sqle.printStackTrace();
			return null;
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		log.info("Finished getting a referenced user...returning him: " + referencedUser);
		return referencedUser;
				
	}
	/***
	 * Populate a Role Object given only a Role ID....useful for populating a Users bean since it will have a Role object but we initially
	 * only have the Role Id from the database
	 * @param userId
	 * @return
	 */
	public UserRoles populateRole(int userId) {
		UserRoles ur = new UserRoles();
		Statement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		
		
		try {
			conn = DAOUtilities.getConnection();
			stmt = conn.createStatement();

			String sql = ("SELECT ers_user_role_id, user_role FROM ers_user_roles, ers_users\n" +
						  "WHERE (ers_users_id = " + userId +" AND ers_user_roles.ers_user_role_id = ers_users.user_role_id);");
			
			log.debug("sql below:");
			log.debug(sql);

			rs = stmt.executeQuery(sql);
			
			log.debug("result set below:");
			log.debug(rs);

			rs.next();
			log.debug("result set has data - populate Role");
			ur.setErsUserRoleid(rs.getInt("ers_user_role_id"));
			ur.setUserRole(rs.getString("user_role"));
		}
		catch (SQLException sqle) {
			log.error("SQL Exception thrown");
			sqle.printStackTrace();
			return null;
		}finally {
		try {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		log.info("Finished getting a role...returning it: " + ur);
		return ur;
	}
	
	/***
	 * I want to delete a reimbursement record, but I want to return the user owner so the client knows whose records to refresh at the page
	 * @param reimbId - a reimbursement ID is needed so we know which record to remove from the database.
	 * @return a User is returned to be written on the response
	 */
	public void deleteRecord(int reimbId) {
		Statement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		
		
		try {
			conn = DAOUtilities.getConnection();
			stmt = conn.createStatement();
			
			//Second Delete the reimbursement record
			log.debug("The reimbId (reimbursementId); we are trying to delete a record with is : "+ reimbId);
			String sql = ("DELETE FROM ers_reimbursement WHERE reimb_id = " + reimbId +";");
			rs = stmt.executeQuery(sql);
			
		}
		catch (SQLException sqle) {
			log.error("SQL Exception thrown");
			sqle.printStackTrace();
			return;
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		log.info("Finished deleting a reimbursement record...returning");
		return;

	}
	
}
