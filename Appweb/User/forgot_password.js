// Forgot Password Flow JavaScript
let generatedCode = '';

// Generate a random 6-digit code
function generateVerificationCode() {
    return Math.floor(100000 + Math.random() * 900000).toString();
}

// Show mockup email popup (for resend functionality only)
function showEmailPopup(email, code = null) {
    generatedCode = code || generateVerificationCode();

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

// Handle forgot password form submission
document.getElementById('forgotPasswordForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const email = document.getElementById('email').value.trim();

    if (!email) {
        showDialog('Error', 'Please enter your email address.');
        return;
    }

    // Basic email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        showDialog('Error', 'Please enter a valid email address.');
        return;
    }

    try {
        // First, check if email exists in database
        const checkFormData = new FormData();
        checkFormData.append('email', email);

        const checkResponse = await fetch('api/check_email.php', {
            method: 'POST',
            body: checkFormData
        });

        const checkResult = await checkResponse.json();

        if (!checkResult.success) {
            showDialog('Error', checkResult.error || 'Failed to verify email. Please try again.');
            return;
        }

        // If email doesn't exist in database, show error popup
        if (!checkResult.exists) {
            showDialog('Email Not Found',
                'The email address you entered is not registered with Cephra. Please check your email address or create a new account.');
            return;
        }

        // If email exists, proceed with password reset request
        const formData = new FormData();
        formData.append('action', 'request-reset');
        formData.append('email', email);

        const response = await fetch('api/forgot_password.php', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.success) {
            // Store email in session for next steps
            sessionStorage.setItem('resetEmail', email);

            // Show success message - email will be sent automatically
            showDialog('Success', 'Reset code has been sent to your email address. Please check your inbox and spam folder.');
        } else {
            showDialog('Error', result.error || 'Failed to send reset code. Please try again.');
        }
    } catch (error) {
        showDialog('Error', 'An error occurred. Please try again.');
    }
});