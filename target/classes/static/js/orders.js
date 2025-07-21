// Orders page functionality
let allOrders = [];
let clients = [];
let funds = [];

document.addEventListener('DOMContentLoaded', function() {
    loadOrdersData();
});

async function loadOrdersData() {
    try {
        // Load all data in parallel
        const [ordersData, clientsData, fundsData] = await Promise.all([
            makeApiCall(`${API_BASE}/orders/all`),
            makeApiCall(`${API_BASE}/orders/clients`),
            makeApiCall(`${API_BASE}/orders/funds`)
        ]);
        
        allOrders = ordersData;
        clients = clientsData;
        funds = fundsData;
        
        displayOrders();
    } catch (error) {
        console.error('Failed to load orders data:', error);
        showMessage('Failed to load orders data', true);
    }
}

function displayOrders() {
    const container = document.getElementById('ordersContainer');
    
    if (!container) return;
    
    if (allOrders.length === 0) {
        container.innerHTML = '<p>No orders found. <a href="/">Create your first order</a>.</p>';
        return;
    }
    
    let html = `
        <table class="orders-table">
            <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Client</th>
                    <th>Fund</th>
                    <th>Amount</th>
                    <th>Current Stage</th>
                    <th>Status</th>
                    <th>Created</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
    `;
    
    allOrders.forEach(order => {
        const client = clients.find(c => c.clientId === order.clientId);
        const fund = funds.find(f => f.fundId === order.fundId);
        
        html += `
            <tr>
                <td>${order.orderId}</td>
                <td>${client ? client.clientName : order.clientId}</td>
                <td>${fund ? fund.fundName : order.fundId}</td>
                <td>${formatCurrency(order.requestedAmount)}</td>
                <td>${order.currentStage}</td>
                <td>${getStatusBadge(order.overallStatus)}</td>
                <td>${formatTimestamp(order.createdAt)}</td>
                <td>
                    <button onclick="showOrderDetails('${order.orderId}')" class="btn-small">Details</button>
                    ${order.overallStatus === 'PENDING_APPROVAL' ? 
                        `<button onclick="approveOrder('${order.orderId}')" class="btn-small btn-approve">Approve</button>
                         <button onclick="rejectOrder('${order.orderId}')" class="btn-small btn-reject">Reject</button>` : ''}
                </td>
            </tr>
        `;
    });
    
    html += `
            </tbody>
        </table>
    `;
    
    container.innerHTML = html;
}

async function approveOrder(orderId) {
    try {
        await makeApiCall(`${API_BASE}/orders/${orderId}/approve`, 'POST');
        showMessage('Order approved successfully!');
        loadOrdersData(); // Refresh the orders list
    } catch (error) {
        console.error('Failed to approve order:', error);
        showMessage('Failed to approve order', true);
    }
}

async function rejectOrder(orderId) {
    try {
        await makeApiCall(`${API_BASE}/orders/${orderId}/reject`, 'POST');
        showMessage('Order rejected successfully!');
        loadOrdersData(); // Refresh the orders list
    } catch (error) {
        console.error('Failed to reject order:', error);
        showMessage('Failed to reject order', true);
    }
}

async function showOrderDetails(orderId) {
    try {
        const order = allOrders.find(o => o.orderId === orderId);
        if (!order) {
            showMessage('Order not found', true);
            return;
        }
        
        const client = clients.find(c => c.clientId === order.clientId);
        const fund = funds.find(f => f.fundId === order.fundId);
        
        let html = `
            <div class="order-details">
                <div class="order-info">
                    <h3>Order Information</h3>
                    <p><strong>Order ID:</strong> ${order.orderId}</p>
                    <p><strong>Client:</strong> ${client ? client.clientName : order.clientId}</p>
                    <p><strong>Fund:</strong> ${fund ? fund.fundName : order.fundId}</p>
                    <p><strong>Amount:</strong> ${formatCurrency(order.requestedAmount)}</p>
                    <p><strong>Status:</strong> ${getStatusBadge(order.overallStatus)}</p>
                    <p><strong>Current Stage:</strong> ${order.currentStage}</p>
                    <p><strong>Created:</strong> ${formatTimestamp(order.createdAt)}</p>
                    <p><strong>Updated:</strong> ${formatTimestamp(order.updatedAt)}</p>
                </div>
                
                <div class="order-history">
                    <h3>Processing History</h3>
                    <div class="stages-container">
        `;
        
        order.orderHistory.forEach((stage, index) => {
            html += `
                <div class="order-stage ${stage.status.toLowerCase()}">
                    <div class="stage-header">
                        <h4>${stage.stage}</h4>
                        <span class="${stage.status.toLowerCase()}">${stage.status}</span>
                    </div>
                    <p><strong>Timestamp:</strong> ${formatTimestamp(stage.timestamp)}</p>
                    <p><strong>Hash:</strong> <code>${stage.hash.substring(0, 16)}...</code></p>
                    ${stage.message ? `<p><strong>Message:</strong> ${stage.message}</p>` : ''}
                    <details>
                        <summary>Stage Data</summary>
                        <pre>${JSON.stringify(stage.data, null, 2)}</pre>
                    </details>
                </div>
            `;
        });
        
        html += `
                    </div>
                </div>
            </div>
        `;
        
        const modal = document.getElementById('orderDetailModal');
        const content = document.getElementById('orderDetailContent');
        
        if (modal && content) {
            content.innerHTML = html;
            modal.style.display = 'block';
        }
        
    } catch (error) {
        console.error('Failed to show order details:', error);
        showMessage('Failed to load order details', true);
    }
}
