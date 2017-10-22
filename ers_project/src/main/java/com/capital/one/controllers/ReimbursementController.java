package com.capital.one.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.capital.one.daos.DAOUtilities;
import com.capital.one.daos.EmployeeDAO;
import com.capital.one.datamodelbeans.Reimbursement;
import com.capital.one.datamodelbeans.Users;
import com.capital.one.services.ReimbursementService;

public class ReimbursementController {

    private Logger log = Logger.getLogger("ReimbursementController");

    private ReimbursementService rs = new ReimbursementService();

    Users myUser = new Users();

    Reimbursement tempReimbursement = new Reimbursement();

    EmployeeDAO empDao = DAOUtilities.getEmployeeDao();

    public void processGetRequests(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("ReimbursementController processing get request");
        // I'm not populating a list here like he was...could do later but for now, nothing I want to do on get request.

        String requestUrl = req.getRequestURI().substring(req.getContextPath().length());
        log.debug("requestUrl = " + requestUrl);
        String shortUrl = requestUrl;

        if (requestUrl.contains("/static/reimbursements/pendingOther/")) {
            shortUrl = requestUrl.substring(0, 35);
        }
        else if (requestUrl.contains("/static/reimbursements/getOtherUser")) {
            shortUrl = requestUrl.substring(0, 35);
        }
        else if (requestUrl.contains("/static/reimbursements/deleteRecord")) {
            shortUrl = requestUrl.substring(0, 35);
        }
        log.debug("shortUrl = " + shortUrl);

        switch (shortUrl) {

            case "/static/reimbursements/getRole":
                rs.populateRole(req);
                DAOUtilities.writeJSONtoResponse(req.getSession().getAttribute("currentRole"), resp);
                log.debug(req.getSession().getAttribute("currentRole"));
                break;

            case "/static/reimbursements/MyPending": // forward to DisplayReimbursements and let the java script call
                                                     // pendingReimb to pull data
                try {
                    req.getRequestDispatcher("/static/DisplayReimbursements.html").forward(req, resp);
                }
                catch (ServletException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;

            case "/static/reimbursements/managersearch":
                String searchType = req.getParameter("optType");
                String searchStatus = req.getParameter("optStatus");
                String[] searchParam = new String[] {searchType, searchStatus};
                req.getSession().setAttribute("searchParam", searchParam);
                try {
                    req.getRequestDispatcher("/static/DisplayReimbursements.html").forward(req, resp);
                }
                catch (ServletException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;

            case "/static/reimbursements/doSearch":
                // call the service to go get the data
                rs.searchReimbursements(req, resp);
                DAOUtilities.writeJSONtoResponse(req.getSession().getAttribute("searchResults"), resp);
                log.debug(req.getSession().getAttribute("searchResults"));
                break;

            case "/static/reimbursements/pendingReimb":
                rs.myPendingReimbursements(req, resp);
                DAOUtilities.writeJSONtoResponse(req.getSession().getAttribute("myPending"), resp);
                log.debug(req.getSession().getAttribute("myPending"));
                break;

            case "/static/reimbursements/getCurrentUser":
                // nothing to call...we should already have one
                try {
                    DAOUtilities.writeJSONtoResponse(req.getSession().getAttribute("currentUser"), resp);
                    log.debug(req.getSession().getAttribute("currentUser"));
                }
                catch (Exception e) {
                    log.error("For some reason we don't have a 'currentUser' attribute stored, so can't write");
                    e.printStackTrace();
                }
                break;

            case "/static/reimbursements/getOtherUser":
                // need to call service for retrieving user
                Users otherUser = new Users();

                String getOtherUserId = requestUrl.substring(36);
                otherUser = empDao.getUser(Integer.valueOf(getOtherUserId));
                try {
                    DAOUtilities.writeJSONtoResponse(otherUser, resp);
                    req.getSession().setAttribute("otherUser", otherUser);
                    log.debug("Other User = " + otherUser);
                }
                catch (Exception e) {
                    log.error("For some reason we don't have 'otherUser' so can't write");
                    e.printStackTrace();
                }
                break;

            case "/static/reimbursements/deleteRecord":
                // need to call service for deleting record
                int delReimbursementId = Integer.valueOf(requestUrl.substring(36));

                rs.deleteReimbursement(req, delReimbursementId);

                break;

            case "/static/reimbursements/pendingOther":
                String pendingOtherId = requestUrl.substring(36);
                int otherId = Integer.valueOf(pendingOtherId);
                log.debug("otherId has been set to: " + otherId);
                rs.otherPendingReimbursements(req, otherId);
                DAOUtilities.writeJSONtoResponse(req.getSession().getAttribute("otherPending"), resp);
                log.debug(req.getSession().getAttribute("otherPending"));

                break;

            case "/static/reimbursements/MyPast":
                try {
                    req.getRequestDispatcher("/static/DisplayReimbursements.html").forward(req, resp);
                }
                catch (ServletException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;
            case "/static/reimbursements/pastReimb":
                rs.myPastReimbursements(req, resp);
                DAOUtilities.writeJSONtoResponse(req.getSession().getAttribute("myPast"), resp);
                log.debug(req.getSession().getAttribute("myPast"));
                break;
            case "/static/reimbursements/AllPending":
                try {
                    if ((int) req.getSession().getAttribute("currentRoleId") == 2) {
                        req.getRequestDispatcher("/static/DisplayReimbursements.html").forward(req, resp);
                    }
                    else {
                        req.getRequestDispatcher("/static/NotAuthorized.html").forward(req, resp);
                    }
                }
                catch (Exception e) {
                    log.error("Page did not exist");
                }
                break;
            case "/static/reimbursements/pendingAll":
                rs.allPendingReimbursements(req, resp);
                DAOUtilities.writeJSONtoResponse(req.getSession().getAttribute("allPending"), resp);
                log.debug(req.getSession().getAttribute("allPending"));
                break;
            case "/static/reimbursements/AllPast":
                try {
                    if ((int) req.getSession().getAttribute("currentRoleId") == 2) {
                        req.getRequestDispatcher("/static/DisplayReimbursements.html").forward(req, resp);
                    }
                    else {
                        req.getRequestDispatcher("/static/NotAuthorized.html").forward(req, resp);
                    }
                }
                catch (Exception e) {
                    log.error("Page did not exist");
                }
                break;
            case "/static/reimbursements/pastAll":
                rs.allPastReimbursements(req, resp);
                DAOUtilities.writeJSONtoResponse(req.getSession().getAttribute("allPast"), resp);
                log.debug(req.getSession().getAttribute("allPast"));
                break;
            case "/static/reimbursements/MySearch":
                try {
                    if ((int) req.getSession().getAttribute("currentRoleId") == 2) {
                        req.getRequestDispatcher("/static/FinanceManagerSearch.html").forward(req, resp);
                    }
                    else {
                        req.getRequestDispatcher("/static/EmployeeSearch.html").forward(req, resp);
                    }
                }
                catch (Exception e) {
                    log.error("Page did not exist");
                }
                break;
            case "/static/reimbursements/MyNew":
                try {
                    req.getRequestDispatcher("/static/NewReimbursement.html").forward(req, resp);
                }
                catch (ServletException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;
            case "/static/reimbursements/OtherNew":
                try {
                    req.getRequestDispatcher("/static/NewReimbursement.html").forward(req, resp);
                }
                catch (ServletException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case "/static/reimbursements/MyDelete":
                // TODO: can call for Employee's pending reimbursements so page has that list to work with
                try {
                    req.getRequestDispatcher("/static/DisplayReimbursements.html").forward(req, resp);
                }
                catch (ServletException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;
            case "/static/reimbursements/OtherDelete":
                // TODO: can call for All pending reimbursements so page has that list to work with
                try {
                    req.getRequestDispatcher("/static/DisplayReimbursements.html").forward(req, resp);
                }
                catch (ServletException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;
            case "/static/reimbursements/StatusNew":
                // TODO: call rs for getting List of Reimbursements but filter by Pending Last 10 days
                resp.sendRedirect("/ers_project/static/StatusReimbursements.html");
                break;
            case "/static/reimbursements/StatusAll":
                // TODO: call rs for getting List of Reimbursements but filter by Pending
                resp.sendRedirect("/ers_project/static/StatusReimbursements.html");
                break;

            // /users/{id}
            default:
                // User user = us.findById(requestUrl.substring(7), resp);
                log.trace("requestURL: " + requestUrl);
                break;
        }
    }

    public void processPostRequests(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String requestUrl = req.getRequestURI().substring(req.getContextPath().length());
        log.info("ReimbursementController processing post request with requestUrl = " + requestUrl);

        String shortUrl = requestUrl;

        if (requestUrl.contains("/MyNew?") || requestUrl.contains("/MyOther?")) {
            shortUrl = requestUrl.substring(requestUrl.indexOf('?') + 1);
        }
        log.debug("shortUrl = " + shortUrl);

        switch (requestUrl) {

            case "/static/reimbursements/MyNew/?":
            case "/static/reimbursements/MyOther/?":
                log.debug("I THINK I AM EXECUTING THE MyNew and MyOther code HERE");
                req.getSession().setAttribute("newId", req.getParameter("new-id"));
                req.getSession().setAttribute("newDescription", req.getParameter("new-description"));
                req.getSession().setAttribute("newAmount", req.getParameter("new-amount"));
                req.getSession().setAttribute("newType", req.getParameter("new-type"));
                rs.createNewReimbursement(req);
                try {
                    Users tempUser = (Users) req.getSession().getAttribute("currentUser");
                    if (tempUser
                            .getErsUsersId() == (Integer.valueOf((String) req.getSession().getAttribute("newId")))) {
                        log.info("returning to the NewReimbursement for Self with ID");
                        // shouldn't need to forward...they are already on the page...let's see what happens...hopefully
                        // page refreshes
                        // req.getRequestDispatcher("/static/NewReimbursements.html").forward(req, resp);
                    }
                    else {
                        log.info("returning to the NewReimbursement that defaults to Other with ID ");
                        // shouldn't need to forward...they are already on the page...let's see what happens...hopefully
                        // page refreshes
                        // req.getRequestDispatcher("/static/NewReimbursements.html").forward(req, resp);
                    }

                }
                catch (Exception e) { // (ServletException e1)
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            case "/static/reimbursement/new":
                log.debug("TRYING TO PARSE THE POST PARAMETERS");
                log.trace("newId: " + req.getParameter("new-id"));
                log.trace("newDescription: " + req.getParameter("new-description"));
                log.trace("newAmount: " + req.getParameter("new-amount"));
                log.trace("newType: " + req.getParameter("new-type"));
                req.getSession().setAttribute("newId", req.getParameter("new-id"));
                req.getSession().setAttribute("newDescription", req.getParameter("new-description"));
                req.getSession().setAttribute("newAmount", req.getParameter("new-amount"));
                req.getSession().setAttribute("newType", req.getParameter("new-type"));
                rs.createNewReimbursement(req);
                try {
                    Users tempUser = (Users) req.getSession().getAttribute("currentUser");
                    if (tempUser
                            .getErsUsersId() == (Integer.valueOf((String) req.getSession().getAttribute("newId")))) {
                        log.info("returning to the NewReimbursement for Self with ID");
                        // shouldn't need to forward...they are already on the page...let's see what happens...hopefully
                        // page refreshes
                        // req.getRequestDispatcher("/static/NewReimbursements.html").forward(req, resp);
                    }
                    else {
                        log.info("returning to the NewReimbursement that defaults to Other with ID ");
                        // shouldn't need to forward...they are already on the page...let's see what happens...hopefully
                        // page refreshes
                        // req.getRequestDispatcher("/static/NewReimbursements.html").forward(req, resp);
                    }

                }
                catch (Exception e) { // (ServletException e1)
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            case "/static/reimbursements/MyNew":
                try {
                    req.getRequestDispatcher("/static/NewReimbursement.html").forward(req, resp);
                }
                catch (ServletException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;
            case "/static/reimbursements/OtherNew":
                try {
                    req.getRequestDispatcher("/static/NewReimbursement.html").forward(req, resp);
                }
                catch (ServletException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;

            // Example of what Donna's code might look like
            // case "/static/reimbursements/searchRequest":
            //
            // req.getSession().setAttribute("SearchCriteria", req.getParameter("searchField"));
            // try {
            // req.getRequestDispatcher("/static/DisplayReimbursements.html").forward(req, resp);
            // } catch (ServletException e1) {
            // // TODO Auto-generated catch block
            // e1.printStackTrace();
            // }
            // break;
            //
            default:
                break;
        }
    }

}