function showDialog(title, message) {
    const overlay = document.createElement('div');
    overlay.style.cssText = 'position:fixed;inset:0;background:rgba(0,0,0,0.6);display:flex;align-items:center;justify-content:center;z-index:10000;padding:16px;';
    const dialog = document.createElement('div');
    dialog.style.cssText = 'width:100%;max-width:360px;background:#fff;border-radius:12px;box-shadow:0 10px 30px rgba(0,0,0,0.25);overflow:hidden;';
    const header = document.createElement('div');
    header.style.cssText = 'background:#00c2ce;color:#fff;padding:12px 16px;font-weight:700';
    header.textContent = title || 'Notice';
    const body = document.createElement('div');
    body.style.cssText = 'padding:16px;color:#333;line-height:1.5;';
    body.textContent = message || '';
    const footer = document.createElement('div');
    footer.style.cssText = 'padding:12px 16px;display:flex;justify-content:flex-end;gap:8px;background:#f7f7f7;';
    const ok = document.createElement('button');
    ok.textContent = 'OK';
    ok.style.cssText = 'background:#00c2ce;color:#fff;border:0;padding:8px 14px;border-radius:8px;cursor:pointer;';
    ok.onclick = () => document.body.removeChild(overlay);
    footer.appendChild(ok);
    dialog.appendChild(header);
    dialog.appendChild(body);
    dialog.appendChild(footer);
    overlay.appendChild(dialog);
    document.body.appendChild(overlay);
}

document.getElementById('registerForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const firstname = document.getElementById('firstname').value.trim();
    const lastname = document.getElementById('lastname').value.trim();
    const username = document.getElementById('username').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    const confirmPassword = document.getElementById('confirmPassword').value.trim();

    if (!firstname || !lastname || !username || !email || !password || !confirmPassword) {
        showDialog('Registration', 'Please fill in all fields.');
        return;
    }

    if (password !== confirmPassword) {
        showDialog('Registration', 'Passwords do not match.');
        return;
    }

    try {
        const formData = new FormData();
        formData.append('action', 'register');
        formData.append('firstname', firstname);
        formData.append('lastname', lastname);
        formData.append('username', username);
        formData.append('email', email);
        formData.append('password', password);

        const response = await fetch('api/mobile.php', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.success) {
            showDialog('Registration', 'Registration successful! You can now login.');
            // Optionally redirect to login page
            setTimeout(() => { window.location.href = 'index.php'; }, 600);
        } else {
            showDialog('Registration failed', (result.error || 'Unknown error'));
        }
    } catch (error) {
        showDialog('Registration error', error.message);
    }
});