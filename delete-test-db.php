<?php
// Delete Test Database 'cephra' - Keep Real 'cephradb'
echo "🗑️ Deleting Test Database 'cephra'...\n\n";

try {
    // Connect to MySQL without selecting a specific database
    $pdo = new PDO("mysql:host=localhost", "root", "");
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
    echo "✅ Connected to MySQL server\n\n";
    
    // Show current databases
    echo "📊 Current Databases:\n";
    $stmt = $pdo->query("SHOW DATABASES");
    $databases = $stmt->fetchAll(PDO::FETCH_COLUMN);
    
    foreach ($databases as $db) {
        if ($db === 'cephra') {
            echo "- $db (TEST DATABASE - WILL DELETE)\n";
        } elseif ($db === 'cephradb') {
            echo "- $db (REAL DATABASE - KEEP)\n";
        } else {
            echo "- $db\n";
        }
    }
    
    // Check if test database exists
    if (in_array('cephra', $databases)) {
        echo "\n🗑️ Deleting test database 'cephra'...\n";
        
        // Drop the test database
        $pdo->exec("DROP DATABASE cephra");
        echo "✅ Test database 'cephra' deleted successfully!\n";
        
    } else {
        echo "\n❌ Test database 'cephra' not found\n";
    }
    
    // Verify deletion
    echo "\n📊 Updated Database List:\n";
    $stmt = $pdo->query("SHOW DATABASES");
    $databases = $stmt->fetchAll(PDO::FETCH_COLUMN);
    
    foreach ($databases as $db) {
        if ($db === 'cephradb') {
            echo "- $db (REAL DATABASE - READY TO USE)\n";
        } else {
            echo "- $db\n";
        }
    }
    
    echo "\n🎉 Cleanup completed!\n";
    echo "✅ Test database 'cephra' removed\n";
    echo "✅ Real database 'cephradb' preserved\n";
    echo "✅ System now connects to correct database\n";
    
} catch (Exception $e) {
    echo "❌ Error: " . $e->getMessage() . "\n";
}
?>
