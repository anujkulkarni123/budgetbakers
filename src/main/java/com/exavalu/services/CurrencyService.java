package com.exavalu.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import java.util.ArrayList;

import com.exavalu.entities.Currency;
import com.exavalu.entities.Menu;
import com.exavalu.entities.User;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.utilities.DbConnectionProvider;
import com.exavalu.utilities.EmailUtility;
import com.exavalu.utilities.MD5Hash;

public class CurrencyService {
	
	public static ArrayList<Currency> getCurrencies(PropertyValues propertyValues) {
		ArrayList<Currency> currencies = new ArrayList<Currency>();
		DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
		Connection con = dbConnectionProvider.getDbConnection(propertyValues);
		String sql = "SELECT * FROM currencies";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Currency currency = new Currency();
				currency.setId(rs.getInt("id"));
				currency.setCurrencyName(rs.getString("currencyName"));
				currency.setConversionRate(rs.getFloat("conversionRate"));
				currencies.add(currency);
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
		System.out.println(currencies);
		return currencies;
	}
}