package cephra.Admin;

import java.awt.Font;
import java.awt.Window;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.AbstractCellEditor;
import javax.swing.JOptionPane;
import java.awt.Component;


public class Queue extends javax.swing.JPanel {

    public Queue() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);       
        
        JTableHeader header = queTab.getTableHeader();
        header.setFont(new Font("Sogie UI", Font.BOLD, 16));
        
        // Register the queue table model so other modules can add rows
        cephra.Admin.QueueBridge.registerModel((DefaultTableModel) queTab.getModel());
        
        // Setup Action column with an invisible button that shows text "Proceed"
        setupActionColumn();
        // Setup Payment column for marking as paid
        setupPaymentColumn();
        
    }

    private void setupActionColumn() {
        final int actionCol = getColumnIndex("Action");
        final int statusCol = getColumnIndex("Status");
        if (actionCol < 0 || statusCol < 0) {
            return;
        }

        // Renderer: show plain text "Proceed" for rows with a ticket (no blue styling)
        queTab.getColumnModel().getColumn(actionCol).setCellRenderer(new TableCellRenderer() {
            private final JLabel label = new JLabel("");
            private final JLabel empty = new JLabel("");

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Object ticketVal = table.getValueAt(row, getColumnIndex("Ticket"));
                boolean hasTicket = ticketVal != null && String.valueOf(ticketVal).trim().length() > 0;
                if (hasTicket) {
                    label.setText("Proceed");
                    label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    return label;
                }
                empty.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                return empty;
            }
        });

        // Editor: clicking updates status to "Waiting"
        queTab.getColumnModel().getColumn(actionCol).setCellEditor(new ProceedEditor(statusCol));
    }

    private static JButton createFlatButton() {
        JButton b = new JButton();
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b.setText("Proceed");
        return b;
    }

    private int getColumnIndex(String name) {
        for (int i = 0; i < queTab.getColumnModel().getColumnCount(); i++) {
            if (name.equals(queTab.getColumnModel().getColumn(i).getHeaderValue())) {
                return i;
            }
        }
        return -1;
    }

    private class ProceedEditor extends AbstractCellEditor implements TableCellEditor, java.awt.event.ActionListener {
        private final JButton button = createFlatButton();
        private int editingRow = -1;
        private final int statusColumnIndex;
        private final JLabel empty = new JLabel("");

        ProceedEditor(int statusColumnIndex) {
            this.statusColumnIndex = statusColumnIndex;
            button.addActionListener(this);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            editingRow = row;
            Object ticketVal = table.getValueAt(row, getColumnIndex("Ticket"));
            boolean hasTicket = ticketVal != null && String.valueOf(ticketVal).trim().length() > 0;
            if (hasTicket) {
                button.setText("Proceed");
                return button;
            }
            return empty;
        }

        @Override
        public Object getCellEditorValue() {
            return "Proceed";
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (editingRow >= 0 && editingRow < queTab.getRowCount()) {
                Object statusVal = queTab.getValueAt(editingRow, statusColumnIndex);
                String status = statusVal == null ? "" : String.valueOf(statusVal).trim();
                int paymentCol = getColumnIndex("Payment");
                int ticketCol = getColumnIndex("Ticket");
                int customerCol = getColumnIndex("Customer");
                if ("Pending".equalsIgnoreCase(status)) {
                    queTab.setValueAt("Waiting", editingRow, statusColumnIndex);
                } else if ("Waiting".equalsIgnoreCase(status)) {
                    queTab.setValueAt("Charging", editingRow, statusColumnIndex);
                } else if ("Charging".equalsIgnoreCase(status)) {
                    queTab.setValueAt("Complete", editingRow, statusColumnIndex);
                    if (paymentCol >= 0) {
                        // Upon completion, payment becomes Pending
                        queTab.setValueAt("Pending", editingRow, paymentCol);
                    }
                    // Notify phone frame to show payment popup
                    try {
                        java.awt.Window[] windows = java.awt.Window.getWindows();
                        for (java.awt.Window window : windows) {
                            if (window instanceof cephra.Frame.Phone) {
                                ((cephra.Frame.Phone) window).switchPanel(new cephra.Phone.PayPop());
                                break;
                            }
                        }
                    } catch (Throwable t) {
                        // ignore if phone frame not running
                    }
                } else if ("Complete".equalsIgnoreCase(status)) {
                    // If paid, move to History and remove from Queue
                    String payment = paymentCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, paymentCol)) : "";
                    if ("Paid".equalsIgnoreCase(payment)) {
                        final String ticket = ticketCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, ticketCol)) : "";
                        final String customer = customerCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, customerCol)) : "";
                        final String servedBy = "Admin";
                        final String dateTime = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        final String reference = generateReference();
                        final int rowToRemove = editingRow;

                        // Use invokeLater to avoid EDT conflicts
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                Object[] historyRow = new Object[] { ticket, customer, "", "", servedBy, dateTime, reference };
                                try {
                                    cephra.Admin.HistoryBridge.addRecord(historyRow);
                                } catch (Throwable t) {
                                    // ignore if history not ready
                                }
                                try {
                                    ((DefaultTableModel) queTab.getModel()).removeRow(rowToRemove);
                                } catch (Throwable t) {
                                    // ignore if row already removed
                                }
                                try {
                                    cephra.Admin.QueueBridge.removeTicket(ticket);
                                } catch (Throwable t) {
                                    // ignore if queue not ready
                                }
                            }
                        });
                    }
                }
            }
            stopCellEditing();
        }
    }

    private static String generateReference() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random r = new java.util.Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(r.nextInt(chars.length())));
        }
        return sb.toString();
    }



    private void setupPaymentColumn() {
        final int paymentCol = getColumnIndex("Payment");
        if (paymentCol < 0) return;

        queTab.getColumnModel().getColumn(paymentCol).setCellRenderer(new TableCellRenderer() {
            private final JButton btn = createFlatButton();
            private final JLabel label = new JLabel("");

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                String v = value == null ? "" : String.valueOf(value).trim();
                int statusCol = getColumnIndex("Status");
                String status = statusCol >= 0 ? String.valueOf(table.getValueAt(row, statusCol)) : "";
                if ("Complete".equalsIgnoreCase(status) && "Pending".equalsIgnoreCase(v)) {
                    btn.setText("Pending");
                    return btn; // transparent, unstyled button
                }
                label.setText(v);
                return label;
            }
        });

        queTab.getColumnModel().getColumn(paymentCol).setCellEditor(new PaymentEditor());
    }

    private class PaymentEditor extends AbstractCellEditor implements TableCellEditor, java.awt.event.ActionListener {
        private final JButton btn = createFlatButton();
        private final JLabel label = new JLabel("");
        private int editingRow = -1;
        private String editorValue = "";

        PaymentEditor() {
            btn.addActionListener(this);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            editingRow = row;
            String v = value == null ? "" : String.valueOf(value).trim();
            editorValue = v;
            int statusCol = getColumnIndex("Status");
            String status = statusCol >= 0 ? String.valueOf(table.getValueAt(row, statusCol)) : "";
            if ("Complete".equalsIgnoreCase(status) && "Pending".equalsIgnoreCase(v)) {
                btn.setText("Pending");
                return btn;
            }
            label.setText(v);
            return label;
        }

        @Override
        public Object getCellEditorValue() {
            return editorValue;
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (editingRow >= 0 && editingRow < queTab.getRowCount()) {
                Object[] options = new Object[] { "Mark as Paid", "Cancel" };
                int choice = JOptionPane.showOptionDialog(
                    Queue.this,
                    "Mark this payment as paid?",
                    "Payment",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
                );
                if (choice == 0) {
                    editorValue = "Paid";
                }
            }
            stopCellEditing();
        }
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
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
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only Action column (column 5) and Payment column (column 4) are editable for button clicks
                return column == 5 || column == 4;
            }
        });
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
        B1.setBounds(30, 70, 100, 40);

        B2.setBackground(new java.awt.Color(0, 147, 73));
        B2.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        B2.setForeground(new java.awt.Color(0, 147, 73));
        B2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B2.setText("EV - 001");
        ControlPanel.add(B2);
        B2.setBounds(40, 230, 100, 40);

        B3.setBackground(new java.awt.Color(0, 147, 73));
        B3.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        B3.setForeground(new java.awt.Color(0, 147, 73));
        B3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B3.setText("EV - 001");
        ControlPanel.add(B3);
        B3.setBounds(40, 390, 100, 40);

        B4.setBackground(new java.awt.Color(0, 147, 73));
        B4.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        B4.setForeground(new java.awt.Color(0, 147, 73));
        B4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B4.setText("EV - 001");
        ControlPanel.add(B4);
        B4.setBounds(40, 550, 100, 40);

        B5.setBackground(new java.awt.Color(0, 147, 73));
        B5.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        B5.setForeground(new java.awt.Color(0, 147, 73));
        B5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B5.setText("EV - 001");
        ControlPanel.add(B5);
        B5.setBounds(200, 70, 100, 40);

        B6.setBackground(new java.awt.Color(0, 147, 73));
        B6.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        B6.setForeground(new java.awt.Color(0, 147, 73));
        B6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B6.setText("EV - 001");
        ControlPanel.add(B6);
        B6.setBounds(200, 230, 100, 40);

        B7.setBackground(new java.awt.Color(0, 147, 73));
        B7.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        B7.setForeground(new java.awt.Color(0, 147, 73));
        B7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B7.setText("EV - 001");
        ControlPanel.add(B7);
        B7.setBounds(200, 390, 100, 40);

        B8.setBackground(new java.awt.Color(0, 147, 73));
        B8.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        B8.setForeground(new java.awt.Color(0, 147, 73));
        B8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B8.setText("EV - 001");
        ControlPanel.add(B8);
        B8.setBounds(200, 550, 100, 40);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 147, 73));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("EV - 001");
        ControlPanel.add(jLabel1);
        jLabel1.setBounds(610, 130, 140, 60);

        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        ControlPanel.add(jButton1);
        jButton1.setBounds(330, 450, 140, 70);

        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        ControlPanel.add(jButton2);
        jButton2.setBounds(330, 530, 140, 60);

        queIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/ControlQe.png"))); // NOI18N
        ControlPanel.add(queIcon);
        queIcon.setBounds(0, -60, 1010, 740);

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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
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
