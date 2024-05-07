package com.exavalu.servlets;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.exavalu.pojos.PropertyValues;
import com.exavalu.services.FilterService;
import com.exavalu.services.RecordsService;
import com.exavalu.entities.User;
import com.exavalu.entities.Record;

/**
 * Servlet implementation class ViewRecords
 */
@WebServlet("/Records")
public class Records extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Records() {
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
		
		
//		List<Record> records = RecordsService.getRecords("", null, propertyValues);
//		request.setAttribute("RECORDS", records);
		
		request.getRequestDispatcher("pages/viewRecords.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Read the entire input stream
	    StringBuilder buffer = new StringBuilder();
	    try (BufferedReader reader = request.getReader()) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            buffer.append(line);
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }

	    String rawData = buffer.toString();

	    String decodedData = URLDecoder.decode(rawData, "UTF-8");

	    // Parse the decoded query string into a map of strings to lists of strings
	    Map<String, List<String>> queryParams = FilterService.parseString(decodedData);

	    
		User user = (User) request.getSession().getAttribute("USER");

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

		List<Record> records = RecordsService.getRecords(user.getEmailAddress(), queryParams, propertyValues);

		 // Create the main JSON response object
	    JSONObject jsonResponse = new JSONObject();
	    JSONArray recordsArray = new JSONArray();

	    // Dynamically add records to the JSON array
	    for (Record record : records) {
	        JSONObject recordJson = new JSONObject();
	        for (Field field : record.getClass().getDeclaredFields()) {
	            field.setAccessible(true); // Make the field accessible if it is private
	            try {
	                Object value = field.get(record);
	                if (value != null) { // Only add the field if value is not null
	                    recordJson.put(field.getName(), value);
	                }
	            } catch (IllegalAccessException e) {
	                System.err.println("Error accessing field: " + field.getName());
	                e.printStackTrace();
	            }
	        }
	        recordsArray.put(recordJson);
	    }

	    // Add the records array to the main JSON response object
	    jsonResponse.put("records", recordsArray);

	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(jsonResponse.toString());
	}

}
