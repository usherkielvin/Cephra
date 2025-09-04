document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();

    if (!username || !password) {
        alert('Please enter both username and password.');
        return;
    }

    try {
        const formData = new FormData();
        formData.append('action', 'login');
        formData.append('username', username);
        formData.append('password', password);

        const response = await fetch('../api/mobile.php', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.success) {
            alert('Login successful! Welcome, ' + result.username);
            // Redirect to dashboard.php after successful login
            window.location.href = 'dashboard.php';
        } else {
            alert('Login failed: ' + (result.error || 'Unknown error'));
        }
    } catch (error) {
        alert('Error during login: ' + error.message);
    }
});