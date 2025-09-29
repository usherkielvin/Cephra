<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
    exit();
}

require_once 'config/database.php';

$db = new Database();
$conn = $db->getConnection();

$username = $_SESSION['username'];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    try {
        // Fetch profile picture before deletion
        $stmt = $conn->prepare("SELECT profile_picture FROM users WHERE username = :username");
        $stmt->bindParam(':username', $username);
        $stmt->execute();
        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        // Delete profile picture file if exists
        if ($user && !empty($user['profile_picture'])) {
            $filePath = 'uploads/profile_pictures/' . $user['profile_picture'];
            if (file_exists($filePath)) {
                unlink($filePath);
            }
        }

        // Delete user from database
        $stmt = $conn->prepare("DELETE FROM users WHERE username = :username");
        $stmt->bindParam(':username', $username);
        $stmt->execute();

        // Destroy session
        session_destroy();

        // Redirect to login page with success message
        header("Location: index.php?message=Account deleted successfully");
        exit();
    } catch (Exception $e) {
        // Log error and redirect with error message
        error_log("Delete account error: " . $e->getMessage());
        header("Location: profile.php?error=Failed to delete account. Please try again.");
        exit();
    }
} else {
    // Invalid request method
    header("Location: profile.php");
    exit();
}
?>
