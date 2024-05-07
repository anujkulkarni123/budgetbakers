package com.exavalu.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
			System.out.println("Array Function Success");
			return filterItems;
		} catch (SQLException e) {
			// Log or print more detailed error information
			System.err.println("SQLState: " + e.getSQLState());
			System.err.println("Error Code: " + e.getErrorCode());
			System.err.println("Message: " + e.getMessage());
			e.printStackTrace();
			// Optionally, rethrow as a custom exception or handle accordingly
			throw new RuntimeException("Database access error encountered", e);
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

	public static Map<String, List<String>> getCategories(PropertyValues propertyValues) {
		
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
			System.out.println("Map Function Success");
			return categoryMap;

		} catch (SQLException e) {
			// Log or print more detailed error information
			System.err.println("SQLState: " + e.getSQLState());
			System.err.println("Error Code: " + e.getErrorCode());
			System.err.println("Message: " + e.getMessage());
			e.printStackTrace();
			// Optionally, rethrow as a custom exception or handle accordingly
			throw new RuntimeException("Database access error encountered", e);
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
	
	public static ArrayList<String> getCurrencies(PropertyValues propertyValues)	{
		
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		
		ArrayList<String> currencies = new ArrayList<>();
		
		String sql = "SELECT currencyName FROM Currencies";
		
		try (PreparedStatement ps = con.prepareStatement(sql))	{
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String item = rs.getString("currencyName");
				currencies.add(item);
			}
			return currencies;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Map<String, List<String>> parseString(String data) {
        Map<String, List<String>> map = new HashMap<>();
        
        // Remove everything up to and including the question mark
        int index = data.indexOf('?');
        if (index == -1) {
            return map; // Return empty map if no '?' found
        }
        String keyValuePairs = data.substring(index + 1);

        // Split into key-value pairs
        String[] pairs = keyValuePairs.split("&");
        for (String pair : pairs) {
            if (pair.isEmpty()) {
                continue;
            }
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length < 2) {
                continue; // Skip if there is no '=' or no value after '='
            }
            String key = keyValue[0];
            String[] values = keyValue[1].split(",");
            
            // Add to the map, ensuring multiple values are stored in a list
            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).addAll(Arrays.asList(values));
        }
        
        return map;
    }
	
	
}
