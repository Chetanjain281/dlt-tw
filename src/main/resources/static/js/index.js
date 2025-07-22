// Index page functionality
let clients = [];
let funds = [];

document.addEventListener('DOMContentLoaded', function() {
    loadClients();
    loadFunds();
    loadRecentOrders();
    updateSystemStatus();
    
    // Set up form submission
    const orderForm = document.getElementById('orderForm');
    if (orderForm) {
        orderForm.addEventListener('submit', handleOrderSubmission);
    }
});

async function loadClients() {
    try {
        clients = await makeApiCall(`${API_BASE}/orders/clients`);
        const clientSelect = document.getElementById('clientSelect');
        
        if (clientSelect) {
            clientSelect.innerHTML = '<option value="">Choose a client...</option>';
            clients.forEach(client => {
                const option = document.createElement('option');
                option.value = client.id;
                option.textContent = `${client.name} (${client.riskProfile})`;
                clientSelect.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Failed to load clients:', error);
        showMessage('Failed to load clients', true);
    }
}

async function loadFunds() {
    try {
        funds = await makeApiCall(`${API_BASE}/orders/funds`);
        const fundSelect = document.getElementById('fundSelect');
        
        if (fundSelect) {
            fundSelect.innerHTML = '<option value="">Choose a fund...</option>';
            funds.forEach(fund => {
                const option = document.createElement('option');
                option.value = fund.id;
                option.textContent = `${fund.name} (${fund.riskLevel} Risk)`;
                fundSelect.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Failed to load funds:', error);
        showMessage('Failed to load funds', true);
    }
}

async function loadRecentOrders() {
    try {
        const orders = await makeApiCall(`${API_BASE}/orders/all`);
        const recentOrdersDiv = document.getElementById('recentOrders');
        
        if (recentOrdersDiv) {
            if (orders.length === 0) {
                recentOrdersDiv.innerHTML = '<p>No orders found. Create your first order above.</p>';
                return;
            }
            
            // Show last 5 orders
            const recentOrders = orders.slice(-5).reverse();
            let html = '<div class="recent-orders-list">';
            
            recentOrders.forEach(order => {
                const client = clients.find(c => c.id === order.clientId);
                const fund = funds.find(f => f.id === order.fundId);
                
                html += `
                    <div class="order-item">
                        <div class="order-header">
                            <span class="order-id">${order.orderId}</span>
                            ${getStatusBadge(order.overallStatus)}
                        </div>
                        <div class="order-details">
                            <span>${client ? client.name : order.clientId}</span> → 
                            <span>${fund ? fund.name : order.fundId}</span>
                            <span class="amount">${formatCurrency(order.requestedAmount)}</span>
                        </div>
                        <div class="order-stage">Current: ${order.currentStage}</div>
                    </div>
                `;
            });
            
            html += '</div>';
            recentOrdersDiv.innerHTML = html;
        }
    } catch (error) {
        console.error('Failed to load recent orders:', error);
        const recentOrdersDiv = document.getElementById('recentOrders');
        if (recentOrdersDiv) {
            recentOrdersDiv.innerHTML = '<p>Failed to load orders.</p>';
        }
    }
}

async function updateSystemStatus() {
    try {
        const orders = await makeApiCall(`${API_BASE}/orders/all`);
        
        const totalOrders = orders.length;
        const completedOrders = orders.filter(o => o.overallStatus === 'COMPLETED').length;
        const inProgressOrders = orders.filter(o => o.overallStatus === 'IN_PROGRESS').length;
        const failedOrders = orders.filter(o => o.overallStatus === 'FAILED').length;
        
        document.getElementById('totalOrders').textContent = totalOrders;
        document.getElementById('completedOrders').textContent = completedOrders;
        document.getElementById('inProgressOrders').textContent = inProgressOrders;
        document.getElementById('failedOrders').textContent = failedOrders;
    } catch (error) {
        console.error('Failed to update system status:', error);
    }
}

async function handleOrderSubmission(event) {
    event.preventDefault();
    
    const clientId = document.getElementById('clientSelect').value;
    const fundId = document.getElementById('fundSelect').value;
    const requestedAmount = parseFloat(document.getElementById('requestedAmount').value);
    
    if (!clientId || !fundId || !requestedAmount) {
        showMessage('Please fill in all fields', true);
        return;
    }
    
    // Validate minimum investment
    const selectedFund = funds.find(f => f.id === fundId);
    if (selectedFund && requestedAmount < selectedFund.minimumInvestment) {
        showMessage(`Minimum investment for this fund is ${formatCurrency(selectedFund.minimumInvestment)}`, true);
        return;
    }
    
    try {
        const orderData = {
            clientId: clientId,
            fundId: fundId,
            requestedAmount: requestedAmount
        };
        
        const newOrder = await makeApiCall(`${API_BASE}/orders/create`, 'POST', orderData);
        
        showMessage(`Order ${newOrder.orderId} solicitated successfully!`);
        
        // Reset form
        document.getElementById('orderForm').reset();
        
        // Refresh data
        await loadRecentOrders();
        await updateSystemStatus();
        
    } catch (error) {
        console.error('Failed to solicitate order:', error);
        showMessage('Failed to solicitate order. Please try again.', true);
    }
}
