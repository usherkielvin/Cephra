/**
 * Frontend API Integration Tests for Cephra Phone Interface
 * 
 * This file tests the API integration from the frontend perspective.
 * Run this in the browser console or include it in your test page.
 */

class CephraApiTester {
    constructor() {
        this.baseUrl = 'http://localhost:8080/api';
        this.testResults = [];
    }
    
    /**
     * Run all frontend API integration tests
     */
    async runAllTests() {
        console.log('🧪 Starting Cephra Frontend API Integration Tests...');
        
        await this.testApiConnectivity();
        await this.testLoginFlow();
        await this.testQueueDataFlow();
        await this.testTicketCreationFlow();
        await this.testErrorHandling();
        
        this.displayResults();
    }
    
    /**
     * Test basic API connectivity
     */
    async testApiConnectivity() {
        console.log('📡 Testing API Connectivity...');
        
        try {
            const response = await fetch(`${this.baseUrl}/mobile.php?action=queue`);
            const data = await response.json();
            
            this.addResult('API Connectivity', 'PASS', 
                `API is reachable. Response status: ${response.status}`);
        } catch (error) {
            this.addResult('API Connectivity', 'FAIL', 
                `API is not reachable: ${error.message}`);
        }
    }
    
    /**
     * Test login flow
     */
    async testLoginFlow() {
        console.log('🔐 Testing Login Flow...');
        
        // Test valid login
        try {
            const formData = new FormData();
            formData.append('username', 'dizon');
            formData.append('password', '123');
            formData.append('action', 'login');
            
            const response = await fetch(`${this.baseUrl}/mobile.php`, {
                method: 'POST',
                body: formData
            });
            
            const data = await response.json();
            
            if (data.success && data.username === 'dizon') {
                this.addResult('Valid Login', 'PASS', 
                    `Login successful for user: ${data.username}`);
            } else {
                this.addResult('Valid Login', 'FAIL', 
                    `Login failed: ${JSON.stringify(data)}`);
            }
        } catch (error) {
            this.addResult('Valid Login', 'FAIL', 
                `Login request failed: ${error.message}`);
        }
        
        // Test invalid login
        try {
            const formData = new FormData();
            formData.append('username', 'invalid');
            formData.append('password', 'wrong');
            formData.append('action', 'login');
            
            const response = await fetch(`${this.baseUrl}/mobile.php`, {
                method: 'POST',
                body: formData
            });
            
            const data = await response.json();
            
            if (data.error) {
                this.addResult('Invalid Login', 'PASS', 
                    `Correctly rejected invalid credentials: ${data.error}`);
            } else {
                this.addResult('Invalid Login', 'FAIL', 
                    `Should have rejected invalid credentials: ${JSON.stringify(data)}`);
            }
        } catch (error) {
            this.addResult('Invalid Login', 'FAIL', 
                `Invalid login request failed: ${error.message}`);
        }
    }
    
    /**
     * Test queue data flow
     */
    async testQueueDataFlow() {
        console.log('📋 Testing Queue Data Flow...');
        
        try {
            const response = await fetch(`${this.baseUrl}/mobile.php?action=queue`);
            const data = await response.json();
            
            if (Array.isArray(data)) {
                this.addResult('Queue Data Retrieval', 'PASS', 
                    `Retrieved ${data.length} queue tickets`);
                
                // Test data structure
                if (data.length > 0) {
                    const ticket = data[0];
                    const requiredFields = ['ticket_id', 'username', 'service_type', 'status'];
                    const hasRequiredFields = requiredFields.every(field => field in ticket);
                    
                    if (hasRequiredFields) {
                        this.addResult('Queue Data Structure', 'PASS', 
                            'Queue tickets have required fields');
                    } else {
                        this.addResult('Queue Data Structure', 'FAIL', 
                            'Queue tickets missing required fields');
                    }
                }
            } else {
                this.addResult('Queue Data Retrieval', 'FAIL', 
                    `Invalid response format: ${JSON.stringify(data)}`);
            }
        } catch (error) {
            this.addResult('Queue Data Retrieval', 'FAIL', 
                `Queue data request failed: ${error.message}`);
        }
    }
    
