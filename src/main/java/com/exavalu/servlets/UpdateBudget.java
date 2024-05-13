package com.exavalu.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.exavalu.entities.BudgetItem;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.services.BudgetService;
import com.exavalu.services.FilterService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class AdminAcess
 */
@WebServlet("/UpdateBudget")
public class UpdateBudget extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateBudget() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	        
	        System.out.println("Database Name: " + propertyValues.getDbname());
	        System.out.println("URL: " + propertyValues.getUrl());
	       
	        List<BudgetItem> budgetItems = BudgetService.getBudgetItems(propertyValues);
	        List<String> currencies = FilterService.getCurrencies(propertyValues);

	        Gson gson = new Gson();
	        JsonElement jsonBudgetItems = gson.toJsonTree(budgetItems); // Convert directly to JsonElement
	        JsonElement jsonCurrencies = gson.toJsonTree(currencies); // Convert directly to JsonElement

	        // Create a combined JSON object to send both lists
	        JsonObject jsonResponse = new JsonObject();
	        jsonResponse.add("budgetItems", jsonBudgetItems);
	        jsonResponse.add("currencies", jsonCurrencies);

	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().write(jsonResponse.toString());
	        
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
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
        
	        StringBuilder buffer = new StringBuilder();
	        String line;
	        while ((line = request.getReader().readLine()) != null) {
	            buffer.append(line);
	        }
	        String payload = buffer.toString();
	        JsonObject json = JsonParser.parseString(payload).getAsJsonObject();
	        JsonArray updates = json.getAsJsonArray("updates");
	
	        boolean allUpdatesSuccess = true;
	        for (JsonElement elem : updates) {
	            JsonObject obj = elem.getAsJsonObject();
	            BudgetItem item = new BudgetItem(
	                obj.get("id").getAsInt(),
	                obj.get("category").getAsString(),
	                obj.get("budgetLimit").getAsDouble(),
	                obj.get("currency").getAsString()
	            );
	            
	            boolean updateStatus = BudgetService.updateBudgetItem(item, propertyValues);
	            if (!updateStatus) {
	                allUpdatesSuccess = false;
	                break; // Optionally stop on first failure, or continue updating others
	            }
	        }
	
	        JsonObject responseJson = new JsonObject();
	        responseJson.addProperty("success", allUpdatesSuccess);
	        response.getWriter().write(responseJson.toString());
	    }
	}
}