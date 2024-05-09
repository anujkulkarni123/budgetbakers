<%@ page import="java.util.ArrayList"%>
<%@ page import="com.exavalu.entities.Category"%>
<%@ page import="com.exavalu.entities.Currency"%>
<%@ page import="com.exavalu.entities.Account"%>
<%@ page import="com.exavalu.entities.RecordState"%>

<%
ArrayList<Currency> sidebarCurrencies = (ArrayList<Currency>) request.getAttribute("SIDEBAR_CURRENCIES");
ArrayList<Category> sidebarCategories = (ArrayList<Category>) request.getAttribute("SIDEBAR_CATEGORIES");
ArrayList<String> sidebarPaymentTypes = (ArrayList<String>) request.getAttribute("SIDEBAR_PAYMENTTYPES");
ArrayList<RecordState> sidebarRecordStates = (ArrayList<RecordState>) request.getAttribute("SIDEBAR_RECORDSTATES");
ArrayList<String> sidebarRecordTypes = (ArrayList<String>) request.getAttribute("SIDEBAR_RECORDTYPES");
ArrayList<Account> sidebarAccounts = (ArrayList<Account>) request.getAttribute("SIDEBAR_ACCOUNTS");
%>

<style>
/* Secondary color class */
.bg-secondary-accordion {
	background-color: #6c757d;
}

.accordion-item.bg-secondary-accordion {
	background-color: #6c757d;
}

.accordion-button {
	background-color: #f8f9fa;
	color: #000; /* Default text color for accordion buttons */
}

