document.addEventListener('DOMContentLoaded', function() {
    loadTransactions();

    const serviceForm = document.getElementById('serviceForm');
    serviceForm.addEventListener('submit', handleSubmitTransaction);
});

function handleSubmitTransaction(e) {
    e.preventDefault();

    const transaction = {
        customerDetails: document.getElementById('customerName').value + ' - ' + document.getElementById('vehicleDetails').value,
        amount: parseFloat(document.getElementById('amount').value),
        paymentMethod: document.getElementById('paymentMethod').value,
        transactionDate: new Date()
    };

    fetch('/api/transactions', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(transaction)
    })
    .then(response => response.json())
    .then(data => {
        showAlert('Transaction saved successfully!', 'success');
        serviceForm.reset();
        loadTransactions();
    })
    .catch(error => {
        showAlert('Error saving transaction: ' + error.message, 'error');
    });
}

function loadTransactions() {
    fetch('/api/transactions')
        .then(response => response.json())
        .then(transactions => {
            const tbody = document.querySelector('#transactionsTable tbody');
            tbody.innerHTML = '';

            transactions.forEach(transaction => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${new Date(transaction.transactionDate).toLocaleDateString()}</td>
                    <td>${transaction.customerDetails.split('-')[0].trim()}</td>
                    <td>${transaction.customerDetails.split('-')[1].trim()}</td>
                    <td>${transaction.serviceType || 'N/A'}</td>
                    <td>₹${transaction.amount.toFixed(2)}</td>
                    <td>${transaction.paymentMethod}</td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(error => {
            showAlert('Error loading transactions: ' + error.message, 'error');
        });
}

function showAlert(message, type) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type}`;
    alertDiv.textContent = message;

    const container = document.querySelector('.container');
    container.insertBefore(alertDiv, container.firstChild);

    setTimeout(() => {
        alertDiv.remove();
    }, 3000);
}
