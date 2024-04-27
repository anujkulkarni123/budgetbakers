package com.exavalu.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.exavalu.entities.Category;
import com.exavalu.entities.Duration;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.utilities.DbConnectionProvider;

public class TransactionService {
	// 0 => getTotalIncome, 1 => getTotalExpense
	public static double getTotalIncome(boolean getTotalExpense, PropertyValues propertyValues, Duration duration) {
		double totalAmount = 0d;
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		String sql;
		// if getTotalExpense is true, we want to find all expenses, instead of income
		String method = getTotalExpense ? "Expense" : "Income";

		if (duration.getLength() == -1) {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\"";

		} // we want to get total income from all time
		else if (duration.getLength() == -2) {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\""
					+ " AND Transactions.createdAt BETWEEN ? AND ?;";
		} else {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\""
					+ " AND createdAt >= DATE_SUB(NOW(), INTERVAL ? DAY)";
		} // get all queries between today and the specified number of days

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			if (duration.getLength() == -2) {
				String startTime = duration.getStartDate() + " 00:00:00";
				String endTime = duration.getEndDate() + " 00:00:00";
				System.out.println("start: " + startTime + " end: " + endTime);

				Timestamp startTimestamp = Timestamp.valueOf(startTime);
				Timestamp endTimestamp = Timestamp.valueOf(endTime);

				ps.setTimestamp(1, startTimestamp);
				ps.setTimestamp(2, endTimestamp);
			} else if (duration.getLength() != -1) {
				ps.setInt(1, duration.getLength());
			} // if its not -1 or -2, it should be a specified number of days
			try (ResultSet rs = ps.executeQuery()) {
				System.out.println("sql: " + sql);
				while (rs.next()) {
					if (method.equals("Income")) {
						totalAmount += rs.getDouble("transactionAmount");
					} else {
						totalAmount -= rs.getDouble("transactionAmount");
					}

				}
			}
		} catch (SQLException e) {
			// Log and handle the exception
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
		return totalAmount;
	}

