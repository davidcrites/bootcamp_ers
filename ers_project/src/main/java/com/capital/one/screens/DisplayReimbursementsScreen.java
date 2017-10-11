package com.capital.one.screens;

import java.util.List;
import java.util.Scanner;

import com.capital.one.datamodelbeans.Reimbursement;

public class DisplayReimbursementsScreen {
	
	DisplayReimbursementsScreen(){
		super();
	}
	
	public static void run(List<Reimbursement> listToDisplay) {
		Scanner scan = new Scanner(System.in);
		
		for (Reimbursement r: listToDisplay) {
			System.out.println(r);
		}
		System.out.println("Enter any key to return.");
		scan.nextLine();
		//scan.close();
		return;
	}
	

}
