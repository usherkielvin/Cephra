package cephra.Phone;
import javax.swing.SwingUtilities;
public class Wallet extends javax.swing.JPanel {

     private static float balance = 1000.0f;
    
    private boolean balanceHidden = false; 
    public Wallet() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        loadWalletData();
        HideBalance.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png")));
        
        
        SwingUtilities.invokeLater(() -> {
            if (cephra.Phone.PayPop.hasPendingPayPop()) {
                System.out.println("Wallet: Detected pending PayPop, restoring after top-up");
                cephra.Phone.PayPop.restorePayPopAfterTopUp();
            }
            
            
            refreshWalletData();
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        profilebutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        Topupbtn = new javax.swing.JButton();
        charge = new javax.swing.JButton();
        Balancetxt = new javax.swing.JLabel();
        HideBalance = new javax.swing.JButton();
        Latestranscat = new javax.swing.JPanel();
        historybutton1 = new javax.swing.JButton();
        bckToHome = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

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
        historybutton.setBounds(210, 680, 50, 40);

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

        Topupbtn.setBorderPainted(false);
        Topupbtn.setContentAreaFilled(false);
        Topupbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TopupbtnActionPerformed(evt);
            }
        });
        add(Topupbtn);
        Topupbtn.setBounds(50, 310, 70, 50);

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

        Balancetxt.setFont(new java.awt.Font("Segoe UI Semibold", 1, 40)); // NOI18N
        Balancetxt.setForeground(new java.awt.Color(255, 255, 255));
        Balancetxt.setText("1000.00");
        add(Balancetxt);
        Balancetxt.setBounds(80, 200, 200, 60);

        HideBalance.setBorderPainted(false);
        HideBalance.setContentAreaFilled(false);
        HideBalance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HideBalanceActionPerformed(evt);
            }
        });
        add(HideBalance);
        HideBalance.setBounds(250, 190, 90, 90);

        Latestranscat.setOpaque(false);
        add(Latestranscat);
        Latestranscat.setBounds(40, 430, 300, 240);

        historybutton1.setBorder(null);
        historybutton1.setBorderPainted(false);
        historybutton1.setContentAreaFilled(false);
        historybutton1.setFocusPainted(false);
        historybutton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historybutton1ActionPerformed(evt);
            }
        });
        add(historybutton1);
        historybutton1.setBounds(160, 320, 50, 40);

        bckToHome.setBorderPainted(false);
        bckToHome.setContentAreaFilled(false);
        bckToHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bckToHomeActionPerformed(evt);
            }
        });
        add(bckToHome);
        bckToHome.setBounds(170, 680, 30, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Wallet.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 370, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void HideBalanceActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        balanceHidden = !balanceHidden;
        updateBalanceDisplay();      
        if (balanceHidden) {
         //   HideBalance.setText("Show");
            HideBalance.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeOpen.png")));
        } else {
          //  HideBalance.setText("Hide");
               HideBalance.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png")));
        }
    }                                           

    private void loadWalletData() {
        try {
            String currentUser = cephra.CephraDB.getCurrentUsername();
            if (currentUser != null && !currentUser.isEmpty()) {
                // Load balance from database
                balance = (float) cephra.CephraDB.getUserWalletBalance(currentUser);
                updateBalanceDisplay();
                loadTransactionHistory();
            } else {
                // No user logged in, show default values
                balance = 0.0f;
                Balancetxt.setText("0.00");
                clearTransactionHistory();
            }
        } catch (Exception e) {
            System.err.println("Error loading wallet data: " + e.getMessage());
            e.printStackTrace();
            balance = 0.0f;
            Balancetxt.setText("0.00");
            clearTransactionHistory();
        }
    }
 
    private void updateBalanceDisplay() {
        if (balanceHidden) {
            Balancetxt.setText("******");
        } else {
            Balancetxt.setText(String.format("%.2f", balance));
        }
    }
    
    private void loadTransactionHistory() {
        try {
            String currentUser = cephra.CephraDB.getCurrentUsername();
            if (currentUser == null || currentUser.isEmpty()) {
                clearTransactionHistory();
                return;
            }
            
            java.util.List<Object[]> transactions = cephra.CephraDB.getWalletTransactionHistory(currentUser);
            displayTransactionHistory(transactions);
            
        } catch (Exception e) {
            System.err.println("Error loading transaction history: " + e.getMessage());
            e.printStackTrace();
            clearTransactionHistory();
        }
    }
    
    /**
     * Displays transaction history in the panel
     */
    private void displayTransactionHistory(java.util.List<Object[]> transactions) {
        // Clear existing components
        Latestranscat.removeAll();
        Latestranscat.setLayout(new javax.swing.BoxLayout(Latestranscat, javax.swing.BoxLayout.Y_AXIS));
        
        if (transactions.isEmpty()) {
            javax.swing.JLabel noTransactionsLabel = new javax.swing.JLabel("No recent transactions");
            noTransactionsLabel.setFont(new java.awt.Font("Segoe UI", 0, 12));
            noTransactionsLabel.setForeground(java.awt.Color.GRAY);
            noTransactionsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
         //  noTransactionsLabel.setAlignmentX(javax.swing.CENTER_ALIGNMENT);
            Latestranscat.setLayout(new java.awt.BorderLayout());
            Latestranscat.add(javax.swing.Box.createVerticalGlue(), java.awt.BorderLayout.NORTH);
            Latestranscat.add(noTransactionsLabel, java.awt.BorderLayout.CENTER);
            Latestranscat.add(javax.swing.Box.createVerticalGlue(), java.awt.BorderLayout.SOUTH);
        } else {
            // Add title
           
            
            // Add each transaction
            for (Object[] transaction : transactions) {
                String transactionType = (String) transaction[0];
                double amount = (Double) transaction[1];
                String description = (String) transaction[3];
                java.sql.Timestamp timestamp = (java.sql.Timestamp) transaction[5];
                
                javax.swing.JPanel transactionPanel = createTransactionPanel(transactionType, amount, description, timestamp);
                Latestranscat.add(transactionPanel);
                Latestranscat.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 5)));
            }
        }
        
        // Refresh the display
        Latestranscat.revalidate();
        Latestranscat.repaint();
    }
    
    /**
     * Creates a transaction panel for display
     */
    private javax.swing.JPanel createTransactionPanel(String transactionType, double amount, String description, java.sql.Timestamp timestamp) {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(new java.awt.BorderLayout());
        panel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 60));
        panel.setPreferredSize(new java.awt.Dimension(280, 60));
        
        // Left side - transaction info
        javax.swing.JPanel leftPanel = new javax.swing.JPanel();
        leftPanel.setLayout(new javax.swing.BoxLayout(leftPanel, javax.swing.BoxLayout.Y_AXIS));
        
        javax.swing.JLabel typeLabel = new javax.swing.JLabel(getTransactionTypeDisplay(transactionType));
        typeLabel.setFont(new java.awt.Font("Segoe UI", 1, 12));
        
        leftPanel.add(typeLabel);
        
        javax.swing.JLabel descLabel = new javax.swing.JLabel(truncateDescription(description));
        descLabel.setFont(new java.awt.Font("Segoe UI", 0, 10));
        descLabel.setForeground(java.awt.Color.GRAY);
        leftPanel.add(descLabel);
        
        // Right side - amount and date
        javax.swing.JPanel rightPanel = new javax.swing.JPanel();
        rightPanel.setLayout(new javax.swing.BoxLayout(rightPanel, javax.swing.BoxLayout.Y_AXIS));
        
        javax.swing.JLabel amountLabel = new javax.swing.JLabel(formatTransactionAmount(transactionType, amount));
        amountLabel.setFont(new java.awt.Font("Segoe UI", 1, 12));
        amountLabel.setForeground(getAmountColor(transactionType));
        rightPanel.add(amountLabel);
        
        javax.swing.JLabel dateLabel = new javax.swing.JLabel(formatTransactionDate(timestamp));
        dateLabel.setFont(new java.awt.Font("Segoe UI", 0, 10));
        dateLabel.setForeground(java.awt.Color.GRAY);
        rightPanel.add(dateLabel);
        
        panel.add(leftPanel, java.awt.BorderLayout.WEST);
        panel.add(rightPanel, java.awt.BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Clears the transaction history display
     */
    private void clearTransactionHistory() {
        Latestranscat.removeAll();
        Latestranscat.revalidate();
        Latestranscat.repaint();
    }
    
    // Helper methods for transaction display formatting
    private String getTransactionTypeDisplay(String transactionType) {
        switch (transactionType) {
            case "TOP_UP": return "Top Up";
            case "PAYMENT": return "Payment";
           
            default: return transactionType;
        }
    }
    //walang refund ok rpompt error
    
    private String truncateDescription(String description) {
        if (description == null) return "";
        return description.length() > 50 ? description.substring(0, 50) + "..." : description;
    }
    
    private String formatTransactionAmount(String transactionType, double amount) {
        String sign = transactionType.equals("TOP_UP") ? "+" : "-";
        return sign + String.format("₱%.2f", Math.abs(amount));
    }
    
    private java.awt.Color getAmountColor(String transactionType) {
        return transactionType.equals("TOP_UP") ? 
               new java.awt.Color(0, 150, 0) : new java.awt.Color(200, 0, 0);
    }
    
    private String formatTransactionDate(java.sql.Timestamp timestamp) {
        if (timestamp == null) return "";
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd HH:mm");
        return dateFormat.format(timestamp);
    }
    
    /**
     * Public method to refresh wallet data - can be called when returning to wallet screen
     */
    public void refreshWalletData() {
        loadWalletData();
    }

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

    private void historybutton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historybutton1ActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        cephra.Phone.WalletHistory walletHistory = new cephra.Phone.WalletHistory();
                        walletHistory.setPreviousPanel(Wallet.this); // Set current wallet panel as previous
                        phoneFrame.switchPanel(walletHistory);
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_historybutton1ActionPerformed

    private void TopupbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TopupbtnActionPerformed
       
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.TopUppanel());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_TopupbtnActionPerformed

    private void bckToHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bckToHomeActionPerformed
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
    }//GEN-LAST:event_bckToHomeActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Balancetxt;
    private javax.swing.JButton HideBalance;
    private javax.swing.JPanel Latestranscat;
    private javax.swing.JButton Topupbtn;
    private javax.swing.JButton bckToHome;
    private javax.swing.JButton charge;
    private javax.swing.JButton historybutton;
    private javax.swing.JButton historybutton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton linkbutton;
    private javax.swing.JButton profilebutton;
    // End of variables declaration//GEN-END:variables
}
