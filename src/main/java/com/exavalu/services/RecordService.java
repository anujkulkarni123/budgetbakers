package com.exavalu.services;

import java.sql.Connection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.exavalu.entities.Currency;
import com.exavalu.entities.Menu;
import com.exavalu.entities.User;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.utilities.DbConnectionProvider;
import com.exavalu.utilities.EmailUtility;
import com.exavalu.utilities.MD5Hash;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RecordService {

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
	
	private static String getAccountCurrencyName(int accountId, Connection con) {
		String sql = "SELECT currencyId FROM accounts WHERE accountId = ?";
		int currencyId = 0;

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, accountId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					currencyId = rs.getInt("currencyId");
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		String currencySql = "SELECT currencyName FROM currencies WHERE id = ?";
		try (PreparedStatement ps = con.prepareStatement(currencySql)) {
			ps.setInt(1, currencyId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("currencyName");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return null;
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
	
	private static String getNameById(int currencyName, Connection con) {
		System.out.println("CONVERSION NAME");
		System.out.println(currencyName);
		String sql = "SELECT currencyName FROM currencies WHERE id = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, currencyName);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("currencyName");
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
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
	
    private static double getExchangeRate(String fromCurrency, String toCurrency) {
        String urlString = "https://openexchangerates.org/api/latest.json?app_id=30d9f6c91dc34128871f587edc7de213";
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            if (status != 200) {
                throw new RuntimeException("HTTP GET Request Failed with Error code : " + status);
            }

            StringBuilder content = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
            con.disconnect();

            JsonObject jsonObject = JsonParser.parseString(content.toString()).getAsJsonObject();
            JsonObject rates = jsonObject.getAsJsonObject("rates");

            double fromRate = rates.get(fromCurrency).getAsDouble();
            double toRate = rates.get(toCurrency).getAsDouble();

            System.out.println("EXCHANGE RATE");
            System.out.println(fromCurrency + ": " + fromRate);
            System.out.println(toCurrency + ": " + toRate);
            System.out.println("Conversion Rate: " + (toRate / fromRate));

            return toRate / fromRate;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
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

		//double accountConversionRate = getAccountCurrencyConversionRate(accountId, con);
		//double expenseConversionRate = getConversionRateById(currency, con);
		//double actualConversion = (expenseConversionRate / accountConversionRate)*amount;
		String cur1 = getNameById(currency, con);
		String cur2 = getAccountCurrencyName(accountId, con);
		double actualConversion = (getExchangeRate(cur1, cur2))*amount;
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
		//double accountConversionRate = getAccountCurrencyConversionRate(accountId, con);
		//double incomeConversionRate = getConversionRateById(currency, con);

		//double actualConversion = (incomeConversionRate / accountConversionRate)*amount;
		String cur1 = getNameById(currency, con);
		String cur2 = getAccountCurrencyName(accountId, con);
		double actualConversion = (getExchangeRate(cur1, cur2))*amount;

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
		//double fromAccountConversionRate = getAccountCurrencyConversionRate(fromAccountId, con);
		//double toAccountConversionRate = getAccountCurrencyConversionRate(toAccountId, con);
		//double transferConversionRate = getConversionRateById(currency, con);
		
		//double fromActualConversion = (transferConversionRate / fromAccountConversionRate)*amount;
		//double toActualConversion = (transferConversionRate / toAccountConversionRate)*amount;
		
		//boolean modify1 = subtractFromAccount(fromAccountId, fromActualConversion, con);
		//boolean modify2 = addToAccount(toAccountId, toActualConversion, con);
		
		
		String transferCurr = getNameById(currency, con);
		String firstCurr = getAccountCurrencyName(fromAccountId, con);
		String secondCurr = getAccountCurrencyName(toAccountId, con);
		
		double actualConversionFirst = (getExchangeRate(transferCurr, firstCurr))*amount;
		double actualConversionSecond = (getExchangeRate(transferCurr, secondCurr))*amount;
		
		boolean modify1 = subtractFromAccount(fromAccountId, actualConversionFirst, con);
		boolean modify2 = addToAccount(toAccountId, actualConversionSecond, con);
		
		if (modify1 && modify2) {
			return true;
		}
		return false;
	}

	public static boolean handleSaveRecord(int accountId, double amount, Date recordDate,
			int currencyName, int type, String paymentStatus, int secondAccountId, String userEmail,
			PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		
		String insertSQL = "INSERT INTO records (accountId, amount, recordDate, currencyName, type, secondAccountId, userEmail) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try {
            PreparedStatement preparedStatement = con.prepareStatement(insertSQL);
            preparedStatement.setInt(1, accountId);
            preparedStatement.setDouble(2, amount);
            preparedStatement.setDate(3, recordDate);
            preparedStatement.setInt(4, currencyName);
            preparedStatement.setInt(5, type);
            preparedStatement.setInt(6, secondAccountId);
            preparedStatement.setString(7, userEmail);
 
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