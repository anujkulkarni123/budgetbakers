<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.exavalu.entities.BudgetItem"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Budget Management</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/admin.css">
    <script src="js/updateBudget.js"></script> <!-- Link to external JS file -->
</head>
<body class="bg-secondary-subtle">
    <jsp:include page="components/dashboardHeader.jsp" />

   	<div class="container mt-5">
	    <div class="row justify-content-center">
	        <div class="col-md-8"> <!-- Adjust this to change the size of the card -->
	            <div class="card">
	                <div class="card-body">
	                    <h1 class="card-title mb-4">Manage Budget Categories</h1>
	                    <div class="row align-items-center">
	                        <div class="col-auto">
	                            <select id="currencySelect" class="form-select">
	                                <!-- Options will be dynamically populated -->
	                            </select>
	                        </div>
	                        <div class="col-auto">
	                            <button type="button" onclick="updateAllBudgetItems()" class="btn btn-primary">Update All</button>
	                        </div>
	                    </div>
	
	                    <div class="mt-4 w-100"> <!-- Adjusted width to w-100 to use the full card width -->
	                        <table class="table table-hover">
	                            <thead>
	                                <tr>
	                                    <th scope="col">Category</th>
	                                    <th scope="col">Budget Limit ($)</th>
	                                </tr>
	                            </thead>
	                            <tbody>
	                                <!-- Table rows will be dynamically populated -->
	                            </tbody>
	                        </table>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
</body>
</html>
