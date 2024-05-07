package com.exavalu.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import java.util.ArrayList;

import com.exavalu.entities.Category;
import com.exavalu.entities.Duration;
import com.exavalu.entities.Menu;
import com.exavalu.entities.Report;
import com.exavalu.entities.User;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.utilities.DbConnectionProvider;
import com.exavalu.utilities.EmailUtility;
import com.exavalu.utilities.MD5Hash;

public class UserService {

	public static User validateUser(String emailAddress, String password, PropertyValues propertyValues) {
        
        // Get the database connection
        DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
        Connection con = dbConnectionProvider.getDbConnection(propertyValues);
        
        String hashedPassword = "";
        
        User user = new User();
	    
	    System.out.println("Encoding password: " + password); // This will log null if password is indeed null
	    if (password != null) {
	        hashedPassword = MD5Hash.encode(password);
	        // Proceed with using hashedPassword
	    } else {
	        // Handle the case where password is null
	        System.out.println("Received null password for encoding");
	    }
        
        // If the connection is null, log the error and return false
        if (con == null) {
            System.out.println("Unable to create database connection.");
            return null; // Cannot proceed without a connection
        }
       
        // Define the SQL query
        String sql = "SELECT emailAddress, firstName, lastName, roleId, password, serialNumber, status, imagePath FROM Users "
                + "WHERE emailAddress=? AND password=? AND status=1;";

        // Attempt to execute the query
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, emailAddress);
            ps.setString(2, hashedPassword);
            System.out.println("SQL: " + ps);

