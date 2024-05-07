package com.exavalu.entities;

import java.io.Serializable;

public class Report implements Serializable {
	private static final long serialVersionUID = 1L;
	private String analysisName;
	private int analysisValue; 
	
	public String getAnalysisName() {
		return analysisName;
	}
	public void setAnalysisName(String analysisName) {
		this.analysisName = analysisName;
	}
	public int getAnalysisValue() {
		return analysisValue;
	}
	public void setAnalysisValue(int analysisValue) {
		this.analysisValue = analysisValue;
	}
	
}
