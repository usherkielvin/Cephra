// Reset Password JavaScript
const newPasswordInput = document.getElementById('newPassword');
const confirmPasswordInput = document.getElementById('confirmPassword');
const resetForm = document.getElementById('resetPasswordForm');
const resetBtn = document.getElementById('resetBtn');

// Password requirements elements
const lengthReq = document.getElementById('req-length');
const uppercaseReq = document.getElementById('req-uppercase');
const lowercaseReq = document.getElementById('req-lowercase');
const numberReq = document.getElementById('req-number');

// Validate password requirements
function validatePassword(password) {
    const requirements = {
        length: password.length >= 8,
        uppercase: /[A-Z]/.test(password),
        lowercase: /[a-z]/.test(password),
        number: /\d/.test(password)
    };

    return requirements;
}

// Update requirement indicators
function updateRequirements(password) {
    const requirements = validatePassword(password);

    // Update length requirement
    if (requirements.length) {
        lengthReq.classList.add('valid');
    } else {
        lengthReq.classList.remove('valid');
    }

    // Update uppercase requirement
    if (requirements.uppercase) {
        uppercaseReq.classList.add('valid');
    } else {
        uppercaseReq.classList.remove('valid');
    }

    // Update lowercase requirement
    if (requirements.lowercase) {
        lowercaseReq.classList.add('valid');
    } else {
        lowercaseReq.classList.remove('valid');
    }

    // Update number requirement
    if (requirements.number) {
        numberReq.classList.add('valid');
    } else {
        numberReq.classList.remove('valid');
    }

    return requirements;
}

// Check if passwords match
function checkPasswordsMatch() {
    const password = newPasswordInput.value;
    const confirmPassword = confirmPasswordInput.value;

    if (confirmPassword && password !== confirmPassword) {
        confirmPasswordInput.setCustomValidity('Passwords do not match');
        return false;
    } else {
        confirmPasswordInput.setCustomValidity('');
        return true;
    }
}

// Check if all requirements are met
function checkAllRequirements() {
    const requirements = updateRequirements(newPasswordInput.value);
    const passwordsMatch = checkPasswordsMatch();

    const allValid = requirements.length &&
        requirements.uppercase &&
        requirements.lowercase &&
        requirements.number &&
        passwordsMatch &&
        newPasswordInput.value.length > 0;

    resetBtn.disabled = !allValid;
    return allValid;
}

// Add event listeners
newPasswordInput.addEventListener('input', function() {
    updateRequirements(this.value);
    checkPasswordsMatch();
    checkAllRequirements();
});

confirmPasswordInput.addEventListener('input', function() {
    checkPasswordsMatch();
    checkAllRequirements();
});

// Handle form submission
resetForm.addEventListener('submit', async function(e) {
    e.preventDefault();

    const newPassword = newPasswordInput.value;
    const confirmPassword = confirmPasswordInput.value;
    const email = sessionStorage.getItem('resetEmail');

    if (!email) {
        showDialog('Error', 'Session expired. Please start the process again.');
        window.location.href = 'forgot_password.php';
        return;
    }

    if (newPassword !== confirmPassword) {
        showDialog('Error', 'Passwords do not match. Please try again.');
        return;
    }

    const requirements = validatePassword(newPassword);
    if (!requirements.length || !requirements.uppercase || !requirements.lowercase || !requirements.number) {
        showDialog('Error', 'Password does not meet all requirements.');
        return;
    }

    try {
        const formData = new FormData();
        formData.append('action', 'reset-password');
        formData.append('email', email);
        formData.append('new_password', newPassword);

        const response = await fetch('api/forgot_password_updated.php', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.success) {
            // Show success message
            showDialog('Success!', 'Your password has been successfully reset. You can now log in with your new password.');

            // Clear session storage
            sessionStorage.removeItem('resetEmail');

            // Redirect to login page after a delay
            setTimeout(() => {
                window.location.href = 'index.php';
            }, 2000);
        } else {
            showDialog('Error', result.error || 'Failed to reset password. Please try again.');
        }
    } catch (error) {
        showDialog('Error', 'An error occurred. Please try again.');
    }
});

// Initialize form state
document.addEventListener('DOMContentLoaded', function() {
    checkAllRequirements();
});