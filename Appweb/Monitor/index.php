<?php
// New Queue Monitor UI for Cephra
// File: Appweb/Monitor/monitor.php
// This is a new visual design and lightweight frontend logic for previewing the queue monitor.
// Start session if not already started and detect logged-in user
if (session_status() === PHP_SESSION_NONE) {
  session_start();
}
$showClose = isset($_SESSION['username']) && !empty($_SESSION['username']);

// Attempt to load live data from the database to populate the monitor
// Default to empty arrays so the client can use its own simulated data if desired
$baysJson = '[]';
$waitingsJson = '[]';
try {
  // Use __DIR__ so includes are robust regardless of cwd
  require_once __DIR__ . '/../Admin/config/database.php';
  $db = new Database();
  $conn = $db->getConnection();
  if ($conn) {
    // Fetch bays (same logic as api/monitor.php but normalize fields for the frontend)
    $stmt = $conn->query("SELECT
            cb.bay_number,
            cb.bay_type,
            CASE
                WHEN cb.status = 'Available'
                     AND EXISTS (SELECT 1 FROM charging_grid cg
                                 WHERE cg.bay_number = cb.bay_number
                                   AND cg.ticket_id IS NOT NULL)
                THEN 'Occupied'
                ELSE cb.status
            END AS status,
            cb.current_username,
            COALESCE(cb.current_ticket_id, (SELECT cg.ticket_id FROM charging_grid cg WHERE cg.bay_number = cb.bay_number LIMIT 1)) AS current_ticket_id,
            cb.start_time,
            u.plate_number
        FROM charging_bays cb
        LEFT JOIN users u ON cb.current_username = u.username
        ORDER BY cb.bay_number");
    $bays = $stmt->fetchAll(PDO::FETCH_ASSOC);

    // Map DB rows into a simple structure but do not fabricate any fields.
    $jsBays = array_map(function($b){
      // keep bay_number as-is; frontend will interpret it
      return [
        'bay' => $b['bay_number'],
        'type' => $b['bay_type'] ?? 'Normal',
        'status' => $b['status'] ?? 'Available',
        'ticket' => $b['current_ticket_id'] ?? '',
        'user' => $b['current_username'] ?? '',
        'plate' => $b['plate_number'] ?? '',
        'last_updated' => $b['start_time'] ?? ''
      ];
    }, $bays ?: []);

    // Fetch waiting queue with plate if available
    $stmt = $conn->query("SELECT q.ticket_id, q.username, q.service_type, q.created_at, u.plate_number FROM queue_tickets q LEFT JOIN users u ON q.username = u.username WHERE q.status = 'Waiting' ORDER BY q.created_at ASC");
    $queue = $stmt->fetchAll(PDO::FETCH_ASSOC);
    // Do not synthesize any missing fields; pass DB values (or empty strings) to JS
    $jsWaitings = array_map(function($q){
      return [
        'ticket' => $q['ticket_id'] ?? '',
        'plate' => $q['plate_number'] ?? '',
        'user' => $q['username'] ?? '',
        'service' => $q['service_type'] ?? '',
        'time' => $q['created_at'] ?? ''
      ];
    }, $queue ?: []);

    $baysJson = json_encode($jsBays, JSON_UNESCAPED_SLASHES | JSON_UNESCAPED_UNICODE);
    $waitingsJson = json_encode($jsWaitings, JSON_UNESCAPED_SLASHES | JSON_UNESCAPED_UNICODE);
  }
} catch (Exception $e) {
  // On error keep arrays empty so client-side retains control over simulated values
  $baysJson = '[]';
  $waitingsJson = '[]';
}
?>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" />
  <title>Cephra — Queue Monitor (Preview)</title>
  <link rel="icon" href="../Admin/images/MONU.png">
  <style>
    /* Refreshed light-blue theme and improved dark mode */
    :root{
      --bg: #eef9ff;               /* light subtle blue */
      --surface: #ffffff;
      --muted: #4b5563;
      --accent: #0ea5ff;           /* bright sky blue */
      --accent-2: #0284c7;         /* deeper blue */
      --accent-3: #7dd3fc;         /* soft highlight */
      --danger: #ef4444;
      --glass: rgba(255,255,255,0.7);
      --shadow: 0 12px 30px rgba(2,6,23,0.06);
      --panel-border: rgba(2,6,23,0.06);
      --card-border: rgba(2,6,23,0.08);
      --grid-bg: linear-gradient(180deg, rgba(14,165,255,0.02), rgba(255,255,255,0));
      --table-row: rgba(2,6,23,0.02);
      --radius:14px;
      --text: #02263b;             /* dark slate for good contrast */
    }

    /* Dark mode variables: used when body has 'dark' class */
    body.dark{
      --bg: linear-gradient(180deg,#031022,#042035);
      --surface: #071226;
      --muted: #9fb3c8;
      --accent: #38bdf8;
      --accent-2: #0ea5ff;
      --accent-3: rgba(255,255,255,0.04);
      --glass: rgba(255,255,255,0.03);
      --shadow: 0 18px 40px rgba(2,6,23,0.6);
      --panel-border: rgba(255,255,255,0.04);
      --card-border: rgba(255,255,255,0.06);
      --grid-bg: linear-gradient(180deg, rgba(255,255,255,0.02), rgba(255,255,255,0.01));
      --table-row: rgba(255,255,255,0.02);
      --text: #e6f6ff;
    }

  html,body{height:100%;margin:0;font-family:Inter,Segoe UI,Arial,sans-serif;background:var(--bg);color:var(--text);-webkit-font-smoothing:antialiased;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%}
  /* make sizing predictable */
  *, *::before, *::after { box-sizing: border-box; }
    body{padding:20px;box-sizing:border-box;transition:background .25s,color .25s;overflow-x:hidden}
    
    /* Improve scrolling on mobile */
    *{-webkit-overflow-scrolling:touch}
    
    /* Better touch targets for mobile */
    input, select, button, .toggle-switch{min-height:44px;touch-action:manipulation}
    
    /* Force responsive behavior */
    *{max-width:100%;box-sizing:border-box}
    html{overflow-x:hidden}
    body{width:100%;max-width:100vw;overflow-x:hidden}
    main{width:100%;max-width:100%}
    .panel{width:100%;max-width:100%}
    
    /* Ensure no horizontal overflow */
    #controls{max-width:100%;overflow-x:auto}
    #queueGrid{max-width:100%}
    .table-wrap{max-width:100%;overflow-x:auto}
    header{display:flex;align-items:center;gap:12px;margin-bottom:18px}
    .brand{display:flex;flex-direction:column}
    h1{font-size:20px;margin:0}
    p.lead{margin:0;color:var(--muted);font-size:13px}

    main{display:flex;flex-direction:column;gap:18px}

    /* Panel base */
  .panel{background:var(--surface);border-radius:var(--radius);box-shadow:var(--shadow);padding:14px;border:1px solid var(--panel-border)}

    /* Controls redesigned: force a single horizontal row with horizontal scrolling on small screens */
    #controls{
      display:flex;
      flex-direction:row;
      gap:8px;
      width:100%;
      max-width:100%;
      margin:0 auto;
      align-items:center;
      justify-content:flex-start;
      padding:4px 6px;
      overflow-x:auto; /* allow horizontal scroll only if absolutely necessary */
      -webkit-overflow-scrolling:touch;
      flex-wrap:nowrap; /* ensure single row */
      position:relative;
    }

    /* each control card remains compact and inline */
    .ctrl{
      display:flex;
      align-items:center;
      gap:6px;
      padding:6px 8px;
      border-radius:12px;
      background: linear-gradient(180deg, rgba(255,255,255,0.9), rgba(255,255,255,0.7));
      border:1px solid var(--panel-border);
      box-shadow: 0 6px 16px rgba(2,6,23,0.04);
      min-height:42px;
      justify-content:space-between; /* label left, control right */
      flex:1 1 0; /* allow controls to share available width and shrink if needed */
      min-width:0; /* allow full shrink to prevent page overflow */
      max-width:none;
      white-space:nowrap; /* keep label + inline elements on one line */
      overflow:hidden;text-overflow:ellipsis
    }
    .ctrl > label{font-size:13px;color:var(--muted);flex:0 0 auto;min-width:36px;max-width:30%;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}
    .ctrl .control-body{display:flex;flex:1 1 auto;align-items:center;gap:8px;justify-content:flex-end;min-width:0}

  /* Announcer control should not shrink; keep label visible */
  .ctrl-announcer{flex:0 0 auto}
  .ctrl-announcer > label{min-width:110px;max-width:160px}

  /* If a control has no label (control-body is the only child), center its content */
  .ctrl > .control-body:only-child{margin-left:auto;margin-right:auto;justify-content:center}

    /* make sliders and controls scale correctly but keep them compact */
  /* sliders should use the available space inside the control body */
  .control-body input[type="range"]{width:100%;max-width:160px;min-width:48px}
  .control-body select{min-width:56px}

  /* reduce button padding so buttons don't force overflow */
  button.action{padding:8px 10px;font-size:13px}
  button.ghost{padding:6px 8px;font-size:13px}

    /* small fade to indicate horizontal scroll when overflowing */
    #controls::after{
      content:'';position:absolute;right:8px;top:0;bottom:0;width:40px;background:linear-gradient(90deg,transparent,var(--bg));pointer-events:none;border-radius:0 12px 12px 0;display:block
    }

    /* CSS fallback to center controls without a label (for browsers without :has support) */
    .ctrl.centered > .control-body{margin-left:auto;margin-right:auto;justify-content:center}

    /* Slightly darker card for dark mode */
    body.dark .ctrl{
      background: linear-gradient(180deg, rgba(255,255,255,0.02), rgba(255,255,255,0.01));
      border:1px solid rgba(255,255,255,0.03);
      box-shadow: 0 8px 20px rgba(2,6,23,0.3);
    }

    /* Toggle switch for Bay announcer: default neutral surface and strong active state */
    .ctrl .control-body .toggle-switch,
    .toggle-switch{
      width:46px;
      height:15px;
      border-radius:px;
      background:var(--surface);
      border:1px solid var(--panel-border);
      position:relative;
      cursor:pointer;
      display:inline-block;
      vertical-align:middle;
      transition:all .18s ease;
      box-shadow: 0 6px 14px rgba(2,6,23,0.04);
      padding:3px;
      touch-action:manipulation;
      min-height:28px;
      min-width:44px;
    }
    .toggle-switch .knob{
      position:absolute;top:3px;left:3px;width:20px;height:20px;border-radius:50%;background:white;box-shadow:0 6px 16px rgba(2,6,23,0.08);transition:all .18s ease}
    /* Active state: bright gradient, no border, subtle glow */
    .ctrl .control-body .toggle-switch.active,
    .toggle-switch.active{
      background:linear-gradient(90deg,var(--accent-2),var(--accent));
      border-color:transparent;
      box-shadow: 0 10px 30px rgba(14,165,255,0.18), 0 3px 8px rgba(2,6,23,0.06) inset;
    }
    .toggle-switch.active .knob{transform:translateX(20px)}

  /* Compact icon labels - reserve space so toggles don't overlap labels */
  .label-icon{display:inline-flex;align-items:center;gap:8px;min-width:84px;max-width:40%;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}
  .label-icon svg{opacity:0.9;flex:0 0 18px}
  /* ensure toggle switches don't visually intrude into the label area */
  .ctrl .toggle-switch{margin-left:6px}

    /* Range (sliders) modern look */
    input[type=range]{-webkit-appearance:none;height:8px;border-radius:8px;background:linear-gradient(90deg,var(--accent-3),transparent);outline:none}
    input[type=range]::-webkit-slider-thumb{-webkit-appearance:none;width:18px;height:18px;border-radius:50%;background:var(--accent);box-shadow:0 4px 10px rgba(14,165,255,0.25);border:2px solid white}

    /* Buttons */
  button.action{background:linear-gradient(90deg,var(--accent),var(--accent-2));border:none;color:white;padding:10px 16px;border-radius:12px;cursor:pointer;font-weight:600;box-shadow:0 10px 22px rgba(2,6,23,0.08);display:inline-flex;align-items:center;gap:8px;transition:all 0.2s ease;min-height:44px;touch-action:manipulation}
  button.ghost{background:transparent;border:1px solid var(--panel-border);padding:9px 12px;border-radius:12px;cursor:pointer;color:var(--text);display:inline-flex;align-items:center;gap:8px;transition:all 0.2s ease;min-height:44px;touch-action:manipulation}
  
  /* Button hover and active states for better mobile interaction */
  button.action:hover{transform:translateY(-2px);box-shadow:0 12px 28px rgba(2,6,23,0.12)}
  button.action:active{transform:translateY(0);box-shadow:0 6px 16px rgba(2,6,23,0.08)}
  button.ghost:hover{background:var(--accent-3);border-color:var(--accent)}
  button.ghost:active{background:var(--accent-3);transform:scale(0.98)}

    select{background:var(--surface);border:1px solid rgba(2,6,23,0.06);padding:8px 10px;border-radius:8px;color:var(--text)}

    /* Queue grid - bigger, more prominent */
  #queueGrid{display:grid;grid-template-columns:repeat(4,1fr);grid-template-rows:repeat(2,240px);gap:18px;padding:12px;border-radius:12px;background:var(--grid-bg);border:1px solid var(--panel-border);overflow:hidden;box-sizing:border-box}
  .card{border-radius:14px;padding:16px;background:linear-gradient(180deg,var(--surface),rgba(255,255,255,0.02));display:flex;flex-direction:column;justify-content:space-between;border:1px solid var(--card-border);box-shadow:0 8px 22px rgba(2,6,23,0.05);transition:transform .18s ease,box-shadow .18s ease}
  .card:hover{transform:translateY(-6px) scale(1.01);box-shadow:0 18px 40px rgba(2,6,23,0.09)}
    .card h3{margin:0;font-size:18px;color:var(--accent-2)}
    .status{font-weight:800;font-size:20px}
    .meta{display:flex;justify-content:space-between;color:var(--muted);font-size:14px}

  /* Make ticket and plate values more prominent on the cards */
  .card .meta strong{font-size:16px;color:var(--text);font-weight:800}
  /* place ticket and plate on separate lines for clarity */
  .card .meta{display:flex;flex-direction:column;gap:6px}
  .card .meta .line{display:flex;align-items:center;gap:6px}
  .card .meta .plate{font-size:15px;color:var(--accent-2);letter-spacing:0.6px}
  body.tv .card .meta strong{font-size:26px}
  body.tv .card .meta .plate{font-size:22px}

    /* Status badges - Consistent sizing */
  .badge{
    display:inline-block;
    padding:8px 12px;
    border-radius:999px;
    color:white;
    font-weight:700;
    font-size:13px;
    transition:all .18s ease;
    box-shadow:0 6px 18px rgba(2,6,23,0.06);
    min-width:100px;
    text-align:center;
    white-space:nowrap;
    box-sizing:border-box;
  }
  .badge.occupied{background:linear-gradient(90deg,#ff8a65,#ff5252);box-shadow:0 8px 24px rgba(255,82,82,0.12)}
  .badge.available{background:linear-gradient(90deg,#34d399,#10b981);box-shadow:0 8px 24px rgba(16,185,129,0.12)}
  .badge.maintenance{background:linear-gradient(90deg,#f59e0b,#f97316);box-shadow:0 8px 24px rgba(249,115,22,0.10)}
  .badge.unknown{background:linear-gradient(90deg,#94a3b8,#64748b);box-shadow:0 8px 24px rgba(100,116,139,0.06)}

    /* Waitings table */
  table{width:100%;border-collapse:collapse;font-size:14px}
  th,td{padding:12px;border-bottom:1px solid var(--panel-border);text-align:left}
    th{background:transparent;color:var(--muted);font-weight:700}
  tbody tr:nth-child(even){background:var(--table-row)}

    /* Responsive behaviour - More aggressive mobile fixes */
    @media (max-width:1000px){
      #queueGrid{grid-template-columns:repeat(2,1fr);grid-template-rows:repeat(4,220px)}
    }
    
    @media (max-width:820px){
      /* Keep single-row behavior but make controls more compact on narrower screens */
      #controls{gap:6px;padding:4px 6px;}
      .ctrl{padding:5px 6px;min-width:48px;flex:1 1 0}
      .ctrl > label{min-width:30px;max-width:32%}
      .ctrl .control-body{justify-content:flex-end}
      .control-body input[type="range"]{min-width:28px;max-width:120px}
    }

    @media (max-width:768px){
      /* Mobile landscape and smaller tablets */
      body{padding:8px !important;margin:0 !important;}
      .panel{padding:8px !important;margin:0 0 12px 0 !important;}
      #queueGrid{grid-template-columns:repeat(2,1fr) !important;grid-template-rows:repeat(4,160px) !important;gap:8px !important;padding:8px !important;}
      .card{padding:8px !important;}
      .card h3{font-size:14px !important;margin:0 0 4px 0 !important;}
      .status{font-size:16px !important;}
      .badge{padding:4px 8px !important;font-size:11px !important;min-width:80px !important;}
      
      /* Make controls more mobile-friendly */
      #controls{flex-wrap:wrap !important;gap:6px !important;padding:6px !important;}
      .ctrl{min-width:100px !important;flex:1 1 calc(50% - 3px) !important;padding:4px 6px !important;}
      .ctrl-announcer{flex:1 1 100% !important;}
      .ctrl-announcer > label{min-width:auto !important;max-width:none !important;}
      
      /* Header adjustments */
      header{margin-bottom:12px !important;padding:0 !important;}
      h1{font-size:18px !important;}
      
      /* Table adjustments */
      .table-wrap{max-height:200px !important;}
      .waitings-table{font-size:11px !important;}
      .waitings-table th, .waitings-table td{padding:6px 3px !important;}
    }

    @media (max-width:600px){
      /* Mobile portrait - More aggressive fixes */
      body{padding:4px !important;margin:0 !important;overflow-x:hidden !important;}
      .panel{padding:6px !important;margin:0 0 8px 0 !important;}
      #queueGrid{grid-template-columns:1fr !important;grid-template-rows:repeat(8,120px) !important;gap:6px !important;padding:6px !important;}
      .card{padding:6px !important;}
      .card h3{font-size:12px !important;margin:0 0 2px 0 !important;}
      .status{font-size:14px !important;}
      .badge{padding:3px 6px !important;font-size:10px !important;min-width:70px !important;}
      
      /* Stack controls vertically on very small screens */
      #controls{flex-direction:column !important;gap:6px !important;padding:4px !important;}
      .ctrl{flex:1 1 auto !important;min-width:auto !important;width:100% !important;padding:4px 6px !important;}
      .ctrl > label{min-width:auto !important;max-width:none !important;flex:0 0 auto !important;font-size:11px !important;}
      .ctrl .control-body{justify-content:flex-start !important;}
      
      /* Header adjustments */
      header{margin-bottom:8px !important;flex-direction:column !important;align-items:flex-start !important;gap:4px !important;}
      h1{font-size:16px !important;}
      
      /* Make table more mobile-friendly */
      .table-wrap{max-height:150px !important;overflow-x:auto !important;}
      .waitings-table{font-size:10px !important;min-width:100% !important;}
      .waitings-table th, .waitings-table td{padding:4px 2px !important;white-space:nowrap !important;}
      .waitings-ticket{font-size:11px !important;min-width:60px !important;}
      .waitings-plate{font-size:10px !important;}
      .waitings-user{min-width:80px !important;font-size:10px !important;}
      .service-badge{padding:2px 4px !important;font-size:9px !important;}
      
      /* Adjust card content for mobile */
      .card .meta{font-size:10px !important;}
      .card .meta strong{font-size:11px !important;}
      .card .meta .plate{font-size:10px !important;}
      .card .muted{font-size:9px !important;}
      
      /* Button adjustments */
      button.action, button.ghost{font-size:10px !important;padding:4px 6px !important;min-height:36px !important;}
    }

    @media (max-width:420px){
      /* Very small screens - Maximum compression */
      body{padding:2px !important;margin:0 !important;}
      .panel{padding:4px !important;margin:0 0 6px 0 !important;}
      #queueGrid{grid-template-rows:repeat(8,100px) !important;gap:4px !important;padding:4px !important;}
      .card{padding:4px !important;}
      .card h3{font-size:11px !important;margin:0 0 1px 0 !important;}
      .status{font-size:12px !important;}
      .badge{padding:2px 4px !important;font-size:9px !important;min-width:60px !important;}
      
      /* Controls */
      .ctrl > label{font-size:10px !important;}
      button.action, button.ghost{font-size:9px !important;padding:3px 4px !important;min-height:32px !important;}
      .label-icon svg{display:none !important;}
      
      /* Header */
      header{margin-bottom:4px !important;}
      h1{font-size:14px !important;}
      
      /* Table */
      .table-wrap{max-height:120px !important;}
      .waitings-table{font-size:9px !important;}
      .waitings-table th, .waitings-table td{padding:3px 1px !important;}
      .waitings-ticket{font-size:10px !important;min-width:50px !important;}
      .waitings-plate{font-size:9px !important;}
      .waitings-user{min-width:60px !important;font-size:9px !important;}
      .service-badge{padding:1px 3px !important;font-size:8px !important;}
      
      /* Card content */
      .card .meta{font-size:9px !important;}
      .card .meta strong{font-size:10px !important;}
      .card .meta .plate{font-size:9px !important;}
      .card .muted{font-size:8px !important;}
    }

    /* small helper styles */
    .muted{color:var(--muted)}
    .pill{display:inline-block;padding:6px 10px;border-radius:999px;background:var(--accent-2);color:white;font-size:12px;font-weight:600}

    /* Enhanced waitings table visuals */
    .waitings-table{width:100%;border-collapse:separate;border-spacing:0 10px}
    .waitings-table thead{position:sticky;top:0;background:transparent}
    .waitings-table tbody tr{background:transparent}
    .waitings-row{display:flex;align-items:center;gap:12px;padding:12px;border-radius:12px;background:var(--surface);border:1px solid var(--panel-border);box-shadow:0 6px 18px rgba(2,6,23,0.03)}
    .waitings-cell{padding:0;margin:0;flex:1;min-width:0}
    .waitings-ticket{font-weight:800;font-size:15px;color:var(--text);min-width:100px}
    .waitings-plate{display:inline-flex;align-items:center;gap:8px;font-weight:700;color:var(--accent-2)}
    .waitings-user{display:flex;align-items:center;gap:8px;color:var(--muted);min-width:160px}
    .avatar{width:36px;height:36px;border-radius:50%;background:linear-gradient(90deg,var(--accent-3),transparent);display:inline-flex;align-items:center;justify-content:center;color:var(--accent-2);font-weight:700}
    .service-badge{padding:6px 10px;border-radius:999px;font-weight:700;color:white;font-size:12px}
  /* Fast: red, Normal: blue */
  .service-badge.Fast{background:linear-gradient(90deg,#ef4444,#dc2626)}
  .service-badge.Normal{background:linear-gradient(90deg,#3b82f6,#2563eb)}
    .service-badge.Other{background:linear-gradient(90deg,#94a3b8,#64748b)}
  /* center the Service column */
  thead th:nth-child(4), tbody td:nth-child(4){text-align:center}
    .empty-waitings{padding:28px;text-align:center;color:var(--muted);border-radius:12px;border:1px dashed var(--panel-border);background:linear-gradient(180deg,rgba(255,255,255,0.02),transparent)}

    /* Fullscreen helper */
    .fullscreen{position:fixed;inset:0;z-index:9999;background:var(--bg);padding:22px;overflow:auto}

  /* Center pager under tables */
  #waitingsPager{justify-content:center}
  #waitingsPager .muted{min-width:110px;text-align:center}

  /* TV / fullscreen presentation tweaks - applied when body has .tv */
  body.tv{
    font-size:1.5rem;
    overflow:hidden; /* prevent page vertical scroll in TV mode */
    padding:0; /* remove extra padding so content fits exactly */
    margin:0;
    height:100vh;
    display:flex;
    align-items:center;
    justify-content:center; /* center the main area on the screen */
    background:var(--bg);
  }
  /* Make usernames more prominent in TV mode */
  body.tv .tv-user{font-weight:800;color:var(--text);}
  body.tv h1{font-size:36px}
  /* Layout: two-column split - queue on the left, waitings on the right */
  body.tv main{display:grid;grid-template-columns:2.2fr 1fr;grid-template-rows:1fr;gap:20px;height:95vh;width:95vw;max-width:1900px;transform:scale(1.0);transform-origin:center;overflow:hidden;padding:10px;box-sizing:border-box}
  body.tv .panel{padding:16px;height:100%;overflow:hidden;box-sizing:border-box}
  body.tv #queueGrid{grid-template-columns:repeat(4,minmax(0,1fr));grid-template-rows:repeat(2,1fr);gap:16px;height:100%;overflow:auto;padding:8px;box-sizing:border-box;max-width:100%;width:100%}
  body.tv #queueGrid .card{width:100%;max-width:100%;box-sizing:border-box;overflow:hidden;min-width:0}
  body.tv .card{padding:20px;border-radius:16px;min-width:0;overflow:hidden}
  body.tv .card h3{font-size:24px;margin:0 0 8px 0}
  body.tv .status{font-size:26px}
  body.tv .badge{padding:10px 14px;font-size:16px;min-width:120px}
  body.tv .badge.maintenance{padding:10px 14px;font-size:16px;min-width:120px}
  body.tv table{font-size:22px}
  body.tv th, body.tv td{padding:20px}
  body.tv #controls{display:none !important} /* hide controls in tv mode */
  
  /* Mobile fullscreen mode - responsive TV mode */
  @media (max-width:768px) {
    body.tv{
      font-size:1rem;
      padding:8px;
      align-items:flex-start;
      justify-content:flex-start;
    }
    body.tv main{
      display:flex;
      flex-direction:column;
      gap:12px;
      height:calc(100vh - 16px);
      width:calc(100vw - 16px);
      max-width:none;
      transform:none;
      overflow-y:auto;
      overflow-x:hidden;
      box-sizing:border-box;
    }
    body.tv .panel{
      padding:12px;
      height:auto;
      flex:1;
      min-height:0;
    }
    body.tv #queuePanel{
      flex:2;
    }
    body.tv #waitingsPanel{
      flex:1;
      min-height:200px;
    }
    body.tv #queueGrid{
      grid-template-columns:repeat(2,minmax(0,1fr));
      grid-template-rows:repeat(4,1fr);
      gap:12px;
      height:100%;
      padding:8px;
      box-sizing:border-box;
      overflow:hidden;
      width:100%;
      max-width:100%;
    }
    body.tv #queueGrid .card{
      width:100%;
      max-width:100%;
      box-sizing:border-box;
      overflow:hidden;
      min-width:0;
    }
    body.tv .card{
      padding:12px;
      border-radius:12px;
    }
    body.tv .card h3{font-size:16px}
    body.tv .status{font-size:18px}
    body.tv .badge{padding:6px 10px;font-size:12px;min-width:90px}
    body.tv .badge.maintenance{padding:6px 10px;font-size:12px;min-width:90px}
    body.tv table{font-size:14px}
    body.tv th, body.tv td{padding:8px}
    body.tv h1{font-size:20px}
  }
  
  @media (max-width:600px) {
    body.tv{
      padding:4px;
    }
    body.tv main{
      height:calc(100vh - 8px);
      width:calc(100vw - 8px);
      gap:8px;
      overflow-x:hidden;
      box-sizing:border-box;
    }
    body.tv .panel{
      padding:8px;
    }
    body.tv #queueGrid{
      grid-template-columns:minmax(0,1fr);
      grid-template-rows:repeat(8,1fr);
      gap:8px;
      padding:4px;
      box-sizing:border-box;
      overflow:hidden;
      width:100%;
      max-width:100%;
    }
    body.tv #queueGrid .card{
      width:100%;
      max-width:100%;
      box-sizing:border-box;
      overflow:hidden;
      min-width:0;
    }
    body.tv .card{
      padding:8px;
    }
    body.tv .card h3{font-size:14px}
    body.tv .status{font-size:16px}
    body.tv .badge{padding:4px 8px;font-size:11px;min-width:80px}
    body.tv table{font-size:12px}
    body.tv th, body.tv td{padding:6px}
    body.tv h1{font-size:18px}
  }
  
  @media (max-width:420px) {
    body.tv{
      padding:2px;
    }
    body.tv main{
      height:calc(100vh - 4px);
      width:calc(100vw - 4px);
      gap:4px;
      overflow-x:hidden;
      box-sizing:border-box;
    }
    body.tv .panel{
      padding:4px;
    }
    body.tv #queueGrid{
      grid-template-columns:minmax(0,1fr);
      gap:4px;
      padding:2px;
      box-sizing:border-box;
      overflow:hidden;
      width:100%;
      max-width:100%;
    }
    body.tv #queueGrid .card{
      width:100%;
      max-width:100%;
      box-sizing:border-box;
      overflow:hidden;
      min-width:0;
    }
    body.tv .card{
      padding:4px;
    }
    body.tv .card h3{font-size:12px}
    body.tv .status{font-size:14px}
    body.tv .badge{padding:3px 6px;font-size:10px;min-width:70px}
    body.tv table{font-size:11px}
    body.tv th, body.tv td{padding:4px}
    body.tv h1{font-size:16px}
  }
  
  /* iOS standalone mode support */
  body.ios-standalone{
    padding-top:env(safe-area-inset-top);
    padding-bottom:env(safe-area-inset-bottom);
    padding-left:env(safe-area-inset-left);
    padding-right:env(safe-area-inset-right);
  }
  
  /* Mobile fullscreen button improvements */
  @media (max-width:768px) {
    #fullscreenToggle{
      min-height:44px;
      font-size:14px;
      padding:8px 12px;
    }
    #fullscreenToggle svg{
      width:18px;
      height:18px;
    }
  }
  
  @media (max-width:420px) {
    #fullscreenToggle{
      font-size:12px;
      padding:6px 8px;
    }
    #fullscreenToggle svg{
      width:16px;
      height:16px;
    }
  }

    /* Waitings table wrapper sizing: fill its panel and scroll internally (no page scroll) */
  .table-wrap{max-height:320px;overflow:auto;margin-top:14px;-webkit-overflow-scrolling:touch}
  body.tv .table-wrap{max-height:100%;height:100%;overflow:auto}
  
  /* Mobile table improvements */
  @media (max-width:600px){
    .table-wrap{max-height:280px;overflow-x:auto;overflow-y:auto}
    .waitings-table{min-width:100%;white-space:nowrap}
    .waitings-table th, .waitings-table td{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}
  }
  /* Ensure panels fill their column without causing body scroll */
  body.tv #queuePanel, body.tv #waitingsPanel{height:100%;overflow:hidden}
  /* hide timestamps in TV mode */
  body.tv .ts{display:none !important}
  /* Hide the pager in TV/fullscreen for a cleaner view */
  body.tv #waitingsPager{display:none !important}

  </style>
</head>
<body>
  <header>
    <div class="brand">
      <h1>Cephra Live Monitor</h1>
    </div>
    <?php if (!empty($showClose)): ?>
      <a href="../User/dashboard.php" title="Back to dashboard" class="close-btn" style="margin-left:auto;text-decoration:none;color:var(--text);font-size:22px;font-weight:700;background:transparent;border:0;padding:6px 10px;border-radius:8px;">&times;</a>
    <?php endif; ?>
  </header>

  <main>
    <!-- Controls panel -->
    <section class="panel" id="controlsPanel">

      <div id="controls">
        <div class="ctrl ctrl-announcer">
          <label for="announcer" class="label-icon"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M12 3v18" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"/></svg>Bay announcer</label>
          <div class="control-body"><div id="announcer" class="toggle-switch active"><div class="knob"></div></div></div>
        </div>

        <div class="ctrl">
          <label for="volume" class="label-icon"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M11 5L6 9H2v6h4l5 4V5z" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"/></svg>Volume</label>
          <div class="control-body"><input id="volume" type="range" min="0" max="100" value="80"></div>
        </div>

        <div class="ctrl">
          <label for="speed" class="label-icon"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M3 12h3M12 3v3M21 12h-3M12 21v-3" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"/></svg>Speed</label>
          <div class="control-body"><input id="speed" type="range" min="50" max="200" value="90"></div>
        </div>

        <div class="ctrl">
          <div class="control-body"><button class="action" id="testTts"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M4 4v16h4l6 3V1L8 4H4z" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"/></svg> Test TTS</button></div>
        </div>

        <div class="ctrl">
          <div class="control-body"><button class="ghost" id="fullscreenToggle"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M3 7v-4h4" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/><path d="M21 17v4h-4" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/><path d="M21 3h-6" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/><path d="M9 21H3" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/></svg> Enter fullscreen</button></div>
        </div>

        <div class="ctrl">
          <div class="control-body"><button class="ghost" id="darkToggle"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M21 12.79A9 9 0 1111.21 3 7 7 0 0021 12.79z" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"/></svg> Dark mode</button></div>
        </div>

        <div class="ctrl">
          <label for="lang">Language</label>
          <div class="control-body">
            <select id="lang">
              <option value="en">EN</option>
              <option value="es">ES</option>
              <option value="fil">FIL</option>
              <option value="zh">ZH</option>
            </select>
          </div>
        </div>
      </div>
    </section>

    <!-- Queue panel -->
    <section class="panel" id="queuePanel">
      <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:10px">
      </div>

      <div id="queueGrid">
        <!-- Cards injected by JS for visualization -->
      </div>
    </section>

    <!-- Waitings panel -->
    <section class="panel" id="waitingsPanel">
      <div id="waitingsTop" style="display:flex;justify-content:space-between;align-items:center;margin-bottom:6px;gap:12px;flex-wrap:wrap;border-bottom:1px solid var(--panel-border);padding-bottom:18px">
        <div style="display:flex;align-items:center;gap:12px">
          <strong>Waiting Queue</strong>
          <div id="waitingsCount" class="muted">&nbsp;</div>
        </div>
      </div>

  <div class="table-wrap" style="overflow:auto">
        <table>
          <thead>
            <tr>
              <th>Ticket</th>
              <th>Plate Number</th>
              <th>Name</th>
              <th>Service</th>
            </tr>
          </thead>
          <tbody id="waitingsBody">
            <!-- injected -->
          </tbody>
        </table>
  <div style="display:flex;align-items:center;gap:10px;margin-top:10px" id="waitingsPager">
          <button class="ghost" id="prevWaitings">Prev</button>
          <div class="muted" id="waitingsPageInfo">Page 1 / 1</div>
          <button class="ghost" id="nextWaitings">Next</button>
        </div>
      </div>
    </section>

  </main>

  <script>
    // Lightweight frontend logic for visualization and basic interaction
    // If server-side provided live JSON is available, use it. Otherwise fall back to simulated sample data.
    const __serverBays = <?php echo $baysJson; ?>;
      // Do not fabricate client-side data. If server provides bays use them, otherwise use an empty list
      // expose arrays on window so polling can replace them and renders will pick up changes
      let sampleBays = window.sampleBays = (__serverBays && Array.isArray(__serverBays) && __serverBays.length>0) ? __serverBays.map(b=>({
        bay: b.bay || '',
        type: b.type || '',
        status: b.status || '',
        ticket: b.ticket || '',
        user: b.user || '',
        plate: b.plate || '',
        last_updated: b.last_updated || ''
      })) : [];

    const __serverWaitings = <?php echo $waitingsJson; ?>;
      // Use server waitings if present, otherwise no client-side fabricated waitings
      // expose on window and keep local reference so renders use updated data when polling replaces them
      let sampleWaitings = window.sampleWaitings = (__serverWaitings && Array.isArray(__serverWaitings) && __serverWaitings.length>0) ? __serverWaitings.map(w=>({
        ticket: w.ticket || '',
        plate: w.plate || '',
        user: w.user || '',
        service: w.service || '',
        time: w.time ? new Date(w.time) : null
      })) : [];

    // Render helpers
    function renderGrid(){
      const wrap = document.getElementById('queueGrid');
      wrap.innerHTML = '';
      // fill exactly 8 cells
      for(let i=0;i<8;i++){
        const b = sampleBays[i] || {bay:i+1,type:'Normal',status:'Available',ticket:'-',user:'-'};
        const card = document.createElement('div');
        card.className='card';
        // compute status normalized
        const status = (b.status||'').toLowerCase();
        const pillClass = status === 'available' ? 'available' : (status === 'occupied' ? 'occupied' : (status === 'maintenance' ? 'maintenance' : 'unknown'));
        const last = b.last_updated ? new Date(b.last_updated) : new Date();
        card.innerHTML = `
          <div>
            <div style="display:flex;justify-content:space-between;align-items:center">
              <h3>${b.bay} <span style='font-size:12px;color:var(--muted)'>(${b.type})</span></h3>
              <span class="badge ${pillClass}">${b.status}</span>
            </div>
            <div style="margin-top:8px" class="meta">
              <div class="line">Ticket: <strong>${b.ticket}</strong></div>
              <div class="line">Plate: <strong class="plate">${b.plate||'-'}</strong></div>
            </div>
          </div>
          <div style="display:flex;justify-content:space-between;align-items:center;margin-top:10px">
            <div class="muted">User: <span class="tv-user">${b.user}</span></div>
            <div class="muted"><span class="ts">Updated: ${last.toLocaleTimeString([], {hour:'2-digit',minute:'2-digit'})}</span></div>
          </div>
        `;
        wrap.appendChild(card);
      }
    }

  // Pagination state for waitings
  let waitingsPage = 1;
  const waitingsPageSize = 5; // default page size

    function renderWaitings(){
      const tbody = document.getElementById('waitingsBody');
      const totalItems = sampleWaitings.length;
      const totalPages = Math.max(1, Math.ceil(totalItems / waitingsPageSize));
      if(waitingsPage > totalPages) waitingsPage = totalPages;
      if(waitingsPage < 1) waitingsPage = 1;
      const start = (waitingsPage - 1) * waitingsPageSize;
      const pageItems = sampleWaitings.slice(start, start + waitingsPageSize);
      // update count
      document.getElementById('waitingsCount').textContent = `${totalItems} waiting` + (totalItems!==1? 's':'');
      if(pageItems.length === 0){
        tbody.innerHTML = `
          <tr><td colspan="4">
            <div class="empty-waitings">
              <div style="font-size:20px;font-weight:800;margin-bottom:6px">No waiting tickets</div>
              <div class="muted">There are currently no customers in the waiting queue.</div>
            </div>
          </td></tr>`;
      } else {
        // Render simple table rows: Ticket | Plate (copy) | Name | Service
        tbody.innerHTML = pageItems.map(w=>{
          const serviceClass = (w.service||'Other');
          return `<tr>
            <td>${w.ticket}</td>
            <td><span class="waitings-plate">${w.plate}</span></td>
            <td><span class="tv-user">${w.user}</span></td>
            <td><div class="service-badge ${serviceClass}">${w.service}</div></td>
          </tr>`;
        }).join('');
      }
      document.getElementById('waitingsPageInfo').textContent = `Page ${waitingsPage} / ${totalPages}`;
    }
    // No search/filter controls at top anymore; default paging used

    function timeAgo(d){
      const s = Math.floor((Date.now()-d.getTime())/1000);
      if(s<60) return s+'s';
      if(s<3600) return Math.floor(s/60)+'m';
      return Math.floor(s/3600)+'h';
    }

    // Controls
  const volumeEl = document.getElementById('volume');
  const speedEl = document.getElementById('speed');
  const announcerEl = document.getElementById('announcer');
    const testTtsBtn = document.getElementById('testTts');
    const fullscreenToggle = document.getElementById('fullscreenToggle');
    const darkToggle = document.getElementById('darkToggle');
    const langSelect = document.getElementById('lang');

    // Simple queued TTS player so announcements don't cut each other off
    const TTS = (function(){
      const queue = [];
      let speaking = false;
      let selectedVoice = null;

      function getLangTag(code){
        return code==='en'? 'en-US' : (code==='es'? 'es-ES' : (code==='fil'? 'en-PH' : 'zh-CN'));
      }

      function loadPreferredVoice(){
        const preferredLang = langSelect.value || 'en';
        const voices = window.speechSynthesis.getVoices() || [];
        // Try to pick a voice matching the language tag then a generic default
        const tag = getLangTag(preferredLang).split('-')[0];
        selectedVoice = voices.find(v=>v.lang && v.lang.toLowerCase().startsWith(tag)) || voices[0] || null;
      }

      // voices may load asynchronously
      if('speechSynthesis' in window){
        window.speechSynthesis.onvoiceschanged = loadPreferredVoice;
        // attempt initial load
        setTimeout(loadPreferredVoice, 250);
      }

      function speakNow(text){
        if(!('speechSynthesis' in window)){
          console.log('TTS not supported:', text);
          return Promise.resolve();
        }
        return new Promise((resolve)=>{
          try{
            const u = new SpeechSynthesisUtterance(text);
            u.volume = (volumeEl.value/100);
            u.rate = Math.max(0.1, (speedEl.value/100));
            u.lang = getLangTag(langSelect.value || 'en');
            if(selectedVoice) u.voice = selectedVoice;
            u.onend = ()=>{ speaking = false; resolve(); };
            u.onerror = (e)=>{ speaking = false; console.warn('TTS error', e); resolve(); };
            speaking = true;
            window.speechSynthesis.speak(u);
            console.log('TTS:', text);
          }catch(err){ speaking = false; console.warn('TTS failed', err); resolve(); }
        });
      }

      async function runQueue(){
        if(speaking) return;
        while(queue.length){
          const item = queue.shift();
          // respect announcer toggle
          if(!announcerEl.classList.contains('active')) return queue.splice(0);
          await speakNow(item);
        }
      }

      return {
        enqueue(text){
          if(!text) return;
          queue.push(String(text));
          // update voice selection each enqueue in case language changed
          if('speechSynthesis' in window) loadPreferredVoice();
          runQueue().catch(e=>console.warn(e));
        },
        cancel(){ if('speechSynthesis' in window){ window.speechSynthesis.cancel(); queue.length=0; speaking=false;} }
      };
    })();

    function speak(text){
      if(!announcerEl.classList.contains('active')) return;
      if(!('speechSynthesis' in window)){
        console.log('TTS not supported in this browser');
        return;
      }
      TTS.enqueue(text);
    }

    testTtsBtn.addEventListener('click', ()=>{
      speak('This is a test announcement from Cephra monitor');
    });

    // announcer toggle behavior + persist preference
    const savedAnnouncer = localStorage.getItem('monitor_announcer');
    if(savedAnnouncer === null || savedAnnouncer === 'true'){
      announcerEl.classList.add('active');
    } else {
      announcerEl.classList.remove('active');
    }

    announcerEl.addEventListener('click', ()=>{
      const active = announcerEl.classList.toggle('active');
      localStorage.setItem('monitor_announcer', active ? 'true' : 'false');
    });

    fullscreenToggle.addEventListener('click', ()=>{
      console.log('fullscreenToggle clicked');
      // If we're already fullscreen, try to exit. If we're in simulated mode, disable it.
      if(document.fullscreenElement || window._tvSimulated){
        if(document.fullscreenElement){
          document.exitFullscreen().catch(()=>{});
        } else {
          window._tvSimulated = false;
          setTvMode(false);
        }
        return;
      }

      // Try native Fullscreen API first (must be initiated by user gesture)
      const el = document.documentElement;
      
      // Check for mobile-specific fullscreen methods
      if(el.requestFullscreen){
        el.requestFullscreen().then(()=>{
          // on success, fullscreenchange listener will call setTvMode(true)
        }).catch((err)=>{
          console.log('Fullscreen API failed:', err);
          // Fullscreen API failed (e.g., blocked by browser). Fall back to simulated TV mode.
          window._tvSimulated = true;
          setTvMode(true);
        });
      } else if(el.webkitRequestFullscreen) {
        // Safari iOS fullscreen
        el.webkitRequestFullscreen().then(()=>{
          setTvMode(true);
        }).catch((err)=>{
          console.log('Webkit fullscreen failed:', err);
          window._tvSimulated = true;
          setTvMode(true);
        });
      } else if(el.mozRequestFullScreen) {
        // Firefox fullscreen
        el.mozRequestFullScreen().then(()=>{
          setTvMode(true);
        }).catch((err)=>{
          console.log('Moz fullscreen failed:', err);
          window._tvSimulated = true;
          setTvMode(true);
        });
      } else if(el.msRequestFullscreen) {
        // IE/Edge fullscreen
        el.msRequestFullscreen().then(()=>{
          setTvMode(true);
        }).catch((err)=>{
          console.log('MS fullscreen failed:', err);
          window._tvSimulated = true;
          setTvMode(true);
        });
      } else {
        // No Fullscreen API available - simulate TV mode
        console.log('No fullscreen API available, using simulated mode');
        window._tvSimulated = true;
        setTvMode(true);
      }
    });

    darkToggle.addEventListener('click', ()=>{
      document.body.classList.toggle('dark');
      const isDark = document.body.classList.contains('dark');
      darkToggle.textContent = isDark ? 'Light mode' : 'Dark mode';
      // small visual feedback for theme change
      if(isDark){
        document.documentElement.style.transition = 'background .3s, color .3s';
      } else {
        document.documentElement.style.transition = 'background .3s, color .3s';
      }
    });

    // Simple clock
    function tick(){
      const t = new Date();
      const clockEl = document.getElementById('clock');
      if(clockEl) clockEl.textContent = t.toLocaleTimeString([], {hour:'2-digit', minute:'2-digit'});
    }

    // Initial render
    renderGrid();
    renderWaitings();
    tick();
    setInterval(tick,1000);
    
    // Mobile responsiveness handler
    function handleMobileResize() {
      const width = window.innerWidth;
      const body = document.body;
      
      // Remove any existing mobile classes
      body.classList.remove('mobile-landscape', 'mobile-portrait', 'mobile-small');
      
      // Add appropriate mobile class based on screen size
      if (width <= 420) {
        body.classList.add('mobile-small');
      } else if (width <= 600) {
        body.classList.add('mobile-portrait');
      } else if (width <= 768) {
        body.classList.add('mobile-landscape');
      }
      
      // Force reflow to ensure CSS changes take effect
      body.style.display = 'none';
      body.offsetHeight; // Trigger reflow
      body.style.display = '';
    }
    
    // Handle initial load and resize events
    handleMobileResize();
    window.addEventListener('resize', handleMobileResize);
    window.addEventListener('orientationchange', function() {
      setTimeout(handleMobileResize, 100); // Delay to ensure orientation change is complete
    });

    // Fullscreen behaviour: when fullscreen, hide header and controls and apply TV-friendly styles
    function setTvMode(enabled){
      if(enabled){
        document.body.classList.add('tv');
        const header = document.querySelector('header'); if(header) header.style.display='none';
        const controlsPanel = document.getElementById('controlsPanel'); if(controlsPanel) controlsPanel.style.display='none';
        fullscreenToggle.textContent = 'Exit fullscreen';
        const pager = document.getElementById('waitingsPager'); if(pager) pager.setAttribute('aria-hidden','true');
        
        // Mobile-specific fullscreen optimizations
        if(window.innerWidth <= 768) {
          // Prevent zoom on mobile fullscreen
          document.body.style.zoom = '1';
          // Ensure proper viewport handling
          const viewport = document.querySelector('meta[name="viewport"]');
          if(viewport) {
            viewport.setAttribute('content', 'width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,viewport-fit=cover');
          }
          // Hide mobile browser UI elements
          if('standalone' in window.navigator && window.navigator.standalone) {
            document.body.classList.add('ios-standalone');
          }
        }
      } else {
        document.body.classList.remove('tv');
        const header = document.querySelector('header'); if(header) header.style.display='flex';
        const controlsPanel = document.getElementById('controlsPanel'); if(controlsPanel) controlsPanel.style.display='block';
        fullscreenToggle.textContent = 'Enter fullscreen';
        const pager = document.getElementById('waitingsPager'); if(pager) pager.removeAttribute('aria-hidden');
        
        // Reset mobile-specific fullscreen optimizations
        document.body.style.zoom = '';
        document.body.classList.remove('ios-standalone');
        const viewport = document.querySelector('meta[name="viewport"]');
        if(viewport) {
          viewport.setAttribute('content', 'width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no');
        }
      }
      
      // Force reflow to ensure changes take effect
      document.body.offsetHeight;
    }

    // sync on fullscreenchange (supports escape key and external toggles)
    document.addEventListener('fullscreenchange', ()=>{
      const enabled = !!document.fullscreenElement;
      // if we left fullscreen natively but had previously simulated, clear simulated flag
      if(!enabled && window._tvSimulated){ window._tvSimulated = false; }
      setTvMode(enabled || !!window._tvSimulated);
    });
    
    // Handle mobile-specific fullscreen events
    document.addEventListener('webkitfullscreenchange', ()=>{
      const enabled = !!document.webkitFullscreenElement;
      if(!enabled && window._tvSimulated){ window._tvSimulated = false; }
      setTvMode(enabled || !!window._tvSimulated);
    });
    
    document.addEventListener('mozfullscreenchange', ()=>{
      const enabled = !!document.mozFullScreenElement;
      if(!enabled && window._tvSimulated){ window._tvSimulated = false; }
      setTvMode(enabled || !!window._tvSimulated);
    });
    
    document.addEventListener('MSFullscreenChange', ()=>{
      const enabled = !!document.msFullscreenElement;
      if(!enabled && window._tvSimulated){ window._tvSimulated = false; }
      setTvMode(enabled || !!window._tvSimulated);
    });

    // Pager handlers
    document.getElementById('prevWaitings').addEventListener('click', ()=>{
      if(waitingsPage > 1){ waitingsPage--; renderWaitings(); }
    });
    document.getElementById('nextWaitings').addEventListener('click', ()=>{
      const total = Math.max(1, Math.ceil(sampleWaitings.length / waitingsPageSize));
      if(waitingsPage < total){ waitingsPage++; renderWaitings(); }
    });

    // No client-side simulation: live updates should come from server/WebSocket or API.

      // Center controls which do not have a label (fullscreen/dark toggles)
      (function centerLabelessControls(){
        try{
          const ctrlEls = document.querySelectorAll('#controls .ctrl');
          ctrlEls.forEach(c=>{
            if(!c.querySelector('label')) c.classList.add('centered');
          });
        }catch(e){/* noop */}
      })();

    // Integration notes (not active):
    // - To fetch live snapshot: GET 'api/monitor.php' (JSON) — similar to existing monitor/index.php
    // - To use websocket: connect to ws://<host>:8080/ and parse messages similar to the other monitor implementation
    // Polling logic: fetch live snapshot every 3 seconds and update UI only when data changed
    (function enablePolling(){
  const POLL_URL = './api/monitor.php';
  const POLL_INTERVAL = 2000; // ms (refresh every 2 seconds)
      let lastSnapshot = null; // stringified snapshot for simple change detection

      // Helper to normalize API response into structures used by renderers
      function normalizeApiData(data){
        const bays = Array.isArray(data.bays) ? data.bays.map(b => ({
          bay: b.bay_number || b.bay || '',
          type: b.bay_type || b.type || 'Normal',
          status: b.status || 'Available',
          ticket: b.current_ticket_id || b.ticket || '',
          user: b.current_username || b.user || '',
          plate: b.plate_number || b.plate || '',
          last_updated: b.start_time || b.last_updated || ''
        })) : [];

        // map queue into waiting items shape used by renderWaitings
        const waitings = Array.isArray(data.queue) ? data.queue.map(w => ({
          ticket: w.ticket_id || w.ticket || '',
          plate: w.plate_number || w.plate || '',
          user: w.username || w.user || '',
          service: w.service_type || w.service || '',
          time: w.created_at ? new Date(w.created_at) : (w.time ? new Date(w.time) : null)
        })) : [];

        return { bays, waitings };
      }

      // announce changes between two snapshots for bays and waitings
      function announceDiff(oldBays, newBays){
        try{
          const oldMap = (oldBays||[]).reduce((m,b)=>{ m[b.bay]=b; return m; },{});
          (newBays||[]).forEach(nb=>{
            const ob = oldMap[nb.bay] || {ticket:'', status: ''};
            const oldTicket = ob.ticket || '';
            const newTicket = nb.ticket || '';
            const oldStatus = (ob.status||'').toLowerCase();
            const newStatus = (nb.status||'').toLowerCase();

            // ticket added
            if(!oldTicket && newTicket){
              const plate = nb.plate ? ` plate ${nb.plate}` : '';
              TTS.enqueue(` ${nb.bay} is now occupied by ticket ${newTicket}${plate}`);
            }
            // ticket removed
            else if(oldTicket && !newTicket){
              TTS.enqueue(` ${nb.bay} is now available`);
            }
            // status changed (maintenance, occupied, available, etc.)
            else if(oldStatus !== newStatus){
              TTS.enqueue(` ${nb.bay} status changed to ${nb.status}`);
            }
          });
        }catch(e){console.warn('announceDiff failed',e)}
      }

      function announceWaitingsDiff(oldWaitings, newWaitings){
        try{
          const oldMap = (oldWaitings||[]).reduce((m,w)=>{ if(w && w.ticket) m[w.ticket]=w; return m; },{});
          const newMap = (newWaitings||[]).reduce((m,w)=>{ if(w && w.ticket) m[w.ticket]=w; return m; },{});

          // new tickets
          (newWaitings||[]).forEach(w=>{
            if(!w || !w.ticket) return;
            if(!oldMap[w.ticket]){
              const plate = w.plate ? ` plate ${w.plate}` : '';
              const svc = w.service ? ` for ${w.service}` : '';
              TTS.enqueue(`New waiting ticket ${w.ticket}${plate}${svc}`);
            }
          });

          // removed tickets (served)
          (oldWaitings||[]).forEach(w=>{
            if(!w || !w.ticket) return;
            if(!newMap[w.ticket]){
              TTS.enqueue(`Waiting ticket ${w.ticket} has been served`);
            }
          });
        }catch(e){console.warn('announceWaitingsDiff failed',e)}
      }

      async function pollOnce(){
        try{
          const res = await fetch(POLL_URL, { cache: 'no-store' });
          if(!res.ok) return; // silent fail
          const json = await res.json();
          if(!json || json.success !== true) return;

          const normalized = normalizeApiData(json);
          // create lightweight snapshot string for change detection
          const snap = JSON.stringify({b: normalized.bays.map(x=>({bay:x.bay,ticket:x.ticket,status:x.status})), w: normalized.waitings.map(x=>({ticket:x.ticket,plate:x.plate}))});
          if(snap !== lastSnapshot){
            // announce diffs: bays and waitings
            try{ announceDiff(window.sampleBays || [], normalized.bays); }catch(e){}
            try{ announceWaitingsDiff(window.sampleWaitings || [], normalized.waitings); }catch(e){}

            // replace sample arrays and local references so render functions use updated data
            window.sampleBays = sampleBays = normalized.bays;
            window.sampleWaitings = sampleWaitings = normalized.waitings;
            // re-render UI
            renderGrid();
            renderWaitings();
            lastSnapshot = snap;
          }
        }catch(err){
          // network or parse errors - keep existing UI
          console.warn('monitor poll error', err);
        }
      }

      // Start immediate poll then interval
      pollOnce();
      setInterval(pollOnce, POLL_INTERVAL);
    })();
  </script>
</body>
</html>
