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

    <link rel="stylesheet" href="css/index.css" />
    <link rel="stylesheet" href="assets/css/fontawesome-all.min.css" />
    <style>
        /* Password input styling to match design */
        .password-input-container {
            position: relative;
            width: 100%;
        }

        .input {
            width: 100%;
            padding: 16px 50px 16px 16px;
            font-size: 16px;
            background-color: rgba(255, 255, 255, 0.1);
            color: #fff;
            border: 2px solid rgba(0, 194, 206, 0.3);
            border-radius: 12px;
            outline: none;
            transition: all 0.25s ease;
        }

        .input:focus {
            border-color: #00c2ce;
            box-shadow: 0 0 0 3px rgba(0, 194, 206, 0.2);
            background-color: rgba(0, 194, 206, 0.1);
        }

        .input:valid {
            border-color: #00c2ce;
            background-color: rgba(0, 194, 206, 0.1);
        }

        .input + span {
            position: absolute;
            left: 16px;
            top: 16px;
            color: rgba(255, 255, 255, 0.7);
            pointer-events: none;
            transition: all 0.25s ease;
        }

        .input:focus + span,
        .input:valid + span {
            top: -8px;
            left: 12px;
            font-size: 12px;
            color: #00c2ce;
            background-color: #062635;
            padding: 0 4px;
        }

        .password-toggle-btn {
            position: absolute;
            right: 16px;
            top: 50%;
            transform: translateY(-50%);
            background: none;
            border: none;
            color: var(--toggle-icon-color);
            cursor: pointer;
            font-size: 16px;
            padding: 4px;
            transition: color 0.25s ease;
        }

        .password-toggle-btn:hover {
            color: var(--toggle-icon-hover);
        }

        /* Dark theme adjustments */
        .dark-theme .input {
            background-color: rgba(255, 255, 255, 0.1);
            border-color: rgba(0, 194, 206, 0.3);
            color: #fff;
        }

        .dark-theme .input:focus {
            background-color: rgba(0, 194, 206, 0.1);
        }

        .dark-theme .input + span {
            color: rgba(255, 255, 255, 0.7);
        }

        /* Light theme adjustments */
        .light-theme .input {
            background-color: rgba(0, 0, 0, 0.05);
            border-color: rgba(0, 194, 206, 0.4);
            color: #333;
        }

        .light-theme .input:focus {
            background-color: rgba(0, 194, 206, 0.1);
            border-color: #00c2ce;
        }

        .light-theme .input + span {
            color: rgba(0, 0, 0, 0.6);
        }

        .light-theme .input:focus + span,
        .light-theme .input:valid + span {
            background-color: #fff;
            color: #00c2ce;
        }

        .light-theme .password-toggle-btn {
            color: var(--toggle-icon-color);
        }

        .light-theme .password-toggle-btn:hover {
            color: var(--toggle-icon-hover);
        }

        /* Password requirements styling */
        .password-requirements {
            margin: 16px 0;
            padding: 12px;
            background-color: rgba(255, 255, 255, 0.05);
            border-radius: 8px;
            border: 1px solid rgba(0, 194, 206, 0.2);
        }

        .requirements-title {
            margin: 0 0 8px 0;
            font-size: 14px;
            font-weight: 600;
            color: #00c2ce;
        }

        .requirements-list {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .requirement {
            font-size: 12px;
            color: rgba(255, 255, 255, 0.7);
            margin: 4px 0;
            display: flex;
            align-items: center;
        }

        .requirement:before {
            content: '✗';
            color: #ff6b6b;
            margin-right: 8px;
            font-weight: bold;
        }

        .requirement.valid:before {
            content: '✓';
            color: #51cf66;
        }

        /* Dark theme adjustments */
        .dark-theme .password-requirements {
            background-color: rgba(255, 255, 255, 0.05);
        }

        .dark-theme .requirement {
            color: rgba(255, 255, 255, 0.7);
        }

        /* Light theme adjustments */
        .light-theme .password-requirements {
            background-color: rgba(0, 0, 0, 0.05);
        }

        .light-theme .requirement {
            color: rgba(0, 0, 0, 0.7);
        }

        /* Responsive design */
        @media (max-width: 480px) {
            .input {
                padding: 14px 45px 14px 14px;
                font-size: 14px;
            }
            .input + span {
                left: 14px;
                top: 14px;
                font-size: 14px;
            }
            .input:focus + span,
            .input:valid + span {
                top: -6px;
                left: 10px;
                font-size: 11px;
            }
            .password-toggle-btn {
                right: 14px;
                font-size: 14px;
            }
        }
    </style>
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
                <p class="main-description">Create a new secure password to regain access to your charging dashboard and continue your EV journey.</p>
            </div>
        </div>

        <!-- Right Panel - Reset Password Form -->
        <div class="right-panel">
            <form class="form" id="resetPasswordForm">
                <div class="form-header">
                    <h2 class="form-title">Reset Password</h2>
                    <p class="form-subtitle">Enter your new password below</p>
                </div>

                <div class="form-group">
                    <label>
                        <div class="input-container">
                            <div class="password-input-container">
                                <input class="input" type="password" id="newPassword" name="newPassword" placeholder="" required />
                                <span>New Password</span>
                                <button type="button" class="password-toggle-btn" id="newPasswordToggleBtn" onclick="togglePasswordVisibility('newPassword')">
                                    <i class="fas fa-eye" id="newPasswordToggleIcon"></i>
                                </button>
                            </div>
                        </div>
                    </label>
                </div>

                <div class="form-group">
                    <label>
                        <div class="input-container">
                            <div class="password-input-container">
                                <input class="input" type="password" id="confirmPassword" name="confirmPassword" placeholder="" required />
                                <span>Confirm Password</span>
                                <button type="button" class="password-toggle-btn" id="confirmPasswordToggleBtn" onclick="togglePasswordVisibility('confirmPassword')">
                                    <i class="fas fa-eye" id="confirmPasswordToggleIcon"></i>
                                </button>
                            </div>
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

                <div class="form-actions">
                    <a href="verify_code.php" class="forgot-link">← Back to Verify</a>
                </div>

                <button class="submit" type="submit" id="resetBtn" disabled>Reset Password</button>
            </form>
        </div>
    </div>

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
