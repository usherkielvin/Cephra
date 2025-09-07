
package cephra.Phone;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.SwingUtilities;

public class QueueTicket extends javax.swing.JPanel {

   
    public QueueTicket() {
        initComponents();
         setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
        setupLabelPosition(); // Set label position
        makeDraggable();
        
        // Update battery percentage and estimated time
        updateBatteryDisplay();
        updateEstimatedTime();

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
        batterypercent.setBounds(120, 280, 100, 40);

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
                        phoneFrame.switchPanel(new cephra.Phone.serviceoffered());
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
                String s = cephra.Phone.QueueFlow.getCurrentServiceName();
                String t = cephra.Phone.QueueFlow.getCurrentTicketId();
                // If ticket is not assigned yet, preview the next based on service
                if (t == null || t.length() == 0) {
                    if (s != null && s.toLowerCase().contains("fast")) {
                        QTicket.setText("FCH" + String.format("%03d",  cephra.Phone.QueueFlow.getEntries().size() + 1));
                    } else if (s != null && s.toLowerCase().contains("normal")) {
                        QTicket.setText("NCH" + String.format("%03d",  cephra.Phone.QueueFlow.getEntries().size() + 1));
                    }
                } else {
                    QTicket.setText(t);
                }
                if (s != null && s.length() > 0) {
                    typeofcharge.setText(s);
                }
            }
        });
    }
 // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(-15, 0, 398, 750);
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
                        phoneFrame.switchPanel(new cephra.Phone.link());
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
                        phoneFrame.switchPanel(new cephra.Phone.phonehistory());
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
                        phoneFrame.switchPanel(new cephra.Phone.Profile());
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
                        phoneFrame.switchPanel(new cephra.Phone.home());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_homebutton2ActionPerformed

    private void cancelticketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelticketActionPerformed
       // Clear the active ticket when user cancels
       String username = cephra.CephraDB.getCurrentUsername();
       cephra.CephraDB.clearActiveTicket(username);
       
       SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.serviceoffered());
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
                cephra.Phone.QueueFlow.addCurrentToAdminAndStore(cephra.CephraDB.getCurrentUsername());
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.QueueStatus());
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
            String username = cephra.CephraDB.getCurrentUsername();
            int batteryLevel = cephra.CephraDB.getUserBatteryLevel(username);
            String status = batteryLevel <= 20 ? " (LOW)" : batteryLevel <= 50 ? " (MED)" : " (OK)";
            batterypercent.setText(batteryLevel + "%" + status);
        } catch (Exception e) {
            System.err.println("Error updating battery display: " + e.getMessage());
        }
    }

    private void updateEstimatedTime() {
        try {
            String ticket = cephra.Phone.QueueFlow.getCurrentTicketId();
            String service = cephra.Phone.QueueFlow.getCurrentServiceName();
            String username = cephra.CephraDB.getCurrentUsername();
            int start = cephra.CephraDB.getUserBatteryLevel(username);
            int minutes;
            if (ticket != null && !ticket.isEmpty()) {
                minutes = cephra.Admin.QueueBridge.computeEstimatedMinutes(ticket);
            } else {
                minutes = cephra.Admin.QueueBridge.computeEstimatedMinutes(start, service);
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
}
