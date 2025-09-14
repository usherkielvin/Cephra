
package cephra.Phone.RewardsWallet;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class WalletHistory extends javax.swing.JPanel {
    
    
    
   
    private String currentUsername;
    private JPanel previousPanel; // To store the previous panel for back navigation

    public WalletHistory() {
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
        
        // Add close button
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goBackToPreviousPanel();
            }
        });
        
        // Load wallet transaction entries
        loadWalletTransactions();
        
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
    
    private void loadWalletTransactions() {
        // Get current user's wallet transactions
        currentUsername = cephra.CephraDB.getCurrentUsername();
        System.out.println("WalletHistory: Loading wallet transactions for user: " + currentUsername);
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
                            phoneFrame.switchPanel(new cephra.Phone.Dashboard.Home());
                        }
                        break;
                    }
                }
            }
        });
    }
    
    public void refreshHistoryDisplay() {
        // Clear existing transaction items
        historyPanel.removeAll();
        
        // Get ALL wallet transactions for complete history
        java.util.List<Object[]> transactions = cephra.CephraDB.getAllWalletTransactionHistory(currentUsername);
        
        // Debug information
        System.out.println("WalletHistory: Found " + transactions.size() + " wallet transactions");
        
        if (transactions.isEmpty()) {
            // Show "No transactions" message
            JPanel noHistoryPanel = new JPanel(new BorderLayout());
            noHistoryPanel.setBackground(Color.WHITE);
            JLabel noHistoryLabel = new JLabel("No wallet transactions found");
            noHistoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
            noHistoryLabel.setForeground(Color.GRAY);
            noHistoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noHistoryPanel.add(noHistoryLabel, BorderLayout.CENTER);
            historyPanel.add(noHistoryPanel);
        } else {
            // Add wallet transaction entries
            for (Object[] transaction : transactions) {
                addTransactionItem(transaction);
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
    
    /**
     * Public method to refresh wallet history when called externally
     */
    public void refreshWalletHistory() {
        SwingUtilities.invokeLater(this::refreshHistoryDisplay);
    }
    
    private void addTransactionItem(final Object[] transaction) {
        // Transaction data structure: [transaction_type, amount, new_balance, description, reference_id, transaction_date]
        String transactionType = (String) transaction[0];
        double amount = (Double) transaction[1];
        double newBalance = (Double) transaction[2];
        String description = (String) transaction[3];
        
        java.sql.Timestamp timestamp = (java.sql.Timestamp) transaction[5];
        
        // Create a panel for a single transaction item
        JPanel transactionItemPanel = new JPanel();
        transactionItemPanel.setLayout(new BorderLayout(5, 0));
        transactionItemPanel.setBackground(Color.WHITE);
        transactionItemPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        transactionItemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Set cursor to hand to indicate clickable
        transactionItemPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Set a fixed preferred and maximum height for each transaction item panel
        int itemHeight = 80; // Increased height for better visibility
        int itemWidth = 290; // Width of the scroll pane
        transactionItemPanel.setPreferredSize(new Dimension(itemWidth, itemHeight));
        transactionItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));

        // Format date and time
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMM dd, yyyy");
        java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm");
        String dateStr = dateFormat.format(timestamp);
        String timeStr = timeFormat.format(timestamp);
        
        // Date label at top
        JLabel dateLabel = new JLabel(dateStr);
        dateLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dateLabel.setForeground(new Color(70, 70, 70));

        // Left panel for details
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);

        // Time
        JLabel timeLabel = new JLabel(timeStr);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        timeLabel.setForeground(Color.DARK_GRAY);
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Transaction type with color coding
        String typeDisplay = getTransactionTypeDisplay(transactionType);
        JLabel typeLabel = new JLabel(typeDisplay);
        typeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        typeLabel.setForeground(getTransactionTypeColor(transactionType));
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Description preview (first 30 chars + ...)
        String descriptionPreview = description != null ? description : "";
        if (descriptionPreview.length() > 30) {
            descriptionPreview = descriptionPreview.substring(0, 30) + "...";
        }
        JLabel descriptionLabel = new JLabel(descriptionPreview);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        descriptionLabel.setForeground(new Color(80, 80, 80));
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(timeLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        leftPanel.add(typeLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        leftPanel.add(descriptionLabel);

        // Right panel for amount and balance
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);

        // Amount with proper formatting
        String amountText = formatTransactionAmount(transactionType, amount);
        JLabel amountLabel = new JLabel(amountText);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 13));
        amountLabel.setForeground(getAmountColor(transactionType));
        amountLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        rightPanel.add(amountLabel);
        
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // New balance
        JLabel balanceLabel = new JLabel("Balance: ₱" + String.format("%.2f", newBalance));
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        balanceLabel.setForeground(new Color(100, 100, 100));
        balanceLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        rightPanel.add(balanceLabel);

        transactionItemPanel.add(dateLabel, BorderLayout.NORTH);
        transactionItemPanel.add(leftPanel, BorderLayout.WEST);
        transactionItemPanel.add(rightPanel, BorderLayout.EAST);
        
        // Add click listener to show transaction details
        transactionItemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showTransactionDetails(transaction);
            }
        });

        historyPanel.add(transactionItemPanel);
    }
    
    /**
     * Gets display name for transaction type
     */
    private String getTransactionTypeDisplay(String transactionType) {
        switch (transactionType) {
            case "TOP_UP": return "Top Up";
            case "PAYMENT": return "Payment";
            default: return transactionType;
        }
    }
    
    /**
     * Gets color for transaction type
     */
    private Color getTransactionTypeColor(String transactionType) {
        switch (transactionType) {
            case "TOP_UP": return new Color(0, 150, 0); // Green for income
            case "PAYMENT": return new Color(200, 0, 0); // Red for payment
            default: return new Color(50, 100, 150); // Blue for others
        }
    }
    
    /**
     * Formats transaction amount with proper sign
     */
    private String formatTransactionAmount(String transactionType, double amount) {
        String sign = transactionType.equals("TOP_UP") ? "+" : "-";
        return sign + String.format("₱%.2f", Math.abs(amount));
    }
    
    /**
     * Gets color for amount display
     */
    private Color getAmountColor(String transactionType) {
        return transactionType.equals("TOP_UP") ? 
               new Color(0, 150, 0) : new Color(200, 0, 0);
    }
