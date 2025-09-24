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
            --bg: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
            --panel: linear-gradient(135deg, #2a5298 0%, #1e3c72 100%);
            --card: linear-gradient(135deg, #2a5298 0%, #1e3c72 100%);
            --text: #ffffff;
            --muted: #b0c4de;
            --accent: #00d4aa;
            --accent-hover: #00e6b8;
            --avail: #00d4aa;
            --availText: #ffffff;
            --occ: #ff6b6b;
            --occText: #ffffff;
            --maint: #ffa500;
            --maintText: #ffffff;
            --shadow-sm: 0 2px 5px rgba(0,0,0,0.2);
            --shadow-md: 0 4px 10px rgba(0,0,0,0.3);
            --shadow-lg: 0 8px 20px rgba(0,0,0,0.4);
            --transition-fast: all 0.2s ease;
            --transition-normal: all 0.3s ease;
        }
        .light {
            --bg: linear-gradient(135deg, #f0f8ff 0%, #e6f3ff 100%);
            --panel: linear-gradient(135deg, #ffffff 0%, #f0f8ff 100%);
            --card: linear-gradient(135deg, #ffffff 0%, #f0f8ff 100%);
            --text: #1e3c72;
            --muted: #6c757d;
            --accent: #007bff;
            --accent-hover: #0056b3;
            --shadow-sm: 0 2px 5px rgba(0,0,0,0.1);
            --shadow-md: 0 4px 10px rgba(0,0,0,0.15);
            --shadow-lg: 0 8px 20px rgba(0,0,0,0.2);
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
            grid-template-rows: repeat(auto-fit, minmax(200px, auto)); 
            gap: 20px; 
        }
        .bay {
            background: var(--card);
            border-radius: 16px;
            padding: 18px;
            border: 1px solid rgba(255,255,255,0.1);
            min-width: 0;
            height: 200px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            box-shadow: var(--shadow-md);
            transition: var(--transition-normal);
            position: relative;
            overflow: hidden;
            background-image: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%);
        }
        .bay:hover { 
            transform: translateY(-5px); 
            box-shadow: var(--shadow-lg);
        }
        .bay::after {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 4px;
            background: var(--accent);
            opacity: 0.7;
        }
        .bay h3 { 
            margin: 0 0 12px; 
            font-size: 20px; 
            color:var(--accent); 
            word-break: break-word; 
            font-weight: 600;
            letter-spacing: 0.5px;
        }
        .bay-info {
            margin-top: 12px;
            display: flex;
            flex-direction: column;
            gap: 6px;
        }
        .info-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 14px;
        }
        .info-label {
            color: var(--muted);
            font-weight: 500;
        }
        .info-value {
            font-weight: 500;
        }
        .badge { 
            display:inline-block; 
            padding:8px 14px; 
            border-radius: 999px; 
            font-size: 14px; 
            font-weight: 500;
            box-shadow: var(--shadow-sm);
            transition: var(--transition-fast);
        }
        .available { 
            background:var(--avail); 
            color:var(--availText); 
            font-size: 16px; 
            font-weight: bold; 
            padding: 10px 16px; 
        }
        .occupied { 
            background:var(--occ); 
            color:var(--occText); 
        }
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
        .btn:hover {
            background: var(--accent);
            color: var(--bg);
            border-color: var(--accent);
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
        
        /* Fullscreen styles */
        .fullscreen-mode { 
            position: fixed; 
            top: 0; 
            left: 0; 
            width: 100vw; 
            height: 100vh; 
            z-index: 1000; 
            background: var(--bg); 
            padding: 10px; 
            overflow-y: auto; 
            box-sizing: border-box;
        }
        .fullscreen-mode h1 { 
            margin-bottom: 10px; 
            font-size: 18px;
            flex-wrap: wrap;
        }
        .fullscreen-mode .toolbar {
            flex-wrap: wrap;
            gap: 5px;
        }
        .fullscreen-mode .toolbar .btn {
            padding: 4px 8px;
            font-size: 11px;
        }
        .fullscreen-mode .toolbar label {
            display: none;
        }
        .fullscreen-mode .row { display: none; }
        .fullscreen-mode .grid { 
            display: grid;
            grid-template-columns: repeat(4, 1fr); 
            grid-template-rows: repeat(2, 1fr); 
            gap: 30px; 
            padding: 25px;
            height: calc(100vh - 80px);
            width: 100%;
            box-sizing: border-box;
        }
        .fullscreen-mode .bay { 
            padding: 25px; 
            font-size: 16px; 
            height: 100%; 
            width: 100%;
            min-height: 180px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            border-radius: 20px;
            margin: 0;
            box-sizing: border-box;
            box-shadow: var(--shadow-lg);
        }
        .fullscreen-mode .bay h3 { 
            font-size: 18px; 
            margin-bottom: 8px; 
        }
        .fullscreen-mode .badge { 
            padding: 6px 10px; 
            font-size: 14px; 
        }
        .fullscreen-mode .available { 
            font-size: 16px; 
            font-weight: bold; 
            padding: 8px 12px; 
        }
        .fullscreen-mode .muted { 
            font-size: 12px; 
            margin-top: 8px; 
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
        @media (max-width: 420px) {
            body { padding: 8px; }
            h1 { font-size: 18px; margin-bottom: 10px; }
            .toolbar { flex-direction: column; align-items: flex-start; }
            .toolbar .btn { width: 100%; margin-bottom: 5px; }
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
<body>
    <h1>
        <span class="logo"><img src="../Admin/images/MONU.png" alt="Cephra" /></span>
        Cephra Live Monitor <span id="ts" class="ts"></span>
        <div class="toolbar">
            <label class="muted" style="white-space:nowrap;"><input type="checkbox" id="announcerChk" checked /> Bay Announcer</label>
            <button class="btn" id="fullscreenBtn">Fullscreen Bays</button>
            <button class="btn" id="themeBtn">Toggle Theme</button>
            <button class="btn" id="languageBtn">EN</button>
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
        
        // TTS messages for different languages
        const ttsMessages = {
            'EN': {
                waiting: 'Ticket {ticketId} is now waiting',
                done: 'Ticket {ticketId} is done charging at Bay {bayNumber}',
                assigned: 'Ticket {ticketId} is now assigned to Bay {bayNumber}',
                available: 'Bay {bayNumber} is now available',
                maintenance: 'Bay {bayNumber} is under maintenance'
            },
            'ZH': {
                waiting: '票据 {ticketId} 现在正在等待',
                done: '票据 {ticketId} 在 {bayNumber} 号充电桩充电完成',
                assigned: '票据 {ticketId} 现在已分配到 {bayNumber} 号充电桩',
                available: '{bayNumber} 号充电桩现在可用',
                maintenance: '{bayNumber} 号充电桩正在维护中'
            },
            'FIL': {
                waiting: 'Tiket {ticketId} ay naghihintay na',
                done: 'Tapos na ang pagkarga ng Tiket {ticketId} sa Bay {bayNumber}',
                assigned: 'Nakatalaga na ang Tiket {ticketId} sa Bay {bayNumber}',
                available: 'Available na ang Bay {bayNumber}',
                maintenance: 'Under maintenance ang Bay {bayNumber}'
            },
            'CEB': {
                waiting: 'Tiket {ticketId} naghulat na',
                done: 'Nahuman na ang pag-charge sa Tiket {ticketId} sa Bay {bayNumber}',
                assigned: 'Nakatalaga na ang Tiket {ticketId} sa Bay {bayNumber}',
                available: 'Available na ang Bay {bayNumber}',
                maintenance: 'Under maintenance ang Bay {bayNumber}'
            },
            'ES': {
                waiting: 'Boleto {ticketId} está ahora esperando',
                done: 'Boleto {ticketId} terminó de cargar en la Bahía {bayNumber}',
                assigned: 'Boleto {ticketId} ahora está asignado a la Bahía {bayNumber}',
                available: 'La Bahía {bayNumber} está ahora disponible',
                maintenance: 'La Bahía {bayNumber} está en mantenimiento'
            }
        };
        
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
        // Theme toggle
        function setTheme(light) {
            document.body.classList.toggle('light', !!light);
        }
        document.getElementById('themeBtn').onclick = () => setTheme(!document.body.classList.contains('light'));
        
        // Language toggle functionality
        const languageBtn = document.getElementById('languageBtn');
        languageBtn.onclick = () => {
            languageIndex = (languageIndex + 1) % languageKeys.length;
            currentLanguage = languageKeys[languageIndex];
            languageBtn.textContent = currentLanguage;
            
            // Visual feedback
            languageBtn.style.backgroundColor = '#4CAF50';
            setTimeout(() => {
                languageBtn.style.backgroundColor = '';
            }, 500);
            
            // Save language preference
            localStorage.setItem('monitor_language', currentLanguage);
            
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
            languageBtn.textContent = currentLanguage;
        }

        // Fullscreen functionality
        let isFullscreen = false;
        const fsBtn = document.getElementById('fullscreenBtn');
        if (fsBtn) {
            fsBtn.onclick = () => {
                isFullscreen = !isFullscreen;
                document.body.classList.toggle('fullscreen-mode', isFullscreen);
                fsBtn.textContent = isFullscreen ? 'Exit Fullscreen' : 'Fullscreen Bays';
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
                const b = bayMap[id] || { bay_number: id, bay_type: fallbackType, status: 'Available', current_ticket_id: '', current_username: '' };
                const ticket = b.current_ticket_id || '';
                const user = b.current_username || '';
                const type = b.bay_type ? ` <small class=\"muted\">(${b.bay_type})</small>` : '';
                return `
                <div class="bay">
                    <h3>${b.bay_number}${type}</h3>
                    <div>${statusBadge(b.status)}</div>
                    <div class="bay-info">
                        <div class="info-row"><span class="info-label">Ticket:</span> <span class="info-value">${ticket || '-'}</span></div>
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
                utterance.rate = 0.9;  // Slightly faster but still clear
                utterance.pitch = 1.1; // Slightly higher pitch for clarity
                utterance.volume = 1.0; // Full volume
                
                // Get all available voices
                let voices = [];
                
                // Handle the case where voices might not be loaded yet
                const loadVoices = () => {
                    voices = speechSynthesis.getVoices();
                    
                    // Try to find the best voice based on current language
                    const currentLang = languages[currentLanguage].code;
                    let preferredVoices = [];
                    
                    // Language-specific voice preferences
                    switch (currentLang) {
                        case 'zh':
                            preferredVoices = ['Chinese', 'Mandarin', 'Cantonese', 'Microsoft Huihui'];
                            break;
                        case 'fil':
                        case 'ceb':
                            preferredVoices = ['Filipino', 'Tagalog', 'Cebuano', 'English']; // Fallback to English
                            break;
                        case 'es':
                            preferredVoices = ['Spanish', 'Español', 'Microsoft Helena', 'Microsoft Sabina'];
                            break;
                        default: // English
                            preferredVoices = [
                                'Microsoft Zira',
                                'Google UK English Female',
                                'Samantha',
                                'Karen',
                                'Susan',
                                'Female',
                                'female'
                            ];
                    }
                    
                    // Try to find a preferred voice
                    let selectedVoice = null;
                    for (const preferred of preferredVoices) {
                        const found = voices.find(voice => 
                            voice.name.includes(preferred) && voice.localService
                        );
                        if (found) {
                            selectedVoice = found;
                            break;
                        }
                    }
                    
                    // If no preferred voice with localService, try without localService restriction
                    if (!selectedVoice) {
                        for (const preferred of preferredVoices) {
                            const found = voices.find(voice => 
                                voice.name.includes(preferred)
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
            
            const statusMap = {
                'available': 'available',
                'occupied': 'occupied',
                'maintenance': 'under maintenance'
            };
            
            let message = '';
            if (oldStatus && newStatus && oldStatus !== newStatus) {
                const newStatusText = statusMap[newStatus.toLowerCase()] || newStatus;
                
                // Create clear announcements for each status change
                if (newStatus.toLowerCase() === 'available') {
                    message = ` ${bayNumber} is now available`;
                } else if (newStatus.toLowerCase() === 'occupied') {
                    message = ` ${bayNumber} is now occupied`;
                    if (username) {
                        message += ` by ${username}`;
                    }
                    if (ticketId) {
                        message += ` with ticket ${ticketId}`;
                    }
                } else if (newStatus.toLowerCase() === 'maintenance') {
                    message = ` ${bayNumber} is now under maintenance`;
                }
                
                // Debug log to help troubleshoot
                console.log('Bay announcement:', message, {bayNumber, oldStatus, newStatus, ticketId, username});
                
                speak(message);
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
