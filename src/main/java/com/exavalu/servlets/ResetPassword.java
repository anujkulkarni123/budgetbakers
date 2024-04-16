package com.exavalu.servlets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.exavalu.entities.User;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.services.UserService;
import com.exavalu.utilities.MD5Hash;

/**
 * Servlet implementation class ResetPassword
 */
@WebServlet("/ResetPassword")
public class ResetPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResetPassword() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String email = request.getParameter("emailAddress");
		
		request.setAttribute("USEREMAIL", email);
		
		request.getRequestDispatcher("pages/resetPassword.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String email = request.getParameter("emailAddress");
		String pass1 = MD5Hash.encode(request.getParameter("password"));
		String pass2 = MD5Hash.encode(request.getParameter("password2"));
		
		ServletContext context = getServletContext();
		String fullPath = context.getRealPath("/WEB-INF/config.properties");
		Properties properties = new Properties();
		PropertyValues propertyValues = PropertyValues.getInstance();

		try (InputStream input = new FileInputStream(fullPath)) {
			properties.load(input);
			propertyValues.setDbname(properties.getProperty("dbname"));
			propertyValues.setPassword(properties.getProperty("password"));
			propertyValues.setUrl(properties.getProperty("url"));
			propertyValues.setUser(properties.getProperty("user"));
		} catch (IOException e) {
			e.printStackTrace(); // Handle the exception appropriately
		}
		boolean passwordUpdated = false;
		System.out.println(pass1 + " - " +  pass2);
		if (pass1.equals(pass2)) {
		    User user = new User();
		    user.setEmailAddress(email);
		    user.setPassword(pass2);
		    passwordUpdated = UserService.updatePassword(user.getEmailAddress(), user.getPassword(), propertyValues);
		}
		
		if (passwordUpdated) {
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
	}

}
