<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require_once __DIR__ . '/../config/database.php';
$db = new Database();
$conn = $db->getConnection();
$username = isset($_SESSION['username']) ? $_SESSION['username'] : '';

// Compute avatar source
$pfpSrc = 'images/logo.png';
if ($username && $conn) {
    try {
        $stmt2 = $conn->prepare("SELECT profile_picture FROM users WHERE username = :u LIMIT 1");
        $stmt2->bindParam(':u', $username);
        $stmt2->execute();
        $row2 = $stmt2->fetch(PDO::FETCH_ASSOC);
        if ($row2 && !empty($row2['profile_picture'])) {
            $pp = $row2['profile_picture'];
            if (strpos($pp, 'data:image') === 0) {
                $pfpSrc = $pp;
            } elseif (strpos($pp, 'iVBORw0KGgo') === 0) {
                $pfpSrc = 'data:image/png;base64,' . $pp;
            } elseif (preg_match('/\.(jpg|jpeg|png|gif)$/i', $pp)) {
                $path = 'uploads/profile_pictures/' . $pp;
                if (file_exists(__DIR__ . '/../' . $path)) {
                    $pfpSrc = $path;
                }
            }
        }
    } catch (Exception $e) { /* ignore */ }
}
?>
<header class="header">
    <div class="container">
        <div class="header-content" style="display:flex;align-items:center;justify-content:space-between;gap:16px;width:100%;">
            <!-- Logo -->
            <div class="logo" style="display:flex;align-items:center;gap:12px;margin-right:16px;">
                <img src="images/logo.png" alt="Cephra" class="logo-img" />
                <span class="logo-text">CEPHRA</span>
            </div>
            <!-- Navigation -->
            <nav class="nav" style="flex:1;">
                <ul class="nav-list" style="display:flex;gap:1.25rem;align-items:center;">
                    <li><a href="#" onclick="openMonitorWeb && openMonitorWeb(); return false;" class="nav-link">Monitor</a></li>
                    <li><a href="link.php" class="nav-link">Link</a></li>
                    <li><a href="history.php" class="nav-link">History</a></li>
                    <li><a href="rewards.php" class="nav-link">Rewards</a></li>
                </ul>
            </nav>
            <!-- Actions on right -->
            <div class="header-actions actions-desktop" style="display:flex;align-items:center;gap:12px;margin-left:auto;">
                <a href="wallet.php" title="Wallet" style="display:inline-flex;align-items:center;justify-content:center;width:auto;height:auto;border:none;background:transparent;color:inherit;cursor:pointer;padding:4px;">
                    <i class="fas fa-wallet" aria-hidden="true" style="font-size:18px;"></i>
                </a>
                <div class="notifications" style="position:relative;">
                    <button id="notifBtn" title="Notifications" style="display:inline-flex;align-items:center;justify-content:center;width:auto;height:auto;border:none;background:transparent;color:inherit;cursor:pointer;padding:4px;">
                        <i class="fas fa-bell" aria-hidden="true" style="font-size:18px;"></i>
                    </button>
                </div>
                <div class="language-selector" style="position:relative;">
                    <button class="language-btn" id="languageBtn" title="Language" style="display:inline-flex;align-items:center;justify-content:center;width:auto;height:auto;border:none;background:transparent;color:inherit;cursor:pointer;padding:4px;">
                        <i class="fas fa-globe" aria-hidden="true" style="font-size:18px;line-height:1;"></i>
                    </button>
                    <div class="language-dropdown" id="languageDropdown" style="position:absolute;top:28px;right:0;">
                        <div class="language-option" data-lang="en">English</div>
                        <div class="language-option" data-lang="fil">Filipino</div>
                        <div class="language-option" data-lang="ceb">Bisaya</div>
                        <div class="language-option" data-lang="zh">中文</div>
                    </div>
                </div>
                <a href="profile.php" title="Profile" style="display:inline-flex;width:38px;height:38px;border-radius:50%;overflow:hidden;border:2px solid rgba(0,0,0,0.08);">
                    <img src="<?php echo htmlspecialchars($pfpSrc); ?>" alt="Profile" style="width:100%;height:100%;object-fit:cover;display:block;" />
                </a>

                <!-- Burger for small screens -->
                <button id="mobileMenuToggle" aria-label="Menu" style="display:none;align-items:center;justify-content:center;width:40px;height:40px;border:none;background:transparent;color:inherit;cursor:pointer;margin-left:8px;">
                    <span style="display:block;width:20px;height:2px;background:#1a202c;margin:3px 0;"></span>
                    <span style="display:block;width:20px;height:2px;background:#1a202c;margin:3px 0;"></span>
                    <span style="display:block;width:20px;height:2px;background:#1a202c;margin:3px 0;"></span>
                </button>
            </div>
        </div>
    </div>

    <!-- Minimal inline responsive rule to show burger on small widths -->
    <style>
        @media (max-width: 1024px) {
            #mobileMenuToggle { display: inline-flex !important; }
            .actions-desktop { display: none !important; }
            .nav-list { display: none !important; }
        }
        .mobile-menu-overlay{position:fixed;inset:0;background:rgba(0,0,0,0.5);display:none;z-index:1100;}
        .mobile-menu-panel{position:fixed;top:0;right:0;width:260px;height:100vh;background:#fff;box-shadow:-6px 0 20px rgba(0,0,0,0.15);transform:translateX(100%);transition:transform .25s ease;z-index:1110;display:flex;flex-direction:column;padding:16px;}
        .mobile-menu-panel a{padding:10px 8px;color:#1a202c;text-decoration:none;border-radius:8px;}
        .mobile-menu-panel a:hover{background:#f3f4f6;}
        .mobile-menu-open .mobile-menu-overlay{display:block;}
        .mobile-menu-open .mobile-menu-panel{transform:translateX(0);}
    </style>
</header>

<!-- Mobile menu markup -->
<div id="mobileMenuOverlay" class="mobile-menu-overlay"></div>
<div id="mobileMenuPanel" class="mobile-menu-panel">
    <a href="dashboard.php">Dashboard</a>
    <a href="link.php">Link</a>
    <a href="history.php">History</a>
    <a href="rewards.php">Rewards</a>
    <a href="wallet.php">Wallet</a>
    <a href="profile.php">Profile</a>
    <a href="#" title="Notifications"><i class="fas fa-bell"></i> Notifications</a>
    <div style="margin-top:auto;display:flex;gap:8px;">
        <a href="wallet.php" title="Wallet"><i class="fas fa-wallet"></i>&nbsp;Wallet</a>
        <a href="profile.php" title="Profile"><i class="fas fa-user"></i>&nbsp;Profile</a>
    </div>
    <button id="mobileMenuClose" style="position:absolute;top:8px;right:8px;border:none;background:transparent;font-size:20px;cursor:pointer;">×</button>
    <div style="height:24px"></div>
    <div>
        <button id="mobileLangBtn" style="border:1px solid #e2e8f0;padding:8px 12px;border-radius:8px;background:#fff;cursor:pointer;width:100%;text-align:left;">
            <i class="fas fa-globe"></i> Language
        </button>
        <div id="mobileLangList" style="display:none;margin-top:6px;">
            <a href="#" data-lang="en">English</a>
            <a href="#" data-lang="fil">Filipino</a>
            <a href="#" data-lang="ceb">Bisaya</a>
            <a href="#" data-lang="zh">中文</a>
        </div>
    </div>
</div>
<script>
// Language dropdown behaviour (shared)
(function(){
    const languageBtn = document.getElementById('languageBtn');
    const languageDropdown = document.getElementById('languageDropdown');
    if (languageBtn && languageDropdown) {
        languageBtn.addEventListener('click', function(e){
            e.stopPropagation();
            languageDropdown.classList.toggle('show');
            languageBtn.classList.toggle('active');
        });
        languageDropdown.querySelectorAll('.language-option').forEach(opt => {
            opt.addEventListener('click', function(){
                languageDropdown.classList.remove('show');
                languageBtn.classList.remove('active');
                localStorage.setItem('selectedLanguage', this.dataset.lang || 'en');
            });
        });
        document.addEventListener('click', function(e){
            if (!languageBtn.contains(e.target) && !languageDropdown.contains(e.target)) {
                languageDropdown.classList.remove('show');
                languageBtn.classList.remove('active');
            }
        });
    }
})();

// Mobile menu behaviour
(function(){
    const toggle = document.getElementById('mobileMenuToggle');
    const overlay = document.getElementById('mobileMenuOverlay');
    const panel = document.getElementById('mobileMenuPanel');
    const closeBtn = document.getElementById('mobileMenuClose');
    const body = document.body;
    function open(){
        body.classList.add('mobile-menu-open');
        if (panel){ panel.style.transform = 'translateX(0)'; }
        if (overlay){ overlay.style.display = 'block'; }
        body.style.overflow = 'hidden';
    }
    function close(){
        body.classList.remove('mobile-menu-open');
        if (panel){ panel.style.transform = 'translateX(100%)'; }
        if (overlay){ overlay.style.display = 'none'; }
        body.style.overflow = '';
    }
    if (toggle){ toggle.addEventListener('click', open); }
    if (overlay){ overlay.addEventListener('click', close); }
    if (closeBtn){ closeBtn.addEventListener('click', close); }

    const mobileLangBtn = document.getElementById('mobileLangBtn');
    const mobileLangList = document.getElementById('mobileLangList');
    if (mobileLangBtn && mobileLangList){
        mobileLangBtn.addEventListener('click', function(){
            mobileLangList.style.display = mobileLangList.style.display === 'none' ? 'block' : 'none';
        });
        mobileLangList.querySelectorAll('a[data-lang]').forEach(a=>{
            a.addEventListener('click', function(e){
                e.preventDefault();
                localStorage.setItem('selectedLanguage', this.dataset.lang);
            });
        });
    }
})();
</script>


