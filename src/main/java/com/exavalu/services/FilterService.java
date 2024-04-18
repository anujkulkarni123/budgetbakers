package com.exavalu.services;

import java.sql.Connection;

import com.exavalu.pojos.PropertyValues;
import com.exavalu.utilities.DbConnectionProvider;

public class FilterService {
	
	public static void getCategories(PropertyValues propertyValues)	{
		 DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
	     Connection con = dbConnectionProvider.getDbConnection(propertyValues);
	}
}
