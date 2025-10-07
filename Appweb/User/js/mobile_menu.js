// Mobile menu toggle
document.getElementById('mobileMenuToggle').addEventListener('click', function() {
    const mobileMenu = document.getElementById('mobileMenu');
    mobileMenu.classList.toggle('mobile-menu-open');
    this.classList.toggle('active');
});

// Close mobile menu when clicking on mobile nav links
document.querySelectorAll('.mobile-nav-link').forEach(link => {
    link.addEventListener('click', function() {
        const mobileMenu = document.getElementById('mobileMenu');
        const mobileMenuToggle = document.getElementById('mobileMenuToggle');
        mobileMenu.classList.remove('mobile-menu-open');
        mobileMenuToggle.classList.remove('active');
    });
});

// Mobile Language Selector Functionality
const mobileLanguageBtn = document.getElementById('mobileLanguageBtn');
const mobileLanguageDropdown = document.getElementById('mobileLanguageDropdown');
const mobileLanguageOptions = mobileLanguageDropdown.querySelectorAll('.language-option');

// Toggle mobile language dropdown
mobileLanguageBtn.addEventListener('click', function(e) {
    e.stopPropagation();
    mobileLanguageDropdown.classList.toggle('show');
    mobileLanguageBtn.classList.toggle('active');
});

// Handle mobile language selection
mobileLanguageOptions.forEach(option => {
    option.addEventListener('click', function() {
        const selectedLang = this.dataset.lang;
        updateLanguageDisplay(selectedLang);
        mobileLanguageDropdown.classList.remove('show');
        mobileLanguageBtn.classList.remove('active');

        // Also update desktop language display
        const desktopLanguageText = document.querySelector('#languageBtn .language-text');
        if (desktopLanguageText) {
            const langMap = { 'en': 'EN', 'fil': 'Fil', 'ceb': 'Bisaya', 'zh': '中文' };
            desktopLanguageText.textContent = langMap[selectedLang] || 'EN';
        }

        console.log('Language changed to:', selectedLang);
    });
});

// Mobile QR Code Popup Functionality
const mobileDownloadBtn = document.getElementById('mobileDownloadBtn');
const mobileQrPopup = document.getElementById('mobileQrPopup');

// Show mobile QR popup on click
mobileDownloadBtn.addEventListener('click', function(e) {
    e.stopPropagation();
    mobileQrPopup.classList.toggle('show');
});

// Hide mobile QR popup when clicking outside
document.addEventListener('click', function(e) {
    if (!mobileDownloadBtn.contains(e.target) && !mobileQrPopup.contains(e.target)) {
        mobileQrPopup.classList.remove('show');
    }
});
