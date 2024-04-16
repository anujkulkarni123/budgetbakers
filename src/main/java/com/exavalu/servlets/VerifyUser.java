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

import com.exavalu.pojos.PropertyValues;
import com.exavalu.services.UserService;

/**
 * Servlet implementation class VerifyUser
 */
@WebServlet("/VerifyUser")
public class VerifyUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerifyUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		int code = Integer.parseInt(request.getParameter("code"));
		request.setAttribute("RANDCODE", code);
		
		request.getRequestDispatcher("pages/verifyUser.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		int verificationCode = Integer.parseInt(request.getParameter("code"));
		
		System.out.println(verificationCode);
		
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
		
		boolean verifyUser = UserService.verifyCode(verificationCode, propertyValues);
		
		
		if (verifyUser) {
			request.getRequestDispatcher("Login").forward(request, response);
		}
		
	}

}
