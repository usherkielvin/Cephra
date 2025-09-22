// Verify Code JavaScript
const codeInputs = document.querySelectorAll('.code-input');
const verifyForm = document.getElementById('verifyCodeForm');
const resendBtn = document.getElementById('resendCode');

// Generate a new verification code
function generateVerificationCode() {
    return Math.floor(100000 + Math.random() * 900000).toString();
}

// Show mockup email popup (for resend functionality only)
function showEmailPopup(email, code = null) {
    const generatedCode = code || generateVerificationCode();
    sessionStorage.setItem('verificationCode', generatedCode);

    const overlay = document.createElement('div');
    overlay.style.cssText = 'position:fixed;inset:0;background:rgba(0,0,0,0.6);display:flex;align-items:center;justify-content:center;z-index:10000;padding:16px;';

    const dialog = document.createElement('div');
    dialog.style.cssText = 'width:100%;max-width:480px;background:#fff;border-radius:12px;box-shadow:0 10px 30px rgba(0,0,0,0.25);overflow:hidden;';

    const header = document.createElement('div');
    header.style.cssText = 'background:#00c2ce;color:#fff;padding:16px 20px;font-weight:700;font-size:18px;';
    header.textContent = '📧 New Verification Code Sent!';

    const body = document.createElement('div');
    body.style.cssText = 'padding:20px;color:#333;line-height:1.6;';

    const emailText = document.createElement('p');
    emailText.style.cssText = 'margin:0 0 16px 0;font-size:16px;';
    emailText.textContent = `We've sent a new verification code to:`;

    const emailAddress = document.createElement('p');
    emailAddress.style.cssText = 'margin:0 0 20px 0;font-size:18px;font-weight:600;color:#00c2ce;';
    emailAddress.textContent = email;

    const codeText = document.createElement('p');
    codeText.style.cssText = 'margin:0 0 16px 0;font-size:16px;';
    codeText.textContent = `Your new verification code is:`;

    const codeDisplay = document.createElement('div');
    codeDisplay.style.cssText = 'background:#f8f9fa;padding:16px;border-radius:8px;margin:0 0 20px 0;text-align:center;border:2px dashed #00c2ce;';

    const codeNumber = document.createElement('span');
    codeNumber.style.cssText = 'font-size:32px;font-weight:700;color:#00c2ce;letter-spacing:4px;font-family:monospace;';
    codeNumber.textContent = generatedCode;

    const noteText = document.createElement('p');
    noteText.style.cssText = 'margin:0;font-size:14px;color:#666;font-style:italic;';
    noteText.textContent = 'Copy this code and paste it in the form below.';

    const footer = document.createElement('div');
    footer.style.cssText = 'padding:16px 20px;display:flex;justify-content:flex-end;gap:12px;background:#f7f7f7;';

    const copyBtn = document.createElement('button');
    copyBtn.textContent = 'Copy Code';
    copyBtn.style.cssText = 'background:#6c757d;color:#fff;border:0;padding:10px 16px;border-radius:8px;cursor:pointer;font-size:14px;';
    copyBtn.onclick = function() {
        navigator.clipboard.writeText(generatedCode).then(() => {
            copyBtn.textContent = 'Copied!';
            copyBtn.style.background = '#28a745';
            setTimeout(() => {
                copyBtn.textContent = 'Copy Code';
                copyBtn.style.background = '#6c757d';
            }, 2000);
        });
    };

    const okBtn = document.createElement('button');
    okBtn.textContent = 'OK';
    okBtn.style.cssText = 'background:#00c2ce;color:#fff;border:0;padding:10px 16px;border-radius:8px;cursor:pointer;font-size:14px;';
    okBtn.onclick = function() {
        document.body.removeChild(overlay);
    };

    footer.appendChild(copyBtn);
    footer.appendChild(okBtn);

    codeDisplay.appendChild(codeNumber);
    body.appendChild(emailText);
    body.appendChild(emailAddress);
    body.appendChild(codeText);
    body.appendChild(codeDisplay);
    body.appendChild(noteText);
    dialog.appendChild(header);
    dialog.appendChild(body);
    dialog.appendChild(footer);
    overlay.appendChild(dialog);
    document.body.appendChild(overlay);
}

