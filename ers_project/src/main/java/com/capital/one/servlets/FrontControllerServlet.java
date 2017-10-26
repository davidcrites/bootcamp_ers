package com.capital.one.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.catalina.servlets.DefaultServlet;
import org.apache.log4j.Logger;

import com.capital.one.controllers.ReimbursementController;
import com.capital.one.controllers.UserController;
import com.capital.one.daos.DAOUtilities;

public class FrontControllerServlet extends DefaultServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    Logger log = Logger.getLogger("FrontControllerServlet");

    UserController uc = new UserController();

    ReimbursementController rc = new ReimbursementController();

    @Override
    public void init() throws ServletException {
        // TODO Auto-generated method stub
        super.init();
        // ServletConfig config = getServletConfig();
        // System.out.println(config.getInitParameter("param1"));
        // ServletContext context = getServletContext();
        // System.out.println("servlet context param1: " + context.getInitParameter("param1"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub

        log.debug("Processing Front Controller Get Request.");
        String requestURL = req.getRequestURI().substring(req.getContextPath().length());
        log.debug("request made with URI: " + req.getRequestURI());
        log.debug("request made with url: " + requestURL);

        if (requestURL.startsWith("/static/js") ||
                requestURL.startsWith("/static/css") ||
                requestURL.startsWith("/static/custom_css") ||
                requestURL.startsWith("/static/fonts") ||
                requestURL.startsWith("/static/images")) {
            log.trace("/static/[folder]: " + requestURL);
            super.doGet(req, resp);
            return;
        }
        else if (requestURL.startsWith("/static")) { // checking if static first, otherwise non-site urls will fail
                                                     // validate check
            // route to user controller
            if (!(requestURL.startsWith("/static/ERS_signin") || requestURL.startsWith("/static/NotAuthorized.html"))) {
                log.debug("We need to validateUser");
                if (!this.validateUser(req, requestURL)) { // if NOT a validated User
                    resp.sendRedirect("/ers_project/static/NotAuthorized.html");
                    resp.setContentLength(0);
                    return;
                }
            }
//            if (requestURL.startsWith("/static/NotAuthorized.html")) {
//               resp.sendRedirect("/ers_project/static/NotAuthorized.html");
//               //return;
//            }
            // action="/ers_project/static/reimbursements/managersearch"
            // process Employee or Finance Manager static requests only if authorized check above passed
            if (requestURL.startsWith("/static/Employee")) { // process Employee Gets normally, we already validated
                log.trace("/static/Employee: " + requestURL);
                super.doGet(req, resp);
                return;
            }
            else if (requestURL.startsWith("/static/FinanceManager")) { // process Finance Man Gets normally, we already
                                                                        // validated
                log.trace("/static/FinanceManager: " + requestURL);
                super.doGet(req, resp);
                return;
            }
            else if (requestURL.startsWith("/static/ERS_logout")) {
            	    // signing in, so remove the current user if there is one
            	    req.getSession().removeAttribute("currentUser");
            	    req.getSession().removeAttribute("currentRoleId");
                // send to Reimbursement Controller
                log.trace("/static/ERS_signin.html: " + requestURL);
                resp.sendRedirect("/ers_project/static/ERS_signin.html");
                return;
            }
            else if (requestURL.startsWith("/static/users")) { // send user functions to user controller
                // send to User Controller
                log.trace("/static/users: " + requestURL);
                uc.processGetRequests(req, resp);
                return;
            }
            else //
            if (requestURL.startsWith("/static/reimbursements")) { // send reimbursement functions to reimbursement
                                                                   // controller
                // send to Reimbursement Controller
                log.trace("/static/reimbursements: " + requestURL);
                rc.processGetRequests(req, resp);
                return;
            }
            else //
            if (requestURL.startsWith("/static/reimbursement")) { // send reimbursement functions to reimbursement
                                                                  // controller
                // send to Reimbursement Controller
                log.trace("/static/reimbursement: " + requestURL);
                // try doing nothing here.
            }
            else {
                log.trace("non-site URL: " + requestURL);
                super.doGet(req, resp); // non-site Get requests processed normally
                return;
            }
        }
        // log.trace("non-site URL: " + requestURL);
        // super.doGet(req, resp); // non-site Get requests processed normally
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        // super.doPost(req, resp);
        log.debug("Processing Front Controller Post Request.");
        String requestURL = req.getRequestURI().substring(req.getContextPath().length());
        log.debug("request made with URI: " + req.getRequestURI());
        log.debug("request made with url: " + requestURL);

        if (requestURL.startsWith("/static/js") ||
                requestURL.startsWith("/static/css") ||
                requestURL.startsWith("/static/custom_css") ||
                requestURL.startsWith("/static/fonts") ||
                requestURL.startsWith("/static/images/")) {
            super.doPost(req, resp);
            return;
        }
        else if (requestURL.startsWith("/static")) {

            if (!(requestURL.startsWith("/static/users/login"))) { // if NOT /users/login
                if (!this.validateUser(req, requestURL)) { // if NOT a validated User
                    req.getRequestDispatcher("/static/NotAuthorized.html").forward(req, resp);
                    return;
                }
            }

            // route to user controller
            if (requestURL.startsWith("/static/users")) {
                uc.processPostRequests(req, resp);
            }
            if (requestURL.startsWith("/static/reimbursement")) {
                rc.processPostRequests(req, resp);
            }
        }
        else if (requestURL.contains("uploadImage")) {
        		boolean success = false;
        		//success = uploadImage(req,resp);
        		if (!success) {
        			resp.setStatus(501);
        		}
        }
        log.debug("requestURL");
        super.doPost(req, resp);
        return;
    }

    private boolean validateUser(HttpServletRequest req, String requestURL) {
        // process Employee or Finance Manager static requests only if authorized
        if (requestURL.startsWith("/static/FinanceManager")) {
            log.debug("got to Manager check");
            log.debug(req.getSession().getAttribute("currentRoleId"));
            try {
                if ((int) req.getSession().getAttribute("currentRoleId") == 2) {
                    return true;
                }
                else {
                    return false;
                }
            }
            catch (NullPointerException npe) {
                return false;
            }
        }
        else {
            try {
                if ((int) req.getSession().getAttribute("currentRoleId") == 1 ||
                        (int) req.getSession().getAttribute("currentRoleId") == 2) {
                    return true;
                }
                else {
                    return false;
                }
            }
            catch (NullPointerException npe) {
                return false;
            }
        }
    }
    
