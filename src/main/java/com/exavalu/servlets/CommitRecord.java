package com.exavalu.servlets;

import java.io.IOException;
import java.io.InputStream;
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
import com.exavalu.pojos.PropertyValues;
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

		} catch (IOException e) {
			e.printStackTrace(); 
		}

        int recordId = Integer.parseInt(request.getParameter("recordId"));
        int accountId = Integer.parseInt(request.getParameter("accountId"));
        int secondAccountId = Integer.parseInt(request.getParameter("secondAccountId")); 
        double amount = Double.parseDouble(request.getParameter("amount"));
        String currencyName = request.getParameter("currencyName");
        String type = request.getParameter("type");
        String paymentStatus = request.getParameter("paymentStatus");
        String userEmail = request.getParameter("userEmail");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date recordDate = null;
        try {
            java.util.Date utilDate = sdf.parse(request.getParameter("recordDate"));
            recordDate = new Date(utilDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format.");
            return;
        }

        boolean operationSuccess = false;
        switch (type) {
            case "Expense":
                operationSuccess = RecordService.HandleExpense(userEmail, currencyName, accountId, amount, propertyValues);
                break;
            case "Income":
                operationSuccess = RecordService.HandleIncome(userEmail, currencyName, accountId, amount, propertyValues);
                break;
            case "Transfer":
                operationSuccess = RecordService.HandleTransfer(userEmail, currencyName, accountId, secondAccountId, amount, propertyValues);
                break;
        }

        if (operationSuccess) {
            boolean saveSuccess = RecordService.handleSaveRecord(recordId, accountId, amount, recordDate, currencyName, type, paymentStatus, secondAccountId, userEmail, propertyValues);
            if (saveSuccess) {
                request.getRequestDispatcher("pages/success.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save record.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to process record type operation.");
        }
    }
}