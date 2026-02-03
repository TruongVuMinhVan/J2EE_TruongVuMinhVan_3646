// Cart functionality
document.addEventListener('DOMContentLoaded', function() {
    // Handle quantity updates
    const quantityInputs = document.querySelectorAll('.quantity');
    
    quantityInputs.forEach(input => {
        input.addEventListener('change', function() {
            const bookId = this.getAttribute('data-id');
            const quantity = this.value;
            
            if (quantity > 0) {
                // Update cart via AJAX or redirect
                window.location.href = `/cart/updateCart/${bookId}/${quantity}`;
            }
        });
    });
    
    // Confirm before removing items
    const removeButtons = document.querySelectorAll('.btn-danger');
    removeButtons.forEach(button => {
        if (button.textContent.trim() === 'Remove') {
            button.addEventListener('click', function(e) {
                if (!confirm('Are you sure you want to remove this item from cart?')) {
                    e.preventDefault();
                }
            });
        }
    });
    
    // Confirm before clearing cart
    const clearCartButton = document.querySelector('a[href*="/cart/clearCart"]');
    if (clearCartButton) {
        clearCartButton.addEventListener('click', function(e) {
            if (!confirm('Are you sure you want to clear the entire cart?')) {
                e.preventDefault();
            }
        });
    }
});