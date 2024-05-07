<head>
<script type="text/javascript"
	src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>
<script type="text/javascript"
	src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
<script type="text/javascript"
	src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
</head>
<div class="dropdown-center d-flex justify-content-center mt-2">
	<div class="dropdown">
		<button type="button" style="padding-right: 8rem;"
			class="btn btn-light dropdown-toggle mt-3 mb-3 "
			data-bs-auto-close="false" data-bs-toggle="dropdown"
			aria-expanded="false">Last 30 Days</button>
		<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">

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
				<div class="flex-container">
					<div class="row g-3">
						<input class="col-6" type="text" name="startDate" /> 
						<input class="col-6" type="text" name="endDate" />
					</div>
				</div>

				<button onclick="loadReport()" class="btn btn-sm btn-outline-primary mt-2">Apply</button>
			</div>
		</div>
	</div>

	<script>
		$(function() {
			$('input[name="startDate"]').daterangepicker({
				singleDatePicker : true,
				showDropdowns : true,
				minYear : 1901,
				maxYear : parseInt(moment().format('YYYY'), 10)
			}, function(start, end, label) {
				var years = moment().diff(start, 'years');
			});
		});
	</script>
	<script>
		$(function() {
			$('input[name="endDate"]').daterangepicker({
				singleDatePicker : true,
				showDropdowns : true,
				minYear : 1901,
				maxYear : parseInt(moment().format('YYYY'), 10)
			}, function(start, end, label) {
				var years = moment().diff(start, 'years');
			});
		});
	</script>
	<script>
	</script>
</div>

