package com.exavalu.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.exavalu.entities.Account;
import com.exavalu.entities.Currency;
import com.exavalu.entities.Category;
import com.exavalu.entities.Duration;
import com.exavalu.entities.Report;
import com.exavalu.entities.User;
import com.exavalu.entities.RecordState;

import com.exavalu.pojos.PropertyValues;
import com.exavalu.services.CurrencyService;
import com.exavalu.services.TransactionService;
import com.exavalu.services.UserService;

/**
 * Servlet implementation class ViewAnalytics
 */
@WebServlet("/ViewAnalytics")
public class ViewAnalytics extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewAnalytics() {
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
			e.printStackTrace(); // Handle the exception appropriately
		}
		
		// list of reports we can view
		ArrayList<Report> reports = UserService.getReports(propertyValues);
		ArrayList<Category> categories = UserService.getCategories(propertyValues);
		// we need to pass the calendar information from the dropdown to our AJAX 
		// so we can query the correct range of data 
		Duration timeRange = new Duration();
		timeRange.setLength(30);
		timeRange.setEndDate("");
		timeRange.setStartDate("");

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("USER");
		String emailAddress = user.getEmailAddress();
		
		double totalIncome = TransactionService.getTotalIncome(false, propertyValues, timeRange, "", emailAddress);
		double totalExpense = TransactionService.getTotalIncome(true, propertyValues, timeRange, "", emailAddress);
		Hashtable<String, Double> totalByCategory = TransactionService.getCategoriesExpenses(propertyValues, 
				timeRange, "", emailAddress);
		double pastPeriodTotalIncome = TransactionService.getTotalIncome(false, propertyValues, 
				TransactionService.getPreviousPeriod(timeRange), "", emailAddress);
		double pastPeriodTotalExpense = TransactionService.getTotalIncome(true, propertyValues, 
				TransactionService.getPreviousPeriod(timeRange), "", emailAddress);
		Hashtable<String, Double> pastPeriodTotalByCategory = TransactionService.getCategoriesExpenses(propertyValues, 
				TransactionService.getPreviousPeriod(timeRange), "", emailAddress);
		ArrayList<Currency> currencies = CurrencyService.getCurrencies(propertyValues);
		ArrayList<Account> accounts = TransactionService.getAccounts(propertyValues);
		ArrayList<String> paymentTypes = TransactionService.getPaymentTypes(propertyValues);
		ArrayList<RecordState> recordStates = TransactionService.getRecordStates(propertyValues);
		ArrayList<String> recordTypes = TransactionService.getRecordTypes(propertyValues);
		
		request.setAttribute("TOTAL_INCOME", totalIncome);
		request.setAttribute("TOTAL_EXPENSE", totalExpense);
		request.setAttribute("TOTAL_BY_CATEGORY", totalByCategory);
		request.setAttribute("REPORTS", reports);
		request.setAttribute("CATEGORIES", categories);
		request.setAttribute("CURRENCIES", currencies);
		
		request.setAttribute("SIDEBAR_CATEGORIES", categories);
		request.setAttribute("SIDEBAR_CURRENCIES", currencies);
		request.setAttribute("SIDEBAR_ACCOUNTS", accounts);
		request.setAttribute("SIDEBAR_PAYMENTTYPES", paymentTypes);
		request.setAttribute("SIDEBAR_RECORDSTATES", recordStates);
		request.setAttribute("SIDEBAR_RECORDTYPES", recordTypes);
		
		request.setAttribute("INITIAL_DURATION", "Last 30 Days");
		request.setAttribute("PAST_TOTAL_INCOME", pastPeriodTotalIncome);
		request.setAttribute("PAST_TOTAL_EXPENSE", pastPeriodTotalExpense);
		request.setAttribute("PAST_TOTAL_BY_CATEGORY", pastPeriodTotalByCategory);
		request.getRequestDispatcher("pages/analytics.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
