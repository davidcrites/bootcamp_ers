package com.capital.one.servlets;

import java.io.IOException;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.log4j.Logger;

import com.capital.one.controllers.ReimbursementController;
import com.capital.one.controllers.UserController;



public class FrontControllerServlet extends DefaultServlet{

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
//		ServletConfig config = getServletConfig();
//		System.out.println(config.getInitParameter("param1"));
//		ServletContext context = getServletContext();
//		System.out.println("servlet context param1: " + context.getInitParameter("param1"));
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		log.debug("Processing Front Controller Get Request.");
		String requestURL = req.getRequestURI().substring(req.getContextPath().length());
		log.debug("request made with URI: " + req.getRequestURI());
		log.debug("request made with url: " + requestURL);
		
		if(requestURL.startsWith("/static/js") ||
				requestURL.startsWith("/static/css") ||
				requestURL.startsWith("/static/custom_css") ||
				requestURL.startsWith("/static/fonts") ||
				requestURL.startsWith("/static/images")) {
			log.trace("/static/[folder]: " + requestURL);
			super.doGet(req, resp);
		} else
		if(requestURL.startsWith("/static")) {                       //checking if static first, otherwise non-site urls will fail validate check
		// route to user controller
			if(!(requestURL.startsWith("/static/ERS_signin") || requestURL.startsWith("/static/NotAuthorized.html"))) { 		//if NOT static/users/login
				if (!this.validateUser(req, requestURL)) {           //if NOT a validated User
					req.getRequestDispatcher("/static/NotAuthorized.html").forward(req, resp);
				}
			}
			
			//process Employee or Finance Manager static requests only if authorized check above passed
			if(requestURL.startsWith("/static/Employee")){  //process Employee Gets normally, we already validated
				log.trace("/static/Employee: " + requestURL);
				super.doGet(req, resp);
			}else
			if(requestURL.startsWith("/static/FinanceManager")){  //process Finance Man Gets normally, we already validated
				log.trace("/static/FinanceManager: " + requestURL);
				super.doGet(req, resp);
			}else
			if(requestURL.startsWith("/static/ERS_signin.html")) {  //send reimbursement functions to reimbursement controller
				//send to Reimbursement Controller
				log.trace("/static/ERS_signin.html: " + requestURL);
				super.doGet(req, resp);
			}else
			if(requestURL.startsWith("/static/users")) {  //send user functions to user controller
				//send to User Controller
				log.trace("/static/users: " + requestURL);
				uc.processGetRequests(req, resp);
			}else //  
			if(requestURL.startsWith("/static/reimbursements")) {  //send reimbursement functions to reimbursement controller
				//send to Reimbursement Controller
				log.trace("/static/reimbursements: " + requestURL);
				rc.processGetRequests(req, resp);
			}else {
				log.trace("non-site URL: " + requestURL);
				super.doGet(req, resp);  // non-site Get requests processed normally
			}
		}
//		log.trace("non-site URL: " + requestURL);
//		super.doGet(req, resp);  // non-site Get requests processed normally
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doPost(req, resp);
		log.debug("Processing Front Controller Post Request.");
		String requestURL = req.getRequestURI().substring(req.getContextPath().length());
		log.debug("request made with URI: " + req.getRequestURI());
		log.debug("request made with url: " + requestURL);
		
		if(requestURL.startsWith("/static/js") ||
				requestURL.startsWith("/static/css") ||
				requestURL.startsWith("/static/custom_css") ||
				requestURL.startsWith("/static/fonts") ||
				requestURL.startsWith("/static/images/")) {
			super.doPost(req, resp);
		}else
		if(requestURL.startsWith("/static")) {
			
			if(!( requestURL.startsWith("/static/users/login") ) ) { 		//if NOT /users/login
				if (!this.validateUser(req, requestURL)) {           //if NOT a validated User
					req.getRequestDispatcher("/static/NotAuthorized.html").forward(req, resp);
				}
			}
			
			// route to user controller
			if(requestURL.startsWith("/static/users")) {
				uc.processPostRequests(req, resp);
			}
			if(requestURL.startsWith("/static/reimbursement")) {
				rc.processPostRequests(req, resp);
			}
		}
		log.debug("requestURL");
		super.doPost(req, resp);		
	}
	
	private boolean validateUser(HttpServletRequest req, String requestURL) {
		//process Employee or Finance Manager static requests only if authorized
		if(requestURL.startsWith("/static/FinanceManager")){
			log.debug("got to Manager check");
			log.debug(req.getSession().getAttribute("currentRoleId"));
			try {
				if((int)req.getSession().getAttribute("currentRoleId")==2){
					return true;
				} else {
					return false;
				}
			}
			catch(NullPointerException npe) {
				return false;
			}
		} else {
			try {
				if((int)req.getSession().getAttribute("currentRoleId")==1 ||
						(int)req.getSession().getAttribute("currentRoleId")==2) {
					return true;
				} else {
					return false;
				}
			}
			catch (NullPointerException npe) {
				return false;
			}
		}
	}


}
