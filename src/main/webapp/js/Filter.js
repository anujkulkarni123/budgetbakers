var globalData = {
    filterItems: [],
    categories: [],
    filterType: ""
};

document.addEventListener('DOMContentLoaded', function() {
    fetchDataAndInitializeAccordions();
    
    console.log("The filter type is: " + filterType);
    globalData.filterType = filterType;
    
    const applyFiltersButton = document.getElementById('applyFiltersButton');
    const clearFiltersButton = document.getElementById('clearFiltersButton');
    const resultsDiv = document.getElementById('results');

    applyFiltersButton.addEventListener('click', function() {
        // Create a FormData object directly if your input fields are inside a <form> element
        // Alternatively, manually construct an object with the necessary data.
        const formData = new FormData();
        let query = FilterQueryBuilder();
        
        console.log(query);
     
        /*// Fetch with POST method
        fetch('http://example.com/api/filter', {
            method: 'POST',
            body: formData  // Make sure your server can handle FormData, or use JSON.stringify for JSON data
        })
        .then(response => response.json())
        .then(data => {
            resultsDiv.innerHTML = `<p>Filters Applied. Check console for data.</p>`;
            console.log("Filtered Data:", data);
        })
        .catch(error => {
            console.error('Error fetching data:', error);
            resultsDiv.innerHTML = `<p>Error applying filters. Check console.</p>`;
        });*/
    });

    clearFiltersButton.addEventListener('click', function() {
        document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
            checkbox.checked = false;
        });
        resultsDiv.innerHTML = `<p>Filters Cleared.</p>`;
        // Optionally, refresh data or reset views here
    });
});

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