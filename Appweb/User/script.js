function showDialog(title, message) {
    // Remove any existing dialogs first
    const existingDialogs = document.querySelectorAll('[data-dialog-overlay]');
    existingDialogs.forEach(dialog => {
        try {
            if (dialog.parentNode) {
                dialog.parentNode.removeChild(dialog);
            }
        } catch (e) {
            // Element already removed
        }
    });
    
    const overlay = document.createElement('div');
    overlay.setAttribute('data-dialog-overlay', 'true');
    overlay.style.cssText = `position:fixed; inset:0; background:rgba(0,0,0,0.6); display:flex; align-items:center; justify-content:center; z-index:10000; padding:16px;`;

    const dialog = document.createElement('div');
    dialog.style.cssText = `width:100%; max-width:360px; background:#fff; border-radius:12px; box-shadow:0 10px 30px rgba(0,0,0,0.25); overflow:hidden;`;

    const header = document.createElement('div');
    header.style.cssText = `background:#00c2ce; color:#fff; padding:12px 16px; font-weight:700;`;
    header.textContent = title || 'Notice';

    const body = document.createElement('div');
    body.style.cssText = `padding:16px; color:#333; line-height:1.5;`;
    body.textContent = message || '';

    const footer = document.createElement('div');
    footer.style.cssText = `padding:12px 16px; display:flex; justify-content:flex-end; gap:8px; background:#f7f7f7;`;

    const ok = document.createElement('button');
    ok.textContent = 'OK';
    ok.style.cssText = `background:#00c2ce; color:#fff; border:0; padding:8px 14px; border-radius:8px; cursor:pointer;`;
    ok.onclick = () => {
        try {
            if (overlay && overlay.parentNode) {
                overlay.style.display = 'none';
                document.body.removeChild(overlay);
            }
        } catch (e) {
            // Element already removed
        }
    };

    footer.appendChild(ok);
    dialog.appendChild(header);
    dialog.appendChild(body);
    dialog.appendChild(footer);
    overlay.appendChild(dialog);
    document.body.appendChild(overlay);
}

document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();

    if (!username || !password) {
        showDialog('Login', 'Please enter both username and password.');
        return;
    }

    try {
        const formData = new FormData();
        formData.append('action', 'login');
        formData.append('username', username);
        formData.append('password', password);

        const response = await fetch('api/mobile.php', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.success) {
            var uname = result.username || 'User';
            showDialog('Welcome ' + uname, 'You are now signed in.');
            // Redirect to dashboard.php after a short delay
            setTimeout(() => { window.location.href = 'dashboard.php'; }, 700);
        } else {
            showDialog('Login failed', (result.error || 'Unknown error'));
        }
    } catch (error) {
        showDialog('Login error', error.message);
    }
});