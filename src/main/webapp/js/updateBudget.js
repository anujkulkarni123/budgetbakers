window.onload = function() {
    fetch('UpdateBudget', { // Replace 'GetData' with the actual endpoint.
        method: 'GET'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        console.log('Data fetched successfully:', data);
        populateCurrencies(data.currencies);
        populateBudgetItems(data.budgetItems);
    })
    .catch(error => {
        console.error('Error fetching initial data:', error);
    });
};

function populateCurrencies(currencies) {
    const select = document.getElementById('currencySelect');
    for (currency of currencies)	{
        const option = document.createElement('option');
        option.value = currency;
        option.textContent = currency;
        select.appendChild(option);
    };
}

function populateBudgetItems(budgetItems) {
    const tbody = document.querySelector('table tbody');
    budgetItems.forEach(item => {
        const tr = document.createElement('tr');

        const tdCategory = document.createElement('td');
        const inputCategory = document.createElement('input');
        inputCategory.type = 'text';
        inputCategory.value = item.category;
        inputCategory.className = 'form-control-plaintext';
        inputCategory.name="category"
        inputCategory.readOnly = true;
        tdCategory.appendChild(inputCategory);

        const tdLimit = document.createElement('td');
        const inputLimit = document.createElement('input');
        inputLimit.type = 'number';
        inputLimit.value = item.budgetLimit;
        inputLimit.className = 'form-control';
        inputLimit.name = 'budgetLimit';
        tdLimit.appendChild(inputLimit);

        const inputId = document.createElement('input');
        inputId.type = 'hidden';
        inputId.value = item.id;
        inputId.name = 'id';
        tr.appendChild(inputId);

        tr.appendChild(tdCategory);
        tr.appendChild(tdLimit);

        tbody.appendChild(tr);
    });
}

function updateAllBudgetItems() {
    const currency = document.getElementById('currencySelect').value;
    const rows = document.querySelectorAll('tbody tr');
    const updates = Array.from(rows).map(row => {
        const idInput = row.querySelector('input[name="id"]');
        const categoryInput = row.querySelector('input[name="category"]');
        const budgetLimitInput = row.querySelector('input[name="budgetLimit"]');

        if (!idInput || !categoryInput || !budgetLimitInput) {
            console.error('Missing inputs in row:', row);
        }

        return {
            id: idInput ? idInput.value : 'No ID',
            category: categoryInput ? categoryInput.value : 'No Category',
            budgetLimit: budgetLimitInput ? budgetLimitInput.value : 'No Budget',
            currency: currency
        };
    });

    console.log('Updates:', updates);

    fetch('UpdateBudget', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ updates: updates })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        alert('All budgets updated successfully!');
        console.log(data);
    })
    .catch(error => console.error('Error updating budgets:', error));
}