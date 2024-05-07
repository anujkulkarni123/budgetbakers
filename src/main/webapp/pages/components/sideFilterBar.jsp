
<%
	String filterHeader = request.getParameter("filterType");

%>

<head>
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
    <script src="js/Filter.js"></script> <!-- Include your external JS file -->
    
    <script>
    	var filterType = `<%= filterHeader%>`;
    </script>
</head>

<body>
    <div class="card" style="min-height: 100%;">
        <div class="card-body">
            <h2><% out.println(filterHeader); %></h2>
            <button class="login-button">+ Add</button>
            <br><br>
            <input type="text" class="form-control form-input" placeholder="Search anything...">
            <label style="margin-top: 10px; font-size: 14px;">Filters</label>
            <div class="filterContainer">
               	<div class="d-flex justify-content-between">
	                <button class="btn btn-primary btn-sm login-button" id="applyFiltersButton">Apply Filters</button>
	    			<button class="btn btn-primary btn-sm login-button" id="clearFiltersButton">Clear Filters</button>
                </div>
                <div class="accordion" id="accordionExample">
                    <!-- Filter items will be added here dynamically by JavaScript -->
                </div>
            </div>
        </div>
    </div>
</body>
</html>
