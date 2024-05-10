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

import com.exavalu.entities.Currency;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.services.AdminService;

/**
 * Servlet implementation class UpdateInfo
 */
@WebServlet("/UpdateInfo")
public class SaveCurrenciesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SaveCurrenciesServlet() {
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
		
		// saving a new currency to db
		String currencyName = (String) request.getAttribute("newCurrencyName");
		float conversionRate = (Float) request.getAttribute("newConversionRate");
		
		if (currencyName != null && conversionRate != 0) {
			Currency currency = new Currency();
			currency.setConversionRate(conversionRate);
			currency.setCurrencyName(currencyName);
			if (AdminService.addCurrency(currency, propertyValues)) {
				System.out.println("Successfully added new currency"); 
			} else {
				System.out.println("Something went wrong.");
			}
		} 
		// updating a currencies values
		int currencyId = (Integer) request.getAttribute("id");
		String currencyNameToUpdate = (String) request.getAttribute("currencyName");
		float conversionRateToUpdate = (Float) request.getAttribute("conversionRate");
		if (currencyNameToUpdate != null && conversionRateToUpdate > 0) {
			Currency currencyToUpdate = new Currency();
			currencyToUpdate.setConversionRate(conversionRateToUpdate);
			currencyToUpdate.setCurrencyName(currencyNameToUpdate);
			currencyToUpdate.setId(currencyId);
			if (AdminService.updateCurrency(currencyToUpdate, propertyValues)) {
				System.out.println("Successfully added new currency"); 
			} else {
				System.out.println("Something went wrong.");
			}
		}
		request.getRequestDispatcher("pages/admin.jsp").forward(request, response);
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
