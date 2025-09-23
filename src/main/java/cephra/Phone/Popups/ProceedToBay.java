package cephra.Phone.Popups;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProceedToBay extends javax.swing.JDialog {
    
    // Static state management to prevent multiple instances
    private static ProceedToBay currentInstance = null;
    private static String currentTicketId = null;
    private static boolean isShowing = false;
    
    // Dialog result
    private static boolean userConfirmed = false;
    
    // Popup dimensions (centered in phone frame)
    private static final int POPUP_WIDTH = 280;
    private static final int POPUP_HEIGHT = 200;
    
    private String ticketId;
    private String bayNumber;
    
    /**
     * Creates new form ProceedToBay
     */
    public ProceedToBay(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setupDialog();
    }
    
    private void setupDialog() {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        
        // Center the dialog
        centerDialog();
        
        // Add window listener to handle close button
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleCancel();
            }
        });
    }
    
    private void centerDialog() {
        java.awt.Window parent = getOwner();
        if (parent != null) {
            int x = parent.getX() + (parent.getWidth() - POPUP_WIDTH) / 2;
            int y = parent.getY() + (parent.getHeight() - POPUP_HEIGHT) / 2;
            setLocation(x, y);
        } else {
            // Fallback to screen center
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screenSize.width - POPUP_WIDTH) / 2;
            int y = (screenSize.height - POPUP_HEIGHT) / 2;
            setLocation(x, y);
        }
    }
    
    /**
     * Shows the ProceedToBay dialog for a specific ticket and bay
     * @param ticketId the ticket ID
     * @param bayNumber the bay number
     * @param username the username
     * @return true if user confirmed (OK), false if cancelled
     */
    public static boolean showProceedDialog(String ticketId, String bayNumber, String username) {
        return showProceedDialog(ticketId, bayNumber, username, true);
    }
    
    /**
     * Shows the ProceedToBay dialog for a specific ticket and bay
     * @param ticketId the ticket ID
     * @param bayNumber the bay number
     * @param username the username
     * @param requireConfirmation if true, shows OK/Cancel buttons; if false, shows only OK button
     * @return true if user confirmed (OK), false if cancelled
     */
    public static boolean showProceedDialog(String ticketId, String bayNumber, String username, boolean requireConfirmation) {
        // Validate user is logged in
        if (!cephra.Database.CephraDB.isUserLoggedIn()) {
            return false;
        }
        
        // Get and validate current user
        String currentUser = cephra.Database.CephraDB.getCurrentUsername();
        if (currentUser == null || currentUser.trim().isEmpty()) {
            return false;
        }
        
        // Validate user matches ticket owner
        if (!currentUser.trim().equals(username.trim())) {
            return false;
        }
        
        // Hide any existing instance
        if (isShowing && currentInstance != null) {
            currentInstance.dispose();
        }
        
        // Find Phone frame
        java.awt.Window[] windows = java.awt.Window.getWindows();
        cephra.Frame.Phone phoneFrame = null;
        
        for (java.awt.Window window : windows) {
            if (window instanceof cephra.Frame.Phone) {
                phoneFrame = (cephra.Frame.Phone) window;
                break;
            }
        }
        
        if (phoneFrame == null) {
            return false;
        }
        
        // Create and show dialog
        currentInstance = new ProceedToBay(phoneFrame, true);
        currentInstance.ticketId = ticketId;
        currentInstance.bayNumber = bayNumber;
        currentInstance.updateDisplay();
        currentInstance.setConfirmationMode(requireConfirmation);
        
        currentTicketId = ticketId;
        isShowing = true;
        userConfirmed = false;
        
        currentInstance.setVisible(true);
        
        return userConfirmed;
    }
    
    private void updateDisplay() {
        if (ticketId != null) {
            titleLabel.setText("Please proceed to your bay");
            if (bayNumber != null && !bayNumber.equals("TBD")) {
                messageLabel.setText("Bay Number: " + bayNumber);
            } else {
                messageLabel.setText("Bay will be assigned shortly");
            }
            ticketLabel.setText("Ticket: " + ticketId);
        }
    }
    
    private void setConfirmationMode(boolean requireConfirmation) {
        if (requireConfirmation) {
            cancelButton.setVisible(true);
            okButton.setText("OK");
        } else {
            cancelButton.setVisible(false);
            okButton.setText("OK");
        }
    }
    
    private void handleOK() {
        userConfirmed = true;
        isShowing = false;
        currentTicketId = null;
        dispose();
    }
    
    private void handleCancel() {
        userConfirmed = false;
        isShowing = false;
        currentTicketId = null;
        dispose();
    }
    
    /**
     * Checks if the dialog is currently showing
     * @return true if showing
     */
    public static boolean isDialogShowing() {
        return isShowing;
    }
    
    /**
     * Checks if the dialog is showing for a specific ticket
     * @param ticketId the ticket ID to check
     * @return true if showing for this ticket
     */
    public static boolean isShowingForTicket(String ticketId) {
        return isShowing && ticketId != null && ticketId.equals(currentTicketId);
    }
    
    /**
     * Hides the dialog if it's currently showing
     */
    public static void hideProceedDialog() {
        if (isShowing && currentInstance != null) {
            currentInstance.dispose();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        messageLabel = new javax.swing.JLabel();
        ticketLabel = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        mainPanel.setLayout(null);

        titleLabel.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new java.awt.Color(0, 120, 215));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Please proceed to your bay");
        mainPanel.add(titleLabel);
        titleLabel.setBounds(20, 20, 240, 30);

        messageLabel.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(new java.awt.Color(0, 0, 0));
        messageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        messageLabel.setText("Bay Number: Bay-1");
        mainPanel.add(messageLabel);
        messageLabel.setBounds(20, 60, 240, 25);

        ticketLabel.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 12));
        ticketLabel.setForeground(new java.awt.Color(100, 100, 100));
        ticketLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ticketLabel.setText("Ticket: FCH001");
        mainPanel.add(ticketLabel);
        ticketLabel.setBounds(20, 90, 240, 20);

        buttonPanel.setBackground(new java.awt.Color(245, 245, 245));
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 10));

        okButton.setBackground(new java.awt.Color(0, 120, 215));
        okButton.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 12));
        okButton.setForeground(new java.awt.Color(255, 255, 255));
        okButton.setText("OK");
        okButton.setBorderPainted(false);
        okButton.setFocusPainted(false);
        okButton.setPreferredSize(new java.awt.Dimension(80, 30));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(okButton);

        cancelButton.setBackground(new java.awt.Color(200, 200, 200));
        cancelButton.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 12));
        cancelButton.setForeground(new java.awt.Color(0, 0, 0));
        cancelButton.setText("Cancel");
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setPreferredSize(new java.awt.Dimension(80, 30));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel);
        buttonPanel.setBounds(0, 120, 280, 60);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(280, 200));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        handleOK();
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        handleCancel();
    }//GEN-LAST:event_cancelButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel ticketLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
