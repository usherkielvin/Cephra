<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Cephra Login</title>
    <link rel="icon" type="image/png" href="images/logo.png?v=2" />
    <link rel="apple-touch-icon" href="images/logo.png?v=2" />
    <link rel="manifest" href="manifest.webmanifest" />
    <meta name="theme-color" content="#062635" />

    <!-- Open Graph meta tags for social media previews -->
    <meta property="og:title" content="Sign in to Cephra - Your Electric Vehicle Charging Platform" />
    <meta property="og:description" content="Sign in to Cephra - Your Electric Vehicle Charging Platform" />
    <meta property="og:image" content="https://cephra.ct.ws/images/thumbnail.png?v=1" />
    <meta property="og:image:width" content="1200" />
    <meta property="og:image:height" content="630" />
    <meta property="og:url" content="https://cephra.ct.ws" />
    <meta property="og:type" content="website" />
    <meta property="og:site_name" content="Cephra" />

    <!-- Twitter Card meta tags -->
    <meta name="twitter:card" content="summary_large_image" />
    <meta name="twitter:title" content="Sign in to Cephra - Your Electric Vehicle Charging Platform" />
    <meta name="twitter:description" content="Sign in to Cephra - Your Electric Vehicle Charging Platform" />
    <meta name="twitter:image" content="https://cephra.ct.ws/images/thumbnail.png?v=1" />

    <link rel="stylesheet" href="css/index.css" />
    <link rel="stylesheet" href="assets/css/fontawesome-all.min.css" />
</head>
<body class="auth-page">
    <div class="auth-wrapper">
        <form class="form" id="loginForm">
            <div class="brand" style="margin-bottom:10px;">
                <img src="images/logo.png" alt="Cephra" class="brand-img" />
                <span class="brand-text">CEPHRA</span>
            </div>
            <p class="title">Sign in</p>
            <p class="message" style="opacity:.8">Enter your username and password to log in.</p>
            <div class="flex">
                <label>
                    <input class="input" type="text" id="username" name="username" placeholder="" required />
                    <span>Username</span>
                </label>
            </div>
            <label>
                <div class="password-input-container">
                    <input class="input" type="password" id="password" name="password" placeholder="" required />
                    <span>Password</span>
                    <button type="button" class="password-toggle-btn" id="passwordToggleBtn" onclick="togglePasswordVisibility()">
                        <i class="fas fa-eye" id="passwordToggleIcon"></i>
                    </button>
                </div>
            </label>
            <div style="display:flex; justify-content:space-between; align-items:center; margin:6px 0 12px 0;">
                <a href="forgot_password.php" class="ds-link">Forgot password?</a>
                <a href="Register_Panel.php" class="ds-link">Register here</a>
            </div>
            <button class="submit" type="submit">Login</button>
        </form>
    </div>

    <script>
    (function(){
        // Ensure custom UI dialog is available for login feedback
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

        // Password toggle functionality
        window.togglePasswordVisibility = function() {
            const passwordInput = document.getElementById('password');
            const toggleBtn = document.getElementById('passwordToggleBtn');
            const toggleIcon = document.getElementById('passwordToggleIcon');

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

    <script src="script.js"></script>
</body>
</html>
