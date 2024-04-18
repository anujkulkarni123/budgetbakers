<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.exavalu.entities.Account"%>
<%@ page import="com.exavalu.entities.AccountType"%>
<%@ page import="com.exavalu.entities.Currency"%>
<%@ page import="com.exavalu.entities.User"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Account Page</title>
</head>
<script type="text/javascript">
    function openAddAccountPopup() {
        var popup = document.getElementById('addAccountPopup');
        popup.style.display = 'block';
    }

    function closeAddAccountPopup() {
        var popup = document.getElementById('addAccountPopup');
        popup.style.display = 'none';
    }
</script>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="css/accountPage.css">
</head>
<body>
<%@ include file="components/dashboardHeader.jsp"%>
<%
	User currentUser = (User) session.getAttribute("USER");
	System.out.println("currentUser");
	System.out.println(currentUser.getEmailAddress());
    ArrayList<Account> accounts = (ArrayList<Account>)request.getAttribute("ACCOUNTS");
    ArrayList<AccountType> accountTypeList = (ArrayList<AccountType>)request.getAttribute("ACCOUNTTYPES");
    ArrayList<Currency> currencyList = (ArrayList<Currency>)request.getAttribute("CURRENCIES");
    
    Map<Integer, String> accountTypeMap = new HashMap<>();
    Map<Integer, String> currencyMap = new HashMap<>();

    for (AccountType accountType : accountTypeList) {
        accountTypeMap.put(accountType.getId(), accountType.getType());
    }

    for (Currency currency : currencyList) {
        currencyMap.put(currency.getId(), currency.getCurrencyName());
    }
%>
<div class="wrapper">
<div class="left-container">
    <button onclick="openAddAccountPopup()">Add Account</button>
</div>
<div id="addAccountPopup" class="add-account-popup">
    <!-- Popup content -->
    <form action="AddAccount" method="post"> <!-- Using POST method -->
        <div class="form-group">
            <label for="name">Name</label>
            <input type="text" id="name" name="name" required>
        </div>
        <div class="form-group">
            <label for="accountType">Account Type</label>
            <select id="accountType" name="accountType" required>
                <% for (AccountType type : accountTypeList) { %>
                    <option value="<%= type.getId() %>"><%= type.getType() %></option>
                <% } %>
            </select>
        </div>
        <div class="form-group">
            <label for="initialAmount">Initial Amount</label>
            <input type="number" id="initialAmount" name="initialAmount" required>
        </div>
        <div class="form-group">
            <label for="currency">Currency</label>
            <select id="currency" name="currency" required>
                <% for (Currency currency : currencyList) { %>
                    <option value="<%= currency.getId() %>"><%= currency.getCurrencyName() %></option>
                <% } %>
            </select>
        </div>
        <input type="hidden" name="emailAddress" value="<%= currentUser.getEmailAddress() %>" />
        <div class="form-actions">
            <button type="submit">Save</button>
            <button type="button" onclick="closeAddAccountPopup()">Cancel</button>
        </div>
    </form>
</div>

<div class="card-container">
    <% if (accounts != null) {
        for (Account account : accounts) { %>
            <div class="account-card">
                <h3><%= account.getName() %></h3>
                <p>Type: <%= accountTypeMap.get(account.getAccountTypeId()) %></p>
                <p>Balance: <%= account.getAccountBalance() %> <%= currencyMap.get(account.getCurrencyId()) %></p>
            </div>
    <%   }
    } else { %>
        <p></p>
    <% } %>
</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>