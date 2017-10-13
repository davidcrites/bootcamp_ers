package com.capital.one.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.capital.one.daos.EmployeeDAO;
import com.capital.one.daos.EmployeeDAOImpl;
import com.capital.one.datamodelbeans.Users;

public class UserService {
	
	EmployeeDAO empDao = new EmployeeDAOImpl();
	Logger log = Logger.getLogger("UserService");
	Users newUser = new Users();

	public boolean login(HttpServletRequest req) {
		
		
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		log.debug("user " + username + " is trying to login");
		
		newUser = empDao.loginEmployee(username, email, password);

		if (newUser != null) {
			req.getSession().setAttribute("currentUser", newUser);
			log.info("user " + username + " succesfully logged in");
			return true;
		}
		return false;
	}

}
