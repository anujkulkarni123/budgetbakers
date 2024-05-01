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

public class RecordsService {

    // Method to get records based on a dynamic filter
    public static List<Record> getRecords(String email ,String filterQuery, PropertyValues propertyValues) {
        List<Record> records = new ArrayList<>();

        // Get the database connection
        DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
        Connection con = dbConnectionProvider.getDbConnection(propertyValues);

        if (con == null) {
            System.out.println("Unable to create database connection.");
            return records; // Return empty list if connection is not established
        }

        String sql = "SELECT * FROM Records " + filterQuery;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            System.out.println("SQL Query: " + ps);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Record record = new Record();
                    record.setAccountId(rs.getInt("accountId"));
                    record.setAmount(rs.getDouble("amount"));
                    record.setDate(rs.getDate("date"));
                    record.setType(rs.getString("type"));
                    record.setCurrencyName(rs.getString("currencyType"));
                    record.setActive(rs.getString("active"));
                    
                    
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
}
