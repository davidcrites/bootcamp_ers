package com.capital.one.services;

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

    public void createNewReimbursement(HttpServletRequest req) {
        //List<Reimbursement> displayList = new ArrayList<Reimbursement>();
        Users tempUser = new Users();
        Reimbursement newReimbursement = new Reimbursement();
        tempUser = (Users) req.getSession().getAttribute("currentUser");
        newReimbursement.setReimbDescription((String) req.getSession().getAttribute("newDescription"));
        newReimbursement.setReimbursementAmount((Double) req.getSession().getAttribute("newAmount"));
        newReimbursement.setReimbTypeId((int) req.getSession().getAttribute("newType"));
        
        if (tempUser.getErsUsersId() == ((int) req.getSession().getAttribute("newId"))) {
        		log.info("createNewReimbursement service being used for Self");
        		tempUser = empDao.getUser((int) req.getSession().getAttribute("newId"));
        		newReimbursement.setAuthor((int) req.getSession().getAttribute("newId"));
        }else {
        		log.info("createNewReimbursement service being used for Other");
        		newReimbursement.setAuthor(tempUser.getErsUsersId());
        }
        try {
        		log.debug("My newRembursement I should be submitting now is : " + newReimbursement);
			empDao.submitReimbursement(newReimbursement);
		} catch (Exception e) {
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