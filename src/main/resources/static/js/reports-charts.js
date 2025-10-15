// Initialize charts and data
let revenueChart, paymentMethodsChart;
let currentStartDate, currentEndDate;

document.addEventListener('DOMContentLoaded', function() {
    // Initialize date range picker
    $('#dateRange').daterangepicker({
        startDate: moment().subtract(30, 'days'),
        endDate: moment(),
        ranges: {
            'Today': [moment(), moment()],
            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
            'This Month': [moment().startOf('month'), moment().endOf('month')],
            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        }
    }, function(start, end) {
        currentStartDate = start;
        currentEndDate = end;
        loadReportData();
    });

    // Initialize charts
    initializeCharts();

    // Load initial data
    loadReportData();
});

function initializeCharts() {
    // Revenue Chart
    const revenueCtx = document.getElementById('revenueChart').getContext('2d');
    revenueChart = new Chart(revenueCtx, {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: 'Daily Revenue',
                data: [],
                borderColor: '#2196F3',
                backgroundColor: 'rgba(33, 150, 243, 0.1)',
                borderWidth: 2,
                fill: true,
                tension: 0.4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(0, 0, 0, 0.05)'
                    }
                }
            },
            plugins: {
                legend: {
                    display: false
                }
            }
        }
    });

    // Payment Methods Chart
    const paymentCtx = document.getElementById('paymentMethodsChart').getContext('2d');
    paymentMethodsChart = new Chart(paymentCtx, {
        type: 'doughnut',
        data: {
            labels: [],
            datasets: [{
                data: [],
                backgroundColor: [
                    '#2196F3',
                    '#4CAF50',
                    '#FFC107',
                    '#F44336',
                    '#9C27B0'
                ]
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

function loadReportData() {
    const reportType = document.getElementById('reportType').value;

    if (reportType === 'sales') {
        loadSalesReport();
    } else if (reportType === 'business') {
        loadBusinessIntelligence();
    } else {
        loadSupplierReport();
    }
}

function loadSalesReport() {
    fetch(`/api/reports/sales?startDate=${currentStartDate.toISOString()}&endDate=${currentEndDate.toISOString()}`)
        .then(response => response.json())
        .then(data => {
            updateRevenueChart(data.dailyRevenue);
            updatePaymentMethodsChart(data.paymentMethodDistribution);
            updateTransactionsTable(data.transactions);
        })
        .catch(error => console.error('Error loading sales report:', error));
}

function loadBusinessIntelligence() {
    fetch('/api/reports/business-intelligence')
        .then(response => response.json())
        .then(data => {
            updateRevenueChart(data.monthlyRevenue, true);
            updateGrowthRates(data.monthlyGrowthRates);
            updateTopItems(data.topPerformingItems);
        })
        .catch(error => console.error('Error loading business intelligence:', error));
}

function updateRevenueChart(data, isMonthly = false) {
    const labels = Object.keys(data);
    const values = Object.values(data);

    revenueChart.data.labels = labels;
    revenueChart.data.datasets[0].data = values;
    revenueChart.data.datasets[0].label = isMonthly ? 'Monthly Revenue' : 'Daily Revenue';
    revenueChart.update();
}

function updatePaymentMethodsChart(data) {
    const labels = Object.keys(data);
    const values = Object.values(data);

    paymentMethodsChart.data.labels = labels;
    paymentMethodsChart.data.datasets[0].data = values;
    paymentMethodsChart.update();
}

function updateTransactionsTable(transactions) {
    const tbody = document.querySelector('#transactionsTable tbody');
    tbody.innerHTML = '';

    transactions.forEach(transaction => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${formatDate(transaction.transactionDate)}</td>
            <td>INV-${transaction.id}</td>
            <td>$${transaction.amount.toFixed(2)}</td>
            <td>${transaction.paymentMethod}</td>
            <td><span class="badge badge-${getStatusBadgeClass(transaction.status)}">${transaction.status}</span></td>
            <td>
                <button class="btn btn-sm btn-primary" onclick="viewInvoice(${transaction.id})">
                    <i class="fas fa-file-invoice"></i>
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function viewInvoice(transactionId) {
    fetch(`/api/reports/invoice/${transactionId}`)
        .then(response => response.json())
        .then(data => {
            // Handle invoice display (e.g., open in new window or modal)
            window.open(`/invoice/${transactionId}`, '_blank');
        })
        .catch(error => console.error('Error loading invoice:', error));
}

function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function getStatusBadgeClass(status) {
    switch (status.toUpperCase()) {
        case 'COMPLETED':
            return 'success';
        case 'PENDING':
            return 'warning';
        case 'CANCELLED':
            return 'danger';
        default:
            return 'secondary';
    }
}

// Export functionality
function exportReport() {
    const reportType = document.getElementById('reportType').value;
    const format = document.getElementById('exportFormat').value;

    fetch(`/api/reports/export?type=${reportType}&format=${format}&startDate=${currentStartDate.toISOString()}&endDate=${currentEndDate.toISOString()}`)
        .then(response => response.blob())
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `report-${reportType}-${currentStartDate.format('YYYY-MM-DD')}.${format}`;
            document.body.appendChild(a);
            a.click();
            a.remove();
        })
        .catch(error => console.error('Error exporting report:', error));
}
