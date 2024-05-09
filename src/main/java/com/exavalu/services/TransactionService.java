package com.exavalu.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.exavalu.entities.Account;
import com.exavalu.entities.Category;
import com.exavalu.entities.Duration;
import com.exavalu.entities.RecordState;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.utilities.DbConnectionProvider;
import com.google.gson.Gson;

public class TransactionService {

	private static String emailQuery(String email) {
		String query = " AND EXISTS (SELECT 1 FROM Records WHERE Transactions.accountId = Records.accountId AND Records.userEmail = \""
				+ email + "\")";
		return query;
	}

	// 0 => getTotalIncome, 1 => getTotalExpense
	public static double getTotalIncome(boolean getTotalExpense, PropertyValues propertyValues, Duration duration,
			String filterQuery, String emailAddress) {
		double totalAmount = 0d;
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		String sql;
		// if getTotalExpense is true, we want to find all expenses, instead of income
		String method = getTotalExpense ? "Expense" : "Income";
		String caseEmptyFilterQuery = filterQuery.isEmpty() ? "" : " AND " + filterQuery;
		if (duration.getLength() == -1) {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\" "
					+ caseEmptyFilterQuery + emailQuery(emailAddress);

		} // we want to get total income from all time
		else if (duration.getLength() == -2) {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\""
					+ " AND Transactions.createdAt BETWEEN ? AND ?" + caseEmptyFilterQuery + emailQuery(emailAddress);
		} else {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\""
					+ " AND createdAt >= DATE_SUB(NOW(), INTERVAL ? DAY)" + caseEmptyFilterQuery
					+ emailQuery(emailAddress);
		} // get all queries between today and the specified number of days
		System.out.println(sql);
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

	// we nned to also handle the all cases!!!
	public static double getTotalFromCategory(Category category, PropertyValues propertyValues, Duration duration,
			String filterQuery, String emailAddress) {
		double totalAmount = 0d;
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		String caseEmptyFilterQuery = filterQuery.isEmpty() ? "" : " AND " + filterQuery;
		String sql;

		if (duration.getLength() == -1) {
			sql = "SELECT * FROM Transactions WHERE Transactions.category = \"" + category.getCategoryName() + "\" "
					+ caseEmptyFilterQuery + " " + emailQuery(emailAddress);

		} // we want to get total income from all time
		else if (duration.getLength() == -2) {
			sql = "SELECT * FROM Transactions WHERE Transactions.category = \"" + category.getCategoryName() + "\" "
					+ caseEmptyFilterQuery + " AND Transactions.createdAt BETWEEN ? AND ? " + emailQuery(emailAddress);
		} else {
			sql = "SELECT * FROM Transactions WHERE Transactions.category = \"" + category.getCategoryName() + "\" "
					+ caseEmptyFilterQuery + " AND createdAt >= DATE_SUB(NOW(), INTERVAL ? DAY) "
					+ emailQuery(emailAddress);
		} // get all queries between today and the specified number of days
		System.out.println(sql);
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

	public static Hashtable<String, Double> getCategoriesExpenses(PropertyValues propertyValues, Duration duration,
			String filterQuery, String emailAddress) {
		Hashtable<String, Double> expenseByCategories = new Hashtable<>();
		ArrayList<Category> categories = UserService.getCategories(propertyValues);
		for (Category category : categories) {
			expenseByCategories.put(category.getCategoryName(),
					getTotalFromCategory(category, propertyValues, duration, filterQuery, emailAddress));
		}
		return expenseByCategories;
	}

	public static ArrayList<String> getPaymentTypes(PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		ArrayList<String> paymentTypes = new ArrayList<>();
		String sql = "SELECT * FROM PaymentTypes";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery(sql)) {
				while (rs.next()) {
					paymentTypes.add(rs.getString("type"));
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return paymentTypes;
	}

	public static double getAllAssets(PropertyValues propertyValues, String emailAddress) {
		double allAssets = 0d;
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		String sql = "SELECT * FROM Accounts " + emailQuery(emailAddress) ;
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

	public static Duration getPreviousPeriod(Duration duration) {
		Duration previousPeriod = new Duration();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		previousPeriod.setLength(-2);

		if (duration.getLength() == -1) {
			return duration;
		} else if (duration.getLength() == -2) {
			// the starting day in duration becomes the end date of the previous period
			previousPeriod.setEndDate(duration.getStartDate());

			// we need to calculate the number of days between these two dates
			LocalDateTime currentStartDate = LocalDateTime.parse(duration.getStartDate() + "T00:00:00");
			LocalDateTime currentEndDate = LocalDateTime.parse(duration.getEndDate() + "T00:00:00");
			long daysBetween = ChronoUnit.DAYS.between(currentStartDate, currentEndDate);
			// now we need to find how many days back from our current endDate to determine
			// the startDate
			LocalDateTime dateXDaysBefore = currentStartDate.minusDays(daysBetween);
			previousPeriod.setStartDate(formatter.format(dateXDaysBefore));
		} else {
			// first we need to find the starting date
			LocalDateTime today = LocalDateTime.now();
			LocalDateTime endDateXDaysBefore = today.minusDays(duration.getLength());
			previousPeriod.setEndDate(formatter.format(endDateXDaysBefore));
			LocalDateTime startDateXDaysBefore = endDateXDaysBefore.minusDays(duration.getLength());
			previousPeriod.setStartDate(formatter.format(startDateXDaysBefore));
		}

		System.out.println("current date information: " + duration.getLength() + " " + duration.getStartDate() + " "
				+ duration.getEndDate());
		System.out.println("past period date information: " + previousPeriod.getLength() + " "
				+ previousPeriod.getStartDate() + " " + previousPeriod.getEndDate());
		return previousPeriod;
	}

	public static String getCumulativeCashFlow(PropertyValues propertyValues, Duration duration, String filterQuery,
			String emailAddress) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		String caseEmptyFilterQuery = filterQuery.isEmpty() ? "" : " AND " + filterQuery;
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		double allAssets = getAllAssets(propertyValues, emailAddress);
		String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));

		Map<String, Double> data = new LinkedHashMap<>();
		data.put(currentDateTime, allAssets);
		String sql;
		if (duration.getLength() == -2) {
			sql = "SELECT * FROM Transactions WHERE Transactions.createdAt BETWEEN ? AND ? " + caseEmptyFilterQuery
					+ " " + emailQuery(emailAddress) + " ORDER BY createdAt ASC ";
		} // custom range
		else if (duration.getLength() == -1) {
			sql = "SELECT * FROM Transactions ORDER BY createdAt ASC " + caseEmptyFilterQuery + " "
					+ emailQuery(emailAddress);
		} else {
			sql = "SELECT * FROM Transactions WHERE createdAt >= DATE_SUB(NOW(), " + "INTERVAL ? DAY) " + " "
					+ caseEmptyFilterQuery + " " + emailQuery(emailAddress) + " ORDER BY createdAt ASC;";
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
						cashFlowAtMoment = -allAssets - rs.getDouble("transactionAmount");
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
		Gson gson = new Gson();
		String json = gson.toJson(data, LinkedHashMap.class);
		return json;
	}

	public static String getCashFlow(PropertyValues propertyValues, Duration duration, String filterQuery,
			String emailAddress) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		String caseEmptyFilterQuery = filterQuery.isEmpty() ? "" : " AND " + filterQuery;
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		double allAssets = getAllAssets(propertyValues, emailAddress);
		String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));

