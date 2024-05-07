package com.exavalu.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.exavalu.entities.Card;

import javax.servlet.http.HttpSession;

import com.exavalu.entities.Account;
import com.exavalu.entities.AccountType;
import com.exavalu.entities.Currency;
import com.exavalu.entities.Menu;
import com.exavalu.entities.User;
import com.exavalu.entities.Category;
import com.exavalu.entities.SubCategory;
import com.exavalu.pojos.CustomMessage;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.services.CardService;
import com.exavalu.services.AccountService;
import com.exavalu.services.CategoryService;
import com.exavalu.services.CurrencyService;
import com.exavalu.services.UserService;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				// Here I need to get all the values received
				String emailAddress = request.getParameter("emailAddress");
				String password = request.getParameter("password");

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
				

				User user = UserService.getUser(emailAddress, password, propertyValues);

				System.out.println("USER");
				System.out.println(user);

				if (user != null) {
					
					List<Card> cards = CardService.getCards(propertyValues);
					Map<String, List<Card>> cardsByType = cards.stream().collect(Collectors.groupingBy(Card::getType));
					request.setAttribute("CARDS_BY_TYPE", cardsByType);
			        request.setAttribute("CARDS", cards);
					
			        ArrayList<Menu> menuItems = UserService.getMenu(user.getRoleId(), propertyValues);
					request.getSession().setAttribute("USER", user);
					request.getSession().setAttribute("MENULIST", menuItems);
					
					ArrayList<AccountType> accountTypes = AccountService.getAccountTypes(propertyValues);
					ArrayList<Account> accounts = AccountService.getAccounts(user.getEmailAddress(), propertyValues);
					ArrayList<Currency> currencies = CurrencyService.getCurrencies(propertyValues);
					
					System.out.println(accountTypes);
					System.out.println(accounts);
					System.out.println(currencies);
					
					request.getSession().setAttribute("ACCOUNTTYPES", accountTypes);
					request.getSession().setAttribute("ACCOUNTS", accounts);
					request.getSession().setAttribute("CURRENCIES", currencies);

					ArrayList<Category> categoryList = CategoryService.getCategories(propertyValues);
					ArrayList<SubCategory> subCategoryList = CategoryService.getSubCategories(propertyValues);
					System.out.println("CATGEGORIES");
					System.out.println(categoryList);
					System.out.println(subCategoryList);
					
					request.getSession().setAttribute("CATEGORIES", categoryList);
					request.getSession().setAttribute("SUBCATEGORIES", subCategoryList);

					request.getRequestDispatcher("pages/dashboard.jsp").forward(request, response);
				} else {
					//go back to login page with error message
					CustomMessage msg = new CustomMessage();
					msg.setMessage("Either email address or password is wrong");
					request.setAttribute("MSG", msg);
					request.getRequestDispatcher("index.jsp").forward(request, response);
				}
	}

}
