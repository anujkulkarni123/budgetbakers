/**
 * 
 */

function toggleSelectAll(checked) {
    document.querySelectorAll('.record-checkbox').forEach(checkbox => {
        checkbox.checked = checked;
    });
    updateConditionalDivVisibility();
}

function handleRecordSelection() {
    updateConditionalDivVisibility();
}

function updateConditionalDivVisibility() {
    const anyChecked = document.querySelector('.record-checkbox:checked') !== null;
    const conditionalDiv = document.getElementById('conditionalDiv');
    conditionalDiv.style.display = anyChecked ? 'block' : 'none';
}

function deleteSelectedRecords() {
    const selectedIds = Array.from(document.querySelectorAll('.record-checkbox:checked'))
                             .map(checkbox => checkbox.value);
    console.log("Deleting records with IDs:", selectedIds);
    // Implement the deletion logic here, possibly sending a request to your server
}

document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.record-checkbox').forEach(checkbox => {
        checkbox.addEventListener('change', handleRecordSelection);
    });
    document.getElementById('selectAll').addEventListener('change', function() {
        toggleSelectAll(this.checked);
    });
});