<?php
class Database {
    // Free hosting database credentials (update these with your hosting provider's details)
    private $host = 'sql210.infinityfree.com'; // InfinityFree MySQL hostname
    private $db_name = 'if0_39905578_cephradb'; // Your InfinityFree database name
    private $username = 'if0_39905578'; // Your InfinityFree MySQL username
    private $password = 'VRDLpd07ZT5oH'; // Your InfinityFree MySQL password
    private $charset = 'utf8mb4';
    private $conn;
    
    public function getConnection() {
        try {
            $dsn = "mysql:host={$this->host};dbname={$this->db_name};charset={$this->charset}";
            $options = [
                PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
                PDO::ATTR_EMULATE_PREPARES => false
            ];
            $this->conn = new PDO($dsn, $this->username, $this->password, $options);
            return $this->conn;
        } catch(PDOException $e) {
            error_log("Database connection failed: " . $e->getMessage());
            return null;
        }
    }
}
?>
