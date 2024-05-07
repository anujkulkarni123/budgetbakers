var globalData = {
	filterItems: [],
	categories: [],
	filterType: ""
};

document.addEventListener('DOMContentLoaded', function() {
	fetchDataAndInitializeAccordions();
	fetchRecords("")

	console.log("The filter type is: " + filterType);
	globalData.filterType = filterType;

	const applyFiltersButton = document.getElementById('applyFiltersButton');
	const clearFiltersButton = document.getElementById('clearFiltersButton');
	const resultsDiv = document.getElementById('results');

	applyFiltersButton.addEventListener('click', function() {
		let query = FilterQueryBuilder();
		fetchRecords(query)
	});


	clearFiltersButton.addEventListener('click', function() {
		document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
			checkbox.checked = false;
		});
		resultsDiv.innerHTML = `<p>Filters Cleared.</p>`;
		// Optionally, refresh data or reset views here
	});
});

function fetchRecords(query) {
	// Fetch with POST method
	fetch("Records", {
		method: 'POST',
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded', // Ensure form data is properly encoded
		},
		body: query
	}).then(response => response.json())
	
		.then(data => {
			console.log(data.records);
			updateResultsContainer(data.records); // Assuming 'records' is the key in your JSON response containing the records array
		})
		.catch(error => {
			console.error('Fetch error:', error);
		});
}

function updateResultsContainer(records) {
    const resultsContainer = document.getElementById('result-container');
    const sumSpan = document.getElementById('numberValue');
    resultsContainer.innerHTML = '';
    let totalSum = 0;

    if (records && records.length > 0) {
        records.forEach(record => {
            totalSum += parseFloat(record.amount);
            const cardDiv = document.createElement('div');
            cardDiv.className = 'card card-enhanced my-3 w-full';
            cardDiv.innerHTML = `
                <div class="card-body px-4">
                    <div class="row justify-content-between align-items-center">
                        <div class="col-md-3">
                            <input type="checkbox" value="${record.recordId}" class="form-check-input record-checkbox me-2">
                            <span>${record.subCategory}</span>
                        </div>
                        <div class="col-md-2">${record.type}</div>
                        <div class="col-md-2">${record.payee || ""}</div>
                        <div class="col-md-2">${record.note || ""}</div>
                        <div class="col-md-2">${record.amount.toFixed(2)}</div>
                        <div class="col-md-1">
                            <button class="btn btn-primary" onclick="editRecord('${record.recordId}')">
                                <i class="fas fa-edit"></i>
                            </button>
                        </div>
                    </div>
                </div>
            `;
            resultsContainer.appendChild(cardDiv);
        });
        // Add event listeners to each checkbox after appending them to the DOM
        resultsContainer.querySelectorAll('.record-checkbox').forEach(checkbox => {
            checkbox.addEventListener('change', handleRecordSelection);
        });
        sumSpan.textContent = totalSum.toFixed(2);
    } else {
        resultsContainer.innerHTML = '<p>No records found.</p>';
        sumSpan.textContent = '0.00';
    }
}



function fetchDataAndInitializeAccordions() {
	fetch('GetFilterItems') // Adjust this URL to your endpoint
		.then(response => {
			if (!response.ok) {
				throw new Error('Network response was not ok: ' + response.statusText);
			}
			return response.json();
		})
		.then(data => {
			globalData.filterItems = data.filterItems;
			globalData.categories = data.categories;
			initializeAccordions();
		})
		.catch(error => {
			console.error('Fetch error:', error);
			document.querySelector('.filterContainer .accordion').innerHTML = '<p>Error loading filter items.</p>';
		});
}

function initializeAccordions() {
	const filterContainer = document.querySelector('.filterContainer .accordion');
	filterContainer.innerHTML = '';

	globalData.filterItems.forEach((item, index) => {
		const accordionItem = createFilterItem(item, index);
		filterContainer.appendChild(accordionItem);
	});
}

function createFilterItem(item, index) {
	const accordionItem = document.createElement('div');
	accordionItem.className = 'accordion-item';

	const accordionHeader = document.createElement('h2');
	accordionHeader.className = 'accordion-header';
	accordionHeader.id = `heading${index}`;

	const button = document.createElement('button');
	button.className = 'accordion-button collapsed'; // Always start collapsed
	button.type = 'button';
	button.setAttribute('data-bs-toggle', 'collapse');
	button.setAttribute('data-bs-target', `#collapse${index}`);
	button.setAttribute('aria-expanded', 'false'); // Start with aria-expanded as false
	button.setAttribute('aria-controls', `collapse${index}`);
	button.textContent = item.filterName;

	const collapseDiv = document.createElement('div');
	collapseDiv.id = `collapse${index}`;
	collapseDiv.className = 'accordion-collapse collapse'; // Remove 'show' class for all
	collapseDiv.setAttribute('aria-labelledby', `heading${index}`);

	const accordionBody = document.createElement('div');
	accordionBody.className = 'accordion-body';

	accordionHeader.appendChild(button);
	collapseDiv.appendChild(accordionBody);
	accordionItem.appendChild(accordionHeader);
	accordionItem.appendChild(collapseDiv);

	if (item.filterName === "Categories") {
		renderAdditionalContent(accordionBody, index);
	}

	return accordionItem;
}


