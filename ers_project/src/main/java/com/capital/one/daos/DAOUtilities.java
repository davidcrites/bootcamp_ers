package com.capital.one.daos;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Class used to retrieve DAO Implementations. Serves as a factory. Also used to create JDBC connections.
 * 
 * @author David Crites
 *
 */
public class DAOUtilities {

    // private static AnimalDaoImpl animalDaoImpl;
    private static EmployeeDAOImpl employeeDaoImpl;

    private static FinanceManagerDao fianceManagerDaoImpl;

    private static Connection connection;

    private static Logger log = Logger.getLogger("DAOUtilities");


    
    public static synchronized EmployeeDAO getEmployeeDao() {
        if (employeeDaoImpl == null) {
            employeeDaoImpl = new EmployeeDAOImpl();
        }
        return employeeDaoImpl;
    }

    public static synchronized FinanceManagerDao getFinanceManagerDao() {
        if (fianceManagerDaoImpl == null) {
            fianceManagerDaoImpl = new FinanceManagerDaoImpl();
        }
        return fianceManagerDaoImpl;
    }

    public static synchronized String getEmployeeRole(int roleId) {
        Connection conn2;
        Statement stmt;
        try {
            conn2 = DAOUtilities.getConnection();

            stmt = conn2.createStatement();

            String sql = ("SELECT user_role FROM ers_user_roles WHERE ers_user_role_id = " + roleId);
            // System.out.println(sql);

            ResultSet rs = stmt.executeQuery(sql);
            log.debug("roleId = " + roleId);
            if(roleId != 0) {
	            rs.next();
	            return rs.getString("user_role");
            }
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized int getEmployeeId(String role) {
        Connection conn2;
        Statement stmt;
        try {
            conn2 = DAOUtilities.getConnection();

            stmt = conn2.createStatement();

            String sql = ("SELECT ers_user_role_id FROM ers_user_roles WHERE user_role = " + role);
            // System.out.println(sql);

            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getInt("ers_user_role_id");
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized String getReimbursementType(int typeId) {
        Connection conn2;
        Statement stmt;
        try {
            conn2 = DAOUtilities.getConnection();

            stmt = conn2.createStatement();

            String sql = ("SELECT reimb_type FROM ers_reimbursement_type WHERE reimb_type_id = " + typeId);
            // System.out.println(sql);

            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString("reimb_type");
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized int getReimbursementTypeId(String type) {
        Connection conn2;
        Statement stmt;
        try {
            conn2 = DAOUtilities.getConnection();

            stmt = conn2.createStatement();

            String sql = ("SELECT reimb_type_id FROM ers_reimbursement_type WHERE reimb_type = " + type);
            // System.out.println(sql);

            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getInt("reimb_type_id");
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized String getReimbursementStatus(int statusId) {
        Connection conn2;
        Statement stmt;
        try {
            conn2 = DAOUtilities.getConnection();

            stmt = conn2.createStatement();

            String sql = ("SELECT reimb_status FROM ers_reimbursement_status WHERE reimb_status_id = " + statusId);
            // System.out.println(sql);

            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString("reimb_status");
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized int getReimbursementStatusId(String status) {
        Connection conn2;
        Statement stmt;
        try {
            conn2 = DAOUtilities.getConnection();

            stmt = conn2.createStatement();

            String sql = ("SELECT reimb_status_id FROM ers_reimbursement_status WHERE reimb_status = " + status);
            // System.out.println(sql);

            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getInt("reimb_status_id");
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized String getEmployeeUsername(int userId) {
        Connection conn2;
        Statement stmt;
        try {
            conn2 = DAOUtilities.getConnection();

            stmt = conn2.createStatement();

            String sql = ("SELECT ers_username FROM ers_users WHERE ers_users_id = " + userId);
            // System.out.println(sql);

            ResultSet rs = stmt.executeQuery(sql);
            
            if (userId != 0) {
	            rs.next();
	            return rs.getString("ers_username");
            }
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized int getEmployeeUsername(String username) {
        Connection conn2;
        Statement stmt;
        try {
            conn2 = DAOUtilities.getConnection();

            stmt = conn2.createStatement();

            String sql = ("SELECT ers_user_id FROM ers_users WHERE ers_username = " + username);
            // System.out.println(sql);

            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getInt("ers_user_id");
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    static synchronized Connection getConnection() throws SQLException {
        log.debug("Attempting to get connection");
        Properties dbProps = new Properties();

        if (connection == null) {
            log.trace("connection was null, registering driver");
            try {
                Class.forName("org.postgresql.Driver");
            }
            catch (ClassNotFoundException e) {
                System.out.println("Could not register driver!");
                e.printStackTrace();
                return null;
            }
        }

        try {
            log.trace("getting connection from data source");
            dbProps.load(new FileInputStream(
                    "/Users/den421/Documents/bootcamp_ers/ers_project/src/main/resources/database.properties"));
            connection = DriverManager.getConnection(dbProps.getProperty("url"), dbProps.getProperty("username"),
                    dbProps.getProperty("password"));
            log.trace("retreived connection from data source");
            return connection;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.trace("could not retreive connection");
        return null;
    }
    
    public static void writeJSONtoResponse(Object o, HttpServletResponse resp) {
		log.debug("writing js to response");
    		try {
    		// jackson code for converting to json
		ObjectMapper om = new ObjectMapper();
		ObjectWriter ow = om.writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(o);
		log.debug("MY JSON after converting the Reimbursement Object List is: " + json);
		
		// write to response body
		resp.getWriter().print(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}