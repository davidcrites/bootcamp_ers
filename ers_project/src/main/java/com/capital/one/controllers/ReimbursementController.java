package com.capital.one.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.capital.one.datamodelbeans.Reimbursement;
import com.capital.one.datamodelbeans.Users;
import com.capital.one.services.ReimbursementService;


public class ReimbursementController {
	private Logger log = Logger.getLogger("ReimbursementController");
	private ReimbursementService rs = new ReimbursementService();
	Users myUser = new Users();
	Reimbursement tempReimbursement = new Reimbursement();
	
	public void processGetRequests(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		log.info("ReimbursementController processing get request");
		// I'm not populating a list here like he was...could do later but for now, nothing I want to do on get request.
		
		String requestUrl = req.getRequestURI().substring(req.getContextPath().length());

		switch (requestUrl) {

		case "/static/reimbursements/MyPending":
			rs.myPendingReimbursements(req, resp);
			resp.sendRedirect("/ers_project/static/DisplayReimbursements.html");
			break;

		// /users/{id}
		default:
			//User user = us.findById(requestUrl.substring(7), resp);
			log.trace("requestURL: " + requestUrl);
			break;
		}
	}
	
	public void processPostRequests(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		log.info("UserController processing post request.");
		String requestUrl = req.getRequestURI().substring(req.getContextPath().length());

		switch (requestUrl) {
		case "/users/login":
			//login(req, resp);
			break;

		default:
			break;
		}
	}

}