function renderAdditionalContent(container, index) {
	const categoriesAccordion = document.createElement('div');
	categoriesAccordion.className = "accordion";
	categoriesAccordion.id = `categoryAccordion`;

	for (let category in globalData.categories) {
		const subcategories = globalData.categories[category];
		const accordionItem = document.createElement('div');
		accordionItem.className = 'accordion-item';

		const header = document.createElement('h2');
		header.className = 'accordion-header';
		header.id = `heading${index}-${category}`;

		const headerButton = document.createElement('button');
		headerButton.className = 'accordion-button collapsed';
		headerButton.type = 'button';
		headerButton.setAttribute('data-bs-toggle', 'collapse');
		headerButton.setAttribute('data-bs-target', `#collapse${index}-${category}`);
		headerButton.setAttribute('aria-expanded', 'false');
		headerButton.setAttribute('aria-controls', `collapse${index}-${category}`);
		headerButton.textContent = category;

		header.appendChild(headerButton);

		const collapseContainer = document.createElement('div');
		collapseContainer.id = `collapse${index}-${category}`;
		collapseContainer.className = 'accordion-collapse collapse';
		collapseContainer.setAttribute('aria-labelledby', `heading${index}-${category}`);

		const body = document.createElement('div');
		body.className = 'accordion-body';

		const selectAllCheckbox = createSelectAllCheckbox(index, category);
		body.appendChild(selectAllCheckbox.wrapper);

		subcategories.forEach(subcategory => {
			const checkbox = createSubcategoryCheckbox(index, category, subcategory);
			body.appendChild(checkbox.wrapper);
		});

		collapseContainer.appendChild(body);
		accordionItem.appendChild(header);
		accordionItem.appendChild(collapseContainer);

		categoriesAccordion.appendChild(accordionItem);
	}

	container.appendChild(categoriesAccordion);
}

function createSelectAllCheckbox(index, category) {
	const checkbox = document.createElement('input');
	checkbox.type = 'checkbox';
	checkbox.className = 'form-check-input';
	checkbox.id = `selectAll-${index}-${category}`;
	checkbox.onclick = function() {
		toggleSubcategoryCheckboxes(this, index, category);
	};

	const label = document.createElement('label');
	label.className = 'form-check-label';
	label.htmlFor = checkbox.id;
	label.textContent = "Select All";

	const wrapper = document.createElement('div');
	wrapper.className = 'form-check form-check-select-all';
	wrapper.appendChild(checkbox);
	wrapper.appendChild(label);

	return { wrapper };
}

function createSubcategoryCheckbox(index, category, subcategory) {
	const checkbox = document.createElement('input');
	checkbox.type = 'checkbox';
	checkbox.className = 'form-check-input';
	checkbox.id = `check-${index}-${category}-${subcategory}`;
	checkbox.name = `${category}`;
	checkbox.value = subcategory;

	const label = document.createElement('label');
	label.className = 'form-check-label';
	label.htmlFor = checkbox.id;
	label.textContent = subcategory;

	const wrapper = document.createElement('div');
	wrapper.className = 'form-check';
	wrapper.appendChild(checkbox);
	wrapper.appendChild(label);

	return { wrapper };
}

function toggleSubcategoryCheckboxes(selectAllCheckbox, index, category) {
	const isChecked = selectAllCheckbox.checked;
	const allCategoryCheckboxes = document.querySelectorAll(`input[name='check-${category}']`);

	allCategoryCheckboxes.forEach(checkbox => {
		checkbox.checked = isChecked;
	});
}

function FilterQueryBuilder() {
	let queryString = '';

	const allCheckboxes = document.querySelectorAll('input[type="checkbox"]:checked');

	let queryData = {};

	allCheckboxes.forEach(checkbox => {
		const name = checkbox.name;
		const value = checkbox.value;

		if (!queryData[name]) {
			queryData[name] = [];
		}

		queryData[name].push(value);
	});

	for (let key in queryData) {
		if (queryData.hasOwnProperty(key)) {
			const values = queryData[key].join(',');
			if (queryString.length > 0) {
				queryString += '&';
			}
			queryString += `${encodeURIComponent(key)}=${encodeURIComponent(values)}`;
		}
	}
	if (queryString.length > 0) {
		queryString = `${globalData.filterType}?${queryString}`;
	}

	console.log(queryString);

	return queryString;
}