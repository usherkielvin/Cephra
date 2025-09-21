// verify_code_updated.js
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('verifyCodeForm');
    const inputs = Array.from(document.querySelectorAll('.code-input'));
    const resendBtn = document.getElementById('resendCode');

    // Auto-focus next input on input
    inputs.forEach((input, idx) => {
        input.addEventListener('input', () => {
            if (input.value.length === 1 && idx < inputs.length - 1) {
                inputs[idx + 1].focus();
            }
            checkAllInputsFilled();
        });

        input.addEventListener('keydown', (e) => {
            if (e.key === 'Backspace' && input.value === '' && idx > 0) {
                inputs[idx - 1].focus();
            }
        });

        // Add paste event listener to handle pasting full code
        input.addEventListener('paste', (e) => {
            e.preventDefault();
            const paste = (e.clipboardData || window.clipboardData).getData('text');
            const digits = paste.replace(/\D/g, '').slice(0, inputs.length);
            digits.split('').forEach((digit, i) => {
                inputs[i].value = digit;
            });
            if (digits.length > 0) {
                const nextIndex = digits.length >= inputs.length ? inputs.length - 1 : digits.length;
                inputs[nextIndex].focus();
            }
            checkAllInputsFilled();
        });
    });

    function getCode() {
        return inputs.map(input => input.value).join('');
    }

    function checkAllInputsFilled() {
        const allFilled = inputs.every(input => input.value.length === 1);
        form.querySelector('button[type="submit"]').disabled = !allFilled;
    }

    form.addEventListener('submit', async function(e) {
        e.preventDefault();

        const code = getCode();
        const email = sessionStorage.getItem('resetEmail');
        if (!email) {
            showDialog('Error', 'Session expired. Please start the process again.');
            window.location.href = 'forgot_password_updated.php';
            return;
        }

        try {
            const formData = new FormData();
            formData.append('action', 'verify-code');
            formData.append('email', email);
            formData.append('code', code);

            const response = await fetch('api/forgot_password_updated.php', {
                method: 'POST',
                body: formData
            });

            const result = await response.json();

            if (result.success) {
                sessionStorage.setItem('tempToken', result.temp_token);
                showDialog('Success', 'Code verified. Please reset your password.');
                setTimeout(() => {
                    window.location.href = 'reset_password.php';
                }, 2000);
            } else {
                showDialog('Error', result.error || 'Invalid or expired code.');
            }
        } catch (error) {
            showDialog('Error', 'An error occurred. Please try again.');
        }
    });

    // Resend code with cooldown
    let cooldown = 120;
    let cooldownInterval;

    resendBtn.addEventListener('click', async function() {
        if (resendBtn.disabled) return;

        const email = sessionStorage.getItem('resetEmail');
        if (!email) {
            showDialog('Error', 'Session expired. Please start the process again.');
            window.location.href = 'forgot_password_updated.php';
            return;
        }

        try {
            const formData = new FormData();
            formData.append('action', 'resend-code');
            formData.append('email', email);

            const response = await fetch('api/forgot_password_updated.php', {
                method: 'POST',
                body: formData
            });

            const result = await response.json();

            if (result.success) {
                showDialog('Success', 'Reset code resent to your email.');
                startCooldown();
            } else {
                // Fix: If error is about waiting time, show specific message
                if (result.error && result.error.toLowerCase().includes('please wait')) {
                    showDialog('Info', result.error);
                } else {
                    showDialog('Error', result.error || 'Failed to resend code.');
                }
            }
        } catch (error) {
            showDialog('Error', 'An error occurred. Please try again.');
        }
    });

    function startCooldown() {
        resendBtn.disabled = true;
        resendBtn.textContent = `Resend Code (${cooldown}s)`;
        cooldownInterval = setInterval(() => {
            cooldown--;
            resendBtn.textContent = `Resend Code (${cooldown}s)`;
            if (cooldown <= 0) {
                clearInterval(cooldownInterval);
                resendBtn.disabled = false;
                resendBtn.textContent = 'Resend Code';
                cooldown = 120;
            }
        }, 1000);
    }

    checkAllInputsFilled();
});