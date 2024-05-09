<%@ page import="com.exavalu.entities.Category"%>
<%@ page import="com.exavalu.entities.Currency"%>
<%@ page import="com.exavalu.entities.Duration"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.NumberFormat" %>
<%
System.out.println("Entered Income/Expense Table");

ArrayList<Category> categories = (ArrayList<Category>) request.getAttribute("CATEGORIES");
Hashtable<String, Double> totalByCategories = (Hashtable<String, Double>) request.getAttribute("TOTAL_BY_CATEGORY");
double totalIncome = (Double) request.getAttribute("TOTAL_INCOME");
double totalExpense = (Double) request.getAttribute("TOTAL_EXPENSE");
String initialPeriod = (String) request.getAttribute("INITIAL_DURATION");
Duration duration = (Duration) request.getAttribute("DURATION");
ArrayList<Currency> currencies = (ArrayList<Currency>) request.getAttribute("CURRENCIES");

// past period
double pastTotalIncome = (Double) request.getAttribute("PAST_TOTAL_INCOME");
double pastTotalExpense = (Double) request.getAttribute("PAST_TOTAL_EXPENSE");
Hashtable<String, Double> pastPeriodTotalByCategories = (Hashtable<String, Double>) request.getAttribute("PAST_TOTAL_BY_CATEGORY");

String period = ""; 
if (duration != null) {
	if (duration.getLength() == -2) {
		period = duration.getStartDate() + " " + duration.getEndDate();
	} else if (duration.getLength() == -1) {
		period = "All";
	} else {
		period = "Last " + duration.getLength() + " days";
	}
}

NumberFormat formatter = NumberFormat.getCurrencyInstance();
%>

<style>
.icon {
	text-align: center;
	width: 80px;
}

table tr {
	padding: 5rem 0;
}
</style>

<div>
	<h2 class="reportHeader mb-3 mt-1" style="text-align: center">Income
		and Expenses Report</h2>
	<h5 class="text-secondary mb-1">
		Time Period -
		<%=period = duration == null ? initialPeriod : period %></h5>

	<h4 class="mb-1"><%=formatter.format(totalIncome + totalExpense)%></h4>
	<table class="table table-borderless table-hover">
		<!-- this is the expense part of the table -->
		<thead class="pt-1 pb-1 ">
			<tr>
				<th scope="col"></th>
				<th scope="col"></th>
				<th scope="col">Current Period</th>
				<th scope="col">Previous Period</th>
			</tr>
		</thead>
		<thead class="table-dark shadow-md">
			<tr>
				<th scope="col"></th>
				<th scope="col">Total Income</th>
				<th scope="col"><%=formatter.format(totalIncome)%></th>
				<th scope="col"><%=formatter.format(pastTotalIncome)%></th>
				<th scope="col"></th>
			</tr>
		</thead>
		<tbody>
			<%
			for (Category category : categories) {
				if (category.getCategoryId() == 10) {

					String categoryFavIcon = category.getCategoryIcon();
					String categoryColor = category.getCategoryColor();
			%>
			<tr>
				<th class="icon" scope="row"><i class="<%=categoryFavIcon%>"
					style="color: <%=categoryColor%>"></i></th>
				<td><%=category.getCategoryName()%></td>
				<td class=<%=totalByCategories.get(category.getCategoryName()) != 0 ? "fw-b" : ""%>><%=formatter.format(totalByCategories.get(category.getCategoryName()))%></td>
				<td class="text-secondary"><%=formatter.format(pastPeriodTotalByCategories.get(category.getCategoryName()))%></td>
				<td class="text-secondary"></td>
			</tr>
			<%
			}
			}
			%>
		</tbody>

		<thead class="pt-1 pb-1 ">
			<tr>
				<th scope="col"></th>
				<th scope="col"></th>
				<th scope="col">Current Period</th>
				<th scope="col">Previous Period</th>
			</tr>
		</thead>
		<thead class="table-dark shadow-md">
			<tr>
				<th scope="col"></th>
				<th scope="col">Total Expense</th>
				<th scope="col"><%=formatter.format(totalExpense)%></th>
				<th scope="col"><%=formatter.format(pastTotalExpense)%></th>
				<th scope="col"></th>
			</tr>
		</thead>
		<tbody>
			<%
			for (Category category : categories) {
				System.out.println(
				"category " + category.getCategoryName() + ": " + totalByCategories.get(category.getCategoryName()));
				if (category.getCategoryId() != 10) {

					String categoryFavIcon = category.getCategoryIcon();
					String categoryColor = category.getCategoryColor();
			%>
			<tr>
				<th class="icon" scope="row"><i class="<%=categoryFavIcon%>"
					style="color: <%=categoryColor%>"></i></th>
				<td><%=category.getCategoryName()%></td>
				<td class=<%=totalByCategories.get(category.getCategoryName()) != 0 ? "fw-b" : ""%>><%=formatter.format(totalByCategories.get(category.getCategoryName()))%></td>
				<td class="text-secondary"><%=formatter.format(pastPeriodTotalByCategories.get(category.getCategoryName()))%></td>
				<td class="text-secondary"></td>
			</tr>
			<%
			}
			}
			%>
		</tbody>
	</table>
</div>
