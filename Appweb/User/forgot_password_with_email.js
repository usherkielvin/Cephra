// Forgot Password Flow JavaScript - WITH EMAIL FUNCTIONALITY
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

// Check if email exists in database
async function checkEmailExists(email) {
    try {
        const formData = new FormData();
        formData.append('email', email);

        const response = await fetch('api/check_email.php', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();
        return result;
    } catch (error) {
        console.error('Error checking email:', error);
        return { success: false, error: 'Failed to check email' };
    }
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

    // Check if email exists in database
    const emailCheck = await checkEmailExists(email);

    if (!emailCheck.success) {
        showDialog('Error', emailCheck.error || 'Failed to check email. Please try again.');
        return;
    }

    if (!emailCheck.exists) {
        showDialog('Email Not Found', 'The email address you entered is not registered in our system. Please check your email or create an account.');
        return;
    }

    try {
        const formData = new FormData();
        formData.append('action', 'request-reset');
        formData.append('email', email);

        // Updated API endpoint to call
        const response = await fetch('api/forgot_password_updated.php', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.success === true) {
            // Store email in session for next steps
            sessionStorage.setItem('resetEmail', email);

            // Show success message - email will be sent automatically
            showDialog('Success', 'Reset code has been sent to your email address. Please check your inbox and spam folder.', function() {
                // Redirect to verify_code.php only when user clicks OK
                window.location.href = 'verify_code.php';
            });
        } else {
            const errorMsg = result.error ? result.error : 'Failed to send reset code.';
            showDialog('Error', errorMsg);
            console.error('Forgot Password Error:', errorMsg);
        }
    } catch (error) {
        showDialog('Error', `Error: ${error.message || error}`);
        console.error('Forgot Password Exception:', error);
    }
});