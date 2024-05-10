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

import com.exavalu.entities.SubCategory;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.services.AdminService;

/**
 * Servlet implementation class SaveSubCategoriesServlet
 */
@WebServlet("/SaveSubCategoriesServlet")
public class SaveSubCategoriesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveSubCategoriesServlet() {
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
		
		// add a new subcategory
		String newSubCategoryName = (String) request.getAttribute("newSubCategoryName");
		Integer newSubCategoryCategoryId = (Integer) request.getAttribute("newSubCategoryCategoryId");
		if (!newSubCategoryName.isEmpty()) {
			SubCategory subcategory = new SubCategory();
			subcategory.setCategoryId(newSubCategoryCategoryId);
			subcategory.setSubCategoryName(newSubCategoryName);
			if (AdminService.addSubCategory(subcategory, propertyValues)) {
				System.out.println("Successfully added new sub category"); 
			} else {
				System.out.println("Something went wrong.");
			}
		}
		
		Integer subCategoryId = (Integer) request.getAttribute("subCategoryId");
		Integer subCategoryCategoryId = (Integer) request.getAttribute("subCategoryCategoryId");
		String subCategoryName = (String) request.getAttribute("subCategoryName");
		// update a current subcategory
		if (!subCategoryName.isEmpty()) {
			SubCategory subcategory = new SubCategory();
			subcategory.setCategoryId(subCategoryCategoryId);
			subcategory.setSubCategoryId(subCategoryId);
			subcategory.setSubCategoryName(subCategoryName);
			if (AdminService.updateSubCategory(subcategory, propertyValues)) {
				System.out.println("Successfully updated new sub category"); 
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
