<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login Page</title>
<link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>  

	<%@ include file="components/header.jsp"%>

	<div class="row">
		<div class="column left-side">
			<h1 style="width: 300px; font-size: 40px;">Your Finances in One
				Place</h1>
			<div class="image-container">
				<img src="images/laptop.png" />
			</div>
			<h3 style="width: 350px; font-size: 20px; font-weight: 70;">
				Dive into reports, build budgets, sync with your banks and enjoy
				automatic categorization.</h3>
			<a href="#">Learn about how Wallet works</a> <br>
			<h1 style="font-weight: 60;">Wallet</h1>
		</div>

		<div class="column">
			<div class="login-container">
				<div class="login-form">
					<h1>Verify your Account</h1>
					<span>Enter your code below</span>
					<br></br>
					<form action="VerifyUser" method="post">
						<div class="form-group">
							<label for="email">Verification Code:</label> 
							<input type="text" id="code"
								name="code" 
								value="<% out.println(request.getAttribute("RANDCODE")); %>"
								required>
						</div>
						<div class="form-group">
							<button type="submit" class="login-button">Verify</button>
						</div>
						
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>