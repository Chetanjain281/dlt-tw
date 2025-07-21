// Common utility functions
const API_BASE = '/api';

// Utility function to make API calls
async function makeApiCall(url, method = 'GET', data = null) {
    try {
        const options = {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            }
        };
        
        if (data) {
            options.body = JSON.stringify(data);
        }
        
        const response = await fetch(url, options);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const text = await response.text();
        return text ? JSON.parse(text) : null;
    } catch (error) {
        console.error('API call failed:', error);
        throw error;
    }
}

// Show modal message
function showMessage(message, isError = false) {
    const modal = document.getElementById('messageModal');
    const messageElement = document.getElementById('modalMessage');
    
    if (modal && messageElement) {
        messageElement.textContent = message;
        messageElement.style.color = isError ? '#dc3545' : '#28a745';
        modal.style.display = 'block';
    } else {
        // Fallback to alert if modal not available
        alert(message);
    }
}

// Close modal
document.addEventListener('DOMContentLoaded', function() {
    const modals = document.querySelectorAll('.modal');
    const closeButtons = document.querySelectorAll('.close');
    
    closeButtons.forEach(button => {
        button.addEventListener('click', function() {
            const modal = this.closest('.modal');
            if (modal) {
                modal.style.display = 'none';
            }
        });
    });
    
    // Close modal when clicking outside
    window.addEventListener('click', function(event) {
        modals.forEach(modal => {
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        });
    });
});

// Format timestamp
function formatTimestamp(timestamp) {
    const date = new Date(timestamp);
    return date.toLocaleString();
}

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

// Get status badge HTML
function getStatusBadge(status) {
    let className = 'status-badge ';
    switch (status.toLowerCase()) {
        case 'completed':
            className += 'status-completed';
            break;
        case 'in_progress':
        case 'in-progress':
            className += 'status-progress';
            break;
        case 'pending_approval':
            className += 'status-pending';
            break;
        case 'failed':
            className += 'status-failed';
            break;
        default:
            className += 'status-progress';
    }
    return `<span class="${className}">${status}</span>`;
}

// Truncate text
function truncateText(text, maxLength = 50) {
    if (text.length <= maxLength) return text;
    return text.substring(0, maxLength) + '...';
}
