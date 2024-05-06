package com.exavalu.servlets;

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

/**
 * Servlet implementation class CommitRecord
 */
@WebServlet("/CommitRecord")
public class CommitRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommitRecord() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	    int accountId = 0;
	    int secondAccountId = 0;
	    int type = 0;
	    double amount = 0;
	    int currencyName = -1;
	    boolean operationSuccess = false;
	    String userEmail = request.getParameter("userEmail");

	    String incomeAmountStr = request.getParameter("incomeAmount");
	    String expenseAmountStr = request.getParameter("expenseAmount");
	    String transferAmountStr = request.getParameter("transferAmount");

	    if (incomeAmountStr != null && !incomeAmountStr.trim().isEmpty()) {
	        accountId = Integer.parseInt(request.getParameter("incomeAccount"));
	        type = 2;
	        amount = Double.parseDouble(incomeAmountStr);
	        currencyName = Integer.parseInt(request.getParameter("incomeCurrency"));
	        operationSuccess = RecordService.HandleIncome(userEmail, currencyName, accountId, amount, propertyValues);
	    } else if (expenseAmountStr != null && !expenseAmountStr.trim().isEmpty()) {
	        accountId = Integer.parseInt(request.getParameter("expenseAccount"));
	        type = 1;
	        amount = Double.parseDouble(expenseAmountStr);
	        currencyName = Integer.parseInt(request.getParameter("expenseCurrency"));
	        operationSuccess = RecordService.HandleExpense(userEmail, currencyName, accountId, amount, propertyValues);
	    } else if (transferAmountStr != null && !transferAmountStr.trim().isEmpty()) {
	        accountId = Integer.parseInt(request.getParameter("transferFromAccount"));
	        secondAccountId = Integer.parseInt(request.getParameter("transferToAccount"));
	        type = 3;
	        amount = Double.parseDouble(transferAmountStr);
	        currencyName = Integer.parseInt(request.getParameter("transferCurrency"));
	        operationSuccess = RecordService.HandleTransfer(userEmail, currencyName, accountId, secondAccountId, amount, propertyValues);
	    }

	    	    
	    	    
	    
	    

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    Date recordDate = null;
	    try {
	        java.util.Date utilDate = sdf.parse(request.getParameter("date"));
	        recordDate = new Date(utilDate.getTime());
	    } catch (ParseException e) {
	        e.printStackTrace();
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format.");
	        return;
	    }

	    if (operationSuccess) {
	    	boolean saveSuccess = RecordService.handleSaveRecord(accountId, amount, recordDate, currencyName, type, request.getParameter("paymentStatus"), secondAccountId, userEmail, propertyValues);
	        if (saveSuccess) {
				System.out.println("redirect to pages/accounts");
				
				ArrayList<AccountType> accountTypes = AccountService.getAccountTypes(propertyValues);
				ArrayList<Account> accounts = AccountService.getAccounts(user.getEmailAddress(), propertyValues);
				ArrayList<Currency> currencies = CurrencyService.getCurrencies(propertyValues);
				request.setAttribute("ACCOUNTTYPES", accountTypes);
				request.setAttribute("ACCOUNTS", accounts);
				request.setAttribute("CURRENCIES", currencies);
	        	request.getRequestDispatcher("/pages/dashboard.jsp").forward(request, response);
	        } else {
	            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save record.");
	        }
	    } else {
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to process record type operation.");
	    }
	}
}