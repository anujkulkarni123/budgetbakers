<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
<meta charset="ISO-8859-1">
<title>Account Page</title>

</head>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script>
	function updateAccountInDOM(accountsData) {
		accountsData.forEach(function(updatedAccount) {
			var accountElement = $("#account_" + updatedAccount.accountId);
			accountElement.find(".account-balance").text(
					updatedAccount.accountBalance);
		});
	}

	function updateAccount() {
		$.ajax({
			type : "GET",
			url : "/GetUpdatedAccounts",
			success : function(response) {
				updateAccountInDOM(response);
			},
			error : function(xhr, status, error) {
				console.error("Error updating account:", error);
			}
		});
	}
	
    document.addEventListener('DOMContentLoaded', function() {
        var form = document.getElementById('myForm');
        form.addEventListener('submit', function(event) {
            event.preventDefault();
            updateAccount();
            form.submit();
        });
    });
</script>

<script>
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
	<jsp:include page="components/dashboardHeader.jsp" />
	<div
		style="display: flex; justify-content: space-between; padding-top: 5rem; background-color: #eff0f2; min-height: 100rem; height: 100%;">
		<div
			style="margin-left: 10%; margin-right: 2%; width: 30%; min-height: 80%; background-color: #fafbfc; border-radius: 5px;">
			<div style="margin-left: 10%; margin-right: 10%; width: 80%;">
				<h1 style="text-align: left; font-size: 24px; padding-top: 20px;">Accounts</h1>

				<div style="display: flex; padding: 10px 0; width: 100%;">
					<button onclick="openAddAccountPopup()"
						style="background-color: #00aa70; color: white; padding: 10px 15px; border: none; border-radius: 20px; width: 100%; text-align: center; font-size: 16px; cursor: pointer;">
						+ Add</button>
				</div>
			</div>
		</div>
		<div id="addAccountPopup"
			style="display: none; position: fixed; left: 0; top: 0; width: 100%; height: 100%; overflow: auto; background-color: rgba(0, 0, 0, 0.4); z-index: 1; justify-content: center; align-items: center;">
			<form action="AddAccount" method="post"
				style="background-color: #fefefe; margin: auto; padding: 20px; border: 1px solid #888; width: 30%; border-radius: 10px; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2); position: relative; top: 50%; transform: translateY(-50%);">
				<div
					style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #ccc; padding-bottom: 10px;">
					<h3 style="margin: 0;">ADD ACCOUNT</h3>
					<button type="button" onclick="closeAddAccountPopup()"
						style="border: none; background: none; font-size: 24px; cursor: pointer;">&times;</button>
				</div>
				<div class="form-group"
					style="margin-top: 15px; margin-bottom: 15px;">
					<label for="name" style="display: block; margin-bottom: 5px;">Name</label>
					<input type="text" id="name" name="name" required
						style="width: 100%; padding: 10px; margin: 5px 0; display: inline-block; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;">
				</div>
				<div class="form-group" style="margin-bottom: 15px;">
					<label for="accountType"
						style="display: block; margin-bottom: 5px;">Account Type</label> <select
						id="accountType" name="accountType" required
						style="width: 100%; padding: 10px; margin: 5px 0; display: inline-block; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;">
						<%
						for (AccountType type : accountTypeList) {
						%>
						<option value="<%=type.getId()%>"><%=type.getType()%></option>
						<%
						}
						%>
					</select>
				</div>
				<div
					style="display: flex; justify-content: space-between; margin-bottom: 15px;">
					<div class="form-group" style="flex: 1; margin-right: 10px;">
						<label for="initialAmount"
							style="display: block; margin-bottom: 5px;">Initial
							Amount</label> <input type="number" id="initialAmount"
							name="initialAmount" required
							style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;">
					</div>
					<div class="form-group" style="flex: 1;">
						<label for="currency" style="display: block; margin-bottom: 5px;">Currency</label>
						<select id="currency" name="currency" required
							style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;">
							<%
							for (Currency currency : currencyList) {
							%>
							<option value="<%=currency.getId()%>"><%=currency.getCurrencyName()%></option>
							<%
							}
							%>
						</select>
					</div>
				</div>
				<input type="hidden" name="emailAddress"
					value="<%=currentUser.getEmailAddress()%>" />
				<div class="form-actions" style="padding-top: 10px;">
					<button type="submit"
						style="background-color: #00aa70; color: white; padding: 10px 15px; border: none; border-radius: 20px; width: 100%; text-align: center; font-size: 16px; cursor: pointer;">Save</button>
				</div>
			</form>
		</div>

		<div
			style="margin-right: 10%; width: 100%; display: flex; flex-wrap: wrap; gap: 5px; max-height: 4rem; min-height: 4rem;">
			<%
			if (accounts != null) {
				for (Account account : accounts) {
					String iconPath = "";
					if (account.getAccountTypeId() == 1) {
				iconPath = "images/cash.png";
					} else if (account.getAccountTypeId() == 2) {
				iconPath = "images/savings.png";
					}
			%>
			<div id="account_<%=account.getAccountId()%>"
				style="display: flex; align-items: center; justify-content: space-between; width: 100%; margin-bottom: 10px; background-color: white; border-radius: 5px; padding: 10px;">
				<%
				if (!iconPath.isEmpty()) {
				%>
				<img src="<%=request.getContextPath()%>/<%=iconPath%>"
					alt="Account Icon"
					style="width: 24px; height: 24px; margin-right: 10px;">
				<%
				}
				%>
				<p
					style="flex: 3; text-align: left; margin: 0; padding-right: 10px;"><%=account.getName()%></p>
				<p style="flex: 2; text-align: center; margin: 0; padding: 0 10px;">
					<%=accountTypeMap.get(account.getAccountTypeId())%></p>
				<p
					style="flex: 3; text-align: right; margin: 0; padding-left: 10px;">
					<%=account.getAccountBalance()%>
					<%=currencyMap.get(account.getCurrencyId())%></p>
			</div>
			<%
			}
			}
			%>
		</div>
	</div>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>