/////
    private JPanel detailsPanel;
   
    
    private void showTransactionDetails(Object[] transaction) {
        // Transaction data: [transaction_type, amount, new_balance, description, reference_id, transaction_date]
        String transactionType = (String) transaction[0];
        double amount = (Double) transaction[1];
        double newBalance = (Double) transaction[2];
        String description = (String) transaction[3];
        String referenceId = (String) transaction[4];
        java.sql.Timestamp timestamp = (java.sql.Timestamp) transaction[5];
        
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
        JLabel headerLabel = new JLabel("Transaction Details");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(new Color(50, 50, 50));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(headerLabel);
        
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Add transaction type
        addDetailRow(detailsPanel, "Type", getTransactionTypeDisplay(transactionType));
        
        // Add amount
        addDetailRow(detailsPanel, "Amount", formatTransactionAmount(transactionType, amount));
        
        // Add description
        if (description != null && !description.isEmpty()) {
            addDetailRow(detailsPanel, "Description", description);
        }
        
        // Add reference ID if available
        if (referenceId != null && !referenceId.isEmpty()) {
            addDetailRow(detailsPanel, "Reference", referenceId);
        }
        
        // Add new balance after transaction
        addDetailRow(detailsPanel, "New Balance", String.format("₱%.2f", newBalance));
        
        // Add username
        addDetailRow(detailsPanel, "User", currentUsername != null ? currentUsername : "Unknown");
        
        // Add date and time
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMM dd, yyyy");
        java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm:ss");
        addDetailRow(detailsPanel, "Date", dateFormat.format(timestamp));
        addDetailRow(detailsPanel, "Time", timeFormat.format(timestamp));
        
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(WalletHistory.this);
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
        historyScrollPane.setBounds(30, 150, 310, 520);

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
        closeButton.setToolTipText("Back to Wallet");
        closeButton.setBorder(null);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        add(closeButton);
        closeButton.setBounds(10, 110, 75, 23);

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
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.Home());
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

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
       
    }//GEN-LAST:event_closeButtonActionPerformed


   

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
        // Clean up resources when panel is removed
        super.removeNotify();
    }
}
