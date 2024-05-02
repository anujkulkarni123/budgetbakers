<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.exavalu.entities.Account"%>
<%@ page import="com.exavalu.entities.AccountType"%>
<%@ page import="com.exavalu.entities.Currency"%>
<%@ page import="com.exavalu.entities.User"%>
<%@ page import="com.exavalu.entities.Category"%>
<%@ page import="com.exavalu.entities.SubCategory"%>

<%
User currentUserRecord = (User) session.getAttribute("USER");

ArrayList<Account> accountsRecord = (ArrayList<Account>) request.getAttribute("accounts");
ArrayList<AccountType> accountTypeListRecord = (ArrayList<AccountType>) request.getAttribute("accountTypeList");
ArrayList<Currency> currencyListRecord = (ArrayList<Currency>) request.getAttribute("currencyList");

ArrayList<Category> categoryList = (ArrayList<Category>) request.getAttribute("categoryList");
ArrayList<SubCategory> subCategoryList = (ArrayList<SubCategory>) request.getAttribute("SubCategoryList");

Map<Integer, String> accountTypeMapRecord = (Map<Integer, String>) request.getAttribute("accountTypeMap");
Map<Integer, String> currencyMapRecord = (Map<Integer, String>) request.getAttribute("currencyMap");
%>
<script>
function updateSubCategories(categoryId) {
    var subCategorySelect = document.getElementById('subCategory');
    subCategorySelect.innerHTML = '';  
    var categoryIdInt = parseInt(categoryId);

    <%for (SubCategory sub : subCategoryList) {%>
        if (<%=sub.getCategoryId()%> === categoryIdInt) {
            var option = document.createElement('option');
            option.value = '<%=sub.getSubCategoryId()%>';
            option.text = '<%=sub.getSubCategoryName()%>
	';
			subCategorySelect.appendChild(option);
		}
<%}%>
	}
	