/* Styles for accordion buttons when active */
.accordion-button:not(.collapsed) {
	background-color: #f8f9fa;
	color: #000; /* Text color for active accordion buttons */
}
</style>
<div class="card fs-6" style="min-height: 100%;">
	<div class="card-body">
		<h2 class="mt-3 mb-3">Analytics</h2>
		<div class="filterContainer ">
			<div class="d-flex justify-content-around mb-2">
				<button class="btn btn-success login-button" id="applyFiltersButton"
					onClick="applyFilters()">Apply Filters</button>
				<button class="btn btn-outline-primary btn-md login-button"
					id="clearFiltersButton" onClick="clearFilters()">Clear Filters</button>
			</div>
			<div class="accordion mt-4" id="accordionExample">
				<div class="accordion-item">
					<h2 class="accordion-header">
						<button class="accordion-button" type="button"
							data-bs-toggle="collapse" data-bs-target="#collapseOne"
							aria-expanded="true" aria-controls="collapseOne">
							Accounts</button>
					</h2>
					<div id="collapseOne" class="accordion-collapse collapse show">
						<div class="accordion-body">
							<div class="form-check">
								<input class="form-check-input" type="checkbox" value="all"
									checked id="flexCheckDefault"> <label
									class="form-check-label" for="flexCheckDefault"> All</label>
							</div>
							<%
							for (Account account : sidebarAccounts) {
							%>
							<div class="form-check">
								<input class="form-check-input" type="checkbox"
									value="<%=account.getAccountId()%>" id="flexCheckDefault">
								<label class="form-check-label" for="flexCheckDefault">
									<%=account.getAccountName()%></label>
							</div>
							<%
							}
							%>
						</div>
					</div>
				</div>
				<div class="accordion-item">
					<h2 class="accordion-header">
						<button class="accordion-button" type="button"
							data-bs-toggle="collapse" data-bs-target="#collapseTwo"
							aria-expanded="true" aria-controls="#collapseTwo">
							Categories</button>
					</h2>
					<div id="collapseTwo" class="accordion-collapse collapse">
						<div class="accordion-body">
							<div class="form-check">
								<input class="form-check-input" type="checkbox" value="all"
									checked id="flexCheckDefault"> <label
									class="form-check-label" for="flexCheckDefault"> All</label>
							</div>
							<%
							for (Category category : sidebarCategories) {
							%>
							<div class="form-check">
								<input class="form-check-input" type="checkbox"
									value="<%=category.getCategoryName()%>" id="flexCheckDefault">
								<label class="form-check-label" for="flexCheckDefault">
									<%=category.getCategoryName()%></label>
							</div>
							<%
							}
							%>
						</div>
					</div>
				</div>
				<div class="accordion-item">
					<h2 class="accordion-header">
						<button class="accordion-button" type="button"
							data-bs-toggle="collapse" data-bs-target="#collapseThree"
							aria-expanded="true" aria-controls="#collapseThree">
							Currencies</button>
					</h2>
					<div id="collapseThree" class="accordion-collapse collapse">
						<div class="accordion-body">
							<div class="form-check">
								<input class="form-check-input" type="checkbox" value="all"
									id="flexCheckDefault" checked> <label
									class="form-check-label" for="flexCheckDefault"> All</label>
							</div>
							<%
							for (Currency currency : sidebarCurrencies) {
							%>
							<div class="form-check">
								<input class="form-check-input" type="checkbox"
									value="<%=currency.getCurrencyName()%>" id="flexCheckDefault"> <label
									class="form-check-label" for="flexCheckDefault"> <%=currency.getCurrencyName()%></label>
							</div>
							<%
							}
							%>
						</div>
					</div>
				</div>
				<div class="accordion-item">
					<h2 class="accordion-header">
						<button class="accordion-button" type="button"
							data-bs-toggle="collapse" data-bs-target="#collapseFour"
							aria-expanded="true" aria-controls="#collapseFour">
							Record State</button>
					</h2>
					<div id="collapseFour" class="accordion-collapse collapse">
						<div class="accordion-body">
							<div class="form-check">
								<input class="form-check-input" type="checkbox" value="all"
									checked id="flexCheckDefault"> <label
									class="form-check-label" for="flexCheckDefault"> All</label>
							</div>
							<%
							for (RecordState recordState : sidebarRecordStates) {
							%>
							<div class="form-check">
								<input class="form-check-input" type="checkbox"
									value="<%=recordState.getId()%>" id="flexCheckDefault">
								<label class="form-check-label" for="flexCheckDefault">
									<%=recordState.getRecordType()%></label>
							</div>
							<%
							}
							%>
						</div>
					</div>
				</div>
				<div class="accordion-item">
					<h2 class="accordion-header">
						<button class="accordion-button" type="button"
							data-bs-toggle="collapse" data-bs-target="#collapseFive"
							aria-expanded="true" aria-controls="#collapseFive">
							Payment Types</button>
					</h2>
					<div id="collapseFive" class="accordion-collapse collapse">
						<div class="accordion-body">
							<div class="form-check">
								<input class="form-check-input" type="checkbox" value="all"
									id="flexCheckDefault" checked> <label
									class="form-check-label" for="flexCheckDefault"> All</label>
							</div>
							<%
							for (String paymentType : sidebarPaymentTypes) {
							%>
							<div class="form-check">
								<input class="form-check-input" type="checkbox"
									value="<%=paymentType%>" id="flexCheckDefault"> <label
									class="form-check-label" for="flexCheckDefault"> <%=paymentType%></label>
							</div>
							<%
							}
							%>
						</div>
					</div>
				</div>
				<div class="accordion-item">
					<h2 class="accordion-header">
						<button class="accordion-button" type="button"
							data-bs-toggle="collapse" data-bs-target="#collapseSix"
							aria-expanded="true" aria-controls="#collapseSix">
							Record Types</button>
					</h2>
					<div id="collapseSix" class="accordion-collapse collapse">
						<div class="accordion-body">
							<div class="form-check">
								<input class="form-check-input" type="checkbox" value="all"
									checked id="flexCheckDefault"> <label
									class="form-check-label" for="flexCheckDefault"> All</label>
							</div>
							<%
							for (String recordType : sidebarRecordTypes) {
							%>
							<div class="form-check">
								<input class="form-check-input" type="checkbox"
									value="<%=recordType%>" id="flexCheckDefault"> <label
									class="form-check-label" for="flexCheckDefault"> <%=recordType%></label>
							</div>
							<%
							}
							%>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
