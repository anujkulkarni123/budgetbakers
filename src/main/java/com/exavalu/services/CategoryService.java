package com.exavalu.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.exavalu.entities.Account;
import com.exavalu.entities.AccountType;
import com.exavalu.entities.Currency;
import com.exavalu.entities.Menu;
import com.exavalu.entities.SubCategory;
import com.exavalu.entities.User;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.utilities.DbConnectionProvider;


public class CategoryService {
	public static ArrayList<Category> getCategories(PropertyValues propertyValues) {
		 DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
	     Connection con = dbConnectionProvider.getDbConnection(propertyValues);
	     
		String sql = "SELECT * FROM categories";
		ArrayList<Category> categoryList = new ArrayList<>();
		
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
               while (rs.next()) {
               	Category category = new Category();
               	category.setCategoryId(rs.getInt("categoryId"));
               	category.setCategoryName(rs.getString("categoryName"));
               	categoryList.add(category);
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
		
		
		return categoryList;
	}
	
	
	public static ArrayList<SubCategory> getSubCategories(PropertyValues propertyValues) {
		 DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
	     Connection con = dbConnectionProvider.getDbConnection(propertyValues);
	     
		String sql = "SELECT * FROM subcategories";
		ArrayList<SubCategory> subCategoryList = new ArrayList<>();
		
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
              while (rs.next()) {
              	SubCategory category = new SubCategory();
              	category.setSubCategoryId(rs.getInt("subCategoryId"));
              	category.setCategoryId(rs.getInt("categoryId"));
              	category.setSubCategoryName(rs.getString("subCategoryaName"));
              	subCategoryList.add(category);
              }
          }
		} catch (SQLException e) {
          e.printStackTrace();

      } finally {
 
          try {
              if (con != null) {
                  con.close();
              }
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
		
		
		return subCategoryList;
	}
	

}