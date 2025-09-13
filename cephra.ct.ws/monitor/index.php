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
    <link rel="icon" type="image/png" href="../images/MONU.png" />
    <link rel="apple-touch-icon" sizes="180x180" href="../images/MONU.png" />
    <link rel="apple-touch-icon" sizes="152x152" href="../images/MONU.png" />
    <link rel="apple-touch-icon" sizes="167x167" href="../images/MONU.png" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
    <meta name="apple-mobile-web-app-title" content="Monitor" />
    <meta name="theme-color" content="#0b1e29" />
    <style>
        :root {
            --bg:#0b1e29; --panel:#0e2230; --card:#122938; --text:#e7f6fa; --muted:#9fb6bf; --accent:#9adcf0;
            --avail:#0c3; --availText:#041; --occ:#3cf; --occText:#002; --maint:#f55; --maintText:#200;
        }
        .light { --bg:#f6fbfd; --panel:#ffffff; --card:#f1f7fa; --text:#0b1e29; --muted:#557; --accent:#0b7fa2; }
        body { font-family: Arial, sans-serif; margin: 0; padding: 12px; background:var(--bg); color:var(--text); transition: background .2s,color .2s; }
        h1 { margin: 0 0 12px; font-size: 22px; display:flex; align-items:center; gap:8px; flex-wrap: wrap; }
        .logo { width:32px; height:32px; border-radius:4px; overflow:hidden; display:inline-block; background:transparent; }
        .logo img { width:100%; height:100%; object-fit:contain; object-position:center; display:block; background:transparent; }
        .grid { display: grid; grid-template-columns: repeat(3, 1fr); grid-template-rows: repeat(auto-fit, minmax(200px, auto)); gap: 12px; }
        .bay { background:var(--card); border-radius: 12px; padding: 12px; border:1px solid rgba(255,255,255,0.08); min-width: 0; height: 200px; display: flex; flex-direction: column; justify-content: space-between; }
        .bay h3 { margin: 0 0 8px; font-size: 18px; color:var(--accent); word-break: break-word; }
        .badge { display:inline-block; padding:6px 10px; border-radius: 999px; font-size: 13px; }
        .available { background:var(--avail); color:var(--availText); }
        .occupied { background:var(--occ); color:var(--occText); }
        .maintenance { background:var(--maint); color:var(--maintText); }
        .row { margin-top: 16px; display:flex; gap:12px; align-items:flex-start; flex-wrap: wrap; }
        .panel { background:var(--panel); border:1px solid rgba(255,255,255,0.08); border-radius:12px; padding:12px; flex:1 1 320px; min-width: 0; }
        table { width:100%; border-collapse: collapse; }
        th, td { text-align:left; padding:8px; border-bottom:1px solid rgba(255,255,255,0.06); font-size: 14px; }
        th { color:var(--accent); }
        .muted { color:var(--muted); font-size: 12px; }
        .ts { font-size: 12px; color:var(--muted); margin-left:8px; }
        .toolbar { margin-left:auto; display:flex; gap:8px; align-items:center; }
        .btn { background:transparent; color:var(--text); border:1px solid rgba(255,255,255,0.25); border-radius:8px; padding:6px 10px; cursor:pointer; font-size:12px; }
        .pager { display:flex; gap:6px; align-items:center; margin-top:8px; flex-wrap: wrap; }
        .pager .btn { padding:4px 8px; }
        
        /* Fullscreen styles */
        .fullscreen-mode { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; z-index: 1000; background: var(--bg); padding: 20px; overflow-y: auto; }
        .fullscreen-mode h1 { margin-bottom: 20px; }
        .fullscreen-mode .row { display: none; }
        .fullscreen-mode .grid { grid-template-columns: repeat(3, 1fr); grid-template-rows: repeat(auto-fit, minmax(250px, auto)); gap: 20px; }
        .fullscreen-mode .bay { padding: 20px; font-size: 16px; height: 250px; }
        .fullscreen-mode .bay h3 { font-size: 24px; margin-bottom: 12px; }
        .fullscreen-mode .badge { padding: 10px 16px; font-size: 16px; }
        .fullscreen-mode .muted { font-size: 14px; margin-top: 10px; }
        
        @media (max-width: 420px) {
            body { padding: 8px; }
            .grid { gap: 10px; grid-template-columns: 1fr; grid-template-rows: repeat(auto-fit, minmax(180px, auto)); }
            .bay { padding:10px; height: 180px; }
            .bay h3 { font-size: 16px; }
            th, td { font-size: 13px; }
        }
        @media (min-width: 421px) and (max-width: 640px) {
            .grid { grid-template-columns: repeat(2, 1fr); grid-template-rows: repeat(auto-fit, minmax(190px, auto)); }
            .bay { height: 190px; }
        }
        @media (min-width: 641px) and (max-width: 1024px) {
            .grid { grid-template-columns: repeat(3, 1fr); grid-template-rows: repeat(auto-fit, minmax(200px, auto)); }
            .bay { height: 200px; }
        }
        @media (min-width: 1025px) {
            .grid { grid-template-columns: repeat(4, 1fr); grid-template-rows: repeat(auto-fit, minmax(200px, auto)); }
            .bay { height: 200px; }
        }
    </style>
</head>
<body>
    <h1>
        <span class="logo"><img src="../images/MONU.png" alt="Cephra" /></span>
        Cephra Live Monitor <span id="ts" class="ts"></span>
        <div class="toolbar">
            <button class="btn" id="fullscreenBtn">Fullscreen Bays</button>
            <button class="btn" id="themeBtn">Toggle Theme</button>
            <label class="muted"><input type="checkbox" id="soundChk" /> Sound alerts</label>
            <label class="muted"><input type="checkbox" id="waitingSoundChk" /> Waiting alerts</label>
            <label class="muted"><input type="checkbox" id="ttsChk" /> Voice announcements</label>
            <button class="btn" id="installBtn" style="display:none;">Install</button>
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

    <audio id="beep" preload="auto">
        <source src="data:audio/wav;base64,UklGRiQAAABXQVZFZm10IBAAAAABAAEAESsAACJWAAACABYBAGFhYWFhAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" type="audio/wav">
    </audio>
    
    <audio id="waitingSound" preload="auto">
        <source src="../../AUDIO/WAITING.wav" type="audio/wav">
        <source src="../../AUDIO/WAITING.mp3" type="audio/mpeg">
    </audio>

    <script>
        // PWA install and SW registration
        let deferredPrompt;
        window.addEventListener('beforeinstallprompt', (e) => {
            e.preventDefault();
            deferredPrompt = e;
            const btn = document.getElementById('installBtn');
            btn.style.display = 'inline-block';
            btn.onclick = async () => {
                btn.style.display = 'none';
                deferredPrompt.prompt();
                await deferredPrompt.userChoice.catch(()=>{});
                deferredPrompt = null;
            };
        });
        if ('serviceWorker' in navigator) {
            navigator.serviceWorker.register('../sw.js').catch(()=>{});
        }

        let currentQueue = [];
        let lastBays = {};
        let lastQueueCount = 0;
        let lastQueueTopTicket = '';
        let page = 1;
        const pageSize = 10;

        function setTheme(light) {
            document.body.classList.toggle('light', !!light);
        }

        document.getElementById('themeBtn').onclick = () => setTheme(!document.body.classList.contains('light'));
        
        // Fullscreen functionality
        let isFullscreen = false;
        document.getElementById('fullscreenBtn').onclick = () => {
            isFullscreen = !isFullscreen;
            document.body.classList.toggle('fullscreen-mode', isFullscreen);
            document.getElementById('fullscreenBtn').textContent = isFullscreen ? 'Exit Fullscreen' : 'Fullscreen Bays';
        };

        async function fetchSnapshot() {
            try {
                const res = await fetch('../api/mobile.php?action=monitor', { cache: 'no-store' });
                const data = await res.json();
                if (data && !data.error) {
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
            wrap.innerHTML = bays.map(b => {
                const ticket = b.current_ticket_id || '';
                const user = b.current_username || '';
                const type = b.bay_type ? ` <small class=\"muted\">(${b.bay_type})</small>` : '';
                return `
                <div class="bay">
                    <h3>${b.bay_number}${type}</h3>
                    <div>${statusBadge(b.status)}</div>
                    <div class="muted" style="margin-top:6px;">Ticket: ${ticket || '-'} | User: ${user || '-'}</div>
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

        function handleAlerts(data) {
            const bays = data.bays || [];
            let changeDetected = false;
            const currentQueue = data.queue || [];
            
            // Check for bay status changes and announce them
            bays.forEach(b => {
                const key = b.bay_number;
                const prev = lastBays[key];
                const now = (b.status || '') + '|' + (b.current_ticket_id || '') + '|' + (b.current_username || '');
                
                if (prev !== now) {
                    changeDetected = true;
                    
                    // Parse previous state for TTS announcement
                    let prevStatus = '';
                    let newStatus = b.status || '';
                    let newTicketId = b.current_ticket_id || '';
                    let newUsername = b.current_username || '';
                    
                    if (prev) {
                        const prevParts = prev.split('|');
                        prevStatus = prevParts[0] || '';
                    }
                    
                    // Only announce if we have a previous state and status actually changed
                    if (prev && prevStatus !== newStatus) {
                        announceBayChange(b.bay_number, prevStatus, newStatus, newTicketId, newUsername);
                    }
                }
                lastBays[key] = now;
            });
            
            // Check for queue changes
            const prevTop = currentQueue[0]?.ticket_id;
            const newTop = (data.queue || [])[0]?.ticket_id;
            if (prevTop && newTop && prevTop !== newTop) changeDetected = true;
            
            // Handle waiting queue sound alerts
            const currentQueueCount = currentQueue.length;
            if (document.getElementById('waitingSoundChk').checked && currentQueueCount > 0 && currentQueueCount > lastQueueCount) {
                playWaitingSound();
            }
            
            // Handle waiting queue TTS announcements
            if (currentQueueCount !== lastQueueCount || (newTop && newTop !== lastQueueTopTicket)) {
                announceQueueChange(currentQueueCount, newTop, currentQueue[0]?.username);
            }
            
            if (document.getElementById('soundChk').checked && changeDetected) beep();
            
            lastQueueCount = currentQueueCount;
            lastQueueTopTicket = newTop;
        }

        function beep() {
            const el = document.getElementById('beep');
            try { el.currentTime = 0; el.play(); } catch (e) {}
        }
        
        function playWaitingSound() {
            const el = document.getElementById('waitingSound');
            try { el.currentTime = 0; el.play(); } catch (e) {}
        }
        
        function speak(text) {
            if (document.getElementById('ttsChk').checked && 'speechSynthesis' in window) {
                // Stop any current speech
                speechSynthesis.cancel();
                
                const utterance = new SpeechSynthesisUtterance(text);
                utterance.rate = 0.8;
                utterance.pitch = 1.0;
                utterance.volume = 0.7;
                
                // Try to use a female voice if available
                const voices = speechSynthesis.getVoices();
                const femaleVoice = voices.find(voice => 
                    voice.name.includes('Female') || 
                    voice.name.includes('female') || 
                    voice.name.includes('Zira') ||
                    voice.name.includes('Susan') ||
                    voice.name.includes('Karen')
                );
                if (femaleVoice) {
                    utterance.voice = femaleVoice;
                }
                
                speechSynthesis.speak(utterance);
            }
        }
        
        function announceBayChange(bayNumber, oldStatus, newStatus, ticketId, username) {
            const statusMap = {
                'available': 'available',
                'occupied': 'occupied',
                'maintenance': 'under maintenance'
            };
            
            let message = '';
            if (oldStatus && newStatus && oldStatus !== newStatus) {
                const newStatusText = statusMap[newStatus.toLowerCase()] || newStatus;
                message = `Bay ${bayNumber} is now ${newStatusText}`;
                
                if (newStatus.toLowerCase() === 'occupied' && username) {
                    message += ` by ${username}`;
                }
                if (ticketId && newStatus.toLowerCase() !== 'available') {
                    message += ` with ticket ${ticketId}`;
                }
                
                // Debug log to help troubleshoot
                console.log('Bay announcement:', message, {bayNumber, oldStatus, newStatus, ticketId, username});
                
                speak(message);
            }
        }
        
        function announceQueueChange(queueCount, topTicket, username) {
            if (queueCount > 0) {
                if (queueCount === 1) {
                    speak(`One customer is waiting for service`);
                } else {
                    speak(`${queueCount} customers are waiting for service`);
                }
            } else if (queueCount === 0 && lastQueueCount > 0) {
                speak('Waiting queue is now empty');
            }
        }

        fetchSnapshot();
        setInterval(fetchSnapshot, 3000);
    </script>
</body>
</html>
