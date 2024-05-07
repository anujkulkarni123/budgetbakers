//package com.exavalu.services;
//
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Random;
//
//import java.util.ArrayList;
//
//import com.exavalu.entities.Currency;
//import com.exavalu.entities.Menu;
//import com.exavalu.entities.User;
//import com.exavalu.pojos.PropertyValues;
//import com.exavalu.utilities.DbConnectionProvider;
//import com.exavalu.utilities.EmailUtility;
//import com.exavalu.utilities.MD5Hash;
//
//public class RecordService {
//
//	private static double getAccountCurrencyConversionRate(int accountId, Connection con) {
//		String sql = "SELECT currencyId FROM accounts WHERE accountId = ?";
//		int currencyId = 0;
//
//		try (PreparedStatement ps = con.prepareStatement(sql)) {
//			ps.setInt(1, accountId);
//			try (ResultSet rs = ps.executeQuery()) {
//				if (rs.next()) {
//					currencyId = rs.getInt("currencyId");
//				} else {
//					return -1.0;
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return -1.0;
//		}
//
//		String currencySql = "SELECT conversionRate FROM currencies WHERE id = ?";
//		try (PreparedStatement ps = con.prepareStatement(currencySql)) {
//			ps.setInt(1, currencyId);
//			try (ResultSet rs = ps.executeQuery()) {
//				if (rs.next()) {
//					return rs.getDouble("conversionRate");
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return -1.0;
//		}
//
//		return -1.0;
//	}
//
//	private static double getConversionRateById(int currencyName, Connection con) {
//		System.out.println("CONVERSION NAME");
//		System.out.println(currencyName);
//		String sql = "SELECT conversionRate FROM currencies WHERE id = ?";
//
//		try (PreparedStatement ps = con.prepareStatement(sql)) {
//			ps.setInt(1, currencyName);
//			try (ResultSet rs = ps.executeQuery()) {
//				if (rs.next()) {
//					return rs.getDouble("conversionRate");
//				} else {
//					return -1.0;
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return -1.0;
//		}
//	}
//
//	public static boolean subtractFromAccount(int accountId, double amount, Connection con) {
//		String fetchSql = "SELECT accountBalance FROM accounts WHERE accountId = ?";
//		double currentBalance = 0.0;
//
//		try (PreparedStatement fetchPs = con.prepareStatement(fetchSql)) {
//			fetchPs.setInt(1, accountId);
//			try (ResultSet rs = fetchPs.executeQuery()) {
//				if (rs.next()) {
//					currentBalance = rs.getDouble("accountBalance");
//				} else {
//					System.out.println("Account not found.");
//					return false;
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return false;
//		}
//
//		System.out.println(currentBalance);
//		double newBalance = currentBalance - amount;
//		System.out.println(newBalance);
//		String updateSql = "UPDATE accounts SET accountBalance = ? WHERE accountId = ?";
//		try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
//			updatePs.setDouble(1, newBalance);
//			updatePs.setInt(2, accountId);
//			int affectedRows = updatePs.executeUpdate();
//			return affectedRows == 1;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	public static boolean addToAccount(int accountId, double amount, Connection con) {
//
//		String fetchSql = "SELECT accountBalance FROM accounts WHERE accountId = ?";
//		double currentBalance = 0.0;
//		try (PreparedStatement fetchPs = con.prepareStatement(fetchSql)) {
//			fetchPs.setInt(1, accountId);
//			try (ResultSet rs = fetchPs.executeQuery()) {
//				if (rs.next()) {
//					currentBalance = rs.getDouble("accountBalance");
//				} else {
//					System.out.println("Account not found.");
//					return false;
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return false;
//		}
//
//		double newBalance = currentBalance + amount;
//
//		String updateSql = "UPDATE accounts SET accountBalance = ? WHERE accountId = ?";
//		try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
//			updatePs.setDouble(1, newBalance);
//			updatePs.setInt(2, accountId);
//			int affectedRows = updatePs.executeUpdate();
//			return affectedRows == 1;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	public static boolean HandleExpense(String userEmail, int currency, int accountId, double amount,
//			PropertyValues propertyValues) {
//		System.out.println("IN HANDLE EXPENSE");
//		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
//		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
//
//		if (con == null) {
//			System.out.println("Unable to create database connection.");
//			return false;
//		}
//
//		double accountConversionRate = getAccountCurrencyConversionRate(accountId, con);
//		double expenseConversionRate = getConversionRateById(currency, con);
//		
//		System.out.println("HERE");
//		System.out.println(amount);
//		System.out.println(expenseConversionRate);
//		System.out.println(accountConversionRate);
//		System.out.println((expenseConversionRate / accountConversionRate));
//		double actualConversion = (expenseConversionRate / accountConversionRate)*amount;
//
//		boolean modify = subtractFromAccount(accountId, actualConversion, con);
//
//		if (modify) {
//			return true;
//		}
//		return false;
//	}
//
//	public static boolean HandleIncome(String userEmail, int currency, int accountId, double amount,
//			PropertyValues propertyValues) {
//		System.out.println("IN HANDLE INCOME");
//		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
//		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
//		double accountConversionRate = getAccountCurrencyConversionRate(accountId, con);
//		double incomeConversionRate = getConversionRateById(currency, con);
//
//		double actualConversion = (incomeConversionRate / accountConversionRate)*amount;
//
//		boolean modify = addToAccount(accountId, actualConversion, con);
//
//		if (modify) {
//			return true;
//		}
//		return false;
//	}
//
//	public static boolean HandleTransfer(String userEmail, int currency, int fromAccountId, int toAccountId,
//			double amount, PropertyValues propertyValues) {
//		System.out.println("IN HANDLE TRANSER");
//		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
//		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
//		double fromAccountConversionRate = getAccountCurrencyConversionRate(fromAccountId, con);
//		double toAccountConversionRate = getAccountCurrencyConversionRate(toAccountId, con);
//		double transferConversionRate = getConversionRateById(currency, con);
//		
//		double fromActualConversion = (transferConversionRate / fromAccountConversionRate)*amount;
//		double toActualConversion = (transferConversionRate / toAccountConversionRate)*amount;
//		
//		boolean modify1 = subtractFromAccount(fromAccountId, fromActualConversion, con);
//		boolean modify2 = addToAccount(toAccountId, toActualConversion, con);
//		
//		if (modify1 && modify2) {
//			return true;
//		}
//		return false;
//	}
//
//	public static boolean handleSaveRecord(int accountId, double amount, Date recordDate,
//			int currencyName, int type, String paymentStatus, int secondAccountId, String userEmail,
//			PropertyValues propertyValues) {
//		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
//		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
//		
//		String insertSQL = "INSERT INTO records (accountId, amount, recordDate, currencyName, type, secondAccountId, userEmail) VALUES (?, ?, ?, ?, ?, ?, ?)";
//		try {
//            PreparedStatement preparedStatement = con.prepareStatement(insertSQL);
//            preparedStatement.setInt(1, accountId);
//            preparedStatement.setDouble(2, amount);
//            preparedStatement.setDate(3, recordDate);
//            preparedStatement.setInt(4, currencyName);
//            preparedStatement.setInt(5, type);
//            preparedStatement.setInt(6, secondAccountId);
//            preparedStatement.setString(7, userEmail);
// 
//            int rowsAffected = preparedStatement.executeUpdate();
//            if (rowsAffected > 0) {
//	            return true;
//            } else {
//                return false;
//            }
//		}catch (SQLException ex) {
//            ex.printStackTrace();
//            return false;
//        }
//	}
//
//}