</script>
<div id="recordPopup"
	style="display: none; position: fixed; left: 0; top: 0; width: 100%; height: 100%; overflow: auto; background-color: rgba(0, 0, 0, 0.4); z-index: 1050; justify-content: center; align-items: center;">
	<div
		style="background-color: #fefefe; border: 1px solid #888; width: 60%; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2); border-radius: 5px;">
		<div
			style="display: flex; justify-content: space-between; align-items: center; padding: 10px; padding-right: 20px; padding-left: 20px;">
			<h4 style="margin: 0;">ADD RECORD</h4>
			<span onclick="closeAddRecordPopup()"
				style="font-size: 28px; font-weight: bold; cursor: pointer;">&times;</span>
		</div>
		<div style="display: flex; height: 80%;">
			<div
				style="border-right: 1px solid #ccc; border-top: 1px solid #ccc;">
				<div style="background-color: rgb(77, 182, 172); padding-right: 10%; padding-left: 10%; padding-top: 5%; padding-bottom: 5%; max-height: 500px; overflow: auto; min-width: 700px;">

					<div style="margin-bottom: 10px; display: flex; ">
						<button id="btn-expense" class="section-button"
							onclick="showSection('expense')"
							style="flex: 1; border: solid white 1px; background-color: #fefefe; color: rgb(77, 182, 172); padding: 5px 10px; border-radius: 5px 0 0 5px;">Expense</button>
						<button id="btn-income" class="section-button"
							onclick="showSection('income')"
							style="flex: 1; border: solid white 1px; background-color: rgb(77, 182, 172); color: white; padding: 5px 10px;">Income</button>
						<button id="btn-transfer" class="section-button"
							onclick="showSection('transfer')"
							style="flex: 1; border: solid white 1px; background-color: rgb(77, 182, 172); color: white; padding: 5px 10px; border-radius: 0 5px 5px 0;">Transfer</button>
					</div>


					<div id="expense" class="content" style="display: block;">
						<div>
						<label for="expenseAccount">Account:</label> <select
							id="expenseAccount" name="expenseAccount">
							<%
							for (Account account : accountsRecord) {
							%>
							<option value="<%=account.getAccountId()%>"><%=account.getName()%></option>
							<%
							}
							%>
						</select>
						</div> <label for="expenseAmount">Amount:</label> <input type="number"
							id="expenseAmount" name="expenseAmount" required
							style="margin-right: 10px;"> <label for="expenseCurrency">Currency:</label>
						<select id="expenseCurrency" name="expenseCurrency">
							<%
							for (Currency currency : currencyListRecord) {
							%>
							<option value="<%=currency.getId()%>"><%=currency.getCurrencyName()%></option>
							<%
							}
							%>
						</select>
					</div>


					<div id="income" class="content" style="display: none;">

						<label for="incomeAccount">Account:</label> <select
							id="incomeAccount" name="incomeAccount">
							<%
							for (Account account : accountsRecord) {
							%>
							<option value="<%=account.getAccountId()%>"><%=account.getName()%></option>
							<%
							}
							%>
						</select> <label for="incomeAmount">Amount:</label> <input type="number"
							id="incomeAmount" name="incomeAmount" required
							style="margin-right: 10px;"> <label for="incomeCurrency">Currency:</label>
						<select id="incomeCurrency" name="incomeCurrency">
							<%
							for (Currency currency : currencyListRecord) {
							%>
							<option value="<%=currency.getId()%>"><%=currency.getCurrencyName()%></option>
							<%
							}
							%>
						</select>

					</div>



					<div id="transfer" class="content" style="display: none;">

						<label for="transferFromAccount">From Account:</label> <select
							id="transferFromAccount" name="transferFromAccount">
							<%
							for (Account account : accountsRecord) {
							%>
							<option value="<%=account.getAccountId()%>"><%=account.getName()%></option>
							<%
							}
							%>
						</select> <label for="transferToAccount">To Account:</label> <select
							id="transferToAccount" name="transferToAccount">
							<%
							for (Account account : accountsRecord) {
							%>
							<option value="<%=account.getAccountId()%>"><%=account.getName()%></option>
							<%
							}
							%>
						</select> <label for="transferAmount">Amount:</label> <input type="number"
							id="transferAmount" name="transferAmount" required>

					</div>

				</div>
				<div style="padding: 20px; padding-right: 30px; padding-left: 30px;">
					<!-- Category and Subcategory in one row -->
					<div
						style="display: flex; justify-content: space-between; margin-bottom: 10px;">
						<div style="margin-right: 10px; flex: 1;">
							<label for="category" class="form-label">Category</label> <select
								id="category" name="category" class="form-control"
								onchange="updateSubCategories(this.value)" required>
								<option selected>Choose...</option>
								<%
								for (Category cat : categoryList) {
								%>
								<option value="<%=cat.getCategoryId()%>"><%=cat.getCategoryName()%></option>
								<%
								}
								%>
							</select>
						</div>
						<div style="flex: 1;">
							<label for="subCategory" class="form-label">SubCategory</label> <select
								id="subCategory" name="subCategory" class="form-control"
								required>
								<!-- Options will be dynamically added based on the selected category -->
							</select>
						</div>
					</div>

					<!-- Date and Time in one row -->
					<div
						style="display: flex; justify-content: space-between; margin-bottom: 10px;">
						<div style="margin-right: 10px; flex: 1;">
							<label for="date" class="form-label">Date</label> <input
								type="date" id="date" name="date" class="form-control" required>
						</div>
						<div style="flex: 1;">
							<label for="time" class="form-label">Time</label> <input
								type="time" id="time" name="time" class="form-control" required>
						</div>
					</div>

					<!-- Center the Record button -->
					<div
						style="display: flex; justify-content: center; margin-top: 20px;">
						<button onclick="openAddAccountPopup()"
							style="background-color: #00aa70; color: white; padding: 10px 15px; width: 60%; border: none; border-radius: 20px; text-align: center; font-size: 16px; cursor: pointer; margin-bottom: 5px;">
							Add Record</button>
					</div>
				</div>

			</div>
			<div
				style="width: 40%; padding: 20px; background-color: #eff0f2; border-top: 1px solid #ccc;">
				<form>
					<div class="mb-3">
						<label for="payee" class="form-label">Payee</label> <input
							type="text" id="payee" name="payee" class="form-control">
					</div>
					<div class="mb-3">
						<label for="note" class="form-label">Note</label> <input
							type="text" id="note" name="note" class="form-control">
					</div>
					<div class="mb-3">
						<label for="paymentType" class="form-label">Payment Type</label> <select
							id="paymentType" name="paymentType" class="form-select">
							<option selected>Choose...</option>
							<option value="cash">Cash</option>
							<option value="credit_card">Credit Card</option>
							<option value="debit_card">Debit Card</option>
							<option value="other">Other</option>
						</select>
					</div>
					<div class="mb-3">
						<label for="paymentStatus" class="form-label">Payment
							Status</label> <select id="paymentStatus" name="paymentStatus"
							class="form-select">
							<option selected>Choose...</option>
							<option value="paid">Paid</option>
							<option value="pending">Pending</option>
							<option value="failed">Failed</option>
						</select>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