    /**
     * Test ticket creation flow
     */
    async testTicketCreationFlow() {
        console.log('🎫 Testing Ticket Creation Flow...');
        
        try {
            const ticketData = {
                ticket_id: 'TEST' + Date.now(),
                username: 'testuser',
                service_type: 'Fast Charging',
                initial_battery_level: 25
            };
            
            const response = await fetch(`${this.baseUrl}/mobile.php?action=create-ticket`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(ticketData)
            });
            
            const data = await response.json();
            
            if (data.success && data.ticket_id) {
                this.addResult('Ticket Creation', 'PASS', 
                    `Ticket created successfully: ${data.ticket_id}`);
            } else {
                this.addResult('Ticket Creation', 'FAIL', 
                    `Ticket creation failed: ${JSON.stringify(data)}`);
            }
        } catch (error) {
            this.addResult('Ticket Creation', 'FAIL', 
                `Ticket creation request failed: ${error.message}`);
        }
    }
    
    /**
     * Test error handling
     */
    async testErrorHandling() {
        console.log('⚠️ Testing Error Handling...');
        
        // Test unknown action
        try {
            const response = await fetch(`${this.baseUrl}/mobile.php?action=unknown`);
            const data = await response.json();
            
            if (data.error) {
                this.addResult('Unknown Action Handling', 'PASS', 
                    `Correctly handled unknown action: ${data.error}`);
            } else {
                this.addResult('Unknown Action Handling', 'FAIL', 
                    `Should have returned error for unknown action: ${JSON.stringify(data)}`);
            }
        } catch (error) {
            this.addResult('Unknown Action Handling', 'FAIL', 
                `Unknown action request failed: ${error.message}`);
        }
        
        // Test CORS headers
        try {
            const response = await fetch(`${this.baseUrl}/mobile.php?action=queue`);
            const corsHeader = response.headers.get('Access-Control-Allow-Origin');
            
            if (corsHeader) {
                this.addResult('CORS Headers', 'PASS', 
                    `CORS headers present: ${corsHeader}`);
            } else {
                this.addResult('CORS Headers', 'WARN', 
                    'CORS headers not found (may cause issues in production)');
            }
        } catch (error) {
            this.addResult('CORS Headers', 'FAIL', 
                `CORS test failed: ${error.message}`);
        }
    }
    
    /**
     * Test response time performance
     */
    async testPerformance() {
        console.log('⚡ Testing API Performance...');
        
        const startTime = performance.now();
        
        try {
            const response = await fetch(`${this.baseUrl}/mobile.php?action=queue`);
            const data = await response.json();
            
            const endTime = performance.now();
            const responseTime = endTime - startTime;
            
            if (responseTime < 2000) {
                this.addResult('API Performance', 'PASS', 
                    `Response time: ${responseTime.toFixed(2)}ms`);
            } else {
                this.addResult('API Performance', 'WARN', 
                    `Slow response time: ${responseTime.toFixed(2)}ms`);
            }
        } catch (error) {
            this.addResult('API Performance', 'FAIL', 
                `Performance test failed: ${error.message}`);
        }
    }
    
    /**
     * Add a test result
     */
    addResult(test, status, message) {
        this.testResults.push({
            test: test,
            status: status,
            message: message,
            timestamp: new Date().toISOString()
        });
        
        const emoji = status === 'PASS' ? '✅' : status === 'FAIL' ? '❌' : '⚠️';
        console.log(`${emoji} ${test}: ${status} - ${message}`);
    }
    
    /**
     * Display final results
     */
    displayResults() {
        console.log('\n📊 Test Results Summary:');
        console.log('========================');
        
        const passed = this.testResults.filter(r => r.status === 'PASS').length;
        const failed = this.testResults.filter(r => r.status === 'FAIL').length;
        const warnings = this.testResults.filter(r => r.status === 'WARN').length;
        const total = this.testResults.length;
        
        console.log(`Total Tests: ${total}`);
        console.log(`✅ Passed: ${passed}`);
        console.log(`❌ Failed: ${failed}`);
        console.log(`⚠️ Warnings: ${warnings}`);
        
        const passRate = total > 0 ? ((passed / total) * 100).toFixed(1) : 0;
        console.log(`Pass Rate: ${passRate}%`);
        
        if (failed === 0) {
            console.log('\n🎉 All tests passed! Your API integration is working correctly.');
        } else {
            console.log('\n⚠️ Some tests failed. Please check the issues above.');
        }
        
        // Return results for programmatic use
        return {
            total: total,
            passed: passed,
            failed: failed,
            warnings: warnings,
            passRate: passRate,
            results: this.testResults
        };
    }
    
    /**
     * Generate a test report
     */
    generateReport() {
        const results = this.displayResults();
        
        const report = {
            timestamp: new Date().toISOString(),
            summary: {
                total: results.total,
                passed: results.passed,
                failed: results.failed,
                warnings: results.warnings,
                passRate: results.passRate
            },
            tests: results.results
        };
        
        console.log('\n📋 Detailed Test Report:');
        console.log(JSON.stringify(report, null, 2));
        
        return report;
    }
}

// Auto-run tests when this script is loaded
if (typeof window !== 'undefined') {
    // Browser environment
    window.CephraApiTester = CephraApiTester;
    
    // Auto-run if in test mode
    if (window.location.search.includes('test=true')) {
        const tester = new CephraApiTester();
        tester.runAllTests();
    }
} else {
    // Node.js environment
    module.exports = CephraApiTester;
}

// Usage examples:
/*
// Run tests manually in browser console:
const tester = new CephraApiTester();
tester.runAllTests();

// Run performance test only:
const tester = new CephraApiTester();
tester.testPerformance();

// Generate detailed report:
const tester = new CephraApiTester();
await tester.runAllTests();
const report = tester.generateReport();
*/
