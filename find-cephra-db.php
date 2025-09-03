<?php
// Find All Available Databases
echo "🔍 Finding All Available Databases...\n\n";

try {
    // Connect to MySQL without selecting a specific database
    $pdo = new PDO("mysql:host=localhost", "root", "");
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
    echo "✅ Connected to MySQL server\n\n";
    
    // Show all databases
    echo "📊 All Available Databases:\n";
    $stmt = $pdo->query("SHOW DATABASES");
    $databases = $stmt->fetchAll(PDO::FETCH_COLUMN);
    
    foreach ($databases as $db) {
        echo "- $db\n";
    }
    
    echo "\n🔍 Looking for Cephra-related databases...\n";
    
    // Check each database for Cephra-related tables
    foreach ($databases as $db) {
        if ($db !== 'information_schema' && $db !== 'mysql' && $db !== 'performance_schema' && $db !== 'phpmyadmin') {
            echo "\n📋 Checking database: $db\n";
            
            try {
                $pdo->exec("USE $db");
                
                // Check for queue-related tables
                $stmt = $pdo->query("SHOW TABLES");
                $tables = $stmt->fetchAll(PDO::FETCH_COLUMN);
                
                $cephraTables = [];
                foreach ($tables as $table) {
                    if (strpos(strtolower($table), 'queue') !== false || 
                        strpos(strtolower($table), 'ticket') !== false ||
                        strpos(strtolower($table), 'user') !== false) {
                        $cephraTables[] = $table;
                    }
                }
                
                if (count($cephraTables) > 0) {
                    echo "  ✅ Found Cephra-related tables: " . implode(', ', $cephraTables) . "\n";
                    
                    // Check queue table if it exists
                    foreach ($cephraTables as $table) {
                        if (strpos(strtolower($table), 'queue') !== false) {
                            $stmt = $pdo->query("SELECT COUNT(*) as count FROM $table");
                            $result = $stmt->fetch(PDO::FETCH_ASSOC);
                            echo "  📋 $table has {$result['count']} records\n";
                            
                            // Show sample data
                            if ($result['count'] > 0) {
                                $stmt = $pdo->query("SELECT * FROM $table LIMIT 3");
                                $sample = $stmt->fetchAll(PDO::FETCH_ASSOC);
                                echo "  📝 Sample data:\n";
                                foreach ($sample as $row) {
                                    echo "    " . json_encode($row) . "\n";
                                }
                            }
                        }
                    }
                } else {
                    echo "  ❌ No Cephra-related tables found\n";
                }
                
            } catch (Exception $e) {
                echo "  ❌ Error accessing $db: " . $e->getMessage() . "\n";
            }
        }
    }
    
    echo "\n🎯 RECOMMENDATION:\n";
    echo "Look for the database that has your actual Cephra project data.\n";
    echo "It might be named differently or in a different location.\n";
    
} catch (Exception $e) {
    echo "❌ Error: " . $e->getMessage() . "\n";
}
?>
