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
        
        JTableHeader header = queTab.getTableHeader();
        header.setFont(new Font("Sogie UI", Font.BOLD, 16));
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Baybutton = new javax.swing.JButton();
        businessbutton = new javax.swing.JButton();
        exitlogin = new javax.swing.JButton();
        staffbutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelLists = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        queTab = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        ControlPanel = new javax.swing.JPanel();
        B1 = new javax.swing.JLabel();
        B2 = new javax.swing.JLabel();
        B3 = new javax.swing.JLabel();
        B4 = new javax.swing.JLabel();
        B5 = new javax.swing.JLabel();
        B6 = new javax.swing.JLabel();
        B7 = new javax.swing.JLabel();
        B8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        queIcon = new javax.swing.JLabel();
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

        jTabbedPane1.setBackground(new java.awt.Color(63, 98, 110));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        panelLists.setLayout(null);

        jLabel3.setText("jLabel3");
        panelLists.add(jLabel3);
        jLabel3.setBounds(50, 480, 120, 70);

        jLabel4.setText("jLabel3");
        panelLists.add(jLabel4);
        jLabel4.setBounds(50, 150, 120, 70);

        jLabel5.setText("jLabel3");
        panelLists.add(jLabel5);
        jLabel5.setBounds(50, 310, 120, 70);

        queTab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Ticket", "Customer", "Service", "Status", "Payment", "Action"
            }
        ));
        jScrollPane1.setViewportView(queTab);

        panelLists.add(jScrollPane1);
        jScrollPane1.setBounds(210, 90, 750, 550);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/QUEUEtable.png"))); // NOI18N
        panelLists.add(jLabel2);
        jLabel2.setBounds(0, -80, 1010, 750);

        jTabbedPane1.addTab("Queue Lists", panelLists);

        ControlPanel.setLayout(null);

        B1.setBackground(new java.awt.Color(0, 147, 73));
        B1.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        B1.setForeground(new java.awt.Color(0, 147, 73));
        B1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B1.setText("EV - 001");
        ControlPanel.add(B1);
        B1.setBounds(200, 140, 100, 40);

        B2.setBackground(new java.awt.Color(0, 147, 73));
        B2.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        B2.setForeground(new java.awt.Color(0, 147, 73));
        B2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B2.setText("EV - 001");
        ControlPanel.add(B2);
        B2.setBounds(355, 140, 100, 40);

        B3.setBackground(new java.awt.Color(0, 147, 73));
        B3.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        B3.setForeground(new java.awt.Color(0, 147, 73));
        B3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B3.setText("EV - 001");
        ControlPanel.add(B3);
        B3.setBounds(200, 280, 100, 40);

        B4.setBackground(new java.awt.Color(0, 147, 73));
        B4.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        B4.setForeground(new java.awt.Color(0, 147, 73));
        B4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B4.setText("EV - 001");
        ControlPanel.add(B4);
        B4.setBounds(355, 280, 100, 40);

        B5.setBackground(new java.awt.Color(0, 147, 73));
        B5.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        B5.setForeground(new java.awt.Color(0, 147, 73));
        B5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B5.setText("EV - 001");
        ControlPanel.add(B5);
        B5.setBounds(200, 420, 100, 40);

        B6.setBackground(new java.awt.Color(0, 147, 73));
        B6.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        B6.setForeground(new java.awt.Color(0, 147, 73));
        B6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B6.setText("EV - 001");
        ControlPanel.add(B6);
        B6.setBounds(355, 420, 100, 40);

        B7.setBackground(new java.awt.Color(0, 147, 73));
        B7.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        B7.setForeground(new java.awt.Color(0, 147, 73));
        B7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B7.setText("EV - 001");
        ControlPanel.add(B7);
        B7.setBounds(200, 570, 100, 40);

        B8.setBackground(new java.awt.Color(0, 147, 73));
        B8.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        B8.setForeground(new java.awt.Color(0, 147, 73));
        B8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B8.setText("EV - 001");
        ControlPanel.add(B8);
        B8.setBounds(355, 570, 100, 40);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 147, 73));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("EV - 001");
        ControlPanel.add(jLabel1);
        jLabel1.setBounds(610, 150, 140, 50);

        queIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/ControlQe.png"))); // NOI18N
        ControlPanel.add(queIcon);
        queIcon.setBounds(0, -80, 1010, 760);

        jTabbedPane1.addTab("Queue Control", ControlPanel);

        add(jTabbedPane1);
        jTabbedPane1.setBounds(0, 50, 1020, 710);

        MainIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/Tab Pane.png"))); // NOI18N
        add(MainIcon);
        MainIcon.setBounds(0, -10, 1000, 770);
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel B1;
    private javax.swing.JLabel B2;
    private javax.swing.JLabel B3;
    private javax.swing.JLabel B4;
    private javax.swing.JLabel B5;
    private javax.swing.JLabel B6;
    private javax.swing.JLabel B7;
    private javax.swing.JLabel B8;
    private javax.swing.JButton Baybutton;
    private javax.swing.JPanel ControlPanel;
    private javax.swing.JLabel MainIcon;
    private javax.swing.JButton businessbutton;
    private javax.swing.JButton exitlogin;
    private javax.swing.JButton historybutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel panelLists;
    private javax.swing.JLabel queIcon;
    private javax.swing.JTable queTab;
    private javax.swing.JButton staffbutton;
    // End of variables declaration//GEN-END:variables

}
