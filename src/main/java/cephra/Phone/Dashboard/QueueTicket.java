
package cephra.Phone.Dashboard;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.SwingUtilities;

public class QueueTicket extends javax.swing.JPanel {

   
    public QueueTicket() {
        initComponents();
         setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        setupLabelPosition(); // Set label position
        makeDraggable();
        
        // Update battery percentage and estimated time
        updateBatteryDisplay();
        updateEstimatedTime();
        
        // Add listener to refresh when panel becomes visible
        addHierarchyListener(new java.awt.event.HierarchyListener() {
            @Override
            public void hierarchyChanged(java.awt.event.HierarchyEvent e) {
                if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0) {
                    if (isShowing()) {
                        // Refresh display when panel becomes visible
                        updateBatteryDisplay();
                        updateTicketDisplay();
                    }
                }
            }
        });

       // jLabel1.setText("<html>You are next after<br>current charging session</html>");
    }

 
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        checkstatus = new javax.swing.JButton();
        charge = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        cancelticket = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        homebutton2 = new javax.swing.JButton();
        profilebutton = new javax.swing.JButton();
        batterypercent = new javax.swing.JLabel();
        QTicket = new javax.swing.JLabel();
        typeofcharge = new javax.swing.JLabel();
        mins1 = new javax.swing.JLabel();
        label1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        checkstatus.setBorder(null);
        checkstatus.setBorderPainted(false);
        checkstatus.setContentAreaFilled(false);
        checkstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkstatusActionPerformed(evt);
            }
        });
        add(checkstatus);
        checkstatus.setBounds(30, 500, 280, 40);

        charge.setBorder(null);
        charge.setBorderPainted(false);
        charge.setContentAreaFilled(false);
        charge.setFocusPainted(false);
        charge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeActionPerformed(evt);
            }
        });
        add(charge);
        charge.setBounds(30, 680, 50, 40);

        linkbutton.setBorder(null);
        linkbutton.setBorderPainted(false);
        linkbutton.setContentAreaFilled(false);
        linkbutton.setFocusPainted(false);
        linkbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkbuttonActionPerformed(evt);
            }
        });
        add(linkbutton);
        linkbutton.setBounds(90, 680, 50, 40);

        cancelticket.setBorder(null);
        cancelticket.setBorderPainted(false);
        cancelticket.setContentAreaFilled(false);
        cancelticket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelticketActionPerformed(evt);
            }
        });
        add(cancelticket);
        cancelticket.setBounds(30, 543, 280, 40);

        historybutton.setBorder(null);
        historybutton.setBorderPainted(false);
        historybutton.setContentAreaFilled(false);
        historybutton.setFocusPainted(false);
        historybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historybuttonActionPerformed(evt);
            }
        });
        add(historybutton);
        historybutton.setBounds(200, 680, 50, 40);

        homebutton2.setBorder(null);
        homebutton2.setBorderPainted(false);
        homebutton2.setContentAreaFilled(false);
        homebutton2.setFocusPainted(false);
        homebutton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homebutton2ActionPerformed(evt);
            }
        });
        add(homebutton2);
        homebutton2.setBounds(150, 680, 40, 40);

        profilebutton.setBorder(null);
        profilebutton.setBorderPainted(false);
        profilebutton.setContentAreaFilled(false);
        profilebutton.setFocusPainted(false);
        profilebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilebuttonActionPerformed(evt);
            }
        });
        add(profilebutton);
        profilebutton.setBounds(260, 670, 50, 50);

        batterypercent.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        batterypercent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        batterypercent.setText("18% (LOW)");
        add(batterypercent);
        batterypercent.setBounds(135, 280, 100, 40);

        QTicket.setFont(new java.awt.Font("Segoe UI", 3, 36)); // NOI18N
        QTicket.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        QTicket.setText("FCPOO5");
        add(QTicket);
        QTicket.setBounds(70, 190, 200, 40);

        typeofcharge.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        typeofcharge.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        typeofcharge.setText("Fast Charging");
        add(typeofcharge);
        typeofcharge.setBounds(70, 230, 200, 30);

        mins1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        mins1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mins1.setText("5 minutes");
        add(mins1);
        mins1.setBounds(35, 390, 280, 40);

        label1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label1.setText("<html><div style='text-align:center;'>You are next after<br>current charging session</div></html>\n");
        add(label1);
        label1.setBounds(95, 340, 150, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/ServiceFast.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-15, 0, 398, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void chargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.ChargingOption());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_chargeActionPerformed
    
    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Apply selected ticket/service to labels
                String s = cephra.Phone.Utilities.QueueFlow.getCurrentServiceName();
                String t = cephra.Phone.Utilities.QueueFlow.getCurrentTicketId();
                
                // Check if user has low battery and should show priority ticket
                try {
                    String username = cephra.Database.CephraDB.getCurrentUsername();
                    int batteryLevel = cephra.Database.CephraDB.getUserBatteryLevel(username);
                    
                    if (batteryLevel < 20) {
                        // Low battery - show priority ticket format
                        if (t == null || t.length() == 0) {
                            // Generate preview priority ticket using QueueFlow
                            String priorityTicket = cephra.Phone.Utilities.QueueFlow.previewNextPriorityTicketIdForService(s, batteryLevel);
                            QTicket.setText(priorityTicket);
                        } else {
                            // Ensure existing ticket shows priority format
                            String priorityTicket = convertToPriorityTicket(t);
                            QTicket.setText(priorityTicket);
                        }
                    } else {
                        // Normal battery - show regular ticket
                        if (t == null || t.length() == 0) {
                            // Preview exact next ticket from QueueFlow counters
                            QTicket.setText(cephra.Phone.Utilities.QueueFlow.previewNextTicketIdForService(s));
                        } else {
                            QTicket.setText(t);
                        }
                    }
                } catch (Exception e) {
                    // Fallback to original logic if battery check fails
                    if (t == null || t.length() == 0) {
                        QTicket.setText(cephra.Phone.Utilities.QueueFlow.previewNextTicketIdForService(s));
                    } else {
                        QTicket.setText(t);
                    }
                }
                
                if (s != null && s.length() > 0) {
                    typeofcharge.setText(s);
                }
            }
        });
    }
    
    
    // Helper method to convert regular ticket to priority ticket
    private String convertToPriorityTicket(String ticketId) {
        if (ticketId == null || ticketId.length() == 0) return ticketId;
        
        // If already a priority ticket, return as is
        if (ticketId.contains("P")) return ticketId;
        
        // Convert regular ticket to priority ticket
        if (ticketId.startsWith("FCH")) {
            return ticketId.replace("FCH", "FCHP");
        } else if (ticketId.startsWith("NCH")) {
            return ticketId.replace("NCH", "NCHP");
        }
        
        return ticketId; // Return original if no conversion needed
    }
 // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(0, 0, 370, 750);
        }
    }
     private void makeDraggable() {
        final Point[] dragPoint = {null};

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragPoint[0] = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragPoint[0] != null) {
                    java.awt.Window window = SwingUtilities.getWindowAncestor(QueueTicket.this);
                    if (window != null) {
                        Point currentLocation = window.getLocation();
                        window.setLocation(
                            currentLocation.x + e.getX() - dragPoint[0].x,
                            currentLocation.y + e.getY() - dragPoint[0].y
                        );
                    }
                }
            }
        });
    }
    private void linkbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkbuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.LinkConnect());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_linkbuttonActionPerformed

    private void historybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historybuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.ChargeHistory());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_historybuttonActionPerformed

    private void profilebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilebuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.Profile());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_profilebuttonActionPerformed

    private void homebutton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homebutton2ActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(cephra.Phone.Dashboard.Home.getAppropriateHomePanel());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_homebutton2ActionPerformed

    private void cancelticketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelticketActionPerformed
       // Clear the active ticket when user cancels
       String username = cephra.Database.CephraDB.getCurrentUsername();
       cephra.Database.CephraDB.clearActiveTicket(username);
       
       SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.ChargingOption());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_cancelticketActionPerformed

    private void checkstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkstatusActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Push current selection to Admin and store in memory list
                cephra.Phone.Utilities.QueueFlow.addCurrentToAdminAndStore(cephra.Database.CephraDB.getCurrentUsername());
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(cephra.Phone.Dashboard.Home.getAppropriateHomePanel());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_checkstatusActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel QTicket;
    private javax.swing.JLabel batterypercent;
    private javax.swing.JButton cancelticket;
    private javax.swing.JButton charge;
    private javax.swing.JButton checkstatus;
    private javax.swing.JButton historybutton;
    private javax.swing.JButton homebutton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel label1;
    private javax.swing.JButton linkbutton;
    private javax.swing.JLabel mins1;
    private javax.swing.JButton profilebutton;
    private javax.swing.JLabel typeofcharge;
    // End of variables declaration//GEN-END:variables
    
    private void updateBatteryDisplay() {
        try {
            String username = cephra.Database.CephraDB.getCurrentUsername();
            int batteryLevel = cephra.Database.CephraDB.getUserBatteryLevel(username);
            
            // Show PRIO for low battery (< 20%), otherwise show normal status
            String status;
            if (batteryLevel < 20) {
                status = " PRIO";
            } else if (batteryLevel <= 50) {
                status = " (MED)";
            } else {
                status = " (OK)";
            }
            
            batterypercent.setText(batteryLevel + "%" + status);
        } catch (Exception e) {
            System.err.println("Error updating battery display: " + e.getMessage());
        }
    }

    private void updateEstimatedTime() {
        try {
            String ticket = cephra.Phone.Utilities.QueueFlow.getCurrentTicketId();
            String service = cephra.Phone.Utilities.QueueFlow.getCurrentServiceName();
            String username = cephra.Database.CephraDB.getCurrentUsername();
            int start = cephra.Database.CephraDB.getUserBatteryLevel(username);
            int minutes;
            if (ticket != null && !ticket.isEmpty()) {
                minutes = cephra.Admin.Utilities.QueueBridge.computeEstimatedMinutes(ticket);
            } else {
                minutes = cephra.Admin.Utilities.QueueBridge.computeEstimatedMinutes(start, service);
            }
            mins1.setText(formatTimeDisplay(minutes));
        } catch (Exception e) {
            System.err.println("Error updating estimated time: " + e.getMessage());
        }
    }
    
    private String formatTimeDisplay(int minutes) {
        if (minutes >= 60) {
            int hours = minutes / 60;
            int remainingMinutes = minutes % 60;
            if (remainingMinutes == 0) {
                return hours + " hour" + (hours > 1 ? "s" : "");
            } else {
                return hours + " hour" + (hours > 1 ? "s" : "") + " " + remainingMinutes + " min" + (remainingMinutes > 1 ? "s" : "");
            }
        } else {
            return minutes + " minute" + (minutes != 1 ? "s" : "");
        }
    }
    
    // Method to update ticket display based on current battery level
    private void updateTicketDisplay() {
        try {
            String username = cephra.Database.CephraDB.getCurrentUsername();
            int batteryLevel = cephra.Database.CephraDB.getUserBatteryLevel(username);
            String s = cephra.Phone.Utilities.QueueFlow.getCurrentServiceName();
            String t = cephra.Phone.Utilities.QueueFlow.getCurrentTicketId();
            
            if (batteryLevel < 20) {
                // Low battery - show priority ticket format
                if (t == null || t.length() == 0) {
                    String priorityTicket = cephra.Phone.Utilities.QueueFlow.previewNextPriorityTicketIdForService(s, batteryLevel);
                    QTicket.setText(priorityTicket);
                } else {
                    String priorityTicket = convertToPriorityTicket(t);
                    QTicket.setText(priorityTicket);
                }
            } else {
                // Normal battery - show regular ticket
                if (t == null || t.length() == 0) {
                    QTicket.setText(cephra.Phone.Utilities.QueueFlow.previewNextTicketIdForService(s));
                } else {
                    QTicket.setText(t);
                }
            }
        } catch (Exception e) {
            System.err.println("Error updating ticket display: " + e.getMessage());
        }
    }
}
