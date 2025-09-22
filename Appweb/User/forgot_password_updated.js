// forgot_password_updated.js
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('forgotPasswordForm');
    const emailInput = document.getElementById('email');

    form.addEventListener('submit', async function(e) {
        e.preventDefault();

        const email = emailInput.value.trim();
        if (!email) {
            showDialog('Error', 'Please enter your email address.');
            return;
        }

        try {
            const formData = new FormData();
            formData.append('action', 'request-reset');
            formData.append('email', email);

            const response = await fetch('api/forgot_password_updated.php', {
                method: 'POST',
                body: formData
            });

            const result = await response.json();

            if (result.success) {
                showDialog('Success', 'Reset code sent to your email. Please check your inbox.');
                // Store email in sessionStorage for later steps
                sessionStorage.setItem('resetEmail', email);
                // Redirect to verify code page after short delay
                setTimeout(() => {
                    window.location.href = 'verify_code.php';
                }, 2000);
            } else {
                showDialog('Error', result.error || 'Failed to send reset code. Please try again.');
            }
        } catch (error) {
            showDialog('Error', 'An error occurred. Please try again.');
        }
    });
});