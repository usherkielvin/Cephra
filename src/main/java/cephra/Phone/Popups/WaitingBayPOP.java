/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package cephra.Phone.Popups;

/**
 *
 * @author Kenji
 */
public class WaitingBayPOP extends javax.swing.JPanel {

    /**
     * Creates new form WaitingBayPOP
     */
    public WaitingBayPOP() {
        initComponents();
    }

   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TicketNumber = new javax.swing.JLabel();
        OKBTN = new javax.swing.JButton();
        ICON = new javax.swing.JLabel();

        setLayout(null);

        TicketNumber.setFont(new java.awt.Font("Segoe UI", 0, 26)); // NOI18N
        TicketNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TicketNumber.setText("NCH04");
        add(TicketNumber);
        TicketNumber.setBounds(170, 233, 120, 35);

        OKBTN.setBorder(null);
        OKBTN.setBorderPainted(false);
        OKBTN.setContentAreaFilled(false);
        OKBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKBTNActionPerformed(evt);
            }
        });
        add(OKBTN);
        OKBTN.setBounds(30, 220, 310, 120);

        ICON.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/WaitingBayPOP.png"))); // NOI18N
        add(ICON);
        ICON.setBounds(50, 40, 270, 320);
		// Ensure the invisible OK button stays on top and receives clicks
		setComponentZOrder(OKBTN, 0);
    }// </editor-fold>//GEN-END:initComponents

    private void OKBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKBTNActionPerformed
        cephra.Phone.Popups.CustomPopupManager.executeCallback();
    }//GEN-LAST:event_OKBTNActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ICON;
    private javax.swing.JButton OKBTN;
    private javax.swing.JLabel TicketNumber;
    // End of variables declaration//GEN-END:variables
}
