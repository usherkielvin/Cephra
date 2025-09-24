package cephra.Phone.Dashboard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChargingOption extends javax.swing.JPanel {

    public ChargingOption() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        setupLabelPosition(); // Set label position
        makeDraggable();
        
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(ChargingOption.this);
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

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        normalcharge = new javax.swing.JButton();
        profilebutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        fastcharge = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        homebutton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(370, 750));
        setPreferredSize(new java.awt.Dimension(370, 750));
        setLayout(null);

        normalcharge.setBorder(null);
        normalcharge.setBorderPainted(false);
        normalcharge.setContentAreaFilled(false);
        normalcharge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                normalchargeActionPerformed(evt);
            }
        });
        add(normalcharge);
        normalcharge.setBounds(50, 380, 280, 120);

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
        profilebutton.setBounds(280, 680, 40, 40);

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
        historybutton.setBounds(220, 680, 40, 40);

        fastcharge.setBorder(null);
        fastcharge.setBorderPainted(false);
        fastcharge.setContentAreaFilled(false);
        fastcharge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fastchargeActionPerformed(evt);
            }
        });
        add(fastcharge);
        fastcharge.setBounds(50, 243, 280, 120);

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
        linkbutton.setBounds(110, 680, 40, 40);

        homebutton.setBorder(null);
        homebutton.setBorderPainted(false);
        homebutton.setContentAreaFilled(false);
        homebutton.setFocusPainted(false);
        homebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homebuttonActionPerformed(evt);
            }
        });
        add(homebutton);
        homebutton.setBounds(160, 680, 40, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/fast.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 370, 750);
    }// </editor-fold>//GEN-END:initComponents

    // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(0, 0, 370, 750);
        }
    }

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

    private void homebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homebuttonActionPerformed
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
    }//GEN-LAST:event_homebuttonActionPerformed

    private void fastchargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fastchargeActionPerformed
        // If car isn't linked, show LinkFirst popup instead of Ticketing
        boolean linked = false;
        try {
            String username = cephra.Database.CephraDB.getCurrentUsername();
            int carIndex = (username == null || username.isEmpty()) ? -1 : cephra.Database.CephraDB.getUserCarIndex(username);
            linked = (carIndex != -1);
        } catch (Throwable ignore) {}
        if (!linked) {
            String username = null;
            try { username = cephra.Database.CephraDB.getCurrentUsername(); } catch (Throwable ignore) {}
            cephra.Phone.Popups.LinkFirst.showPayPop(null, username);
            return;
        }
        // If user already has an active/pending ticket, show AlreadyTicket
        try {
            String username = cephra.Database.CephraDB.getCurrentUsername();
            String status = cephra.Database.CephraDB.getUserCurrentTicketStatus(username);
            String existingTicket = cephra.Phone.Utilities.QueueFlow.getCurrentTicketId();
            boolean isOpen = false;
            if (status != null) {
                String s = status.trim().toLowerCase();
                isOpen = ("pending".equals(s) || "waiting".equals(s) || "charging".equals(s));
            }
            if (isOpen && existingTicket != null && existingTicket.trim().length() > 0) {
                cephra.Phone.Popups.AlreadyTicket.showPayPop(existingTicket, username);
                return;
            }
        } catch (Throwable ignore) {}
        // Battery guard: if user's battery is already full, show AlreadyFull popup
        try {
            String username = cephra.Database.CephraDB.getCurrentUsername();
            int batteryLevel = (username == null || username.isEmpty()) ? -1 : cephra.Database.CephraDB.getUserBatteryLevel(username);
            if (batteryLevel >= 100) {
                cephra.Phone.Popups.AlreadyFull.showPayPop(null, username);
                return;
            }
        } catch (Throwable ignore) {}
        // Set Fast Charging service and show ticketing popup
        cephra.Phone.Utilities.QueueFlow.setCurrentServiceOnly("Fast Charging");
        cephra.Phone.Popups.Ticketing.showPopup();
    }//GEN-LAST:event_fastchargeActionPerformed

    private void normalchargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_normalchargeActionPerformed
        // If car isn't linked, show LinkFirst popup instead of Ticketing
        boolean linked = false;
        try {
            String username = cephra.Database.CephraDB.getCurrentUsername();
            int carIndex = (username == null || username.isEmpty()) ? -1 : cephra.Database.CephraDB.getUserCarIndex(username);
            linked = (carIndex != -1);
        } catch (Throwable ignore) {}
        if (!linked) {
            String username = null;
            try { username = cephra.Database.CephraDB.getCurrentUsername(); } catch (Throwable ignore) {}
            cephra.Phone.Popups.LinkFirst.showPayPop(null, username);
            return;
        }
        // If user already has an active/pending ticket, show AlreadyTicket
        try {
            String username = cephra.Database.CephraDB.getCurrentUsername();
            String status = cephra.Database.CephraDB.getUserCurrentTicketStatus(username);
            String existingTicket = cephra.Phone.Utilities.QueueFlow.getCurrentTicketId();
            boolean isOpen = false;
            if (status != null) {
                String s = status.trim().toLowerCase();
                isOpen = ("pending".equals(s) || "waiting".equals(s) || "charging".equals(s));
            }
            if (isOpen && existingTicket != null && existingTicket.trim().length() > 0) {
                cephra.Phone.Popups.AlreadyTicket.showPayPop(existingTicket, username);
                return;
            }
        } catch (Throwable ignore) {}
        // Battery guard: if user's battery is already full, show AlreadyFull popup
        try {
            String username = cephra.Database.CephraDB.getCurrentUsername();
            int batteryLevel = (username == null || username.isEmpty()) ? -1 : cephra.Database.CephraDB.getUserBatteryLevel(username);
            if (batteryLevel >= 100) {
                cephra.Phone.Popups.AlreadyFull.showPayPop(null, username);
                return;
            }
        } catch (Throwable ignore) {}
        // Set Normal Charging service and show ticketing popup
        cephra.Phone.Utilities.QueueFlow.setCurrentServiceOnly("Normal Charging");
        cephra.Phone.Popups.Ticketing.showPopup();
    }//GEN-LAST:event_normalchargeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton fastcharge;
    private javax.swing.JButton historybutton;
    private javax.swing.JButton homebutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton linkbutton;
    private javax.swing.JButton normalcharge;
    private javax.swing.JButton profilebutton;
    // End of variables declaration//GEN-END:variables
    

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition();
            }
        });
    }
}
