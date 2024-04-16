package com.exavalu.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.exavalu.entities.Menu;
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
	
	public static User getUser(String emailAddress, String password, PropertyValues propertyValues) {
		User user = null;
		System.out.println("Reached UserService.getUser");

        // Get the database connection
        DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
        Connection con = dbConnectionProvider.getDbConnection(propertyValues);
        
        // If the connection is null, log the error and return false
        if (con == null) {
            System.out.println("Unable to create database connection.");
            return user; // Cannot proceed without a connection
        }
       
        // Define the SQL query
        String sql = "SELECT emailAddress, firstname, lastname, roleid, password, serialNumber, status, imagePath FROM Users "
                + "WHERE emailAddress=? AND password=?;";
     // Attempt to execute the query
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, emailAddress);
            ps.setString(2, password);
            System.out.println("SQL: " + ps);

            // Execute the query and process the results
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                	// if we can find a user, then pass its details
                    user = new User();
                	System.out.println("User found: " + rs.getString("emailAddress"));
                    user.setFirstName(rs.getString("firstName"));
                    user.setLastName(rs.getString("lastName"));
                    user.setEmailAddress(emailAddress);
                    user.setPassword(password);
                    user.setRoleId(rs.getInt("roleid"));
                    user.setStatus(rs.getInt("status"));
                    user.setProfilePicturePath(rs.getString("imagePath")+ "/" 
                    + String.valueOf(rs.getInt("serialNumber")) + ".jpg");
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
		
		return user;
	}
	
	public static ArrayList<Menu> getMenu(int roleId, PropertyValues propertyValues) {
		 DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
	     Connection con = dbConnectionProvider.getDbConnection(propertyValues);
	     
		String sql = "SELECT * FROM MENU INNER JOIN MENU_ROLE ON MENU.menuid = MENU_ROLE.menuid WHERE roleid = ? ";
		ArrayList<Menu> menuList = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, roleId);
			try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                	Menu menu = new Menu();
                	menu.setMenuId(rs.getInt("menuid"));
                	menu.setMenuLink(rs.getString("menuLink"));
                	menu.setMenuName(rs.getString("menuName"));
                	menuList.add(menu);
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
		
		System.out.print("Menu contains: [");
		for (Menu menu : menuList) {
			System.out.print(menu.getMenuName() + ",");
			
		}
		System.out.println("]");
		
		return menuList;
	}

}
