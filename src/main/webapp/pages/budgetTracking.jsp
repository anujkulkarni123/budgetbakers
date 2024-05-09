<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Budget</title>
	<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
	<script src="js/viewBudget.js"></script>
</head>
<body class="bg-secondary-subtle">

	<%@ include file="components/dashboardHeader.jsp"%>
	<div class="d-flex justify-content-center">
		<div class="card" style="margin-top: 10vh; padding-bottom: 20px; width: 80%;">
			<div class="container">
			    <canvas id="budgetVsExpensesChart"></canvas>
			</div>
		</div>
	</div>
	
</body>
</html>