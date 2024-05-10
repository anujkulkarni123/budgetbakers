package com.exavalu.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.exavalu.entities.Category;
import com.exavalu.entities.Currency;
import com.exavalu.entities.SubCategory;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.utilities.DbConnectionProvider;

public class AdminService {
	public static boolean updateCategories(Category category, PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		String sql = "UPDATE Categories SET categoryName = ? WHERE categoryId = ?";
		int row = -1;
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, category.getCategoryName());
			// need to still figure out how to deal with subcategories
			ps.setInt(2, category.getCategoryId());
			row = ps.executeUpdate();
			
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
		return (row > 0);
	}
	public static boolean updateCurrency(Currency currency, PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		String sql = "UPDATE Currencies SET currencyName = `?`, conversionRate = ? WHERE id = ?";
		int row = -1;
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, currency.getCurrencyName());
			ps.setFloat(2, currency.getConversionRate());
			ps.setInt(3, currency.getId());
			row = ps.executeUpdate();
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
		return (row > 0);
	}
	
	public static boolean addCategories(Category category, PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		String sql = "INSERT INTO Categories (categoryId, categoryName) VALUES (?, ?)";
		int row = -1;
		try (PreparedStatement ps = con.prepareStatement(sql)) { 
			ps.setInt(1, category.getCategoryId());
			ps.setString(2, category.getCategoryName());
			row = ps.executeUpdate();
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
		return (row > 0);
	}
	
	public static boolean addCurrency(Currency currency, PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		String sql = "INSERT INTO Currencies (id, currencyName, conversionRate) VALUES (?, ?, ?) ";
		int row = -1;
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, currency.getId());
			ps.setString(2, currency.getCurrencyName());
			ps.setFloat(3, currency.getConversionRate());
			row = ps.executeUpdate();
			
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
		return (row > 0);
	}

	public static boolean addSubCategory(SubCategory subcategory, PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		String sql = "INSERT INTO SubCategories (categoryId, subCategoryName) VALUES (?, ?)";
		int row = -1;
		try(PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, subcategory.getCategoryId());
			ps.setString(2, subcategory.getSubCategoryName());
			row = ps.executeUpdate();
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
		return (row > 0);
 	}
	public static boolean updateSubCategory(SubCategory subcategory, PropertyValues propertyValues) {
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		String sql = "UPDATE SubCategories SET categoryId = ?, subCategoryName = ? WHERE subCategoryId = ?";
		int row = -1;
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, subcategory.getCategoryId());
			ps.setString(2, subcategory.getSubCategoryName());
			ps.setInt(3, subcategory.getSubCategoryId());
			row = ps.executeUpdate();
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
		return (row > 0);
	}
}
