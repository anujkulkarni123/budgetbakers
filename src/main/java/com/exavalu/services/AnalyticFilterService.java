package com.exavalu.services;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.exavalu.entities.AnalyticFilter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AnalyticFilterService {
	public static AnalyticFilter getConditions(String JSONdata) {
		// get item filters 
		AnalyticFilter filters = new AnalyticFilter(); 
		Map<String, Object> filterMap = new Gson().fromJson(JSONdata, new TypeToken<Hashtable<String, Object>>() {
		}.getType());
		Set<String> filterKeys = filterMap.keySet();
		for (String key: filterKeys) {
			switch(key) {
			case "accounts":
				ArrayList<String> accountFilters = (ArrayList<String>) filterMap.get(key);
				if (accountFilters.contains("all")) {
					filters.addAccountIdFilter(-1);
					break;
				} else {
					ArrayList<Integer> accountIdFilters = new ArrayList<>();
					for (String id : accountFilters) {
						accountIdFilters.add(Integer.parseInt(id));
					}
					filters.setAccountIds(accountIdFilters);
					break;
				}
			case "categories":
				ArrayList<String> categoryFilters = (ArrayList<String>) filterMap.get(key);
				if (categoryFilters.contains("all")) {
					filters.addCategoryFilter("all");
					break;
				} else {
					ArrayList<String> filtersFormatted = new ArrayList<>();
					for (String filter : categoryFilters) {
						filtersFormatted.add("'" + filter + "'");
					}
					filters.setCategories(filtersFormatted);
					break;
				}
				
			case "currencies":
				ArrayList<String> currencyFilters = (ArrayList<String>) filterMap.get(key);
				if (currencyFilters.contains("all")) {
					filters.addCurrencyFilter("all");
					break;
				} else {
					ArrayList<String> filtersFormatted = new ArrayList<>();
					for (String filter : currencyFilters) {
						filtersFormatted.add("'" + filter + "'");
					}
					filters.setCurrencies(filtersFormatted);
					break;
				}
			case "recordstate":
				ArrayList<String> recordStateFilters = (ArrayList<String>) filterMap.get(key);
				if (recordStateFilters.contains("all")) {
					filters.addRecordStateFilter(-2);
					break;
				} else {
					ArrayList<Integer> filtersFormatted = new ArrayList<>();
					for (String filter : recordStateFilters) {
						filtersFormatted.add(Integer.parseInt(filter));
						
					}
					filters.setRecordStates(filtersFormatted);
					break;
				}
			case "paymenttypes":
				ArrayList<String> paymentTypesFilters = (ArrayList<String>) filterMap.get(key);
				if (paymentTypesFilters.contains("all")) {
					filters.addPaymentTypeFilter("all");
					break;
				} else {
					ArrayList<String> filtersFormatted = new ArrayList<>();
					for (String filter : paymentTypesFilters) {
						filtersFormatted.add("'" + filter + "'");
					}
					filters.setPaymentTypes(filtersFormatted);
					break;
				}
			case "recordtypes":
				ArrayList<String> recordTypesFilters = (ArrayList<String>) filterMap.get(key);
				if (recordTypesFilters.contains("all")) {
					filters.addRecordTypeFilter("all");
					break;
				} else {
					ArrayList<String> filtersFormatted = new ArrayList<>();
					for (String filter : recordTypesFilters) {
						filtersFormatted.add("'" + filter + "'");
					}
					
					filters.setRecordTypes(filtersFormatted);
					System.out.println("HI " + filtersFormatted.toString());
					break;
				}
			}
		}
		System.out.println("Filters selected: ");
		
		for (Integer id: filters.getAccountIds()) {
			System.out.println(id);
		}
		System.out.println("---------------");
		for (String category: filters.getCategories()) {
			System.out.println(category);
		}
		return filters;
	}
	
	public static String getFilterQuery(AnalyticFilter filters) {
		String categoryFilters = filters.getCategories().contains("all") ? "" : 
			filters.getCategories().toString().replace("[", "(") .replace("]", ")"); 
		
		String accountFilters = filters.getAccountIds().contains(-1) ? "" :
				filters.getAccountIds().toString().replace("[", "(") .replace("]", ")"); 
		
		String currencyFilters = filters.getCurrencies().contains("all") ? "" :
			filters.getCurrencies().toString().replace("[", "(") .replace("]", ")"); 
		
		String paymentTypes = filters.getPaymentTypes().contains("all") ? "" :
			filters.getPaymentTypes().toString().replace("[", "(") .replace("]", ")"); 
		
		String recordStates = filters.getRecordStates().contains(-2) ? "" :
			filters.getRecordStates().toString().replace("[", "(") .replace("]", ")"); 
		
		String recordTypes = filters.getRecordTypes().toString().contains("all") ? "" :
			filters.getRecordTypes().toString().replace("[", "(") .replace("]", ")"); 

		String queryAccountIdFilter = accountFilters.isEmpty() ? "" : "accountId in " + accountFilters + " AND";
		String queryCategoriesFilter = categoryFilters.isEmpty() ? "" : " category in " + categoryFilters + " AND";
		String queryCurrencyFilter = currencyFilters.isEmpty() ? "" : " currencyType in " + currencyFilters + " AND";
		String queryPaymentTypes = paymentTypes.isEmpty() ? "" : " paymentType in " + paymentTypes + " AND";
		String queryRecordStates = recordStates.isEmpty() ? "" : " paymentStatus in " + recordStates + " AND";
		String queryRecordTypes = recordTypes.isEmpty() ? "" : " transactionType in " + recordTypes; 
		
		String query = queryAccountIdFilter + queryCategoriesFilter + queryCurrencyFilter + queryPaymentTypes
				+ queryRecordStates + queryRecordTypes; 
		if (query.trim().toUpperCase().endsWith("AND")) query = query.trim().substring(0, query.trim().length() - 3);
		System.out.println("sql: SELECT * FROM TRANSACTIONS WHERE " + query); // need to trim off extra 'AND's 
		return query;
	}
}
