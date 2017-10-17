package com.capital.one.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.capital.one.daos.DAOUtilities;
import com.capital.one.daos.EmployeeDAO;
import com.capital.one.daos.FinanceManagerDao;
import com.capital.one.datamodelbeans.Reimbursement;
import com.capital.one.datamodelbeans.Users;

public class FinanceManagerMenuScreen {

    public static void run(Users financeManager) {
        Scanner scan = new Scanner(System.in);
        FinanceManagerDao finManDao = DAOUtilities.getFinanceManagerDao();
        EmployeeDAO empDao = DAOUtilities.getEmployeeDao();
        List<Reimbursement> reimbursementsResultList = new ArrayList<Reimbursement>();
        List<Reimbursement> filteredResultList = new ArrayList<Reimbursement>();
        int choice = 0;
        String types = "";
        int lookupId = 0;
        boolean pastTickets = false;
        String[] parts;
        int[] filterTypes;

        while (choice == 0) {
            System.out.println(
                    "Hello, " + financeManager.getUserFirstName() + "!  Which of the following would you like to do:");
            System.out.println("1) View resolved Reimbursements (Approved/Denied) for a specific employee");
            System.out.println("2) View pending Reimbursement tickets for a specific employee");
            System.out.println("3) Submit a new reimbursement");
            System.out.println("4) View all resolved Reimbursements (filtered by type)");
            System.out.println("5) View all pending Reimbursements (filtered by type)");
            System.out.println("6) Approve or Deny a Reimbursement request");
            System.out.println("7) Logout/Quit");

            choice = scan.nextInt();

            switch (choice) {
                case 1:
                    // call the resolved reimbursements method and send the results to a Display Reimbursements Screen
                    // .run()
                    System.out.println("What is the Employee ID that you would like to see tickets for?");
                    lookupId = scan.nextInt();
                    System.out.println(
                            "Which types are you interested in?  (You can type '0' for all, or '1', '1 2', '1 2 3', '2', '2 3', '3', '1 3', '4', '1 4' etc.)");
                    System.out.println("The types are 1) Lodging, 2) Travel, 3) Food, and 4) Other");
                    scan.nextLine();
                    types = scan.nextLine();
                    if ("0".equals(types)) {
                        // System.out.println("employee.getErsUsersId() returns " + financeManager.getErsUsersId());
                        reimbursementsResultList = empDao.viewEmployeeTickets(lookupId, true);
                    }
                    else {
                        parts = types.split(" ");
                        filterTypes = new int[parts.length];
                        for (int n = 0; n < parts.length; n++) {
                            try {
                                filterTypes[n] = Integer.parseInt(parts[n]);
                            }
                            catch (Exception e) {
                                System.out.println("you did not give proper input.  Try again.");
                                choice = 0;
                                continue;
                            }
                        }
                        reimbursementsResultList = empDao.viewEmployeeTickets(lookupId, true, filterTypes);
                    }
                    // send the reimbursementsResultList to a Display Reimbursements Screen.
                    DisplayReimbursementsScreen.run(reimbursementsResultList);
                    choice = 0; // if they come back here they will be prompted with the menu again
                    continue;
                case 2:
                    // call the pending reimbursement method and send the results to a Display Reimbursements Screen
                    // .run()
                    System.out.println("What is the Employee ID that you would like to see tickets for?");
                    lookupId = scan.nextInt();
                    System.out.println(
                            "Which types are you interested in?  (You can type '0' for all, or '1', '1 2', '1 2 3', '2', '2 3', '3', '1 3', '4', '1 4' etc.)");
                    System.out.println("The types are 1) Lodging, 2) Travel, 3) Food, and 4) Other");
                    scan.nextLine();
                    types = scan.nextLine();
                    if ("0".equals(types)) {
                        // System.out.println("employee.getErsUsersId() returns " + employee.getErsUsersId());
                        reimbursementsResultList = empDao.viewEmployeeTickets(lookupId, false);

                    }
                    else {
                        parts = types.split(" "); // parsing the arguments to see what types they want to filter by
                        filterTypes = new int[parts.length];
                        for (int n = 0; n < parts.length; n++) {
                            try {
                                filterTypes[n] = Integer.parseInt(parts[n]);
                            }
                            catch (Exception e) {
                                System.out.println("you did not give proper input.  Try again.");
                                choice = 0;
                                continue;
                            }
                        }
                        reimbursementsResultList = empDao.viewEmployeeTickets(lookupId, false, filterTypes);

                    }
                    // send the reimbursementsResultList to a Display Reimbursements Screen.
                    DisplayReimbursementsScreen.run(reimbursementsResultList);
                    choice = 0; // if they come back here they will be prompted with the menu again
                    continue;
                case 3:
                    // send to the Submit Reimbursement Screen .run() method, passing the User
                    SubmitReimbursementScreen.run(financeManager);
                    choice = 0; // if they come back here they will be prompted with the menu again
                    continue;
                case 4:
                    // System.out.println("4) View all resolved Reimbursements (filtered by type)");
                    System.out.println(
                            "Which types are you interested in?  (You can type '0' for all, or '1', '1 2', '1 2 3', '2', '2 3', '3', '1 3', '4', '1 4' etc.)");
                    System.out.println("The types are 1) Lodging, 2) Travel, 3) Food, and 4) Other");
                    scan.nextLine();
                    types = scan.nextLine();
                    filteredResultList = new ArrayList<Reimbursement>();
                    reimbursementsResultList = finManDao.getAllEmployeeReimbursement();

                    if ("0".equals(types)) {
                        // System.out.println("employee.getErsUsersId() returns " + employee.getErsUsersId());
                        // reimbursementsResultList = empDao.viewEmployeeTickets(lookupId, false);
                        // TODO: Change list by sorting
                        filteredResultList = reimbursementsResultList.stream()
                                .filter((element) -> !("Pending".equals(element.getStatus().getReimbStatus())))
                                .sorted((f1, f2) -> f1.getType().getReimbType().compareTo(f2.getType().getReimbType()))
                                .collect(Collectors.toList());
                        ;
                    }
                    else {
                        parts = types.split(" "); // parsing the arguments to see what types they want to filter by
                        filterTypes = new int[parts.length];
                        for (int n = 0; n < parts.length; n++) {
                            try {
                                filterTypes[n] = Integer.parseInt(parts[n]);
                            }
                            catch (Exception e) {
                                System.out.println("you did not give proper input.  Try again.");
                                choice = 0;
                                continue;
                            }
                        }
                        // reimbursementsResultList = empDao.viewEmployeeTickets(lookupId, false, Types);
                        // TODO: change list by only including the requested Types and sorting.
                        final int[] myTypes = filterTypes;
                        filteredResultList = reimbursementsResultList.stream()
                                .filter((element) ->
                                    {
                                        if (!("Pending".equals(element.getStatus().getReimbStatus()))) {
                                            for (int t : myTypes) {
                                                return (element.getReimbTypeId() == myTypes[t]);
                                            }
                                        }
                                        return false;
                                    })
                                .sorted((f1, f2) -> f1.getType().getReimbType().compareTo(f2.getType().getReimbType()))
                                .collect(Collectors.toList());
                    }
                    // send the reimbursementsResultList to a Display Reimbursements Screen.
                    DisplayReimbursementsScreen.run(filteredResultList);
                    choice = 0;
                    continue;
                case 5:
                    // System.out.println("5) View all pending Reimbursements (filtered by type)");
                    System.out.println(
                            "Which types are you interested in?  (You can type '0' for all, or '1', '1 2', '1 2 3', '2', '2 3', '3', '1 3', '4', '1 4' etc.)");
                    System.out.println("The types are 1) Lodging, 2) Travel, 3) Food, and 4) Other");
                    scan.nextLine();
                    types = scan.nextLine();
                    filteredResultList = new ArrayList<Reimbursement>();
                    reimbursementsResultList = finManDao.getAllEmployeeReimbursement();

                    if ("0".equals(types)) {
                        // System.out.println("employee.getErsUsersId() returns " + employee.getErsUsersId());
                        // reimbursementsResultList = empDao.viewEmployeeTickets(lookupId, false);
                        // TODO: Change list by sorting
                        filteredResultList = reimbursementsResultList.stream()
                                .filter((element) -> ("Pending".equals(element.getStatus().getReimbStatus())))
                                .sorted((f1, f2) -> f1.getType().getReimbType().compareTo(f2.getType().getReimbType()))
                                .collect(Collectors.toList());
                        ;
                    }
                    else {
                        parts = types.split(" "); // parsing the arguments to see what types they want to filter by
                        filterTypes = new int[parts.length];
                        for (int n = 0; n < parts.length; n++) {
                            try {
                                filterTypes[n] = Integer.parseInt(parts[n]);
                            }
                            catch (Exception e) {
                                System.out.println("you did not give proper input.  Try again.");
                                choice = 0;
                                continue;
                            }
                        }
                        // reimbursementsResultList = empDao.viewEmployeeTickets(lookupId, false, Types);
                        // TODO: change list by only including the requested Types and sorting.
                        final int[] myTypes = filterTypes;
                        filteredResultList = reimbursementsResultList.stream()
                                .filter((element) ->
                                    {
                                        if (("Pending".equals(element.getStatus().getReimbStatus()))) {
                                            for (int t : myTypes) {
                                                return (element.getReimbTypeId() == myTypes[t]);
                                            }
                                        }
                                        return false;
                                    })
                                .sorted((f1, f2) -> f1.getType().getReimbType().compareTo(f2.getType().getReimbType()))
                                .collect(Collectors.toList());
                    }
                    // send the reimbursementsResultList to a Display Reimbursements Screen.
                    DisplayReimbursementsScreen.run(filteredResultList);

                    choice = 0;
                    continue;
                case 6:
                    // System.out.println("6) Approve or Deny a Reimbursement request");
                    String statusChange = "undecided";

                    while ("undecided".equals(statusChange)) {
                        System.out
                                .println("What is the ID of the Reimbursement that you would like to approve or deny?");
                        choice = scan.nextInt();
                        System.out.println("Type 'Approve' or 'Deny'");
                        scan.nextLine();
                        statusChange = scan.nextLine();
                        switch (statusChange) {
                            case "Approve":
                                // call the approve method with the choice for ID
                                // finManDao.approveRequest(choice);
                                break;
                            case "Deny":
                                // call the deny method with the choice for ID
                                // finManDao.denyRequest(choice);
                                break;
                            default:
                                System.out.println("Unrecognized choice.  Try again.");
                                statusChange = "undecided";
                                continue;
                        }
                    }

                    choice = 0;
                    continue;
                case 7:
                    return; // this will send them back to the login page
                default:
                    System.out.println("That was not a valid entry.  Please try again.");
                    choice = 0;
                    continue;
            }

        }
        // scan.close();
    }

}
