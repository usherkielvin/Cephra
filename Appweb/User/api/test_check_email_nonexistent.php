<?php
// Test check_email.php API with non-existent email
$_SERVER["REQUEST_METHOD"] = "POST";
$_POST["email"] = "nonexistent@example.com";

require_once "check_email.php";
?>
