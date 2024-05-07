package com.exavalu.entities;

import java.util.List;
import java.io.Serializable;

public class Category implements Serializable {
	private static final long serialVersionUID = 1L;
	private int categoryId;
	private String categoryName;
	private List<String> subCategories;

	    
	private String categoryIcon;
	private String categoryColor;
	
	
	public Category(String categoryName, List<String> subCategories) {
        this.categoryName = categoryName;
        this.setSubCategories(subCategories);
    }
	
	public Category() {
		// TODO Auto-generated constructor stub
	}

	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryIcon() {
		return categoryIcon;
	}
	public void setCategoryIcon(String categoryIcon) {
		this.categoryIcon = categoryIcon;
	}
	public String getCategoryColor() {
		return categoryColor;
	}
	public void setCategoryColor(String categoryColor) {
		this.categoryColor = categoryColor;
	}

	public List<String> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<String> subCategories) {
		this.subCategories = subCategories;
	}
}
