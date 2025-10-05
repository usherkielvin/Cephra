<!-- Mobile Menu -->
<div class="mobile-menu" id="mobileMenu">
    <div class="mobile-menu-content">
        <!-- Mobile Navigation -->
        <div class="mobile-nav">
            <ul class="mobile-nav-list">
                <li class="mobile-nav-item">
                    <a href="#charging" class="mobile-nav-link">Charging</a>
                </li>
                <li class="mobile-nav-item">
                    <a href="#promotions" class="mobile-nav-link">Offers</a>
                </li>
                <li class="mobile-nav-item">
                    <a href="#features" class="mobile-nav-link">About</a>
                </li>
            </ul>
        </div>

        <!-- Mobile Header Actions -->
        <div class="mobile-header-actions" style="display:flex;gap:16px;align-items:center;justify-content:center;flex-wrap:wrap;">
            <!-- Actions row: Download + Auth in one row on small screens -->
            <div class="mobile-actions-row" style="display:flex;gap:16px;align-items:center;justify-content:center;margin-top:12px;">
                <!-- Mobile Download App -->
                <div class="mobile-download-app" style="display:flex;align-items:center;">
                    <div class="download-app">
                        <button class="download-btn" id="mobileDownloadBtn">
                            <i class="fas fa-download"></i>
                        </button>
                        <div class="qr-popup" id="mobileQrPopup">
                            <div class="qr-content">
                                <h4>Download Cephra App</h4>
                                <div class="qr-code">
                                    <img src="images/qr.png" alt="QR Code - Download Cephra App" width="120" height="120" style="display: block; border-radius: 8px;" />
                                </div>
                                <p>Scan to download the Cephra mobile app</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Mobile Auth Buttons -->
                <div class="mobile-auth-buttons" style="display:flex;gap:12px;align-items:center;">
                    <a href="Register_Panel.php" class="nav-link auth-link">Register</a>
                    <a href="login.php" class="nav-link auth-link">Login</a>
                </div>
            </div>
        </div>
    </div>
</div>