		Map<String, Double> data = new LinkedHashMap<>();
		data.put(currentDateTime, allAssets);

		String sql;
		if (duration.getLength() == -2) {
			sql = "SELECT * FROM Transactions WHERE Transactions.createdAt BETWEEN ? AND ? " + caseEmptyFilterQuery
					+ emailQuery(emailAddress) + " ORDER BY createdAt ASC AND ";
		} // custom range
		else if (duration.getLength() == -1) {
			sql = "SELECT * FROM Transactions WHERE " + caseEmptyFilterQuery + " " + emailQuery(emailAddress)
					+ " ORDER BY createdAt ASC ";
		} else {
			sql = "SELECT * FROM Transactions WHERE createdAt >= DATE_SUB(NOW(), " + "INTERVAL ? DAY) "
					+ caseEmptyFilterQuery + " " + emailQuery(emailAddress) + " ORDER BY createdAt ASC;";
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
		Gson gson = new Gson();
		String json = gson.toJson(data, LinkedHashMap.class);
		return json;
	}

	public static String getIncome(boolean getExpense, PropertyValues propertyValues, Duration duration,
			String filterQuery, String emailAddress) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		String caseEmptyFilterQuery = filterQuery.isEmpty() ? "" : " AND " + filterQuery;
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		Map<String, Double> data = new LinkedHashMap<>();
		String method = getExpense ? "Expense" : "Income";
		String sql;

