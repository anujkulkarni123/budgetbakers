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
import javax.servlet.http.HttpSession;

import com.exavalu.entities.User;
import com.exavalu.pojos.CustomMessage;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.services.UserService;
import com.exavalu.utilities.MD5Hash;

/**
 * Servlet implementation class RegisterUser
 */
@WebServlet("/RegisterUser")
public class RegisterUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("pages/registerUser.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		System.out.println("reached");
		// Get parameters from request
//		String firstName = request.getParameter("firstName");
//		String lastName = request.getParameter("lastName");
		String email = request.getParameter("emailAddress");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");

		System.out.println(email);
		// Load properties for database connection
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
			propertyValues.setEmailAddress(properties.getProperty("emailAddress"));
			propertyValues.setEmailPassword(properties.getProperty("emailPassword"));
		} catch (IOException e) {
			e.printStackTrace(); // Handle the exception appropriately
		}

		// Register user using UserService
		User user = new User(firstName, lastName, email, password);

		boolean isRegistered = UserService.registerUser(user, propertyValues);
		System.out.println(isRegistered);

		if (isRegistered) {
			// Registration successful, redirect to login with success message
			HttpSession session = request.getSession();
			session.setAttribute("MSG", "Registration successful. Please verify email.");

			request.getRequestDispatcher("index.jsp").forward(request, response);
		} else {
			// Registration failed, return to registration page with error message
			CustomMessage msg = new CustomMessage();
			msg.setMessage("Registration failed. Please try again.");
			request.setAttribute("MSG", msg);
			request.getRequestDispatcher("pages/registerUser.jsp").forward(request, response);
		
		}
	}
}
