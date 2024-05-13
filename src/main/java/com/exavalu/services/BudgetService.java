package com.exavalu.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.exavalu.entities.BudgetItem;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.utilities.DbConnectionProvider;

public class BudgetService {

    // Method to get budget items based on a dynamic filter
    public static List<BudgetItem> getBudgetItems(PropertyValues propertyValues) {
        List<BudgetItem> budgetItems = new ArrayList<>();

        // Get the database connection
        DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
        Connection con = dbConnectionProvider.getDbConnection(propertyValues);

        if (con == null) {
            System.out.println("Unable to create database connection.");
            return budgetItems; // Return empty list if connection is not established
        }
        String sql = "SELECT * FROM Budget"; // Adjust table name as necessary
        

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            System.out.println("SQL Query: " + sql);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BudgetItem item = new BudgetItem();
                    item.setId(rs.getInt("id"));
                    item.setCategory(rs.getString("category"));
                    item.setBudgetLimit(rs.getDouble("budgetLimit"));
                    item.setCurrency(rs.getString("currency"));
                    budgetItems.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

        return budgetItems;
    }

    public static boolean updateBudgetItem(BudgetItem item, PropertyValues propertyValues) {
        // Get the database connection
    	
    	System.out.println("rteached" + item.getCategory());
    	
        DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
        Connection con = dbConnectionProvider.getDbConnection(propertyValues);

        

        // Define the SQL query to update the budget item
        String query = "UPDATE Budget SET  budgetLimit = ?, currency= ? WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            // Set values from the BudgetItem object
            stmt.setDouble(1, item.getBudgetLimit());
            stmt.setString(2, item.getCurrency());
            stmt.setInt(3, item.getId());

            // Execute the update
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;  // Return true if the update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
}