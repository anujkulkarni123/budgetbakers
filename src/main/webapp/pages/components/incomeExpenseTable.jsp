<%@ page import="com.exavalu.entities.Category"%>
<%@ page import="com.exavalu.entities.Duration"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.text.DecimalFormat"%>

<%
System.out.println("Entered Income/Expense Table");

ArrayList<Category> categories = (ArrayList<Category>) request.getAttribute("CATEGORIES");
Hashtable<String, Double> totalByCategories = (Hashtable<String, Double>) request.getAttribute("TOTAL_BY_CATEGORY");
double totalIncome = (Double) request.getAttribute("TOTAL_INCOME");
double totalExpense = (Double) request.getAttribute("TOTAL_EXPENSE");
String initialPeriod = (String) request.getAttribute("INITIAL_DURATION");
Duration duration = (Duration) request.getAttribute("DURATION");
ArrayList<String> currencies = (ArrayList<String>) request.getAttribute("CURRENCIES");

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


DecimalFormat df = new DecimalFormat("#.00");
double total = totalIncome + totalExpense;
String totalFormatted = total < 0 ? currencies.get(0) + " -$" + df.format(total * -1)
		: currencies.get(0) + " $" + df.format(total * -1);
String totalIncomeFormatted = totalIncome < 0 ? currencies.get(0) + " -$" + df.format(totalIncome)
		: currencies.get(0) + " $" + df.format(totalIncome);
String totalExpenseFormatted = totalExpense < 0 ? currencies.get(0) + " -$" + df.format(totalExpense * -1)
		: currencies.get(0) + " $" + df.format(totalExpense);
String pastTotalIncomeFormatted = pastTotalIncome < 0 ? currencies.get(0) + " -$" + df.format(pastTotalIncome) : currencies.get(0) + " $" + df.format(pastTotalIncome);
String pastTotalExpenseFormatted =  pastTotalExpense < 0 ? currencies.get(0) + " -$" + df.format(pastTotalExpense * -1)
: currencies.get(0) + " $" + df.format(pastTotalExpense);

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

	<h4 class="mb-1"><%=totalFormatted%></h4>
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
		<thead class="table-light bg-secondary shadow-md">
			<tr>
				<th scope="col"></th>
				<th scope="col">Total Income</th>
				<th scope="col"><%=totalIncomeFormatted%></th>
				<th class="text-secondary" scope="col"><%=pastTotalIncome%></th>
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
				<td>$<%=df.format(totalByCategories.get(category.getCategoryName()))%></td>
				<td class="text-secondary">$<%=df.format(pastPeriodTotalByCategories.get(category.getCategoryName()))%></td>
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
		<thead class="table-light shadow-md">
			<tr>
				<th scope="col"></th>
				<th scope="col">Total Expense</th>
				<th scope="col"><%=totalExpenseFormatted%></th>
				<th class="text-secondary" scope="col">143134.00</th>
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
				<td>-$<%=df.format(totalByCategories.get(category.getCategoryName()) * -1)%></td>
				<td class="text-secondary">-$<%=df.format(pastPeriodTotalByCategories.get(category.getCategoryName()))%></td>
				<td class="text-secondary"></td>
			</tr>
			<%
			}
			}
			%>
		</tbody>
	</table>
</div>

