<%@ page import="java.util.ArrayList"%>

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
		<h2 class="mt-3">Analytics</h2>

		<br> <br> <input type="text" class="form-control form-input"
			placeholder="Search anything..."> <label
			style="margin-top: 5px; font-size: 14px;">Filters</label>
		<div class="filterContainer ">
			<div class="d-flex justify-content-around">
				<button class="btn btn-success login-button" id="applyFiltersButton"
					onClick=>Apply Filters</button>
				<button class="btn btn-outline-primary btn-md login-button"
					id="clearFiltersButton">Clear Filters</button>
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
						<div class="accordion-body">Accounts</div>
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
						<div class="accordion-body">Categories</div>
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
								<input class="form-check-input" type="checkbox" value="CAD"
									id="flexCheckDefault"> <label class="form-check-label"
									for="flexCheckDefault"> CAD</label>
							</div>
							<div class="form-check">
								<input class="form-check-input" type="checkbox" value="USD"
									id="flexCheckDefault"> <label class="form-check-label"
									for="flexCheckDefault"> USD</label>
							</div>
						</div>
					</div>
				</div>
				<div class="accordion-item">
					<h2 class="accordion-header">
						<button class="accordion-button" type="button"
							data-bs-toggle="collapse" data-bs-target="#collapseFour"
							aria-expanded="true" aria-controls="#collapseFour">
							Record Types</button>
					</h2>
					<div id="collapseFour" class="accordion-collapse collapse">
						<div class="accordion-body">Record Types</div>
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
						<div class="accordion-body">Payment Types</div>
					</div>
				</div>
				<div class="accordion-item">
					<h2 class="accordion-header">
						<button class="accordion-button" type="button"
							data-bs-toggle="collapse" data-bs-target="#collapseSix"
							aria-expanded="true" aria-controls="#collapseSix">
							Record States</button>
					</h2>
					<div id="collapseSix" class="accordion-collapse collapse">
						<div class="accordion-body">Record States</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

