package com.exavalu.servlets;

import java.io.FileInputStream;
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

import com.exavalu.entities.Account;
import com.exavalu.entities.AccountType;
import com.exavalu.entities.Category;
import com.exavalu.entities.Currency;
import com.exavalu.entities.SubCategory;
import com.exavalu.entities.User;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.services.AccountService;
import com.exavalu.services.CategoryService;
import com.exavalu.services.CurrencyService;

/**
 * Servlet implementation class AdminAccess
 */
@WebServlet("/AdminAccess")
public class AdminAccess extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminAccess() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		User user = (User) request.getSession().getAttribute("USER");
		
		if (user != null) {
			ServletContext context = getServletContext();
			String fullPath = context.getRealPath("/WEB-INF/config.properties");
			
			Properties properties = new Properties();
			PropertyValues propertyValues = PropertyValues.getInstance();

			try (InputStream input = new FileInputStream(fullPath)) {

				properties.load(input);
				String dbName = properties.getProperty("dbname");
				String url = properties.getProperty("url");
				String user1 = properties.getProperty("user");
				String dbpassword = properties.getProperty("password");

				propertyValues.setDbname(dbName);
				propertyValues.setPassword(dbpassword);
				propertyValues.setUrl(url);
				propertyValues.setUser(user1);

			} catch (IOException e) {
				e.printStackTrace(); // Handle the exception appropriately
			}
			
			System.out.println("redirect to pages/accounts");
			ArrayList<AccountType> accountTypes = AccountService.getAccountTypes(propertyValues);
			ArrayList<Account> accounts = AccountService.getAccounts(user.getEmailAddress(), propertyValues);
			ArrayList<Currency> currencies = CurrencyService.getCurrencies(propertyValues);
			request.setAttribute("ACCOUNTTYPES", accountTypes);
			request.setAttribute("ACCOUNTS", accounts);
			request.setAttribute("CURRENCIES", currencies);
			
			ArrayList<Category> categoryList = CategoryService.getCategories(propertyValues);
			ArrayList<SubCategory> subCategoryList = CategoryService.getSubCategories(propertyValues);
			
			request.setAttribute("CATEGORIES", categoryList);
			request.setAttribute("SUBCATEGORIES", subCategoryList);
			
			request.getRequestDispatcher("pages/admin.jsp").forward(request, response);
		}
		else
		{
			request.getRequestDispatcher("pages/index.jsp").forward(request, response);
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
