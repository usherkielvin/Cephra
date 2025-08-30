
package cephra.Phone;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class phonehistory extends javax.swing.JPanel implements UserHistoryManager.HistoryUpdateListener {
    
    
    
   
    private String currentUsername;

    public phonehistory() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
        setupLabelPosition(); // Set label position
        makeDraggable();
        
        JScrollPane scrollPane = new JScrollPane(historyPanel);
        

// Hide scrollbars but keep scrolling possible
scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));



     scrollPane.getVerticalScrollBar().setPreferredSize(new java.awt.Dimension(0, 0));
        // Create and add history panel with scroll pane
        createHistoryPanel();
        
        // Register as listener for history updates
        UserHistoryManager.addHistoryUpdateListener(this);
        
        // Load history entries
        loadHistoryEntries();
        
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
            historyScrollPane.setBounds(30, 150, 290, 480);
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
    
    private void loadHistoryEntries() {
        // Get current user's history
        currentUsername = cephra.CephraDB.getCurrentUsername();
        refreshHistoryDisplay();
    }
    
    public void refreshHistoryDisplay() {
        // Clear existing history items
        historyPanel.removeAll();
        
        // Get current user's history (now includes admin history entries)
        List<UserHistoryManager.HistoryEntry> entries = UserHistoryManager.getUserHistory(currentUsername);
        
        // Debug information
        System.out.println("PhoneHistory: Refreshing history display for user: " + currentUsername);
        System.out.println("PhoneHistory: Found " + entries.size() + " history entries");
        
        if (entries.isEmpty()) {
            // Show "No history" message
            JPanel noHistoryPanel = new JPanel(new BorderLayout());
            noHistoryPanel.setBackground(Color.WHITE);
            JLabel noHistoryLabel = new JLabel("No charging history found");
            noHistoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
            noHistoryLabel.setForeground(Color.GRAY);
            noHistoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noHistoryPanel.add(noHistoryLabel, BorderLayout.CENTER);
            historyPanel.add(noHistoryPanel);
            
            // Debug: Check if there are any charging history records in database
            try {
                List<Object[]> dbHistory = cephra.CephraDB.getChargingHistoryForUser(currentUsername);
                System.out.println("PhoneHistory: Database has " + dbHistory.size() + " charging history records for user: " + currentUsername);
                if (!dbHistory.isEmpty()) {
                    System.out.println("PhoneHistory: First record - Ticket: " + dbHistory.get(0)[0] + ", Service: " + dbHistory.get(0)[2]);
                }
            } catch (Exception e) {
                System.err.println("PhoneHistory: Error checking database history: " + e.getMessage());
            }
        } else {
            // Add history entries
            for (UserHistoryManager.HistoryEntry entry : entries) {
                System.out.println("PhoneHistory: Adding entry - Ticket: " + entry.getTicketId() + 
                                 ", Service: " + entry.getServiceType() + 
                                 ", Total: " + entry.getTotal() + 
                                 ", Date: " + entry.getFormattedDate());
                addHistoryItem(entry);
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
    public void onHistoryUpdated(String username) {
        if (username != null && username.equals(currentUsername)) {
            SwingUtilities.invokeLater(this::refreshHistoryDisplay);
        }
    }
    
    private void addHistoryItem(final UserHistoryManager.HistoryEntry entry) {
        // Create a panel for a single history item
        JPanel historyItemPanel = new JPanel();
        historyItemPanel.setLayout(new BorderLayout(5, 0));
        historyItemPanel.setBackground(Color.WHITE);
        historyItemPanel.setBorder(null);
        historyItemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Set cursor to hand to indicate clickable
        historyItemPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Set a fixed preferred and maximum height for each history item panel
        int itemHeight = 70; // Increased height for better visibility
        int itemWidth = 290; // Width of the scroll pane
        historyItemPanel.setPreferredSize(new Dimension(itemWidth, itemHeight));
        historyItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));

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

        // Service type
        JLabel serviceLabel = new JLabel(entry.getServiceType());
        serviceLabel.setFont(new Font("Arial", Font.BOLD, 11));
        serviceLabel.setForeground(new Color(50, 100, 150));
        serviceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Total amount
        JLabel totalLabel = new JLabel("Total: " + entry.getTotal());
        totalLabel.setFont(new Font("Arial", Font.BOLD, 11));
        totalLabel.setForeground(new Color(0, 100, 0));
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(timeLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        leftPanel.add(serviceLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        leftPanel.add(totalLabel);

        // Right panel for ticket and reference
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);

        // Ticket ID
        JLabel ticketLabel = new JLabel("Ticket: " + entry.getTicketId());
        ticketLabel.setFont(new Font("Arial", Font.BOLD, 11));
        ticketLabel.setForeground(new Color(50, 50, 50));
        ticketLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Reference number
        String refNumber = entry.getReferenceNumber();
        System.out.println("PhoneHistory: Reference number for ticket " + entry.getTicketId() + " is: '" + refNumber + "'");
        
        // If reference number is empty or null, try to get it from QueueBridge
        if (refNumber == null || refNumber.trim().isEmpty() || refNumber.equals("null")) {
            refNumber = cephra.Admin.QueueBridge.getTicketRefNumber(entry.getTicketId());
            System.out.println("PhoneHistory: Got reference number from QueueBridge: '" + refNumber + "'");
        }
        
        JLabel referenceLabel = new JLabel("Ref: " + refNumber);
        referenceLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        referenceLabel.setForeground(Color.GRAY);
        referenceLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rightPanel.add(ticketLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        rightPanel.add(referenceLabel);

        historyItemPanel.add(dateLabel, BorderLayout.NORTH);
        historyItemPanel.add(leftPanel, BorderLayout.WEST);
        historyItemPanel.add(rightPanel, BorderLayout.EAST);
        
        // Add click listener to show details when clicked
        historyItemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showHistoryDetails(entry);
            }
        });

        historyPanel.add(historyItemPanel);
    }
