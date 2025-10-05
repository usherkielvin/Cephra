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

// Mobile Language Selector removed - language selection available in profile dropdown

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
    if (mobileDownloadBtn && mobileQrPopup && !mobileDownloadBtn.contains(e.target) && !mobileQrPopup.contains(e.target)) {
        mobileQrPopup.classList.remove('show');
    }
});
