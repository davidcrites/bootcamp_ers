package com.capital.one.daos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.capital.one.datamodelbeans.Reimbursement;
import com.capital.one.datamodelbeans.Users;


public class EmployeeDAOImpl implements EmployeeDAO{

	/***
	 * This method will be used for validating employee login and determining which screen to go to next, EmployeeMenu or FiManagerMenu
	 * @param username - this is provided by the user for authentication
	 * @param password - this is provided by the user for authentication
	 * @return - If the Employee is validated, we will populate a User object and return it, else we will give error message and return null
	 */
	public Users loginEmployee(String username, String password) {
		// TODO Auto-generated method stub
		//*******
		// Fill prepared statement with username and password
				PreparedStatement preparedStmt = null;
				Statement stmt = null;
				Connection conn = null;
				Connection conn2 = null;
				ResultSet preparedResultSet;
				boolean authenticated = false;
				
				try {
					conn = DAOUtilities.getConnection();
					String sql = "SELECT\n" + 
							"	crypt(?,ers_password)=ers_password\n" + 
							"    	AS acessed\n" + 
							"    FROM ers_users\n" + 
							"    where ers_username= ?;";
					

					// Setup PreparedStatement
					preparedStmt = conn.prepareStatement(sql);
					
					// Add parameters into PreparedStatement
					preparedStmt.setString(1, password);
					preparedStmt.setString(2, username);
					
					preparedResultSet = preparedStmt.executeQuery();
					preparedResultSet.next();
					authenticated = preparedResultSet.getBoolean("acessed");

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
					// then update didn't occur, throw an exception
					System.out.println("Username or Password was incorrect.");
					//System.out.println("authenticated = " + authenticated);
				}else {
					Users authenticatedUser = new Users();
					System.out.println("You successfully authenticated!");
					//System.out.println("authenticated = " + authenticated);
					try {
						conn2 = DAOUtilities.getConnection();
						stmt = conn2.createStatement();

						String sql = ("SELECT * FROM ers_users WHERE ers_username = '" + username +"'");
						//System.out.println(sql);
	
						ResultSet rs = stmt.executeQuery(sql);
	
						while (rs.next()) {
							//System.out.println("result set has data - populate authenticatedUser");
							authenticatedUser.setErsUsersId(rs.getInt("ers_users_id"));
							authenticatedUser.setErsUsername(username);
							authenticatedUser.setErsPassword(password);
							authenticatedUser.setUserFirstName(rs.getString("user_first_name"));
							authenticatedUser.setUserLastName(rs.getString("user_last_name"));
							authenticatedUser.setUserEmail(rs.getString("user_email"));
							authenticatedUser.setUserRoleId(rs.getInt("user_role_id"));
							authenticatedUser.setRole(rs.getInt("user_role_id"));
						}
					}
					catch (SQLException sqle) {
						System.out.println("SQL Exception thrown");
					}
					//System.out.println("Finished getting an authenticated user...returning him");
					//System.out.println(authenticatedUser);
					return authenticatedUser;
				}
				
				
				
		
		//*******
		return null;
	}

	/***
	 * This method will be used to populate a sql query with the Reimbursement object properties and insert the row into the database
	 * @param reimbRecord - the Reimbursement Screen will have populated a Reimbursement object from user input and passed the object into this method
	 * @throws SQLException - if the insert into the database fails, an exception will be thrown, which should be caught and handled.
	 */
	public void submitReimbursement(Reimbursement reimbRecord) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/***
	 * This method will allow an Employee to view all the tickets for a particular employee, although a Fi Manager may use this also to view tickets on one empl.
	 * @param employeeId - this is the user ID of the employee 
	 * @param boolResolved - if Resolved = true, returns all past tickets (Denied/Approved), else if Resolved=false will return all Pending tickets
	 * @param ...Types - optionally the method will take an array of Types, and the Tickets returned will be filtered by those types
	 * @return
	 */
	public List<Reimbursement> viewEmployeeTickets(int employeeId, boolean boolResolved, String... Types) {
		// TODO Auto-generated method stub
		return null;
	}

}
