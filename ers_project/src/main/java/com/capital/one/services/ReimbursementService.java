package com.capital.one.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.capital.one.daos.DAOUtilities;
import com.capital.one.daos.EmployeeDAO;
import com.capital.one.daos.FinanceManagerDao;
import com.capital.one.datamodelbeans.Reimbursement;
import com.capital.one.datamodelbeans.UserRoles;
import com.capital.one.datamodelbeans.Users;

public class ReimbursementService {

    EmployeeDAO empDao = DAOUtilities.getEmployeeDao();

    FinanceManagerDao finManDao = DAOUtilities.getFinanceManagerDao();

    Logger log = Logger.getLogger("ReimbursementService");

    Users currentUser = new Users();

    Reimbursement tempReimbursement = new Reimbursement();

    public void populateRole(HttpServletRequest req) {
        UserRoles currentRole = new UserRoles();
        Users u = new Users();
        int userId;
        u = ((Users) req.getSession().getAttribute("currentUser"));
        userId = u.getErsUsersId();
        log.debug("The userId we are getting from currentUser's getErsUsersId is: " + userId);
        currentRole = empDao.populateRole(userId);
        log.debug("The ROLE that we just populated with populateRole is" + currentRole);
        req.getSession().setAttribute("currentRole", currentRole);
    }

    public void myPendingReimbursements(HttpServletRequest req, HttpServletResponse resp) {
        List<Reimbursement> displayList = new ArrayList<Reimbursement>();
        currentUser = (Users) req.getSession().getAttribute("currentUser");
        // false parameter in below call gets pending tickets
        displayList = empDao.viewEmployeeTickets(currentUser.getErsUsersId(), false);
        req.getSession().setAttribute("myPending", displayList);
        for (int i = 0; i < displayList.size(); i++) {
            log.trace(displayList.get(i));
        }
    }

    public void otherPendingReimbursements(HttpServletRequest req, int otherId) {
        List<Reimbursement> displayList = new ArrayList<Reimbursement>();

        // false parameter in below call gets pending tickets
        displayList = empDao.viewEmployeeTickets(otherId, false);
        req.getSession().setAttribute("otherPending", displayList);
        for (int i = 0; i < displayList.size(); i++) {
            log.trace(displayList.get(i));
        }
    }

    public void myPastReimbursements(HttpServletRequest req, HttpServletResponse resp) {
        List<Reimbursement> displayList = new ArrayList<Reimbursement>();
        currentUser = (Users) req.getSession().getAttribute("currentUser");
        // true paramater in below call gets past tickets
        displayList = empDao.viewEmployeeTickets(currentUser.getErsUsersId(), true);
        req.getSession().setAttribute("myPast", displayList);
        for (int i = 0; i < displayList.size(); i++) {
            log.trace(displayList.get(i));
        }
    }

    public void deleteReimbursement(HttpServletRequest req, int reimbId) {
    		empDao.deleteRecord(reimbId);
    }
    public boolean approveReimbursement(HttpServletRequest req, int reimbId) {
    	    boolean success = false;
    	    Users tempUser = new Users();
    	    tempUser = (Users) req.getSession().getAttribute("currentUser");
    	    int resolverId = tempUser.getErsUsersId();
        success = finManDao.approveRequest(reimbId, resolverId);
        if (!success) {
        		log.error("Unable to Approve Record.");
        }
        return success;
    }
    public boolean denyReimbursement(HttpServletRequest req, int reimbId) {
	    boolean success = false; 
	    Users tempUser = new Users();
	    tempUser = (Users) req.getSession().getAttribute("currentUser");
	    int resolverId = tempUser.getErsUsersId();
	    success = finManDao.denyRequest(reimbId, resolverId);
	    if (!success) {
	    		log.error("Unable to Approve Record.");	
	    }
	    return success;
    }

