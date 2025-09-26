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
            <div class="header-actions" style="display:flex;align-items:center;gap:12px;margin-left:auto;">
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
            </div>
        </div>
    </div>
</header>
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
</script>


