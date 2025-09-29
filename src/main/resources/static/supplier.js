// Mock Data
const supplierData = {
    name: "TechDistributor Inc.",
    purchaseRequests: [
        {
            id: "REQ001",
            part_id: "RAM001",
            part_name: "Corsair Vengeance LPX 32GB",
            quantity: 20,
            estimated_cost: 2599.80,
            supplier: "TechDistributor Inc.",
            status: "pending",
            created_date: "2024-01-15",
            delivery: null
        },
        {
            id: "REQ002",
            part_id: "GPU001",
            part_name: "NVIDIA RTX 4070",
            quantity: 5,
            estimated_cost: 2999.95,
            supplier: "TechDistributor Inc.",
            status: "approved",
            created_date: "2024-01-16",
            delivery: null
        }
    ],
    supplierParts: [
        {
            part_id: "RAM001",
            name: "Corsair Vengeance LPX 32GB",
            brand: "Corsair",
            category: "RAM",
            supplier_price: 125.00,
            available_stock: 50,
            supplier_id: "SUP001",
            min_order_quantity: 1
        },
        {
            part_id: "GPU001",
            name: "NVIDIA RTX 4070",
            brand: "NVIDIA",
            category: "GPU",
            supplier_price: 580.00,
            available_stock: 15,
            supplier_id: "SUP001",
            min_order_quantity: 1
        },
        {
            part_id: "SSD002",
            name: "Samsung 980 PRO 2TB",
            brand: "Samsung",
            category: "Storage",
            supplier_price: 149.99,
            available_stock: 30,
            supplier_id: "SUP001",
            min_order_quantity: 1
        },
        {
            part_id: "CPU002",
            name: "Intel Core i9-13900K",
            brand: "Intel",
            category: "CPU",
            supplier_price: 580.00,
            available_stock: 8,
            supplier_id: "SUP001",
            min_order_quantity: 1
        }
    ]
};

let currentRequest = null;

// Initialize the application
function init() {
    document.getElementById('supplier-name').textContent = supplierData.name;

    // Set minimum date to today
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('delivery-date').min = today;

    updateStats();
    renderRequests();
    renderInventory();

    // Add event listeners
    document.getElementById('inventory-search').addEventListener('input', handleInventorySearch);
    document.getElementById('delivery-form').addEventListener('submit', handleDeliverySubmit);
    document.getElementById('delivered-quantity').addEventListener('input', updateQuantityHint);

    // Close modal when clicking outside
    document.getElementById('delivery-modal').addEventListener('click', function(e) {
        if (e.target === this) {
            closeDeliveryModal();
        }
    });
}

// Update stats in header
function updateStats() {
    const partsCount = supplierData.supplierParts.length;
    const pendingCount = supplierData.purchaseRequests.filter(req =>
        req.supplier === supplierData.name && req.status === "pending"
    ).length;
    const readyCount = supplierData.purchaseRequests.filter(req =>
        req.supplier === supplierData.name && req.status === "approved"
    ).length;

    document.getElementById('parts-count').textContent = `${partsCount} Parts Available`;
    document.getElementById('pending-count').textContent = `${pendingCount} Pending Reviews`;
    document.getElementById('ready-count').textContent = `${readyCount} Ready to Ship`;

    // Show/hide badges based on counts
    const pendingBadge = document.getElementById('pending-badge');
    const readyBadge = document.getElementById('ready-badge');

    if (pendingCount > 0) {
        pendingBadge.style.display = 'flex';
    } else {
        pendingBadge.style.display = 'none';
    }

    if (readyCount > 0) {
        readyBadge.style.display = 'flex';
    } else {
        readyBadge.style.display = 'none';
    }
}

// Switch between tabs
function switchTab(tabName) {
    // Update tab buttons
    document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));
    document.querySelector(`[onclick="switchTab('${tabName}')"]`).classList.add('active');

    // Update tab content
    document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));
    document.getElementById(`${tabName}-tab`).classList.add('active');
}

// Render purchase requests
function renderRequests() {
    const requestsList = document.getElementById('requests-list');
    const myRequests = supplierData.purchaseRequests.filter(req =>
        req.supplier === supplierData.name &&
        (req.status === "pending" || req.status === "approved" || req.status === "delivered" || req.status === "partially_delivered")
    );

    if (myRequests.length === 0) {
        requestsList.innerHTML = `
      <div class="empty-state">
        <p>No purchase requests found for your company.</p>
      </div>
    `;
        return;
    }

    requestsList.innerHTML = myRequests.map(request => `
    <div class="request-card">
      <div class="request-header">
        <div class="request-info">
          <h4>
            ${request.part_name}
            ${getStatusBadge(request.status)}
          </h4>
          <div class="request-details">
            <div class="request-detail">
              <span class="request-detail-label">Request ID:</span>
              <span>${request.id}</span>
            </div>
            <div class="request-detail">
              <span class="request-detail-label">Quantity:</span>
              <span>${request.quantity}</span>
            </div>
            <div class="request-detail">
              <span class="request-detail-label">Est. Cost:</span>
              <span>$${request.estimated_cost.toFixed(2)}</span>
            </div>
            <div class="request-detail">
              <span class="request-detail-label">Created:</span>
              <span>${formatDate(request.created_date)}</span>
            </div>
          </div>
          ${request.notes ? `
            <div class="request-notes">
              <span class="request-notes-label">Notes:</span> ${request.notes}
            </div>
          ` : ''}
          ${request.delivery ? renderDeliveryInfo(request.delivery) : ''}
        </div>
        ${request.status === "approved" && !request.delivery ? `
          <button class="btn btn-primary" onclick="openDeliveryModal('${request.id}')">
            Schedule Delivery
          </button>
        ` : ''}
      </div>
    </div>
  `).join('');
}

