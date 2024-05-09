package com.exavalu.services;

import com.exavalu.entities.Record;  // Ensure you have a Record entity class
import com.exavalu.pojos.PropertyValues;
import com.exavalu.utilities.DbConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
    
    private static double getAccountCurrencyConversionRate(int accountId, Connection con) {
		String sql = "SELECT currencyId FROM accounts WHERE accountId = ?";
		int currencyId = 0;

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, accountId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					currencyId = rs.getInt("currencyId");
				} else {
					return -1.0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1.0;
		}

		String currencySql = "SELECT conversionRate FROM currencies WHERE id = ?";
		try (PreparedStatement ps = con.prepareStatement(currencySql)) {
			ps.setInt(1, currencyId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getDouble("conversionRate");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1.0;
		}

		return -1.0;
	}

	private static double getConversionRateById(int currencyName, Connection con) {
		System.out.println("CONVERSION NAME");
		System.out.println(currencyName);
		String sql = "SELECT conversionRate FROM currencies WHERE id = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, currencyName);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getDouble("conversionRate");
				} else {
					return -1.0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1.0;
		}
	}

	public static boolean subtractFromAccount(int accountId, double amount, Connection con) {
		String fetchSql = "SELECT accountBalance FROM accounts WHERE accountId = ?";
		double currentBalance = 0.0;

		try (PreparedStatement fetchPs = con.prepareStatement(fetchSql)) {
			fetchPs.setInt(1, accountId);
			try (ResultSet rs = fetchPs.executeQuery()) {
				if (rs.next()) {
					currentBalance = rs.getDouble("accountBalance");
				} else {
					System.out.println("Account not found.");
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		System.out.println(currentBalance);
		double newBalance = currentBalance - amount;
		System.out.println(newBalance);
		String updateSql = "UPDATE accounts SET accountBalance = ? WHERE accountId = ?";
		try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
			updatePs.setDouble(1, newBalance);
			updatePs.setInt(2, accountId);
			int affectedRows = updatePs.executeUpdate();
			return affectedRows == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean addToAccount(int accountId, double amount, Connection con) {

		String fetchSql = "SELECT accountBalance FROM accounts WHERE accountId = ?";
		double currentBalance = 0.0;
		try (PreparedStatement fetchPs = con.prepareStatement(fetchSql)) {
			fetchPs.setInt(1, accountId);
			try (ResultSet rs = fetchPs.executeQuery()) {
				if (rs.next()) {
					currentBalance = rs.getDouble("accountBalance");
				} else {
					System.out.println("Account not found.");
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		double newBalance = currentBalance + amount;

		String updateSql = "UPDATE accounts SET accountBalance = ? WHERE accountId = ?";
		try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
			updatePs.setDouble(1, newBalance);
			updatePs.setInt(2, accountId);
			int affectedRows = updatePs.executeUpdate();
			return affectedRows == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean HandleExpense(String userEmail, int currency, int accountId, double amount,
			PropertyValues propertyValues) {
		System.out.println("IN HANDLE EXPENSE");
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);

		if (con == null) {
			System.out.println("Unable to create database connection.");
			return false;
		}

		double accountConversionRate = getAccountCurrencyConversionRate(accountId, con);
		double expenseConversionRate = getConversionRateById(currency, con);
		
		System.out.println("HERE");
		System.out.println(amount);
		System.out.println(expenseConversionRate);
		System.out.println(accountConversionRate);
		System.out.println((expenseConversionRate / accountConversionRate));
		double actualConversion = (expenseConversionRate / accountConversionRate)*amount;

		boolean modify = subtractFromAccount(accountId, actualConversion, con);

		if (modify) {
			return true;
		}
		return false;
	}

	public static boolean HandleIncome(String userEmail, int currency, int accountId, double amount,
			PropertyValues propertyValues) {
		System.out.println("IN HANDLE INCOME");
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		double accountConversionRate = getAccountCurrencyConversionRate(accountId, con);
		double incomeConversionRate = getConversionRateById(currency, con);

		double actualConversion = (incomeConversionRate / accountConversionRate)*amount;

		boolean modify = addToAccount(accountId, actualConversion, con);

		if (modify) {
			return true;
		}
		return false;
	}

	public static boolean HandleTransfer(String userEmail, int currency, int fromAccountId, int toAccountId,
			double amount, PropertyValues propertyValues) {
		System.out.println("IN HANDLE TRANSER");
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		double fromAccountConversionRate = getAccountCurrencyConversionRate(fromAccountId, con);
		double toAccountConversionRate = getAccountCurrencyConversionRate(toAccountId, con);
		double transferConversionRate = getConversionRateById(currency, con);
		
		double fromActualConversion = (transferConversionRate / fromAccountConversionRate)*amount;
		double toActualConversion = (transferConversionRate / toAccountConversionRate)*amount;
		
		boolean modify1 = subtractFromAccount(fromAccountId, fromActualConversion, con);
		boolean modify2 = addToAccount(toAccountId, toActualConversion, con);
		
		if (modify1 && modify2) {
			return true;
		}
		return false;
	}

	public static boolean handleSaveRecord(int accountId, double amount, Date recordDate,
			int currencyName, int type, String paymentStatus, int secondAccountId, String userEmail,
			PropertyValues propertyValues, String category, String subCategory) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		
		String insertSQL = "INSERT INTO records (accountId, amount, recordDate, currencyName, type, secondAccountId, userEmail, active, paymentStatus, category, subCategory) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
            PreparedStatement preparedStatement = con.prepareStatement(insertSQL);
            preparedStatement.setInt(1, accountId);
            preparedStatement.setDouble(2, amount);
            preparedStatement.setDate(3, (java.sql.Date) recordDate);
            preparedStatement.setInt(4, currencyName);
            preparedStatement.setInt(5, type);
            preparedStatement.setInt(6, secondAccountId);
            preparedStatement.setString(7, userEmail);
            preparedStatement.setInt(8, 1);
            preparedStatement.setInt(9, 1);
            preparedStatement.setString(10, category);
            preparedStatement.setString(11, subCategory);
 
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
	            return true;
            } else {
                return false;
            }
		}catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
	}
    
    
    
}
