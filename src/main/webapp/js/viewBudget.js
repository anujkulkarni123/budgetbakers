/**
 * 
 */
const Data = {
	budgetItems : null,
	expense : null
}


 window.onload = function() {
    fetchBudgetInfo();
    getExpenseInfo();
    
};

function fetchBudgetInfo()	{
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
        
       	Data.budgetItems =  data.budgetItems;
    })
    .catch(error => {
        console.error('Error fetching initial data:', error);
    });
}

function getExpenseInfo() {
    fetch('TrackBudget', { // Ensure this points to the correct endpoint for fetching budget data.
        method: 'POST'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        // Assuming the data object has a property named 'totalExpense' based on your servlet's JSON response.
        console.log('Total Expense:', data.totalExpense);
        console.log('Past Period Total Expense:', data.pastPeriodTotalExpense);
        console.log('Expenses by Category:', data.totalByCategory);
        
        Data.expense =  {
			total: data.totalExpense,
			categoryExpense: data.totalByCategory
		}
		
		formatData()
    })
    .catch(error => {
        console.error('Error fetching expense data:', error);
    });
}

function formatData() {
    const categories = [];
    const budgetData = [];
    const expenseData = [];

    // Assuming data.budgetItems is an array of objects with properties like {id: 1, category: 'Food', budgetLimit: 500}
    // And data.expense is an object like {categoryExpense: {'Food': 450, 'Housing': 1200, ...}}
    if (Data.budgetItems && Data.expense) {
        for (const item of Data.budgetItems) {
            // Add category to categories array
            categories.push(item.category);

            // Add budget limit to budgetData array
            budgetData.push(item.budgetLimit);

            // Try to find the expense for this category and add it to expenseData
            const expenseAmount = Data.expense.categoryExpense[item.category] || Data.expense.total; // default to 0 if not found
            expenseData.push(expenseAmount);
        }

        console.log("Categories:", categories);
        console.log("Budget Data:", budgetData);
        console.log("Expense Data:", expenseData);
    } else {
        console.log("Invalid or incomplete data provided.");
    }
    
    populateChart(categories, budgetData, expenseData);
}

function populateChart(categories, budgetData, expenseData)	{
	const ctx = document.getElementById('budgetVsExpensesChart').getContext('2d');
	const budgetVsExpensesChart = new Chart(ctx, {
	    type: 'bar',
	    data: {
	        labels: categories,
	        datasets: [{
	            label: 'Budget',
	            data: budgetData,
	            backgroundColor: 'rgba(54, 162, 235, 0.2)',
	            borderColor: 'rgba(54, 162, 235, 1)',
	            borderWidth: 1
	        }, {
	            label: 'Expenses',
	            data: expenseData,
	            backgroundColor: 'rgba(255, 99, 132, 0.2)',
	            borderColor: 'rgba(255, 99, 132, 1)',
	            borderWidth: 1
	        }]
	    },
	    options: {
	        scales: {
	            y: {
	                beginAtZero: true
	            }
	        },
	        responsive: true,
	        plugins: {
	            legend: {
	                position: 'top',
	            },
	            title: {
	                display: true,
	                text: 'Budget vs Actual Expenses by Category'
	            }
	        }
	    }
	});
}
	
