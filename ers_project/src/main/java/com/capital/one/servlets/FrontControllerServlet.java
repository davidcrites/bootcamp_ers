package com.capital.one.servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.servlets.DefaultServlet;
import org.apache.log4j.Logger;

import com.capital.one.controllers.UserController;
import com.capital.one.datamodelbeans.Users;



public class FrontControllerServlet extends DefaultServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getRootLogger();
	UserController uc = new UserController();
	
	
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
		
		System.out.println("Processing Front Controller Get Request.");
		String requestURL = req.getRequestURI().substring(req.getContextPath().length());
		log.debug("request made with url: " + requestURL);
		
		//process Employee or Finance Manager static requests only if authorized
		if(requestURL.startsWith("/static/Employee")){
			System.out.println("got to Employee check");
			try {
				if((int)req.getSession().getAttribute("currentRoleId")==1 ||
						(int)req.getSession().getAttribute("currentRoleId")==2) {
					super.doGet(req, resp);
				} else {
					req.getRequestDispatcher("/static/NotAuthorized.html").forward(req, resp);
				}
			}
			catch (NullPointerException npe) {
				req.getRequestDispatcher("/static/NotAuthorized.html").forward(req, resp);
			}
		}
		
		if(requestURL.startsWith("/static/FinanceManager")){
			System.out.println("got to Manager check");
			try {
				if((int)req.getSession().getAttribute("currentRoleId")==2){
					super.doGet(req, resp);
				} else {
					req.getRequestDispatcher("/static/NotAuthorized.html").forward(req, resp);
				}
			}
			catch(NullPointerException npe) {
				req.getRequestDispatcher("/static/NotAuthorized.html").forward(req, resp);
			}
		}

		//process other static requests normally
		if(req.getRequestURI()
				.substring(req.getContextPath().length())
				.startsWith("/static/")){
			System.out.println("processing normally");
			super.doGet(req, resp);
		}
		
		// route to user controller
		if(requestURL.startsWith("/users")) {
			uc.processGetRequests(req, resp);
		}
		
		//req.getRequestDispatcher("/static/ERS_signin.html").forward(req, resp);
		
		//below only works with the user bean defined...casting to User because only know it is an object
//		System.out.println((User)req.getSession().getAttribute("user"));
		

	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doPost(req, resp);
		
		String requestURL = req.getRequestURI().substring(req.getContextPath().length());
		log.debug("request made with url: " + requestURL);
		
		// route to user controller
		if(requestURL.startsWith("/users")) {
			uc.processPostRequests(req, resp);
		}
		
		//below was a test and only works if you had a user bean defined with the login method to store the user w/attributes
//		HttpSession sess = req.getSession();
//		if (sess.getAttribute("user")!=null)){
//		User user = us.login("blake", "pass");
//			if(user != null) {
//				sess.setAttribute("user", user);
//			}
//		}
		
	}


}
