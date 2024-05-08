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

import com.exavalu.entities.AnalyticFilter;
import com.exavalu.entities.Category;
import com.exavalu.entities.Duration;
import com.exavalu.entities.Report;
import com.exavalu.entities.User;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.services.AnalyticFilterService;
import com.exavalu.services.TransactionService;
import com.exavalu.services.UserService;

/**
 * Servlet implementation class LoadIncomeExpenseReport
 */
@WebServlet("/LoadReport")
public class LoadReport extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoadReport() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

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
		// we need to get the correct report to query:
		int reportId = Integer.parseInt(request.getParameter("reportId"));
		request.setAttribute("REPORTID", reportId);
		ArrayList<Report> reports = UserService.getReports(propertyValues);
		ArrayList<Category> categories = UserService.getCategories(propertyValues);
		ArrayList<String> currencies = UserService.getCurrencies(propertyValues);
		request.setAttribute("REPORTS", reports);
		request.setAttribute("CATEGORIES", categories);
		request.setAttribute("CURRENCIES", currencies);

		// data for filtering reports
		System.out.println("Report Type ID: " + reportId);
		System.out.println("JSON for filters: " + request.getParameter("filters"));
		
		AnalyticFilter filters = AnalyticFilterService.getConditions(request.getParameter("filters"));
		String filterQuery = AnalyticFilterService.getFilterQuery(filters);
		
		
		int duration = Integer.parseInt(request.getParameter("duration"));
		String startDate = request.getParameter("customStartDate");
		String endDate = request.getParameter("customEndDate");
		Duration dateTimes = new Duration();
		dateTimes.setEndDate(endDate);
		dateTimes.setStartDate(startDate);
		dateTimes.setLength(duration);

		System.out.println("Date Range Selected: " + dateTimes.getLength() + " with: " + dateTimes.getStartDate() + " "
				+ dateTimes.getEndDate()); 
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("USER");
		String emailAddress = user.getEmailAddress();

		// we need to get all of our report data here
		double totalIncome = TransactionService.getTotalIncome(false, propertyValues, dateTimes,filterQuery, emailAddress);
		double totalExpense = TransactionService.getTotalIncome(true, propertyValues, dateTimes,filterQuery, emailAddress);
		Hashtable<String, Double> totalByCategory = TransactionService.getCategoriesExpenses(propertyValues, dateTimes,filterQuery, emailAddress);
		double pastPeriodTotalIncome = TransactionService.getTotalIncome(false, propertyValues, TransactionService.getPreviousPeriod(dateTimes),filterQuery, emailAddress);
		double pastPeriodTotalExpense = TransactionService.getTotalIncome(true, propertyValues, TransactionService.getPreviousPeriod(dateTimes),filterQuery, emailAddress);
		Hashtable<String, Double> pastPeriodTotalByCategory = TransactionService.getCategoriesExpenses(propertyValues, 
				TransactionService.getPreviousPeriod(dateTimes),filterQuery, emailAddress);
		
		request.setAttribute("DURATION", dateTimes);
		request.setAttribute("TOTAL_INCOME", totalIncome);
		request.setAttribute("TOTAL_EXPENSE", totalExpense);
		request.setAttribute("TOTAL_BY_CATEGORY", totalByCategory);
		request.setAttribute("PAST_TOTAL_INCOME", pastPeriodTotalIncome);
		request.setAttribute("PAST_TOTAL_EXPENSE", pastPeriodTotalExpense);
		request.setAttribute("PAST_TOTAL_BY_CATEGORY", pastPeriodTotalByCategory);

		switch (reportId) {
		case 1:
			// we need to check dateTimes and get the appropriate duration
			request.getRequestDispatcher("pages/components/incomeExpenseTable.jsp").forward(request, response);
			break;
		case 2:
			String json2 = TransactionService.getCashFlow(propertyValues, dateTimes,filterQuery, emailAddress);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json2);
			System.out.println("Sending data for generating Cash Flow chart");
			break;
		case 3:
			String json3 = TransactionService.getCumulativeCashFlow(propertyValues, dateTimes,filterQuery, emailAddress);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json3);
			System.out.println("Sending data for generating Cumulative Cash Flow chart.");
			break;
		case 4:
			String json4 = TransactionService.getCumulativeIncome(true, propertyValues, dateTimes,filterQuery, emailAddress);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json4.toString());
			System.out.println("Sending data for generating cumulative Expense bar chart."); 
			break;
		case 5:
			String json5 = TransactionService.getCumulativeIncome(false, propertyValues, dateTimes,filterQuery, emailAddress);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json5);
			System.out.println("Sending data for generating Cumulative income chart.");
			break;
		case 6:
			String json6 = TransactionService.getIncome(true, propertyValues, dateTimes,filterQuery, emailAddress);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json6);
			System.out.println("Sending data for generating Expense bar chart."); 
			break;
		case 7:
			String json7 = TransactionService.getIncome(false, propertyValues, dateTimes,filterQuery, emailAddress);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json7);
			System.out.println("Sending data for generating Income bar chart");
			break;
		case 8:
			String json8 = TransactionService.getBalance(dateTimes, filterQuery, propertyValues, emailAddress);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json8);
			System.out.println("Sending data for generating balance chart");
			break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