    public void createNewReimbursement(HttpServletRequest req) {
        // List<Reimbursement> displayList = new ArrayList<Reimbursement>();
        Users tempUser = new Users();
        Reimbursement newReimbursement = new Reimbursement();
        int typeId = 0;
        tempUser = (Users) req.getSession().getAttribute("currentUser");
        newReimbursement.setReimbDescription((String) req.getSession().getAttribute("newDescription"));
        newReimbursement.setReimbursementAmount(Double.valueOf((String) req.getSession().getAttribute("newAmount")));
        typeId = DAOUtilities.getReimbursementTypeId((String) req.getSession().getAttribute("newType"));
        newReimbursement.setReimbTypeId(typeId);
        newReimbursement.setReimbStatusId(1); // new Reimbursements are by default Pending, status ID = 1
        newReimbursement.setReimbSubmitted(LocalDateTime.now());

        if (tempUser.getErsUsersId() == (Integer.valueOf((String) req.getSession().getAttribute("newId")))) {
            log.info("createNewReimbursement service being used for Self");
            newReimbursement.setAuthor(tempUser.getErsUsersId());
            newReimbursement.setReimbAuthor(tempUser.getErsUsersId());

        }
        else {
            log.info("createNewReimbursement service being used for Other");
            int tempId = Integer.valueOf((String) req.getSession().getAttribute("newId"));
            newReimbursement.setAuthor(tempId);
            newReimbursement.setReimbAuthor(tempId);
        }
        try {
            log.debug("My newRembursement I should be submitting now is : " + newReimbursement);
            empDao.submitReimbursement(newReimbursement);
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void myDeletedRecords(HttpServletRequest req, HttpServletResponse resp) {
        List<Reimbursement> displayList = new ArrayList<Reimbursement>();
        displayList = empDao.viewEmployeeTickets(currentUser.getErsUsersId(), true).stream()
                .filter((element) ->
                    {
                        return ("Denied".equals(element.getStatus().getReimbStatus()));
                    })
                .collect(Collectors.toList());
        req.getSession().setAttribute("allPast", displayList);
        for (int i = 0; i < displayList.size(); i++) {
            log.trace(displayList.get(i));

        }
    }

    public void allPendingReimbursements(HttpServletRequest req, HttpServletResponse resp) {
        List<Reimbursement> displayList = new ArrayList<Reimbursement>();
        displayList = finManDao.getAllEmployeeReimbursement().stream()
                .filter((element) -> "Pending".equals(element.getStatus().getReimbStatus()))
                .collect(Collectors.toList());
        req.getSession().setAttribute("allPending", displayList);
        for (int i = 0; i < displayList.size(); i++) {
            log.trace(displayList.get(i));
        }
    }

    public void allPastReimbursements(HttpServletRequest req, HttpServletResponse resp) {
        List<Reimbursement> displayList = new ArrayList<Reimbursement>();
        log.trace("In rs.allPastReimbursements");
        displayList = finManDao.getAllEmployeeReimbursement().stream()
                .filter((element) ->
                    {
                        return ("Approved".equals(element.getStatus().getReimbStatus()) ||
                                "Denied".equals(element.getStatus().getReimbStatus()));
                    })
                .collect(Collectors.toList());
        req.getSession().setAttribute("allPast", displayList);
        log.trace("Geting ready to display the filtered displayList");
        for (int i = 0; i < displayList.size(); i++) {
            log.trace(displayList.get(i));
        }
    }

    public void searchReimbursements(HttpServletRequest req, HttpServletResponse resp) {
        List<Reimbursement> displayList = new ArrayList<Reimbursement>();
        log.trace("In rs.searchReimbursements");
        String[] searchParam;
        searchParam = (String[]) req.getSession().getAttribute("searchParam");
        // searchType, searchStatus
        log.debug("my search parameters are" + searchParam[0] + ", " + searchParam[1]);
        if ("All".equals(searchParam[0]) && !("All".equals(searchParam[1]))) {
            // only need to search by 2nd parameter status
        	    log.info("filtering by search status of " + searchParam[1]);
            displayList = finManDao.getAllEmployeeReimbursement().stream()
                    .filter((element) ->
                        {
                            return (searchParam[1].equals(element.getStatus().getReimbStatus()));
                        })
                    .collect(Collectors.toList());
        }
        else if (!("All".equals(searchParam[0])) && ("All".equals(searchParam[1]))) {
            // only need to search by 1st parameter type
        	    log.info("filtering by search type of " + searchParam[0]);
            displayList = finManDao.getAllEmployeeReimbursement().stream()
                    .filter((element) ->
                        {
                            return (searchParam[0].equals(element.getType().getReimbType()));
                        })
                    .collect(Collectors.toList());
        }
        else if (!("All".equals(searchParam[0])) && !("All".equals(searchParam[1]))) {
            // need to search by type and status
           	log.info("filtering by search status of " + searchParam[1] + "and search type of " + searchParam[0]);
            displayList = finManDao.getAllEmployeeReimbursement().stream()
                    .filter((element) ->
                        {
                            return (searchParam[0].equals(element.getType().getReimbType()) &&
                                    searchParam[1].equals(element.getStatus().getReimbStatus()));
                        })
                    .collect(Collectors.toList());
        }
        else {
            // no filter return all
        	    log.info("not applying any filtering because the search params were " + searchParam[1] + " " + searchParam[0]);
            displayList = finManDao.getAllEmployeeReimbursement();
        }

        req.getSession().setAttribute("searchResults", displayList);
        log.trace("Getting ready to display the filtered displayList");
        for (int i = 0; i < displayList.size(); i++) {
            log.trace(displayList.get(i));
        }
    }

    public void allReimbursementsFilterByPending(HttpServletRequest req, HttpServletResponse resp) {
        List<Reimbursement> displayList = new ArrayList<Reimbursement>();
        displayList = finManDao.getAllEmployeeReimbursementOrderByPending().stream()
                .filter((element) ->
                    {
                        return ("Pending".equals(element.getStatus().getReimbStatus()) ||
                                "Approved".equals(element.getStatus().getReimbStatus()) ||
                                "Denied".equals(element.getStatus().getReimbStatus()));
                    })
                .collect(Collectors.toList());
        req.getSession().setAttribute("allPending", displayList);
        req.getSession().setAttribute("allPast", displayList);
        for (int i = 0; i < displayList.size(); i++) {
            log.trace(displayList.get(i));

        }
    }

}