// Render delivery information
function renderDeliveryInfo(delivery) {
    return `
    <div class="delivery-info">
      <h5>Delivery Information</h5>
      <div class="delivery-details">
        <div>Delivery Date: ${formatDate(delivery.delivery_date)}</div>
        <div>Delivery Time: ${delivery.delivery_time}</div>
        <div>Quantity: ${delivery.delivered_quantity}</div>
        <div>Status: ${delivery.delivery_status}</div>
        ${delivery.tracking_number ? `<div>Tracking: ${delivery.tracking_number}</div>` : ''}
      </div>
      ${delivery.supplier_notes ? `
        <div class="delivery-notes">
          Notes: ${delivery.supplier_notes}
        </div>
      ` : ''}
    </div>
  `;
}

// Get status badge HTML
function getStatusBadge(status) {
    const badges = {
        'pending': '<span class="badge badge-secondary">Pending Review</span>',
        'approved': '<span class="badge badge-default">Approved - Ready to Ship</span>',
        'delivered': '<span class="badge badge-success">Delivered</span>',
        'partially_delivered': '<span class="badge badge-warning">Partial Delivery</span>',
        'rejected': '<span class="badge badge-destructive">Rejected</span>'
    };
    return badges[status] || `<span class="badge badge-secondary">${status}</span>`;
}

// Render inventory table
function renderInventory(searchTerm = '') {
    const tableBody = document.getElementById('inventory-table-body');
    let filteredParts = supplierData.supplierParts;

    if (searchTerm) {
        filteredParts = supplierData.supplierParts.filter(part =>
            part.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
            part.brand.toLowerCase().includes(searchTerm.toLowerCase()) ||
            part.category.toLowerCase().includes(searchTerm.toLowerCase())
        );
    }

    tableBody.innerHTML = filteredParts.map(part => `
    <tr>
      <td>${part.part_id}</td>
      <td class="font-medium">${part.name}</td>
      <td>${part.brand}</td>
      <td>${part.category}</td>
      <td>$${part.supplier_price.toFixed(2)}</td>
      <td>${part.available_stock}</td>
      <td>
        ${part.available_stock > 0
        ? '<span class="badge badge-in-stock">In Stock</span>'
        : '<span class="badge badge-out-stock">Out of Stock</span>'
    }
      </td>
    </tr>
  `).join('');
}

// Handle inventory search
function handleInventorySearch(e) {
    renderInventory(e.target.value);
}

// Open delivery modal
function openDeliveryModal(requestId) {
    const request = supplierData.purchaseRequests.find(req => req.id === requestId);
    if (!request) return;

    currentRequest = request;

    // Update modal content
    document.getElementById('modal-description').textContent =
        `${request.part_name} - Quantity: ${request.quantity}`;

    // Reset form
    document.getElementById('delivery-form').reset();
    document.getElementById('delivered-quantity').value = request.quantity;
    updateQuantityHint();

    // Show modal
    document.getElementById('delivery-modal').classList.add('active');
    document.body.style.overflow = 'hidden';
}

// Close delivery modal
function closeDeliveryModal() {
    document.getElementById('delivery-modal').classList.remove('active');
    document.body.style.overflow = '';
    currentRequest = null;
}

// Update quantity hint
function updateQuantityHint() {
    if (!currentRequest) return;

    const deliveredQuantity = parseInt(document.getElementById('delivered-quantity').value) || 0;
    const isValid = deliveredQuantity > 0 && deliveredQuantity <= currentRequest.quantity;

    document.getElementById('quantity-hint').innerHTML =
        `Requested: ${currentRequest.quantity} | Available: ${isValid ? '✓' : '✗'}`;
}

// Handle delivery form submission
function handleDeliverySubmit(e) {
    e.preventDefault();

    if (!currentRequest) return;

    const formData = new FormData(e.target);
    const deliveryDate = document.getElementById('delivery-date').value;
    const deliveryTime = document.getElementById('delivery-time').value;
    const deliveredQuantity = parseInt(document.getElementById('delivered-quantity').value);
    const trackingNumber = document.getElementById('tracking-number').value;
    const supplierNotes = document.getElementById('supplier-notes').value;

    if (!deliveryDate || !deliveryTime || deliveredQuantity <= 0) {
        alert('Please fill in all required fields');
        return;
    }

    // Create delivery info
    const deliveryInfo = {
        delivery_date: deliveryDate,
        delivery_time: deliveryTime,
        delivered_quantity: deliveredQuantity,
        delivery_status: deliveredQuantity >= currentRequest.quantity ? "full" : "partial",
        supplier_notes: supplierNotes || null,
        tracking_number: trackingNumber || null
    };

    // Update request
    const requestIndex = supplierData.purchaseRequests.findIndex(req => req.id === currentRequest.id);
    if (requestIndex !== -1) {
        supplierData.purchaseRequests[requestIndex].delivery = deliveryInfo;
        supplierData.purchaseRequests[requestIndex].status =
            deliveryInfo.delivery_status === "full" ? "delivered" : "partially_delivered";
    }

    // Update UI
    updateStats();
    renderRequests();
    closeDeliveryModal();

    // Show success message
    alert(`Delivery scheduled successfully! Status: ${deliveryInfo.delivery_status}`);
}

// Switch to manager view (placeholder - would navigate to manager page)
function switchToManager() {
    alert('This would switch to the Manager View. In a real application, this would navigate to the manager interface.');
}

// Format date helper
function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString();
}

// Initialize when page loads
document.addEventListener('DOMContentLoaded', init);