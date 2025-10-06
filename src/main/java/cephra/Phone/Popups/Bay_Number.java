/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package cephra.Phone.Popups;

/**
 *
 * @author Kenji
 */
public class Bay_Number extends javax.swing.JPanel {

    /**
     * Creates new form ProceedBayPOP
     */
    public Bay_Number() {
        initComponents();
    }

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        OKBTN = new javax.swing.JButton();
        BayNumber = new javax.swing.JLabel();
        TicketNumber = new javax.swing.JLabel();
        ICON = new javax.swing.JLabel();

        setLayout(null);

        OKBTN.setBorder(null);
        OKBTN.setBorderPainted(false);
        OKBTN.setContentAreaFilled(false);
        OKBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKBTNActionPerformed(evt);
            }
        });
        add(OKBTN);
        OKBTN.setBounds(55, 316, 210, 30);

        BayNumber.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        BayNumber.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BayNumber.setText("BAY 01");
        add(BayNumber);
        BayNumber.setBounds(35, 110, 250, 100);

        TicketNumber.setFont(new java.awt.Font("Segoe UI", 0, 26)); // NOI18N
        TicketNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TicketNumber.setText("NCH04");
        add(TicketNumber);
        TicketNumber.setBounds(150, 238, 120, 35);

        ICON.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/ProceedBayPOP.png"))); // NOI18N
        add(ICON);
        ICON.setBounds(25, 40, 270, 330);
    }// </editor-fold>//GEN-END:initComponents

    private void OKBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKBTNActionPerformed
        System.out.println("Bay_Number OK button clicked");
        
        // Trigger bay assignment and waiting → charging transition
        try {
            // Get current ticket ID
            String ticketId = cephra.Phone.Utilities.CustomPopupManager.getCurrentTicketId();
            System.out.println("Bay_Number OK: Retrieved ticketId: " + ticketId);
            
            if (ticketId != null && !ticketId.trim().isEmpty()) {
                System.out.println("Bay_Number OK: Executing bay assignment callback");
                // Execute the bay assignment callback that was originally in Charge_Now
                cephra.Phone.Utilities.CustomPopupManager.executeBayAssignmentCallback(ticketId);
                System.out.println("Bay_Number OK: Callback executed successfully");
            } else {
                System.out.println("Bay_Number OK: No ticket ID found, hiding popup");
                // Fallback: just hide popup if no ticket ID
                cephra.Phone.Utilities.CustomPopupManager.hideCurrentPopup();
            }
        } catch (Exception e) {
            System.err.println("Error in Bay_Number OK button: " + e.getMessage());
            e.printStackTrace();
            // Ensure popup is hidden even if there's an error
            cephra.Phone.Utilities.CustomPopupManager.hideCurrentPopup();
        }
    }//GEN-LAST:event_OKBTNActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BayNumber;
    private javax.swing.JLabel ICON;
    private javax.swing.JButton OKBTN;
    private javax.swing.JLabel TicketNumber;
    // End of variables declaration//GEN-END:variables
    public void startIntroAnimation() {
        throw new UnsupportedOperationException("Unimplemented method 'startIntroAnimation'");
    }
}
