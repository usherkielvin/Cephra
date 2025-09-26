<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Cephra - Forgot Password</title>
    <link rel="icon" type="image/png" href="images/logo.png?v=2" />
    <link rel="apple-touch-icon" href="images/logo.png?v=2" />
    <link rel="manifest" href="manifest.webmanifest" />
    <meta name="theme-color" content="#062635" />

    <!-- Open Graph meta tags for social media previews -->
    <meta property="og:title" content="Reset Password - Cephra" />
    <meta property="og:description" content="Reset your password for Cephra - Your Electric Vehicle Charging Platform" />
    <meta property="og:image" content="https://cephra.ct.ws/images/thumbnail.png?v=1" />
    <meta property="og:image:width" content="1200" />
    <meta property="og:image:height" content="630" />
    <meta property="og:url" content="https://cephra.ct.ws" />
    <meta property="og:type" content="website" />
    <meta property="og:site_name" content="Cephra" />

    <!-- Twitter Card meta tags -->
    <meta name="twitter:card" content="summary_large_image" />
    <meta name="twitter:title" content="Reset Password - Cephra" />
    <meta name="twitter:description" content="Reset your password for Cephra - Your Electric Vehicle Charging Platform" />
    <meta name="twitter:image" content="https://cephra.ct.ws/images/thumbnail.png?v=1" />

<link rel="stylesheet" href="css/forgot_password.css" />
    <link rel="stylesheet" href="assets/css/fontawesome-all.min.css" />
</head>
<body class="auth-page" id="authPage">
    <!-- Theme Toggle Button -->
    <button type="button" class="theme-toggle" id="themeToggleBtn" onclick="toggleTheme()">
        <i class="fas fa-sun" id="themeIcon"></i>
    </button>

    <div class="auth-wrapper">
        <!-- Left Panel - Branding -->
        <div class="left-panel">
            <div class="brand-section">
                <div class="brand-icon">
                    <img src="images/logo.png" alt="Cephra" class="brand-logo" />
                </div>
                <h1 class="main-title">CEPHRA</h1>
                <p class="main-subtitle">Your Electric Vehicle Charging Platform</p>
                <p class="main-description">Reset your password to regain access to your charging dashboard and manage your electric vehicle charging sessions.</p>
            </div>
        </div>

        <!-- Right Panel - Forgot Password Form -->
        <div class="right-panel">
            <form class="form" id="forgotPasswordForm">
                <div class="form-header">
                    <h2 class="form-title">Reset Password</h2>
                    <p class="form-subtitle">Enter your email to receive a verification code</p>
                </div>

                <div class="form-group">
                    <label>
                        <div class="input-container">
                            <input class="input" type="email" id="email" name="email" placeholder="" required />
                            <span>Email Address</span>
                        </div>
                    </label>
                </div>

                <div class="form-actions">
                    <a href="login.php" class="forgot-link">Back to Login</a>
                </div>

                <button class="submit" type="submit">Send Verification Code</button>

    <script>
    (function(){
        // Ensure custom UI dialog is available for feedback
        if (typeof window.showDialog === 'undefined') {
            window.showDialog = function(title, message, callback) {
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
                ok.onclick = function(){
                    document.body.removeChild(overlay);
                    if (callback && typeof callback === 'function') {
                        callback();
                    }
                };
                footer.appendChild(ok);
                dialog.appendChild(header);
                dialog.appendChild(body);
                dialog.appendChild(footer);
                overlay.appendChild(dialog);
                document.body.appendChild(overlay);
            };
        }

        // Input visibility toggle functionality for email input
        window.toggleInputVisibility = function(fieldId) {
            const input = document.getElementById(fieldId);
            const toggleBtn = document.getElementById(fieldId + 'ToggleBtn');
            const toggleIcon = document.getElementById(fieldId + 'ToggleIcon');

            if (input.type === 'email') {
                input.type = 'text';
                toggleIcon.className = 'fas fa-eye-slash';
            } else {
                input.type = 'email';
                toggleIcon.className = 'fas fa-eye';
            }
        };

        // Theme toggle functionality
        window.toggleTheme = function() {
            const body = document.getElementById('authPage');
            const themeIcon = document.getElementById('themeIcon');

            if (body.classList.contains('light-theme')) {
                body.classList.remove('light-theme');
                body.classList.add('dark-theme');
                themeIcon.className = 'fas fa-sun';
                localStorage.setItem('theme', 'dark');
            } else {
                body.classList.remove('dark-theme');
                body.classList.add('light-theme');
                themeIcon.className = 'fas fa-moon';
                localStorage.setItem('theme', 'light');
            }
        };

        // Initialize theme on page load
        const savedTheme = localStorage.getItem('theme') || 'dark';
        const body = document.getElementById('authPage');
        const themeIcon = document.getElementById('themeIcon');

        if (savedTheme === 'light') {
            body.classList.add('light-theme');
            themeIcon.className = 'fas fa-moon';
        } else {
            body.classList.add('dark-theme');
            themeIcon.className = 'fas fa-sun';
        }
    })();
    </script>

    <script src="forgot_password_with_email.js"></script>
</body>
</html>