		if (duration.getLength() == -2) {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\""
					+ " AND Transactions.createdAt BETWEEN ? AND ? " + caseEmptyFilterQuery + " "
					+ emailQuery(emailAddress) + "ORDER BY createdAt ASC;";
		} // custom range
		else if (duration.getLength() == -1) {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\"" + " "
					+ caseEmptyFilterQuery + " " + emailQuery(emailAddress) + " ORDER BY createdAt ASC;";
		} else {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\""
					+ " AND createdAt >= DATE_SUB(NOW(), INTERVAL ? DAY) " + caseEmptyFilterQuery + " "
					+ emailQuery(emailAddress) + " ORDER BY createdAt ASC;";
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
						System.out.println(
								"Putting: " + rs.getString("createdAt") + ": " + -rs.getDouble("transactionAmount"));
						data.put(rs.getString("createdAt"), -rs.getDouble("transactionAmount"));
					} else {
						System.out.println(
								"Putting: " + rs.getString("createdAt") + ": " + rs.getDouble("transactionAmount"));
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
		Gson gson = new Gson();
		String json = gson.toJson(data, LinkedHashMap.class);
		return json;
	}

	private static double getBalanceOnDay(String day, String emailAddress, PropertyValues propertyValues,
			String filterQuery) {
		String caseEmptyFilterQuery = filterQuery.isEmpty() ? "" : " AND " + filterQuery;
		String sql = "SELECT * FROM Transactions WHERE DATE(createdAt) = '?' " + emailQuery(emailAddress) 
				+ caseEmptyFilterQuery;
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		double moneyflow = 0;
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, day);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					moneyflow += rs.getDouble("transactionAmount");
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
		return moneyflow;
	}

	public static String getBalance(Duration duration, String filterQuery, PropertyValues propertyValues,
			String emailAddress) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		String caseEmptyFilterQuery = filterQuery.isEmpty() ? "" : " AND " + filterQuery;
		Map<String, Double> data = new LinkedHashMap<>();
		String sql;

		if (duration.getLength() == -2) {
			sql = "SELECT * FROM Transactions WHERE Transactions.createdAt BETWEEN ? AND ? " + caseEmptyFilterQuery
					+ " " + emailQuery(emailAddress) + "ORDER BY createdAt ASC;";
		} else if (duration.getLength() == -1) {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType IN ('Expense', 'Income') "
					+ caseEmptyFilterQuery + " " + emailQuery(emailAddress) + " ORDER BY createdAt ASC;";
		} else {
			sql = "SELECT * FROM Transactions WHERE createdAt >= DATE_SUB(NOW(), INTERVAL ? DAY) "
					+ caseEmptyFilterQuery + " " + emailQuery(emailAddress) + " ORDER BY createdAt ASC;";
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
				while (rs.next()) {
					data.put(rs.getString("createdAt"),
							getBalanceOnDay(rs.getString("createdAt"), emailAddress, propertyValues, filterQuery));
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
		Gson gson = new Gson();
		String json = gson.toJson(data, LinkedHashMap.class);
		return json;
	}

	public static String getCumulativeIncome(boolean getCumulativeExpense, PropertyValues propertyValues,
			Duration duration, String filterQuery, String emailAddress) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		String caseEmptyFilterQuery = filterQuery.isEmpty() ? "" : " AND " + filterQuery;
		Map<String, Double> data = new LinkedHashMap<>();
		String method = getCumulativeExpense ? "Expense" : "Income";
		String sql;
		if (duration.getLength() == -2) {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\""
					+ " AND Transactions.createdAt BETWEEN ? AND ? " + caseEmptyFilterQuery + " "
					+ emailQuery(emailAddress) + " ORDER BY createdAt ASC;";
		} // custom range
		else if (duration.getLength() == -1) {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = ? \"" + method + "\" "
					+ caseEmptyFilterQuery + " " + emailQuery(emailAddress) + " ORDER BY createdAt ASC;";
		} else {
			sql = "SELECT * FROM Transactions WHERE Transactions.transactionType = \"" + method + "\" "
					+ caseEmptyFilterQuery + " AND createdAt >= DATE_SUB(NOW(), INTERVAL ? DAY) "
					+ emailQuery(emailAddress) + " ORDER BY createdAt ASC;";
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
			} else {
				ps.setString(1, method);
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
		Gson gson = new Gson();
		String json = gson.toJson(data, LinkedHashMap.class);
		return json;
	}

	public static ArrayList<Account> getAccounts(PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		ArrayList<Account> accounts = new ArrayList<>();

		String sql = "SELECT * FROM Accounts";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				System.out.println("sql: " + ps);
				while (rs.next()) {
					Account account = new Account();
					account.setAccountId(rs.getInt("accountId"));
					account.setTransactionId(rs.getInt("transactionId"));
					account.setAccountType(rs.getString("accountType"));
					account.setRecordId(rs.getInt("recordId"));
					account.setCreatedAt(rs.getString("createdAt"));
					account.setAccountName(rs.getString("accountName"));
					accounts.add(account);
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
		return accounts;
	}

	public static ArrayList<RecordState> getRecordStates(PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		ArrayList<RecordState> recordStates = new ArrayList<>();

		String sql = "SELECT * FROM RecordStates";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				System.out.println("sql: " + ps);
				while (rs.next()) {
					RecordState recordState = new RecordState();
					recordState.setId(rs.getInt("id"));
					recordState.setRecordType(rs.getString("type"));
					recordStates.add(recordState);
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
		return recordStates;
	}

	public static ArrayList<String> getRecordTypes(PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		ArrayList<String> recordTypes = new ArrayList<>();

		String sql = "SELECT * FROM RecordTypes";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				System.out.println("sql: " + ps);
				while (rs.next()) {
					recordTypes.add(rs.getString("recordTypeName"));
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
		return recordTypes;
	}

	public static ArrayList<String> getFilterHeaders(PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		ArrayList<String> filterHeaders = new ArrayList<>();

		String sql = "SELECT * FROM FilterItem";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				System.out.println("sql: " + ps);
				while (rs.next()) {
					filterHeaders.add(rs.getString("filterName"));
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
		return filterHeaders;
	}
	public static ArrayList<Currency> getCurrencies (PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		ArrayList<Currency> currencies = new ArrayList<>();
		String sql = "SELECT * FROM Currencies";
		try (PreparedStatement ps = con.prepareStatement(sql) ) {
			try (ResultSet rs = ps.executeQuery()) {
				System.out.println("sql: " + ps);
				while (rs.next()) {
					Currency currency = new Currency();
					currency.setConversionRate(rs.getFloat("conversionRate"));
					currency.setCurrencyName(rs.getString("currencyName"));
					currency.setId(rs.getInt("id"));
					currencies.add(currency);
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
		return currencies;
	}
}
