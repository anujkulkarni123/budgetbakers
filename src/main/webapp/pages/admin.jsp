<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.text.NumberFormat"%>
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
<title>Admin Panel</title>
<style>
table, th, td {
	border: 1px solid black;
	border-collapse: collapse;
	padding: 8px;
	text-align: left;
}
</style>
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

	NumberFormat formatter = NumberFormat.getCurrencyInstance();
	%>
	<jsp:include page="components/dashboardHeader.jsp" />

	<h2 style="padding-top: 5rem;">Edit Currencies</h2>
	<form action="SaveCurrenciesServlet" method="post">
		<table>
			<tr>
				<th>Currency ID</th>
				<th>Currency Name</th>
				<th>Conversion Rate</th>
				<th>Action</th>
			</tr>
			<%
			for (Currency currency : currencyList) {
			%>
			<tr>
				<td><input type="text" name="id" value="<%=currency.getId()%>"
					readonly></td>
				<td><input type="text" name="currencyName<%=currency.getId()%>"
					value="<%=currency.getCurrencyName()%>"></td>
				<td><input type="text"
					name="conversionRate<%=currency.getId()%>"
					value="<%=currency.getConversionRate()%>"></td>
				<td><button type="submit">Save</button></td>
			</tr>
			<%
			}
			%>
			<tr>
				<!-- New currency entry row -->
				<td>New</td>
				<td><input type="text" name="newCurrencyName"
					placeholder="Enter name"></td>
				<td><input type="text" name="newConversionRate"
					placeholder="Enter rate"></td>
				<td><button type="submit" name="action" value="addCurrency">Add
						New Currency</button></td>
			</tr>
		</table>
	</form>

	<h2>Edit Categories</h2>
	<form action="SaveCategoriesServlet" method="post">
		<table>
			<tr>
				<th>Category ID</th>
				<th>Category Name</th>
				<th>Action</th>
			</tr>
			<%
			for (Category category : categoryList) {
			%>
			<tr>
				<td><%=category.getCategoryId()%></td>
				<td><input type="text"
					name="categoryName<%=category.getCategoryId()%>"
					value="<%=category.getCategoryName()%>"></td>
				<td><button type="submit">Save</button></td>
			</tr>
			<%
			}
			%>
			<tr>
				<td>New</td>
				<td><input type="text" name="newCategoryName"
					placeholder="Enter name"></td>
				<td><button type="submit" name="action" value="addCategory">Add
						New Category</button></td>
			</tr>
		</table>
	</form>

	<h2>Edit SubCategories</h2>
	<form action="SaveSubCategoriesServlet" method="post">
		<table>
			<tr>
				<th>SubCategory ID</th>
				<th>Category ID</th>
				<th>SubCategory Name</th>
				<th>Action</th>
			</tr>
			<%
			for (SubCategory subCategory : SubCategoryList) {
			%>
			<tr>
				<td><%=subCategory.getSubCategoryId()%></td>
				<td><%=subCategory.getCategoryId()%></td>
				<td><input type="text"
					name="subCategoryName<%=subCategory.getSubCategoryId()%>"
					value="<%=subCategory.getSubCategoryName()%>"></td>
				<td><button type="submit">Save</button></td>
			</tr>
			<%
			}
			%>
			<tr>
				<td>New</td>
				<td><select name="newSubCategoryCategoryId">
						<%
						for (Category category : categoryList) {
						%>
						<option value="<%=category.getCategoryId()%>"><%=category.getCategoryName()%></option>
						<%
						}
						%>
				</select></td>
				<td><input type="text" name="newSubCategoryName"
					placeholder="Enter name"></td>
				<td><button type="submit" name="action" value="addSubCategory">Add
						New SubCategory</button></td>
			</tr>
		</table>
	</form>

</body>
</html>