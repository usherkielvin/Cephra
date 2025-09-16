
package cephra.Phone.Dashboard;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class NotificationHistory extends javax.swing.JPanel implements cephra.Phone.Utilities.NotificationManager.NotificationUpdateListener {
    
    private String currentUsername;
    private JPanel previousPanel;
    private JPanel messagePopupPanel;
    private JPanel modalOverlay;

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
        
        // Add a test notification
        String currentUser = cephra.Database.CephraDB.getCurrentUsername();
        if (currentUser != null) {
            cephra.Phone.Utilities.NotificationManager.addChargingCompleteNotification(
                currentUser, "FCH06", "3"
            );
        }
        
        // Load notification entries
        loadNotificationEntries();
        
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
        // Create a panel for a single notification item
        JPanel notificationItemPanel = new JPanel();
        notificationItemPanel.setLayout(new BorderLayout(5, 0));
        notificationItemPanel.setBackground(Color.WHITE);
        notificationItemPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        notificationItemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Set cursor to hand
        notificationItemPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Dynamic height calculation
        int baseHeight = 80;
        int itemWidth = 260;

        // Date label at top
        JLabel dateLabel = new JLabel(entry.getFormattedDate());
        dateLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dateLabel.setForeground(new Color(70, 70, 70));

        // Left panel for details
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);

        // Time
        JLabel timeLabel = new JLabel(entry.getFormattedTime());
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        timeLabel.setForeground(Color.DARK_GRAY);
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Notification type
        JLabel typeLabel = new JLabel(entry.getType().getDisplayName());
        typeLabel.setFont(new Font("Arial", Font.BOLD, 11));
        typeLabel.setForeground(new Color(50, 100, 150));
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Use JTextArea for proper multiline text display
        JTextArea messageTextArea = new JTextArea(entry.getMessage());
        messageTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        messageTextArea.setForeground(new Color(40, 40, 40));
        messageTextArea.setBackground(Color.WHITE);
        messageTextArea.setEditable(false);
        messageTextArea.setFocusable(false);
        messageTextArea.setWrapStyleWord(true);
        messageTextArea.setLineWrap(true);
        messageTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        messageTextArea.setBorder(null);
        
        // Calculate proper height based on text content
        messageTextArea.setColumns(25);
        messageTextArea.setSize(new Dimension(200, Short.MAX_VALUE));
        int preferredHeight = messageTextArea.getPreferredSize().height;
        int messageTextHeight = Math.max(60, preferredHeight + 10);
        
        messageTextArea.setPreferredSize(new Dimension(200, messageTextHeight));
        messageTextArea.setMaximumSize(new Dimension(200, messageTextHeight));
        
        // Calculate the total panel height
        int totalHeight = baseHeight + messageTextHeight + 20;
        int itemHeight = Math.max(100, Math.min(totalHeight, 450));
        
        notificationItemPanel.setPreferredSize(new Dimension(itemWidth, itemHeight));
        notificationItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));
        

        leftPanel.add(timeLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        leftPanel.add(typeLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        leftPanel.add(messageTextArea);
        leftPanel.add(Box.createVerticalGlue());

        // Right panel for ticket ID if available
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);

        // Add some space at the top
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Only add ticket info if there's a ticket ID
        if (entry.getTicketId() != null && !entry.getTicketId().isEmpty()) {
            // Ticket ID
            JLabel ticketLabel = new JLabel("Ticket: " + entry.getTicketId());
            ticketLabel.setFont(new Font("Arial", Font.BOLD, 11));
            ticketLabel.setForeground(new Color(50, 50, 50));
            ticketLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            rightPanel.add(ticketLabel);
            rightPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        }

        // Bay number if available
        if (entry.getBayNumber() != null && !entry.getBayNumber().isEmpty()) {
            JLabel bayLabel = new JLabel("Bay: " + entry.getBayNumber());
            bayLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            bayLabel.setForeground(new Color(80, 80, 80));
            bayLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            rightPanel.add(bayLabel);
        }
        
        // Add glue to push ticket info to top
        rightPanel.add(Box.createVerticalGlue());

        notificationItemPanel.add(dateLabel, BorderLayout.NORTH);
        notificationItemPanel.add(leftPanel, BorderLayout.WEST);
        if (rightPanel.getComponentCount() > 0) {
            notificationItemPanel.add(rightPanel, BorderLayout.EAST);
        }
        
        // Add click listener to show just the message
        notificationItemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showNotificationMessage(entry);
            }
        });

        historyPanel.add(notificationItemPanel);
    }
    
    private void showNotificationMessage(cephra.Phone.Utilities.NotificationManager.NotificationEntry entry) {
        // Hide existing popup if any
        if (messagePopupPanel != null) {
            remove(messagePopupPanel);
        }
        
        // Create modal overlay to block background clicks
        if (modalOverlay == null) {
            modalOverlay = new JPanel();
            modalOverlay.setBackground(new java.awt.Color(0, 0, 0, 100));
            modalOverlay.setOpaque(false);
            modalOverlay.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    e.consume();
                }
            });
            add(modalOverlay);
        }
        
        // Position and show modal overlay
        modalOverlay.setBounds(0, 0, getWidth(), getHeight());
        modalOverlay.setVisible(true);
        
        // Create popup panel for the message
        messagePopupPanel = new JPanel();
        messagePopupPanel.setLayout(new BoxLayout(messagePopupPanel, BoxLayout.Y_AXIS));
        messagePopupPanel.setBackground(Color.WHITE);
        messagePopupPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2),
            new javax.swing.border.EmptyBorder(20, 20, 20, 20)
        ));
        
        // Position popup in center
        int popupWidth = Math.min(300, Math.max(250, entry.getMessage().length() * 2));
        int popupHeight = Math.min(400, Math.max(150, (entry.getMessage().length() / 40) * 25 + 100));
        int x = (getWidth() - popupWidth) / 2;
        int y = (getHeight() - popupHeight) / 2;
        messagePopupPanel.setBounds(x, y, popupWidth, popupHeight);
        
        // Add title
        JLabel titleLabel = new JLabel(entry.getType().getDisplayName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(50, 50, 50));
        messagePopupPanel.add(titleLabel);
        
        messagePopupPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Add message with HTML wrapping
        String wrappedMessage = "<html><div style='width:" + (popupWidth - 60) + "px; line-height:1.4; text-align:left;'>" + 
                               entry.getMessage() + "</div></html>";
        JLabel messageLabel = new JLabel(wrappedMessage);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setVerticalAlignment(JLabel.TOP);
        messagePopupPanel.add(messageLabel);
        
        messagePopupPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Add OK button
        JButton okButton = new JButton("OK");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.setFont(new Font("Arial", Font.BOLD, 12));
        okButton.addActionListener(_ -> closeMessagePopup());
        messagePopupPanel.add(okButton);
        
        // Add popup to main panel
        add(messagePopupPanel);
        
        // Set proper layering
        setComponentZOrder(modalOverlay, 1);
        setComponentZOrder(messagePopupPanel, 0);
        
        // Keep background behind everything
        if (jLabel1 != null) {
            setComponentZOrder(jLabel1, getComponentCount() - 1);
        }
        
        revalidate();
        repaint();
    }
    
    private void closeMessagePopup() {
        if (messagePopupPanel != null) {
            remove(messagePopupPanel);
            messagePopupPanel = null;
        }
        
        if (modalOverlay != null) {
            modalOverlay.setVisible(false);
        }
        
        revalidate();
        repaint();
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
        jLabel1 = new javax.swing.JLabel();

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
        add(closeButton);
        closeButton.setBounds(240, 120, 75, 50);

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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/notification.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-15, 0, 398, 750);
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton charge;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel historyPanel;
    private javax.swing.JScrollPane historyScrollPane;
    private javax.swing.JButton homebutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton linkbutton;
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
}
