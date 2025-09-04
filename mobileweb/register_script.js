document.getElementById('registerForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const firstname = document.getElementById('firstname').value.trim();
    const lastname = document.getElementById('lastname').value.trim();
    const username = document.getElementById('username').value.trim(); // create username from firstname+lastname
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    const confirmPassword = document.getElementById('confirmPassword').value.trim();

    if (!firstname || !lastname || !email || !password || !confirmPassword) {
        alert('Please fill in all fields.');
        return;
    }

    if (password !== confirmPassword) {
        alert('Passwords do not match.');
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

        const response = await fetch('../api/mobile.php', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.success) {
            alert('Registration successful! You can now login.');
            // Optionally redirect to login page
            window.location.href = 'index.php';
        } else {
            alert('Registration failed: ' + (result.error || 'Unknown error'));
        }
    } catch (error) {
        alert('Error during registration: ' + error.message);
    }
});