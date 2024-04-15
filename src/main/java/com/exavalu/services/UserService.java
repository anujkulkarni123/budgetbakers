package com.exavalu.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.exavalu.entities.User;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.utilities.DbConnectionProvider;

public class UserService {

	public static boolean validateUser(String emailAddress, String password, PropertyValues propertyValues) {
        
        System.out.println("Reached UserService.validateUser");

        // Get the database connection
        DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
        Connection con = dbConnectionProvider.getDbConnection(propertyValues);
        
        // If the connection is null, log the error and return false
        if (con == null) {
            System.out.println("Unable to create database connection.");
            return false; // Cannot proceed without a connection
        }
       
        // Define the SQL query
        String sql = "SELECT emailAddress, firstname, lastname FROM Users "
                + "WHERE emailAddress=? AND password=?;";

        // Attempt to execute the query
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, emailAddress);
            ps.setString(2, password);
            System.out.println("SQL: " + ps);

            // Execute the query and process the results
            try (ResultSet rs = ps.executeQuery()) {
                // If we find a user, return true
                if (rs.next()) {
                    System.out.println("User found: " + rs.getString("emailAddress"));
                    return true; // User found and validated
                }
                // If no rows returned, the user was not found
                return false;
            }
        } catch (SQLException e) {
            // Log and handle the exception
            e.printStackTrace();
            // Return false as the validation was not successful
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
