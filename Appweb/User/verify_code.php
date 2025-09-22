<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Cephra - Verify Code</title>
    <link rel="icon" type="image/png" href="images/logo.png?v=2" />
    <link rel="apple-touch-icon" href="images/logo.png?v=2" />
    <link rel="manifest" href="manifest.webmanifest" />
    <meta name="theme-color" content="#062635" />

    <!-- Open Graph meta tags for social media previews -->
    <meta property="og:title" content="Verify Code - Cephra" />
    <meta property="og:description" content="Enter your verification code for Cephra" />
    <meta property="og:image" content="https://cephra.ct.ws/images/thumbnail.png?v=1" />
    <meta property="og:image:width" content="1200" />
    <meta property="og:image:height" content="630" />
    <meta property="og:url" content="https://cephra.ct.ws" />
    <meta property="og:type" content="website" />
    <meta property="og:site_name" content="Cephra" />

    <!-- Twitter Card meta tags -->
    <meta name="twitter:card" content="summary_large_image" />
    <meta name="twitter:title" content="Verify Code - Cephra" />
    <meta name="twitter:description" content="Enter your verification code for Cephra" />
    <meta name="twitter:image" content="https://cephra.ct.ws/images/thumbnail.png?v=1" />

    <link rel="stylesheet" href="css/index.css" />
    <link rel="stylesheet" href="assets/css/fontawesome-all.min.css" />
    <style>
        /* Code input styling to match original design */
        .code-input-container {
            display: flex;
            justify-content: center;
            margin: 20px 0;
        }

        .code-inputs {
            display: flex;
            gap: 12px;
            justify-content: center;
        }

        .code-input {
            width: 50px;
            height: 60px;
            text-align: center;
            font-size: 24px;
            font-weight: 600;
            background-color: rgba(255, 255, 255, 0.1);
            color: #fff;
            border: 2px solid rgba(0, 194, 206, 0.3);
            border-radius: 12px;
            outline: none;
            transition: all 0.25s ease;
        }

        .code-input:focus {
            border-color: #00c2ce;
            box-shadow: 0 0 0 3px rgba(0, 194, 206, 0.2);
            background-color: rgba(0, 194, 206, 0.1);
        }

        .code-input:valid {
            border-color: #00c2ce;
            background-color: rgba(0, 194, 206, 0.1);
        }

        /* Dark theme adjustments */
        .dark-theme .code-input {
            background-color: rgba(255, 255, 255, 0.1);
            border-color: rgba(0, 194, 206, 0.3);
        }

        .dark-theme .code-input:focus {
            background-color: rgba(0, 194, 206, 0.1);
        }

        /* Light theme adjustments */
        .light-theme .code-input {
            background-color: rgba(0, 0, 0, 0.05);
            border-color: rgba(0, 194, 206, 0.4);
            color: #333;
        }

        .light-theme .code-input:focus {
            background-color: rgba(0, 194, 206, 0.1);
            border-color: #00c2ce;
        }

        /* Resend link styling */
        .resend-link {
            background: none;
            border: none;
            padding: 0;
            color: #00c2ce;
            cursor: pointer;
            font-size: inherit;
            font-family: inherit;
            text-decoration: none;
            transition: color 0.25s ease;
        }

        .resend-link:hover {
            color: #00a8b3;
            text-decoration: underline;
        }

        .resend-link:disabled {
            color: #666;
            cursor: not-allowed;
            text-decoration: none;
        }

        /* Dark theme adjustments */
        .dark-theme .resend-link {
            color: #00c2ce;
        }

        .dark-theme .resend-link:hover {
            color: #00a8b3;
        }

        /* Light theme adjustments */
        .light-theme .resend-link {
            color: #00c2ce;
        }

        .light-theme .resend-link:hover {
            color: #00a8b3;
        }

        /* Responsive design */
        @media (max-width: 480px) {
            .code-inputs {
                gap: 8px;
            }
            .code-input {
                width: 45px;
                height: 55px;
                font-size: 20px;
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
                <p class="main-description">Enter your verification code to complete the password reset process and regain access to your charging dashboard.</p>
            </div>
        </div>

        <!-- Right Panel - Verification Form -->
        <div class="right-panel">
            <form class="form" id="verifyCodeForm">
                <div class="form-header">
                    <h2 class="form-title">Verify Code</h2>
                    <p class="form-subtitle">Enter the 6-digit code sent to your email</p>
                </div>

                <div class="form-group">
                    <label>
                        <div class="input-container">
                            <div class="code-input-container">
                                <div class="code-inputs">
                                    <input type="text" class="code-input" maxlength="1" id="code1" />
                                    <input type="text" class="code-input" maxlength="1" id="code2" />
                                    <input type="text" class="code-input" maxlength="1" id="code3" />
                                    <input type="text" class="code-input" maxlength="1" id="code4" />
                                    <input type="text" class="code-input" maxlength="1" id="code5" />
                                    <input type="text" class="code-input" maxlength="1" id="code6" />
                                </div>
                            </div>
                            <span>Verification Code</span>
                        </div>
                    </label>
                </div>

                <div class="form-actions">
                    <a href="forgot_password.php" class="forgot-link">← Back to Reset</a>
                    <button type="button" class="resend-link" id="resendCode">Resend Code</button>
                </div>

                <button class="submit" type="submit">Verify Code</button>
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
    })();
    </script>

    <script src="verify_code_updated.js"></script>
</body>
</html>
