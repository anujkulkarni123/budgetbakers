package com.exavalu.entities;

import java.io.Serializable;

public class RecordState implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String recordType;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
}
