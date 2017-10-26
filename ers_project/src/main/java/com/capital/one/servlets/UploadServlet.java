package com.capital.one.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import com.capital.one.daos.DAOUtilities;
 
//@WebServlet("/uploadImage")
@MultipartConfig(maxFileSize = 16177215)    // upload file's size up to 16MB
public class UploadServlet extends HttpServlet {
     
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

     
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // gets values of text fields
//        String firstName = request.getParameter("firstName");
//        String lastName = request.getParameter("lastName");
         
        InputStream inputStream = null; // input stream of the upload file
        //int newId = Integer.valueOf((String) request.getSession().getAttribute("newId"));
        //int newId = Integer.valueOf(request.getParameter("new-id"));
        Logger log = Logger.getLogger("UploadServlet");
        
        log.debug("Arrived in the UploadServlet");
        String requestURL = request.getRequestURI().substring(request.getContextPath().length());
        log.debug("request made with URI: " + request.getRequestURI());
        log.debug("request made with url: " + requestURL);
         
        // obtains the upload file part in this multipart request
        Part filePart = request.getPart("new-receipt");
        if (filePart != null) {
            // prints out some information for debugging
            System.out.println(filePart.getName());
            System.out.println(filePart.getSize());
            System.out.println(filePart.getContentType());
             
            // obtains input stream of the upload file
            inputStream = filePart.getInputStream();
            
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] image = new byte[16384];
	    		while ((nRead = inputStream.read(image, 0, image.length)) != -1) {
	  		  buffer.write(image, 0, nRead);
	  		}
	
	  		buffer.flush();
	  		//request.getSession().setAttribute("new-id", newId);
	  		request.getSession().setAttribute("new-image", image);
	  		log.debug("The byte array we just set as an attribute on the session is " + image);
	  		log.info("Just storing the image as attribute here - will pull later when writing reimbursement");
        }else {
  			log.info("IMAGE file is null,");
  		}
         


    }
}
