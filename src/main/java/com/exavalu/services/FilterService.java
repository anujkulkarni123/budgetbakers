package com.exavalu.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.exavalu.pojos.PropertyValues;
import com.exavalu.entities.FilterItem;
import com.exavalu.entities.Category;
import com.exavalu.utilities.DbConnectionProvider;
import com.google.gson.Gson;

public class FilterService {

	public static ArrayList<FilterItem> getFilterItems(PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);

		ArrayList<FilterItem> filterItems = new ArrayList<>();

		String sql = "SELECT filterName From FilterItem ";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				FilterItem item = new FilterItem();

				item.setFilterName(rs.getString("filterName"));
				filterItems.add(item);
			}
			return filterItems;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			// Close the connection
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getCategories(PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);

		String sql = "SELECT c.categoryName, sc.subCategoryName\n" + "FROM Categories c\n"
				+ "JOIN SubCategories sc ON c.categoryId = sc.categoryId\n"
				+ "ORDER BY c.categoryId, sc.subCategoryId;";

		Map<String, List<String>> categoryMap = new LinkedHashMap<>();

		// Assume 'getConnection()' gets a valid database connection
		try (PreparedStatement pstmt = con.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

			// Process the result set
			while (rs.next()) {
				String categoryName = rs.getString("categoryName");
				String subCategoryName = rs.getString("subCategoryName");

				// If the map doesn't have the category, add it with a new list
				categoryMap.computeIfAbsent(categoryName, k -> new ArrayList<>()).add(subCategoryName);
			}

			// Convert the map into a list of category objects for JSON conversion
			List<Category> categories = new ArrayList<>();
			for (Map.Entry<String, List<String>> entry : categoryMap.entrySet()) {
				categories.add(new Category(entry.getKey(), entry.getValue()));
			}

			// Convert the list of categories into JSON
			Gson gson = new Gson();
			
			System.out.println(gson.toJson(categories));
			
			return gson.toJson(categories);

		} catch (SQLException e) {
			// Handle exceptions properly
			e.printStackTrace();
		}

		return null;

	}

}
