package com.exavalu.entities;

public class Currency {
	private int id;
	private String currencyName;
	private float conversionRate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public float getConversionRate() {
		return conversionRate;
	}
	public void setConversionRate(float conversionRate) {
		this.conversionRate = conversionRate;
	}
}
