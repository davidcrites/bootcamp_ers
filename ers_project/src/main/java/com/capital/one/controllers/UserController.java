package com.capital.one.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.capital.one.datamodelbeans.Users;

public class UserController {
	
	private Logger log = Logger.getRootLogger();
	private UserService us = new UserService();
	Users myUser = new Users();
	
	public void processGetRequests(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		System.out.println("UserController processing get request");
		// I'm not populating a list here like he was...could do later but for now, nothing I want to do on get request.
		
//		String requestUrl = req.getRequestURI().substring(req.getContextPath().length());
//
//		switch (requestUrl) {
//
//		// get all users
//		case "/users":
//			List<User> users = us.getAllUser(req);
//			break;
//
//		// /users/{id}
//		default:
//			User user = us.findById(requestUrl.substring(7), resp);
//			log.trace("user from default: " + user);
//			break;
//		}
	}
	
	public void processPostRequests(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		System.out.println("UserController processing post request.");
		String requestUrl = req.getRequestURI().substring(req.getContextPath().length());

		switch (requestUrl) {
		case "/users/login":
			login(req, resp);
			break;

		default:
			break;
		}
	}
	
	private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		myUser = (Users) req.getSession().getAttribute("currentUser");
		System.out.println("Current Role ID = " + req.getSession().getAttribute("currentRoleId"));
		if (us.login(req)) {
			if((int)req.getSession().getAttribute("currentRoleId")==2) {
				resp.sendRedirect("/ers_project/static/FinanceManagerMenu.html");
			} else {
				resp.sendRedirect("/ers_project/static/EmployeeMenu.html");
			}
		} else {
			resp.sendRedirect("/ers_project/static/ERS_signin.html");
		}
	}

}
