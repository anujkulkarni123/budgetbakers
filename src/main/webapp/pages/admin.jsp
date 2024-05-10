<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="java.util.ArrayList, java.util.HashMap, java.util.Map, java.text.NumberFormat, com.exavalu.entities.*"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin Panel</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 0;
	padding: 0;
	display: flex;
	flex-direction: column;
	align-items: center;
}

.container {
	width: 100%;
	margin-top: 20px;
}

select, button, input[type="text"] {
	padding: 8px;
	margin-top: 5px;
	margin-bottom: 5px;
	border: 1px solid #ccc;
	border-radius: 4px;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

th, td {
	border: 1px solid #ddd;
	padding: 8px;
	text-align: left;
}

th {
	background-color: #f2f2f2;
}

.hidden {
	display: none;
}

form {
	margin-top: 20px;
}

h2, h3 {
	color: #333;
}

.title-and-dropdown {
	text-align: center;
	width: 100%;
	padding-top: 50px;
}

</style>
<script>
function showSelectedTable() {
    var selectedValue = document.getElementById("dataSelector").value;
    var tables = document.querySelectorAll(".data-table");
    tables.forEach(table => table.style.display = 'none'); // Hide all tables initially
    document.getElementById(selectedValue).style.display = 'table'; // Show the selected table
}
</script>
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
	<div class="title-and-dropdown">
		<h2>Data Management</h2>
		<label for="dataSelector">Choose data to edit:</label>
		<select id="dataSelector" onchange="showSelectedTable()">
			<option value="currencyTable">Currencies</option>
			<option value="categoryTable">Categories</option>
			<option value="subCategoryTable">SubCategories</option>
		</select>
	</div>
	<div class="container">
		<jsp:include page="components/dashboardHeader.jsp" />
		<div id="currencyTable" class="data-table hidden">

			<form action="SaveCurrenciesServlet" method="post">
				<table>
					<tr>
						<th>Currency ID</th>
						<th>Currency Name</th>
						<th>Conversion Rate</th>
						<th>Action</th>
					</tr>
					<% for (Currency currency : currencyList) { %>
					<tr>
						<td><%=currency.getId()%></td>
						<td><input type="text" name="currencyName<%=currency.getId()%>" value="<%=currency.getCurrencyName()%>"></td>
						<td><input type="text" name="conversionRate<%=currency.getId()%>" value="<%=currency.getConversionRate()%>"></td>
						<td><button type="submit">Save</button></td>
					</tr>
					<% } %>
					<tr>
						<td>New</td>
						<td><input type="text" name="newCurrencyName" placeholder="Enter name"></td>
						<td><input type="text" name="newConversionRate" placeholder="Enter rate"></td>
						<td><button type="submit" name="action" value="addCurrency">Add New Currency</button></td>
					</tr>
				</table>
			</form>
		</div>

		<div id="categoryTable" class="data-table hidden">
			<form action="SaveCategoriesServlet" method="post">
				<table>
					<tr>
						<th>Category ID</th>
						<th>Category Name</th>
						<th>Action</th>
					</tr>
					<% for (Category category : categoryList) { %>
					<tr>
						<td><%=category.getCategoryId()%></td>
						<td><input type="text" name="categoryName<%=category.getCategoryId()%>" value="<%=category.getCategoryName()%>"></td>
						<td><button type="submit">Save</button></td>
					</tr>
					<% } %>
					<tr>
						<td>New</td>
						<td><input type="text" name="newCategoryName" placeholder="Enter name"></td>
						<td><button type="submit" name="action" value="addCategory">Add New Category</button></td>
					</tr>
				</table>
			</form>
		</div>

		<div id="subCategoryTable" class="data-table hidden">
			<form action="SaveSubCategoriesServlet" method="post">
				<table>
					<tr>
						<th>SubCategory ID</th>
						<th>Category ID</th>
						<th>SubCategory Name</th>
						<th>Action</th>
					</tr>
					<% for (SubCategory subCategory : SubCategoryList) { %>
					<tr>
						<td><%=subCategory.getSubCategoryId()%></td>
						<td><%=subCategory.getCategoryId()%></td>
						<td><input type="text" name="subCategoryName<%=subCategory.getSubCategoryId()%>" value="<%=subCategory.getSubCategoryName()%>"></td>
						<td><button type="submit">Save</button></td>
					</tr>
					<% } %>
					<tr>
						<td>New</td>
						<td><select name="newSubCategoryCategoryId">
							<% for (Category category : categoryList) { %>
							<option value="<%=category.getCategoryId()%>"><%=category.getCategoryName()%></option>
							<% } %>
						</select></td>
						<td><input type="text" name="newSubCategoryName" placeholder="Enter name"></td>
						<td><button type="submit" name="action" value="addSubCategory">Add New SubCategory</button></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<script>
        document.getElementById('currencyTable').style.display = 'table';
    </script>
</body>
</html>
