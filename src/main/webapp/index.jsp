<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.exavalu.pojos.CustomMessage"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login Page</title>
<link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>

	<%@ include file="pages/components/header.jsp"%>

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
					<h1>Log In</h1>
					<span class="mb-2 text-danger"> <%
										CustomMessage msgObj= (CustomMessage)request.getAttribute("MSG");
										if(msgObj !=null)
										out.print(msgObj.getMessage());
									%>
					</span>
					<form action="Login" method="post">
						<div class="form-group">
							<label for="email">Email:</label> <input type="email" id="email"
								name="emailAddress" required>
						</div>
						<div class="form-group">
							<label for="password">Password:</label> <input type="password"
								id="password" name="password" required>
						</div>
						<div class="form-group">
							<button type="submit" class="login-button">Log In</button>
						</div>
						<div class="links">
							<a href="#">Forgot password?</a> <a href="register.jsp">Sign
								Up</a>
						</div>
						<div class="social-login">
							<button class="facebook">Login with Facebook</button>
							<button class="google">Login with Google</button>
							<button class="apple">Login with Apple</button>
						</div>
					</form>
				</div>
			</div>
		</div>

	</div>
</body>
</html>