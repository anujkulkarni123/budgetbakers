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
<body style="margin: 0; padding: 0; font-family: Arial, sans-serif;">
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
<div style="display: flex; justify-content: space-between; padding-top: 5rem; background-color: #eff0f2; min-height: 100rem; height: 100%;">
<div style="margin-left: 10%; margin-right:5%; width: 20%; min-height: 80%; background-color: #fafbfc;">
    <h1 style="text-align: center; font-size: 24px; padding-top:20px;">Accounts</h1>

    <div style="display: flex; justify-content: center; align-items: center; padding: 10px 0;">
    <button onclick="openAddAccountPopup()" style="background-color: #4CAF50; color: white; padding: 10px 15px; border: none; border-radius: 20px; width: 70%; text-align: center; font-size: 16px; cursor: pointer;">Add Account</button>
    </div>
</div>
<div id="addAccountPopup" style="display: none; position: fixed; left: 0; top: 0; width: 100%; height: 100%; overflow: auto; background-color: rgba(0,0,0,0.4); z-index: 1; justify-content: center; align-items: center;">
    <form action="AddAccount" method="post" style="background-color: #fefefe; margin: auto; padding: 20px; border: 1px solid #888; width: 40%; border-radius: 10px; box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2); position: relative; top: 50%; transform: translateY(-50%);">
    <div class="form-group" style="margin-bottom: 15px;">
        <label for="name" style="display: block; margin-bottom: 5px;">Name</label>
        <input type="text" id="name" name="name" required style="width: 100%; padding: 10px; margin: 5px 0 22px 0; display: inline-block; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;">
    </div>
    <div class="form-group" style="margin-bottom: 15px;">
        <label for="accountType" style="display: block; margin-bottom: 5px;">Account Type</label>
        <select id="accountType" name="accountType" required style="width: 100%; padding: 10px; margin: 5px 0 22px 0; display: inline-block; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;">
            
                <% for (AccountType type : accountTypeList) { %>
                    <option value="<%= type.getId() %>"><%= type.getType() %></option>
                <% } %>
            </select>
        </div>
    <div class="form-group" style="margin-bottom: 15px;">
        <label for="initialAmount" style="display: block; margin-bottom: 5px;">Initial Amount</label>
        <input type="number" id="initialAmount" name="initialAmount" required style="width: 100%; padding: 10px; margin: 5px 0 22px 0; display: inline-block; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;">
    </div>
    <div class="form-group" style="margin-bottom: 15px;">
        <label for="currency" style="display: block; margin-bottom: 5px;">Currency</label>
        <select id="currency" name="currency" required style="width: 100%; padding: 10px; margin: 5px 0 22px 0; display: inline-block; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;">
            
                <% for (Currency currency : currencyList) { %>
                    <option value="<%= currency.getId() %>"><%= currency.getCurrencyName() %></option>
                <% } %>
            </select>
        </div>
        <input type="hidden" name="emailAddress" value="<%= currentUser.getEmailAddress() %>" />
    <div class="form-actions" style="padding-top: 10px;">
        <button type="submit" style="background-color: #4CAF50; color: white; padding: 14px 20px; margin: 8px 0; border: none; border-radius: 4px; cursor: pointer; width: 100%;">Save</button>
        <button type="button" onclick="closeAddAccountPopup()" style="background-color: #f44336; color: white; padding: 14px 20px; margin: 8px 0; border: none; border-radius: 4px; cursor: pointer; width: 100%;">Cancel</button>
    </div>
    </form>
</div>

<div style="margin-right: 10%; width: 70%; display: flex; flex-wrap: wrap; gap: 20px; max-height: 4rem; min-height: 4rem;">
    <% if (accounts != null) {
        for (Account account : accounts) { %>
            <div style="display: flex; justify-content: space-between; width: 100%; margin-bottom: 10px; background-color: white; border-radius: 10px; padding: 10px;">
                <p style="flex: 3; text-align: left; margin: 0; padding-right: 10px;"><%= account.getName() %></h3>
                <p style="flex: 2; text-align: center; margin: 0; padding: 0 10px;">Type: <%= accountTypeMap.get(account.getAccountTypeId()) %></p>
                <p style="flex: 3; text-align: right; margin: 0; padding-left: 10px;">Balance: <%= account.getAccountBalance() %> <%= currencyMap.get(account.getCurrencyId()) %></p>
            </div>
    <%   }
    } %>
</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>