
package cephra.Phone;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class NotificationHistory extends javax.swing.JPanel implements NotificationHistoryManager.NotificationUpdateListener {
    
    
    
   
    private String currentUsername;
    private JPanel previousPanel; // To store the previous panel for back navigation

    public NotificationHistory() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        setupLabelPosition(); // Set label position
        makeDraggable();
        
        JScrollPane scrollPane = new JScrollPane(historyPanel);
        
        // Hide scrollbars but keep scrolling possible
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setPreferredSize(new java.awt.Dimension(0, 0));
        
        // Create and add history panel with scroll pane
        createHistoryPanel();
        
        // Register as listener for notification updates
        NotificationHistoryManager.addNotificationUpdateListener(this);
        
        // Add close button
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goBackToPreviousPanel();
            }
        });
        
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
            historyScrollPane.setBounds(50, 150, 290, 480);
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
            // Adjust scroll speed (higher = faster). Try 20-40 for quicker scrolling
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
        currentUsername = cephra.CephraDB.getCurrentUsername();
        System.out.println("NotificationHistory: Loaded notifications for user: " + currentUsername);
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
                            phoneFrame.switchPanel(new cephra.Phone.home());
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
        List<NotificationHistoryManager.NotificationEntry> entries = NotificationHistoryManager.getNotificationsForUser(currentUsername);
        
        // Debug information
        System.out.println("NotificationHistory: Found " + entries.size() + " notification entries");
        
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
            for (NotificationHistoryManager.NotificationEntry entry : entries) {
                addNotificationItem(entry);
            }
        }
        
        // Ensure scroll position is at the top to show newest entries
        SwingUtilities.invokeLater(() -> {
            if (historyScrollPane != null && historyScrollPane.getVerticalScrollBar() != null) {
                historyScrollPane.getVerticalScrollBar().setValue(0);
            }
            historyPanel.revalidate();
            historyPanel.repaint();
        });
    }
    
    @Override
    public void onNotificationAdded(NotificationHistoryManager.NotificationEntry entry) {
        // This is called when a new notification is added
        // We'll update the display in onNotificationHistoryUpdated
    }
    
    @Override
    public void onNotificationHistoryUpdated(String username) {
        System.out.println("NotificationHistory: onNotificationHistoryUpdated called for username: " + username + ", currentUsername: " + currentUsername);
        if (username != null && username.equals(currentUsername)) {
            System.out.println("NotificationHistory: Username matches, refreshing notification display");
            SwingUtilities.invokeLater(this::refreshHistoryDisplay);
        } else {
            System.out.println("NotificationHistory: Username does not match, ignoring update");
        }
    }
    
    private void addNotificationItem(final NotificationHistoryManager.NotificationEntry entry) {
        // Create a panel for a single notification item
        JPanel notificationItemPanel = new JPanel();
        notificationItemPanel.setLayout(new BorderLayout(5, 0));
        notificationItemPanel.setBackground(Color.WHITE);
        notificationItemPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        notificationItemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Set cursor to hand to indicate clickable
        notificationItemPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Set a fixed preferred and maximum height for each notification item panel
        int itemHeight = 70; // Increased height for better visibility
        int itemWidth = 290; // Width of the scroll pane
        notificationItemPanel.setPreferredSize(new Dimension(itemWidth, itemHeight));
        notificationItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));

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

        // Message preview (first 25 chars + ...)
        String messagePreview = entry.getMessage();
        if (messagePreview.length() > 25) {
            messagePreview = messagePreview.substring(0, 25) + "...";
        }
        JLabel messageLabel = new JLabel(messagePreview);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        messageLabel.setForeground(new Color(50, 50, 50));
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(timeLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        leftPanel.add(typeLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        leftPanel.add(messageLabel);

        // Right panel for ticket ID if available
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);

        // Only add ticket info if there's a ticket ID
        if (entry.getTicketId() != null && !entry.getTicketId().isEmpty()) {
            // Ticket ID
            JLabel ticketLabel = new JLabel("Ticket: " + entry.getTicketId());
            ticketLabel.setFont(new Font("Arial", Font.BOLD, 11));
            ticketLabel.setForeground(new Color(50, 50, 50));
            ticketLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            rightPanel.add(ticketLabel);
        }

        // Bay number if available
        if (entry.getBayNumber() != null && !entry.getBayNumber().isEmpty()) {
            JLabel bayLabel = new JLabel("Bay: " + entry.getBayNumber());
            bayLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            bayLabel.setForeground(new Color(80, 80, 80));
            bayLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            rightPanel.add(bayLabel);
        }

        notificationItemPanel.add(dateLabel, BorderLayout.NORTH);
        notificationItemPanel.add(leftPanel, BorderLayout.WEST);
        if (rightPanel.getComponentCount() > 0) {
            notificationItemPanel.add(rightPanel, BorderLayout.EAST);
        }
        
        // Add click listener to show details when clicked
        notificationItemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showNotificationDetails(entry);
            }
        });

        historyPanel.add(notificationItemPanel);
    }
