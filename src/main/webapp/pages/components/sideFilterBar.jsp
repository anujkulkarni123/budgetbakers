<%
    String filterType = request.getParameter("filterType");
	String[] filterItems = {"Accounts", "Categories", "Labels", "Currencies", "Record Types", "Payment Types", "Record States"};
%>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>


<div class="card" style="min-height: 100%;">
 	<div class="card-body">
   		<h2><% out.println(filterType); %></h2>
    	
    	<button class="login-button"> + Add </button>
    	
		<br></br>
		<div class="input-group-prepend">
			<input type="text" class="form-control form-input"
				placeholder="Search anything...">
		</div>
		
		<label style="margin-top: 10px; font-size: 14px;">Filters</label>
		
		<div class=filterContainer>
			<div class="accordion">
				<%
                    for (int i = 0; i < filterItems.length; i++) {
                        String item = filterItems[i];
                %>
                        <div class="accordion-item" >
                            <h2 class="accordion-header" id="heading<%= i %>">
                                <button class="accordion-button <%=(i != 0 ? "collapsed" : "")%>" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%= i %>" aria-expanded="<%= (i == 0 ? "true" : "false") %>" aria-controls="collapse<%= i %>">
                                    <%= item %>
                                </button>
                            </h2>
                            <div id="collapse<%= i %>" class="accordion-collapse collapse <%= (i == 0 ? "show" : "") %>" aria-labelledby="heading<%= i %>" data-bs-parent="#accordionExample">
                                <div class="accordion-body">
                                    Content for <%= item %>
                                </div>
                            </div>
                        </div>
                <%
                    }
                %>
			</div>
		</div>

	</div>
</div>