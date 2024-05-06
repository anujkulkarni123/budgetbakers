package com.exavalu.services;

import com.exavalu.entities.Record;  // Ensure you have a Record entity class
import com.exavalu.pojos.PropertyValues;
import com.exavalu.utilities.DbConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordsService {

    // Method to get records based on a dynamic filter
    public static List<Record> getRecords(String email ,Map<String, List<String>> queryParams, PropertyValues propertyValues) {
        List<Record> records = new ArrayList<>();

        // Get the database connection
        DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
        Connection con = dbConnectionProvider.getDbConnection(propertyValues);

        if (con == null) {
            System.out.println("Unable to create database connection.");
            return records; // Return empty list if connection is not established
        }
        String sql = null;
        
        if (queryParams != null)	{
        	sql = buildQuery(queryParams);
        } else {
        	sql = "SELECT * FROM Records";
        }
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            System.out.println("SQL Query: " + ps);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Record record = new Record();
                    record.setRecordId(rs.getInt("recordId"));
                    record.setAccountId(rs.getInt("accountId"));
                    record.setAmount(rs.getDouble("amount"));
                    //record.setDate(rs.getString("recordDate"));
                    record.setType(rs.getString("type"));
                    record.setCurrencyName(rs.getString("currencyName"));
                    record.setActive(rs.getString("active"));
                    record.setPaymentStatus(rs.getBoolean("paymentStatus"));
                    record.setCategory(rs.getString("category"));
                    record.setSubCategory(rs.getString("subCategory"));
                    record.setUserEmail(rs.getString("userEmail"));
                    record.setPayee(rs.getString("payee"));
                    record.setNote(rs.getString("note"));
                    records.add(record);
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

        return records;
    }
    
    public static String buildQuery(Map<String, List<String>> criteria) {
        StringBuilder query = new StringBuilder("SELECT * FROM records");

        if (!criteria.isEmpty()) {
            query.append(" WHERE");
            boolean firstCategory = true;
            
            // Iterate through each category and its subcategories
            for (Map.Entry<String, List<String>> entry : criteria.entrySet()) {
                if (!firstCategory) {
                    query.append(" OR"); // Separate multiple categories with OR
                }
                query.append(" (category = '").append(entry.getKey()).append("'");

                List<String> subcategories = entry.getValue();
                if (!subcategories.isEmpty()) {
                    query.append(" AND (");
                    boolean firstSubcategory = true;
                    
                    // Add subcategory conditions
                    for (String subcategory : subcategories) {
                        if (!firstSubcategory) {
                            query.append(" OR"); // Separate multiple subcategories with OR
                        }
                        query.append(" subcategory = '").append(subcategory).append("'");
                        firstSubcategory = false;
                    }
                    query.append(")"); // Close subcategory conditions
                }

                query.append(")"); // Close category condition
                firstCategory = false;
            }
        }
        
        return query.toString();
    }
    
    
    
}
