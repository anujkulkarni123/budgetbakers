package com.exavalu.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.exavalu.pojos.PropertyValues;
import com.exavalu.services.TransactionService;

import javax.servlet.http.HttpSession;

import com.exavalu.entities.Duration;
import com.exavalu.entities.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class TrackBudget
 */
@WebServlet("/TrackBudget")
public class TrackBudget extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TrackBudget() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.getRequestDispatcher("pages/budgetTracking.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		Duration timeRange = new Duration();
		timeRange.setLength(30);
		timeRange.setEndDate("");
		timeRange.setStartDate("");

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("USER");
		String emailAddress = user.getEmailAddress();

		double totalIncome = TransactionService.getTotalIncome(false, propertyValues, timeRange, "", emailAddress);
		double totalExpense = TransactionService.getTotalIncome(true, propertyValues, timeRange, "", emailAddress);
		Hashtable<String, Double> totalByCategory = TransactionService.getCategoriesExpenses(propertyValues, timeRange,
				"", emailAddress);
		double pastPeriodTotalIncome = TransactionService.getTotalIncome(false, propertyValues,
				TransactionService.getPreviousPeriod(timeRange), "", emailAddress);
		double pastPeriodTotalExpense = TransactionService.getTotalIncome(true, propertyValues,
				TransactionService.getPreviousPeriod(timeRange), "", emailAddress);
		Hashtable<String, Double> pastPeriodTotalByCategory = TransactionService.getCategoriesExpenses(propertyValues,
				TransactionService.getPreviousPeriod(timeRange), "", emailAddress);
		
		JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("totalIncome", totalIncome);
        jsonObject.addProperty("totalExpense", totalExpense);
        jsonObject.add("totalByCategory", new Gson().toJsonTree(totalByCategory));
        jsonObject.addProperty("pastPeriodTotalIncome", pastPeriodTotalIncome);
        jsonObject.addProperty("pastPeriodTotalExpense", pastPeriodTotalExpense);
        jsonObject.add("pastPeriodTotalByCategory", new Gson().toJsonTree(pastPeriodTotalByCategory));
        
        // Set response type and encoding
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Send JSON response
        response.getWriter().write(jsonObject.toString());
	}

}
