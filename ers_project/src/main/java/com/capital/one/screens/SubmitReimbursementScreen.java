package com.capital.one.screens;

import java.time.LocalDateTime;
import java.util.Scanner;

import com.capital.one.daos.DAOUtilities;
import com.capital.one.daos.EmployeeDAO;
import com.capital.one.datamodelbeans.Reimbursement;
import com.capital.one.datamodelbeans.Users;

public class SubmitReimbursementScreen {
	
	SubmitReimbursementScreen(){
		super();
	}
	
	public static void run(Users reimbursementSubmitter) {
		
		Scanner scan = new Scanner(System.in);
		Reimbursement newReimbursement = new Reimbursement();
		String reimbType = "";
		boolean validType = false;
		boolean more = true;
		String moreString="";
		EmployeeDAO dao = DAOUtilities.getEmployeeDao();
		
		while (more) {
			System.out.println("Please enter the amount (ex. '20.00' for $20):");
			newReimbursement.setReimbursementAmount(scan.nextDouble());
			System.out.println("Please enter a description of the expense:");
			scan.nextLine();
			newReimbursement.setReimbDescription(scan.nextLine());
			while (!validType) {
				System.out.println("Please enter what type of Expense or 'Cancel' (valid words are 'Lodging', 'Travel', 'Food', or 'Other'):");
				reimbType = scan.nextLine();
				if ("Lodging".equals(reimbType) || "Travel".equals(reimbType) || "Food".equals(reimbType) || "Other".equals(reimbType) || "Cancel".equals(reimbType)) {
					validType = true;
					switch (reimbType) {
					case "Lodging":
						newReimbursement.setReimbTypeId(1);
						continue;
					case "Travel":
						newReimbursement.setReimbTypeId(2);
						continue;
					case "Food":
						newReimbursement.setReimbTypeId(3);
						continue;
					case "Other":
						newReimbursement.setReimbTypeId(4);
						continue;
					case "Cancel":
						return;
						
					}
				}
			}
			//**** Filling the fields not already filled
			//Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now()); //don't need because storing now as LocalDateTime.
			newReimbursement.setReimbSubmitted(LocalDateTime.now());
			newReimbursement.setReimbAuthor(reimbursementSubmitter.getErsUsersId());
			newReimbursement.setReimbStatusId(1);
			
			//Call dao method to submit the request
//			try {
//				dao.submitReimbursement(req,newReimbursement);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println("Would you like to enter another reimbursment? (Y or N):");
//			moreString = scan.nextLine();
//			if ("Y".equals(moreString) || "YES".equals(moreString) || "yes".equals(moreString)) {
//				more=true;
//			}
//			else {
//				more = false;
//			}
		}
		System.out.println("Enter any key to return");
		scan.nextLine();
		
		return;
	}

}
