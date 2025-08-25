package cephra.Admin;

import java.awt.Font;
import java.awt.Window;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;


public class Queue extends javax.swing.JPanel {

    public Queue() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);
        
        JTableHeader header = TableQue.getTableHeader();
        header.setFont(new Font("Sage UI", Font.BOLD, 16));
        header.setBackground(new java.awt.Color(255,255,255));
        header.setForeground(new java.awt.Color(0,0,0));
        
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Baybutton = new javax.swing.JButton();
        businessbutton = new javax.swing.JButton();
        exitlogin = new javax.swing.JButton();
        staffbutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        QueueList = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableQue = new javax.swing.JTable();
        IconTab = new javax.swing.JLabel();
        QueueControl = new javax.swing.JPanel();
        CS1 = new javax.swing.JLabel();
        CS2 = new javax.swing.JLabel();
        CS3 = new javax.swing.JLabel();
        CS4 = new javax.swing.JLabel();
        CS5 = new javax.swing.JLabel();
        CS6 = new javax.swing.JLabel();
        CS7 = new javax.swing.JLabel();
        CS8 = new javax.swing.JLabel();
        Call = new javax.swing.JButton();
        StartC = new javax.swing.JButton();
        EndC = new javax.swing.JButton();
        Next = new javax.swing.JButton();
        Del = new javax.swing.JButton();
        W1 = new javax.swing.JLabel();
        W2 = new javax.swing.JLabel();
        W3 = new javax.swing.JLabel();
        W4 = new javax.swing.JLabel();
        W5 = new javax.swing.JLabel();
        W6 = new javax.swing.JLabel();
        W7 = new javax.swing.JLabel();
        W8 = new javax.swing.JLabel();
        IconCtrl = new javax.swing.JLabel();
        MainIcon = new javax.swing.JLabel();

        setLayout(null);

        Baybutton.setBorder(null);
        Baybutton.setBorderPainted(false);
        Baybutton.setContentAreaFilled(false);
        Baybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BaybuttonActionPerformed(evt);
            }
        });
        add(Baybutton);
        Baybutton.setBounds(380, 10, 40, 40);

        businessbutton.setBorder(null);
        businessbutton.setBorderPainted(false);
        businessbutton.setContentAreaFilled(false);
        businessbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                businessbuttonActionPerformed(evt);
            }
        });
        add(businessbutton);
        businessbutton.setBounds(610, 10, 140, 40);

        exitlogin.setBorder(null);
        exitlogin.setBorderPainted(false);
        exitlogin.setContentAreaFilled(false);
        exitlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitloginActionPerformed(evt);
            }
        });
        add(exitlogin);
        exitlogin.setBounds(930, 0, 70, 60);

        staffbutton.setBorder(null);
        staffbutton.setBorderPainted(false);
        staffbutton.setContentAreaFilled(false);
        staffbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffbuttonActionPerformed(evt);
            }
        });
        add(staffbutton);
        staffbutton.setBounds(500, 10, 110, 40);

        historybutton.setBorder(null);
        historybutton.setBorderPainted(false);
        historybutton.setContentAreaFilled(false);
        historybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historybuttonActionPerformed(evt);
            }
        });
        add(historybutton);
        historybutton.setBounds(430, 10, 60, 40);

        MainIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/t.png"))); // NOI18N
        add(MainIcon);
        MainIcon.setBounds(0, 0, 1000, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void businessbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_businessbuttonActionPerformed
        Window w = SwingUtilities.getWindowAncestor(Queue.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Dashboard());
        }
    }//GEN-LAST:event_businessbuttonActionPerformed

    private void staffbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffbuttonActionPerformed
        Window w = SwingUtilities.getWindowAncestor(Queue.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new StaffRecord());
        }
    }//GEN-LAST:event_staffbuttonActionPerformed

    private void exitloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitloginActionPerformed
        Window w = SwingUtilities.getWindowAncestor(Queue.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Login());
        }
    }//GEN-LAST:event_exitloginActionPerformed

    private void historybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historybuttonActionPerformed
        Window w = SwingUtilities.getWindowAncestor(Queue.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new History());
        }
    }//GEN-LAST:event_historybuttonActionPerformed

    private void BaybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BaybuttonActionPerformed
        Window w = SwingUtilities.getWindowAncestor(Queue.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new Bay());
        }
    }//GEN-LAST:event_BaybuttonActionPerformed

    private void StartCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StartCActionPerformed

    private void EndCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EndCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EndCActionPerformed

    private void NextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NextActionPerformed

    private void DelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Baybutton;
    private javax.swing.JLabel CS1;
    private javax.swing.JLabel CS2;
    private javax.swing.JLabel CS3;
    private javax.swing.JLabel CS4;
    private javax.swing.JLabel CS5;
    private javax.swing.JLabel CS6;
    private javax.swing.JLabel CS7;
    private javax.swing.JLabel CS8;
    private javax.swing.JButton Call;
    private javax.swing.JButton Del;
    private javax.swing.JButton EndC;
    private javax.swing.JLabel IconCtrl;
    private javax.swing.JLabel IconTab;
    private javax.swing.JLabel MainIcon;
    private javax.swing.JButton Next;
    private javax.swing.JPanel QueueControl;
    private javax.swing.JPanel QueueList;
    private javax.swing.JButton StartC;
    private javax.swing.JTable TableQue;
    private javax.swing.JLabel W1;
    private javax.swing.JLabel W2;
    private javax.swing.JLabel W3;
    private javax.swing.JLabel W4;
    private javax.swing.JLabel W5;
    private javax.swing.JLabel W6;
    private javax.swing.JLabel W7;
    private javax.swing.JLabel W8;
    private javax.swing.JButton businessbutton;
    private javax.swing.JButton exitlogin;
    private javax.swing.JButton historybutton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JButton staffbutton;
    // End of variables declaration//GEN-END:variables

}
