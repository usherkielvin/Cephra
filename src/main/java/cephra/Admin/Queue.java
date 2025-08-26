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
import javax.swing.DefaultCellEditor;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import cephra.Frame.Monitor;

public class Queue extends javax.swing.JPanel {
private JButton[] gridButtons;
private JPopupMenu popupMenu;
private JMenuItem deleteItem;
private JButton currentButton;
private int buttonCount = 0;
private static Monitor monitorInstance;




    public Queue() {
        initComponents();
    setPreferredSize(new java.awt.Dimension(1000, 750));
    setSize(1000, 750);
 
    popupMenu = new JPopupMenu();
    deleteItem = new JMenuItem("Delete");
    popupMenu.add(deleteItem);
    
    // Initialize the button array with the buttons from the ControlPanel's jPanel1
    gridButtons = new JButton[] {
        jButton1, jButton2, jButton3, jButton4,
        jButton5, jButton6, jButton7, jButton8
    };

    // Initially make all grid buttons invisible except the first one (our "Next" button)
    for (JButton button : gridButtons) {
        button.setVisible(false);
        
        // Add mouse listener for right-click popup menu
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (evt.isPopupTrigger() || evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                    currentButton = (JButton) evt.getSource();
                    popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
            
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                if (evt.isPopupTrigger() || evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                    currentButton = (JButton) evt.getSource();
                    popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        });
    }  
        
        JTableHeader header = queTab.getTableHeader();
        header.setFont(new Font("Sogie UI", Font.BOLD, 16));
        
        // Register the queue table model so other modules can add rows
        cephra.Admin.QueueBridge.registerModel((DefaultTableModel) queTab.getModel());
        
        // Setup Action column with an invisible button that shows text "Proceed"
        setupActionColumn();
        // Setup Payment column for marking as paid
        setupPaymentColumn();
        jPanel1.setOpaque(false);
        deleteItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // Use the stored button reference to call the delete method
                if (currentButton != null) {
                    deleteButton(currentButton);
                    // Reset the reference
                    currentButton = null;
                }
            }
        });
        
        // Create a single instance of Monitor
        if (monitorInstance == null) {
            monitorInstance = new cephra.Frame.Monitor();
            monitorInstance.setVisible(true);
        }
    }
    
    private void updateDisplayFrame() {
        String[] texts = new String[8];
        for (int i = 0; i < gridButtons.length; i++) {
            if (i < buttonCount) {
                texts[i] = gridButtons[i].getText();
            } else {
                texts[i] = "";
            }
        }
        
        // Use the existing monitor instance
        if (monitorInstance != null) {
            monitorInstance.updateDisplay(texts);
        }
    }
    
    private void deleteButton(JButton buttonToDelete) {
        int indexToDelete = -1;

        // Find the index of the button to be deleted
        for (int i = 0; i < buttonCount; i++) {
            if (gridButtons[i] == buttonToDelete) {
                indexToDelete = i;
                break;
            }
        }

        if (indexToDelete != -1) {
            // Shift all buttons after the deleted one to the left
            for (int i = indexToDelete; i < buttonCount - 1; i++) {
                gridButtons[i].setText(gridButtons[i + 1].getText());
                gridButtons[i].setVisible(true);
            }
            
            // Hide the last button and clear its text
            gridButtons[buttonCount - 1].setText("");
            gridButtons[buttonCount - 1].setVisible(false);
            
            // Decrease the button counter
            buttonCount--;
            
            // Update the display frame
            updateDisplayFrame();
        }
    }

