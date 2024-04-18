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
					<h1>Create Wallet Account</h1>
					<span>Sign up below to create your wallet account</span>
					<br></br>
					<form action="RegisterUser" method="post">
						<div class="form-group">
							<label for="firstName">First Name:</label> 
							<input type="firstName"
								id="firstName" name="firstName" required>
						</div>
						<div class="form-group">
							<label for="lastName">Last Name:</label> 
							<input type="lastName"
								id="lastName" name="lastName" required>
						</div>
						<div class="form-group">
							<label for="email">Email:</label> 
							<input type="email" id="email"
								name="emailAddress" required>
						</div>
						<div class="form-group">
							<label for="password">Password:</label> 
							<input type="password"
								id="password" name="password" required>
						</div>
						
						
						<div class="form-group">
							<button type="submit" class="login-button">Sign Up</button>
						</div>
					</form>
				</div>
				
			</div>
		</div>
	</div>
</body>
</html>