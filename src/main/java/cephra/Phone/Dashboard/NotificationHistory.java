
package cephra.Phone.Dashboard;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class NotificationHistory extends javax.swing.JPanel implements cephra.Phone.Utilities.NotificationManager.NotificationUpdateListener {
    
    private String currentUsername;
    private JPanel previousPanel;

    public NotificationHistory() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        setupLabelPosition();
        makeDraggable();
        
        JScrollPane scrollPane = new JScrollPane(historyPanel);
        
        // Hide scrollbars but keep scrolling possible
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setPreferredSize(new java.awt.Dimension(0, 0));
        
        // Create and add history panel with scroll pane
        createHistoryPanel();
        
        // Register as listener for notification updates
        cephra.Phone.Utilities.NotificationManager.addNotificationUpdateListener(this);
        
        // Add close button
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goBackToPreviousPanel();
            }
        });
        
        // No test notifications - only show real notifications
        
        // Load notification entries
        loadNotificationEntries();
        
        // Add a simple test method - you can call this to test notifications
        // addTestNotification();
        
        // Add component listener to refresh when panel becomes visible
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshHistoryDisplay();
            }
        });
    }
    
    private void createHistoryPanel() {
        historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        historyPanel.setBackground(Color.WHITE);
        
        
        // Use the existing scroll pane if it exists, otherwise create a new one
        if (historyScrollPane == null) {
            historyScrollPane = new JScrollPane(historyPanel);
            historyScrollPane.setBorder(null);
            // Center the scroll pane
            historyScrollPane.setBounds(43, 150, 285, 520);
            add(historyScrollPane);
        } else {
            // Just update the viewport with the new history panel
            historyScrollPane.setViewportView(historyPanel);
        }

        // Hide scrollbars but keep scrolling possible
        historyScrollPane.setBorder(null);
        historyScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        historyScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        if (historyScrollPane.getVerticalScrollBar() != null) {
            historyScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
            // Adjust scroll speed
            historyScrollPane.getVerticalScrollBar().setUnitIncrement(24);
        }
        if (historyScrollPane.getHorizontalScrollBar() != null) {
            historyScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        }
        historyScrollPane.setWheelScrollingEnabled(true);
        historyScrollPane.putClientProperty("JScrollPane.fastWheelScrolling", Boolean.TRUE);
    }
    
    private void loadNotificationEntries() {
        // Get current user's notifications
        currentUsername = cephra.Database.CephraDB.getCurrentUsername();
        refreshHistoryDisplay();
    }
    
    public void setPreviousPanel(JPanel panel) {
        this.previousPanel = panel;
    }
    
    private void goBackToPreviousPanel() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        if (previousPanel != null) {
                            phoneFrame.switchPanel(previousPanel);
                        } else {
                            phoneFrame.switchPanel(cephra.Phone.Dashboard.Home.getAppropriateHomePanel());
                        }
                        break;
                    }
                }
            }
        });
    }
    
    public void refreshHistoryDisplay() {
        // Clear existing notification items
        historyPanel.removeAll();
        
        // Get current user's notifications
        List<cephra.Phone.Utilities.NotificationManager.NotificationEntry> entries = cephra.Phone.Utilities.NotificationManager.getNotificationsForUser(currentUsername);
        
        
        if (entries.isEmpty()) {
            // Show "No notifications" message
            JPanel noHistoryPanel = new JPanel(new BorderLayout());
            noHistoryPanel.setBackground(Color.WHITE);
            JLabel noHistoryLabel = new JLabel("No notifications found");
            noHistoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
            noHistoryLabel.setForeground(Color.GRAY);
            noHistoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noHistoryPanel.add(noHistoryLabel, BorderLayout.CENTER);
            historyPanel.add(noHistoryPanel);
        } else {
            // Add notification entries
            for (cephra.Phone.Utilities.NotificationManager.NotificationEntry entry : entries) {
                addNotificationItem(entry);
            }
        }
        
        // Ensure scroll position is at the top
        SwingUtilities.invokeLater(() -> {
            if (historyScrollPane != null && historyScrollPane.getVerticalScrollBar() != null) {
                historyScrollPane.getVerticalScrollBar().setValue(0);
            }
            historyPanel.revalidate();
            historyPanel.repaint();
        });
    }
    
    @Override
    public void onNotificationAdded(cephra.Phone.Utilities.NotificationManager.NotificationEntry entry) {
        // This is called when a new notification is added
    }
    
    @Override
    public void onNotificationHistoryUpdated(String username) {
        if (username != null && username.equals(currentUsername)) {
            SwingUtilities.invokeLater(this::refreshHistoryDisplay);
        } else {
        }
    }
    
    private void addNotificationItem(final cephra.Phone.Utilities.NotificationManager.NotificationEntry entry) {
        // Create notification item using the nothis panel as a template but with independent structure
        JPanel notificationItemPanel = createNotificationItemFromTemplate(entry);
        historyPanel.add(notificationItemPanel);
    }
    
    private JPanel createNotificationItemFromTemplate(final cephra.Phone.Utilities.NotificationManager.NotificationEntry entry) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(null);
        
        // Get dimensions and colors from nothis panel if available, otherwise use defaults
        Dimension panelSize = (nothis != null) ? nothis.getSize() : new Dimension(270, 80);
        Color panelBg = (nothis != null) ? nothis.getBackground() : new Color(255, 255, 255);
        
        itemPanel.setPreferredSize(panelSize);
        itemPanel.setMaximumSize(panelSize);
        itemPanel.setBackground(panelBg);
        itemPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Try to copy components from nothis panel, with fallback to manual creation
        if (nothis != null && nothis.getComponentCount() > 0) {
            copyComponentsFromTemplate(itemPanel, entry);
        } else {
            createComponentsManually(itemPanel, entry);
        }
        
        // Removed click listener - no more popup details
        
        return itemPanel;
    }
    
    private void copyComponentsFromTemplate(JPanel itemPanel, cephra.Phone.Utilities.NotificationManager.NotificationEntry entry) {
        // Create components based on nothis template but with actual data
        try {
            // Find and copy icon component
            if (jLabel2 != null) {
                JLabel iconLabel = new JLabel();
                iconLabel.setIcon(jLabel2.getIcon());
                iconLabel.setBounds(jLabel2.getBounds());
                itemPanel.add(iconLabel);
            }
            
            // Find and copy notification type component
            if (NotifType != null) {
                JLabel notifTypeLabel = new JLabel(getNotificationTypeText(entry));
                notifTypeLabel.setBounds(NotifType.getBounds());
                notifTypeLabel.setFont(NotifType.getFont());
                notifTypeLabel.setForeground(getNotificationTypeColor(entry));
                itemPanel.add(notifTypeLabel);
            }
            
            // Find and copy message component
            if (message != null) {
                JTextArea messageTextArea = new JTextArea(getEnhancedMessage(entry));
                messageTextArea.setBounds(message.getBounds());
                messageTextArea.setFont(message.getFont());
                messageTextArea.setForeground(message.getForeground());
                messageTextArea.setBackground(message.getBackground());
                messageTextArea.setEditable(false);
                messageTextArea.setFocusable(false);
                messageTextArea.setWrapStyleWord(true);
                messageTextArea.setLineWrap(true);
                messageTextArea.setBorder(null);
                messageTextArea.setOpaque(false);
                itemPanel.add(messageTextArea);
            }
            
        } catch (Exception e) {
            // If copying from template fails, fall back to manual creation
            createComponentsManually(itemPanel, entry);
        }
    }
    
    private void createComponentsManually(JPanel itemPanel, cephra.Phone.Utilities.NotificationManager.NotificationEntry entry) {
        // Manual component creation as fallback
        
        // Icon
        JLabel iconLabel = new JLabel();
        try {
            iconLabel.setIcon(new ImageIcon(getClass().getResource("/cephra/Cephra Images/forgotpass-pop.png")));
        } catch (Exception e) {
            iconLabel.setText("[i]"); // Fallback text if icon not found
        }
        iconLabel.setBounds(10, 30, 30, 30);
        itemPanel.add(iconLabel);
        
        // Notification Type
        JLabel notifTypeLabel = new JLabel(getNotificationTypeText(entry));
        notifTypeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        notifTypeLabel.setForeground(getNotificationTypeColor(entry));
        notifTypeLabel.setBounds(10, 10, 200, 16);
        itemPanel.add(notifTypeLabel);
        
        // Enhanced Message (JTextArea for automatic line wrapping - fallback if template fails)
        String enhancedMessage = getEnhancedMessage(entry);
        JTextArea messageTextArea = new JTextArea(enhancedMessage);
        messageTextArea.setBounds(60, 25, 200, 50);
        messageTextArea.setFont(new Font("Arial", Font.PLAIN, 11));
        messageTextArea.setForeground(new Color(40, 40, 40));
        messageTextArea.setBackground(itemPanel.getBackground());
        messageTextArea.setEditable(false);
        messageTextArea.setFocusable(false);
        messageTextArea.setWrapStyleWord(true);
        messageTextArea.setLineWrap(true);
        messageTextArea.setBorder(null);
        messageTextArea.setOpaque(false);
        itemPanel.add(messageTextArea);
    }
    
    private String getNotificationTypeText(cephra.Phone.Utilities.NotificationManager.NotificationEntry entry) {
           String ticket =  entry.getTicketId() ;
        
        switch (entry.getType()) {
            case CHARGING_COMPLETE:
            case FULL_CHARGE:
                return ticket +" COMPLETE";
            case TICKET_PENDING:
            case TICKET_WAITING:
                return ticket +" WAITING";
            case MY_TURN:
                return ticket +" CHARGING";
            default:
                return "NOTIFICATION";
        }
    }
    
    private Color getNotificationTypeColor(cephra.Phone.Utilities.NotificationManager.NotificationEntry entry) {
        switch (entry.getType()) {
            
            case CHARGING_COMPLETE:
            case FULL_CHARGE:
                return new Color(34, 139, 34); // Green
            case TICKET_PENDING:
            case TICKET_WAITING:
                return new Color(255, 140, 0); // Orange
            case MY_TURN:
                return new Color(30, 144, 255); // Blue
            default:
                return new Color(70, 70, 70); // Gray
        }
    }
    
    private String getEnhancedMessage(cephra.Phone.Utilities.NotificationManager.NotificationEntry entry) {
        switch (entry.getType()) {
            case CHARGING_COMPLETE:
            case FULL_CHARGE:
                return "Charging session completed successfully. Vehicle ready for use. Thank you for using Cephra services.";
            case TICKET_WAITING:
            case TICKET_PENDING:
                return "You're in the charging queue. Please wait for your turn. Estimated time varies based on queue length.";
            case MY_TURN:
                return "Your turn! Proceed to charging bay within 10 minutes.";
            default:
                // For custom messages, allow longer text
                String msg = entry.getMessage();
                return msg.length() > 100 ? msg.substring(0, 97) + "..." : msg;
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(NotificationHistory.this);
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

        historyScrollPane = new javax.swing.JScrollPane();
        historyPanel = new javax.swing.JPanel();
        profilebutton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        charge = new javax.swing.JButton();
        homebutton = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        nothis = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        NotifType = new javax.swing.JLabel();
        message = new javax.swing.JTextArea();

        setMaximumSize(new java.awt.Dimension(370, 750));
        setPreferredSize(new java.awt.Dimension(370, 750));
        setLayout(null);

        historyScrollPane.setBorder(null);
        historyScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        historyScrollPane.setOpaque(false);

        historyPanel.setOpaque(false);
        historyPanel.setLayout(new javax.swing.BoxLayout(historyPanel, javax.swing.BoxLayout.LINE_AXIS));
        historyScrollPane.setViewportView(historyPanel);

        add(historyScrollPane);
        historyScrollPane.setBounds(43, 180, 285, 490);

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
        profilebutton.setBounds(260, 680, 40, 40);

        closeButton.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        closeButton.setText("←");
        closeButton.setBorder(null);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        add(closeButton);
        closeButton.setBounds(270, 120, 75, 50);

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
        homebutton.setBounds(150, 680, 40, 40);

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
        linkbutton.setBounds(90, 680, 40, 40);

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
        historybutton.setBounds(220, 683, 40, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/notification.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-15, 0, 398, 750);

        nothis.setBackground(new java.awt.Color(255, 255, 255));
        nothis.setLayout(null);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/smalllogo.png"))); // NOI18N
        jLabel2.setPreferredSize(new java.awt.Dimension(370, 370));
        nothis.add(jLabel2);
        jLabel2.setBounds(10, 30, 34, 34);

        NotifType.setText("jLabel3");
        nothis.add(NotifType);
        NotifType.setBounds(50, 10, 200, 16);

        message.setColumns(20);
        message.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        message.setRows(5);
        nothis.add(message);
        message.setBounds(52, 30, 210, 40);

        add(nothis);
        nothis.setBounds(370, 150, 270, 80);
    }// </editor-fold>//GEN-END:initComponents

    // CUSTOM CODE - Setup label position
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(0, 0, 370, 750);
        }
    }

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

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_closeButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel NotifType;
    private javax.swing.JButton charge;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel historyPanel;
    private javax.swing.JScrollPane historyScrollPane;
    private javax.swing.JButton historybutton;
    private javax.swing.JButton homebutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton linkbutton;
    private javax.swing.JTextArea message;
    private javax.swing.JPanel nothis;
    private javax.swing.JButton profilebutton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition();
                // Ensure background stays behind controls
                if (jLabel1 != null) {
                    setComponentZOrder(jLabel1, getComponentCount() - 1);
                }
            }
        });
    }
    
    @Override
    public void removeNotify() {
        // Unregister as listener when panel is removed
        cephra.Phone.Utilities.NotificationManager.removeNotificationUpdateListener(this);
        super.removeNotify();
    }
    
    // Test method to add sample notifications (for testing purposes)
    public void addTestNotification() {
        String currentUser = cephra.Database.CephraDB.getCurrentUsername();
        if (currentUser != null) {
            // Add different types of notifications for testing
            cephra.Phone.Utilities.NotificationManager.addChargingCompleteNotification(
                currentUser, "A1", "FCH001"
            );
            // You can uncomment these for more test notifications:
            // cephra.Phone.Utilities.NotificationManager.addQueueNotification(
            //     currentUser, "FCH002"
            // );
            // cephra.Phone.Utilities.NotificationManager.addChargingStartedNotification(
            //     currentUser, "B2", "FCH003"
            // );
        }
    }
}
