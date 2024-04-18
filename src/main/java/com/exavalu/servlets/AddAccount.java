package com.exavalu.servlets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.exavalu.entities.Account;
import com.exavalu.entities.AccountType;
import com.exavalu.entities.Currency;
import com.exavalu.entities.User;
import com.exavalu.pojos.CustomMessage;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.services.AccountService;
import com.exavalu.services.CurrencyService;
import com.exavalu.services.UserService;

/**
 * Servlet implementation class AddAccount
 */
@WebServlet("/AddAccount")
public class AddAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddAccount() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		//String emailAddress = request.getParameter("emailAddress");
		String name = request.getParameter("name");
        int accountTypeId = Integer.parseInt(request.getParameter("accountType"));
        float accountBalance = Float.parseFloat(request.getParameter("initialAmount"));
        String currency = request.getParameter("currency");
        String userEmail = request.getParameter("emailAddress");

		ServletContext context = getServletContext();
	    Properties properties = new Properties();
	    PropertyValues propertyValues = PropertyValues.getInstance();

	    try (InputStream input = context.getResourceAsStream("/WEB-INF/config.properties")) {
	        if (input == null) {
	            throw new IOException("Cannot find configuration file");
	        }
	        properties.load(input);
	        
	        System.out.println(properties.getProperty("dbname"));
	        
	        String dbName = properties.getProperty("dbname");
	        String url = properties.getProperty("url");
	        String user = properties.getProperty("user");
	        String dbpassword = properties.getProperty("password");
	        
	        propertyValues.setDbname(dbName);
	        propertyValues.setPassword(dbpassword);
	        propertyValues.setUrl(url);
	        propertyValues.setUser(user);
	        
	        System.out.println("Database Name: " + propertyValues.getDbname());
	        System.out.println("URL: " + propertyValues.getUrl());

		} catch (IOException e) {
			e.printStackTrace(); 
		}
		
	    boolean success = AccountService.addAccountToDatabase(userEmail, name, accountTypeId, accountBalance, currency, propertyValues);
		ArrayList<AccountType> accountTypes = AccountService.getAccountTypes(propertyValues);
		ArrayList<Account> accounts = AccountService.getAccounts(userEmail, propertyValues);
		ArrayList<Currency> currencies = CurrencyService.getCurrencies(propertyValues);
		request.setAttribute("ACCOUNTTYPES", accountTypes);
		request.setAttribute("ACCOUNTS", accounts);
		request.setAttribute("CURRENCIES", currencies);
		request.getRequestDispatcher("pages/accounts.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
