package com.capital.one.servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.capital.one.datamodelbeans.Users;



public class FrontControllerServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
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
		super.doGet(req, resp);
		
		//below only works with the user bean defined...casting to User because only know it is an object
//		System.out.println((User)req.getSession().getAttribute("user"));
		

	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doPost(req, resp);
		
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
