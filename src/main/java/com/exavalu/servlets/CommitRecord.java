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

	    int accountId = 0;
	    int secondAccountId = 0;
	    int type = 0;
	    double amount = 0;  
	    int currencyName = -1; 
	    boolean operationSuccess = false;
	    String userEmail = request.getParameter("userEmail");

	    String accountIdStr = request.getParameter("expenseAccount");
	    if (accountIdStr == null || accountIdStr.trim().isEmpty()) {
	        accountIdStr = request.getParameter("incomeAccount");
	        if (accountIdStr == null || accountIdStr.trim().isEmpty()) {
	            accountIdStr = request.getParameter("transferFromAccount");
	            String secondAccountIdStr = request.getParameter("transferToAccount");
	            accountId = Integer.parseInt(accountIdStr);
	            secondAccountId = Integer.parseInt(secondAccountIdStr);
	            type = 3;
	            amount = Double.parseDouble(request.getParameter("transferAmount"));
	            currencyName = Integer.parseInt(request.getParameter("transferCurrency"));
	            
	            operationSuccess = RecordService.HandleTransfer(userEmail, currencyName, accountId, secondAccountId, amount, propertyValues);
	        } else {
	            accountId = Integer.parseInt(accountIdStr);
	            type = 2;
	            amount = Double.parseDouble(request.getParameter("incomeAmount"));
	            currencyName = Integer.parseInt(request.getParameter("incomeCurrency"));
	            operationSuccess = RecordService.HandleIncome(userEmail, currencyName, accountId, amount, propertyValues);
	        }
	    } else {
	        accountId = Integer.parseInt(accountIdStr);
	        type = 1;
	        amount = Double.parseDouble(request.getParameter("expenseAmount"));
	        currencyName = Integer.parseInt(request.getParameter("expenseCurrency"));
	       
	        operationSuccess = RecordService.HandleExpense(userEmail, currencyName, accountId, amount, propertyValues);
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
	        	request.getRequestDispatcher("/ViewAccount").forward(request, response);
	        } else {
	            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save record.");
	        }
	    } else {
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to process record type operation.");
	    }
	}
}