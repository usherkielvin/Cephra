
package cephra.Phone.RewardsWallet;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class TopUppanel extends javax.swing.JPanel {

    // Quick transaction amounts - updated to match the button values
    private static final double[] QUICK_AMOUNTS = {500.0, 1000.0, 1500.0, 2000.0, 3000.0, 5000.0};
    
    // Method panel animation state
    private boolean isMethodPanelVisible = false;
    private final int PANEL_ANIMATION_DELAY = 10; // milliseconds
    private final int PANEL_SLIDE_SPEED = 15; // pixels per step

    public TopUppanel() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        setupQuickAmountButtons();
        loadCurrentBalance();
        initializeCustomAmount();
        setupPanelClickListener();
        initializeMethodPanel();
        initializeMethodSelectedLabel();
        
        Customamount.setOpaque(false);
        Customamount.setBackground(new Color(0, 0, 0, 0));
        
       
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        Currentbalance = new javax.swing.JLabel();
        methodselected = new javax.swing.JLabel();
        A = new javax.swing.JButton();
        B = new javax.swing.JButton();
        C = new javax.swing.JButton();
        D = new javax.swing.JButton();
        E = new javax.swing.JButton();
        F = new javax.swing.JButton();
        Proceed = new javax.swing.JButton();
        Customamount = new javax.swing.JTextField();
        bck = new javax.swing.JButton();
        morePayMethod = new javax.swing.JButton();
        charge = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        topUpIcon = new javax.swing.JLabel();
        historybutton = new javax.swing.JButton();
        profilebutton = new javax.swing.JButton();
        Method = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

        Currentbalance.setFont(new java.awt.Font("Segoe UI Semibold", 1, 40)); // NOI18N
        Currentbalance.setForeground(new java.awt.Color(255, 255, 255));
        Currentbalance.setText("xxxxxxx");
        jPanel1.add(Currentbalance);
        Currentbalance.setBounds(80, 160, 240, 60);

        methodselected.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel1.add(methodselected);
        methodselected.setBounds(50, 569, 220, 30);

        A.setBorderPainted(false);
        A.setContentAreaFilled(false);
        jPanel1.add(A);
        A.setBounds(40, 250, 75, 30);

        B.setBorderPainted(false);
        B.setContentAreaFilled(false);
        B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BActionPerformed(evt);
            }
        });
        jPanel1.add(B);
        B.setBounds(150, 250, 75, 30);

        C.setBorderPainted(false);
        C.setContentAreaFilled(false);
        jPanel1.add(C);
        C.setBounds(260, 250, 75, 30);

        D.setBorderPainted(false);
        D.setContentAreaFilled(false);
        jPanel1.add(D);
        D.setBounds(40, 310, 75, 30);

        E.setBorderPainted(false);
        E.setContentAreaFilled(false);
        jPanel1.add(E);
        E.setBounds(150, 310, 75, 30);

        F.setBorderPainted(false);
        F.setContentAreaFilled(false);
        jPanel1.add(F);
        F.setBounds(260, 310, 75, 30);

        Proceed.setBorderPainted(false);
        Proceed.setContentAreaFilled(false);
        Proceed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProceedActionPerformed(evt);
            }
        });
        jPanel1.add(Proceed);
        Proceed.setBounds(40, 615, 290, 30);

        Customamount.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        Customamount.setBorder(null);
        Customamount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                CustomamountKeyTyped(evt);
            }
        });
        jPanel1.add(Customamount);
        Customamount.setBounds(70, 371, 255, 30);

        bck.setBorderPainted(false);
        bck.setContentAreaFilled(false);
        bck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bckActionPerformed(evt);
            }
        });
        jPanel1.add(bck);
        bck.setBounds(20, 50, 72, 30);

        morePayMethod.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        morePayMethod.setBorder(null);
        morePayMethod.setBorderPainted(false);
        morePayMethod.setContentAreaFilled(false);
        morePayMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                morePayMethodActionPerformed(evt);
            }
        });
        jPanel1.add(morePayMethod);
        morePayMethod.setBounds(290, 570, 40, 30);

        charge.setBorder(null);
        charge.setBorderPainted(false);
        charge.setContentAreaFilled(false);
        charge.setFocusPainted(false);
        charge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeActionPerformed(evt);
            }
        });
        jPanel1.add(charge);
        charge.setBounds(50, 680, 40, 40);

        linkbutton.setBorder(null);
        linkbutton.setBorderPainted(false);
        linkbutton.setContentAreaFilled(false);
        linkbutton.setFocusPainted(false);
        linkbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkbuttonActionPerformed(evt);
            }
        });
        jPanel1.add(linkbutton);
        linkbutton.setBounds(110, 680, 30, 40);

        topUpIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/topUpIcon.png"))); // NOI18N
        jPanel1.add(topUpIcon);
        topUpIcon.setBounds(0, 0, 370, 750);

        historybutton.setBorder(null);
        historybutton.setBorderPainted(false);
        historybutton.setContentAreaFilled(false);
        historybutton.setFocusPainted(false);
        historybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historybuttonActionPerformed(evt);
            }
        });
        jPanel1.add(historybutton);
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
        jPanel1.add(profilebutton);
        profilebutton.setBounds(280, 680, 30, 40);

        add(jPanel1);
        jPanel1.setBounds(0, 0, 370, 750);

        Method.setBackground(new java.awt.Color(204, 204, 204));
        Method.setLayout(null);

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jRadioButton1.setText("                                                      ");
        jRadioButton1.setContentAreaFilled(false);
        jRadioButton1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jRadioButton1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        Method.add(jRadioButton1);
        jRadioButton1.setBounds(30, 260, 310, 50);

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jRadioButton3.setContentAreaFilled(false);
        jRadioButton3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jRadioButton3.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Method.add(jRadioButton3);
        jRadioButton3.setBounds(30, 190, 310, 50);

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jRadioButton2.setContentAreaFilled(false);
        jRadioButton2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jRadioButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Method.add(jRadioButton2);
        jRadioButton2.setBounds(30, 120, 310, 50);

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setContentAreaFilled(false);
        jRadioButton4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jRadioButton4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });
        Method.add(jRadioButton4);
        jRadioButton4.setBounds(40, 60, 300, 40);

        jLabel1.setBackground(new java.awt.Color(204, 204, 204));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/METHO.png"))); // NOI18N
        Method.add(jLabel1);
        jLabel1.setBounds(0, 0, 370, 350);

        add(Method);
        Method.setBounds(0, 680, 370, 450);
    }// </editor-fold>//GEN-END:initComponents

    private void ProceedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProceedActionPerformed
        processTopUp();
        
    }//GEN-LAST:event_ProceedActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        updateMethodSelectedLabel("Earnest Bank");
        slideMethodPanelDown(); // Auto-hide panel after selection
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void bckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bckActionPerformed
       
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
    }//GEN-LAST:event_bckActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        updateMethodSelectedLabel("GCash");
        slideMethodPanelDown(); // Auto-hide panel after selection
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        updateMethodSelectedLabel("PayMaya");
        slideMethodPanelDown(); // Auto-hide panel after selection
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        updateMethodSelectedLabel("VISA");
        slideMethodPanelDown(); // Auto-hide panel after selection
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BActionPerformed
      
    }//GEN-LAST:event_BActionPerformed

    private void CustomamountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CustomamountKeyTyped
        char c = evt.getKeyChar();
            if (!Character.isDigit(c) && c != '\b') {
            java.awt.Toolkit.getDefaultToolkit().beep();
            evt.consume(); // Ignore non-digit input
}
    }//GEN-LAST:event_CustomamountKeyTyped

    private void morePayMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_morePayMethodActionPerformed
        System.out.println("morePayMethod button clicked! Current panel visible: " + isMethodPanelVisible);
        toggleMethodPanel();
    }//GEN-LAST:event_morePayMethodActionPerformed

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

    /**
     * Sets up the quick amount buttons with action listeners
     */
    private void setupQuickAmountButtons() {
        JButton[] buttons = {A, B, C, D, E, F};
        
        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            JButton button = buttons[i];
            
            // Add action listener to set custom amount when clicked
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setCustomAmount(QUICK_AMOUNTS[index]);
                }
            });
        }
    }

    /**
     * Loads and displays the current wallet balance
     */
    private void loadCurrentBalance() {
        try {
            String currentUser = cephra.Database.CephraDB.getCurrentUsername();
            if (currentUser != null && !currentUser.isEmpty()) {
                double balance = cephra.Database.CephraDB.getUserWalletBalance(currentUser);
                Currentbalance.setText(String.format("₱%.2f", balance));
            } else {
                Currentbalance.setText("₱ 0.00");
            }
        } catch (Exception e) {
            System.err.println("Error loading current balance: " + e.getMessage());
            Currentbalance.setText("₱ 0.00");
        }
    }

    /**
     * Initializes the custom amount field
     */
    private void initializeCustomAmount() {
        Customamount.setText("0.00");
        Customamount.setHorizontalAlignment(JTextField.CENTER);
    }

    /**
     * Sets the custom amount field with the selected quick amount
     */
    private void setCustomAmount(double amount) {
        Customamount.setText(String.format("%.2f", amount));
    }

    /**
     * Processes the top-up transaction
     */
    private void processTopUp() {
        try {
            String currentUser = cephra.Database.CephraDB.getCurrentUsername();
            if (currentUser == null || currentUser.isEmpty()) {
                showErrorMessage("No user is currently logged in.");
                return;
            }

            // Check if a payment method (radio button) is selected
            String selectedPaymentMethod = getSelectedPaymentMethod();
            if (selectedPaymentMethod == null) {
                showErrorMessage("Please select a payment method to proceed.");
                return;
            }

            // Parse the amount from the custom amount field
            String amountText = Customamount.getText().trim();
            if (amountText.isEmpty()) {
                showErrorMessage("Please enter an amount to top up.");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                showErrorMessage("Please enter a valid amount.");
                return;
            }

            if (amount <= 0) {
                showErrorMessage("Please enter a positive amount.");
                return;
            }

            // Enforce minimum top-up amount
            if (amount < 200.0) {
                showErrorMessage("Minimum top-up amount is ₱200.00");
                return;
            }

            if (amount > 50000) {
                showErrorMessage("Maximum top-up amount is ₱50,000.00");
                return;
            }

            // Determine which quick amount was selected or if it's custom
            String topUpMethod = "Custom Amount";
            for (int i = 0; i < QUICK_AMOUNTS.length; i++) {
                if (Math.abs(amount - QUICK_AMOUNTS[i]) < 0.01) {
                    topUpMethod = "Quick Amount ";
                    break;
                }
            }
            
            // Include selected payment method in the description
            topUpMethod += " via " + selectedPaymentMethod;

            // Process the top-up
            boolean success = cephra.Database.CephraDB.processWalletTopUp(currentUser, amount, topUpMethod);

            if (success) {
                loadCurrentBalance(); // Refresh balance display
                Customamount.setText("0.00"); // Reset amount field
                clearPaymentMethodSelection(); // Clear radio button selection
                
                // Show the topup receipt immediately without success dialog
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        java.awt.Window[] windows = java.awt.Window.getWindows();
                        for (java.awt.Window window : windows) {
                            if (window instanceof cephra.Frame.Phone) {
                                cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                                phoneFrame.switchPanel(new cephra.Phone.RewardsWallet.WalletReciept1());
                                break;
                            }
                        }
                    }
                });
            } else {
                showErrorMessage("Failed to process top-up. Please try again.");
            }

        } catch (Exception e) {
            System.err.println("Error processing top-up: " + e.getMessage());
            e.printStackTrace();
            showErrorMessage("An error occurred while processing your top-up.");
        }
    }

    /**
     * Shows an error message to the user
     */
    private void showErrorMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                this,
                message,
                "Top-Up Error",
                JOptionPane.ERROR_MESSAGE
            );
        });
    }

    /**
     * Shows a success message to the user
     */
    private void showSuccessMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                this,
                message,
                "Top-Up Successful",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
    }

    /**
     * Public method to refresh balance when returning to this screen
     */
    public void refreshBalance() {
        loadCurrentBalance();
    }
    
    /**
     * Gets the selected payment method from radio buttons
     * @return the selected payment method name, or null if none selected
     */
    private String getSelectedPaymentMethod() {
        if (jRadioButton1.isSelected()) {
            return "Earnest Bank";
        } else if (jRadioButton2.isSelected()) {
            return "VISA";
        } else if (jRadioButton3.isSelected()) {
            return "Maya";
        } else if (jRadioButton4.isSelected()) {
            return "Gcash";
        }
        return null; // No radio button selected
    }
    
    /**
     * Clears the selected payment method (radio button)
     */
    private void clearPaymentMethodSelection() {
        buttonGroup1.clearSelection();
        // Reset the methodselected label to initial state
        methodselected.setText("Select a payment method");
        methodselected.setFont(new java.awt.Font("Segoe UI", 0, 12));
        methodselected.setForeground(new Color(100, 100, 100)); // Gray color
        methodselected.repaint();
    }
    
    /**
     * Toggles the visibility of the Method panel with animation
     */
    private void toggleMethodPanel() {
        System.out.println("toggleMethodPanel called - isMethodPanelVisible: " + isMethodPanelVisible);
        System.out.println("Method panel current position: " + Method.getBounds());
        
        if (isMethodPanelVisible) {
            System.out.println("Sliding panel down...");
            slideMethodPanelDown();
        } else {
            System.out.println("Sliding panel up...");
            slideMethodPanelUp();
        }
    }
    
    /**
     * Slides the Method panel up from the bottom with animation
     */
    private void slideMethodPanelUp() {
        System.out.println("slideMethodPanelUp starting...");
        isMethodPanelVisible = true;
        final int startY = 750; // Off-screen at bottom
        final int endY = 320;   // Final position (750 - 330 = 420 to show full panel)
        final int currentY = Method.getY();
        
        System.out.println("Animation: startY=" + startY + ", endY=" + endY + ", currentY=" + currentY);
        
        Timer slideUpTimer = new Timer(PANEL_ANIMATION_DELAY, null);
        slideUpTimer.addActionListener(new ActionListener() {
            int step = 0;
            int totalSteps = (startY - endY) / PANEL_SLIDE_SPEED;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (step <= totalSteps) {
                    int newY = startY - (step * PANEL_SLIDE_SPEED);
                    if (newY <= endY) {
                        newY = endY;
                    }
                    Method.setBounds(0, newY, 370, 330);
                    Method.repaint();
                    step++;
                    
                    if (newY <= endY) {
                        slideUpTimer.stop();
                        System.out.println("slideMethodPanelUp completed at position: " + Method.getBounds());
                    }
                } else {
                    slideUpTimer.stop();
                    System.out.println("slideMethodPanelUp completed at position: " + Method.getBounds());
                }
            }
        });
        slideUpTimer.start();
    }
    
    /**
     * Slides the Method panel down to hide it with animation
     */
    private void slideMethodPanelDown() {
        isMethodPanelVisible = false;
        final int startY = Method.getY(); // Current position
        final int endY = 750;   // Off-screen at bottom
        
        Timer slideDownTimer = new Timer(PANEL_ANIMATION_DELAY, null);
        slideDownTimer.addActionListener(new ActionListener() {
            int step = 0;
            int totalSteps = (endY - startY) / PANEL_SLIDE_SPEED;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (step <= totalSteps) {
                    int newY = startY + (step * PANEL_SLIDE_SPEED);
                    if (newY >= endY) {
                        newY = endY;
                    }
                    Method.setBounds(0, newY, 370, 330);
                    Method.repaint();
                    step++;
                    
                    if (newY >= endY) {
                        slideDownTimer.stop();
                    }
                } else {
                    slideDownTimer.stop();
                }
            }
        });
        slideDownTimer.start();
    }
    
    /**
     * Sets up click listener to hide Method panel when clicking outside of it
     */
    private void setupPanelClickListener() {
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (isMethodPanelVisible) {
                    // Check if click is outside the Method panel area
                    int methodPanelY = Method.getY();
                    if (evt.getY() < methodPanelY) {
                        slideMethodPanelDown();
                    }
                }
            }
        });
    }
    
    /**
     * Initializes the Method panel for proper popup functionality
     */
    private void initializeMethodPanel() {
        // Ensure Method panel is properly positioned off-screen initially
        Method.setBounds(0, 750, 370, 330);
        Method.setVisible(true);
        
        // Bring Method panel to front to ensure it appears above other components
        SwingUtilities.invokeLater(() -> {
            Method.getParent().setComponentZOrder(Method, 0);
            Method.getParent().repaint();
        });
        
        System.out.println("Method panel initialized at position: " + Method.getBounds());
    }
    
    /**
     * Initializes the methodselected label
     */
    private void initializeMethodSelectedLabel() {
        methodselected.setText("Select a payment method");
        methodselected.setFont(new java.awt.Font("Segoe UI", 0, 12));
        methodselected.setForeground(new Color(100, 100, 100)); // Gray color
    }
    
    /**
     * Updates the methodselected label with the chosen payment method
     * @param methodName the name of the selected payment method
     */
    private void updateMethodSelectedLabel(String methodName) {
        methodselected.setText("Method: " + methodName);
        methodselected.setFont(new java.awt.Font("Segoe UI", 1, 12)); // Bold
        methodselected.setForeground(new Color(0, 120, 0)); // Green color
        methodselected.repaint();
        System.out.println("Payment method selected: " + methodName);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton A;
    private javax.swing.JButton B;
    private javax.swing.JButton C;
    private javax.swing.JLabel Currentbalance;
    private javax.swing.JTextField Customamount;
    private javax.swing.JButton D;
    private javax.swing.JButton E;
    private javax.swing.JButton F;
    private javax.swing.JPanel Method;
    private javax.swing.JButton Proceed;
    private javax.swing.JButton bck;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton charge;
    private javax.swing.JButton historybutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JButton linkbutton;
    private javax.swing.JLabel methodselected;
    private javax.swing.JButton morePayMethod;
    private javax.swing.JButton profilebutton;
    private javax.swing.JLabel topUpIcon;
    // End of variables declaration//GEN-END:variables
}
