package com.capital.one.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.capital.one.daos.DAOUtilities;
import com.capital.one.daos.EmployeeDAO;
import com.capital.one.datamodelbeans.Reimbursement;
import com.capital.one.datamodelbeans.Users;

public class EmployeeMenuScreen {
	
	EmployeeMenuScreen(){
		super();
	}
	
	public static void run(Users employee) {
		Scanner scan = new Scanner(System.in);
		EmployeeDAO dao = DAOUtilities.getEmployeeDao();
		List<Reimbursement> reimbursementsResultList = new ArrayList<Reimbursement>();
		int choice = 0;
		String types = "";
		
		while (choice ==0) {
			System.out.println("Hello, " + employee.getUserFirstName() + "!  Which of the following would you like to do:");
			System.out.println("1) View your resolved Reimbursements (Approved/Denied)");
			System.out.println("2) View your pending Reimbursement tickets ");
			System.out.println("3) Submit a new reimbursement");
			System.out.println("4) Logout/Quit");
			
			choice = scan.nextInt();

			switch(choice) {
			case 1:
				// call the resolved reimbursements method and send the results to a Display Reimbursements Screen .run()
				System.out.println("Which types are you interested in?  (You can type '0' for all, or '1', '1 2', '1 2 3', '2', '2 3', '3', '1 3', '4', '1 4' etc.)");
				System.out.println("The types are 1) Lodging, 2) Travel, 3) Food, and 4) Other");
				scan.nextLine();
				types = scan.nextLine();
				if ("0".equals(types)) {
					System.out.println("employee.getErsUsersId() returns " + employee.getErsUsersId());
					reimbursementsResultList = dao.viewEmployeeTickets(employee.getErsUsersId(), true);
				}else {
					String[] parts = types.split(" ");
					int[] Types = new int[parts.length];
					for(int n = 0; n < parts.length; n++) {
					   try {
					   Types[n] = Integer.parseInt(parts[n]);
					   }
					   catch (Exception e) {
						   System.out.println("you did not give proper input.  Try again.");
						   choice = 0;
						   continue;
					   }
					}
					reimbursementsResultList = dao.viewEmployeeTickets(employee.getErsUsersId(), true, Types);
				}
				// send the reimbursementsResultList to a Display Reimbursements Screen.
				DisplayReimbursementsScreen.run(reimbursementsResultList);
				choice = 0; //if they come back here they will be prompted with the menu again
				continue;
			case 2:
				// call the pending reimbursement method and send the results to a Display Reimbursements Screen .run()
				System.out.println("Which types are you interested in?  (You can type '0' for all, or '1', '1 2', '1 2 3', '2', '2 3', '3', '1 3', '4', '1 4' etc.)");
				System.out.println("The types are 1) Lodging, 2) Travel, 3) Food, and 4) Other");
				scan.nextLine();
				types = scan.nextLine();
				if ("0".equals(types)) {
					//System.out.println("employee.getErsUsersId() returns " + employee.getErsUsersId());
					reimbursementsResultList = dao.viewEmployeeTickets(employee.getErsUsersId(), false);
				}else {
					String[] parts = types.split(" "); //parsing the arguments to see what types they want to filter by
					int[] Types = new int[parts.length];
					for(int n = 0; n < parts.length; n++) {
					   try {
					   Types[n] = Integer.parseInt(parts[n]);
					   }
					   catch (Exception e) {
						   System.out.println("you did not give proper input.  Try again.");
						   choice = 0;
						   continue;
					   }
					}
					reimbursementsResultList = dao.viewEmployeeTickets(employee.getErsUsersId(), false, Types);
				}
				// send the reimbursementsResultList to a Display Reimbursements Screen.
				DisplayReimbursementsScreen.run(reimbursementsResultList);
				choice = 0; //if they come back here they will be prompted with the menu again
				continue;
			case 3:
				// send to the Submit Reimbursement Screen .run() method, passing the User
				SubmitReimbursementScreen.run(employee);
				choice = 0; //if they come back here they will be prompted with the menu again
				continue;
			case 4:
				return; //this will send them back to the login page
			default:
				System.out.println("That was not a valid entry.  Please try again.");
				choice = 0;
				continue;
			}
		
		}
		//scan.close();
	}

}
