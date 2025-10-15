let performanceChart, paymentStatusChart;

document.addEventListener('DOMContentLoaded', function() {
    initializeCharts();
    loadTopSuppliers();
    setupEventListeners();
});

function initializeCharts() {
    // Performance Metrics Chart
    const perfCtx = document.getElementById('performanceChart').getContext('2d');
    performanceChart = new Chart(perfCtx, {
        type: 'radar',
        data: {
            labels: ['Quality', 'On-Time Delivery', 'Cost Efficiency', 'Communication', 'Reliability'],
            datasets: [{
                label: 'Current Period',
                data: [],
                borderColor: '#2196F3',
                backgroundColor: 'rgba(33, 150, 243, 0.2)',
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                r: {
                    beginAtZero: true,
                    max: 5
                }
            }
        }
    });

    // Payment Status Chart
    const paymentCtx = document.getElementById('paymentStatusChart').getContext('2d');
    paymentStatusChart = new Chart(paymentCtx, {
        type: 'doughnut',
        data: {
            labels: ['Paid', 'Pending', 'Overdue'],
            datasets: [{
                data: [],
                backgroundColor: ['#4CAF50', '#FFC107', '#F44336']
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

function loadTopSuppliers() {
    fetch('/api/supplier/top-performers')
        .then(response => response.json())
        .then(data => {
            updateSuppliersTable(data);
        })
        .catch(error => console.error('Error loading top suppliers:', error));
}

function updateSuppliersTable(suppliers) {
    const tbody = document.querySelector('#topSuppliersTable tbody');
    tbody.innerHTML = '';

    suppliers.forEach(supplier => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${supplier.supplier.name}</td>
            <td>
                <div class="star-rating">
                    ${getStarRating(supplier.averageQualityRating)}
                </div>
            </td>
            <td>${supplier.onTimeDeliveryPercentage.toFixed(1)}%</td>
            <td>$${supplier.averageDeliveryCost.toFixed(2)}</td>
            <td><span class="badge badge-${getSupplierStatusBadge(supplier.supplier.status)}">${supplier.supplier.status}</span></td>
            <td>
                <button class="btn btn-sm btn-primary" onclick="viewSupplierDetails(${supplier.supplier.id})">
                    <i class="fas fa-eye"></i>
                </button>
                <button class="btn btn-sm btn-success" onclick="evaluateSupplier(${supplier.supplier.id})">
                    <i class="fas fa-star"></i>
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function getStarRating(rating) {
    const fullStars = Math.floor(rating);
    const halfStar = rating % 1 >= 0.5;
    let stars = '';

    for (let i = 0; i < 5; i++) {
        if (i < fullStars) {
            stars += '<i class="fas fa-star text-warning"></i>';
        } else if (i === fullStars && halfStar) {
            stars += '<i class="fas fa-star-half-alt text-warning"></i>';
        } else {
            stars += '<i class="far fa-star text-warning"></i>';
        }
    }
    return stars;
}

function evaluateSupplier(supplierId) {
    document.querySelector('#evaluationForm input[name="supplierId"]').value = supplierId;
    $('#evaluateModal').modal('show');
}

function saveEvaluation() {
    const form = document.getElementById('evaluationForm');
    const formData = new FormData(form);
    const evaluation = Object.fromEntries(formData.entries());

    fetch('/api/supplier/evaluate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(evaluation)
    })
    .then(response => {
        if (response.ok) {
            $('#evaluateModal').modal('hide');
            showAlert('Evaluation saved successfully', 'success');
            loadTopSuppliers();
            if (evaluation.supplierId) {
                loadSupplierMetrics(evaluation.supplierId);
            }
        } else {
            throw new Error('Failed to save evaluation');
        }
    })
    .catch(error => {
        showAlert('Error saving evaluation: ' + error.message, 'danger');
    });
}

function saveSupplier() {
    const form = document.getElementById('supplierForm');
    const formData = new FormData(form);
    const supplier = Object.fromEntries(formData.entries());

    fetch('/api/suppliers', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(supplier)
    })
    .then(response => {
        if (response.ok) {
            $('#addSupplierModal').modal('hide');
            showAlert('Supplier added successfully', 'success');
            loadTopSuppliers();
            form.reset();
        } else {
            throw new Error('Failed to add supplier');
        }
    })
    .catch(error => {
        showAlert('Error adding supplier: ' + error.message, 'danger');
    });
}

function viewSupplierDetails(supplierId) {
    loadSupplierMetrics(supplierId);
    loadSupplierPayments(supplierId);
}

function loadSupplierMetrics(supplierId) {
    fetch(`/api/supplier/performance/${supplierId}`)
        .then(response => response.json())
        .then(data => {
            updatePerformanceChart(data);
        })
        .catch(error => console.error('Error loading supplier metrics:', error));
}

function loadSupplierPayments(supplierId) {
    fetch(`/api/supplier/payments/${supplierId}`)
        .then(response => response.json())
        .then(data => {
            updatePaymentStatusChart(data);
        })
        .catch(error => console.error('Error loading supplier payments:', error));
}

function updatePerformanceChart(data) {
    performanceChart.data.datasets[0].data = [
        data.averageQualityRating,
        data.onTimeDeliveryPercentage / 20, // Convert percentage to 5-point scale
        5 - (data.averageDeliveryCost / 100), // Convert cost to inverse 5-point scale
        data.communicationRating || 0,
        data.reliabilityRating || 0
    ];
    performanceChart.update();
}

function updatePaymentStatusChart(data) {
    paymentStatusChart.data.datasets[0].data = [
        data.totalPaid,
        data.totalPending,
        data.totalOverdue || 0
    ];
    paymentStatusChart.update();
}

function getSupplierStatusBadge(status) {
    switch (status.toUpperCase()) {
        case 'ACTIVE':
            return 'success';
        case 'INACTIVE':
            return 'danger';
        case 'PENDING':
            return 'warning';
        default:
            return 'secondary';
    }
}

function showAlert(message, type) {
    const alert = document.createElement('div');
    alert.className = `alert alert-${type} alert-dismissible fade show`;
    alert.innerHTML = `
        ${message}
        <button type="button" class="close" data-dismiss="alert">&times;</button>
    `;

    const container = document.querySelector('.container');
    container.insertBefore(alert, container.firstChild);

    setTimeout(() => {
        alert.remove();
    }, 3000);
}

function setupEventListeners() {
    // Add any additional event listeners here
    document.getElementById('supplierForm').addEventListener('submit', function(e) {
        e.preventDefault();
        saveSupplier();
    });

    document.getElementById('evaluationForm').addEventListener('submit', function(e) {
        e.preventDefault();
        saveEvaluation();
    });
}
