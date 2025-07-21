// Audit page functionality
let allAuditLogs = [];

document.addEventListener('DOMContentLoaded', function() {
    loadAuditLogs();
});

async function loadAuditLogs() {
    try {
        allAuditLogs = await makeApiCall(`${API_BASE}/audit/all`);
        displayAuditLogs(allAuditLogs);
    } catch (error) {
        console.error('Failed to load audit logs:', error);
        showMessage('Failed to load audit logs', true);
    }
}

function displayAuditLogs(logs) {
    const container = document.getElementById('auditLogsContainer');
    
    if (!container) return;
    
    if (logs.length === 0) {
        container.innerHTML = '<p>No audit logs found.</p>';
        return;
    }
    
    let html = `
        <table class="audit-table">
            <thead>
                <tr>
                    <th>Timestamp</th>
                    <th>Order ID</th>
                    <th>Stage</th>
                    <th>Action</th>
                    <th>Status</th>
                    <th>Message</th>
                </tr>
            </thead>
            <tbody>
    `;
    
    logs.forEach(log => {
        html += `
            <tr class="${log.status.toLowerCase()}">
                <td>${formatTimestamp(log.timestamp)}</td>
                <td>${log.orderId || 'N/A'}</td>
                <td>${log.stage}</td>
                <td>${log.action}</td>
                <td><span class="status-badge status-${log.status.toLowerCase()}">${log.status}</span></td>
                <td>${truncateText(log.message, 80)}</td>
            </tr>
        `;
    });
    
    html += `
            </tbody>
        </table>
    `;
    
    container.innerHTML = html;
}

function applyFilters() {
    const orderIdFilter = document.getElementById('orderIdFilter').value.toLowerCase();
    const stageFilter = document.getElementById('stageFilter').value;
    const statusFilter = document.getElementById('statusFilter').value;
    
    let filteredLogs = allAuditLogs;
    
    if (orderIdFilter) {
        filteredLogs = filteredLogs.filter(log => 
            log.orderId && log.orderId.toLowerCase().includes(orderIdFilter)
        );
    }
    
    if (stageFilter) {
        filteredLogs = filteredLogs.filter(log => log.stage === stageFilter);
    }
    
    if (statusFilter) {
        filteredLogs = filteredLogs.filter(log => log.status === statusFilter);
    }
    
    displayAuditLogs(filteredLogs);
}

function clearFilters() {
    document.getElementById('orderIdFilter').value = '';
    document.getElementById('stageFilter').value = '';
    document.getElementById('statusFilter').value = '';
    displayAuditLogs(allAuditLogs);
}
