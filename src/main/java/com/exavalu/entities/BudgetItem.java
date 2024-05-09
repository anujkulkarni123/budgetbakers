package com.exavalu.entities;

public class BudgetItem {
	
	private int id;
	private String category;
	private double budgetLimit;
	private String currency;
	
	public BudgetItem(int asInt, String asString, double asDouble, String currency) {
		this.id  = asInt;
		this.category = asString;
		this.budgetLimit = asDouble;
		this.currency = currency;
	}
	public BudgetItem() {
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public double getBudgetLimit() {
		return budgetLimit;
	}
	public void setBudgetLimit(double budgetLimit) {
		this.budgetLimit = budgetLimit;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
