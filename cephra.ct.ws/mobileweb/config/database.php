<?php
class Database {
    private $host;
    private $db_name;
    private $username;
    private $password;
    private $charset = 'utf8mb4';
    private $conn;
    
    public function __construct() {
        // CephraDomain folder - ALWAYS use production settings
        // This folder is specifically for domain deployment
        $this->host = 'sql210.infinityfree.com';
        $this->db_name = 'if0_39905578_cephradb';
        $this->username = 'if0_39905578';
        $this->password = 'VRDLpd07ZT5oH';
    }
    
    private function isLocalhost() {
        // Check if running on localhost
        $host = $_SERVER['HTTP_HOST'] ?? '';
        return (
            $host === 'localhost' || 
            $host === '127.0.0.1' || 
            strpos($host, 'localhost:') === 0 ||
            strpos($host, '127.0.0.1:') === 0
        );
    }
    
    public function getConnection() {
        try {
            $dsn = "mysql:host={$this->host};dbname={$this->db_name};charset={$this->charset}";
            $options = [
                PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
                PDO::ATTR_EMULATE_PREPARES => false
            ];
            $this->conn = new PDO($dsn, $this->username, $this->password, $options);
            
            // Log successful connection (only in development)
            if ($this->isLocalhost()) {
                error_log("Database connected successfully to LOCALHOST: {$this->host}/{$this->db_name}");
            }
            
            return $this->conn;
        } catch(PDOException $e) {
            $env = $this->isLocalhost() ? 'LOCALHOST' : 'PRODUCTION';
            error_log("Database connection failed ({$env}): " . $e->getMessage());
            error_log("Attempted connection: {$this->host}/{$this->db_name} as {$this->username}");
            return null;
        }
    }
    
    public function getEnvironmentInfo() {
        return [
            'environment' => 'domain_production',
            'host' => $this->host,
            'database' => $this->db_name,
            'username' => $this->username,
            'note' => 'CephraDomain folder - always uses production settings'
        ];
    }
}
?>
