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
        .available { background:var(--avail); color:var(--availText); font-size: 16px; font-weight: bold; padding: 8px 12px; }
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
        .fullscreen-mode .available { font-size: 20px; font-weight: bold; padding: 12px 18px; }
        .fullscreen-mode .muted { font-size: 14px; margin-top: 10px; }
        
        @media (max-width: 420px) {
            body { padding: 8px; }
            .grid { gap: 10px; grid-template-columns: 1fr; grid-template-rows: repeat(auto-fit, minmax(180px, auto)); }
            .bay { padding:10px; height: 180px; }
            .bay h3 { font-size: 16px; }
            .available { font-size: 14px; font-weight: bold; padding: 6px 10px; }
            th, td { font-size: 13px; }
        }
        @media (min-width: 421px) and (max-width: 640px) {
            .grid { grid-template-columns: repeat(2, 1fr); grid-template-rows: repeat(auto-fit, minmax(190px, auto)); }
            .bay { height: 190px; }
            .available { font-size: 15px; font-weight: bold; padding: 7px 11px; }
        }
        @media (min-width: 641px) and (max-width: 1024px) {
            .grid { grid-template-columns: repeat(3, 1fr); grid-template-rows: repeat(auto-fit, minmax(200px, auto)); }
            .bay { height: 200px; }
            .available { font-size: 16px; font-weight: bold; padding: 8px 12px; }
        }
        @media (min-width: 1025px) {
            .grid { grid-template-columns: repeat(4, 1fr); grid-template-rows: repeat(auto-fit, minmax(200px, auto)); }
            .bay { height: 200px; }
            .available { font-size: 16px; font-weight: bold; padding: 8px 12px; }
        }
    </style>
</head>
<body>
    <h1>
        <span class="logo"><img src="../Admin/images/MONU.png" alt="Cephra" /></span>
        Cephra Live Monitor <span id="ts" class="ts"></span>
        <div class="toolbar">
            <button class="btn" id="fullscreenBtn">Fullscreen Bays</button>
            <button class="btn" id="themeBtn">Toggle Theme</button>
            <label class="muted"><input type="checkbox" id="announcerChk" checked /> Bay Announcer</label>
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
            navigator.serviceWorker.register('../User/sw.js').catch(()=>{});
        }

        let currentQueue = [];
        let lastBays = {};
        let lastQueueTickets = new Set(); // Track tickets that were already announced
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
                const res = await fetch('api/monitor.php', { cache: 'no-store' });
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
            const currentQueue = data.queue || [];
            
            // Check for bay status changes and announce them
            bays.forEach(b => {
                const key = b.bay_number;
                const prev = lastBays[key];
                const now = (b.status || '') + '|' + (b.current_ticket_id || '') + '|' + (b.current_username || '');
                
                if (prev !== now) {
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
            
            // Check for new waiting tickets
            currentQueue.forEach(ticket => {
                const ticketId = ticket.ticket_id;
                if (ticketId && !lastQueueTickets.has(ticketId)) {
                    // New ticket in waiting queue
                    announceWaitingTicket(ticketId);
                    lastQueueTickets.add(ticketId);
                }
            });
            
            // Clean up tickets that are no longer in waiting queue
            const currentTicketIds = new Set(currentQueue.map(t => t.ticket_id).filter(id => id));
            for (let ticketId of lastQueueTickets) {
                if (!currentTicketIds.has(ticketId)) {
                    lastQueueTickets.delete(ticketId);
                }
            }
        }

        function speak(text) {
            if (document.getElementById('announcerChk').checked && 'speechSynthesis' in window) {
                // Stop any current speech
                speechSynthesis.cancel();
                
                const utterance = new SpeechSynthesisUtterance(text);
                utterance.rate = 0.8;
                utterance.pitch = 1.0;
                utterance.volume = 0.8;
                
                // Try to use a female voice if available
                const voices = speechSynthesis.getVoices();
                const femaleVoice = voices.find(voice => 
                    voice.name.includes('Female') || 
                    voice.name.includes('female') || 
                    voice.name.includes('Zira') ||
                    voice.name.includes('Susan') ||
                    voice.name.includes('Karen') ||
                    voice.name.includes('Microsoft Zira')
                );
                if (femaleVoice) {
                    utterance.voice = femaleVoice;
                }
                
                speechSynthesis.speak(utterance);
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
                    message = `Bay ${bayNumber} is now available`;
                } else if (newStatus.toLowerCase() === 'occupied') {
                    message = `Bay ${bayNumber} is now occupied`;
                    if (username) {
                        message += ` by ${username}`;
                    }
                    if (ticketId) {
                        message += ` with ticket ${ticketId}`;
                    }
                } else if (newStatus.toLowerCase() === 'maintenance') {
                    message = `Bay ${bayNumber} is now under maintenance`;
                }
                
                // Debug log to help troubleshoot
                console.log('Bay announcement:', message, {bayNumber, oldStatus, newStatus, ticketId, username});
                
                speak(message);
            }
        }
        
        function announceWaitingTicket(ticketId) {
            // Simple announcement: just the ticket number
            const message = `Ticket ${ticketId}`;
            
            // Debug log to help troubleshoot
            console.log('Waiting ticket announcement:', message, {ticketId});
            
            speak(message);
        }
        

        fetchSnapshot();
        setInterval(fetchSnapshot, 3000);
    </script>
</body>
</html>