/////
    private JPanel detailsPanel;
   
    
    private void showNotificationDetails(NotificationHistoryManager.NotificationEntry entry) {
        // If details panel already exists, remove it first
        if (detailsPanel != null) {
            remove(detailsPanel);
        }
        
        // Hide the history scroll pane
        historyScrollPane.setVisible(false);
        
        // Create panel for details
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBounds(50, 150, 290, 450);
        
        // Add header
        JLabel headerLabel = new JLabel("Notification Details");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(new Color(50, 50, 50));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(headerLabel);
        
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Add notification type
        addDetailRow(detailsPanel, "Type", entry.getType().getDisplayName());
        
        // Add message
        addDetailRow(detailsPanel, "Message", entry.getMessage());
        
        // Add details
        if (entry.getDetails() != null && !entry.getDetails().isEmpty()) {
            addDetailRow(detailsPanel, "Details", entry.getDetails());
        }
        
        // Add ticket ID if available
        if (entry.getTicketId() != null && !entry.getTicketId().isEmpty()) {
            addDetailRow(detailsPanel, "Ticket", entry.getTicketId());
        }
        
        // Add bay number if available
        if (entry.getBayNumber() != null && !entry.getBayNumber().isEmpty()) {
            addDetailRow(detailsPanel, "Bay Number", entry.getBayNumber());
        }
        
        // Add username
        addDetailRow(detailsPanel, "User", entry.getUsername());
        
        // Add date and time
        addDetailRow(detailsPanel, "Date", entry.getFormattedDate());
        addDetailRow(detailsPanel, "Time", entry.getFormattedTime());
        
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Add OK button
        JButton okButton = new JButton("OK");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(_ -> closeDetailsPanel());
        detailsPanel.add(okButton);
        
        // Add the details panel to the phone frame
        add(detailsPanel);
        
        // Make sure the background stays behind
        if (jLabel1 != null) {
            setComponentZOrder(jLabel1, getComponentCount() - 1);
        }
        
        revalidate();
        repaint();
    }
    
    private void closeDetailsPanel() {
        // Remove the details panel
        if (detailsPanel != null) {
            remove(detailsPanel);
            detailsPanel = null;
        }
        
        // Show the history scroll pane again
        historyScrollPane.setVisible(true);
        
        revalidate();
        repaint();
    }
    
    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BorderLayout(15, 0));
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelComponent = new JLabel(label + ":");
        labelComponent.setFont(new Font("Arial", Font.BOLD, 13));
        labelComponent.setForeground(new Color(70, 70, 70));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 13));
        valueComponent.setForeground(new Color(30, 30, 30));
        
        // Special formatting for certain fields
        if (label.equals("Total")) {
            valueComponent.setFont(new Font("Arial", Font.BOLD, 13));
            valueComponent.setForeground(new Color(0, 100, 0));
        } else if (label.equals("Service")) {
            valueComponent.setForeground(new Color(50, 100, 150));
        } else if (label.equals("Reference")) {
            valueComponent.setFont(new Font("Arial", Font.BOLD, 12));
            valueComponent.setForeground(new Color(100, 50, 150));
        }
        
        rowPanel.add(labelComponent, BorderLayout.WEST);
        rowPanel.add(valueComponent, BorderLayout.CENTER);
        
        panel.add(rowPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
    }
    ////
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
        historyScrollPane.setOpaque(false);

        historyPanel.setOpaque(false);
        historyPanel.setLayout(new javax.swing.BoxLayout(historyPanel, javax.swing.BoxLayout.LINE_AXIS));
        historyScrollPane.setViewportView(historyPanel);

        add(historyScrollPane);
        historyScrollPane.setBounds(20, 150, 300, 520);

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

        closeButton.setText("jButton1");
        add(closeButton);
        closeButton.setBounds(240, 120, 75, 23);

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
        charge.setBounds(30, 680, 50, 50);

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
        homebutton.setBounds(160, 680, 60, 40);

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
        linkbutton.setBounds(90, 680, 60, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/HISTORY - if none.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-15, 0, 398, 750);
    }// </editor-fold>//GEN-END:initComponents

    // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
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
                        phoneFrame.switchPanel(new cephra.Phone.ChargingOption());
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
                        phoneFrame.switchPanel(new cephra.Phone.Profile());
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
                        phoneFrame.switchPanel(new cephra.Phone.home());
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
                        phoneFrame.switchPanel(new cephra.Phone.link());
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
        NotificationHistoryManager.removeNotificationUpdateListener(this);
        super.removeNotify();
    }
}
