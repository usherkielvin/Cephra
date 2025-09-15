
package cephra.Phone.RewardsWallet;

import javax.swing.SwingUtilities;


public class Rewards extends javax.swing.JPanel {
    
    // Points system variables
    private int currentPoints = 0;
    private final int[] ITEM_PRICES = {50, 35, 70, 100, 10, 10, 5, 5}; // a1-a8 prices
    private final String[] ITEM_NAMES = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8"};
    
    // Current user for database operations
    private String currentUsername = null;
    
    // Static methods for database-connected points management
    public static void addPointsForPaymentGlobally(String username, double amountPaid, String ticketId) {
        if (username != null && !username.trim().isEmpty()) {
            cephra.Phone.Utilities.RewardSystem.addPointsForPayment(username, amountPaid, ticketId);
        }
    }


    public Rewards() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        
        // Get current user from session
        currentUsername = getCurrentUser();
        
        // Load points from database
        loadPointsFromDatabase();
        
        // Setup scroll pane content - this won't be overwritten by NetBeans
        // Use SwingUtilities.invokeLater to ensure everything is ready
        javax.swing.SwingUtilities.invokeLater(() -> {
            setupScrollPaneContent();
        });
    }
    
    private void setupScrollPaneContent() {
        // This method runs AFTER initComponents() and will override any NetBeans layout changes
        // NetBeans can't touch this code, so it will always work correctly
        
        try {
            // Create content panel with vertical BoxLayout for flexible sizing
            javax.swing.JPanel contentPanel = new javax.swing.JPanel();
            contentPanel.setLayout(new javax.swing.BoxLayout(contentPanel, javax.swing.BoxLayout.Y_AXIS));
            contentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            contentPanel.setBackground(null);
            contentPanel.setOpaque(false);
            contentPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            
            // Clear the scroll pane first
            jScrollPane1.setViewportView(null);
            
            // Helper function to create header row panels (F panels - smaller)
            java.util.function.BiFunction<javax.swing.JComponent, javax.swing.JComponent, javax.swing.JPanel> createHeaderRow = 
                (left, right) -> {
                    javax.swing.JPanel rowPanel = new javax.swing.JPanel(new java.awt.GridLayout(1, 2, 0, 0));
                    rowPanel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 60)); // Smaller height for headers
                    rowPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                    rowPanel.setBorder(null);
                    rowPanel.setOpaque(false);
                    rowPanel.setBackground(null);
                    
                    // Remove components from their current parents
                    if (left.getParent() != null) left.getParent().remove(left);
                    if (right.getParent() != null) right.getParent().remove(right);
                    
                    // Set smaller preferred sizes for headers and align to left
                    left.setPreferredSize(new java.awt.Dimension(100, 50));
                    right.setPreferredSize(new java.awt.Dimension(100, 50));
                    left.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                    right.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                    
                    rowPanel.add(left);
                    rowPanel.add(right);
                    
                    return rowPanel;
                };
            
            // Helper function to create content row panels (A panels - larger)
            java.util.function.BiFunction<javax.swing.JComponent, javax.swing.JComponent, javax.swing.JPanel> createContentRow = 
                (left, right) -> {
                    javax.swing.JPanel rowPanel = new javax.swing.JPanel(new java.awt.GridLayout(1, 2, 0, 0));
                    rowPanel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 140)); // Larger height for content
                    rowPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                    rowPanel.setBorder(null);
                    rowPanel.setOpaque(false);
                    rowPanel.setBackground(null);
                    
                    // Remove components from their current parents
                    if (left.getParent() != null) left.getParent().remove(left);
                    if (right.getParent() != null) right.getParent().remove(right);
                    
                    // Set larger preferred sizes for content panels and align to left
                    left.setPreferredSize(new java.awt.Dimension(157, 150));
                    right.setPreferredSize(new java.awt.Dimension(157, 150));
                    left.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                    right.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                    
                    rowPanel.add(left);
                    rowPanel.add(right);
                    
                    return rowPanel;
                };
            
            // Row 1: F1  F2 (headers - smaller)
            contentPanel.add(createHeaderRow.apply(f1, f1));
            contentPanel.add(javax.swing.Box.createVerticalStrut(5));
            
            // Row 2: a1  a2 (content panels - larger)
            contentPanel.add(createContentRow.apply(a1, a2));
            contentPanel.add(javax.swing.Box.createVerticalStrut(15));
            
            // Row 3: F3  F4 (headers - smaller)
            contentPanel.add(createHeaderRow.apply(f2, f2));
            contentPanel.add(javax.swing.Box.createVerticalStrut(5));
            
            // Row 4: a3  a4 (content panels - larger)
            contentPanel.add(createContentRow.apply(a3, a4));
            contentPanel.add(javax.swing.Box.createVerticalStrut(15));
            
            // Row 5: F5  F6 (headers - smaller) - Create additional header panels
           contentPanel.add(createContentRow.apply(f3, f3));
            contentPanel.add(javax.swing.Box.createVerticalStrut(5));
            
             contentPanel.add(createContentRow.apply(a5, a6));
            contentPanel.add(javax.swing.Box.createVerticalStrut(15));
            
            contentPanel.add(createHeaderRow.apply(f4, f4));
            contentPanel.add(javax.swing.Box.createVerticalStrut(5));
            
            // Row 6: a5  a6 (content panels - larger)
            contentPanel.add(createContentRow.apply(a7, a8));
            contentPanel.add(javax.swing.Box.createVerticalStrut(15));
            
            // Row 7: F7  F8 (headers - smaller) - Create additional header panels
         
            
         
            
            // Set the content panel as the viewport of the scroll pane
            jScrollPane1.setViewportView(contentPanel);
            jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
            
            // Force a repaint to ensure everything displays correctly
            jScrollPane1.revalidate();
            jScrollPane1.repaint();
            this.revalidate();
            this.repaint();
            
            // Debug: Print success message
            System.out.println("Scroll pane setup completed successfully with " + contentPanel.getComponentCount() + " components");
            
        } catch (Exception e) {
            System.err.println("Error setting up scroll pane content: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Database-connected points system methods
    private String getCurrentUser() {
        // Get current logged-in user from CephraDB
        try {
            return cephra.Database.CephraDB.getCurrentPhoneUsername();
        } catch (Exception e) {
            System.out.println("Could not get current user: " + e.getMessage());
            return null;
        }
    }
    
    private void loadPointsFromDatabase() {
        if (currentUsername != null && !currentUsername.trim().isEmpty()) {
            currentPoints = cephra.Phone.Utilities.RewardSystem.getUserPoints(currentUsername);
            System.out.println("Loaded " + currentPoints + " points from database for user: " + currentUsername);
        } else {
            currentPoints = 0;
            System.out.println("No current user, points set to 0");
        }
        updatePointsDisplay();
    }
    
    private void updatePointsDisplay() {
        points.setText(String.valueOf(currentPoints));
        this.repaint();
    }
    
    public void addPoints(int pointsToAdd, String description, String referenceId) {
        if (currentUsername != null && !currentUsername.trim().isEmpty()) {
            boolean success = cephra.Phone.Utilities.RewardSystem.addPoints(currentUsername, pointsToAdd, description, referenceId);
            if (success) {
                loadPointsFromDatabase(); // Reload from database
                System.out.println("Added " + pointsToAdd + " points to " + currentUsername + ". Total: " + currentPoints);
            }
        } else {
            System.out.println("No current user to add points to");
        }
    }
    
    public void addPointsForPayment(double amountPaid, String ticketId) {
        if (currentUsername != null && !currentUsername.trim().isEmpty()) {
            addPointsForPaymentGlobally(currentUsername, amountPaid, ticketId);
        }
    }
    
    public void syncWithDatabase() {
        loadPointsFromDatabase();
    }
    
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            // Sync with database whenever the panel becomes visible
            currentUsername = getCurrentUser(); // Refresh current user
            syncWithDatabase();
        }
        super.setVisible(visible);
    }
    
    private boolean purchaseItem(int itemIndex) {
        if (currentUsername == null || currentUsername.trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Please log in to make purchases.",
                "Login Required", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        int price = ITEM_PRICES[itemIndex];
        String itemName = ITEM_NAMES[itemIndex];
        
        // Sync with database before purchase
        syncWithDatabase();
        
        if (currentPoints >= price) {
            // Spend points using database
            String description = "Purchased " + itemName + " (Item " + (itemIndex + 1) + ")";
            boolean success = cephra.Phone.Utilities.RewardSystem.spendPoints(currentUsername, price, description, "ITEM_" + (itemIndex + 1));
            
            if (success) {
                // Reload points from database
                loadPointsFromDatabase();
                
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Thank you for purchasing " + itemName + "!\n" +
                    "Points used: " + price + "\n" +
                    "Remaining points: " + currentPoints,
                    "Purchase Successful", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                
                System.out.println("Purchased " + itemName + " for " + price + " points. Remaining: " + currentPoints);
                return true;
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Purchase failed. Please try again.",
                    "Purchase Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Insufficient points!\n" +
                "Required: " + price + " points\n" +
                "You have: " + currentPoints + " points",
                "Cannot Purchase", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        f1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        f4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        f3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        f2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        a1 = new javax.swing.JButton();
        a2 = new javax.swing.JButton();
        a3 = new javax.swing.JButton();
        a4 = new javax.swing.JButton();
        a5 = new javax.swing.JButton();
        a6 = new javax.swing.JButton();
        a7 = new javax.swing.JButton();
        a8 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        points = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        homebutton2 = new javax.swing.JButton();
        profilebutton = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        charge = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(null);

        f1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/f1.png"))); // NOI18N

        javax.swing.GroupLayout f1Layout = new javax.swing.GroupLayout(f1);
        f1.setLayout(f1Layout);
        f1Layout.setHorizontalGroup(
            f1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 52, Short.MAX_VALUE))
        );
        f1Layout.setVerticalGroup(
            f1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(f1);
        f1.setBounds(900, 230, 310, 60);

        f4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/f4.png"))); // NOI18N

        javax.swing.GroupLayout f4Layout = new javax.swing.GroupLayout(f4);
        f4.setLayout(f4Layout);
        f4Layout.setHorizontalGroup(
            f4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f4Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 52, Short.MAX_VALUE))
        );
        f4Layout.setVerticalGroup(
            f4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(f4);
        f4.setBounds(910, 460, 310, 60);

        f3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/f3.png"))); // NOI18N

        javax.swing.GroupLayout f3Layout = new javax.swing.GroupLayout(f3);
        f3.setLayout(f3Layout);
        f3Layout.setHorizontalGroup(
            f3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f3Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 52, Short.MAX_VALUE))
        );
        f3Layout.setVerticalGroup(
            f3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(f3);
        f3.setBounds(910, 390, 310, 60);

        f2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/f2.png"))); // NOI18N

        javax.swing.GroupLayout f2Layout = new javax.swing.GroupLayout(f2);
        f2.setLayout(f2Layout);
        f2Layout.setHorizontalGroup(
            f2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f2Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 52, Short.MAX_VALUE))
        );
        f2Layout.setVerticalGroup(
            f2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(f2);
        f2.setBounds(900, 310, 310, 60);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBorder(null);
        jScrollPane1.setOpaque(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(jPanel1);
        jPanel1.setBounds(9, 129, 370, 485);

        a1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Rewards_1.png"))); // NOI18N
        a1.setBorder(null);
        a1.setBorderPainted(false);
        a1.setContentAreaFilled(false);
        a1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a1ActionPerformed(evt);
            }
        });
        add(a1);
        a1.setBounds(570, 90, 150, 150);

        a2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Rewards_2.png"))); // NOI18N
        a2.setBorder(null);
        a2.setBorderPainted(false);
        a2.setContentAreaFilled(false);
        a2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a2ActionPerformed(evt);
            }
        });
        add(a2);
        a2.setBounds(740, 90, 150, 150);

        a3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Rewards_3.png"))); // NOI18N
        a3.setBorder(null);
        a3.setBorderPainted(false);
        a3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a3ActionPerformed(evt);
            }
        });
        add(a3);
        a3.setBounds(570, 250, 150, 150);

        a4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Rewards_4.png"))); // NOI18N
        a4.setBorder(null);
        a4.setBorderPainted(false);
        a4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a4ActionPerformed(evt);
            }
        });
        add(a4);
        a4.setBounds(740, 250, 150, 150);

        a5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Rewards_5.png"))); // NOI18N
        a5.setBorder(null);
        a5.setBorderPainted(false);
        a5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a5ActionPerformed(evt);
            }
        });
        add(a5);
        a5.setBounds(570, 420, 150, 150);

        a6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Rewards_6.png"))); // NOI18N
        a6.setBorder(null);
        a6.setBorderPainted(false);
        a6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a6ActionPerformed(evt);
            }
        });
        add(a6);
        a6.setBounds(740, 410, 150, 150);

        a7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Rewards_7.png"))); // NOI18N
        a7.setBorder(null);
        a7.setBorderPainted(false);
        a7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a7ActionPerformed(evt);
            }
        });
        add(a7);
        a7.setBounds(570, 580, 150, 150);

        a8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Rewards_8.png"))); // NOI18N
        a8.setBorder(null);
        a8.setBorderPainted(false);
        a8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a8ActionPerformed(evt);
            }
        });
        add(a8);
        a8.setBounds(740, 570, 150, 150);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/rewardtext.png"))); // NOI18N
        jLabel4.setText("jLabel4");
        add(jLabel4);
        jLabel4.setBounds(20, 50, 260, 50);

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Points :");
        add(jLabel5);
        jLabel5.setBounds(260, 90, 50, 16);

        points.setForeground(new java.awt.Color(51, 204, 255));
        points.setText("0");
        add(points);
        points.setBounds(310, 90, 37, 16);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/navbar.png"))); // NOI18N
        add(jLabel7);
        jLabel7.setBounds(30, 670, 310, 60);

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
        homebutton2.setBounds(170, 680, 30, 40);

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
    }// </editor-fold>//GEN-END:initComponents

    private void a1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a1ActionPerformed
        purchaseItem(0); // a1 = 50 points
    }//GEN-LAST:event_a1ActionPerformed

    private void a2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a2ActionPerformed
        purchaseItem(1); // a2 = 35 points
    }//GEN-LAST:event_a2ActionPerformed

    private void a3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a3ActionPerformed
        purchaseItem(2); // a3 = 70 points
    }//GEN-LAST:event_a3ActionPerformed

    private void a4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a4ActionPerformed
        purchaseItem(3); // a4 = 100 points
    }//GEN-LAST:event_a4ActionPerformed

    private void a5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a5ActionPerformed
        purchaseItem(4); // a5 = 10 points
    }//GEN-LAST:event_a5ActionPerformed

    private void a6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a6ActionPerformed
        purchaseItem(5); // a6 = 10 points
    }//GEN-LAST:event_a6ActionPerformed

    private void a7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a7ActionPerformed
        purchaseItem(6); // a7 = 5 points
    }//GEN-LAST:event_a7ActionPerformed

    private void a8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a8ActionPerformed
        purchaseItem(7); // a8 = 5 points
    }//GEN-LAST:event_a8ActionPerformed

    private void homebutton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homebutton2ActionPerformed
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
    }//GEN-LAST:event_homebutton2ActionPerformed

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton a1;
    private javax.swing.JButton a2;
    private javax.swing.JButton a3;
    private javax.swing.JButton a4;
    private javax.swing.JButton a5;
    private javax.swing.JButton a6;
    private javax.swing.JButton a7;
    private javax.swing.JButton a8;
    private javax.swing.JButton charge;
    private javax.swing.JPanel f1;
    private javax.swing.JPanel f2;
    private javax.swing.JPanel f3;
    private javax.swing.JPanel f4;
    private javax.swing.JButton historybutton;
    private javax.swing.JButton homebutton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton linkbutton;
    private javax.swing.JLabel points;
    private javax.swing.JButton profilebutton;
    // End of variables declaration//GEN-END:variables
}
