<?php
// Simple live monitor page for Cephra
?>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Cephra Monitor</title>
    <link rel="manifest" href="monitor.webmanifest" />
    <link rel="icon" type="image/png" href="../Admin/images/MONU.png" />
    <link rel="apple-touch-icon" sizes="180x180" href="../Admin/images/MONU.png" />
    <link rel="apple-touch-icon" sizes="152x152" href="../Admin/images/MONU.png" />
    <link rel="apple-touch-icon" sizes="167x167" href="../Admin/images/MONU.png" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
    <meta name="apple-mobile-web-app-title" content="Monitor" />
    <meta name="theme-color" content="#0b1e29" />
    <style>
        :root {
            /* Dark Mode - Modern Scheme */
            --bg: linear-gradient(135deg, #0a0a0a 0%, #1e1e1e 100%);
            --panel: linear-gradient(135deg, #1e1e1e 0%, #2a2a2a 100%);
            --card: linear-gradient(135deg, #2a2a2a 0%, #3a3a3a 100%);
            --text: #ffffff;
            --muted: #b0b0b0;
            --accent: #0099aa;
            --accent-hover: #0099aa;
            --avail: #00b8cca5;
            --availText: #000000;
            --occ: #ff4444;
            --occText: #ffffff;
            --maint: #ffaa00;
            --maintText: #000000;
            --border: rgba(255,255,255,0.15);
            --shadow-sm: 0 2px 8px rgba(0,0,0,0.5);
            --shadow-md: 0 4px 12px rgba(0,0,0,0.6);
            --shadow-lg: 0 8px 24px rgba(0,0,0,0.7);
            --transition-fast: all 0.2s ease;
            --transition-normal: all 0.3s ease;
        }
        .light {
            /* Light Mode - Updated to match app color scheme */
            --bg: linear-gradient(135deg, #f7fafc 0%, #edf2f7 100%);
            --panel: linear-gradient(135deg, #ffffff 0%, #f7fafc 100%);
            --card: linear-gradient(135deg, #edf2f7 0%, #e2e8f0 100%);
            --text: #2d3748;
            --muted: #718096;
            --accent: #00c2ce;
            --accent-hover: #00e1ee;
            --avail: #38a169;
            --availText: #ffffff;
            --occ: #e53e3e;
            --occText: #ffffff;
            --maint: #dd6b20;
            --maintText: #ffffff;
            --border: rgba(0,0,0,0.1);
            --shadow-sm: 0 2px 8px rgba(0,0,0,0.1);
            --shadow-md: 0 4px 12px rgba(0,0,0,0.15);
            --shadow-lg: 0 8px 24px rgba(0,0,0,0.2);
        }
        .dark-fullscreen {
            /* Enhanced Dark Mode for Fullscreen */
            --bg: linear-gradient(135deg, #0a0e13 0%, #1a202c 100%);
            --panel: linear-gradient(135deg, #1a202c 0%, #2d3748 100%);
            --card: linear-gradient(135deg, #2d3748 0%, #4a5568 100%);
            --text: #ffffff;
            --muted: #a0aec0;
            --accent: #00ffff;
            --accent-hover: #00e6ff;
            --avail: #48bb78;
            --availText: #ffffff;
            --occ: #f56565;
            --occText: #ffffff;
            --maint: #ed8936;
            --maintText: #ffffff;
            --border: rgba(255,255,255,0.15);
            --shadow-sm: 0 2px 8px rgba(0,0,0,0.4);
            --shadow-md: 0 4px 12px rgba(0,0,0,0.5);
            --shadow-lg: 0 8px 24px rgba(0,0,0,0.6);
        }
        body { 
            font-family: 'Segoe UI', Arial, sans-serif; 
            margin: 0; 
            padding: 15px; 
            background:var(--bg); 
            color:var(--text); 
            transition: var(--transition-normal); 
            line-height: 1.5;
        }
        h1 { 
            margin: 0 0 15px; 
            font-size: 24px; 
            display:flex; 
            align-items:center; 
            gap:10px; 
            flex-wrap: wrap; 
            font-weight: 600;
        }
        .logo { 
            width:36px; 
            height:36px; 
            border-radius:6px; 
            overflow:hidden; 
            display:inline-block; 
            background:transparent; 
            box-shadow: var(--shadow-sm);
        }
        .logo img { 
            width:100%; 
            height:100%; 
            object-fit:contain; 
            object-position:center; 
            display:block; 
            background:transparent; 
            transition: var(--transition-fast);
        }
        .logo:hover img { transform: scale(1.05); }
        .grid { 
            display: grid; 
            grid-template-columns: repeat(4, 1fr); 
            grid-auto-rows: 1fr; /* keep cards consistent height */
            gap: 18px; 
            align-items: start;
        }
        .bay { 
            background: var(--card);
            border-radius: 12px; 
        padding: 18px 14px; /* extra top space for header/pill */
            border: 1px solid var(--border);
            min-width: 0; 
            height: 200px; 
            display: flex; 
            flex-direction: column; 
            justify-content: space-between; 
            box-shadow: var(--shadow-md); 
            transition: transform 0.22s ease, box-shadow 0.22s ease;
            position: relative;
            overflow: hidden;
            background-image: linear-gradient(135deg, rgba(255,255,255,0.04) 0%, rgba(255,255,255,0.02) 100%);
        }
        .bay:hover { 
            transform: translateY(-5px); 
            transition: transform 0.22s ease, box-shadow 0.22s ease, background 0.22s ease;
        }
        .bay::after {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 4px;
            background: var(--accent);
            opacity: 0.85;
            transform-origin: left center;
        }
        .bay-header {
            display:flex;
            align-items:center;
            justify-content:space-between;
            gap:12px;
            margin-bottom:8px;
        }
        .bay-type {
            font-size:12px;
            color:var(--muted);
            background:transparent;
            padding:4px 8px;
            border-radius:8px;
            border:1px solid rgba(0,0,0,0.04);
        }
    .bay-status { position:absolute; right:12px; top:10px; z-index:6; }

        .bay h3 {
            margin: 0;
            font-size: 18px;
            color: var(--accent);
            font-weight: 600;
            letter-spacing: 0.3px;
        }

        .bay-center-status {
            display:flex;
            align-items:center;
            justify-content:center;
            font-size:28px;
            font-weight:700;
            color:var(--text);
        margin: 18px 0 8px; /* push down so it doesn't collide with the top-right pill */
        padding-top: 4px;
            text-transform: capitalize;
        }

        .info-row {
            display:flex;
            flex-direction:column;
            gap:4px;
            font-size:13px;
            align-items:flex-end;
        }

        .badge {
            display:inline-block;
            padding:6px 12px;
            border-radius: 999px;
            font-size: 13px;
            font-weight: 600;
            box-shadow: var(--shadow-sm);
            transition: transform 0.16s ease, box-shadow 0.16s ease, opacity 0.16s ease;
        }

        .bay.available { box-shadow: 0 6px 20px rgba(56,161,105,0.08); }
        .bay.occupied { box-shadow: 0 6px 20px rgba(229,62,62,0.08); }
        .bay.maintenance { box-shadow: 0 6px 20px rgba(221,107,32,0.08); }

        .bay::after {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 4px;
            background: var(--accent);
            opacity: 0.85;
            transform-origin: left center;
        }
        .bay.available::after { background: var(--avail); }
        .bay.occupied::after { background: var(--occ); }
        .bay.maintenance::after { background: var(--maint); }

        .maintenance {
            background:var(--maint);
            color:var(--maintText);
        }
        .row { 
            margin-top: 20px; 
            display:flex; 
            gap:20px; 
            align-items:flex-start; 
            flex-wrap: wrap; 
        }
        .panel { 
            background: var(--panel);
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 16px;
            padding: 20px;
            flex: 1 1 320px;
            min-width: 0; 
            box-shadow: var(--shadow-md);
            background-image: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%);
        }
        table { 
            width:100%; 
            border-collapse: collapse; 
        }
        th, td { 
            text-align:left; 
            padding:10px; 
            border-bottom:1px solid rgba(255,255,255,0.08); 
            font-size: 14px; 
        }
        th { 
            color:var(--accent); 
            font-weight: 600;
        }
        .muted { 
            color:var(--muted); 
            font-size: 13px; 
        }
        .ts { 
            font-size: 13px; 
            color:var(--muted); 
            margin-left:10px; 
            font-weight: normal;
        }
        .toolbar { 
            margin-left:auto; 
            display:flex; 
            gap:12px; 
            align-items:center; 
            flex-wrap: wrap;
        }
        /* Burger button (hidden by default, visible on small screens) */
        .burger {
            display:none;
            margin-left:auto;
            width:36px; height:28px;
            border:1px solid rgba(255,255,255,0.3);
            border-radius:8px;
            background:transparent;
            align-items:center; justify-content:center;
            cursor:pointer;
        }
        .burger span {
            display:block; width:18px; height:2px; background:var(--text);
            position:relative;
        }
        .burger span::before, .burger span::after {
            content:""; position:absolute; left:0; width:18px; height:2px; background:var(--text);
        }
        .burger span::before { top:-6px; }
        .burger span::after  { top: 6px; }
        .btn {
            background:transparent;
            color:var(--text);
            border:1px solid rgba(255,255,255,0.3);
            border-radius:10px;
            padding:8px 14px;
            cursor:pointer;
            font-size:13px;
            font-weight: 500;
            transition: var(--transition-fast);
        }
        .light .btn {
            border-color: rgba(0,0,0,0.3);
        }
        .btn:hover {
            background: var(--accent);
            color: var(--bg);
            border-color: var(--accent);
        }

        /* Language select dropdown styling */
        .language-select {
            background: transparent !important;
            color: #f7fafc !important;
            border: 2px solid #4a5568 !important;
            border-radius: 8px !important;
            padding: 8px 12px !important;
            cursor: pointer !important;
            font-size: 13px !important;
            font-weight: 500 !important;
            transition: var(--transition-fast) !important;
            min-width: 60px !important;
            appearance: none !important;
            background-image: url("data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='%23f7fafc' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3e%3cpolyline points='6,9 12,15 18,9'%3e%3c/polyline%3e%3c/svg%3e") !important;
            background-repeat: no-repeat !important;
            background-position: right 8px center !important;
            background-size: 12px !important;
            padding-right: 30px !important;
        }

        .light .language-select {
            background: #ffffff !important;
            color: #2d3748 !important;
            border-color: #e2e8f0 !important;
            background-image: url("data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='%232d3748' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3e%3cpolyline points='6,9 12,15 18,9'%3e%3c/polyline%3e%3c/svg%3e") !important;
            background-repeat: no-repeat !important;
            background-position: right 8px center !important;
            background-size: 12px !important;
            padding-right: 30px !important;
        }

        .language-select:hover {
            border-color: var(--accent) !important;
            background: #4a5568 !important;
        }

        .light .language-select:hover {
            background: #f7fafc !important;
            border-color: #00c2ce !important;
        }

        .language-select:focus {
            outline: none !important;
            border-color: var(--accent) !important;
            box-shadow: 0 0 0 2px rgba(0, 212, 170, 0.3) !important;
        }

        .light .language-select:focus {
            box-shadow: 0 0 0 2px rgba(0, 194, 206, 0.3) !important;
        }

        .language-select option {
            background: transparent !important;
            color: #f7fafc !important;
            padding: 8px !important;
        }

        .light .language-select option {
            background: #ffffff !important;
            color: #2d3748 !important;
        }
        .toolbar label { 
            display:flex; 
            align-items:center; 
            gap:8px; 
            cursor:pointer; 
            font-size:13px; 
            max-width: 100%;
            white-space: nowrap;
            overflow: visible;
            padding: 4px 6px;
            border-radius: 8px;
        }
        .toolbar label input[type="checkbox"] {
            margin:0 8px 0 0;
            accent-color: var(--accent);
        }

        /* Custom checkbox styling */
        .toolbar label input[type="checkbox"] {
            appearance: none;
            width: 18px;
            height: 18px;
            border: 2px solid var(--border);
            border-radius: 4px;
            background: var(--card);
            cursor: pointer;
            position: relative;
            transition: var(--transition-fast);
        }

        .toolbar label input[type="checkbox"]:checked {
            background: var(--accent);
            border-color: var(--accent);
        }

        .toolbar label input[type="checkbox"]:checked::after {
            content: '✓';
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            color: var(--bg);
            font-size: 12px;
            font-weight: bold;
        }

        .toolbar label input[type="checkbox"]:hover {
            border-color: var(--accent);
        }

        /* Custom range slider styling */
        input[type="range"] {
            appearance: none;
            width: 60px;
            height: 6px;
            background: var(--border);
            border-radius: 3px;
            outline: none;
            cursor: pointer;
            transition: var(--transition-fast);
        }

        input[type="range"]::-webkit-slider-thumb {
            appearance: none;
            width: 16px;
            height: 16px;
            background: var(--accent);
            border-radius: 50%;
            cursor: pointer;
            box-shadow: 0 2px 4px rgba(0,0,0,0.2);
            transition: var(--transition-fast);
        }

        input[type="range"]::-webkit-slider-thumb:hover {
            transform: scale(1.1);
            box-shadow: 0 4px 8px rgba(0,0,0,0.3);
        }

        input[type="range"]::-moz-range-thumb {
            width: 16px;
            height: 16px;
            background: var(--accent);
            border-radius: 50%;
            cursor: pointer;
            border: none;
            box-shadow: 0 2px 4px rgba(0,0,0,0.2);
            transition: var(--transition-fast);
        }

        input[type="range"]::-moz-range-thumb:hover {
            transform: scale(1.1);
            box-shadow: 0 4px 8px rgba(0,0,0,0.3);
        }

        input[type="range"]:hover {
            background: var(--muted);
        }
        .pager { 
            display:flex; 
            gap:8px; 
            align-items:center; 
            margin-top:12px; 
            flex-wrap: wrap; 
            justify-content: center;
        }
        
        /* Announcement toast styling */
        .announcement-toast {
            position: fixed;
            bottom: 30px;
            left: 50%;
            transform: translateX(-50%) translateY(100px);
            background: var(--accent);
            color: var(--bg);
            padding: 15px 25px;
            border-radius: 10px;
            font-size: 18px;
            font-weight: 600;
            box-shadow: var(--shadow-lg);
            z-index: 1000;
            opacity: 0;
            transition: transform 0.4s ease, opacity 0.4s ease;
            text-align: center;
            max-width: 95%;
            border: 2px solid var(--bg);
            white-space: normal;
            word-break: break-word;
            overflow-wrap: anywhere;
        }
        
        .announcement-toast.show {
            transform: translateX(-50%) translateY(0);
            opacity: 1;
        }
        .pager .btn { padding:4px 8px; }
        
        /* Fullscreen Mode - Completely Different UI */
        .fullscreen-mode { 
            position: fixed; 
            top: 0; 
            left: 0; 
            width: 100vw; 
            height: 100vh; 
            z-index: 1000; 
            background: linear-gradient(135deg, #0a0a0a 0%, #1a1a1a 100%);
            padding: 0;
            margin: 0;
            overflow: hidden;
            box-sizing: border-box;
            color: #ffffff;
            font-family: 'Segoe UI', Arial, sans-serif;
        }
        
        /* Hide default elements in fullscreen */
        .fullscreen-mode h1,
        .fullscreen-mode .toolbar,
        .fullscreen-mode .row {
            display: none !important;
        }
        
        /* Fullscreen Header */
        .fullscreen-header {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 60px;
            background: linear-gradient(90deg, #1a1a1a 0%, #2a2a2a 100%);
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 30px;
            border-bottom: 2px solid #00d4aa;
            z-index: 10;
        }
        
        .fullscreen-title {
            display: flex;
            align-items: center;
            gap: 15px;
            color: #ffffff;
            font-size: 24px;
            font-weight: 600;
        }
        
        .fullscreen-logo {
            width: 40px;
            height: 40px;
            border-radius: 8px;
            background: #00d4aa;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #000;
            font-weight: bold;
            font-size: 18px;
        }
        
        .fullscreen-controls {
            display: flex;
            gap: 15px;
            align-items: center;
        }
        
        .fullscreen-btn {
            background: #00d4aa;
            color: #000;
            border: none;
            padding: 8px 16px;
            border-radius: 6px;
            font-size: 12px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .fullscreen-btn:hover {
            background: #00e6b8;
            transform: translateY(-1px);
        }
        
        .fullscreen-btn.exit {
            background: #ff6b6b;
            color: #fff;
        }
        
        .fullscreen-btn.exit:hover {
            background: #ff5252;
        }
        
        /* Fullscreen Grid - Different Layout */
        .fullscreen-grid {
            position: absolute;
            top: 60px;
            left: 0;
            right: 0;
            bottom: 0;
            display: grid;
            grid-template-columns: repeat(4, 1fr); 
            grid-template-rows: repeat(2, 1fr); 
            gap: 20px;
            padding: 20px;
            background: linear-gradient(135deg, #0a0a0a 0%, #1a1a1a 100%);
            .badge {
                display:inline-block;
                padding:4px 10px; /* slightly smaller pill to avoid collisions */
                border-radius: 999px;
                font-size: 12px;
                font-weight: 600;
                box-shadow: var(--shadow-sm);
                transition: transform 0.16s ease, box-shadow 0.16s ease, opacity 0.16s ease;
            }
            justify-content: space-between;
            border: 2px solid transparent;
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
        }
        
        .fullscreen-bay::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: #00d4aa;
            transform: scaleX(0);
            transition: transform 0.3s ease;
        }
        
        .fullscreen-bay.occupied::before {
            background: #ff6b6b;
            transform: scaleX(1);
        }
        
        .fullscreen-bay.available::before {
            background: #48bb78;
            .bay-info {
                margin-top: 8px;
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 8px 12px;
                align-items: start;
                /* ensure details are visually lower-right */
                position: absolute;
                right: 12px;
                bottom: 12px;
                width: 42%; /* slightly narrower to avoid overlapping center status */
                background: transparent;
                text-align: right;
            }
        }
        
        .fullscreen-bay-title {
            color: #ffffff;
            font-size: 18px; 
            font-weight: 600;
            margin: 0;
        }
        
        .fullscreen-bay-type {
            color: #a0aec0;
            font-size: 12px;
            background: rgba(160, 174, 192, 0.1);
            padding: 4px 8px;
            border-radius: 4px;
        }
        .fullscreen-bay-type.fast-bay {
            font-weight: bold;
            color: var(--accent);
        }
        
        .fullscreen-bay-status {
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 15px 0;
        }
        
        .fullscreen-status-badge {
            padding: 12px 20px;
            border-radius: 25px;
            font-size: 14px; 
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        
        .fullscreen-status-badge.available {
            background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
            color: #ffffff;
            box-shadow: 0 4px 15px rgba(72, 187, 120, 0.4);
        }
        
        .fullscreen-status-badge.occupied {
            background: linear-gradient(135deg, #ff6b6b 0%, #e53e3e 100%);
            color: #ffffff;
            box-shadow: 0 4px 15px rgba(255, 107, 107, 0.4);
        }
        
        .fullscreen-bay-details {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }
        
        .fullscreen-detail-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 8px 0;
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }
        
        .fullscreen-detail-item:last-child {
            border-bottom: none;
        }
        
        .fullscreen-detail-label {
            color: #a0aec0;
            font-size: 12px; 
            font-weight: 500;
        }
        
        .fullscreen-detail-value {
            color: #ffffff;
            font-size: 14px;
            font-weight: 600;
        }
        
        /* Fullscreen Responsive */
        @media (max-width: 1200px) {
            .fullscreen-grid {
                grid-template-columns: repeat(3, 1fr);
                grid-template-rows: repeat(3, 1fr);
            }
        }
        
        @media (max-width: 900px) {
            .fullscreen-grid {
                grid-template-columns: repeat(2, 1fr);
                grid-template-rows: repeat(4, 1fr);
            }
        }
        
        @media (max-width: 600px) {
            .fullscreen-grid {
                grid-template-columns: 1fr;
                grid-template-rows: repeat(8, 1fr);
            }
        }
        
        /* Fullscreen responsive styles */
        @media (max-width: 480px) {
            .fullscreen-mode { padding: 10px; }
            .fullscreen-mode h1 { font-size: 16px; margin-bottom: 8px; }
            .fullscreen-mode .toolbar .btn { padding: 3px 6px; font-size: 10px; }
            .fullscreen-mode .toolbar label { 
                display: none;
            }
            .fullscreen-mode .grid { 
                grid-template-columns: repeat(2, 1fr); 
                grid-template-rows: repeat(4, 1fr);
                gap: 20px; 
                padding: 20px;
                height: calc(100vh - 60px);
            }
            .fullscreen-mode .bay { 
                padding: 15px; 
                font-size: 14px;
                margin: 0;
                border-radius: 12px;
            }
            .fullscreen-mode .bay h3 { font-size: 16px; margin-bottom: 8px; }
            .fullscreen-mode .badge { padding: 8px 12px; font-size: 12px; }
            .fullscreen-mode .available { font-size: 14px; padding: 10px 14px; }
            .fullscreen-mode .muted { font-size: 10px; margin-top: 8px; }
        }
        
        @media (min-width: 481px) and (max-width: 768px) {
            .fullscreen-mode .grid { 
                grid-template-columns: repeat(3, 1fr); 
                grid-template-rows: repeat(3, 1fr);
                gap: 25px;
                padding: 22px;
                height: calc(100vh - 70px);
            }
            .fullscreen-mode .bay { 
                font-size: 15px;
                padding: 18px;
                margin: 0;
                border-radius: 14px;
            }
        }
        
        @media (min-width: 769px) and (max-width: 1024px) {
            .fullscreen-mode .grid { 
                grid-template-columns: repeat(4, 1fr); 
                grid-template-rows: repeat(2, 1fr);
                gap: 28px;
                padding: 24px;
                height: calc(100vh - 75px);
            }
            .fullscreen-mode .bay { 
                font-size: 16px;
                padding: 20px;
                margin: 0;
                border-radius: 15px;
            }
        }
        
        @media (min-width: 1025px) {
            .fullscreen-mode .grid { 
                grid-template-columns: repeat(4, 1fr); 
                grid-template-rows: repeat(2, 1fr);
                gap: 30px;
                padding: 25px;
                height: calc(100vh - 80px);
            }
            .fullscreen-mode .bay { 
                font-size: 16px;
                padding: 20px;
                margin: 0;
                border-radius: 15px;
            }
        }
        
        /* Large screens - full expansion */
        @media (min-width: 1400px) {
            .fullscreen-mode .grid { 
                grid-template-columns: repeat(4, 1fr); 
                grid-template-rows: repeat(2, 1fr);
                gap: 35px;
                padding: 30px;
                height: calc(100vh - 80px);
            }
            .fullscreen-mode .bay { 
                font-size: 18px;
                padding: 25px;
                margin: 0;
                border-radius: 18px;
            }
            .fullscreen-mode .bay h3 { font-size: 22px; margin-bottom: 12px; }
            .fullscreen-mode .badge { padding: 12px 18px; font-size: 16px; }
            .fullscreen-mode .available { font-size: 20px; padding: 14px 20px; }
        }
        
        /* Improved responsive design for different screen sizes */
        @media (max-width: 640px) {
            body { padding: 8px; }
            h1 { font-size: 18px; margin-bottom: 10px; }
            .burger { display:flex; }
            .toolbar { display:none; width:100%; margin-top:8px; }
            .toolbar.show { display:flex; flex-direction: column; align-items: stretch; }
            .toolbar .btn, .toolbar label { width: 100%; }
            .grid { gap: 12px; grid-template-columns: 1fr; grid-template-rows: repeat(auto-fit, minmax(170px, auto)); }
            .bay { padding: 12px; height: 170px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); transition: transform 0.2s; }
            .bay:active { transform: scale(0.98); }
            .bay h3 { font-size: 16px; margin-bottom: 10px; }
            .available { font-size: 14px; font-weight: bold; padding: 6px 10px; }
            .panel { padding: 10px; }
            th, td { font-size: 12px; padding: 6px; }
            .pager { justify-content: center; }
        }
        @media (min-width: 421px) and (max-width: 640px) {
            body { padding: 10px; }
            h1 { font-size: 20px; }
            .toolbar { flex-wrap: wrap; }
            .grid { gap: 15px; grid-template-columns: repeat(2, 1fr); grid-template-rows: repeat(auto-fit, minmax(180px, auto)); }
            .bay { padding: 15px; height: 180px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); transition: transform 0.2s; }
            .bay:hover { transform: translateY(-3px); }
            .available { font-size: 15px; font-weight: bold; padding: 7px 11px; }
            .panel { padding: 12px; }
        }
        @media (min-width: 641px) and (max-width: 1024px) {
            body { padding: 12px; }
            .grid { gap: 18px; grid-template-columns: repeat(3, 1fr); grid-template-rows: repeat(auto-fit, minmax(200px, auto)); }
            .bay { padding: 16px; height: 200px; box-shadow: 0 3px 8px rgba(0,0,0,0.12); transition: transform 0.2s, box-shadow 0.2s; }
            .bay:hover { transform: translateY(-5px); box-shadow: 0 5px 15px rgba(0,0,0,0.15); }
            .available { font-size: 16px; font-weight: bold; padding: 8px 12px; }
        }
        @media (min-width: 1025px) {
            body { padding: 15px; }
            .grid { gap: 20px; grid-template-columns: repeat(4, 1fr); grid-template-rows: repeat(auto-fit, minmax(200px, auto)); }
            .bay { padding: 18px; height: 200px; box-shadow: 0 3px 10px rgba(0,0,0,0.15); transition: transform 0.2s, box-shadow 0.2s; }
            .bay:hover { transform: translateY(-5px); box-shadow: 0 8px 20px rgba(0,0,0,0.18); }
            .available { font-size: 16px; font-weight: bold; padding: 8px 12px; }
        }
    </style>
</head>
<body class="light">
    <h1>
        
        Cephra Live Monitor <span id="ts" class="ts"></span>
        <button class="burger" id="burgerBtn" aria-label="Menu"><span></span></button>
        <div class="toolbar" id="toolbarWrap">
            <label class="muted" style="white-space:nowrap;"><input type="checkbox" id="announcerChk" checked /> Bay Announcer</label>
            <label class="muted" style="white-space:nowrap; margin-left: 10px;">
                Volume: <input type="range" id="volumeSlider" min="0" max="100" value="80" style="width: 60px; margin: 0 5px;" />
            </label>
            <label class="muted" style="white-space:nowrap;">
                Speed: <input type="range" id="speedSlider" min="50" max="200" value="90" style="width: 60px; margin: 0 5px;" />
            </label>
            <button class="btn" id="testTTSBtn">Test TTS</button>
            <button class="btn" id="fullscreenBtn">Fullscreen</button>
            <button class="btn" id="themeBtn">Toggle Theme</button>
            <select class="btn language-select" id="languageBtn">
                <option value="EN">EN</option>
                <option value="ZH">ZH</option>
                <option value="FIL">FIL</option>
                <option value="CEB">CEB</option>
                <option value="ES">ES</option>
            </select>
        </div>
    </h1>

    <div class="grid" id="bays"></div>

    <div class="row">
        <div class="panel">
            <h3 style="margin:0 0 8px;color:var(--accent);">Waiting Queue</h3>
            <table>
                <thead>
                    <tr>
                        <th>Ticket</th>
                        <th>User</th>
                        <th>Service</th>
                        <th>Since</th>
                    </tr>
                </thead>
                <tbody id="queue"></tbody>
            </table>
            <div class="pager">
                <button class="btn" id="prevPage">Prev</button>
                <span class="muted" id="pageInfo">Page 1</span>
                <button class="btn" id="nextPage">Next</button>
                <span class="muted">Page size: 10</span>
            </div>
            
        </div>
    </div>


    <script>
        // Simplified header: only Bay Announcer toggle retained
        
        // Language support
        const languages = {
            'EN': { name: 'English', code: 'en' },
            'ZH': { name: '中文 (Chinese)', code: 'zh' },
            'FIL': { name: 'Filipino', code: 'fil' },
            'CEB': { name: 'Bisaya', code: 'ceb' },
            'ES': { name: 'Español', code: 'es' }
        };
        
        let currentLanguage = 'EN';
        let languageIndex = 0;
        const languageKeys = Object.keys(languages);
        
        // Enhanced TTS messages for different languages
        const ttsMessages = {
            'EN': {
                waiting: 'Ticket {ticketId} is now waiting in queue',
                done: 'Ticket {ticketId} charging completed at Bay {bayNumber}. Please disconnect your vehicle',
                assigned: 'Ticket {ticketId} assigned to Bay {bayNumber}. Please proceed to your charging bay',
                available: 'Bay {bayNumber} is now available for charging',
                maintenance: 'Bay {bayNumber} is under maintenance. Please use another bay',
                charging_started: 'Charging started for ticket {ticketId} at Bay {bayNumber}',
                queue_position: 'You are position {position} in the queue',
                welcome: 'Welcome to Cephra charging station',
                thank_you: 'Thank you for using Cephra charging station',
                payment_success: 'Payment successful. Please proceed to Bay {bayNumber}',
                payment_failed: 'Payment failed. Please try again',
                emergency: 'Emergency stop activated at Bay {bayNumber}',
                system_error: 'System error detected. Please contact support'
            },
            'ZH': {
                waiting: '票据 {ticketId} 现在正在队列中等待',
                done: '票据 {ticketId} 在 {bayNumber} 号充电桩充电完成。请断开您的车辆',
                assigned: '票据 {ticketId} 已分配到 {bayNumber} 号充电桩。请前往您的充电桩',
                available: '{bayNumber} 号充电桩现在可用于充电',
                maintenance: '{bayNumber} 号充电桩正在维护中。请使用其他充电桩',
                charging_started: '票据 {ticketId} 在 {bayNumber} 号充电桩开始充电',
                queue_position: '您在队列中的位置是第 {position} 位',
                welcome: '欢迎使用Cephra充电站',
                thank_you: '感谢您使用Cephra充电站',
                payment_success: '支付成功。请前往 {bayNumber} 号充电桩',
                payment_failed: '支付失败。请重试',
                emergency: '{bayNumber} 号充电桩紧急停止已激活',
                system_error: '检测到系统错误。请联系技术支持'
            },
            'FIL': {
                waiting: 'Tiket {ticketId} ay naghihintay na sa pila',
                done: 'Tapos na ang pagkarga ng Tiket {ticketId} sa Bay {bayNumber}. Pakidisconnect ang inyong sasakyan',
                assigned: 'Nakatalaga na ang Tiket {ticketId} sa Bay {bayNumber}. Pumunta sa inyong charging bay',
                available: 'Available na ang Bay {bayNumber} para sa pagkarga',
                maintenance: 'Under maintenance ang Bay {bayNumber}. Gumamit ng ibang bay',
                charging_started: 'Nagsimula na ang pagkarga ng Tiket {ticketId} sa Bay {bayNumber}',
                queue_position: 'Ikaw ay nasa position {position} sa pila',
                welcome: 'Maligayang pagdating sa Cephra charging station',
                thank_you: 'Salamat sa paggamit ng Cephra charging station',
                payment_success: 'Successful ang bayad. Pumunta sa Bay {bayNumber}',
                payment_failed: 'Failed ang bayad. Subukan ulit',
                emergency: 'Emergency stop na-activate sa Bay {bayNumber}',
                system_error: 'May system error. Tawagan ang support'
            },
            'CEB': {
                waiting: 'Tiket {ticketId} naghulat na sa pila',
                done: 'Nahuman na ang pag-charge sa Tiket {ticketId} sa Bay {bayNumber}. Pakidisconnect ang inyong sakyanan',
                assigned: 'Nakatalaga na ang Tiket {ticketId} sa Bay {bayNumber}. Adto sa inyong charging bay',
                available: 'Available na ang Bay {bayNumber} para sa pag-charge',
                maintenance: 'Under maintenance ang Bay {bayNumber}. Gamit ang lain nga bay',
                charging_started: 'Nagsugod na ang pag-charge sa Tiket {ticketId} sa Bay {bayNumber}',
                queue_position: 'Ikaw naa sa position {position} sa pila',
                welcome: 'Maayong pag-abot sa Cephra charging station',
                thank_you: 'Salamat sa pag-gamit sa Cephra charging station',
                payment_success: 'Successful ang bayad. Adto sa Bay {bayNumber}',
                payment_failed: 'Failed ang bayad. Sulayi usab',
                emergency: 'Emergency stop na-activate sa Bay {bayNumber}',
                system_error: 'Naay system error. Tawagi ang support'
            },
            'ES': {
                waiting: 'Boleto {ticketId} está ahora esperando en la cola',
                done: 'Boleto {ticketId} terminó de cargar en la Bahía {bayNumber}. Por favor desconecte su vehículo',
                assigned: 'Boleto {ticketId} ahora está asignado a la Bahía {bayNumber}. Por favor diríjase a su bahía',
                available: 'La Bahía {bayNumber} está ahora disponible para cargar',
                maintenance: 'La Bahía {bayNumber} está en mantenimiento. Por favor use otra bahía',
                charging_started: 'La carga comenzó para el boleto {ticketId} en la Bahía {bayNumber}',
                queue_position: 'Usted está en la posición {position} en la cola',
                welcome: 'Bienvenido a la estación de carga Cephra',
                thank_you: 'Gracias por usar la estación de carga Cephra',
                payment_success: 'Pago exitoso. Por favor proceda a la Bahía {bayNumber}',
                payment_failed: 'Pago fallido. Por favor intente de nuevo',
                emergency: 'Parada de emergencia activada en la Bahía {bayNumber}',
                system_error: 'Error del sistema detectado. Por favor contacte soporte'
            }
        };
        
        // TTS Settings
        let ttsVolume = 0.8;
        let ttsRate = 0.9;
        let ttsPitch = 1.1;
        
        let currentQueue = [];
        let lastBays = {};
        let lastQueueTickets = new Set(); // Track tickets that were already announced
        let page = 1;
        const pageSize = 10;
        let socket = null;
        let isConnected = false;
        let reconnectAttempts = 0;
        let reconnectInterval = null;
        const maxReconnectAttempts = 5;
        const reconnectDelay = 3000; // 3 seconds
        let isFirstLoad = true; // Flag to track first data load

        // Toolbar simplified to single Bay Announcer toggle
        // Language toggle functionality
        const languageBtn = document.getElementById('languageBtn');
        languageBtn.onchange = (e) => {
            currentLanguage = e.target.value;
            languageIndex = languageKeys.indexOf(currentLanguage);

            // Visual feedback
            languageBtn.style.backgroundColor = '#4CAF50';
            setTimeout(() => {
                languageBtn.style.backgroundColor = '';
            }, 500);

            // Save language preference
            localStorage.setItem('monitor_language', currentLanguage);

            // Update fullscreen language select if in fullscreen mode
            if (isFullscreen) {
                const fullscreenLangSelect = document.getElementById('fullscreenLangBtn');
                if (fullscreenLangSelect) {
                    fullscreenLangSelect.value = currentLanguage;
                }
            }

            // Announce language change
            const langName = languages[currentLanguage].name;
            speak(`Language changed to ${langName}`);

            console.log('Language changed to:', langName);
        };

        // Load saved language preference
        const savedLang = localStorage.getItem('monitor_language') || 'EN';
        const savedIndex = languageKeys.indexOf(savedLang);
        if (savedIndex !== -1) {
            languageIndex = savedIndex;
            currentLanguage = savedLang;
            languageBtn.value = currentLanguage;
        }
        
        // Initialize TTS controls
        const volumeSlider = document.getElementById('volumeSlider');
        const speedSlider = document.getElementById('speedSlider');
        
        // Load saved TTS settings
        const savedVolume = localStorage.getItem('monitor_tts_volume');
        const savedSpeed = localStorage.getItem('monitor_tts_speed');
        
        if (savedVolume) {
            ttsVolume = parseFloat(savedVolume);
            volumeSlider.value = Math.round(ttsVolume * 100);
        }
        
        if (savedSpeed) {
            ttsRate = parseFloat(savedSpeed);
            speedSlider.value = Math.round(ttsRate * 100);
        }
        
        // Volume control
        volumeSlider.addEventListener('input', (e) => {
            ttsVolume = e.target.value / 100;
            localStorage.setItem('monitor_tts_volume', ttsVolume.toString());
        });
        
        // Speed control
        speedSlider.addEventListener('input', (e) => {
            ttsRate = e.target.value / 100;
            localStorage.setItem('monitor_tts_speed', ttsRate.toString());
        });
        
        // Test TTS button
        const testTTSBtn = document.getElementById('testTTSBtn');
        if (testTTSBtn) {
            testTTSBtn.addEventListener('click', () => {
                // Test different announcement types
                const testMessages = [
                    () => announceWelcome(),
                    () => announceQueuePosition(1),
                    () => announceChargingStarted('TEST001', 1),
                    () => announceTicketDone('TEST001', 1),
                    () => announceBayAvailable(1),
                    () => announcePaymentSuccess(1)
                ];
                
                // Cycle through test messages
                const randomIndex = Math.floor(Math.random() * testMessages.length);
                testMessages[randomIndex]();
            });
        }

        // Responsive burger toggle
        const burgerBtn = document.getElementById('burgerBtn');
        const toolbarWrap = document.getElementById('toolbarWrap');
        if (burgerBtn && toolbarWrap) {
            burgerBtn.addEventListener('click', () => {
                toolbarWrap.classList.toggle('show');
            });
        }

    // Theme / display state
    let isFullscreen = false; // kept for possible future re-enable
    let isDarkMode = false; // Default to light mode
        const themeBtn = document.getElementById('themeBtn');
        
        // Load saved theme preference (default to light)
        const savedTheme = localStorage.getItem('monitor_theme') || 'light';
        if (savedTheme === 'dark') {
            document.body.classList.remove('light');
            document.body.classList.add('dark-theme');
            isDarkMode = true;
        } else {
            document.body.classList.remove('dark-theme');
            document.body.classList.add('light');
            isDarkMode = false;
        }

        // Set initial button text
        themeBtn.textContent = isDarkMode ? 'Light Mode' : 'Dark Mode';

        // Fullscreen button event listener
        const fullscreenBtn = document.getElementById('fullscreenBtn');
        if (fullscreenBtn) {
            fullscreenBtn.addEventListener('click', () => {
                if (isFullscreen) {
                    isFullscreen = false;
                    document.body.classList.remove('fullscreen-mode');
                    removeFullscreenUI();
                    fullscreenBtn.textContent = 'Fullscreen';
                } else {
                    isFullscreen = true;
                    document.body.classList.add('fullscreen-mode');
                    createFullscreenUI();
                    fullscreenBtn.textContent = 'Exit Fullscreen';
                }
            });
        }

        function createFullscreenUI() {
            // Create fullscreen header
            const header = document.createElement('div');
            header.className = 'fullscreen-header';
            header.innerHTML = `
                <div class="fullscreen-title">
                    <span>Cephra Live Monitor</span>
                </div>
                <div class="fullscreen-controls">
                    <button class="fullscreen-btn" id="fullscreenThemeBtn">${isDarkMode ? 'Light Mode' : 'Dark Mode'}</button>
                    <select class="fullscreen-btn language-select" id="fullscreenLangBtn">
                        <option value="EN">EN</option>
                        <option value="ZH">ZH</option>
                        <option value="FIL">FIL</option>
                        <option value="CEB">CEB</option>
                        <option value="ES">ES</option>
                    </select>
                    <button class="fullscreen-btn exit" id="fullscreenExitBtn">Exit</button>
                </div>
            `;
            
            // Create fullscreen grid
            const grid = document.createElement('div');
            grid.className = 'fullscreen-grid';
            grid.id = 'fullscreen-grid';
            
            document.body.appendChild(header);
            document.body.appendChild(grid);
            
            // Add event listeners for fullscreen controls
            document.getElementById('fullscreenExitBtn').onclick = () => {
                isFullscreen = false;
                document.body.classList.remove('fullscreen-mode');
                removeFullscreenUI();
                const fullscreenBtn = document.getElementById('fullscreenBtn');
                if (fullscreenBtn) fullscreenBtn.textContent = 'Fullscreen';
            };
            
            document.getElementById('fullscreenThemeBtn').onclick = () => {
                isDarkMode = !isDarkMode;
                const themeBtn = document.getElementById('themeBtn');
                
                if (isDarkMode) {
                    document.body.classList.remove('light');
                    document.body.classList.add('dark-theme');
                    if (themeBtn) themeBtn.textContent = 'Light Mode';
                    localStorage.setItem('monitor_theme', 'dark');
                } else {
                    document.body.classList.remove('dark-theme');
                    document.body.classList.add('light');
                    if (themeBtn) themeBtn.textContent = 'Dark Mode';
                    localStorage.setItem('monitor_theme', 'light');
                }
                
                document.getElementById('fullscreenThemeBtn').textContent = isDarkMode ? 'Light' : 'Dark';
                
                // Update fullscreen theme classes
                if (isDarkMode) {
                    document.body.classList.add('dark-fullscreen');
                    document.body.classList.remove('light');
                } else {
                    document.body.classList.remove('dark-fullscreen');
                    document.body.classList.add('light');
                }
            };
            
            const fullscreenLangSelect = document.getElementById('fullscreenLangBtn');
            fullscreenLangSelect.value = currentLanguage;
            fullscreenLangSelect.onchange = (e) => {
                currentLanguage = e.target.value;
                languageIndex = languageKeys.indexOf(currentLanguage);

                // Visual feedback
                fullscreenLangSelect.style.backgroundColor = '#4CAF50';
                setTimeout(() => {
                    fullscreenLangSelect.style.backgroundColor = '';
                }, 500);

                // Save language preference
                localStorage.setItem('monitor_language', currentLanguage);
                // Update main language select
                const mainLangSelect = document.getElementById('languageBtn');
                if (mainLangSelect) {
                    mainLangSelect.value = currentLanguage;
                }
                // Announce language change
                const langName = languages[currentLanguage].name;
                speak(`Language changed to ${langName}`);

                console.log('Language changed to:', langName);
            };
            
            // Render bays in fullscreen
            renderFullscreenBays();
        }
        
        function removeFullscreenUI() {
            const header = document.querySelector('.fullscreen-header');
            const grid = document.querySelector('.fullscreen-grid');
            if (header) header.remove();
            if (grid) grid.remove();
        }
        
        function renderFullscreenBays() {
            const grid = document.getElementById('fullscreen-grid');
            if (!grid) return;
            
            grid.innerHTML = '';
            
            // Get current bay data
            const bays = currentQueue.length > 0 ?
                Array.from({length: 8}, (_, i) => {
                    const bayNum = i + 1;
                    const bayData = lastBays[`Bay-${bayNum}`];
                    return {
                        bay_number: bayNum,
                        status: bayData?.status || 'available',
                        ticket_id: bayData?.ticket_id || '-',
                        username: bayData?.username || '-',
                        plate_number: bayData?.plate_number || '-',
                        service_type: bayNum <= 3 ? 'Fast' : 'Normal'
                    };
                }) :
                Array.from({length: 8}, (_, i) => ({
                    bay_number: i + 1,
                    status: 'available',
                    ticket_id: '-',
                    username: '-',
                    plate_number: '-',
                    service_type: i < 3 ? 'Fast' : 'Normal'
                }));
            
            bays.forEach(bay => {
                const bayElement = document.createElement('div');
                bayElement.className = `fullscreen-bay ${bay.status}`;
                bayElement.innerHTML = `
                    <div class="fullscreen-bay-header">
                        <h3 class="fullscreen-bay-title">Bay-${bay.bay_number}</h3>
                        <span class="fullscreen-bay-type ${bay.service_type === 'Fast' ? 'fast-bay' : ''}">${bay.service_type}</span>
                    </div>
                    <div class="fullscreen-bay-status">
                        <div class="fullscreen-status-badge ${bay.status}">
                            ${bay.status === 'available' ? 'Available' : 'Occupied'}
                        </div>
                    </div>
                    <div class="fullscreen-bay-details">
                        <div class="fullscreen-detail-item">
                            <span class="fullscreen-detail-label">Ticket:</span>
                            <span class="fullscreen-detail-value">${bay.ticket_id}</span>
                        </div>
                        <div class="fullscreen-detail-item">
                            <span class="fullscreen-detail-label">User:</span>
                            <span class="fullscreen-detail-value">${bay.username}</span>
                        </div>
                        <div class="fullscreen-detail-item">
                            <span class="fullscreen-detail-label">Plate:</span>
                            <span class="fullscreen-detail-value">${bay.plate_number}</span>
                        </div>
                    </div>
                `;
                grid.appendChild(bayElement);
            });
        }
        
        // Enhanced theme toggle
        if (themeBtn) {
            themeBtn.onclick = () => {
                isDarkMode = !isDarkMode;
                
                if (isDarkMode) {
                    document.body.classList.remove('light');
                    document.body.classList.add('dark-theme');
                    themeBtn.textContent = 'Light Mode';
                    localStorage.setItem('monitor_theme', 'dark');
                } else {
                    document.body.classList.remove('dark-theme');
                    document.body.classList.add('light');
                    themeBtn.textContent = 'Dark Mode';
                    localStorage.setItem('monitor_theme', 'light');
                }
                
                // Update fullscreen theme if in fullscreen
                if (isFullscreen) {
                    if (isDarkMode) {
                        document.body.classList.add('dark-fullscreen');
                        document.body.classList.remove('light');
                    } else {
                        document.body.classList.remove('dark-fullscreen');
                        document.body.classList.add('light');
                    }
                }
    };
}
        
        // WebSocket connection
        function connectWebSocket() {
            // Get the current hostname and use it to build the WebSocket URL
            const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
            const host = window.location.hostname;
            const wsUrl = `${protocol}//${host}:8080`;
            
            // Create WebSocket connection
            socket = new WebSocket(wsUrl);
            
            // Connection opened
            socket.addEventListener('open', (event) => {
                console.log('WebSocket connected');
                isConnected = true;
                // connected
                clearInterval(reconnectInterval);
                reconnectInterval = null;
                // updateConnectionStatus(true); // Removed
            });
            
            // Listen for messages
            socket.addEventListener('message', (event) => {
                try {
                    const data = JSON.parse(event.data);
                    if (data && !data.error) {
                        // Initialize lastQueueTickets with current queue tickets on first load
                        if (lastQueueTickets.size === 0 && data.queue && data.queue.length > 0) {
                            // Add all current tickets to the set to prevent re-announcing
                            data.queue.forEach(ticket => {
                                if (ticket.ticket_id) {
                                    lastQueueTickets.add(ticket.ticket_id);
                                    // Only announce if this is not the first load
                                    if (!isFirstLoad) {
                                        announceWaitingTicket(ticket.ticket_id);
                                    }
                                }
                            });
                        }
                        
                        // After first load, set flag to false
                        isFirstLoad = false;
                        
                        handleAlerts(data);
                        renderBays(data.bays || []);
                        currentQueue = data.queue || [];
                        renderQueuePage();
                        document.getElementById('ts').textContent = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
                        
                        // Update fullscreen UI if active
                        if (isFullscreen) {
                            renderFullscreenBays();
                        }
                    } else {
                        console.error('Monitor error', data.error);
                    }
                } catch (e) {
                    console.error('Error processing WebSocket message', e);
                }
            });
            
            // Connection closed
            socket.addEventListener('close', (event) => {
                console.log('WebSocket disconnected');
                isConnected = false;
                // updateConnectionStatus(false); // Removed
                socket = null;
                
                // Attempt to reconnect if not already trying
                if (!reconnectInterval && reconnectAttempts < maxReconnectAttempts) {
                    reconnectInterval = setInterval(attemptReconnect, reconnectDelay);
                } else if (reconnectAttempts >= maxReconnectAttempts) {
                    // Fall back to polling if max reconnect attempts reached
                    console.log('Max reconnect attempts reached, falling back to polling');
                    startPolling();
                }
            });
            
            // Connection error
            socket.addEventListener('error', (event) => {
                console.error('WebSocket error', event);
                isConnected = false;
                // updateConnectionStatus(false); // Removed
            });
        }
        
        function attemptReconnect() {
            if (reconnectAttempts < maxReconnectAttempts) {
                reconnectAttempts++;
                console.log(`Attempting to reconnect (${reconnectAttempts}/${maxReconnectAttempts})...`);
                connectWebSocket();
            } else {
                clearInterval(reconnectInterval);
                reconnectInterval = null;
                console.log('Max reconnect attempts reached, falling back to polling');
                startPolling();
            }
        }
        
        // Connection status indicator removed
        
        let pollingInterval = null;
        
        function startPolling() {
            if (!pollingInterval) {
                console.log('Starting polling fallback');
                // Fetch immediately
                fetchSnapshot();
                // Then set up interval
                pollingInterval = setInterval(fetchSnapshot, 3000);
            }
        }
        
        function stopPolling() {
            if (pollingInterval) {
                console.log('Stopping polling');
                clearInterval(pollingInterval);
                pollingInterval = null;
            }
        }
        
        async function fetchSnapshot() {
            try {
                const res = await fetch('api/monitor.php', { cache: 'no-store' });
                const data = await res.json();
                if (data && !data.error) {
                    // Initialize lastQueueTickets with current queue tickets on first load
                    if (lastQueueTickets.size === 0 && data.queue && data.queue.length > 0) {
                        // Add all current tickets to the set to prevent re-announcing
                        data.queue.forEach(ticket => {
                            if (ticket.ticket_id) {
                                lastQueueTickets.add(ticket.ticket_id);
                                // Only announce if this is not the first load
                                if (!isFirstLoad) {
                                    announceWaitingTicket(ticket.ticket_id);
                                }
                            }
                        });
                    }
                    
                    // After first load, set flag to false
                    isFirstLoad = false;
                    
                    handleAlerts(data);
                    renderBays(data.bays || []);
                    currentQueue = data.queue || [];
                    renderQueuePage();
                    document.getElementById('ts').textContent = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
                    
                    // Update fullscreen UI if active
                    if (isFullscreen) {
                        renderFullscreenBays();
                    }
                } else {
                    console.error('Monitor error', data.error);
                }
            } catch (e) {
                console.error('Monitor fetch failed', e);
            }
        }

        function statusBadge(status) {
            const s = (status || '').toLowerCase();
            if (s === 'available') return '<span class="badge available">Available</span>';
            if (s === 'occupied') return '<span class="badge occupied">Occupied</span>';
            return '<span class="badge maintenance">Maintenance</span>';
        }

        function renderBays(bays) {
            const wrap = document.getElementById('bays');
            const bayMap = {};
            (bays || []).forEach(b => { bayMap[b.bay_number] = b; });

            const bayIds = ['Bay-1','Bay-2','Bay-3','Bay-4','Bay-5','Bay-6','Bay-7','Bay-8'];
            wrap.innerHTML = bayIds.map(id => {
                const fallbackType = (id === 'Bay-1' || id === 'Bay-2' || id === 'Bay-3') ? 'Fast' : 'Normal';
                const b = bayMap[id] || { bay_number: id, bay_type: fallbackType, status: 'Available', current_ticket_id: '', current_username: '', plate_number: '' };
                const ticket = b.current_ticket_id || '';
                const user = b.current_username || '';
                const plate = b.plate_number || '';
                const type = b.bay_type ? ` <small class=\"muted\">(${b.bay_type})</small>` : '';
                const statusClass = (b.status || '').toLowerCase();
                return `
                    <div class="bay ${statusClass}">
                        <div class="bay-header">
                            <h3>${b.bay_number}${type}</h3>
                            <div class="bay-type">${b.bay_type || 'Normal'}</div>
                        </div>
                        <div class="bay-center-status">${(b.status || 'Available')}</div>
                        <div class="bay-status">${statusBadge(b.status)}</div>
                        <div class="bay-info">
                            <div class="info-row"><span class="info-label">Ticket:</span> <span class="info-value">${ticket || '-'}</span></div>
                            <div class="info-row"><span class="info-label">Plate:</span> <span class="info-value">${plate || '-'}</span></div>
                            <div class="info-row"><span class="info-label">User:</span> <span class="info-value">${user || '-'}</span></div>
                        </div>
                    </div>`;
            }).join('');
        }

        function renderQueuePage() {
            const tbody = document.getElementById('queue');
            // Clamp page to valid range BEFORE slicing
            let totalPages = Math.max(1, Math.ceil(currentQueue.length / pageSize));
            if (page > totalPages) page = totalPages;
            if (page < 1) page = 1;
            const start = (page - 1) * pageSize;
            const rows = currentQueue.slice(start, start + pageSize);
            tbody.innerHTML = (rows.length ? rows : [{ticket_id:'', username:'', service_type:'', created_at:''}]).map((r, idx) => {
                if (rows.length === 0 && idx === 0) {
                    return `<tr><td colspan="4" class="muted">No waiting tickets</td></tr>`;
                }
                const since = r.created_at ? new Date(r.created_at).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '-';
                return `<tr>
                    <td>${r.ticket_id || ''}</td>
                    <td>${r.username || ''}</td>
                    <td>${r.service_type || ''}</td>
                    <td>${since}</td>
                </tr>`;
            }).join('');
            document.getElementById('pageInfo').textContent = `Page ${page} / ${totalPages}`;
        }

        document.getElementById('prevPage').onclick = () => { if (page > 1) { page--; renderQueuePage(); } };
        document.getElementById('nextPage').onclick = () => { const total = Math.max(1, Math.ceil(currentQueue.length / pageSize)); if (page < total) { page++; renderQueuePage(); } };

        // Track tickets that were previously in bays
        let ticketsInBays = new Map(); // Map of ticketId -> bayNumber
        
        function handleAlerts(data) {
            const bays = data.bays || [];
            const currentQueue = data.queue || [];
            
            // Check for bay status changes and announce them
            bays.forEach(b => {
                const key = b.bay_number;
                const prev = lastBays[key];
                const now = (b.status || '') + '|' + (b.current_ticket_id || '') + '|' + (b.current_username || '');
                
                if (prev !== now) {
                    // Parse previous state for TTS announcement
                    let prevStatus = '';
                    let prevTicketId = '';
                    let newStatus = b.status || '';
                    let newTicketId = b.current_ticket_id || '';
                    let newUsername = b.current_username || '';
                    
                    if (prev) {
                        const prevParts = prev.split('|');
                        prevStatus = prevParts[0] || '';
                        prevTicketId = prevParts[1] || '';
                    }
                    
                    // Track tickets in bays
                    if (prevTicketId && !newTicketId && prevStatus === 'occupied' && newStatus === 'available') {
                        // Ticket is done charging
                        announceTicketDone(prevTicketId, b.bay_number);
                    }
                    
                    // Update ticket tracking
                    if (prevTicketId) {
                        ticketsInBays.delete(prevTicketId);
                    }
                    if (newTicketId) {
                        ticketsInBays.set(newTicketId, b.bay_number);
                    }
                    
                    // Only announce if we have a previous state and status actually changed
                    if (prev && prevStatus !== newStatus) {
                        announceBayChange(b.bay_number, prevStatus, newStatus, newTicketId, newUsername);
                    }
                }
                lastBays[key] = now;
             });
            
            
            // Check for new waiting tickets
            currentQueue.forEach(ticket => {
                const ticketId = ticket.ticket_id;
                if (ticketId && !lastQueueTickets.has(ticketId)) {
                    // New ticket in waiting queue
                    announceWaitingTicket(ticketId);
                    lastQueueTickets.add(ticketId);
                    // Ensure the announcement is visible even if the user hasn't charged yet
                    console.log('New waiting ticket detected:', ticketId);
                }
            });
            
            // Check for tickets that moved from waiting to a bay
            const currentTicketIds = new Set(currentQueue.map(t => t.ticket_id).filter(id => id));
            for (let ticketId of lastQueueTickets) {
                if (!currentTicketIds.has(ticketId)) {
                    // Ticket is no longer in waiting queue
                    // Check if it's now in a bay
                    const bayNumber = ticketsInBays.get(ticketId);
                    if (bayNumber) {
                        // Ticket moved from waiting to a bay
                        announceTicketAssigned(ticketId, bayNumber);
                    }
                    lastQueueTickets.delete(ticketId);
                }
            }
        }

        // Improved text-to-speech functionality with better voice selection and audio feedback
        // Function to show a visual toast notification
        function showToast(text) {
            // Create visual feedback for announcement
            const announcementElement = document.createElement('div');
            announcementElement.className = 'announcement-toast';
            announcementElement.textContent = text;
            document.body.appendChild(announcementElement);
            
            // Animate in
            setTimeout(() => {
                announcementElement.classList.add('show');
            }, 10);
            
            // Remove after animation completes
            setTimeout(() => {
                announcementElement.classList.remove('show');
                setTimeout(() => {
                    document.body.removeChild(announcementElement);
                }, 500);
            }, 4000);
        }
        
        function speak(text, messageType = null, variables = {}) {
            // Always show the toast notification
            showToast(text);
            
            // Only speak if announcer is enabled
            if (document.getElementById('announcerChk').checked && 'speechSynthesis' in window) {
                // Stop any current speech
                speechSynthesis.cancel();
                
                // Use localized message if messageType is provided
                let messageToSpeak = text;
                if (messageType && ttsMessages[currentLanguage] && ttsMessages[currentLanguage][messageType]) {
                    messageToSpeak = ttsMessages[currentLanguage][messageType];
                    // Replace variables in the message
                    for (const [key, value] of Object.entries(variables)) {
                        messageToSpeak = messageToSpeak.replace(`{${key}}`, value);
                    }
                }
                
                const utterance = new SpeechSynthesisUtterance(messageToSpeak);
                utterance.rate = ttsRate;  // Use user-controlled rate
                utterance.pitch = ttsPitch; // Use user-controlled pitch
                utterance.volume = ttsVolume; // Use user-controlled volume
                
                // Get all available voices
                let voices = [];
                
                // Handle the case where voices might not be loaded yet
                const loadVoices = () => {
                    voices = speechSynthesis.getVoices();
                    
                    // Enhanced voice selection based on current language
                    const currentLang = languages[currentLanguage].code;
                    let preferredVoices = [];
                    
                    // Language-specific voice preferences with better matching
                    switch (currentLang) {
                        case 'zh':
                            preferredVoices = [
                                'Microsoft Huihui - Chinese (Simplified, PRC)',
                                'Microsoft Yaoyao - Chinese (Simplified, PRC)',
                                'Chinese (Simplified)',
                                'Chinese (Mandarin)',
                                'Mandarin Chinese',
                                'Chinese',
                                'Huihui',
                                'Yaoyao'
                            ];
                            break;
                        case 'fil':
                            preferredVoices = [
                                'Filipino',
                                'Tagalog',
                                'Microsoft Maria - Filipino (Philippines)',
                                'Microsoft Rosa - Filipino (Philippines)',
                                'Google Filipino',
                                'Google Tagalog',
                                'Filipino (Philippines)',
                                'Tagalog (Philippines)',
                                'Filipino Female',
                                'Tagalog Female',
                                'Filipino Male',
                                'Tagalog Male',
                                'English (Philippines)',
                                'English (US)',
                                'English'
                            ];
                            break;
                        case 'ceb':
                            preferredVoices = [
                                'Cebuano',
                                'Bisaya',
                                'Microsoft Cebuano - Cebuano (Philippines)',
                                'Google Cebuano',
                                'Google Bisaya',
                                'Cebuano (Philippines)',
                                'Bisaya (Philippines)',
                                'Cebuano Female',
                                'Bisaya Female',
                                'Cebuano Male',
                                'Bisaya Male',
                                'Filipino',
                                'Tagalog',
                                'English (Philippines)',
                                'English (US)',
                                'English'
                            ];
                            break;
                        case 'es':
                            preferredVoices = [
                                'Microsoft Helena - Spanish (Spain)',
                                'Microsoft Sabina - Spanish (Mexico)',
                                'Microsoft Laura - Spanish (Spain)',
                                'Spanish (Spain)',
                                'Spanish (Mexico)',
                                'Spanish (Latin America)',
                                'Español',
                                'Spanish',
                                'Helena',
                                'Sabina',
                                'Laura'
                            ];
                            break;
                        default: // English
                            preferredVoices = [
                                'Microsoft Zira - English (United States)',
                                'Microsoft Hazel - English (Great Britain)',
                                'Microsoft Susan - English (United States)',
                        'Google UK English Female',
                                'Google US English Female',
                        'Samantha',
                        'Karen',
                        'Susan',
                                'Zira',
                                'Hazel',
                                'English (US)',
                                'English (UK)',
                        'Female',
                        'female'
                    ];
                    }
                    
                    // Enhanced voice selection with multiple matching strategies
                    let selectedVoice = null;
                    
                    // Strategy 1: Exact name match with localService
                    for (const preferred of preferredVoices) {
                        const found = voices.find(voice => 
                            voice.name === preferred && voice.localService
                        );
                        if (found) {
                            selectedVoice = found;
                            break;
                        }
                    }
                    
                    // Strategy 2: Contains match with localService
                    if (!selectedVoice) {
                        for (const preferred of preferredVoices) {
                            const found = voices.find(voice => 
                                voice.name.toLowerCase().includes(preferred.toLowerCase()) && voice.localService
                            );
                            if (found) {
                                selectedVoice = found;
                                break;
                            }
                        }
                    }
                    
                    // Strategy 2.5: Special Filipino/Bisaya native language matching
                    if (!selectedVoice && (currentLang === 'fil' || currentLang === 'ceb')) {
                        const nativeKeywords = currentLang === 'fil' 
                            ? ['filipino', 'tagalog', 'philippines']
                            : ['cebuano', 'bisaya', 'filipino', 'tagalog', 'philippines'];
                        
                        for (const keyword of nativeKeywords) {
                            const found = voices.find(voice => 
                                voice.name.toLowerCase().includes(keyword) && voice.localService
                            );
                            if (found) {
                                selectedVoice = found;
                                break;
                            }
                        }
                    }
                    
                    // Strategy 3: Language code match
                    if (!selectedVoice) {
                        const found = voices.find(voice => 
                            voice.lang.startsWith(currentLang) && voice.localService
                        );
                        if (found) {
                            selectedVoice = found;
                        }
                    }
                    
                    // Strategy 4: Exact name match without localService restriction
                    if (!selectedVoice) {
                        for (const preferred of preferredVoices) {
                            const found = voices.find(voice => 
                                voice.name === preferred
                            );
                            if (found) {
                                selectedVoice = found;
                                break;
                            }
                        }
                    }
                    
                    // Strategy 5: Contains match without localService restriction
                    if (!selectedVoice) {
                        for (const preferred of preferredVoices) {
                            const found = voices.find(voice => 
                                voice.name.toLowerCase().includes(preferred.toLowerCase())
                            );
                            if (found) {
                                selectedVoice = found;
                                break;
                            }
                        }
                    }
                    
                    // Strategy 6: Language code match without localService restriction
                    if (!selectedVoice) {
                        const found = voices.find(voice => 
                            voice.lang.startsWith(currentLang)
                        );
                        if (found) {
                            selectedVoice = found;
                        }
                    }
                    
                    // Strategy 7: Special Filipino/Bisaya native language fallback (without localService restriction)
                    if (!selectedVoice && (currentLang === 'fil' || currentLang === 'ceb')) {
                        const nativeKeywords = currentLang === 'fil' 
                            ? ['filipino', 'tagalog', 'philippines']
                            : ['cebuano', 'bisaya', 'filipino', 'tagalog', 'philippines'];
                        
                        for (const keyword of nativeKeywords) {
                            const found = voices.find(voice => 
                                voice.name.toLowerCase().includes(keyword)
                            );
                            if (found) {
                                selectedVoice = found;
                                break;
                            }
                        }
                    }
                    
                    // If still no voice, use the first available
                    if (!selectedVoice && voices.length > 0) {
                        selectedVoice = voices[0];
                    }
                    
                    if (selectedVoice) {
                        utterance.voice = selectedVoice;
                    }
                    
                    // Speak the text
                    speechSynthesis.speak(utterance);
                };
                
                // If voices are already loaded, use them
                if (speechSynthesis.getVoices().length > 0) {
                    loadVoices();
                } else {
                    // Otherwise wait for them to load
                    speechSynthesis.onvoiceschanged = loadVoices;
                }
            }
        }
        
        function announceBayChange(bayNumber, oldStatus, newStatus, ticketId, username) {
            // Only announce for bays 1-8
            if (bayNumber < 1 || bayNumber > 8) return;
            
            if (oldStatus && newStatus && oldStatus !== newStatus) {
                // Debug log to help troubleshoot
                console.log('Bay announcement:', {bayNumber, oldStatus, newStatus, ticketId, username});
                
                // Use proper messageType approach to avoid "Bay Bay" duplication
                if (newStatus.toLowerCase() === 'available') {
                    speak('', 'available', { bayNumber });
                } else if (newStatus.toLowerCase() === 'occupied') {
                    // For occupied status, create a custom message since we have additional info
                    let message = `Bay ${bayNumber} is now occupied`;
                    if (username) {
                        message += ` by ${username}`;
                    }
                    if (ticketId) {
                        message += ` with ticket ${ticketId}`;
                    }
                    speak(message);
                } else if (newStatus.toLowerCase() === 'maintenance') {
                    speak('', 'maintenance', { bayNumber });
                }
            }
        }
        
        function announceWaitingTicket(ticketId) {
            // Simple announcement: just the ticket number
            const message = `Ticket ${ticketId} is now waiting`;
            
            // Debug log to help troubleshoot
            console.log('Waiting ticket announcement:', message, {ticketId});
            
            if (document.getElementById('announcerChk').checked) {
                speak(message, 'waiting', { ticketId });
            }
        }
        
        function announceChargingStarted(ticketId, bayNumber) {
            const message = `Charging started for ticket ${ticketId} at Bay ${bayNumber}`;
            console.log('Charging started announcement:', message, {ticketId, bayNumber});
            
            if (document.getElementById('announcerChk').checked) {
                speak(message, 'charging_started', { ticketId, bayNumber });
            }
        }
        
        function announceQueuePosition(position) {
            const message = `You are position ${position} in the queue`;
            console.log('Queue position announcement:', message, {position});
            
            if (document.getElementById('announcerChk').checked) {
                speak(message, 'queue_position', { position });
            }
        }
        
        function announceWelcome() {
            const message = 'Welcome to Cephra charging station';
            console.log('Welcome announcement:', message);
            
            if (document.getElementById('announcerChk').checked) {
                speak(message, 'welcome', {});
            }
        }
        
        function announceThankYou() {
            const message = 'Thank you for using Cephra charging station';
            console.log('Thank you announcement:', message);
            
            if (document.getElementById('announcerChk').checked) {
                speak(message, 'thank_you', {});
            }
        }
        
        function announcePaymentSuccess(bayNumber) {
            const message = `Payment successful. Please proceed to Bay ${bayNumber}`;
            console.log('Payment success announcement:', message, {bayNumber});
            
            if (document.getElementById('announcerChk').checked) {
                speak(message, 'payment_success', { bayNumber });
            }
        }
        
        function announcePaymentFailed() {
            const message = 'Payment failed. Please try again';
            console.log('Payment failed announcement:', message);
            
            if (document.getElementById('announcerChk').checked) {
                speak(message, 'payment_failed', {});
            }
        }
        
        function announceEmergencyStop(bayNumber) {
            const message = `Emergency stop activated at Bay ${bayNumber}`;
            console.log('Emergency stop announcement:', message, {bayNumber});
            
            if (document.getElementById('announcerChk').checked) {
                speak(message, 'emergency', { bayNumber });
            }
        }
        
        function announceSystemError() {
            const message = 'System error detected. Please contact support';
            console.log('System error announcement:', message);
            
            if (document.getElementById('announcerChk').checked) {
                speak(message, 'system_error', {});
            }
        }
        
        function announceBayAvailable(bayNumber) {
            const message = `Bay ${bayNumber} is now available for charging`;
            console.log('Bay available announcement:', message, {bayNumber});
            
            if (document.getElementById('announcerChk').checked) {
                speak(message, 'available', { bayNumber });
            }
        }
        
        function announceBayMaintenance(bayNumber) {
            const message = `Bay ${bayNumber} is under maintenance. Please use another bay`;
            console.log('Bay maintenance announcement:', message, {bayNumber});
            
            if (document.getElementById('announcerChk').checked) {
                speak(message, 'maintenance', { bayNumber });
            }
        }
        
        function announceTicketDone(ticketId, bayNumber) {
            // Announcement for when a ticket is done charging
            const message = `Ticket ${ticketId} is done charging at Bay ${bayNumber}`;
            
            // Debug log to help troubleshoot
            console.log('Ticket done announcement:', message, {ticketId, bayNumber});
            
            if (document.getElementById('announcerChk').checked) {
                speak(message, 'done', { ticketId, bayNumber });
            }
        }
        
        function announceTicketAssigned(ticketId, bayNumber) {
            // Announcement for when a ticket is assigned to a bay
            const message = `Ticket ${ticketId} is now assigned to Bay ${bayNumber}`;
            
            // Debug log to help troubleshoot
            console.log('Ticket assigned announcement:', message, {ticketId, bayNumber});
            
            if (document.getElementById('announcerChk').checked) {
                speak(message, 'assigned', { ticketId, bayNumber });
            }
        }
        

        // Removed connection status indicator
        
        // Render placeholders immediately and fetch a snapshot for faster first paint
        renderBays([]);
        fetchSnapshot();
        
        // Welcome announcement on page load (with delay to allow voices to load)
        setTimeout(() => {
            announceWelcome();
        }, 2000);
        
        // Initialize connection
        if ('WebSocket' in window) {
            // WebSockets are supported, try to connect
            connectWebSocket();
        } else {
            // WebSockets are not supported, fall back to polling
            console.log('WebSockets not supported, using polling');
            startPolling();
        }
        
        // Handle page visibility changes
        document.addEventListener('visibilitychange', () => {
            if (document.visibilityState === 'visible') {
                // Page is now visible, reconnect if needed
                if (!socket && !pollingInterval) {
                    if ('WebSocket' in window) {
                        connectWebSocket();
                    } else {
                        startPolling();
                    }
                }
            }
        });
    </script>
</body>
</html>
