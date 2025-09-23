<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
    exit();
}
require_once 'config/database.php';
$db = new Database();
$conn = $db->getConnection();
if ($conn) {
    $username = $_SESSION['username'];
    $stmt = $conn->prepare("SELECT firstname FROM users WHERE username = :username");
    $stmt->bindParam(':username', $username);
    $stmt->execute();
    $user = $stmt->fetch(PDO::FETCH_ASSOC);
$firstname = $user ? $user['firstname'] : 'User';

echo "<!-- DEBUG: Session username: " . htmlspecialchars($_SESSION['username']) . " -->";
echo "<!-- DEBUG: Fetched firstname: " . htmlspecialchars($firstname) . " -->";

} else {
    $firstname = 'User';
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Cephra - Dashboard</title>
    <link rel="icon" type="image/png" href="images/logo.png?v=2" />
    <link rel="apple-touch-icon" href="images/logo.png?v=2" />
    <link rel="manifest" href="manifest.webmanifest" />
    <meta name="theme-color" content="#1a1a2e" />

    <link rel="stylesheet" href="css/vantage-style.css" />
    <link rel="stylesheet" href="assets/css/fontawesome-all.min.css" />
		<style>
			/* ============================================
			   VANTAGE MARKETS INSPIRED WHITE THEME
			   ============================================ */

			:root {
				/* Light Theme - Professional White Theme */
				--primary-color: #00c2ce;
				--primary-dark: #0e3a49;
				--secondary-color: #f8fafc;
				--accent-color: #e2e8f0;
				--text-primary: #1a202c;
				--text-secondary: rgba(26, 32, 44, 0.8);
				--text-muted: rgba(26, 32, 44, 0.6);
				--bg-primary: #ffffff;
				--bg-secondary: #f8fafc;
				--bg-card: #ffffff;
				--border-color: rgba(26, 32, 44, 0.1);
				--shadow-light: rgba(0, 194, 206, 0.1);
				--shadow-medium: rgba(0, 194, 206, 0.2);
				--shadow-strong: rgba(0, 194, 206, 0.3);
				--gradient-primary: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%);
				--gradient-secondary: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
				--gradient-accent: linear-gradient(135deg, #00c2ce 0%, #0e3a49 50%, #1a202c 100%);
			}

			/* ============================================
			   EASY-TO-ADJUST POPUP STYLES
			   ============================================ */

			.popup-overlay {
				position: fixed;
				top: 0;
				left: 0;
				width: 100%;
				height: 100%;
				background: rgba(0, 0, 0, 0.7);
				display: flex;
				align-items: center;
				justify-content: center;
				z-index: 10000;
				padding: 20px;
			}

			.popup-content {
				position: relative;
				width: 90%;
				max-width: 400px;
				max-height: 85vh;
				border-radius: 20px;
				overflow: hidden;
				display: flex;
				align-items: center;
				justify-content: center;
				animation: popupSlideIn 0.3s ease-out;
			}

			.popup-image {
				width: 100%;
				height: auto;
				max-height: 100%;
				max-width: 100%;
				object-fit: contain;
				display: block;
				border-radius: 20px;
			}

			.close-btn {
				position: absolute;
				top: 15px;
				right: 15px;
				background: none;
				border: none;
				color: #333;
				font-size: 24px;
				cursor: pointer;
				z-index: 10001;
				padding: 0;
				min-width: auto;
				font-weight: normal;
			}

			@keyframes popupSlideIn {
				from {
					opacity: 0;
					transform: scale(0.8) translateY(-50px);
				}
				to {
					opacity: 1;
					transform: scale(1) translateY(0);
				}
			}

			/* ============================================
			   MODERN DASHBOARD STYLES - WHITE THEME
			   ============================================ */

			/* Header Styles */
			.header {
				position: fixed;
				top: 0;
				left: 0;
				right: 0;
				width: 100vw;
				background: rgba(255, 255, 255, 0.95);
				backdrop-filter: blur(20px);
				border-bottom: 1px solid var(--border-color);
				z-index: 1000;
				transition: all 0.3s ease;
				box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
			}

			.header.scrolled {
				background: rgba(255, 255, 255, 0.98);
				box-shadow: 0 2px 20px rgba(0, 0, 0, 0.15);
			}

			.header-content {
				display: flex;
				align-items: center;
				justify-content: space-between;
				padding: 1rem 0;
				width: 100%;
			}

			.logo {
				display: flex;
				align-items: center;
				gap: 12px;
				text-decoration: none;
			}

			.logo-img {
				width: 40px;
				height: 40px;
				border-radius: 10px;
				object-fit: cover;
			}

			.logo-text {
				font-size: 24px;
				font-weight: 800;
				color: var(--text-primary);
				letter-spacing: 1px;
			}

			.nav-list {
				display: flex;
				list-style: none;
				gap: 2rem;
				align-items: center;
			}

			.nav-link {
				color: var(--text-secondary);
				text-decoration: none;
				font-weight: 500;
				transition: all 0.3s ease;
				position: relative;
			}

			.nav-link:hover {
				color: var(--primary-color);
			}

			.nav-link::after {
				content: '';
				position: absolute;
				bottom: -5px;
				left: 0;
				width: 0;
				height: 2px;
				background: var(--gradient-primary);
				transition: width 0.3s ease;
			}

			.nav-link:hover::after {
				width: 100%;
			}

			.header-actions {
				display: flex;
				align-items: center;
				gap: 1.5rem;
			}

			.auth-link {
				color: var(--text-secondary);
				text-decoration: none;
				font-weight: 500;
				transition: all 0.3s ease;
				position: relative;
				padding: 0.5rem 0;
			}

			.auth-link:hover {
				color: var(--primary-color);
			}

			.auth-link::after {
				content: '';
				position: absolute;
				bottom: 0;
				left: 0;
				width: 0;
				height: 2px;
				background: var(--primary-color);
				transition: width 0.3s ease;
			}

			.auth-link:hover::after {
				width: 100%;
			}

			.mobile-menu-toggle {
				display: none;
				flex-direction: column;
				background: none;
				border: none;
				cursor: pointer;
				padding: 8px;
				gap: 4px;
			}

			.mobile-menu-toggle span {
				width: 25px;
				height: 3px;
				background: var(--text-primary);
				transition: all 0.3s ease;
			}

			.mobile-menu-toggle.active span:nth-child(1) {
				transform: rotate(45deg) translate(6px, 6px);
			}

			.mobile-menu-toggle.active span:nth-child(2) {
				opacity: 0;
			}

			.mobile-menu-toggle.active span:nth-child(3) {
				transform: rotate(-45deg) translate(6px, -6px);
			}

			/* Mobile Menu Styles */
			.mobile-menu {
				position: fixed;
				top: 0;
				right: -100%;
				width: 280px;
				height: 100vh;
				background: rgba(255, 255, 255, 0.98);
				backdrop-filter: blur(20px);
				border-left: 1px solid var(--border-color);
				z-index: 999;
				transition: right 0.3s ease;
				box-shadow: -5px 0 20px rgba(0, 0, 0, 0.1);
			}

			.mobile-menu.active {
				right: 0;
			}

			.mobile-menu-content {
				padding: 80px 2rem 2rem;
				height: 100%;
				display: flex;
				flex-direction: column;
				gap: 2rem;
			}

			.mobile-nav-list {
				list-style: none;
				padding: 0;
				margin: 0;
				display: flex;
				flex-direction: column;
				gap: 1rem;
			}

			.mobile-nav-link {
				color: var(--text-primary);
				text-decoration: none;
				font-weight: 500;
				padding: 1rem;
				border-radius: 8px;
				transition: all 0.3s ease;
				display: block;
			}

			.mobile-nav-link:hover {
				background: var(--bg-secondary);
				color: var(--primary-color);
			}

			.mobile-header-actions {
				margin-top: auto;
				padding-top: 2rem;
				border-top: 1px solid var(--border-color);
			}

			.mobile-auth-link {
				color: var(--text-secondary);
				text-decoration: none;
				font-weight: 500;
				padding: 1rem;
				border-radius: 8px;
				transition: all 0.3s ease;
				display: block;
				text-align: center;
				background: var(--gradient-primary);
				color: white;
			}

			.mobile-auth-link:hover {
				transform: translateY(-2px);
				box-shadow: 0 5px 15px var(--shadow-medium);
			}

			/* Mobile Menu Overlay */
			.mobile-menu-overlay {
				position: fixed;
				top: 0;
				left: 0;
				width: 100%;
				height: 100%;
				background: rgba(0, 0, 0, 0.5);
				z-index: 998;
				opacity: 0;
				visibility: hidden;
				transition: all 0.3s ease;
			}

			.mobile-menu-overlay.active {
				opacity: 1;
				visibility: visible;
			}

			/* Dashboard Hero Section */
			.dashboard-hero {
				background: var(--gradient-secondary);
				padding: 100px 0;
				text-align: center;
				position: relative;
				overflow: hidden;
			}

			.dashboard-hero::before {
				content: '';
				position: absolute;
				top: 0;
				left: 0;
				right: 0;
				bottom: 0;
				background: radial-gradient(circle at 20% 80%, rgba(0, 194, 206, 0.15) 0%, transparent 50%), radial-gradient(circle at 80% 20%, rgba(14, 58, 73, 0.1) 0%, transparent 50%), radial-gradient(circle at 40% 40%, rgba(0, 194, 206, 0.08) 0%, transparent 50%), linear-gradient(135deg, rgba(248, 250, 252, 0.9) 0%, rgba(226, 232, 240, 0.7) 100%);
				z-index: 1;
			}

			.dashboard-hero::after {
				content: '';
				position: absolute;
				top: 0;
				left: 0;
				right: 0;
				bottom: 0;
				background-image: linear-gradient(45deg, transparent 40%, rgba(0, 194, 206, 0.03) 40%, rgba(0, 194, 206, 0.03) 60%, transparent 60%), linear-gradient(-45deg, transparent 40%, rgba(14, 58, 73, 0.02) 40%, rgba(14, 58, 73, 0.02) 60%, transparent 60%);
				background-size: 60px 60px, 40px 40px;
				opacity: 0.6;
				z-index: 1;
			}

			.dashboard-greeting {
				font-size: clamp(3rem, 8vw, 6rem);
				font-weight: 900;
				line-height: 1.1;
				margin-bottom: 1.5rem;
				position: relative;
				z-index: 2;
			}

			.dashboard-greeting-main {
				display: block;
				background: var(--gradient-primary);
				-webkit-background-clip: text;
				-webkit-text-fill-color: transparent;
				background-clip: text;
			}

			.dashboard-greeting-accent {
				display: block;
				color: var(--primary-color);
				font-style: italic;
				margin: 0.5rem 0;
			}

			.dashboard-greeting-sub {
				display: block;
				color: var(--text-secondary);
				font-size: 0.7em;
				font-weight: 400;
			}

			.dashboard-actions {
				display: flex;
				gap: 1.5rem;
				justify-content: center;
				flex-wrap: wrap;
				position: relative;
				z-index: 2;
			}

			.dashboard-btn {
				padding: 12px 24px;
				border: none;
				border-radius: 8px;
				text-decoration: none;
				font-weight: 600;
				transition: all 0.3s ease;
				cursor: pointer;
				display: inline-block;
				text-align: center;
			}

			.btn-rewards {
				background: var(--gradient-primary);
				color: white;
			}

			.btn-rewards:hover {
				transform: translateY(-2px);
				box-shadow: 0 8px 25px var(--shadow-medium);
			}

			.btn-wallet {
				background: transparent;
				color: var(--text-primary);
				border: 2px solid var(--primary-color);
			}

			.btn-wallet:hover {
				background: var(--primary-color);
				color: white;
				transform: translateY(-2px);
			}

			/* Features Section */
			.features {
				padding: 100px 0;
				background: var(--bg-secondary);
			}

			.section-header {
				text-align: center;
				margin-bottom: 60px;
			}

			.section-title {
				font-size: 2.5rem;
				font-weight: 700;
				margin-bottom: 1rem;
				background: var(--gradient-primary);
				-webkit-background-clip: text;
				-webkit-text-fill-color: transparent;
				background-clip: text;
			}

			.section-description {
				font-size: 1.2rem;
				color: var(--text-secondary);
				max-width: 600px;
				margin: 0 auto;
			}

			.features-grid {
				display: grid;
				grid-template-columns: repeat(4, 1fr);
				gap: 2rem;
			}

			.main-vehicle-card {
				grid-column: span 4;
			}

			.feature-card {
				background: var(--bg-card);
				border-radius: 20px;
				padding: 2rem;
				border: 1px solid var(--border-color);
				transition: all 0.3s ease;
				opacity: 0;
				transform: translateY(30px);
			}

			.feature-card.animate-in {
				opacity: 1;
				transform: translateY(0);
			}

			.feature-card:hover {
				transform: translateY(-10px);
				box-shadow: 0 20px 40px var(--shadow-medium);
				border-color: var(--primary-color);
			}

			.feature-icon {
				width: 60px;
				height: 60px;
				background: var(--gradient-primary);
				border-radius: 15px;
				display: flex;
				align-items: center;
				justify-content: center;
				margin-bottom: 1.5rem;
				font-size: 24px;
				color: white;
			}

			.feature-title {
				font-size: 1.5rem;
				font-weight: 600;
				margin-bottom: 1rem;
				color: var(--text-primary);
			}

			.feature-description {
				color: var(--text-secondary);
				margin-bottom: 1.5rem;
				line-height: 1.6;
			}

			.feature-link {
				color: var(--primary-color);
				text-decoration: none;
				font-weight: 600;
				transition: all 0.3s ease;
			}

			.feature-link:hover {
				color: var(--text-primary);
				text-shadow: 0 0 10px var(--primary-color);
			}

			/* Responsive Design */
			@media (max-width: 768px) {
				.header-content {
					flex-wrap: wrap;
				}

				.nav {
					display: none;
				}

				.header-actions {
					display: none;
				}

				.mobile-menu-toggle {
					display: flex;
				}

				.dashboard-greeting {
					font-size: 2rem;
				}

				.dashboard-actions {
					flex-direction: column;
					align-items: center;
				}

				.section-title {
					font-size: 2rem;
				}

				.features-grid {
					grid-template-columns: 1fr;
				}
			}

			@media (max-width: 690px) {
				.dashboard-hero {
					padding: 60px 0;
				}

				.dashboard-greeting {
					font-size: 1.8rem;
				}

				.feature-card {
					padding: 1.5rem;
				}

				.main-vehicle-card {
					grid-column: 1;
				}
			}

			/* ============================================
			   MAIN VEHICLE CARD STYLES
			   ============================================ */

			.main-vehicle-card {
				background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%);
				color: white;
				position: relative;
				overflow: hidden;
				border-radius: 20px;
				padding: 2.5rem;
				box-shadow: 0 20px 40px rgba(0, 194, 206, 0.3);
			}

			.main-vehicle-content {
				display: flex;
				justify-content: space-between;
				align-items: center;
				position: relative;
				z-index: 2;
			}

			.vehicle-info {
				display: flex;
				align-items: center;
				gap: 2rem;
			}

			.feature-icon-large {
				width: 80px;
				height: 80px;
				background: rgba(255, 255, 255, 0.2);
				border-radius: 20px;
				display: flex;
				align-items: center;
				justify-content: center;
				font-size: 32px;
				color: white;
				backdrop-filter: blur(10px);
			}

			.vehicle-details {
				flex: 1;
			}

			.vehicle-stats {
				display: grid;
				grid-template-columns: repeat(2, 1fr);
				gap: 1rem;
				margin-top: 1rem;
			}

			.stat-item {
				display: flex;
				flex-direction: column;
				gap: 0.25rem;
			}

			.stat-label {
				font-size: 0.9rem;
				opacity: 0.8;
				font-weight: 500;
			}

			.stat-value {
				font-size: 1.1rem;
				font-weight: 600;
			}

			.vehicle-actions {
				display: flex;
				flex-direction: column;
				gap: 1rem;
				align-items: flex-end;
			}

			.quick-action-btn {
				background: rgba(255, 255, 255, 0.2);
				color: white;
				border: 1px solid rgba(255, 255, 255, 0.3);
				padding: 0.75rem 1.5rem;
				border-radius: 25px;
				cursor: pointer;
				font-weight: 600;
				transition: all 0.3s ease;
				backdrop-filter: blur(10px);
			}

			.quick-action-btn:hover {
				background: rgba(255, 255, 255, 0.3);
				transform: translateY(-2px);
			}

			.vehicle-bg-pattern {
				position: absolute;
				top: 0;
				left: 0;
				right: 0;
				bottom: 0;
				background-image: radial-gradient(circle at 20% 20%, rgba(255, 255, 255, 0.1) 0%, transparent 50%), radial-gradient(circle at 80% 80%, rgba(255, 255, 255, 0.05) 0%, transparent 50%);
				z-index: 1;
			}

			/* ============================================
			   ADDITIONAL FEATURE STYLES
			   ============================================ */

			/* Stats Section */
			.stats-section {
				background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
			}

			.stats-grid {
				display: grid;
				grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
				gap: 2rem;
			}

			.stat-card {
				background: white;
				padding: 2rem;
				border-radius: 12px;
				box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
				display: flex;
				align-items: center;
				gap: 1.5rem;
				transition: all 0.3s ease;
			}

			.stat-card:hover {
				transform: translateY(-3px);
				box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
			}

			.stat-icon {
				width: 50px;
				height: 50px;
				background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
				border-radius: 10px;
				display: flex;
				align-items: center;
				justify-content: center;
				font-size: 1.2rem;
				color: white;
			}

			.stat-content {
				flex: 1;
			}

			.stat-number {
				font-size: 2rem;
				font-weight: 700;
				color: #1a1a2e;
				margin: 0;
				line-height: 1;
			}

			.stat-label {
				color: #666;
				margin: 0.5rem 0 0 0;
				font-size: 0.9rem;
				font-weight: 500;
			}

			/* Live Status Section */
			.status-grid {
				display: grid;
				grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
				gap: 2rem;
			}

			.status-card {
				background: white;
				padding: 2rem;
				border-radius: 12px;
				box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
				border-left: 4px solid #28a745;
			}

			.status-title {
				font-size: 1.2rem;
				font-weight: 600;
				color: #1a1a2e;
				margin-bottom: 1rem;
			}

			.status-indicator {
				display: flex;
				align-items: center;
				gap: 0.5rem;
				margin-bottom: 0.5rem;
			}

			.status-dot {
				width: 12px;
				height: 12px;
				border-radius: 50%;
				background: #28a745;
			}

			.status-dot.active {
				animation: pulse 2s infinite;
			}

			@keyframes pulse {
				0% { opacity: 1; }
				50% { opacity: 0.5; }
				100% { opacity: 1; }
			}

			.status-text {
				font-weight: 600;
				color: #28a745;
			}

			.queue-info, .active-sessions {
				display: flex;
				align-items: baseline;
				gap: 0.5rem;
				margin-bottom: 0.5rem;
			}

			.queue-number, .session-number {
				font-size: 2rem;
				font-weight: 700;
				color: #667eea;
			}

			.queue-label, .session-label {
				color: #666;
				font-size: 0.9rem;
			}

			.status-description {
				color: #666;
				line-height: 1.6;
				margin: 0;
			}

			/* Recent Activity Section */
			.activity-list {
				margin-bottom: 2rem;
			}

			.activity-item {
				display: flex;
				align-items: flex-start;
				gap: 1rem;
				padding: 1.5rem;
				background: white;
				border-radius: 8px;
				box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
				margin-bottom: 1rem;
			}

			.activity-icon {
				width: 40px;
				height: 40px;
				background: #e9ecef;
				border-radius: 8px;
				display: flex;
				align-items: center;
				justify-content: center;
				color: #667eea;
				font-size: 1rem;
			}

			.activity-content {
				flex: 1;
			}

			.activity-title {
				font-size: 1rem;
				font-weight: 600;
				color: #1a1a2e;
				margin: 0 0 0.25rem 0;
			}

			.activity-description {
				color: #666;
				font-size: 0.9rem;
				margin: 0 0 0.5rem 0;
			}

			.activity-time {
				color: #999;
				font-size: 0.8rem;
			}

			.activity-actions {
				text-align: center;
			}

			.activity-link {
				color: #667eea;
				text-decoration: none;
				font-weight: 600;
				transition: color 0.3s ease;
			}

			.activity-link:hover {
				color: #764ba2;
			}

			/* Modal Styles */
			.modal-overlay {
				position: fixed;
				top: 0;
				left: 0;
				width: 100%;
				height: 100%;
				background: rgba(0, 0, 0, 0.7);
				display: flex;
				align-items: center;
				justify-content: center;
				z-index: 10000;
				padding: 20px;
			}

			.modal-content {
				background: white;
				border-radius: 12px;
				box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
				max-width: 500px;
				width: 100%;
				max-height: 90vh;
				overflow-y: auto;
				animation: modalSlideIn 0.3s ease-out;
			}

			.modal-content.large {
				max-width: 700px;
			}

			@keyframes modalSlideIn {
				from {
					opacity: 0;
					transform: scale(0.9) translateY(-20px);
				}
				to {
					opacity: 1;
					transform: scale(1) translateY(0);
				}
			}

			.modal-header {
				padding: 1.5rem;
				border-bottom: 1px solid #e9ecef;
				display: flex;
				justify-content: space-between;
				align-items: center;
			}

			.modal-title {
				font-size: 1.25rem;
				font-weight: 600;
				color: #1a1a2e;
				margin: 0;
			}

			.modal-close {
				background: none;
				border: none;
				font-size: 1.5rem;
				color: #666;
				cursor: pointer;
				padding: 0;
				width: 30px;
				height: 30px;
				display: flex;
				align-items: center;
				justify-content: center;
				border-radius: 50%;
				transition: background-color 0.3s ease;
			}

			.modal-close:hover {
				background: #f8f9fa;
				color: #333;
			}

			.modal-body {
				padding: 1.5rem;
			}

			.modal-footer {
				padding: 1rem 1.5rem;
				border-top: 1px solid #e9ecef;
				display: flex;
				justify-content: flex-end;
				gap: 1rem;
			}

			.modal-btn {
				padding: 0.75rem 1.5rem;
				border: none;
				border-radius: 6px;
				font-weight: 600;
				cursor: pointer;
				transition: all 0.3s ease;
			}

			.modal-btn.primary {
				background: #667eea;
				color: white;
			}

			.modal-btn.primary:hover {
				background: #764ba2;
			}

			.modal-btn.secondary {
				background: #f8f9fa;
				color: #666;
				border: 1px solid #dee2e6;
			}

			.modal-btn.secondary:hover {
				background: #e9ecef;
			}

			/* Form Styles */
			.form-group {
				margin-bottom: 1.5rem;
			}

			.form-group label {
				display: block;
				margin-bottom: 0.5rem;
				font-weight: 600;
				color: #1a1a2e;
			}

			.form-group input,
			.form-group select {
				width: 100%;
				padding: 0.75rem;
				border: 1px solid #dee2e6;
				border-radius: 6px;
				font-size: 1rem;
				transition: border-color 0.3s ease;
			}

			.form-group input:focus,
			.form-group select:focus {
				outline: none;
				border-color: #667eea;
				box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
			}

			/* Support Options */
			.support-options {
				display: grid;
				grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
				gap: 1rem;
			}

			.support-option {
				padding: 1.5rem;
				background: #f8f9fa;
				border-radius: 8px;
				text-align: center;
				cursor: pointer;
				transition: all 0.3s ease;
			}

			.support-option:hover {
				background: #e9ecef;
				transform: translateY(-2px);
			}

			.support-icon {
				width: 50px;
				height: 50px;
				background: #667eea;
				border-radius: 50%;
				display: flex;
				align-items: center;
				justify-content: center;
				margin: 0 auto 1rem;
				color: white;
				font-size: 1.2rem;
			}

			.support-option h4 {
				font-size: 1.1rem;
				font-weight: 600;
				color: #1a1a2e;
				margin: 0 0 0.5rem 0;
			}

			.support-option p {
				color: #666;
				font-size: 0.9rem;
				margin: 0;
			}

			/* Stations List */
			.stations-list {
				max-height: 400px;
				overflow-y: auto;
			}

			.station-item {
				display: flex;
				justify-content: space-between;
				align-items: center;
				padding: 1rem;
				border: 1px solid #e9ecef;
				border-radius: 8px;
				margin-bottom: 1rem;
			}

			.station-info h4 {
				font-size: 1.1rem;
				font-weight: 600;
				color: #1a1a2e;
				margin: 0 0 0.5rem 0;
			}

			.station-info p {
				color: #666;
				margin: 0 0 0.5rem 0;
				font-size: 0.9rem;
			}

			.station-features {
				display: flex;
				gap: 0.5rem;
				flex-wrap: wrap;
			}

			.feature-tag {
				background: #e9ecef;
				color: #495057;
				padding: 0.25rem 0.5rem;
				border-radius: 12px;
				font-size: 0.75rem;
				font-weight: 500;
			}

			.station-status {
				display: flex;
				flex-direction: column;
				align-items: flex-end;
				gap: 0.5rem;
			}

			.status-available {
				background: #28a745;
				color: white;
				padding: 0.25rem 0.75rem;
				border-radius: 12px;
				font-size: 0.8rem;
				font-weight: 600;
			}

			.station-btn {
				background: #667eea;
				color: white;
				border: none;
				padding: 0.5rem 1rem;
				border-radius: 6px;
				font-weight: 600;
				cursor: pointer;
				transition: background-color 0.3s ease;
			}

			.station-btn:hover {
				background: #764ba2;
			}

			/* Enhanced Responsive Design */
			@media (max-width: 1200px) {
				.features-grid {
					grid-template-columns: repeat(3, 1fr);
					gap: 1.5rem;
				}
			}

			@media (max-width: 992px) {
				.stats-grid {
					grid-template-columns: repeat(2, 1fr);
				}

				.features-grid {
					grid-template-columns: repeat(2, 1fr);
					gap: 1.5rem;
				}

				.status-grid {
					grid-template-columns: 1fr;
				}

				/* Improve feature card spacing on tablet */
				.feature-card {
					padding: 1.5rem;
				}

				.feature-icon {
					width: 50px;
					height: 50px;
					font-size: 20px;
				}

				.feature-title {
					font-size: 1.3rem;
				}
			}

			@media (max-width: 780px) {
				.stats-grid {
					grid-template-columns: 1fr;
					gap: 1rem;
				}

				.features-grid {
					grid-template-columns: 1fr;
					gap: 1.5rem;
					padding: 0 1rem;
				}

				.status-grid {
					grid-template-columns: 1fr;
					gap: 1rem;
				}

				.stat-card {
					flex-direction: column;
					text-align: center;
					gap: 1rem;
					padding: 1.5rem;
				}

				.support-options {
					grid-template-columns: 1fr;
					gap: 1rem;
				}

				.station-item {
					flex-direction: column;
					align-items: flex-start;
					gap: 1rem;
				}

				.station-status {
					flex-direction: row;
					width: 100%;
					justify-content: space-between;
				}

				/* Optimize feature cards for mobile */
				.feature-card {
					padding: 1.5rem;
					margin-bottom: 1rem;
					border-radius: 15px;
					box-shadow: 0 5px 15px rgba(0, 194, 206, 0.1);
				}

				.feature-icon {
					width: 50px;
					height: 50px;
					font-size: 20px;
					margin-bottom: 1rem;
				}

				.feature-title {
					font-size: 1.25rem;
					margin-bottom: 0.75rem;
					line-height: 1.3;
				}

				.feature-description {
					font-size: 0.95rem;
					line-height: 1.5;
				}

				/* Improve main vehicle card on mobile */
				.main-vehicle-card {
					padding: 1.5rem;
					margin: 0 1rem;
				}

				.main-vehicle-content {
					flex-direction: column;
					gap: 1.5rem;
					text-align: center;
				}

				.vehicle-info {
					flex-direction: column;
					gap: 1rem;
				}

				.feature-icon-large {
					width: 60px;
					height: 60px;
					font-size: 24px;
				}

				.vehicle-stats {
					grid-template-columns: 1fr;
					gap: 0.75rem;
				}

				.vehicle-actions {
					align-items: center;
				}

				/* Additional mobile grid improvements */
				.section-header {
					margin-bottom: 2rem;
				}

				.section-title {
					font-size: 2rem;
				}

				.section-description {
					font-size: 1.1rem;
				}
			}

			@media (max-width: 480px) {
				.modal-content {
					margin: 1rem;
					max-width: calc(100% - 2rem);
				}

				.modal-header,
				.modal-body,
				.modal-footer {
					padding: 1rem;
				}

				.stat-number {
					font-size: 1.5rem;
				}

				.queue-number,
				.session-number {
					font-size: 1.5rem;
				}

				/* Enhanced features grid for 480px and below */
				.features-grid {
					grid-template-columns: 1fr;
					gap: 1rem;
					padding: 0 0.5rem;
				}

				.feature-card {
					padding: 1rem;
					margin-bottom: 0.75rem;
					border-radius: 12px;
					box-shadow: 0 3px 10px rgba(0, 194, 206, 0.08);
				}

				.feature-icon {
					width: 40px;
					height: 40px;
					font-size: 16px;
					margin-bottom: 0.75rem;
				}

				.feature-title {
					font-size: 1.1rem;
					margin-bottom: 0.5rem;
					line-height: 1.2;
				}

				.feature-description {
					font-size: 0.9rem;
					line-height: 1.4;
				}

				/* Improve main vehicle card for small screens */
				.main-vehicle-card {
					padding: 1rem;
					margin: 0 0.5rem;
					grid-column: 1; /* Reset span to fit single column */
				}

				.main-vehicle-content {
					flex-direction: column;
					gap: 1rem;
					text-align: center;
				}

				.vehicle-info {
					flex-direction: column;
					gap: 0.75rem;
				}

				.feature-icon-large {
					width: 50px;
					height: 50px;
					font-size: 20px;
				}

				.vehicle-stats {
					grid-template-columns: 1fr;
					gap: 0.5rem;
				}

				.vehicle-actions {
					align-items: center;
				}

				/* Additional improvements for small screens */
				.section-header {
					margin-bottom: 1.5rem;
				}

				.section-title {
					font-size: 1.75rem;
				}

				.section-description {
					font-size: 1rem;
				}

				/* Optimize dashboard hero for small screens */
				.dashboard-hero {
					padding: 40px 0;
				}

				.dashboard-greeting {
					font-size: 1.5rem;
				}

				.dashboard-actions {
					flex-direction: column;
					gap: 1rem;
				}
			}
		</style>
	</head>
	<body>
		<!-- Header -->
		<header class="header">
			<div class="container">
				<div class="header-content">
					<!-- Logo -->
					<div class="logo">
						<img src="images/logo.png" alt="Cephra" class="logo-img" />
						<span class="logo-text">CEPHRA</span>
					</div>

					<!-- Navigation -->
					<nav class="nav">
						<ul class="nav-list">
							<li><a href="#" onclick="openMonitorWeb(); return false;" class="nav-link">Monitor</a></li>
							<li><a href="link.php" class="nav-link">Link</a></li>
							<li><a href="history.php" class="nav-link">History</a></li>
							<li><a href="profile.php" class="nav-link">Profile</a></li>
						</ul>
					</nav>

					<!-- Header Actions -->
					<div class="header-actions">
						<div class="auth-buttons">
							<a href="profile_logout.php" class="nav-link auth-link">Logout</a>
						</div>
					</div>

					<!-- Mobile Menu Toggle -->
					<button class="mobile-menu-toggle" id="mobileMenuToggle">
						<span></span>
						<span></span>
						<span></span>
					</button>
				</div>
			</div>

			<!-- Mobile Menu -->
			<div class="mobile-menu" id="mobileMenu">
				<div class="mobile-menu-content">
					<ul class="mobile-nav-list">
						<li><a href="#" onclick="openMonitorWeb(); return false;" class="mobile-nav-link">Monitor</a></li>
						<li><a href="link.php" class="mobile-nav-link">Link</a></li>
						<li><a href="history.php" class="mobile-nav-link">History</a></li>
						<li><a href="profile.php" class="mobile-nav-link">Profile</a></li>
					</ul>
					<div class="mobile-header-actions">
						<a href="profile_logout.php" class="mobile-auth-link">Logout</a>
					</div>
				</div>
			</div>
		</header>

		<!-- Dashboard Hero Section -->
		<section class="dashboard-hero">
   		<div class="container">
            <div class="hero-content">
                <h1 class="hero-title">
                    <span class="hero-title-main">Cephra</span>
                    <span class="hero-title-accent">Ultimate</span>
                    <span class="hero-title-sub">Charging Platform</span>
                </h1>
                <p class="hero-description">
                    An award-winning EV charging platform trusted by 50,000+ drivers.
                    Experience the future of electric vehicle charging with intelligent,
                    fast, and reliable charging solutions.
                </p>
                <div class="hero-actions">
                    <a href="ChargingPage.php" class="btn btn-outline">Start Charging</a>
                </div>
            </div>
		</section>

		<!-- Live Status Section -->
		<section class="live-status" style="padding: 60px 0; background: white;">
			<div class="container">
				<div class="section-header">
					<h2 class="section-title">Live Status</h2>
					<p class="section-description">Real-time charging station information</p>
				</div>

				<div class="status-grid">
					<div class="status-card">
						<h4 class="status-title">System Status</h4>
						<div class="status-indicator">
							<span class="status-dot active"></span>
							<span class="status-text">All system operational</span>
						</div>
						<p class="status-description">All charging stations are currently online and available.</p>
					</div>

					<div class="status-card">
						<h4 class="status-title">Current Queue</h4>
						<div class="queue-info">
							<span class="queue-number" id="currentQueue">0</span>
							<span class="queue-label">vehicles waiting</span>
						</div>
						<p class="status-description">Estimated wait time: <strong id="waitTime">0 minutes</strong></p>
					</div>

					<div class="status-card">
						<h4 class="status-title">Active Sessions</h4>
						<div class="active-sessions">
							<span class="session-number" id="activeSessions">0</span>
							<span class="session-label">charging now</span>
						</div>
						<p class="status-description">Average session duration: <strong id="avgDuration">0 min</strong></p>
					</div>
				</div>
			</div>
		</section>

		<!-- Features Section -->
		<section class="features" style="padding: 80px 0;">
			<div class="container">
				<div class="section-header">
					<h2 class="section-title">Vehicle Status</h2>
					<p class="section-description">Monitor your electric vehicle's charging status and performance</p>
				</div>

				<div class="features-grid">
					<!-- Car Status Feature -->
					<div class="feature-card main-vehicle-card" style="background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); color: white; position: relative; overflow: hidden;">
						<div class="main-vehicle-content">
							<div class="vehicle-info">
								<div class="feature-icon-large">
									<i class="fas fa-car"></i>
								</div>
								<div class="vehicle-details">
									<h3 class="feature-title">Tesla Model 3</h3>
									<div class="vehicle-stats">
										<div class="stat-item">
											<span class="stat-label">Status</span>
											<span class="stat-value">Connected & Charging</span>
										</div>
										<div class="stat-item">
											<span class="stat-label">Range</span>
											<span class="stat-value">45 km</span>
										</div>
										<div class="stat-item">
											<span class="stat-label">Time to Full</span>
											<span class="stat-value">2h 15m</span>
										</div>
										<div class="stat-item">
											<span class="stat-label">Battery</span>
											<span class="stat-value">67%</span>
										</div>
									</div>
								</div>
							</div>
							<div class="vehicle-actions">
								<button class="quick-action-btn" onclick="showChargingOptions()">Charge Now</button>
							</div>
						</div>
						<div class="vehicle-bg-pattern"></div>
					</div>

					<!-- Battery Health Monitor -->
					<div class="feature-card">
						<div class="feature-icon">
							<i class="fas fa-battery-three-quarters"></i>
						</div>
						<h3 class="feature-title">Battery Health Monitor</h3>
						<p class="feature-description">
							<strong>Health Score:</strong> 92% (Excellent)<br>
							<strong>Degradation:</strong> 8% over 2 years<br>
							<strong>Temperature:</strong> Optimal (25°C)<br>
							<strong>Cycles:</strong> 340/1000 remaining
						</p>
						<a href="#" onclick="showBatteryHealth(); return false;" class="feature-link">View Health →</a>
					</div>

					<!-- Range Calculator -->
					<div class="feature-card">
						<div class="feature-icon">
							<i class="fas fa-route"></i>
						</div>
						<h3 class="feature-title">Range Calculator</h3>
						<p class="feature-description">
							<strong>Current Range:</strong> 45 km<br>
							<strong>Highway Range:</strong> 38 km<br>
							<strong>City Range:</strong> 52 km<br>
							<strong>Weather Impact:</strong> -5 km (rain)
						</p>
						<a href="#" onclick="showRangeCalculator(); return false;" class="feature-link">Calculate Range →</a>
					</div>

					<!-- Estimated Cost -->
					<div class="feature-card">
						<div class="feature-icon">
							<i class="fas fa-dollar-sign"></i>
						</div>
						<h3 class="feature-title">Estimated Cost</h3>
						<p class="feature-description">
							<strong>Normal Charge:</strong> ₱45.00<br>
							<strong>Fast Charge:</strong> ₱75.00<br>
							<strong>Monthly Savings:</strong> ₱1,250<br>
							<strong>Green Points:</strong> 340 earned
						</p>
						<a href="#" onclick="showEstimatedCost(); return false;" class="feature-link">View Details →</a>
					</div>

					<!-- Vehicle Diagnostics -->
					<div class="feature-card">
						<div class="feature-icon">
							<i class="fas fa-stethoscope"></i>
						</div>
						<h3 class="feature-title">Vehicle Diagnostics</h3>
						<p class="feature-description">
							<strong>System Status:</strong> All systems normal<br>
							<strong>Last Check:</strong> 2 hours ago<br>
							<strong>Alerts:</strong> None<br>
							<strong>Maintenance:</strong> Due in 1,200 km
						</p>
						<a href="#" onclick="showDiagnostics(); return false;" class="feature-link">Run Diagnostics →</a>
					</div>


				</div>
			</div>
		</section>

		<!-- Rewards and Wallet Section -->
		<section class="rewards-wallet" style="padding: 80px 0; background: #f8f9fa;">
			<div class="container">
				<div class="section-header">
					<h2 class="section-title">Rewards & Wallet</h2>
					<p class="section-description">Manage your rewards and wallet balance</p>
				</div>

				<div class="features-grid">
					<!-- Rewards Feature -->
					<div class="feature-card" style="grid-column: span 2;">
						<div class="feature-icon">
							<i class="fas fa-gift"></i>
						</div>
						<h3 class="feature-title">Cephra Rewards</h3>
						<p class="feature-description">
							Earn points on every charge and unlock exclusive benefits,
							discounts, and premium features as you charge more.
						</p>
						<a href="#" onclick="showGreenPointsPopup(); return false;" class="feature-link">View Rewards →</a>
					</div>

					<!-- Wallet Feature -->
					<div class="feature-card" style="grid-column: span 2;">
						<div class="feature-icon">
							<i class="fas fa-wallet"></i>
						</div>
						<h3 class="feature-title">Digital Wallet</h3>
						<p class="feature-description">
							Manage your payment methods, view transaction history,
							and track your spending across all charging sessions.
						</p>
						<a href="profile.php" class="feature-link">Manage Wallet →</a>
					</div>
				</div>
			</div>
		</section>

		<!-- Recent Activity Section -->
		<section class="recent-activity" style="padding: 60px 0; background: #f8f9fa;">
			<div class="container">
				<div class="section-header">
					<h2 class="section-title">Recent Activity</h2>
					<p class="section-description">Your latest charging sessions and transactions</p>
				</div>

				<div class="activity-list" id="recentActivity">
					<div class="activity-item">
						<div class="activity-icon">
							<i class="fas fa-info-circle"></i>
						</div>
						<div class="activity-content">
							<h4 class="activity-title">Welcome to your dashboard!</h4>
							<p class="activity-description">Your personalized dashboard is ready to use.</p>
							<span class="activity-time">Just now</span>
						</div>
					</div>
				</div>

				<div class="activity-actions">
					<a href="history.php" class="activity-link">View All Activity →</a>
				</div>
			</div>
		</section>

		<!-- Image-based Popup Ad -->
		<div id="greenPointsPopup" class="popup-overlay" style="display: none;">
			<div class="popup-content">
				<img src="images/pop-up.png" alt="Cephra Rewards Popup" class="popup-image" />
			</div>
			<button class="close-btn" onclick="closeGreenPointsPopup()">×</button>
		</div>

		<!-- Scripts -->
			<script src="assets/js/jquery.min.js"></script>
			<script src="assets/js/jquery.dropotron.min.js"></script>
			<script src="assets/js/browser.min.js"></script>
			<script src="assets/js/breakpoints.min.js"></script>
			<script src="assets/js/util.js"></script>
			<script src="assets/js/main.js"></script>

            <script>
                function showDialog(title, message) {
                    const overlay = document.createElement('div');
                    overlay.style.cssText = 'position:fixed;inset:0;background:rgba(0,0,0,0.6);display:flex;align-items:center;justify-content:center;z-index:10000;padding:16px;';
                    const dialog = document.createElement('div');
                    dialog.style.cssText = 'width:100%;max-width:360px;background:#fff;border-radius:12px;box-shadow:0 10px 30px rgba(0,0,0,0.25);overflow:hidden;';
                    const header = document.createElement('div');
                    header.style.cssText = 'background:#00c2ce;color:#fff;padding:12px 16px;font-weight:700';
                    header.textContent = title || 'Notice';
                    const body = document.createElement('div');
                    body.style.cssText = 'padding:16px;color:#333;line-height:1.5;';
                    body.textContent = message || '';
                    const footer = document.createElement('div');
                    footer.style.cssText = 'padding:12px 16px;display:flex;justify-content:flex-end;gap:8px;background:#f7f7f7;';
                    const ok = document.createElement('button');
                    ok.textContent = 'OK';
                    ok.style.cssText = 'background:#00c2ce;color:#fff;border:0;padding:8px 14px;border-radius:8px;cursor:pointer;';
                    ok.onclick = () => document.body.removeChild(overlay);
                    footer.appendChild(ok);
                    dialog.appendChild(header);
                    dialog.appendChild(body);
                    dialog.appendChild(footer);
                    overlay.appendChild(dialog);
                    document.body.appendChild(overlay);
                }

                // Function to show Green Points popup
                function showGreenPointsPopup() {
                    document.getElementById('greenPointsPopup').style.display = 'flex';
                }

                // Function to close Green Points popup
                function closeGreenPointsPopup() {
                    document.getElementById('greenPointsPopup').style.display = 'none';
                }

                // Show popup after page loads (with delay)
                $(document).ready(function() {
                    // Show Green Points popup after 2 seconds
                    setTimeout(function() {
                        showGreenPointsPopup();
                    }, 2000);

                    // Normal Charge Button Click Handler
                    $('#normalChargeBtn').click(function(e) {
                        e.preventDefault();
                        processChargeRequest('Normal Charging');
                    });

                    // Fast Charge Button Click Handler
                    $('#fastChargeBtn').click(function(e) {
                        e.preventDefault();
                        processChargeRequest('Fast Charging');
                    });

                    function processChargeRequest(serviceType) {
                        // Force exact service type strings expected by backend
                        let serviceTypeMapped = '';
                        if (serviceType === 'Normal Charging' || serviceType === 'normal charging') {
                            serviceTypeMapped = 'Normal Charging';
                        } else if (serviceType === 'Fast Charging' || serviceType === 'fast charging') {
                            serviceTypeMapped = 'Fast Charging';
                        } else {
                            serviceTypeMapped = serviceType; // fallback
                        }

                        // Disable buttons during processing
                        $('#normalChargeBtn, #fastChargeBtn').prop('disabled', true);

                        $.ajax({
                            url: 'charge_action.php',
                            type: 'POST',
                            data: { serviceType: serviceTypeMapped },
                            dataType: 'json',
                            success: function(response) {
                                if (response.success) {
                                    // Show queue ticket popup
                                    showQueueTicketPopup(response);
                                } else if (response.error) {
                                    showDialog('Charging', response.error);
                                }
                            },
                            error: function(xhr, status, error) {
                                showDialog('Charging', 'An error occurred while processing your request. Please try again.');
                                console.error('AJAX Error:', error);
                            },
                            complete: function() {
                                // Re-enable buttons
                                $('#normalChargeBtn, #fastChargeBtn').prop('disabled', false);
                            }
                        });
                    }

                    function showQueueTicketPopup(response) {
                        if (response.success) {
                            var ticketId = response.ticketId;
                            var serviceType = response.serviceType;
                            var batteryLevel = response.batteryLevel;

                            // Create popup HTML
                            var popupHtml = '<div id="queuePopup" style="position: fixed; top: 20%; left: 50%; transform: translate(-50%, -20%); background: white; border: 2px solid #007bff; border-radius: 10px; padding: 20px; width: 300px; z-index: 10000; box-shadow: 0 0 10px rgba(0,0,0,0.5);">';
                            popupHtml += '<h2 style="margin-top: 0; color: #007bff; text-align: center;">Your Queue Ticket</h2>';
                            popupHtml += '<div style="margin: 10px 0; font-size: 16px; text-align: center;"><strong>Ticket ID:</strong> ' + ticketId + '</div>';
                            popupHtml += '<div style="margin: 10px 0; font-size: 16px; text-align: center;"><strong>Service:</strong> ' + serviceType + '</div>';
                            popupHtml += '<div style="margin: 10px 0; font-size: 16px; text-align: center;"><strong>Battery Level:</strong> ' + batteryLevel + '%</div>';
                            popupHtml += '<div style="margin: 10px 0; font-size: 16px; text-align: center;"><strong>Estimated Wait Time:</strong> 5 minutes</div>';
                            popupHtml += '<button onclick="closePopup()" style="display: block; margin: 15px auto 0; padding: 10px 20px; background: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer;">Close</button>';
                            popupHtml += '</div>';

                            // Append to body
                            $('body').append(popupHtml);
                        }
                    }

                    // Function to close popup (defined globally)
                    window.closePopup = function() {
                        $('#queuePopup').remove();
                    };
                });

                // Function to open Monitor Web in new tab
                window.openMonitorWeb = function() {
                    const monitorUrl = '../Monitor/';
                    window.open(monitorUrl, '_blank', 'noopener,noreferrer');
                };

                // Modal Functions for New Features
                function showNearbyStations() {
                    document.getElementById('stationsModal').style.display = 'flex';
                }

                function closeStationsModal() {
                    document.getElementById('stationsModal').style.display = 'none';
                }

                function showScheduleModal() {
                    document.getElementById('scheduleModal').style.display = 'flex';
                }

                function closeScheduleModal() {
                    document.getElementById('scheduleModal').style.display = 'none';
                }

                function showSupportModal() {
                    document.getElementById('supportModal').style.display = 'flex';
                }

                function closeSupportModal() {
                    document.getElementById('supportModal').style.display = 'none';
                }

                function showEstimatedCost() {
                    showDialog('Estimated Cost', 'View detailed cost breakdown for your charging sessions:\n\n• Normal Charge: ₱45.00 per session\n• Fast Charge: ₱75.00 per session\n• Monthly Savings: ₱1,250 (based on usage)\n• Green Points: 340 earned this month\n• Total Sessions: 12 completed\n• Energy Consumed: 45.2 kWh\n\nTrack your spending and maximize your savings with our cost analysis tools.');
                }

                function submitSchedule() {
                    const form = document.getElementById('scheduleForm');
                    const formData = new FormData(form);

                    // Basic validation
                    const date = document.getElementById('scheduleDate').value;
                    const time = document.getElementById('scheduleTime').value;
                    const chargingType = document.getElementById('chargingType').value;
                    const duration = document.getElementById('estimatedDuration').value;

                    if (!date || !time || !chargingType || !duration) {
                        showDialog('Schedule Charging', 'Please fill in all required fields.');
                        return;
                    }

                    // Show loading dialog
                    showDialog('Schedule Charging', 'Scheduling your charging session...');

                    // Simulate API call (replace with actual endpoint)
                    setTimeout(() => {
                        closeScheduleModal();
                        showDialog('Schedule Charging', 'Your charging session has been scheduled successfully! You will receive a notification when your slot becomes available.');
                    }, 2000);
                }

                function showFAQ() {
                    showDialog('FAQ', 'Frequently Asked Questions:\n\n1. How do I start charging?\n   - Click on "Start Charging" and select your preferred charging type.\n\n2. How do I view my charging history?\n   - Navigate to the "History" section in the navigation menu.\n\n3. How do I update my profile?\n   - Go to "Profile" in the navigation menu to manage your settings.\n\n4. What are Green Points?\n   - Green Points are rewards earned for using our charging stations.\n\nFor more questions, please contact our support team.');
                }

                function contactSupport() {
                    showDialog('Contact Support', 'You can reach our support team through:\n\n📞 Phone: +63 (2) 123-4567\n📧 Email: support@cephra.com\n💬 Live Chat: Available 24/7\n\nOur support team is available Monday to Sunday, 6:00 AM to 10:00 PM.');
                }

                function reportIssue() {
                    showDialog('Report Issue', 'To report a technical issue:\n\n1. Describe the problem in detail\n2. Include any error messages\n3. Mention your device and browser\n4. Note the time when the issue occurred\n\nPlease contact our technical support team at:\n📞 Phone: +63 (2) 123-4567\n📧 Email: techsupport@cephra.com\n\nWe appreciate your feedback and will resolve the issue as quickly as possible.');
                }

                function navigateToStation(stationName) {
                    // Check if geolocation is available
                    if (navigator.geolocation) {
                        navigator.geolocation.getCurrentPosition(
                            function(position) {
                                const lat = position.coords.latitude;
                                const lng = position.coords.longitude;

                                // Use Google Maps or Waze for navigation
                                const mapsUrl = `https://www.google.com/maps/dir/${lat},${lng}/${encodeURIComponent(stationName)}`;
                                window.open(mapsUrl, '_blank', 'noopener,noreferrer');

                                showDialog('Navigation', `Opening navigation to ${stationName}...`);
                            },
                            function(error) {
                                // Fallback if geolocation fails
                                const mapsUrl = `https://www.google.com/maps/search/${encodeURIComponent(stationName + ' charging station')}`;
                                window.open(mapsUrl, '_blank', 'noopener,noreferrer');
                                showDialog('Navigation', 'Opening map directions. Please enable location services for precise navigation.');
                            }
                        );
                    } else {
                        // Fallback for browsers without geolocation
                        const mapsUrl = `https://www.google.com/maps/search/${encodeURIComponent(stationName + ' charging station')}`;
                        window.open(mapsUrl, '_blank', 'noopener,noreferrer');
                        showDialog('Navigation', 'Opening map directions. Please enable location services for precise navigation.');
                    }
                }

                // Load dashboard statistics
                function loadDashboardStats() {
                    // Simulate loading stats from API
                    setTimeout(() => {
                        document.getElementById('totalSessions').textContent = '12';
                        document.getElementById('energyConsumed').textContent = '45.2 kWh';
                        document.getElementById('totalSavings').textContent = '₱1,250';
                        document.getElementById('greenPoints').textContent = '340';
                        document.getElementById('currentQueue').textContent = '3';
                        document.getElementById('waitTime').textContent = '8 minutes';
                        document.getElementById('activeSessions').textContent = '7';
                        document.getElementById('avgDuration').textContent = '45 min';
                    }, 1000);
                }

                // Update live status every 30 seconds
                function updateLiveStatus() {
                    setInterval(() => {
                        // Simulate real-time updates
                        const queue = Math.floor(Math.random() * 10);
                        const active = Math.floor(Math.random() * 15) + 5;
                        const waitTime = Math.floor(Math.random() * 20) + 5;

                        document.getElementById('currentQueue').textContent = queue;
                        document.getElementById('activeSessions').textContent = active;
                        document.getElementById('waitTime').textContent = `${waitTime} minutes`;
                    }, 30000);
                }

                // Mobile Menu Toggle Functionality
                function initMobileMenu() {
                    const mobileMenuToggle = document.getElementById('mobileMenuToggle');
                    const mobileMenu = document.getElementById('mobileMenu');
                    const mobileMenuOverlay = document.createElement('div');
                    mobileMenuOverlay.className = 'mobile-menu-overlay';
                    mobileMenuOverlay.id = 'mobileMenuOverlay';
                    document.body.appendChild(mobileMenuOverlay);

                    // Toggle mobile menu
                    function toggleMobileMenu() {
                        const isActive = mobileMenu.classList.contains('active');

                        if (isActive) {
                            closeMobileMenu();
                        } else {
                            openMobileMenu();
                        }
                    }

                    // Open mobile menu
                    function openMobileMenu() {
                        mobileMenu.classList.add('active');
                        mobileMenuToggle.classList.add('active');
                        mobileMenuOverlay.classList.add('active');
                        document.body.style.overflow = 'hidden';

                        // Add click handlers
                        mobileMenuOverlay.addEventListener('click', closeMobileMenu);
                        document.addEventListener('keydown', handleEscapeKey);
                    }

                    // Close mobile menu
                    function closeMobileMenu() {
                        mobileMenu.classList.remove('active');
                        mobileMenuToggle.classList.remove('active');
                        mobileMenuOverlay.classList.remove('active');
                        document.body.style.overflow = '';

                        // Remove event listeners
                        mobileMenuOverlay.removeEventListener('click', closeMobileMenu);
                        document.removeEventListener('keydown', handleEscapeKey);
                    }

                    // Handle escape key
                    function handleEscapeKey(e) {
                        if (e.key === 'Escape') {
                            closeMobileMenu();
                        }
                    }

                    // Add click handler to toggle button
                    mobileMenuToggle.addEventListener('click', toggleMobileMenu);

                    // Add click handlers to mobile menu links
                    const mobileNavLinks = document.querySelectorAll('.mobile-nav-link');
                    mobileNavLinks.forEach(link => {
                        link.addEventListener('click', closeMobileMenu);
                    });

                    // Close menu when clicking outside on mobile
                    $(document).on('click', function(e) {
                        if (window.innerWidth <= 768) {
                            if (!mobileMenu.contains(e.target) && !mobileMenuToggle.contains(e.target)) {
                                if (mobileMenu.classList.contains('active')) {
                                    closeMobileMenu();
                                }
                            }
                        }
                    });
                }

                // Initialize dashboard features
                $(document).ready(function() {
                    loadDashboardStats();
                    updateLiveStatus();
                    initMobileMenu(); // Initialize mobile menu functionality

                    // Intersection Observer for animations
                    const observerOptions = {
                        threshold: 0.1,
                        rootMargin: '0px 0px -50px 0px'
                    };

                    const observer = new IntersectionObserver((entries) => {
                        entries.forEach(entry => {
                            if (entry.isIntersecting) {
                                entry.target.classList.add('animate-in');
                            }
                        });
                    }, observerOptions);

                    // Observe all feature cards and sections
                    document.querySelectorAll('.feature-card, .status-card, .stat-card, .promo-card, .charging-card').forEach(el => {
                        observer.observe(el);
                    });

                    // Add click handlers for modal triggers
                    $(document).on('click', function(e) {
                        // Close modals when clicking outside
                        if (e.target.classList.contains('modal-overlay')) {
                            closeStationsModal();
                            closeScheduleModal();
                            closeSupportModal();
                        }
                    });

                    // Add keyboard support for modals
                    $(document).on('keydown', function(e) {
                        if (e.key === 'Escape') {
                            closeStationsModal();
                            closeScheduleModal();
                            closeSupportModal();
                        }
                    });
                });
            </script>

		<!-- Footer -->
		<footer class="footer">
			<div class="container">
				<div class="footer-content">
					<div class="footer-section">
						<div class="footer-logo">
							<img src="images/logo.png" alt="Cephra" class="footer-logo-img" />
							<span class="footer-logo-text">CEPHRA</span>
						</div>
						<p class="footer-description">
							Your ultimate electric vehicle charging platform,
							powering the future of sustainable transportation.
						</p>
					</div>

					<div class="footer-section">
						<h4 class="footer-title">Platform</h4>
						<ul class="footer-links">
							<li><a href="dashboard.php">Dashboard</a></li>
							<li><a href="ChargingPage.php">Charging</a></li>
							<li><a href="history.php">History</a></li>
						</ul>
					</div>

					<div class="footer-section">
						<h4 class="footer-title">Support</h4>
						<ul class="footer-links">
							<li><a href="#" onclick="showSupportModal(); return false;">Help Center</a></li>
							<li><a href="profile.php">Contact Us</a></li>
						</ul>
					</div>

					<div class="footer-section">
						<h4 class="footer-title">Company</h4>
						<ul class="footer-links">
							<li><a href="profile.php">About Us</a></li>
							<li><a href="profile.php">Our Team</a></li>
						</ul>
					</div>
				</div>

				<div class="footer-bottom">
					<p>&copy; 2025 Cephra. All rights reserved. | <a href="#privacy">Privacy Policy</a> | <a href="#terms">Terms of Service</a></p>
				</div>
			</div>
		</footer>

		</body>
	</html>
