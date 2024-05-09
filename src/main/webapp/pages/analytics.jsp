<%@ page import="com.exavalu.entities.User"%>
<%@ page import="com.exavalu.entities.Report"%>
<%@ page import="java.util.ArrayList"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Analytics</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<script src="https://kit.fontawesome.com/764c011dcf.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<link rel="stylesheet" type="text/css"
	href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />

<style>
html, body {
	height: 100%;
}

.container-fluid {
	min-height: 100%;
}

.card {
	border: none;
}
</style>
	<%
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
</head>
<body>
	<%@ include file="components/dashboardHeader.jsp"%>
	<div class="container-fluid d-flex flex-column bg-secondary-subtle">
		<div style="min-width: 201px;" class="dropdown-center d-flex justify-content-center mt-2">
			<div class="dropdown">
				<button id="toggleDuration" type="button" style="min-width: 201px; padding-right: 8rem;"
					class="btn btn-light dropdown-toggle mt-3 mb-3"
					data-bs-auto-close="false" data-bs-toggle="dropdown"
					aria-expanded="true">Last 30 Days</button>
				<div class="dropdown-menu" style="min-width: 201px;" aria-labelledby="dropdownMenuButton">

					<div class="flex-container px-4 py-3 row">
						<div class="form-check col-6">
							<input class="form-check-input" type="radio" name="duration"
								id="7days" value="7"> <label class="form-check-label"
								for="7days"> 7 Days </label>
						</div>
						<div class="form-check col-6">
							<input class="form-check-input" type="radio" name="duration"
								id="30days" value="30" checked> <label
								class="form-check-label" for="30days"> 30 Days </label>
						</div>
						<div class="form-check col-6">
							<input class="form-check-input" type="radio" name="duration"
								id="60days" value="60"> <label class="form-check-label"
								for="60days"> 60 Days </label>
						</div>
						<div class="form-check col-6">
							<input class="form-check-input" type="radio" name="duration"
								id="1year" value="365"> <label class="form-check-label"
								for="1year"> 1 Year </label>
						</div>
						<div class="form-check col-6">
							<input class="form-check-input" type="radio" name="duration"
								id="all" value="-1"> <label class="form-check-label"
								for="all"> All </label>
						</div>
						<div class="form-check col-6">
							<input class="form-check-input" type="radio" name="duration"
								id="all" value="-2"> <label class="form-check-label"
								for="all"> Custom </label>
						</div>
						<div class="container">
							<div class="row mt-1">
								<input class="col-6" type="text" id="startDate" name="startDate" />
								<input class="col-6" type="text" id="endDate" name="endDate" />
							</div>
						</div>

						<button onclick="loadReport()"
							class="btn btn-sm btn-outline-primary mt-2"
							data-toggle="dropdown">Apply</button>
					</div>
				</div>
			</div>
		</div>


		<div class="row flex-grow-1 justify-content-center">
			<div class="col-2 mb-3">
				<div style="height: 100%;" class="card border-none">
					<%@ include file="components/filterAnalyticsSidebar.jsp" %>
				</div>
			</div>
			<div class="col-9 mb-3">
				<div class="card h-100 border-none">
					<div class="card-header border-0">
						<select id="report-change" class="form-select ms-2" name="reportId"
							onchange="loadReport(this.value)" style="max-width: 18rem;">
							<%
							ArrayList<Report> analyses = (ArrayList<Report>) request.getAttribute("REPORTS");
							for (Report report : analyses) {
							%>
							<option value="<%=report.getAnalysisValue()%>"
								<%=report.getAnalysisValue() == 1 ? "selected" : ""%>><%=report.getAnalysisName()%></option>
							<%
							}
							%>
						</select>
					</div>
					<div class="card-body">
						<div id="report-body">
							<div id="income-expense-report">
								<%@ include file="components/incomeExpenseTable.jsp"%>
							</div>
							<canvas id="myChart"></canvas>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
	<script type="text/javascript"
		src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>
	<script type="text/javascript"
		src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
	<script type="text/javascript"
		src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.30.1/moment.min.js"></script>
	<script>
	function getFilterSettings() {
	    var filterSettings = {};

	    // Iterate through each accordion item to gather filter values
	    document.querySelectorAll('.accordion-item').forEach(function(group) {
	        var groupName = group.querySelector('.accordion-header button').textContent.trim();
	        var checkedValues = [];
	        group.querySelectorAll('input[type="checkbox"]:checked').forEach(function(checkbox) {
	            checkedValues.push(checkbox.value);
	        });
	        filterSettings[groupName.toLowerCase().replace(/\s+/g, '')] = checkedValues;
	    });

	    // Convert filter settings to JSON and return
	    return JSON.stringify(filterSettings);
	}

	function applyFilters() {
	    // Get the current filter settings
	    filterJSON = getFilterSettings();
	    console.log(filterJSON);

	    // Call the loadReport function with updated filters
	    loadReport();
	}

    // Function to clear filters
    function clearFilters() {
        // Reset all checkboxes to default state
        document.querySelectorAll('.accordion-item').forEach(function(group) {
            var allCheckbox = group.querySelector('input[value="all"]');
            if (allCheckbox) {
                allCheckbox.checked = true; // Reset to default "All" state
            }
            group.querySelectorAll('input[type="checkbox"]').forEach(function(checkbox) {
                if (checkbox.value !== "all") {
                    checkbox.checked = false; // Uncheck all other filters
                }
            });
        });

        // Trigger the applyFiltersButton click event to reapply filters
        document.getElementById("applyFiltersButton").click();
    }
   
    // when we apply filters, we want to call LoadReport to pass it these filters:
    // pass it as a JSON string
		let myChart;
		function loadReport(val) {
			filterJSON = getFilterSettings();
			console.log(filterJSON);
			// we need to update the dropdown button
			let dropdownButton = document.getElementById("toggleDuration");
			let checkedTime = document.querySelector('input[name = "duration"]:checked');
			
			if (checkedTime.value == "7") {
				dropdownButton.textContent = "Last 7 Days";	
			} 
			else if (checkedTime.value == "30") {
				dropdownButton.textContent = "Last 30 Days";	
			}
			else if (checkedTime.value == "60") {
				dropdownButton.textContent = "Last 60 Days";	
			}
			else if (checkedTime.value == "365") {
				dropdownButton.textContent = "Last Year";	
			} 
			else if (checkedTime.value == "-1") {
				dropdownButton.textContent = "All";	
			}
			else {
				dropdownButton.textContent = "Custom";	
			}
			
			console.log(checkedTime.value);
			let startDate;
			let endDate;
			let chartIds = [2,3,4,5,6,7];
			
			if (checkedTime.value == -2) {
				// need to grep the other two dates	
				startDate = document.getElementById("startDate");
				endDate = document.getElementById("endDate");
				console.log(startDate.value);
				console.log(endDate.value);

			} else {
				startDate = "";
				endDate = "";

			}
			// if we are just updating the duration, we need
			// load the current selected report
			if (typeof val === 'undefined') {
				let currentReportSelected = document
						.getElementById("report-change");
				val = currentReportSelected.value;
			}

			console.log("Report ID#: " + val);
			$.ajax({
				url : "LoadReport",
				type : "POST",
				data : {
					reportId : val,
					duration : checkedTime.value,
					customStartDate : startDate.value,
					customEndDate : endDate.value,
					filters: filterJSON,

				},
				dataType : chartIds.includes(val) ? "json" : "html", // might replace with val == 1 ?
				success : function(data) {
					if (val == 1) {
						$("#myChart").empty();
						$("#income-expense-report").html(data);
						$("#myChart").css("display", "none");
					} else if (val != 1) {
						console.log("getting chart with id: " + val);
						$("#income-expense-report").empty();
						$("#myChart").css("display", "block"); // need to re-enable the chart
						console.log("Getting chart info for report id#: " + val);
						const ctx = document.getElementById('myChart');

						if (myChart instanceof Chart) {
							myChart.destroy();
						}
						let reportName = $("#report-change option:selected").text();
						
						let parsedData = JSON.parse(data);
						let chartLabels = Object.keys(parsedData);
					    let chartData = Object.values(parsedData);
					    
						console.log("Labels: " + chartLabels);
						console.log("Data: " + chartData); 
						myChart = new Chart(ctx, {
							type : 'line',
							data : {
								parsing: false,
								labels : chartLabels,
								datasets : [ {
									fill : true,
									label : reportName,
									lineTension : 0.4,
									data : chartData,
									borderWidth : 1
								} ]
							},
							options : {
								scales : {
									y : {
										beginAtZero : false
									}
								}
							}
						});
					}
				},
				error : function(xhr, status, error) {
					console.error(status, error);
				}
			});

		};
		$(function() {
			$('input[name="startDate"]').daterangepicker({
				singleDatePicker : true,
				showDropdowns : true,
				minYear : 1973,
				maxYear : parseInt(moment().format('YYYY'), 10),
				locale : {
					format : 'YYYY-MM-DD'
				}
			});
		});
		$(function() {
			$('input[name="endDate"]').daterangepicker({
				singleDatePicker : true,
				showDropdowns : true,
				minYear : 1973,
				maxYear : parseInt(moment().format('YYYY'), 10),
				locale : {
					format : 'YYYY-MM-DD'
				}
			});
		});
	</script>
</body>
</html>

