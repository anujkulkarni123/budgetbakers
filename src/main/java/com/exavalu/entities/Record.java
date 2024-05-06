package com.exavalu.entities;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Record {
	
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private String date;
	
    private int recordId;
	private int	accountId;
	private double amount;
	private String currencyName;
	private String active;
	private String type;
	private boolean paymentStatus;
	private String category;
	private String subCategory;
	private String userEmail;
	private String payee;
	private String note;
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
