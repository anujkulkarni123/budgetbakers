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

import com.exavalu.entities.Category;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.services.AdminService;

/**
 * Servlet implementation class SaveCategoriesServlet
 */
@WebServlet("/SaveCategoriesServlet")
public class SaveCategoriesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveCategoriesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		// adding a category
		String newCategoryName = (String) request.getAttribute("newCategoryName");
		if (!newCategoryName.isEmpty()) {
			Category category = new Category();
			if (AdminService.addCategories(category, propertyValues)) {
				System.out.println("Successfully added new category"); 
			} else {
				System.out.println("Something went wrong.");
			}
		}
		
		// updating a category
		// we need to be able to grab this parameter, right now I can't really do that
		Integer categoryId = (Integer) request.getAttribute("categoryId");
		String categoryName = (String) request.getAttribute("categoryName");
		System.out.println("Grabbing: categoryId: " + categoryId +" categoryName: " + categoryName);
		if (!categoryName.isEmpty()) {
			Category category = new Category();
			if (AdminService.updateCategories(category, propertyValues)) {
				System.out.println("Successfully updated new category"); 
			} else {
				System.out.println("Something went wrong.");
			}
		}
		request.getRequestDispatcher("pages/admin.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
