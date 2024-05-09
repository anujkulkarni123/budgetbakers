
<%@ page import="java.util.List"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.exavalu.entities.Card"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.exavalu.entities.Account"%>
<%@ page import="com.exavalu.entities.AccountType"%>
<%@ page import="com.exavalu.entities.Currency"%>
<%@ page import="com.exavalu.entities.User"%>
<%@ page import="com.exavalu.entities.Category"%>
<%@ page import="com.exavalu.entities.SubCategory"%>
<!DOCTYPE html>
<html>
<head>
<!-- Load Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>


<meta charset="UTF-8">
<title>Dashboard</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/dashboard.css">


</head>
<body class="bg-secondary-subtle">
	<%
	Gson gson = new Gson();
	
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
    
	%>


	<%@ include file="components/dashboardHeader.jsp"%>

	<div class="content">
		<div class="container mt-4 ">
			<div class="row">
				<div class="row" id="cardRow">

				</div>

				<div class="col-12">
					<!-- Add Card Placeholder -->
					<div class="card m-2 add-card" id="addCard">
						<div
							class="card-body d-flex justify-content-center align-items-center flex-column">
							<!-- Plus icon button (Bootstrap icon can be used if available) -->
							<button type="button"
						        class="btn btn-outline-primary rounded-circle mb-2 add-card-button"
						        data-bs-toggle="modal" data-bs-target="#addCardModal">
						    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
						         fill="currentColor" class="bi bi-plus" viewBox="0 0 16 16">
						        <path d="M8 12a.5.5 0 000-1H3.5a.5.5 0 000 1H8zm-3.5-3a.5.5 0 010-1 .5.5 0 010 1zm3.5 3a.5.5 0 001-1V3.5a.5.5 0 00-1 0v7.5zm0-10a.5.5 0 011 0v7.5a.5.5 0 01-1 0V3.5z" />
						    </svg>
						</button>
							<!-- Text for the button -->
							<span class="add-card-text">Add Card</span>
						</div>
					</div>
				</div>
			</div>

			<!-- Modal -->
			<div class="modal fade" id="addCardModal" tabindex="-1"
				aria-labelledby="addCardModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title" id="addCardModalLabel">Add New Card</h5>
							<button type="button" class="btn-close" data-bs-dismiss="modal"
								aria-label="Close"></button>
						</div>
						<div class="modal-body">
							
						</div> 
						<div class="modal-footer">
							<button type="button" class="btn btn-secondary"
								data-bs-dismiss="modal">Close</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="js/dashboard.js"></script>
</body>
</html>
