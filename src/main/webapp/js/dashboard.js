document.addEventListener("DOMContentLoaded", function(event) {
    createGauges();
    enableSortable();
});

function createGauges() {
    ['balanceGauge', 'cashFlowGauge', 'spendingGauge'].forEach(gauge => {
        createGauge(gauge, {
            balanceGauge: 65,
            cashFlowGauge: 80,
            spendingGauge: 45
        }[gauge], gauge.replace('Gauge', ''), 0, 100);
    });
}

function createGauge(elementId, value, label, min, max) {
    const element = document.getElementById(elementId);
    if (!element) return; // Guard clause if element does not exist

    const ctx = element.getContext('2d');
    new Chart(ctx, {
        type: 'doughnut',
        data: {
            datasets: [{
                data: [value, max - value],
                backgroundColor: ['#FF6384', '#eeeeee'],
                borderColor: ['#ffffff', '#ffffff'],
                borderWidth: 2
            }],
            labels: [label, '']
        },
        options: {
            circumference: 180,
            rotation: -90,
            cutout: '80%',
            plugins: {
                legend: { display: false },
                title: {
                    display: true,
                    text: label,
                    position: 'top'
                },
                tooltip: {
                    enabled: true,
                    callbacks: {
                        label: function(tooltipItem) {
                            return tooltipItem.label + ': ' + tooltipItem.formattedValue;
                        }
                    }
                }
            },
            animation: {
                onComplete: function() {
                    const chartInstance = this;
                    ctx.font = '16px Arial';
                    ctx.fillStyle = '#000';
                    ctx.textAlign = 'center';
                    ctx.textBaseline = 'middle';
                    ctx.fillText('$' + value.toFixed(2),
                        (chartInstance.chartArea.left + chartInstance.chartArea.right) / 2,
                        (chartInstance.chartArea.bottom + chartInstance.chartArea.top + 20) / 2);
                }
            }
        }
    });
}

function enableSortable() {
    $("#cardRow").sortable({
        placeholder: "ui-state-highlight",
        tolerance: 'pointer'
    });
    $("#cardRow").disableSelection();
}


window.selectedCards = [];

function addCard(encodedCardJson, evt) {
    if (evt) {
        evt.preventDefault();  // Prevent default action of the event (e.g., form submission)
        evt.stopPropagation(); // Stop the event from propagating further
    }

    var decodedJson = decodeURIComponent(encodedCardJson);
    var card = safeParseJSON(decodedJson);
    if (!card) return; // Exit if JSON parsing fails

    if (!window.selectedCards.some(e => e.name === card.name)) {
        window.selectedCards.push(card);
        renderCards();
    }
}

// Function to handle JSON parsing safely
function safeParseJSON(json) {
    try {
        return JSON.parse(json);
    } catch (e) {
        console.error("Failed to parse JSON:", json);
        return null;
    }
}

function renderCards() {
    const cardContainer = document.getElementById('cardRow');
    cardContainer.innerHTML = ''; // Clear the container to prepare for new content

    // Using requestAnimationFrame to manage rendering updates
    window.selectedCards.forEach((card, index) => {
        requestAnimationFrame(() => {
            const cardElement = document.createElement('div');
            cardElement.className = 'col-lg-4 col-md-6';
            cardElement.innerHTML = `
                <div class="card m-2">
                    <h5 class="card-title">${card.name}</h5>
                    <hr>
                    <div class="card-body">
                        ${card.json}
                    </div>
                </div>
            `;
            cardContainer.appendChild(cardElement);
        });
    });
}

