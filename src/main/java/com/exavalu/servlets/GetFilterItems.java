package com.exavalu.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.exavalu.entities.FilterItem;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.services.FilterService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class GetFilterItems
 */
@WebServlet("/GetFilterItems")
public class GetFilterItems extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetFilterItems() {
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
	        

	        
	        ArrayList<FilterItem> filterItems = FilterService.getFilterItems(propertyValues);
	        Map<String, List<String>> categories = FilterService.getCategories(propertyValues);
	        
	       
	        // Start building the JSON structure
            JsonObject jsonResponse = new JsonObject();
            Gson gson = new Gson();

            // Add filterItems to the JSON response
            JsonArray filterItemsArray = gson.toJsonTree(filterItems).getAsJsonArray();
            jsonResponse.add("filterItems", filterItemsArray);
            
            // Add categories to the JSON response
            JsonObject categoriesJson = new JsonObject();
            for (Map.Entry<String, List<String>> entry : categories.entrySet()) {
                JsonArray subCategoryArray = gson.toJsonTree(entry.getValue()).getAsJsonArray();
                categoriesJson.add(entry.getKey(), subCategoryArray);
            }
            jsonResponse.add("categories", categoriesJson);

            // Write JSON to response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(jsonResponse));
            response.getWriter().flush();
            response.getWriter().close();
	        
           
            
		} catch (IOException e) {
			e.printStackTrace(); // Handle the exception appropriately
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