/////
    private JPanel detailsPanel;
   
    
    private void showHistoryDetails(UserHistoryManager.HistoryEntry entry) {
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
        detailsPanel.setBounds(30, 150, 290, 450);
        
        // Add header
        JLabel headerLabel = new JLabel("Charging Details");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(new Color(50, 50, 50));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(headerLabel);
        
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Add ticket information
        addDetailRow(detailsPanel, "Ticket", entry.getTicketId());
        addDetailRow(detailsPanel, "Customer", cephra.CephraDB.getCurrentUsername());
        
        // Add service type (Fast Charge or Normal Charge)
        addDetailRow(detailsPanel, "Service", entry.getServiceType());
        
        // Add kWh detail (get from admin history record)
        String kWh = "32.80"; // Default value
        try {
            List<Object[]> adminRecords = cephra.Admin.HistoryBridge.getRecordsForUser(cephra.CephraDB.getCurrentUsername());
            if (adminRecords != null) {
                for (Object[] record : adminRecords) {
                    if (record.length >= 7 && entry.getTicketId().equals(String.valueOf(record[0]))) {
                        kWh = String.valueOf(record[2]); // KWh is at index 2
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting kWh from admin history: " + e.getMessage());
        }
        addDetailRow(detailsPanel, "kWh", kWh);
        
        // Add charging time
        addDetailRow(detailsPanel, "Charging Time", entry.getChargingTime());
        
        // Add total amount
        addDetailRow(detailsPanel, "Total", entry.getTotal());
        
        // Add served by with correct value
        String servedBy = "Admin"; // Default fallback
        try {
            List<Object[]> adminRecords = cephra.Admin.HistoryBridge.getRecordsForUser(cephra.CephraDB.getCurrentUsername());
            if (adminRecords != null) {
                for (Object[] record : adminRecords) {
                    if (record.length >= 5 && entry.getTicketId().equals(String.valueOf(record[0]))) {
                        String servedByValue = String.valueOf(record[4]); // Served By is at index 4
                        if (servedByValue != null && !servedByValue.equals("null")) {
                            servedBy = servedByValue;
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting served by from admin history: " + e.getMessage());
        }
        addDetailRow(detailsPanel, "Served By", servedBy);
        
        // Add date and time
        addDetailRow(detailsPanel, "Date", entry.getFormattedDate());
        addDetailRow(detailsPanel, "Time", entry.getFormattedTime());
        
        // Add reference number
        addDetailRow(detailsPanel, "Reference", entry.getReferenceNumber());
        
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Add OK button
        JButton okButton = new JButton("OK");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(e -> closeDetailsPanel());
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(phonehistory.this);
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
        charge = new javax.swing.JButton();
        homebutton = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(350, 750));
        setPreferredSize(new java.awt.Dimension(350, 750));
        setLayout(null);

        historyScrollPane.setBorder(null);
        historyScrollPane.setOpaque(false);

        historyPanel.setOpaque(false);
        historyPanel.setLayout(new javax.swing.BoxLayout(historyPanel, javax.swing.BoxLayout.LINE_AXIS));
        historyScrollPane.setViewportView(historyPanel);

        add(historyScrollPane);
        historyScrollPane.setBounds(10, 140, 320, 530);

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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/HISTORY - if none.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-15, 0, 398, 750);
    }// </editor-fold>//GEN-END:initComponents

    // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(-15, 0, 398, 750);
        }
    }

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
        UserHistoryManager.removeHistoryUpdateListener(this);
        super.removeNotify();
    }
}
