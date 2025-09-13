<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cephra Mobile Web</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<!-- From Uiverse.io by ammarsaa --> 
<div class="auth-wrapper">
<form class="form" id="registerForm">
    <p class="title">Register </p>
    <p class="message">Signup now and get full access to our app. </p>
        <div class="flex">
        <label>
            <input class="input" type="text" id="firstname" name="firstname" placeholder="" required>
            <span>Firstname</span>
        </label>

        <label>
            <input class="input" type="text" id="lastname" name="lastname" placeholder="" required>
            <span>Lastname</span>
        </label>
    </div>  
        <label>
            <input class="input" type="text" id="username" name="username" placeholder="" required>
            <span>Username</span>
        </label>
    <label>
        <input class="input" type="email" id="email" name="email" placeholder="" required>
        <span>Email</span>
    </label> 
        
    <label>
        <input class="input" type="password" id="password" name="password" placeholder="" required>
        <span>Password</span>
    </label>
    <label>
        <input class="input" type="password" id="confirmPassword" name="confirmPassword" placeholder="" required>
        <span>Confirm password</span>
    </label>
    <button class="submit" type="submit">Submit</button>
    <p class="signin">Already have an acount ? <a href="index.php">Login</a> </p>
</form>
</div>

         

    <script src="register_script.js"></script>
</body>
</html>