// Handle input navigation between code boxes
codeInputs.forEach((input, index) => {
    input.addEventListener('input', (e) => {
        const value = e.target.value;

        // Only allow numbers
        if (value && !/^\d$/.test(value)) {
            e.target.value = '';
            return;
        }

        // Move to next input if value is entered
        if (value && index < codeInputs.length - 1) {
            codeInputs[index + 1].focus();
        }
    });

    input.addEventListener('keydown', (e) => {
        // Handle backspace - move to previous input
        if (e.key === 'Backspace' && !e.target.value && index > 0) {
            codeInputs[index - 1].focus();
        }

        // Handle left arrow - move to previous input
        if (e.key === 'ArrowLeft' && index > 0) {
            codeInputs[index - 1].focus();
        }

        // Handle right arrow - move to next input
        if (e.key === 'ArrowRight' && index < codeInputs.length - 1) {
            codeInputs[index + 1].focus();
        }
    });

    input.addEventListener('paste', (e) => {
        e.preventDefault();
        const pasteData = e.clipboardData.getData('text').replace(/\D/g, '').slice(0, 6);

        // Fill the inputs with pasted data
        pasteData.split('').forEach((char, i) => {
            if (index + i < codeInputs.length) {
                codeInputs[index + i].value = char;
            }
        });

        // Focus the next empty input or the last input
        const nextEmptyIndex = Math.min(index + pasteData.length, codeInputs.length - 1);
        codeInputs[nextEmptyIndex].focus();
    });
});

// Get the complete code from all inputs
function getVerificationCode() {
    return Array.from(codeInputs).map(input => input.value).join('');
}

// Handle form submission
verifyForm.addEventListener('submit', async function(e) {
    e.preventDefault();

    const enteredCode = getVerificationCode();
    const email = sessionStorage.getItem('resetEmail');

    if (!enteredCode || enteredCode.length !== 6) {
        showDialog('Error', 'Please enter the complete 6-digit verification code.');
        return;
    }

    if (!email) {
        showDialog('Error', 'Session expired. Please start the process again.');
        window.location.href = 'forgot_password.php';
        return;
    }

    try {
        const formData = new FormData();
        formData.append('action', 'verify-code');
        formData.append('email', email);
        formData.append('code', enteredCode);

        const response = await fetch('api/forgot_password.php', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.success) {
            // Store temp token for password reset
            sessionStorage.setItem('tempToken', result.temp_token);
            // Code is correct, proceed to reset password
            window.location.href = 'reset_password.php';
        } else {
            showDialog('Error', result.error || 'The verification code you entered is incorrect. Please try again.');
            // Clear all inputs
            codeInputs.forEach(input => input.value = '');
            codeInputs[0].focus();
        }
    } catch (error) {
        showDialog('Error', 'An error occurred. Please try again.');
    }
});

// Handle resend code
resendBtn.addEventListener('click', async function() {
    const email = sessionStorage.getItem('resetEmail');
    if (!email) {
        showDialog('Error', 'Email not found. Please start the process again.');
        window.location.href = 'forgot_password.php';
        return;
    }

    try {
        const formData = new FormData();
        formData.append('action', 'resend-code');
        formData.append('email', email);

        const response = await fetch('api/forgot_password.php', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.success) {
            showDialog('Success', 'New verification code has been sent to your email address. Please check your inbox and spam folder.');
        } else {
            showDialog('Error', result.error || 'Failed to resend code. Please try again.');
        }
    } catch (error) {
        showDialog('Error', 'An error occurred. Please try again.');
    }
});

// Auto-focus first input on page load
document.addEventListener('DOMContentLoaded', function() {
    codeInputs[0].focus();
});