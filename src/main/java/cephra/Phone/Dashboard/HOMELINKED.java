
package cephra.Phone.Dashboard;

import javax.swing.SwingUtilities;

public class HOMELINKED extends javax.swing.JPanel {

    // Car images array - variant versions for HomeLinked (C6.1, C9.1, etc.)
    private static final String[] carImages = {
        "/cephra/Cephra Images/c1.1.png",
        "/cephra/Cephra Images/c2.1.png", 
        "/cephra/Cephra Images/c3.1.png",
        "/cephra/Cephra Images/c4.1.png",
        "/cephra/Cephra Images/c5.1.png",
        "/cephra/Cephra Images/c6.1.png",
        "/cephra/Cephra Images/c7.1.png",
        "/cephra/Cephra Images/c8.1.png",
        "/cephra/Cephra Images/c9.1.png",
        "/cephra/Cephra Images/c10.1.png"
    };

    public HOMELINKED() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        
        // Load wallet balance and reward points
        loadWalletBalance();
        loadRewardPoints();
        
        // Set car image to match the linked car
        setLinkedCarImage();
        
        // Add listeners to refresh car image when panel becomes visible
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                setLinkedCarImage(); // Refresh car image when panel gains focus
            }
        });
        
        addHierarchyListener(new java.awt.event.HierarchyListener() {
            @Override
            public void hierarchyChanged(java.awt.event.HierarchyEvent e) {
                if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0) {
                    if (isShowing()) {
                        setLinkedCarImage(); // Refresh car image when panel becomes visible
                    }
                }
            }
        });
    }
    
    /**
     * Load wallet balance from database
     */
    private void loadWalletBalance() {
        try {
            String username = cephra.Database.CephraDB.getCurrentUsername();
            if (username != null && !username.isEmpty()) {
                double balance = cephra.Database.CephraDB.getUserWalletBalance(username);
                pesobalance.setText(String.format("%.0f", balance));
            }
        } catch (Exception e) {
            System.err.println("Error loading wallet balance: " + e.getMessage());
            pesobalance.setText("0");
        }
    }
    
    /**
     * Load reward points from database
     */
    private void loadRewardPoints() {
        try {
            String username = cephra.Database.CephraDB.getCurrentUsername();
            if (username != null && !username.isEmpty()) {
                int points = cephra.Phone.Utilities.RewardSystem.getUserPoints(username);
                rewardbalance.setText(String.valueOf(points));
            }
        } catch (Exception e) {
            System.err.println("Error loading reward points: " + e.getMessage());
            rewardbalance.setText("0");
        }
    }
    
    /**
     * Set car image to match the user's linked car from database
     */
    private void setLinkedCarImage() {
        try {
            String username = cephra.Database.CephraDB.getCurrentUsername();
            if (username != null && !username.isEmpty()) {
                // Get user's car index from database
                int carIndex = cephra.Database.CephraDB.getUserCarIndex(username);
                
                // If no car assigned yet, assign a random one (same as LinkedCar)
                if (carIndex == -1) {
                    carIndex = new java.util.Random().nextInt(carImages.length);
                    cephra.Database.CephraDB.setUserCarIndex(username, carIndex);
                    System.out.println("HomeLinked: Assigned car " + (carIndex + 1) + " to user " + username);
                }
                
                // Set the car image while preserving the form positioning (x=31, y=173, size=307x209)
                if (carIndex >= 0 && carIndex < carImages.length) {
                    CAR.setIcon(new javax.swing.ImageIcon(getClass().getResource(carImages[carIndex])));
                    System.out.println("HomeLinked: Set car image to " + carImages[carIndex] + " for user " + username);
                }
            }
        } catch (Exception e) {
            System.err.println("Error setting linked car image: " + e.getMessage());
        }
    }

   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CAR = new javax.swing.JLabel();
        Notifications = new javax.swing.JButton();
        charge = new javax.swing.JButton();
        pesobalance = new javax.swing.JLabel();
        rewardbalance = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        linkbutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        profilebutton = new javax.swing.JButton();
        rewards = new javax.swing.JButton();
        wallet = new javax.swing.JButton();

        setLayout(null);

        // Car image will be set dynamically in setLinkedCarImage() method
        add(CAR);
        CAR.setBounds(31, 173, 307, 209);

        Notifications.setBorder(null);
        Notifications.setBorderPainted(false);
        Notifications.setContentAreaFilled(false);
        Notifications.setFocusPainted(false);
        Notifications.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NotificationsActionPerformed(evt);
            }
        });
        add(Notifications);
        Notifications.setBounds(310, 50, 40, 50);

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
        charge.setBounds(50, 680, 40, 40);

        pesobalance.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pesobalance.setForeground(new java.awt.Color(255, 255, 255));
        pesobalance.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pesobalance.setText("500");
        add(pesobalance);
        pesobalance.setBounds(191, 68, 80, 20);

        rewardbalance.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rewardbalance.setForeground(new java.awt.Color(255, 255, 255));
        rewardbalance.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rewardbalance.setText("500");
        add(rewardbalance);
        rewardbalance.setBounds(270, 67, 40, 20);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Home_LINKED.png"))); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(350, 750));
        add(jLabel1);
        jLabel1.setBounds(0, 0, 350, 750);

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
        profilebutton.setBounds(280, 670, 40, 50);

        rewards.setBorder(null);
        rewards.setBorderPainted(false);
        rewards.setContentAreaFilled(false);
        rewards.setFocusPainted(false);
        rewards.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rewardsActionPerformed(evt);
            }
        });
        add(rewards);
        rewards.setBounds(30, 610, 150, 60);

        wallet.setBorder(null);
        wallet.setBorderPainted(false);
        wallet.setContentAreaFilled(false);
        wallet.setFocusPainted(false);
        wallet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                walletActionPerformed(evt);
            }
        });
        add(wallet);
        wallet.setBounds(190, 600, 150, 70);
    }// </editor-fold>//GEN-END:initComponents

    private void NotificationsActionPerformed(java.awt.event.ActionEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        cephra.Phone.Dashboard.NotificationHistory notificationHistory = new cephra.Phone.Dashboard.NotificationHistory();
                        notificationHistory.setPreviousPanel(cephra.Phone.Dashboard.Home.getAppropriateHomePanel());
                        phoneFrame.switchPanel(notificationHistory);
                        break;
                    }
                }
            }
        });
    }

    private void rewardsActionPerformed(java.awt.event.ActionEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.RewardsWallet.Rewards());
                        break;
                    }
                }
            }
        });
    }

    private void walletActionPerformed(java.awt.event.ActionEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.RewardsWallet.Wallet());
                        break;
                    }
                }
            }
        });
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



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CAR;
    private javax.swing.JButton Notifications;
    private javax.swing.JButton charge;
    private javax.swing.JButton historybutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton linkbutton;
    private javax.swing.JLabel pesobalance;
    private javax.swing.JButton profilebutton;
    private javax.swing.JLabel rewardbalance;
    private javax.swing.JButton rewards;
    private javax.swing.JButton wallet;
    // End of variables declaration//GEN-END:variables
}
