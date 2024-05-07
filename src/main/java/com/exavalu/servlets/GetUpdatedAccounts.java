package com.exavalu.servlets;

import java.io.IOException;
import com.google.gson.Gson;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

import com.exavalu.entities.Account;
import com.exavalu.entities.AccountType;
import com.exavalu.entities.Currency;
import com.exavalu.entities.User;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.services.AccountService;
import com.exavalu.services.CurrencyService;
import com.exavalu.services.RecordService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Servlet implementation class GetUpdatedAccounts
 */
@WebServlet("/GetUpdatedAccounts")
public class GetUpdatedAccounts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUpdatedAccounts() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ServletContext context = getServletContext();
	    Properties properties = new Properties();
	    PropertyValues propertyValues = PropertyValues.getInstance();

	    try (InputStream input = context.getResourceAsStream("/WEB-INF/config.properties")) {
	        if (input == null) {
	            throw new IOException("Cannot find configuration file");
	        }
	        properties.load(input);
	        System.out.println("Database Name: " + properties.getProperty("dbname"));
	        propertyValues.setDbname(properties.getProperty("dbname"));
	        propertyValues.setPassword(properties.getProperty("password"));
	        propertyValues.setUrl(properties.getProperty("url"));
	        propertyValues.setUser(properties.getProperty("user"));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    User user = (User) request.getSession().getAttribute("USER");
	    ArrayList<Account> updatedAccounts = AccountService.getAccounts(user.getEmailAddress(), propertyValues);

	    // Convert account data to JSON using Gson
	    Gson gson = new Gson();
	    String json = gson.toJson(updatedAccounts);

	    // Send the JSON response
	    response.setContentType("application/json");
	    response.setStatus(HttpServletResponse.SC_OK);
	    try (PrintWriter out = response.getWriter()) {
	        out.print(json);
	    }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
