package com.capital.one.daos;

import java.sql.SQLException;
import java.util.List;

import com.capital.one.datamodelbeans.Reimbursement;
import com.capital.one.datamodelbeans.UserRoles;
import com.capital.one.datamodelbeans.Users;


/**
 * Interface that will be implemented by the EmployeeDAOimpl class.  It defines the methods an employee would call to work with data on the database.
 * 
 * @author David Crites
 *
 */
public interface EmployeeDAO {
	
	/***
	 * This method will be used for validating employee login and determining which screen to go to next, EmployeeMenu or FiManagerMenu
	 * @param username - this is provided by the user for authentication (combo with email needs to be unique)
	 * @param email - this is provided by the user for authentication (combo with username needs to be unique)
	 * @param password - this is provided by the user for authentication
	 * @return - If the Employee is validated, we will populate a User object and return it, else we will give error message and return null
	 */
	Users loginEmployee(String username, String email, String password);
	
	/***
	 * This method will be used to populate a sql query with the Reimbursement object properties and insert the row into the database
	 * @param reimbRecord - the Reimbursement Screen will have populated a Reimbursement object from user input and passed the object into this method
	 * @throws SQLException - if the insert into the database fails, an exception will be thrown, which should be caught and handled.
	 */
	void submitReimbursement(Reimbursement reimbRecord) throws Exception;
	
	/***
	 * This method will allow an Employee to view all the tickets for a particular employee, although a Fi Manager may use this also to view tickets on one empl.
	 * @param employeeId - this is the user ID of the employee 
	 * @param boolResolved - if Resolved = true, returns all past tickets (Denied/Approved), else if Resolved=false will return all Pending tickets
	 * @param ...Types - optionally the method will take an array of int Types (type_ids), and the Tickets returned will be filtered by those types
	 * @return
	 */
	List<Reimbursement> viewEmployeeTickets(int employeeId, boolean boolResolved, int ...Types);
	
	/***
	 * This method will populate a User object given an ID...useful for the Reimbursement bean, which has an Author and Resolver object that are users
	 * since we initially only have stored the ID in the database object.
	 * @param userID
	 * @return
	 */
	public Users getUser(int userID);

	/***
	 * Populate a Role Object given only a Role ID....useful for populating a Users bean since it will have a Role object but we initially
	 * only have the Role Id from the database
	 * @param userId
	 * @return
	 */
	public UserRoles populateRole(int userId);
	
	/***
	 * I want to delete a reimbursement record
	 * @param reimbId - a reimbursement ID is needed so we know which record to remove from the database.
	 * 
	 */
	public void deleteRecord(int reimbId);
}
