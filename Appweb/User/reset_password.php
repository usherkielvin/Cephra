<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Cephra - Reset Password</title>
    <link rel="icon" type="image/png" href="images/logo.png?v=2" />
    <link rel="apple-touch-icon" href="images/logo.png?v=2" />
    <link rel="manifest" href="manifest.webmanifest" />
    <meta name="theme-color" content="#062635" />

    <!-- Open Graph meta tags for social media previews -->
    <meta property="og:title" content="Reset Password - Cephra" />
    <meta property="og:description" content="Create your new password for Cephra" />
    <meta property="og:image" content="https://cephra.ct.ws/images/thumbnail.png?v=1" />
    <meta property="og:image:width" content="1200" />
    <meta property="og:image:height" content="630" />
    <meta property="og:url" content="https://cephra.ct.ws" />
    <meta property="og:type" content="website" />
    <meta property="og:site_name" content="Cephra" />

    <!-- Twitter Card meta tags -->
    <meta name="twitter:card" content="summary_large_image" />
    <meta name="twitter:title" content="Reset Password - Cephra" />
    <meta name="twitter:description" content="Create your new password for Cephra" />
    <meta name="twitter:image" content="https://cephra.ct.ws/images/thumbnail.png?v=1" />

    <link rel="stylesheet" href="css/reset_password.css" />
    <link rel="stylesheet" href="assets/css/fontawesome-all.min.css" />
</head>
<body class="auth-page">
    <div class="auth-wrapper">
        <form class="form" id="resetPasswordForm">
            <div class="brand" style="margin-bottom:10px;">
                <img src="images/logo.png" alt="Cephra" class="brand-img" />
                <span class="brand-text">CEPHRA</span>
            </div>
            <p class="title">Create New Password</p>
            <p class="message" style="opacity:.8">Please enter your new password below.</p>

            <div class="flex">
                <label>
                    <div class="password-input-container">
                        <input class="input" type="password" id="newPassword" name="newPassword" placeholder="" required />
                        <span>New Password</span>
                        <button type="button" class="password-toggle-btn" id="newPasswordToggleBtn" onclick="togglePasswordVisibility('newPassword')">
                            <i class="fas fa-eye" id="newPasswordToggleIcon"></i>
                        </button>
                    </div>
                </label>
            </div>

            <div class="flex">
                <label>
                    <div class="password-input-container">
                        <input class="input" type="password" id="confirmPassword" name="confirmPassword" placeholder="" required />
                        <span>Confirm Password</span>
                        <button type="button" class="password-toggle-btn" id="confirmPasswordToggleBtn" onclick="togglePasswordVisibility('confirmPassword')">
                            <i class="fas fa-eye" id="confirmPasswordToggleIcon"></i>
                        </button>
                    </div>
                </label>
            </div>

            <div class="password-requirements" id="passwordRequirements">
                <p class="requirements-title">Password must contain:</p>
                <ul class="requirements-list">
                    <li id="req-length" class="requirement">At least 8 characters</li>
                    <li id="req-uppercase" class="requirement">One uppercase letter</li>
                    <li id="req-lowercase" class="requirement">One lowercase letter</li>
                    <li id="req-number" class="requirement">One number</li>
                </ul>
            </div>

            <div style="display:flex; justify-content:space-between; align-items:center; margin:6px 0 12px 0;">
                <a href="verify_code.php" class="ds-link">← Back</a>
            </div>

            <button class="submit" type="submit" id="resetBtn" disabled>Reset Password</button>
        </form>
    </div>

    <script>
    (function(){
        // Ensure custom UI dialog is available for feedback
        if (typeof window.showDialog === 'undefined') {
            window.showDialog = function(title, message) {
                var overlay = document.createElement('div');
                overlay.style.cssText = 'position:fixed;inset:0;background:rgba(0,0,0,0.6);display:flex;align-items:center;justify-content:center;z-index:10000;padding:16px;';
                var dialog = document.createElement('div');
                dialog.style.cssText = 'width:100%;max-width:360px;background:#fff;border-radius:12px;box-shadow:0 10px 30px rgba(0,0,0,0.25);overflow:hidden;';
                var header = document.createElement('div');
                header.style.cssText = 'background:#00c2ce;color:#fff;padding:12px 16px;font-weight:700';
                header.textContent = title || 'Notice';
                var body = document.createElement('div');
                body.style.cssText = 'padding:16px;color:#333;line-height:1.5;';
                body.textContent = message || '';
                var footer = document.createElement('div');
                footer.style.cssText = 'padding:12px 16px;display:flex;justify-content:flex-end;gap:8px;background:#f7f7f7;';
                var ok = document.createElement('button');
                ok.textContent = 'OK';
                ok.style.cssText = 'background:#00c2ce;color:#fff;border:0;padding:8px 14px;border-radius:8px;cursor:pointer;';
                ok.onclick = function(){ document.body.removeChild(overlay); };
                footer.appendChild(ok);
                dialog.appendChild(header);
                dialog.appendChild(body);
                dialog.appendChild(footer);
                overlay.appendChild(dialog);
                document.body.appendChild(overlay);
            };
        }

        // Password toggle functionality for both password fields
        window.togglePasswordVisibility = function(fieldId) {
            const passwordInput = document.getElementById(fieldId);
            const toggleBtn = document.getElementById(fieldId + 'ToggleBtn');
            const toggleIcon = document.getElementById(fieldId + 'ToggleIcon');

            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                toggleIcon.className = 'fas fa-eye-slash';
            } else {
                passwordInput.type = 'password';
                toggleIcon.className = 'fas fa-eye';
            }
        };
    })();
    </script>

    <script src="reset_password.js"></script>
</body>
</html>
