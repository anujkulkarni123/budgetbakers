<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.exavalu.entities.Account"%>
<%@ page import="com.exavalu.entities.AccountType"%>
<%@ page import="com.exavalu.entities.Currency"%>
<%@ page import="com.exavalu.entities.User"%>
<%@ page import="com.exavalu.entities.Category"%>
<%@ page import="com.exavalu.entities.SubCategory"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" />
</head>
<body>
	<%
	User currentUser = (User) session.getAttribute("USER");
	System.out.println("currentUser");
	System.out.println(currentUser.getEmailAddress());
	ArrayList<Account> accounts = (ArrayList<Account>) session.getAttribute("ACCOUNTS");
	ArrayList<AccountType> accountTypeList = (ArrayList<AccountType>) session.getAttribute("ACCOUNTTYPES");
	ArrayList<Currency> currencyList = (ArrayList<Currency>) session.getAttribute("CURRENCIES");
	ArrayList<Category> categoryList = (ArrayList<Category>) session.getAttribute("CATEGORIES");
	ArrayList<SubCategory> SubCategoryList = (ArrayList<SubCategory>) session.getAttribute("SUBCATEGORIES");
	System.out.println("category List");
	System.out.println(categoryList);
	System.out.println(SubCategoryList);
	Map<Integer, String> accountTypeMap = new HashMap<>();
	Map<Integer, String> currencyMap = new HashMap<>();

	for (AccountType accountType : accountTypeList) {
		accountTypeMap.put(accountType.getId(), accountType.getType());
	}

	for (Currency currency : currencyList) {
		currencyMap.put(currency.getId(), currency.getCurrencyName());
	}
	System.out.println("CHECKER");
	System.out.println(accounts);
	System.out.println(accountTypeList);
	request.setAttribute("accounts", accounts);
    request.setAttribute("accountTypeList", accountTypeList);
    request.setAttribute("currencyList", currencyList);
    request.setAttribute("accountTypeMap", accountTypeMap);
    request.setAttribute("currencyMap", currencyMap);
    request.setAttribute("categoryList", categoryList);
    request.setAttribute("SubCategoryList", SubCategoryList);
    
	%>
	<%@ include file="components/dashboardHeader.jsp"%>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>