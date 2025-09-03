package cephra.api;

import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;

public class ApiServer {
    private static final int PORT = 8080;
    private static ServerSocket serverSocket;
    private static volatile boolean running = false;

    public static void start(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            running = true;
            System.out.println("API server started on port " + PORT);
            
            // Use thread pool for handling multiple connections
            var executor = Executors.newCachedThreadPool();
            
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    executor.submit(() -> handleClient(clientSocket));
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Error accepting client: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Could not start server on port " + PORT + ": " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            String requestLine = in.readLine();
            if (requestLine == null) return;
            
            String[] parts = requestLine.split(" ");
            if (parts.length < 2) return;
            
            String method = parts[0];
            String path = parts[1];
            
            if ("GET".equals(method)) {
                if ("/api/health".equals(path)) {
                    sendJsonResponse(out, "{\"status\":\"ok\"}");
                } else if ("/api/queue".equals(path)) {
                    sendQueueResponse(out);
                } else if ("/phone".equals(path) || "/phone/".equals(path) || "/phone/index.html".equals(path)) {
                    sendPhonePage(out);
                } else {
                    sendNotFound(out);
                }
            } else {
                sendMethodNotAllowed(out);
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // Ignore close errors
            }
        }
    }

    private static void sendJsonResponse(PrintWriter out, String json) {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: application/json");
        out.println("Access-Control-Allow-Origin: *");
        out.println("Content-Length: " + json.length());
        out.println();
        out.println(json);
    }

    private static void sendQueueResponse(PrintWriter out) {
        try {
            // Get queue data from your existing system
            String json = getQueueJson();
            sendJsonResponse(out, json);
        } catch (Exception e) {
            sendJsonResponse(out, "[]");
        }
    }

    private static void sendPhonePage(PrintWriter out) {
        String html = getPhoneHtml();
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("Content-Length: " + html.length());
        out.println();
        out.println(html);
    }

    private static void sendNotFound(PrintWriter out) {
        out.println("HTTP/1.1 404 Not Found");
        out.println("Content-Type: text/plain");
        out.println();
        out.println("Not Found");
    }

    private static void sendMethodNotAllowed(PrintWriter out) {
        out.println("HTTP/1.1 405 Method Not Allowed");
        out.println("Content-Type: text/plain");
        out.println();
        out.println("Method Not Allowed");
    }

    private static String getQueueJson() {
        try {
            // Get real queue data from database
            java.util.List<Object[]> queueTickets = cephra.CephraDB.getAllQueueTickets();
            return convertQueueTicketsToJson(queueTickets);
        } catch (Exception e) {
            // Fallback to demo data if database fails
            return "[{\"ticket\":\"FCH001\",\"customer\":\"dizon\",\"service\":\"Fast Charging\",\"status\":\"Pending\",\"payment\":\"Pending\"}]";
        }
    }

    private static String convertQueueTicketsToJson(java.util.List<Object[]> queueTickets) {
        if (queueTickets == null || queueTickets.isEmpty()) {
            return "[]";
        }
        
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < queueTickets.size(); i++) {
            Object[] ticket = queueTickets.get(i);
            if (i > 0) json.append(",");
            
            json.append("{");
            json.append("\"ticket\":\"").append(ticket[0] != null ? ticket[0].toString() : "").append("\",");
            json.append("\"customer\":\"").append(ticket[2] != null ? ticket[2].toString() : "").append("\",");
            json.append("\"service\":\"").append(ticket[3] != null ? ticket[3].toString() : "").append("\",");
            json.append("\"status\":\"").append(ticket[4] != null ? ticket[4].toString() : "").append("\",");
            json.append("\"payment\":\"").append(ticket[5] != null ? ticket[5].toString() : "").append("\"");
            json.append("}");
        }
        json.append("]");
        return json.toString();
    }

    private static String getPhoneHtml() {
        return "<!DOCTYPE html><html><head><meta name='viewport' content='width=device-width,initial-scale=1'><title>Cephra Phone</title><style>" +
            "body{margin:0;padding:0;width:430px;height:932px;font-family:'Segoe UI',sans-serif;overflow:hidden;position:relative;}" +
            ".phone-container{width:430px;height:932px;position:relative;background:linear-gradient(135deg,#667eea 0%,#764ba2 50%,#f093fb 100%);}" +
            ".login-container{position:absolute;top:50%;left:50%;transform:translate(-50%,-50%);width:300px;background:rgba(255,255,255,0.95);padding:30px;border-radius:15px;box-shadow:0 8px 32px rgba(0,0,0,0.3);backdrop-filter:blur(10px);}" +
            ".login-title{text-align:center;font-size:28px;font-weight:bold;color:#333;margin-bottom:30px;}" +
            ".input-group{margin-bottom:20px;}" +
            ".input-group label{display:block;margin-bottom:8px;color:#555;font-weight:500;}" +
            ".input-group input{width:100%;padding:12px;border:2px solid #ddd;border-radius:8px;font-size:16px;box-sizing:border-box;transition:border-color 0.3s;}" +
            ".input-group input:focus{outline:none;border-color:#667eea;}" +
            ".login-btn{width:100%;padding:15px;background:linear-gradient(135deg,#667eea,#764ba2);color:white;border:none;border-radius:8px;font-size:18px;font-weight:bold;cursor:pointer;transition:transform 0.2s;}" +
            ".login-btn:hover{transform:translateY(-2px);}" +
            ".login-btn:active{transform:translateY(0);}" +
            ".home-container{display:none;width:430px;height:932px;position:relative;background:linear-gradient(135deg,#667eea 0%,#764ba2 50%,#f093fb 100%);}" +
            ".welcome-text{position:absolute;top:80px;left:25px;width:280px;height:60px;font-size:36px;font-weight:bold;color:#000;z-index:10;display:flex;align-items:center;text-shadow:1px 1px 2px rgba(255,255,255,0.8);}" +
            ".nav-buttons{position:absolute;bottom:30px;left:0;right:0;height:120px;z-index:20;}" +
            ".nav-btn{position:absolute;border:2px solid #fff;background:rgba(255,255,255,0.3);cursor:pointer;transition:all 0.3s;border-radius:8px;color:#fff;font-weight:bold;}" +
            ".nav-btn:hover{background:rgba(255,255,255,0.6);transform:scale(1.05);}" +
            ".nav-btn:active{transform:scale(0.95);}" +
            ".nav-btn.charge{top:0;left:40px;width:60px;height:50px;}" +
            ".nav-btn.link{top:0;left:120px;width:60px;height:50px;}" +
            ".nav-btn.history{top:0;left:240px;width:60px;height:50px;}" +
            ".nav-btn.profile{top:0;left:320px;width:60px;height:50px;}" +
            ".queue-section{position:absolute;top:180px;left:25px;right:25px;max-height:500px;overflow-y:auto;z-index:5;}" +
            ".queue-item{background:rgba(255,255,255,0.95);margin:10px 0;padding:15px;border-radius:10px;box-shadow:0 4px 12px rgba(0,0,0,0.15);border:1px solid rgba(0,0,0,0.1);backdrop-filter:blur(10px);}" +
            ".queue-title{font-weight:bold;font-size:18px;color:#333;}" +
            ".queue-details{font-size:15px;color:#666;margin-top:6px;}" +
            ".button-icon{font-size:22px;display:block;text-align:center;line-height:1;color:#fff;}" +
            ".button-text{font-size:11px;display:block;text-align:center;color:#fff;margin-top:3px;}" +
            "</style></head><body>" +
            "<div class='phone-container'>" +
            "<div class='login-container' id='loginForm'>" +
            "<div class='login-title'>Cephra Phone</div>" +
            "<div class='input-group'><label>Username</label><input type='text' id='username' placeholder='Enter username'></div>" +
            "<div class='input-group'><label>Password</label><input type='password' id='password' placeholder='Enter password'></div>" +
            "<button class='login-btn' onclick='login()'>Login</button>" +
            "</div>" +
            "<div class='home-container' id='homePage'>" +
            "<div class='welcome-text' id='welcome'>Hi Guest!</div>" +
            "<div class='queue-section' id='queue'></div>" +
            "<div class='nav-buttons'>" +
            "<button class='nav-btn charge' onclick='showCharge()' title='Charge'><span class='button-icon'>⚡</span><span class='button-text'>CHARGE</span></button>" +
            "<button class='nav-btn link' onclick='showLink()' title='Link'><span class='button-icon'>🔗</span><span class='button-text'>LINK</span></button>" +
            "<button class='nav-btn history' onclick='showHistory()' title='History'><span class='button-icon'>📅</span><span class='button-text'>HISTORY</span></button>" +
            "<button class='nav-btn profile' onclick='showProfile()' title='Profile'><span class='button-icon'>👤</span><span class='button-text'>PROFILE</span></button>" +
            "</div></div></div>" +
            "<script>" +
            "async function login(){const username=document.getElementById('username').value;const password=document.getElementById('password').value;" +
            "if(!username||!password){alert('Please enter username and password');return;}" +
            "try{const response=await fetch('/api/login',{method:'POST',headers:{'Content-Type':'application/x-www-form-urlencoded'},body:'username='+encodeURIComponent(username)+'&password='+encodeURIComponent(password)});" +
            "const data=await response.json();if(data.success){document.getElementById('loginForm').style.display='none';document.getElementById('homePage').style.display='block';" +
            "document.getElementById('welcome').textContent='Hi '+username+'!';loadQueue();}else{alert(data.message||'Login failed');}}" +
            "catch(e){alert('Network error. Please try again.');}}" +
            "async function loadQueue(){try{const r=await fetch('/api/queue');const data=await r.json();" +
            "let html='';for(let t of data){html+='<div class=\"queue-item\"><div class=\"queue-title\">'+(t.ticket||'')+'-'+(t.status||'')+'</div><div class=\"queue-details\">'+(t.customer||'')+' • '+(t.service||'')+' • '+(t.payment||'')+'</div></div>';}" +
            "document.getElementById('queue').innerHTML=html;}catch(e){document.getElementById('queue').innerHTML='<div class=\"queue-item\"><div class=\"queue-title\">Queue Loading...</div></div>';}}" +
            "function showCharge(){alert('⚡ Charge Service\\n\\nRedirecting to service selection...');}" +
            "function showLink(){alert('🔗 Link Service\\n\\nConnecting your vehicle...');}" +
            "function showHistory(){alert('📅 History\\n\\nShowing your charging history...');}" +
            "function showProfile(){alert('👤 Profile\\n\\nOpening your profile...');}" +
            "document.getElementById('username').addEventListener('keypress',function(e){if(e.key==='Enter'){document.getElementById('password').focus();}});" +
            "document.getElementById('password').addEventListener('keypress',function(e){if(e.key==='Enter'){login();}});" +
            "</script></body></html>";
    }

    public static void stop() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            // Ignore close errors
        }
    }
}