//    private boolean uploadImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        InputStream inputStream = null; // input stream of the upload file
//        int newId = Integer.valueOf((String) request.getSession().getAttribute("newId"));
//        Logger log = Logger.getLogger("UploadServlet");
//        
//        log.debug("Arrived in the UploadServlet");
//        String requestURL = request.getRequestURI().substring(request.getContextPath().length());
//        log.debug("request made with URI: " + request.getRequestURI());
//        log.debug("request made with url: " + requestURL);
//         
//        // obtains the upload file part in this multipart request
//        Part filePart = request.getPart("new-receipt");
//        if (filePart != null) {
//            // prints out some information for debugging
//            System.out.println(filePart.getName());
//            System.out.println(filePart.getSize());
//            System.out.println(filePart.getContentType());
//             
//            // obtains input stream of the upload file
//            inputStream = filePart.getInputStream();
//        }
//         
//        Connection conn = null; // connection to the database
//        String message = null;  // message will be sent back to client
//         
//        try {
//            // connects to the database
//            conn = DAOUtilities.getConnection();
// 
//            // constructs SQL statement
//            String sql = "UPDATE ers_reimbursement SET reimb_receipt = ? WHERE reimb_id= ?";
//            PreparedStatement statement = conn.prepareStatement(sql);
//            
//            if (inputStream != null) {
//                // fetches input stream of the upload file for the blob column
//                statement.setBlob(1, inputStream);
//            }
//            
//            statement.setInt(2, newId);
//             
//
// 
//            // sends the statement to the database server
//            int row = statement.executeUpdate();
//            if (row > 0) {
//                message = "File uploaded and saved into database";
//            }
//        } catch (SQLException ex) {
//            message = "ERROR: " + ex.getMessage();
//            log.debug(message);
//            ex.printStackTrace();
//            return false;
//        } 
//    	return true;
//    }

}
