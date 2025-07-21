// Validation page functionality
let allOrders = [];

document.addEventListener('DOMContentLoaded', function() {
    loadOrdersForValidation();
});

async function loadOrdersForValidation() {
    try {
        allOrders = await makeApiCall(`${API_BASE}/orders/all`);
        populateOrderSelect();
    } catch (error) {
        console.error('Failed to load orders:', error);
        showMessage('Failed to load orders', true);
    }
}

function populateOrderSelect() {
    const orderSelect = document.getElementById('orderSelect');
    
    if (!orderSelect) return;
    
    orderSelect.innerHTML = '<option value="">Select an order to validate...</option>';
    
    allOrders.forEach(order => {
        const option = document.createElement('option');
        option.value = order.orderId;
        option.textContent = `${order.orderId} - ${order.overallStatus}`;
        orderSelect.appendChild(option);
    });
}

async function validateOrder() {
    const orderSelect = document.getElementById('orderSelect');
    const orderId = orderSelect.value;
    
    if (!orderId) {
        showMessage('Please select an order to validate', true);
        return;
    }
    
    try {
        const validation = await makeApiCall(`${API_BASE}/orders/${orderId}/validate`);
        const order = allOrders.find(o => o.orderId === orderId);
        
        displayValidationResult(validation, order);
        displayHashChain(order);
        
    } catch (error) {
        console.error('Failed to validate order:', error);
        showMessage('Failed to validate order', true);
    }
}

function displayValidationResult(validation, order) {
    const container = document.getElementById('validationResult');
    
    if (!container) return;
    
    const statusClass = validation.isValid ? 'validation-success' : 'validation-error';
    const statusIcon = validation.isValid ? '✓' : '✗';
    
    let html = `
        <div class="validation-result ${statusClass}">
            <h3>${statusIcon} Validation Result</h3>
            <p><strong>Order ID:</strong> ${validation.orderId}</p>
            <p><strong>Status:</strong> ${validation.message}</p>
            <p><strong>Chain Integrity:</strong> ${validation.isValid ? 'Valid' : 'Broken'}</p>
            <p><strong>Total Stages:</strong> ${order.orderHistory.length}</p>
        </div>
    `;
    
    container.innerHTML = html;
}

function displayHashChain(order) {
    const container = document.getElementById('hashChainContainer');
    
    if (!container) return;
    
    let html = `
        <div class="hash-chain">
            <h3>Hash Chain for ${order.orderId}</h3>
            <div class="chain-blocks">
    `;
    
    // Genesis block
    html += `
        <div class="hash-block genesis">
            <div class="block-header">Genesis Block</div>
            <div class="hash-value">0000000000000000000000000000000000000000000000000000000000000000</div>
        </div>
        <div class="chain-arrow">→</div>
    `;
    
    order.orderHistory.forEach((stage, index) => {
        const isLast = index === order.orderHistory.length - 1;
        
        html += `
            <div class="hash-block ${stage.status.toLowerCase()}">
                <div class="block-header">${stage.stage}</div>
                <div class="block-content">
                    <p><strong>Timestamp:</strong> ${formatTimestamp(stage.timestamp)}</p>
                    <p><strong>Status:</strong> ${stage.status}</p>
                    <div class="hash-section">
                        <p><strong>Hash:</strong></p>
                        <code class="hash-value">${stage.hash}</code>
                    </div>
                </div>
            </div>
        `;
        
        if (!isLast) {
            html += '<div class="chain-arrow">→</div>';
        }
    });
    
    html += `
            </div>
        </div>
    `;
    
    container.innerHTML = html;
}

async function validateAllOrders() {
    const container = document.getElementById('systemValidationResult');
    
    if (!container) return;
    
    container.innerHTML = '<p>Validating all orders...</p>';
    
    try {
        const validationPromises = allOrders.map(order => 
            makeApiCall(`${API_BASE}/orders/${order.orderId}/validate`)
        );
        
        const validations = await Promise.all(validationPromises);
        
        const totalOrders = validations.length;
        const validOrders = validations.filter(v => v.isValid).length;
        const invalidOrders = totalOrders - validOrders;
        
        let html = `
            <div class="system-validation">
                <h3>System-wide Validation Results</h3>
                <div class="validation-stats">
                    <div class="stat-item">
                        <span class="stat-value">${totalOrders}</span>
                        <span class="stat-label">Total Orders</span>
                    </div>
                    <div class="stat-item valid">
                        <span class="stat-value">${validOrders}</span>
                        <span class="stat-label">Valid Chains</span>
                    </div>
                    <div class="stat-item invalid">
                        <span class="stat-value">${invalidOrders}</span>
                        <span class="stat-label">Invalid Chains</span>
                    </div>
                </div>
                
                <div class="validation-details">
                    <table class="validation-table">
                        <thead>
                            <tr>
                                <th>Order ID</th>
                                <th>Status</th>
                                <th>Chain Integrity</th>
                            </tr>
                        </thead>
                        <tbody>
        `;
        
        validations.forEach(validation => {
            const statusClass = validation.isValid ? 'valid' : 'invalid';
            const statusIcon = validation.isValid ? '✓' : '✗';
            
            html += `
                <tr class="${statusClass}">
                    <td>${validation.orderId}</td>
                    <td>${validation.message}</td>
                    <td>${statusIcon} ${validation.isValid ? 'Valid' : 'Broken'}</td>
                </tr>
            `;
        });
        
        html += `
                        </tbody>
                    </table>
                </div>
            </div>
        `;
        
        container.innerHTML = html;
        
    } catch (error) {
        console.error('Failed to validate all orders:', error);
        container.innerHTML = '<p>Failed to validate orders. Please try again.</p>';
    }
}
