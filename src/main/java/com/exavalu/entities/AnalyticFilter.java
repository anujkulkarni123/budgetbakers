package com.exavalu.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class AnalyticFilter implements Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<Integer> accountIds = new ArrayList<>();
	private ArrayList<String> categories = new ArrayList<>();
	private ArrayList<String> currencies = new ArrayList<>();
	private ArrayList<Integer> recordStates = new ArrayList<>();
	private ArrayList<String> paymentTypes = new ArrayList<>();
	private ArrayList<String> recordTypes = new ArrayList<>();
	
	public void addAccountIdFilter(Integer accountId) {
		accountIds.add(accountId);
	}
	public void addCategoryFilter(String category) {
		categories.add(category);
	}
	public void addCurrencyFilter(String currency) {
		currencies.add(currency);
	}
	public void addPaymentTypeFilter(String paymentType) {
		paymentTypes.add(paymentType);
	}
	public void addRecordStateFilter(Integer recordState) {
		recordStates.add(recordState);
	}
	public void addRecordTypeFilter(String recordType) {
		recordTypes.add(recordType);
	}
	public ArrayList<Integer> getAccountIds() {
		return accountIds;
	}
	public ArrayList<String> getCategories() {
		return categories;
	}
	public ArrayList<String> getCurrencies() {
		return currencies;
	}
	public ArrayList<Integer> getRecordStates() {
		return recordStates;
	}
	public ArrayList<String> getRecordTypes() {
		return recordTypes;
	}
	public ArrayList<String> getPaymentTypes() {
		return paymentTypes;
	}
	public void setAccountIds(ArrayList<Integer> accountIds) {
		this.accountIds = accountIds;
	}
	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}
	public void setCurrencies(ArrayList<String> currencies) {
		this.currencies = currencies;
	}
	public void setRecordStates(ArrayList<Integer> recordStates) {
		this.recordStates = recordStates;
	}
	public void setRecordTypes(ArrayList<String> recordTypes) {
		this.recordTypes = recordTypes;
	}
	public void setPaymentTypes(ArrayList<String> paymentTypes) {
		this.paymentTypes = paymentTypes;
	}
	
}
