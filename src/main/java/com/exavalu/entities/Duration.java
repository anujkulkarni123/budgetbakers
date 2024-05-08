package com.exavalu.entities;

import java.io.Serializable;

public class Duration implements Serializable{
	private static final long serialVersionUID = 1L;
	private int length;
	private String startDate;
	private String endDate;
	
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