            // Execute the query and process the results
            try (ResultSet rs = ps.executeQuery()) {
                // If we find a user, return true
                if (rs.next()) {
                    System.out.println("User found: " + rs.getString("emailAddress"));
                    user.setEmailAddress(emailAddress);
                    user.setRoleId(rs.getInt("roleId"));
                    user.setEmailAddress(rs.getString("emailAddress"));
                    user.setFirstName(rs.getString("firstName"));
                    user.setLastName(rs.getString("lastName"));
                    user.setStatus(rs.getBoolean("status"));
                    user.setSerialNumber(rs.getInt("serialNumber"));
                    user.setProfilePicturePath(rs.getString("imagePath"));
                    return user; // User found and validated
                }
                // If no rows returned, the user was not found
                return null;
            }
        } catch (SQLException e) {
            // Log and handle the exception
            e.printStackTrace();
            // Return false as the validation was not successful
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
        String hashedPassword = "";
        if (password != null) {
	        hashedPassword = MD5Hash.encode(password);
	        // Proceed with using hashedPassword
	    }
       
        // Define the SQL query
        String sql = "SELECT emailAddress, firstname, lastname, roleid, password, serialNumber, status, imagePath FROM Users "
                + "WHERE emailAddress=? AND password=?;";
     // Attempt to execute the query
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, emailAddress);
            ps.setString(2, hashedPassword);
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
	     
		String sql = "SELECT * FROM MENU INNER JOIN MENUROLES ON MENU.menuid = MENUROLES.menuid WHERE roleid = ? ";
		ArrayList<Menu> menuList = new ArrayList<>();
		System.out.println(roleId);
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

	public static boolean registerUser(User user, PropertyValues propertyValues) {
	    DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
	    Connection con = dbConnectionProvider.getDbConnection(propertyValues);
	    boolean success = false;
	    String emailType = "verify";
	    String hashedPassword = "";
	    
	    System.out.println("Encoding password: " + user.getPassword()); // This will log null if password is indeed null
	    if (user.getPassword() != null) {
	        hashedPassword = MD5Hash.encode(user.getPassword());
	        // Proceed with using hashedPassword
	    } else {
	        // Handle the case where password is null
	        System.out.println("Received null password for encoding");
	    }

	    try {
	        // Turn off auto-commit to manage transactions manually
	        con.setAutoCommit(false);

	        // User registration SQL statement
	        String userSql = "INSERT INTO users (emailAddress, password, firstName, lastName, status, roleid, verificationId) VALUES (?, ?, ?, ?, ?, ?, ?)";
	        PreparedStatement userPs = con.prepareStatement(userSql);
	        Random random = new Random();
	        int sixDigitNumber = 100000 + random.nextInt(900000);
	        userPs.setString(1, user.getEmailAddress());
	        userPs.setString(2,	hashedPassword);
	        userPs.setString(3, user.getFirstName()); 
	        userPs.setString(4, user.getLastName()); 
	        userPs.setInt(5, 0);
	        userPs.setInt(6, 1);
	        userPs.setInt(7, sixDigitNumber);
	        int userResult = userPs.executeUpdate();

	        if (userResult == 1 ) {
	            // Attempt to send verification email
	            try {
	                EmailUtility.sendVerificationEmail(user.getEmailAddress(), sixDigitNumber, emailType, propertyValues);
	                // If email is sent successfully, commit the transaction
	                con.commit();
	                success = true;
	            } catch (Exception e) {
	                // If email sending fails, roll back the transaction
	                con.rollback();
	                e.printStackTrace();
	                success = false;
	            }
	            
	        } else {
	            // If either insertion fails, roll back the transaction
	            con.rollback();
	            success = false;
	        }
	    } catch (SQLException e) {
	        try {
	            // Attempt to roll back the transaction on error
	            con.rollback();
	        
	        e.printStackTrace();
	        success = false;
	        } catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
	        	try {
	        		if (con != null) {
	        			con.setAutoCommit(true); // Reset auto-commit to its default state
	        			con.close();
	        		}
	        	} catch (SQLException e1) {
	            e1.printStackTrace();
	        	}
	        }
	    
	    }
		return success;
	}

	public static boolean verifyCode(int code, PropertyValues propertyValues) {
		// TODO Auto-generated method stub
		
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		
		boolean success = false;
		
		try	{
			
			con.setAutoCommit(false);
			
			String updateSql = "UPDATE Users SET status=1 WHERE verificationId=?;";
			try (PreparedStatement ps = con.prepareStatement(updateSql))	{
				ps.setInt(1, code);
				
				int rs = ps.executeUpdate();
				
				if (rs == 1) {
					con.commit();
					success = true;
				} else {
					con.rollback();
					success = false;
				}
			}
			
		} catch (SQLException e) {
			success = false;
			try {
				con.rollback(); // Attempt to roll back the transaction on error
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.setAutoCommit(true); // Reset auto-commit to its default state
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return success;
	}
	
	public static boolean sendResetPasswordEmail(String emailAddress, PropertyValues propertyValues)	{
		
		try {
			String emailType = "resetPassword";
			EmailUtility.sendVerificationEmail(emailAddress, 123456, emailType, propertyValues);
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

	public static boolean updatePassword(String email, String password, PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		
		System.out.println("reached");
		
		String sql = "UPDATE Users\n"
				+ "SET password=? \n"
				+ "WHERE emailAddress=?;";
		
		try (PreparedStatement ps = con.prepareStatement(sql))	{
			ps.setString(1, password);
			ps.setString(2, email);
			
			int rs = ps.executeUpdate();
			
			if (rs == 1) {
				System.out.println("PasswordUpdated");
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static ArrayList<Category> getCategories(PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		ArrayList<Category> categories = new ArrayList<>();
		String sql = "SELECT * FROM Categories ORDER BY categoryId";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			System.out.println("SQL: " + ps);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Category category = new Category();
					category.setCategoryIcon(rs.getString("categoryIcon"));
					category.setCategoryName(rs.getString("categoryName"));
					category.setCategoryId(rs.getInt("categoryId"));
					category.setCategoryColor(rs.getString("categoryColor"));
					categories.add(category);
				}
			}
		} catch (SQLException e) {
			// Log and handle the exception
			e.printStackTrace();
			// Return false as the validation was not successful
			return categories;
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
		return categories;
	}
	
	public static ArrayList<Report> getReports(PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		ArrayList<Report> reports = new ArrayList<>();
		String sql = "SELECT * FROM Reports ORDER BY analysisIndex";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			System.out.println("SQL: " + ps);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Report report = new Report();
					report.setAnalysisName(rs.getString("analysisName"));
					report.setAnalysisValue(rs.getInt("analysisIndex"));
					reports.add(report);
				}
			}
		} catch (SQLException e) {
			// Log and handle the exception
			e.printStackTrace();
			// Return false as the validation was not successful
			return reports;
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
		return reports;
	}
	
}

