package com.exavalu.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.exavalu.entities.Account;
import com.exavalu.entities.AccountType;
import com.exavalu.entities.Currency;
import com.exavalu.entities.User;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.utilities.DbConnectionProvider;


public class AccountService {
	public static boolean addAccountToDatabase(String userEmail, String name, int accountTypeId, float accountBalance, String currency, PropertyValues propertyValues) {
        DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
        Connection con = dbConnectionProvider.getDbConnection(propertyValues);

        if (con == null) {
            System.out.println("Unable to create database connection.");
            return false;
        }

        String sql = "INSERT INTO accounts (accountTypeId, accountBalance, currencyId, name, emailAddress) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, accountTypeId);
            ps.setFloat(2, accountBalance);
            ps.setString(3, currency);
            
            ps.setString(4, name);
            ps.setString(5,  userEmail);
            
            int result = ps.executeUpdate();

            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
	

	public static ArrayList<Account> getAccounts(String emailAddress,  PropertyValues propertyValues){
        DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
        Connection con = dbConnectionProvider.getDbConnection(propertyValues);
        ArrayList<Account> accountList = new ArrayList<Account>();
        // If the connection is null, log the error and return false
        if (con == null) {
            System.out.println("Unable to create database connection.");
            return null; // Cannot proceed without a connection
        }
       
        // Define the SQL query
        String sql = "SELECT accountId, accountTypeId, accountBalance, currencyId, name FROM accounts "
                + "WHERE emailAddress=?";
		
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, emailAddress);
            
            System.out.println("SQL: " + ps);

            try (ResultSet rs = ps.executeQuery()) {
                // Use a while loop instead of if statement to iterate over all rows
                while (rs.next()) {  
                    Account account = new Account();
                    account.setAccountId(rs.getInt("accountId"));
                    account.setAccountBalance(rs.getFloat("accountBalance"));
                    account.setAccountTypeId(rs.getInt("accountTypeId"));
                    account.setCurrencyId(rs.getInt("currencyId"));
                    account.setName(rs.getString("name"));
                    accountList.add(account);
                }
            }
        } catch (SQLException e) {
            // Log and handle the exception
            e.printStackTrace();
            System.out.println("error");
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
        System.out.println("THE ACCOUNTS ARE");
        System.out.println(accountList);
        return accountList;
    }
	

	public static ArrayList<AccountType> getAccountTypes(PropertyValues propertyValues) {
		ArrayList<AccountType> accountTypeList = new ArrayList<AccountType>();
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		String sql = "SELECT * FROM accounttype";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				AccountType account = new AccountType();
				account.setId(rs.getInt("id"));
				account.setType(rs.getString("type"));

				accountTypeList.add(account);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(accountTypeList);
		return accountTypeList;
	}
	
	
}