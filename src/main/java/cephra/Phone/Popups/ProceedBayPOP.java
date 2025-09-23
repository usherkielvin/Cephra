/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package cephra.Phone.Popups;

/**
 *
 * @author Kenji
 */
public class ProceedBayPOP extends javax.swing.JPanel {

    /**
     * Creates new form ProceedBayPOP
     */
    public ProceedBayPOP() {
        initComponents();
    }

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BayNumber = new javax.swing.JLabel();
        TicketNumber = new javax.swing.JLabel();
        OKBTN = new javax.swing.JButton();
        ICON = new javax.swing.JLabel();

        setLayout(null);

        BayNumber.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        BayNumber.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BayNumber.setText("BAY 01");
        add(BayNumber);
        BayNumber.setBounds(60, 110, 250, 100);

        TicketNumber.setFont(new java.awt.Font("Segoe UI", 0, 26)); // NOI18N
        TicketNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TicketNumber.setText("NCH04");
        add(TicketNumber);
        TicketNumber.setBounds(170, 238, 120, 35);

        OKBTN.setBorder(null);
        OKBTN.setBorderPainted(false);
        OKBTN.setContentAreaFilled(false);
        add(OKBTN);
        OKBTN.setBounds(80, 312, 210, 34);

        ICON.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/ProceedBayPOP.png"))); // NOI18N
        add(ICON);
        ICON.setBounds(50, 40, 270, 330);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BayNumber;
    private javax.swing.JLabel ICON;
    private javax.swing.JButton OKBTN;
    private javax.swing.JLabel TicketNumber;
    // End of variables declaration//GEN-END:variables
}
