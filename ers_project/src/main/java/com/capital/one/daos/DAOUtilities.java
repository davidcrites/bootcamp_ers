package com.capital.one.daos;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * Class used to retrieve DAO Implementations. Serves as a factory.  Also used to create JDBC connections.
 * 
 * @author David Crites
 *
 */
public class DAOUtilities {
	
	//private static AnimalDaoImpl animalDaoImpl;
	private static EmployeeDAOImpl employeeDaoImpl;
	private static Connection connection;
	private static Logger log = Logger.getRootLogger();

// *******CAN ADD VERSIONS OF THIS FOR OUR DAO Implementations....these are methods that we will need
// *******to call from our "screens" package for now....so the screens will be able to use the methods in our DAOImplementation classes
//	public static synchronized AnimalDAO getAnimalDao() {
//
//		if (animalDaoImpl == null) {
//			animalDaoImpl = new AnimalDaoImpl();
//		}
//		return animalDaoImpl;
//	}
//	
	public static synchronized EmployeeDAO getEmployeeDao() {
		if (employeeDaoImpl == null) {
			employeeDaoImpl = new EmployeeDAOImpl();
		}
		return employeeDaoImpl;
	}

	static synchronized Connection getConnection() throws SQLException {
		log.debug("Attempting to get connection");
		Properties dbProps = new Properties();
		
		if (connection == null) {
			log.trace("connection was null, registering driver");
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				System.out.println("Could not register driver!");
				e.printStackTrace();
				return null;
			}
		}
		
		try {
			log.trace("getting connection from data source");
			dbProps.load(new FileInputStream("src/main/resources/database.properties"));
			connection = DriverManager.getConnection(dbProps.getProperty("url"), dbProps.getProperty("username"), dbProps.getProperty("password"));
			log.trace("retreived connection from data source");
			return connection;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.trace("could not retreive connection");
		return null;
	}

}