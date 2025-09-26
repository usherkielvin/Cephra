package cephra.Phone.Popups;

public class Charge_Now extends javax.swing.JPanel {

    public Charge_Now() {
        initComponents();
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
        cephra.Phone.Utilities.CustomPopupManager.executeCallback();
        // Fallback: if no popup is showing after callback, ensure ProceedBay appears with TBD bay
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    if (!cephra.Phone.Utilities.CustomPopupManager.isPopupShowing()) {
                        String username = cephra.Database.CephraDB.getCurrentUsername();
                        String ticket = (username == null || username.isEmpty())
                                ? null
                                : cephra.Database.CephraDB.getUserCurrentTicketId(username);
                        if (ticket == null || ticket.trim().isEmpty()) {
                            ticket = cephra.Phone.Utilities.QueueFlow.getCurrentTicketId();
                        }
                        String assignedBay = null;
                        try { assignedBay = cephra.Admin.BayManagement.getBayNumberByTicket(ticket); } catch (Throwable ignore) {}
                        if (username != null && !username.isEmpty()) {
                            cephra.Phone.Utilities.CustomPopupManager.showProceedBayPopupInfo(ticket, (assignedBay != null ? assignedBay : "TBD"), username);
                        }
                    }
                } catch (Throwable ignore) {}
            }
        });
    }//GEN-LAST:event_OKBTNActionPerformed

    private void CANCELActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CANCELActionPerformed
        cephra.Phone.Utilities.CustomPopupManager.hideCustomPopup();
    }//GEN-LAST:event_CANCELActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CANCEL;
    private javax.swing.JLabel ICON;
    private javax.swing.JButton OKBTN;
    private javax.swing.JLabel TicketNumber;
    // End of variables declaration//GEN-END:variables
}
