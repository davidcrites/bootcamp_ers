package com.capital.one.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.capital.one.daos.DAOUtilities;
import com.capital.one.daos.EmployeeDAO;
import com.capital.one.daos.FinanceManagerDao;
import com.capital.one.datamodelbeans.Reimbursement;
import com.capital.one.datamodelbeans.Users;

public class ReimbursementService {
	
	EmployeeDAO empDao = DAOUtilities.getEmployeeDao();
	FinanceManagerDao finManDao = DAOUtilities.getFinanceManagerDao();
	Logger log = Logger.getLogger("UserService");
	Users currentUser = new Users();
	Reimbursement tempReimbursement = new Reimbursement();
	
	public void myPendingReimbursements(HttpServletRequest req, HttpServletResponse resp) {
		List<Reimbursement> displayList = new ArrayList<Reimbursement>();
		currentUser = (Users) req.getSession().getAttribute("currentUser");
		
		displayList = empDao.viewEmployeeTickets(currentUser.getErsUsersId(), false);
		req.getSession().setAttribute("myPending", displayList);
		System.out.println(displayList);
	}

}
