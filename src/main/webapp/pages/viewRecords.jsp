<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.exavalu.entities.Record"%>
<!DOCTYPE html>
<html>
<head>
<title>Records</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/dashboard.css">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet">

</head>
<body>
	<%@ include file="components/dashboardHeader.jsp"%>


	<div class="main-wrapper">
		<div class="record-container">
			<div class="left-container">
				<jsp:include page="components/sideFilterBar.jsp">
					<jsp:param name="filterType" value="Records" />
				</jsp:include>
			</div>
			<div class="right-container ms-4">
				<div class="card card-enhanced mb-4 mt-0">
					<div
						class="card-body d-flex justify-content-between align-items-center px-4">
						<div>
							<span>Select All</span> <input type="checkbox" id="selectAll" onchange="toggleSelectAll(this.checked)" class="form-check-input">
						</div>

						<div id="conditionalDiv" class="" style="display: none;">
						    <button onclick="deleteSelectedRecords()" class="btn btn-danger">Delete Selected Records</button>
						</div>
						<div id="labelDiv">
							CA$<span id="numberValue">0</span>
						</div>
					</div>
				</div>
				<div class="results" id="result-container">

				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript" src="js/records.js"></script>
</body>
</html>
