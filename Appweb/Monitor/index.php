<?php
// New Queue Monitor UI for Cephra
// File: Appweb/Monitor/index.php
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
  <meta name="viewport" content="width=device-width,initial-scale=1" />
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

  html,body{height:100%;margin:0;font-family:Inter,Segoe UI,Arial,sans-serif;background:var(--bg);color:var(--text);-webkit-font-smoothing:antialiased}
  /* make sizing predictable */
  *, *::before, *::after { box-sizing: border-box; }
    body{padding:20px;box-sizing:border-box;transition:background .25s,color .25s}
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
      height:26px;
      border-radius:999px;
      background:var(--surface);
      border:1px solid var(--panel-border);
      position:relative;
      cursor:pointer;
      display:inline-block;
      vertical-align:middle;
      transition:all .18s ease;
      box-shadow: 0 6px 14px rgba(2,6,23,0.04);
      padding:3px;
    }

    /* ...existing code... */