	public static double getTotalFromCategory(Category category, PropertyValues propertyValues, Duration duration) {
		double totalAmount = 0d;
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		String sql;

		if (duration.getLength() == -1) {
			sql = "SELECT * FROM Transactions WHERE Transactions.category = \"" + category.getCategoryName() + "\"";

		} // we want to get total income from all time
		else if (duration.getLength() == -2) {
			sql = "SELECT * FROM Transactions WHERE Transactions.category = \"" + category.getCategoryName() + "\""
					+ " AND Transactions.createdAt BETWEEN ? AND ?;";
		} else {
			sql = "SELECT * FROM Transactions WHERE Transactions.category = \"" + category.getCategoryName() + "\""
					+ " AND createdAt >= DATE_SUB(NOW(), INTERVAL ? DAY)";
		} // get all queries between today and the specified number of days

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			if (duration.getLength() == -2) {
				String startTime = duration.getStartDate() + " 00:00:00";
				String endTime = duration.getEndDate() + " 00:00:00";
				Timestamp startTimestamp = Timestamp.valueOf(startTime);
				Timestamp endTimestamp = Timestamp.valueOf(endTime);
				System.out.println("start: " + startTime + " end: " + endTime);

				ps.setTimestamp(1, startTimestamp);
				ps.setTimestamp(2, endTimestamp);

			} else if (duration.getLength() != -1) {
				ps.setInt(1, duration.getLength());
			} // if its not -1 or -2, it should be a specified number of days
			
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					if (!category.getCategoryName().equals("Income")) {
						totalAmount -= rs.getDouble("transactionAmount");
					} else {
						totalAmount += rs.getDouble("transactionAmount");
					}

				}
			}
		} catch (SQLException e) {
			// Log and handle the exception
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
		return totalAmount;
	}

	public static Hashtable<String, Double> getCategoriesExpenses(PropertyValues propertyValues, Duration duration) {
		Hashtable<String, Double> expenseByCategories = new Hashtable<>();
		ArrayList<Category> categories = UserService.getCategories(propertyValues);
		for (Category category : categories) {
			expenseByCategories.put(category.getCategoryName(),
					getTotalFromCategory(category, propertyValues, duration));
		}
		return expenseByCategories;
	}

	public static double getAllAssets(PropertyValues propertyValues) {
		double allAssets = 0d;
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		String sql = "SELECT * FROM Accounts";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			System.out.println("SQL: " + ps);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					allAssets += rs.getInt("accountBalance");
				}
			}
		} catch (SQLException e) {
			// Log and handle the exception
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
		return allAssets;
	}

	public static JSONObject getCashFlow(PropertyValues propertyValues, Duration duration) {
		// time period HARD CODED RIGHT NOW for past 30 days
		// we should compare the amount of money from all our accounts, and then
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		double allAssets = getAllAssets(propertyValues);
		String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));

		Map<String, Double> data = new LinkedHashMap<>();
		data.put(currentDateTime, allAssets);
		// we will use this if days is an actual number of days; otherwise, we need to
		// use a different query
		String sql;
		if (duration.getLength() == -2) {
			sql = "SELECT * FROM Transactions WHERE Transactions.createdAt BETWEEN ? AND ? ORDER BY createdAt ASC;";
		} // custom range
		else if (duration.getLength() == -1) {
			sql = "SELECT * FROM Transactions ORDER BY createdAt ASC;";
		} else {
			sql = "SELECT * FROM Transactions WHERE createdAt >= DATE_SUB(NOW(), "
					+ "INTERVAL ? DAY) ORDER BY createdAt ASC;";
		}

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			if (duration.getLength() == -2) {
				String startTime = duration.getStartDate() + " 00:00:00";
				String endTime = duration.getEndDate() + " 00:00:00";
				System.out.println("start: " + startTime + " end: " + endTime);
				Timestamp startTimestamp = Timestamp.valueOf(startTime);
				Timestamp endTimestamp = Timestamp.valueOf(endTime);

				ps.setTimestamp(1, startTimestamp);
				ps.setTimestamp(2, endTimestamp);
			} else if (duration.getLength() != -1) {
				ps.setInt(1, duration.getLength());
			}

			try (ResultSet rs = ps.executeQuery()) {
				System.out.println("SQL Cash Flow: " + ps);
				while (rs.next()) {
					Double cashFlowAtMoment;
					if (rs.getString("transactionType").equals("Expense")) {
						cashFlowAtMoment = allAssets - rs.getDouble("transactionAmount");
					} else {
						cashFlowAtMoment = allAssets + rs.getDouble("transactionAmount");
					}

					data.put(rs.getString("createdAt"), cashFlowAtMoment);
				}
			}
		} catch (SQLException e) {
			// Log and handle the exception
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
		// we need to return this as a JSON object for the Chart
		JSONObject json = new JSONObject(data);
		return json;
	}

	public static JSONObject getIncome(boolean getExpense, PropertyValues propertyValues, Duration duration) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		Map<String, Double> data = new LinkedHashMap<>();
		String method = getExpense ? "Expense" : "Income";
		String sql;

		if (duration.getLength() == -2) {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\""
					+ " AND Transactions.createdAt BETWEEN ? AND ? ORDER BY createdAt ASC;";
		} // custom range
		else if (duration.getLength() == -1) {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\""
					+ "AND ORDER BY createdAt ASC;";
		} else {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\""
					+ " AND createdAt >= DATE_SUB(NOW(), INTERVAL ? DAY) ORDER BY createdAt ASC;";
		}
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			if (duration.getLength() == -2) {
				String startTime = duration.getStartDate() + " 00:00:00";
				String endTime = duration.getEndDate() + " 00:00:00";
				System.out.println("start: " + startTime + " end: " + endTime);
				Timestamp startTimestamp = Timestamp.valueOf(startTime);
				Timestamp endTimestamp = Timestamp.valueOf(endTime);

				ps.setTimestamp(1, startTimestamp);
				ps.setTimestamp(2, endTimestamp);
			} else if (duration.getLength() != -1) {
				ps.setInt(1, duration.getLength());
			}
			
			try (ResultSet rs = ps.executeQuery()) {
				System.out.println("SQL " + method + " query: " + ps);
				while (rs.next()) {
					if (getExpense) {
						System.out.println("Putting: " + rs.getString("createdAt") + ": " + -rs.getDouble("transactionAmount"));
						data.put(rs.getString("createdAt"), -rs.getDouble("transactionAmount"));
					} else {
						System.out.println("Putting: " + rs.getString("createdAt") + ": " + rs.getDouble("transactionAmount"));
						data.put(rs.getString("createdAt"), rs.getDouble("transactionAmount"));
					}
				}
			}

		} catch (SQLException e) {
			// Log and handle the exception
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
		JSONObject json = new JSONObject(data);
		return json;
	}

	public static JSONObject getCumulativeIncome(boolean getCumulativeExpense, PropertyValues propertyValues,
			Duration duration) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		Map<String, Double> data = new LinkedHashMap<>();
		String method = getCumulativeExpense ? "Expense" : "Income";
		String sql;
		if (duration.getLength() == -2) {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\""
					+ " AND Transactions.createdAt BETWEEN ? AND ? ORDER BY createdAt ASC;";
		} // custom range
		else if (duration.getLength() == -1) {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\""
					+ " AND ORDER BY createdAt ASC;";
		} else {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\""
					+ " AND createdAt >= DATE_SUB(NOW(), INTERVAL ? DAY) ORDER BY createdAt ASC;";
		}
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			if (duration.getLength() == -2) {
				String startTime = duration.getStartDate() + " 00:00:00";
				String endTime = duration.getEndDate() + " 00:00:00";
				System.out.println("start: " + startTime + " end: " + endTime);
				Timestamp startTimestamp = Timestamp.valueOf(startTime);
				Timestamp endTimestamp = Timestamp.valueOf(endTime);

				ps.setTimestamp(1, startTimestamp);
				ps.setTimestamp(2, endTimestamp);
			} else if (duration.getLength() != -1) {
				ps.setInt(1, duration.getLength());
			}
			
			try (ResultSet rs = ps.executeQuery()) {
				System.out.println("SQL with method: " + method + " = " + ps);
				double accumulatedTotal = 0;
				while (rs.next()) {
					if (getCumulativeExpense) {
						data.put(rs.getString("createdAt"), accumulatedTotal - rs.getDouble("transactionAmount"));
					} else {
						data.put(rs.getString("createdAt"), accumulatedTotal + rs.getDouble("transactionAmount"));
					}
				}
			}
		} catch (SQLException e) {
			// Log and handle the exception
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
		
		for (Map.Entry<String, Double> entry: data.entrySet()) {
			System.out.println(entry);
		}
		JSONObject json = new JSONObject(data);
		return json;
	}

}
