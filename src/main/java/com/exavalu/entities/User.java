package com.exavalu.entities;

public class User {
	private String emailAddress;
	private String password;
	private int roleId;
	private String firstName;
	private String lastName;
	private boolean status;
	private boolean isLoggedIn;
	
	public User(String firstName, String lastName, String email, String password) {
        // Initialize class fields with constructor parameters
		this.firstName = firstName;
		this.lastName = lastName;
        this.emailAddress = email;
        this.password = password;
    }
	
	public User() {
		// TODO Auto-generated constructor stub
	}

	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setStatus(int int1) {
		// TODO Auto-generated method stub
		
	}
	public boolean getStatus() {
		return status;
	}
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	
	

}
