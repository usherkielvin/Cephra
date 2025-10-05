package cephra.Phone.Popups;

import javax.swing.*;

public class Charge_Now extends javax.swing.JPanel {
    
    // Intro animation fields
    private Timer introTimer;
    private ImageIcon mainImageIcon;
    
    public Charge_Now() {
        // Load intro gif and main image
        // Initially hide interactive content until intro GIF finishes
        initComponents();
        hideContent();
        loadImages();
        startIntroAnimation();
    } 
    
    /**
     * Loads the intro gif and main image icons
     */
    private void loadImages() {
        try {
            mainImageIcon = new ImageIcon(getClass().getResource("/cephra/Cephra Images/WaitingBayPOP.png"));
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Starts the intro animation sequence
     */
    private void startIntroAnimation() {
        if (ICON == null) {
            // Fallback to main image if label not available
            showMainImage();
            return;
        }
        
        // Create a fresh GIF instance to reset animation
        ImageIcon freshGifIcon = null;
        try {
            freshGifIcon = new ImageIcon(getClass().getResource("/cephra/Cephra Images/inbaynumber.gif"));
        } catch (Exception e) {
            System.err.println("Error loading fresh intro gif: " + e.getMessage());
            showMainImage();
            return;
        }
        
        ICON.setIcon(freshGifIcon);

        // Set up timer to forcibly cut GIF and switch to main image after 200ms (0.2 seconds)
        introTimer = new Timer(200, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // Force stop GIF by clearing icon first, then setting main image
                ICON.setIcon(null);
                SwingUtilities.invokeLater(() -> {
                    showMainImage();
                    showContent();
                });
                introTimer.stop();
            }
        });
        introTimer.setRepeats(false);
        introTimer.start();
    }
    
    /**
     * Shows the main image and ends intro animation
     */
    private void showMainImage() {
        if (ICON != null && mainImageIcon != null) {
            ICON.setIcon(mainImageIcon);
        }
        if (introTimer != null) {
            introTimer.stop();
        }
    }

    private void hideContent() {
        try {
            TicketNumber.setVisible(false);
            OKBTN.setVisible(false);
            CANCEL.setVisible(false);
        } catch (Exception ignore) {}
    }

    private void showContent() {
        try {
            TicketNumber.setVisible(true);
            OKBTN.setVisible(true);
            CANCEL.setVisible(true);
        } catch (Exception ignore) {}
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TicketNumber = new javax.swing.JLabel();
        OKBTN = new javax.swing.JButton();
        CANCEL = new javax.swing.JButton();
        ICON = new javax.swing.JLabel();

        setLayout(null);

        TicketNumber.setFont(new java.awt.Font("Segoe UI", 0, 26)); // NOI18N
        TicketNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TicketNumber.setText("NCH04");
        add(TicketNumber);
        TicketNumber.setBounds(150, 233, 120, 35);

        OKBTN.setBorder(null);
        OKBTN.setBorderPainted(false);
        OKBTN.setContentAreaFilled(false);
        OKBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKBTNActionPerformed(evt);
            }
        });
        add(OKBTN);
        OKBTN.setBounds(160, 310, 120, 30);

        CANCEL.setBorder(null);
        CANCEL.setBorderPainted(false);
        CANCEL.setContentAreaFilled(false);
        CANCEL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CANCELActionPerformed(evt);
            }
        });
        add(CANCEL);
        CANCEL.setBounds(40, 310, 110, 30);

        ICON.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/WaitingBayPOP.png"))); // NOI18N
        add(ICON);
        ICON.setBounds(25, 40, 270, 320);
    }// </editor-fold>//GEN-END:initComponents

    private void OKBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKBTNActionPerformed
        // Execute callback to show Bay_Number popup
        try {
            System.out.println("Charge_Now OK button clicked - executing callback to show Bay_Number");
            cephra.Phone.Utilities.CustomPopupManager.executeCallback();
        } catch (Exception e) {
            System.err.println("Error in OK button callback: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace for debugging
            
            // Fallback: Try to show Bay_Number popup directly
            try {
                System.out.println("Charge_Now: Attempting fallback - showing Bay_Number popup directly");
                String ticketId = cephra.Phone.Utilities.CustomPopupManager.getCurrentTicketId();
                if (ticketId != null) {
                    cephra.Phone.Utilities.CustomPopupManager.showBayNumberPopupDirect(ticketId, "TBD");
                    System.out.println("Charge_Now: Fallback successful - Bay_Number popup should be visible");
                } else {
                    System.err.println("Charge_Now: Fallback failed - no ticketId available");
                }
            } catch (Exception fallbackEx) {
                System.err.println("Charge_Now: Fallback also failed: " + fallbackEx.getMessage());
                fallbackEx.printStackTrace();
            }
            
            // Ensure popup is hidden even if there's an error
            try {
                cephra.Phone.Utilities.CustomPopupManager.hideCustomPopup();
            } catch (Exception hideEx) {
                System.err.println("Error hiding popup after callback error: " + hideEx.getMessage());
            }
        }
    }//GEN-LAST:event_OKBTNActionPerformed

    private void CANCELActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CANCELActionPerformed
        // Simple approach like Queue_Ticket - just hide popup with error handling
        try {
            cephra.Phone.Utilities.CustomPopupManager.cancelCurrentCallback();
            cephra.Phone.Utilities.CustomPopupManager.hideCustomPopup();
        } catch (Exception e) {
            System.err.println("Error in CANCEL button: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace for debugging
        }
    }//GEN-LAST:event_CANCELActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CANCEL;
    private javax.swing.JLabel ICON;
    private javax.swing.JButton OKBTN;
    private javax.swing.JLabel TicketNumber;
    // End of variables declaration//GEN-END:variables
}
