<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.util.Map"%>
<%@ page import="com.exavalu.entities.Card"%>

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
<body>
	<%
	    Gson gson = new Gson();
	%>
	
	<%@ include file="components/dashboardHeader.jsp"%>

	<div class="content">
		<div class="account-container">Add Accounts PlaceHolder</div>

		<div class="container mt-4">
			<div class="row" id="cardRow">
				<!-- This row will contain all the cards -->
				 
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
				                    <path
										d="M8 12a.5.5 0 000-1H3.5a.5.5 0 000 1H8zm-3.5-3a.5.5 0 010-1 .5.5 0 010 1zm3.5 3a.5.5 0 001-1V3.5a.5.5 0 00-1 0v7.5zm0-10a.5.5 0 011 0v7.5a.5.5 0 01-1 0V3.5z" />
				                </svg>
							</button>
							<!-- Text for the button -->
							<span class="add-card-text">Add Card</span>
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
							    <%
							    Map<String, List<Card>> cardsByType = (Map<String, List<Card>>) request.getAttribute("CARDS_BY_TYPE");
							    for (Map.Entry<String, List<Card>> entry : cardsByType.entrySet()) {
							        String type = entry.getKey();
							        List<Card> cardsForType = entry.getValue();
							    %>
							    <div class="mb-4">
							        <h6 class="text-uppercase"><%=type%></h6>
							        <%
							        for (Card card : cardsForType) {
							        %>
							        <div class="card m-2" onClick="addCard('<%= URLEncoder.encode(gson.toJson(card), "UTF-8") %>', event)">
									    <h5 class="card-title"><%= card.getName() %></h5>
									    <hr>
									    <div class="card-body ">
									        <%= card.getJson() %>
									    </div>
									</div>
							        <%
							        }
							        %>
							    </div>
							    <%
							    }
							    %>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary"
									data-bs-dismiss="modal">Close</button>
								<button type="button" class="btn btn-primary">Save
									Changes</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="js/dashboard.js"></script>
</body>
</html>
