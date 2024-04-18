<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Records</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/dashboard.css">
</head>
<body>
	<%@ include file="components/dashboardHeader.jsp"%>
	
	<div class="main-wrapper">
		<div class="record-container">
			<div class="left-container">
				<jsp:include page="components/sideFilterBar.jsp">
				    <jsp:param name="filterType" value="Records"/>
				</jsp:include>
			</div>
			<div class="right-container">hello world</div>
		</div>
	</div>
</body>
</html>
 