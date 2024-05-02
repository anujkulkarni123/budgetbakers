<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
    
	%>
	<%@ include file="components/dashboardHeader.jsp"%>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>