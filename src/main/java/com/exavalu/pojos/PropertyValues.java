package com.exavalu.pojos;

import java.io.Serializable;

public class PropertyValues implements Serializable{
	
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		private PropertyValues() {
				
		}
		
		private static PropertyValues propertyValues = null;
		private String url;
		private String dbname;
		private String user;
		private String password;
		
		private String emailAddress;
		private String emailPassword;
		
		public static PropertyValues getInstance() {
			if(propertyValues == null) {
				propertyValues = new PropertyValues();
			}
			return propertyValues;
		}
		
		public String getUrl() {
			return url;
		}
		public String getDbname() {
			return dbname;
		}
		public String getUser() {
			return user;
		}
		public String getPassword() {
			return password;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public void setDbname(String dbname) {
			this.dbname = dbname;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public void setPassword(String password) {
			this.password = password;
		}

		public String getEmailAddress() {
			return emailAddress;
		}

		public void setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
		}

		public String getEmailPassword() {
			return emailPassword;
		}

		public void setEmailPassword(String emailPassword) {
			this.emailPassword = emailPassword;
		}
		
		
		

}
