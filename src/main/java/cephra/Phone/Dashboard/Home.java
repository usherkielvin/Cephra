package cephra.Phone.Dashboard;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.SwingUtilities;

public class Home extends javax.swing.JPanel {

    /**
     * Static utility method to get the appropriate home panel based on car linking status
     * @return Home panel if car not linked, HomeLinked panel if car is linked
     */
    public static javax.swing.JPanel getAppropriateHomePanel() {
        if (cephra.Phone.Utilities.AppState.isCarLinked) {
            try {
                String username = cephra.Database.CephraDB.getCurrentUsername();
                if (username != null && !username.isEmpty()) {
                    int batteryLevel = cephra.Database.CephraDB.getUserBatteryLevel(username);
                    if (batteryLevel != -1) {
                        // Car is linked and battery is initialized - return HomeLinked
                        return new cephra.Phone.Dashboard.HOMELINKED();
                    }
                }
            } catch (Exception e) {
                System.err.println("Error checking battery level for home panel: " + e.getMessage());
            }
        }
        // Car not linked or no battery - return regular Home
        return new cephra.Phone.Dashboard.Home();
    }

    public Home() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        setupLabelPosition(); // Set label position
        makeDraggable();
        
        // Check if car is linked and switch to HomeLinked if needed
        if (cephra.Phone.Utilities.AppState.isCarLinked) {
            try {
                String username = cephra.Database.CephraDB.getCurrentUsername();
                if (username != null && !username.isEmpty()) {
                    int batteryLevel = cephra.Database.CephraDB.getUserBatteryLevel(username);
                    if (batteryLevel != -1) {
                        // Car is linked and battery is initialized - go to HomeLinked
                        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                java.awt.Window[] windows = java.awt.Window.getWindows();
                                for (java.awt.Window window : windows) {
                                    if (window instanceof cephra.Frame.Phone) {
                                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.HOMELINKED());
                                        break;
                                    }
                                }
                            }
                        });
                        return; // Exit early since we're switching to HomeLinked
                    }
                }
            } catch (Exception e) {
                System.err.println("Error checking battery level in Home constructor: " + e.getMessage());
            }
        }
        
        // Get current user's firstname and display welcome message
        if (LoggedName != null) {
            String firstname = cephra.Database.CephraDB.getCurrentFirstname();
            String safeFirstname = firstname != null ? firstname.trim() : "";
            if (safeFirstname.isEmpty()) {
                LoggedName.setText("Welcome to Cephra!");
            } else {
                // Get only the first word of the firstname
                String firstWord = safeFirstname.split("\\s+")[0];
                LoggedName.setText("Welcome to Cephra, " + firstWord + "!");
            }
        }
        
        // Load wallet balance and reward points
        loadWalletBalance();
        loadRewardPoints();
        
        // Background is moved up by 3 pixels via bounds
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(Home.this);
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

        rewardbalance = new javax.swing.JLabel();
        pesobalance = new javax.swing.JLabel();
        LinkVehicle = new javax.swing.JButton();
        Notifications = new javax.swing.JButton();
        profilebutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        charge = new javax.swing.JButton();
        wallet = new javax.swing.JButton();
        rewards = new javax.swing.JButton();
        LoggedName = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(370, 750));
        setPreferredSize(new java.awt.Dimension(370, 750));
        setLayout(null);

        rewardbalance.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rewardbalance.setForeground(new java.awt.Color(255, 255, 255));
        rewardbalance.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rewardbalance.setText("500");
        add(rewardbalance);
        rewardbalance.setBounds(268, 68, 40, 20);

        pesobalance.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pesobalance.setForeground(new java.awt.Color(255, 255, 255));
        pesobalance.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pesobalance.setText("500");
        add(pesobalance);
        pesobalance.setBounds(186, 68, 40, 20);

        LinkVehicle.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        LinkVehicle.setForeground(new java.awt.Color(255, 255, 255));
        LinkVehicle.setBorder(null);
        LinkVehicle.setBorderPainted(false);
        LinkVehicle.setContentAreaFilled(false);
        LinkVehicle.setFocusPainted(false);
        LinkVehicle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        LinkVehicle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LinkVehicleActionPerformed(evt);
            }
        });
        add(LinkVehicle);
        LinkVehicle.setBounds(30, 340, 310, 40);

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
        wallet.setBounds(190, 615, 140, 50);

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
        rewards.setBounds(30, 610, 150, 50);

        LoggedName.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LoggedName.setForeground(new java.awt.Color(0, 204, 204));
        LoggedName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LoggedName.setText("Welcome to Cephra, Dizon");
        add(LoggedName);
        LoggedName.setBounds(30, 100, 300, 50);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/HOME.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 350, 750);
    }// </editor-fold>//GEN-END:initComponents

    // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(0, 0, 370, 750);
        }
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
    
                                                    
    private void NotificationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NotificationsActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        cephra.Phone.Dashboard.NotificationHistory notificationHistory = new cephra.Phone.Dashboard.NotificationHistory();
                        notificationHistory.setPreviousPanel(getAppropriateHomePanel());
                        phoneFrame.switchPanel(notificationHistory);
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_NotificationsActionPerformed

    private void walletActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_walletActionPerformed
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
    }//GEN-LAST:event_walletActionPerformed

    private void rewardsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rewardsActionPerformed
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
    }//GEN-LAST:event_rewardsActionPerformed

    private void LinkVehicleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LinkVehicleActionPerformed
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
    }//GEN-LAST:event_LinkVehicleActionPerformed

    /**
     * Loads and displays the current wallet balance
     */
    private void loadWalletBalance() {
        try {
            String currentUser = cephra.Database.CephraDB.getCurrentUsername();
            if (currentUser != null && !currentUser.isEmpty()) {
                double balance = cephra.Database.CephraDB.getUserWalletBalance(currentUser);
                pesobalance.setText(String.format("%.2f", balance));
            } else {
                pesobalance.setText("0.00");
            }
        } catch (Exception e) {
            System.err.println("Error loading wallet balance: " + e.getMessage());
            pesobalance.setText("0.00");
        }
    }

    /**
     * Loads and displays the current reward points
     */
    private void loadRewardPoints() {
        try {
            String currentUser = cephra.Database.CephraDB.getCurrentUsername();
            if (currentUser != null && !currentUser.isEmpty()) {
                int points = cephra.Phone.Utilities.RewardSystem.getUserPoints(currentUser);
                rewardbalance.setText(String.valueOf(points));
            } else {
                rewardbalance.setText("0");
            }
        } catch (Exception e) {
            System.err.println("Error loading reward points: " + e.getMessage());
            rewardbalance.setText("0");
        }
    }

    /**
     * Public method to refresh both wallet balance and reward points when returning to this screen
     */
    public void refreshBalances() {
        loadWalletBalance();
        loadRewardPoints();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton LinkVehicle;
    private javax.swing.JLabel LoggedName;
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

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition();
                // Ensure background stays behind controls so edited buttons are visible
                if (jLabel1 != null) {
                    setComponentZOrder(jLabel1, getComponentCount() - 1);
                    revalidate();
                    repaint();
                }
                // Refresh balances when panel becomes visible
                refreshBalances();
            }
        });
    }
}