private void setupActionColumn() {
    final int actionCol = getColumnIndex("Action");
    final int statusCol = getColumnIndex("Status");
    if (actionCol < 0 || statusCol < 0) {
        return;
    }

    // Renderer: show Proceed on any row that has a real ticket
    queTab.getColumnModel().getColumn(actionCol).setCellRenderer(new TableCellRenderer() {
        private final JButton button = createFlatButton();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Object ticketVal = table.getValueAt(row, getColumnIndex("Ticket"));
            boolean hasTicket = ticketVal != null && String.valueOf(ticketVal).trim().length() > 0;
            if (hasTicket) {
                button.setText("Proceed");
                button.setVisible(true); // Show the button
                return button; // Return the button if there is a valid ticket
            }
            button.setVisible(false); // Hide the button if no ticket
            return new JLabel(); // Return an empty label if no valid ticket
        }
    });

    // Editor: clicking updates status to "Waiting"
    queTab.getColumnModel().getColumn(actionCol).setCellEditor(new ProceedEditor(statusCol));

    // Add ActionListener to the button
    queTab.getColumnModel().getColumn(actionCol).setCellEditor(new DefaultCellEditor(new JTextField()) {
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            JButton button = createFlatButton();
            button.setText("Proceed");
            button.addActionListener(e -> {
                // Check if the row has a valid ticket before proceeding
                Object ticketVal = table.getValueAt(row, getColumnIndex("Ticket"));
                boolean hasTicket = ticketVal != null && String.valueOf(ticketVal).trim().length() > 0;

                if (hasTicket) {
                    // Logic for button click
                    if (buttonCount < 8) {
                        // Shift the buttons to the right
                        for (int i = buttonCount; i > 0; i--) {
                            gridButtons[i].setText(gridButtons[i - 1].getText());
                            gridButtons[i].setVisible(true);
                        }

                        // Place the new button at the beginning (index 0)
                        gridButtons[0].setVisible(true);
                        gridButtons[0].setText(String.valueOf(ticketVal));
                        
                        buttonCount++;
                        
                        // Update the monitor display immediately
                        updateDisplayFrame();
                    } else {
                        JOptionPane.showMessageDialog(table, "Queue is full. Please wait for some tickets to be processed.");
                    }
                } else {
                    // Optionally, show a message or feedback that the row is empty
                    JOptionPane.showMessageDialog(table, "Cannot proceed: The row is empty.");
                }
            });
            return button; // Return the button for editing
        }
    });
}

    private static JButton createFlatButton() {
        JButton b = new JButton();
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
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
                }
            }
            stopCellEditing();
        }
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
                    return btn;
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
        jButton9 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        queTab = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        ControlPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
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

        jButton9.setText("jButton9");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        panelLists.add(jButton9);
        jButton9.setBounds(740, 60, 75, 23);

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

        jPanel1.setLayout(new java.awt.GridLayout(4, 2, 100, 100));

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);

        jButton3.setText("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);

        jButton4.setText("jButton4");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);

        jButton5.setText("jButton5");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5);

        jButton6.setText("jButton6");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6);

        jButton7.setText("jButton7");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton7);

        jButton8.setText("jButton8");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton8);

        ControlPanel.add(jPanel1);
        jPanel1.setBounds(590, 120, 350, 410);

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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       if (buttonCount < 8) {
            // Shift the buttons to the right
            for (int i = buttonCount; i > 0; i--) {
                gridButtons[i].setText(gridButtons[i - 1].getText());
                gridButtons[i].setVisible(true);
            }

            // Place the new button at the beginning (index 0)
            gridButtons[0].setVisible(true);
            gridButtons[0].setText(String.valueOf(buttonCount + 1));
            
            buttonCount++;
            updateDisplayFrame(); // Update the clone display
        }
    
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
 if (buttonCount < 8) {
            // Shift the buttons to the right
            for (int i = buttonCount; i > 0; i--) {
                gridButtons[i].setText(gridButtons[i - 1].getText());
                gridButtons[i].setVisible(true);
            }

            // Place the new button at the beginning (index 0)
            gridButtons[0].setVisible(true);
            gridButtons[0].setText(String.valueOf(buttonCount + 1));
            
            buttonCount++;
            updateDisplayFrame(); // Update the clone display
        
}
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
          // TODO add your handling code here:
        
        
         if (buttonCount < 8) {
        // Shift the buttons to the right
        for (int i = buttonCount; i > 0; i--) {
            gridButtons[i].setText(gridButtons[i - 1].getText());
            gridButtons[i].setVisible(true);
        }

        // Place the new button at the beginning (index 0)
        gridButtons[0].setVisible(true);
        gridButtons[0].setText(String.valueOf(buttonCount + 1));
        
        buttonCount++;
    }
        
        
        
        
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
 if (buttonCount < 8) {
            // Shift the buttons to the right
            for (int i = buttonCount; i > 0; i--) {
                gridButtons[i].setText(gridButtons[i - 1].getText());
                gridButtons[i].setVisible(true);
            }

            // Place the new button at the beginning (index 0)
            gridButtons[0].setVisible(true);
            gridButtons[0].setText(String.valueOf(buttonCount + 1));
            
            buttonCount++;
            updateDisplayFrame(); // Update the clone display
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
 if (buttonCount < 8) {
            // Shift the buttons to the right
            for (int i = buttonCount; i > 0; i--) {
                gridButtons[i].setText(gridButtons[i - 1].getText());
                gridButtons[i].setVisible(true);
            }

            // Place the new button at the beginning (index 0)
            gridButtons[0].setVisible(true);
            gridButtons[0].setText(String.valueOf(buttonCount + 1));
            
            buttonCount++;
            updateDisplayFrame(); // Update the clone display
        
}        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
 if (buttonCount < 8) {
            // Shift the buttons to the right
            for (int i = buttonCount; i > 0; i--) {
                gridButtons[i].setText(gridButtons[i - 1].getText());
                gridButtons[i].setVisible(true);
            }

            // Place the new button at the beginning (index 0)
            gridButtons[0].setVisible(true);
            gridButtons[0].setText(String.valueOf(buttonCount + 1));
            
            buttonCount++;
            updateDisplayFrame(); // Update the clone display
        
}        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
 if (buttonCount < 8) {
            // Shift the buttons to the right
            for (int i = buttonCount; i > 0; i--) {
                gridButtons[i].setText(gridButtons[i - 1].getText());
                gridButtons[i].setVisible(true);
            }

            // Place the new button at the beginning (index 0)
            gridButtons[0].setVisible(true);
            gridButtons[0].setText(String.valueOf(buttonCount + 1));
            
            buttonCount++;
            updateDisplayFrame(); // Update the clone display
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
 if (buttonCount < 8) {
            // Shift the buttons to the right
            for (int i = buttonCount; i > 0; i--) {
                gridButtons[i].setText(gridButtons[i - 1].getText());
                gridButtons[i].setVisible(true);
            }

            // Place the new button at the beginning (index 0)
            gridButtons[0].setVisible(true);
            gridButtons[0].setText(String.valueOf(buttonCount + 1));
            
            buttonCount++;
            updateDisplayFrame(); // Update the clone display
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
 if (buttonCount < 8) {
            // Shift the buttons to the right
            for (int i = buttonCount; i > 0; i--) {
                gridButtons[i].setText(gridButtons[i - 1].getText());
                gridButtons[i].setVisible(true);
            }

            // Place the new button at the beginning (index 0)
            gridButtons[0].setVisible(true);
            gridButtons[0].setText(String.valueOf(buttonCount + 1));
            
            buttonCount++;
            updateDisplayFrame(); // Update the clone display
        }
    }//GEN-LAST:event_jButton8ActionPerformed


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
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel panelLists;
    private javax.swing.JLabel queIcon;
    private javax.swing.JTable queTab;
    private javax.swing.JButton staffbutton;
    // End of variables declaration//GEN-END:variables

}
