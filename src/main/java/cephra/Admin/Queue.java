package cephra.Admin;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import cephra.Frame.Monitor;

public class Queue extends javax.swing.JPanel {
    private static Monitor monitorInstance;
    private JButton[] gridButtons;
    private int buttonCount = 0;

    public Queue() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);       
        setupDateTimeTimer();    
        
        jtableDesign.apply(queTab);
        jtableDesign.makeScrollPaneTransparent(jScrollPane1);
 
        JTableHeader header = queTab.getTableHeader();
        header.setFont(new Font("Sogie UI", Font.BOLD, 16));
        
        // Register the queue table model so other modules can add rows
        cephra.Admin.QueueBridge.registerModel((DefaultTableModel) queTab.getModel());
        
        // Setup Action column with an invisible button that shows text "Proceed"
        setupActionColumn();
        // Setup Payment column for marking as paid
        setupPaymentColumn();
        jPanel1.setOpaque(false);
        
        // Create a single instance of Monitor
        if (monitorInstance == null) {
            monitorInstance = new cephra.Frame.Monitor();
            monitorInstance.setVisible(true);
        }
        
        // Initialize grid buttons
        gridButtons = new JButton[] {
            jButton1, jButton2, jButton3, jButton4, jButton5,
            jButton6, jButton7, jButton8, jButton9, jButton10
        };
        
        // Initially hide all grid buttons
        for (JButton button : gridButtons) {
            button.setVisible(false);
        }
        
        // Setup next buttons
        setupNextButtons();

        // Listen to table changes to keep counters in sync
        queTab.getModel().addTableModelListener(e -> updateStatusCounters());
        updateStatusCounters();
    }

    private void updateStatusCounters() {
        int waiting = 0;
        int charging = 0;
        for (int i = 0; i < queTab.getRowCount(); i++) {
            Object s = queTab.getValueAt(i, getColumnIndex("Status"));
            String status = s == null ? "" : String.valueOf(s).trim();
            if ("Waiting".equalsIgnoreCase(status)) waiting++;
            else if ("Charging".equalsIgnoreCase(status)) charging++;
        }
        int paidCumulative = 0;
        try {
            paidCumulative = cephra.Admin.QueueBridge.getTotalPaidCount();
        } catch (Throwable t) {
            // Ignore errors
        }
        try { 
            Waitings.setText(String.valueOf(waiting)); 
        } catch (Throwable t) {}
        try { 
            Charging.setText(String.valueOf(charging)); 
        } catch (Throwable t) {}
        try { 
            Paid.setText(String.valueOf(paidCumulative)); 
        } catch (Throwable t) {}
    }

    private void setupActionColumn() {
        final int actionCol = getColumnIndex("Action");
        final int statusCol = getColumnIndex("Status");
        if (actionCol < 0 || statusCol < 0) return;

        // Renderer: show Proceed on any row that has a real ticket
        queTab.getColumnModel().getColumn(actionCol).setCellRenderer(new TableCellRenderer() {
            private final JButton button = createFlatButton();
            private final JLabel empty = new JLabel("");

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Object ticketVal = table.getValueAt(row, getColumnIndex("Ticket"));
                boolean hasTicket = ticketVal != null && String.valueOf(ticketVal).trim().length() > 0;
                if (hasTicket) {
                    button.setText("Proceed");
                    button.setForeground(new java.awt.Color(255, 255, 255)); // Ensure white text color
                    return button;
                }
                return empty;
            }
        });

    // Combined Editor: handles both status progression AND grid button updates
    queTab.getColumnModel().getColumn(actionCol).setCellEditor(new CombinedProceedEditor(statusCol));
}

private class CombinedProceedEditor extends AbstractCellEditor implements TableCellEditor, java.awt.event.ActionListener {
        private final JButton button = createFlatButton();
        private int editingRow = -1;
        private final int statusColumnIndex;

    CombinedProceedEditor(int statusColumnIndex) {
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
        return new JLabel("");
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
            
            // Get ticket value for grid button functionality
            Object ticketVal = queTab.getValueAt(editingRow, ticketCol);
            String ticket = ticketVal != null ? String.valueOf(ticketVal).trim() : "";
            
                if ("Pending".equalsIgnoreCase(status)) {
                    // Only move to Waiting if there is capacity in the waiting grid
                    if (!ticket.isEmpty() && buttonCount < 10) {
                        queTab.setValueAt("Waiting", editingRow, statusColumnIndex);
                        // Update database status
                        cephra.CephraDB.updateQueueTicketStatus(ticket, "Waiting");
                        gridButtons[buttonCount].setText(ticket);
                        gridButtons[buttonCount].setVisible(true);
                        buttonCount++;
                        updateMonitorDisplay();
                        
                        // Update battery level when ticket moves to waiting
                        try {
                            String customer = customerCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, customerCol)) : "";
                            if (!customer.isEmpty()) {
                                int currentBattery = cephra.CephraDB.getUserBatteryLevel(customer);
                                // Slightly decrease battery level while waiting (simulate idle drain)
                                int newBattery = Math.max(1, currentBattery - 1);
                                cephra.CephraDB.setUserBatteryLevel(customer, newBattery);
                                System.out.println("Queue: Updated battery level for " + customer + " to " + newBattery + "% (waiting)");
                            }
                        } catch (Exception batteryEx) {
                            System.err.println("Error updating battery level: " + batteryEx.getMessage());
                        }
                    } else {
                        // Keep status as Pending and inform user that waiting grid is full
                        JOptionPane.showMessageDialog(Queue.this,
                            "Waiting grid is full! Ticket remains Pending.",
                            "Waiting Grid Full",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if ("Waiting".equalsIgnoreCase(status)) {
                    boolean isFast = ticket.startsWith("FCH");
                    boolean assigned = isFast ? assignToFastSlot(ticket) : assignToNormalSlot(ticket);
                    if (assigned) {
                        queTab.setValueAt("Charging", editingRow, statusColumnIndex);
                        // Update database status
                        cephra.CephraDB.updateQueueTicketStatus(ticket, "Charging");
                        removeTicketFromGrid(ticket);
                        
                                                 // Create active ticket when charging starts
                         try {
                             String customer = customerCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, customerCol)) : "";
                             int serviceCol = getColumnIndex("Service");
                             String serviceName = serviceCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, serviceCol)) : "";
                             
                             // Get user's current battery level (this is the initial battery level)
                             int initialBatteryLevel = cephra.CephraDB.getUserBatteryLevel(customer);
                             
                             // Determine bay number based on assignment
                             String bayNumber = "";
                             if (isFast) {
                                 if (ticket.equals(fastslot1.getText())) bayNumber = "Bay-1";
                                 else if (ticket.equals(fastslot2.getText())) bayNumber = "Bay-2";
                                 else if (ticket.equals(fastslot3.getText())) bayNumber = "Bay-3";
                             } else {
                                 if (ticket.equals(normalcharge1.getText())) bayNumber = "Bay-4";
                                 else if (ticket.equals(normalcharge2.getText())) bayNumber = "Bay-5";
                                 else if (ticket.equals(normalcharge3.getText())) bayNumber = "Bay-6";
                                 else if (ticket.equals(normalcharge4.getText())) bayNumber = "Bay-7";
                                 else if (ticket.equals(normalcharge5.getText())) bayNumber = "Bay-8";
                             }
                             
                             // Create active ticket with initial battery level (save the starting point)
                             cephra.CephraDB.setActiveTicket(customer, ticket, serviceName, initialBatteryLevel, bayNumber);
                             System.out.println("Created active ticket: " + ticket + " for customer: " + customer + " in bay: " + bayNumber + " with initial battery: " + initialBatteryLevel + "%");
                             
                             // Store initial battery level in QueueBridge for later calculations
                             cephra.Admin.QueueBridge.setTicketBatteryInfo(ticket, initialBatteryLevel, 40.0);
                             System.out.println("Queue: Saved initial battery level " + initialBatteryLevel + "% for ticket " + ticket);
                         } catch (Exception ex) {
                             System.err.println("Error setting up active ticket: " + ex.getMessage());
                         }
                    } else {
                        String msg = isFast ?
                            "Fast Charge Bays 1-3 are full!\nTicket " + ticket + " remains in waiting queue." :
                            "Normal Charge Bays 1-5 are full!\nTicket " + ticket + " remains in waiting queue.";
                        String title = isFast ? "Fast Charge Bays Full" : "Normal Charge Bays Full";
                        JOptionPane.showMessageDialog(Queue.this, msg, title, JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if ("Charging".equalsIgnoreCase(status)) {
                    queTab.setValueAt("Complete", editingRow, statusColumnIndex);
                    // Update database status
                    cephra.CephraDB.updateQueueTicketStatus(ticket, "Complete");
                    if (paymentCol >= 0) {
                        // Upon completion, payment becomes Pending
                        queTab.setValueAt("Pending", editingRow, paymentCol);
                    }
                    // Remove from charging grids and refresh monitor
                    removeFromChargingSlots(ticket);
                    
                    // Set battery level to 100% when charging is completed
                    try {
                        String customer = customerCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, customerCol)) : "";
                        if (!customer.isEmpty()) {
                            cephra.CephraDB.setUserBatteryLevel(customer, 100);
                            System.out.println("Queue: Charging completed for " + customer + ", battery level set to 100%");
                        }
                    } catch (Exception ex) {
                        System.err.println("Error setting battery to 100%: " + ex.getMessage());
                    }

                    // Set current ticket context for phone and mark payment pending
                    try {
                        int serviceCol = getColumnIndex("Service");
                        String serviceName = serviceCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, serviceCol)) : "";
                        cephra.Phone.QueueFlow.setCurrent(ticket, serviceName);
                        cephra.Phone.QueueFlow.updatePaymentStatus(ticket, "Pending");
                            } catch (Throwable t) {
            // Ignore errors
        }

                                            // Compute amount due and kWh based on actual battery level
                        try {
                            double amount = cephra.Admin.QueueBridge.computeAmountDue(ticket);
                            // Get actual battery info for kWh calculation
                            cephra.Admin.QueueBridge.BatteryInfo batteryInfo = cephra.Admin.QueueBridge.getTicketBatteryInfo(ticket);
                            if (batteryInfo == null) {
                                // Get from user's actual battery level
                                String customer = String.valueOf(queTab.getValueAt(editingRow, getColumnIndex("Customer")));
                                int userBatteryLevel = cephra.CephraDB.getUserBatteryLevel(customer);
                                batteryInfo = new cephra.Admin.QueueBridge.BatteryInfo(userBatteryLevel, 40.0);
                            }
                            double usedKWh = (100.0 - batteryInfo.initialPercent) / 100.0 * batteryInfo.capacityKWh;
                            System.out.println("Amount due for " + ticket + ": " + String.format("%.2f", amount) + ", kWh: " + String.format("%.1f", usedKWh));
                        } catch (Throwable t) {
                            // Ignore compute errors
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
                        // Ignore if phone frame not running
                    }
                } else if ("Complete".equalsIgnoreCase(status)) {
                
                    // If paid, move to History and remove from Queue
                    String payment = paymentCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, paymentCol)) : "";
                    System.out.println("Queue: Processing Complete ticket " + ticket + " with payment status: " + payment);
                    
                    if ("Paid".equalsIgnoreCase(payment)) {
                        final int rowToRemove = editingRow;
                        
                        // Check if payment has already been processed to prevent duplicates
                        boolean alreadyProcessed = cephra.CephraDB.isPaymentAlreadyProcessed(ticket);
                        System.out.println("Queue: Checking if payment already processed for ticket " + ticket + ": " + alreadyProcessed);
                        
                        if (alreadyProcessed) {
                            System.out.println("Queue: Payment already processed for ticket " + ticket + ", removing from queue directly");
                            // Remove from table and queue bridge without processing payment again
                            try {
                                if (rowToRemove >= 0 && rowToRemove < queTab.getRowCount()) {
                                    ((DefaultTableModel) queTab.getModel()).removeRow(rowToRemove);
                                    System.out.println("Queue: Successfully removed row " + rowToRemove + " from table");
                                }
                            } catch (Throwable t) {
                                System.err.println("Error removing row from table: " + t.getMessage());
                            }
                            
                            try {
                                cephra.Admin.QueueBridge.removeTicket(ticket);
                                System.out.println("Queue: Successfully removed ticket " + ticket + " from queue bridge");
                            } catch (Throwable t) {
                                System.err.println("Error removing ticket from queue bridge: " + t.getMessage());
                            }
                            return; // Exit early since payment was already processed
                        }
                        
                        System.out.println("Queue: Payment not yet processed, proceeding with payment transaction for ticket " + ticket);
                        
                        final String customer = customerCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, customerCol)) : "";
                        // Get reference number from QueueBridge to ensure consistency
                        final String reference = cephra.Admin.QueueBridge.getTicketRefNumber(ticket);
                        
                        // Process payment transaction immediately
                        double amount = 0.0;
                        try {
                            // Centralized calculation for total amount
                            amount = cephra.Admin.QueueBridge.computeAmountDue(ticket);
                        } catch (Throwable t) {
                            System.err.println("Error computing amount for ticket " + ticket + ": " + t.getMessage());
                        }
                        
                        // Process payment transaction to move ticket to charging history
                        try {
                            int serviceCol = getColumnIndex("Service");
                            String serviceName = serviceCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, serviceCol)) : "";
                            
                            // Get battery info for calculations
                            cephra.Admin.QueueBridge.BatteryInfo batteryInfo = cephra.Admin.QueueBridge.getTicketBatteryInfo(ticket);
                            if (batteryInfo == null) {
                                int userBatteryLevel = cephra.CephraDB.getUserBatteryLevel(customer);
                                batteryInfo = new cephra.Admin.QueueBridge.BatteryInfo(userBatteryLevel, 40.0);
                            }
                            
                            // Calculate charging time based on service type
                            int chargingTimeMinutes = 0;
                            if (serviceName != null && serviceName.contains("Fast")) {
                                chargingTimeMinutes = (int)((100.0 - batteryInfo.initialPercent) * 0.8); // 0.8 min per 1%
                            } else {
                                chargingTimeMinutes = (int)((100.0 - batteryInfo.initialPercent) * 1.6); // 1.6 min per 1%
                            }
                            
                            // Process the payment transaction (this will add to charging history)
                            boolean success = cephra.CephraDB.processPaymentTransaction(
                                ticket, customer, serviceName, 
                                batteryInfo.initialPercent, chargingTimeMinutes, 
                                amount, "Cash", reference
                            );
                            
                            if (success) {
                                System.out.println("Successfully processed payment transaction for ticket: " + ticket);
                                // Clear the active ticket since it's now in history
                                try {
                                    cephra.CephraDB.clearActiveTicketByTicketId(ticket);
                                } catch (Exception ex) {
                                    System.err.println("Error clearing active ticket: " + ex.getMessage());
                                }
                                
                                // Remove from table and queue bridge immediately after successful payment
                                try {
                                    if (rowToRemove >= 0 && rowToRemove < queTab.getRowCount()) {
                                        ((DefaultTableModel) queTab.getModel()).removeRow(rowToRemove);
                                        System.out.println("Queue: Successfully removed row " + rowToRemove + " from table");
                                    }
                                } catch (Throwable t) {
                                    System.err.println("Error removing row from table: " + t.getMessage());
                                }
                                
                                try {
                                    cephra.Admin.QueueBridge.removeTicket(ticket);
                                    System.out.println("Queue: Successfully removed ticket " + ticket + " from queue bridge");
                                } catch (Throwable t) {
                                    System.err.println("Error removing ticket from queue bridge: " + t.getMessage());
                                }
                            } else {
                                System.err.println("Failed to process payment transaction for ticket: " + ticket);
                                // Show error message to user
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(Queue.this,
                                        "Failed to process payment for ticket " + ticket + ".\nPlease try again.",
                                        "Payment Error",
                                        JOptionPane.ERROR_MESSAGE);
                                });
                            }
                        } catch (Exception ex) {
                            System.err.println("Error processing payment transaction: " + ex.getMessage());
                            // Show error message to user
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(Queue.this,
                                    "Error processing payment for ticket " + ticket + ".\nPlease try again.",
                                    "Payment Error",
                                    JOptionPane.ERROR_MESSAGE);
                            });
                        }
                    }
                }
            }
            // Keep counters in sync after any action
            updateStatusCounters();
            stopCellEditing();
        }
}

    private static JButton createFlatButton() {
        JButton b = new JButton();
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b.setForeground(new java.awt.Color(255, 255, 255)); // Set text color to white to match table text
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


    
    private void setupNextButtons() {
        nxtnormalbtn.addActionListener(e -> nextNormalTicket());
        nxtfastbtn.addActionListener(e -> nextFastTicket());
    }
    
    private void nextNormalTicket() {
        String ticket = findNextTicketByType("NCH");
        if (ticket != null) {
            // Check if there's an available slot in normal charge grid
            boolean slotAvailable = false;
            
            if (normalcharge1.getText().isEmpty() || normalcharge1.getText().equals("jButton11")) {
                normalcharge1.setText(ticket);
                normalcharge1.setVisible(true);
                slotAvailable = true;
            } else if (normalcharge2.getText().isEmpty() || normalcharge2.getText().equals("jButton11")) {
                normalcharge2.setText(ticket);
                normalcharge2.setVisible(true);
                slotAvailable = true;
            } else if (normalcharge3.getText().isEmpty() || normalcharge3.getText().equals("jButton12")) {
                normalcharge3.setText(ticket);
                normalcharge3.setVisible(true);
                slotAvailable = true;
            } else if (normalcharge4.getText().isEmpty() || normalcharge4.getText().equals("jButton13")) {
                normalcharge4.setText(ticket);
                normalcharge4.setVisible(true);
                slotAvailable = true;
            } else if (normalcharge5.getText().isEmpty() || normalcharge5.getText().equals("jButton14")) {
                normalcharge5.setText(ticket);
                normalcharge5.setVisible(true);
                slotAvailable = true;
            }
            
                         // Only remove from waiting grid if a slot was available
             if (slotAvailable) {
                setTableStatusToChargingByTicket(ticket);
                 removeTicketFromGrid(ticket);
                 // Update Monitor normal grid display
                 updateMonitorNormalGrid();
             } else {
                 // Show message that all normal charge bays are full
                 JOptionPane.showMessageDialog(this,
                     "Normal Charge Bays 1-5 are full!\nTicket " + ticket + " remains in waiting queue.",
                     "Normal Charge Bays Full",
                     JOptionPane.INFORMATION_MESSAGE);
             }
             // All slots full - ticket remains in waiting grid
        }
        updateStatusCounters();
    }
    
    private void nextFastTicket() {
        String ticket = findNextTicketByType("FCH");
        if (ticket != null) {
            // Check if there's an available slot in fast panel
            boolean slotAvailable = false;
            
            if (fastslot1.getText().isEmpty() || fastslot1.getText().equals("jButton11")) {
                fastslot1.setText(ticket);
                fastslot1.setVisible(true);
                slotAvailable = true;
            } else if (fastslot2.getText().isEmpty() || fastslot2.getText().equals("jButton12")) {
                fastslot2.setText(ticket);
                fastslot2.setVisible(true);
                slotAvailable = true;
            } else if (fastslot3.getText().isEmpty() || fastslot3.getText().equals("jButton13")) {
                fastslot3.setText(ticket);
                fastslot3.setVisible(true);
                slotAvailable = true;
            }
            
                         // Only remove from waiting grid if a slot was available
             if (slotAvailable) {
                setTableStatusToChargingByTicket(ticket);
                 removeTicketFromGrid(ticket);
                 // Update Monitor fast grid display
                 updateMonitorFastGrid();
             } else {
                 // Show message that all fast charge bays are full
                 JOptionPane.showMessageDialog(this,
                     "Fast Charge Bays 1-3 are full!\nTicket " + ticket + " remains in waiting queue.",
                     "Fast Charge Bays Full",
                     JOptionPane.INFORMATION_MESSAGE);
             }
             // All slots full - ticket remains in waiting grid
        }
        updateStatusCounters();
    }
    
    private String findNextTicketByType(String type) {
        // Find the lowest numbered ticket of the specified type
        String lowestTicket = null;
        int lowestNumber = Integer.MAX_VALUE;
        
        for (int i = 0; i < buttonCount; i++) {
            String ticketText = gridButtons[i].getText();
            if (ticketText.contains(type)) {
                // Extract the number from the ticket (e.g., "NCH001" -> 1, "FCH002" -> 2)
                try {
                    String numberPart = ticketText.replaceAll("[^0-9]", "");
                    if (!numberPart.isEmpty()) {
                        int ticketNumber = Integer.parseInt(numberPart);
                        if (ticketNumber < lowestNumber) {
                            lowestNumber = ticketNumber;
                            lowestTicket = ticketText;
                        }
                    }
                } catch (NumberFormatException e) {
                    // If parsing fails, just use the ticket as is
                    if (lowestTicket == null) {
                        lowestTicket = ticketText;
                    }
                }
            }
        }
        return lowestTicket;
    }
    
    private void removeTicketFromGrid(String ticket) {
        for (int i = 0; i < buttonCount; i++) {
            if (gridButtons[i].getText().equals(ticket)) {
                // Shift remaining buttons
                for (int j = i; j < buttonCount - 1; j++) {
                    gridButtons[j].setText(gridButtons[j + 1].getText());
                    gridButtons[j].setVisible(gridButtons[j + 1].isVisible());
                }
                gridButtons[buttonCount - 1].setText("");
                gridButtons[buttonCount - 1].setVisible(false);
                buttonCount--;
                updateMonitorDisplay();
                break;
            }
        }
    }
    
         private void updateMonitorDisplay() {
         if (monitorInstance != null) {
             String[] buttonTexts = new String[10];
             for (int i = 0; i < 10; i++) {
                 if (i < buttonCount) {
                     buttonTexts[i] = gridButtons[i].getText();
                 } else {
                     buttonTexts[i] = "";
                 }
             }
             monitorInstance.updateDisplay(buttonTexts);
         }
     }
     
     private void updateMonitorFastGrid() {
         if (monitorInstance != null) {
             String[] fastTickets = new String[3];
             fastTickets[0] = fastslot1.getText().equals("jButton11") ? "" : fastslot1.getText();
             fastTickets[1] = fastslot2.getText().equals("jButton12") ? "" : fastslot2.getText();
             fastTickets[2] = fastslot3.getText().equals("jButton13") ? "" : fastslot3.getText();
             monitorInstance.updateFastGrid(fastTickets);
         }
     }
     
     private void updateMonitorNormalGrid() {
         if (monitorInstance != null) {
             String[] normalTickets = new String[5];
             normalTickets[0] = normalcharge1.getText().equals("jButton11") ? "" : normalcharge1.getText();
             normalTickets[1] = normalcharge2.getText().equals("jButton11") ? "" : normalcharge2.getText();
             normalTickets[2] = normalcharge3.getText().equals("jButton12") ? "" : normalcharge3.getText();
             normalTickets[3] = normalcharge4.getText().equals("jButton13") ? "" : normalcharge4.getText();
             normalTickets[4] = normalcharge5.getText().equals("jButton14") ? "" : normalcharge5.getText();
             monitorInstance.updateNormalGrid(normalTickets);
         }
     }

    private boolean assignToNormalSlot(String ticket) {
        if (normalcharge1.getText().isEmpty() || normalcharge1.getText().equals("jButton11")) {
            normalcharge1.setText(ticket);
            normalcharge1.setVisible(true);
            updateMonitorNormalGrid();
            return true;
        } else if (normalcharge2.getText().isEmpty() || normalcharge2.getText().equals("jButton11")) {
            normalcharge2.setText(ticket);
            normalcharge2.setVisible(true);
            updateMonitorNormalGrid();
            return true;
        } else if (normalcharge3.getText().isEmpty() || normalcharge3.getText().equals("jButton12")) {
            normalcharge3.setText(ticket);
            normalcharge3.setVisible(true);
            updateMonitorNormalGrid();
            return true;
        } else if (normalcharge4.getText().isEmpty() || normalcharge4.getText().equals("jButton13")) {
            normalcharge4.setText(ticket);
            normalcharge4.setVisible(true);
            updateMonitorNormalGrid();
            return true;
        } else if (normalcharge5.getText().isEmpty() || normalcharge5.getText().equals("jButton14")) {
            normalcharge5.setText(ticket);
            normalcharge5.setVisible(true);
            updateMonitorNormalGrid();
            return true;
        }
        return false;
    }

    private boolean assignToFastSlot(String ticket) {
        if (fastslot1.getText().isEmpty() || fastslot1.getText().equals("jButton11")) {
            fastslot1.setText(ticket);
            fastslot1.setVisible(true);
            updateMonitorFastGrid();
            return true;
        } else if (fastslot2.getText().isEmpty() || fastslot2.getText().equals("jButton12")) {
            fastslot2.setText(ticket);
            fastslot2.setVisible(true);
            updateMonitorFastGrid();
            return true;
        } else if (fastslot3.getText().isEmpty() || fastslot3.getText().equals("jButton13")) {
            fastslot3.setText(ticket);
            fastslot3.setVisible(true);
            updateMonitorFastGrid();
            return true;
        }
        return false;
    }

    private void removeFromChargingSlots(String ticket) {
        if (ticket.equals(fastslot1.getText())) { fastslot1.setText(""); fastslot1.setVisible(false); }
        if (ticket.equals(fastslot2.getText())) { fastslot2.setText(""); fastslot2.setVisible(false); }
        if (ticket.equals(fastslot3.getText())) { fastslot3.setText(""); fastslot3.setVisible(false); }
        updateMonitorFastGrid();

        if (ticket.equals(normalcharge1.getText())) { normalcharge1.setText(""); normalcharge1.setVisible(false); }
        if (ticket.equals(normalcharge2.getText())) { normalcharge2.setText(""); normalcharge2.setVisible(false); }
        if (ticket.equals(normalcharge3.getText())) { normalcharge3.setText(""); normalcharge3.setVisible(false); }
        if (ticket.equals(normalcharge4.getText())) { normalcharge4.setText(""); normalcharge4.setVisible(false); }
        if (ticket.equals(normalcharge5.getText())) { normalcharge5.setText(""); normalcharge5.setVisible(false); }
        updateMonitorNormalGrid();
    }

    private void setTableStatusToChargingByTicket(String ticket) {
        int ticketCol = getColumnIndex("Ticket");
        int statusCol = getColumnIndex("Status");
        if (ticketCol < 0 || statusCol < 0) return;
        for (int row = 0; row < queTab.getRowCount(); row++) {
            Object val = queTab.getValueAt(row, ticketCol);
            if (val != null && ticket.equals(String.valueOf(val).trim())) {
                queTab.setValueAt("Charging", row, statusCol);
                // Update database status
                cephra.CephraDB.updateQueueTicketStatus(ticket, "Charging");
                break;
            }
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
                    btn.setForeground(new java.awt.Color(255, 255, 255)); // Ensure white text color
                    return btn; // transparent, unstyled button
                } else if ("Complete".equalsIgnoreCase(status) && "Paid".equalsIgnoreCase(v)) {
                    btn.setText("Paid");
                    btn.setBackground(new java.awt.Color(200, 200, 200)); // Gray background for paid
                    btn.setForeground(new java.awt.Color(255, 255, 255)); // White text color
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
            } else if ("Complete".equalsIgnoreCase(status) && "Paid".equalsIgnoreCase(v)) {
                btn.setText("Paid");
                btn.setBackground(new java.awt.Color(200, 200, 200)); // Gray background for paid
                btn.setForeground(new java.awt.Color(255, 255, 255)); // White text color
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
                    // Sync cumulative paid counter and table via QueueBridge
                    int ticketCol = getColumnIndex("Ticket");
                    if (ticketCol >= 0) {
                        Object v = queTab.getValueAt(editingRow, ticketCol);
                        String ticket = v == null ? "" : String.valueOf(v).trim();
                        if (!ticket.isEmpty()) {
                            // Check if payment already processed to prevent duplicates
                            boolean alreadyProcessed = cephra.CephraDB.isPaymentAlreadyProcessed(ticket);
                            if (alreadyProcessed) {
                                System.out.println("Queue: Payment already processed for ticket " + ticket + ", removing from queue directly");
                                // Remove from table and queue bridge since payment is already processed
                                final int rowToRemove = editingRow;
                                try {
                                    if (rowToRemove >= 0 && rowToRemove < queTab.getRowCount()) {
                                        ((DefaultTableModel) queTab.getModel()).removeRow(rowToRemove);
                                        System.out.println("Queue: Successfully removed row " + rowToRemove + " from table");
                                    }
                                } catch (Throwable t) {
                                    System.err.println("Error removing row from table: " + t.getMessage());
                                }
                                
                                try {
                                    cephra.Admin.QueueBridge.removeTicket(ticket);
                                    System.out.println("Queue: Successfully removed ticket " + ticket + " from queue bridge");
                                } catch (Throwable t) {
                                    System.err.println("Error removing ticket from queue bridge: " + t.getMessage());
                                }
                            } else {
                                // Get customer and service information for payment processing
                                int customerCol = getColumnIndex("Customer");
                                int serviceCol = getColumnIndex("Service");
                                String customer = customerCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, customerCol)) : "";
                                String serviceName = serviceCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, serviceCol)) : "";
                                
                                System.out.println("Queue: Processing payment for ticket " + ticket + ", customer: " + customer + ", service: " + serviceName);
                                
                                // Process payment through QueueBridge (which handles all payment logic)
                                System.out.println("Queue: About to call markPaymentPaid for ticket " + ticket);
                                cephra.Admin.QueueBridge.markPaymentPaid(ticket);
                                System.out.println("Queue: Completed markPaymentPaid for ticket " + ticket);
                                
                                // Remove from table and queue bridge after successful payment
                                final int rowToRemove = editingRow;
                                try {
                                    if (rowToRemove >= 0 && rowToRemove < queTab.getRowCount()) {
                                        ((DefaultTableModel) queTab.getModel()).removeRow(rowToRemove);
                                        System.out.println("Queue: Successfully removed row " + rowToRemove + " from table");
                                    }
                                } catch (Throwable t) {
                                    System.err.println("Error removing row from table: " + t.getMessage());
                                }
                                
                                try {
                                    cephra.Admin.QueueBridge.removeTicket(ticket);
                                    System.out.println("Queue: Successfully removed ticket " + ticket + " from queue bridge");
                                } catch (Throwable t) {
                                    System.err.println("Error removing ticket from queue bridge: " + t.getMessage());
                                }
                            }
                        }
                    }
                }
            }
            stopCellEditing();
            updateStatusCounters();
        }
    }
    
     private void setupDateTimeTimer() {
        updateDateTime();
        javax.swing.Timer timer = new javax.swing.Timer(1000, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                updateDateTime();
            }
        });
        timer.start();
    }
    
    private void updateDateTime() {
        java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("hh:mm a");
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd MMMM, EEEE");
        
        java.util.Date now = new java.util.Date();
        String time = timeFormat.format(now);
        String date = dateFormat.format(now);
        
        datetimeStaff.setText(time + " " + date);
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
        Paid = new javax.swing.JLabel();
        Waitings = new javax.swing.JLabel();
        Charging = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        queTab = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        ControlPanel = new javax.swing.JPanel();
        fastpanel = new javax.swing.JPanel();
        fastslot1 = new javax.swing.JButton();
        fastslot2 = new javax.swing.JButton();
        fastslot3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        normalcharge1 = new javax.swing.JButton();
        nxtfastbtn = new javax.swing.JButton();
        nxtnormalbtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        normalcharge2 = new javax.swing.JButton();
        normalcharge3 = new javax.swing.JButton();
        normalcharge4 = new javax.swing.JButton();
        normalcharge5 = new javax.swing.JButton();
        queIcon = new javax.swing.JLabel();
        datetimeStaff = new javax.swing.JLabel();
        labelStaff = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        MainIcon = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1000, 750));
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

        Paid.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        Paid.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Paid.setText("5");
        panelLists.add(Paid);
        Paid.setBounds(50, 530, 120, 70);

        Waitings.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        Waitings.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Waitings.setText("4");
        panelLists.add(Waitings);
        Waitings.setBounds(50, 165, 120, 70);

        Charging.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        Charging.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Charging.setText("4");
        panelLists.add(Charging);
        Charging.setBounds(50, 350, 120, 70);

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
        queTab.setGridColor(new java.awt.Color(0, 0, 0));
        queTab.getTableHeader().setResizingAllowed(false);
        queTab.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(queTab);

        panelLists.add(jScrollPane1);
        jScrollPane1.setBounds(210, 90, 785, 580);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/QUEUEtable.png"))); // NOI18N
        panelLists.add(jLabel2);
        jLabel2.setBounds(0, -90, 1010, 790);

        jTabbedPane1.addTab("Queue Lists", panelLists);

        ControlPanel.setLayout(null);

        fastpanel.setOpaque(false);
        fastpanel.setLayout(new java.awt.GridLayout(3, 1, 10, 45));

        fastslot1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        fastslot1.setForeground(new java.awt.Color(0, 230, 118));
        fastslot1.setBorder(null);
        fastslot1.setBorderPainted(false);
        fastslot1.setContentAreaFilled(false);
        fastslot1.setFocusPainted(false);
        fastslot1.setFocusable(false);
        fastpanel.add(fastslot1);

        fastslot2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        fastslot2.setForeground(new java.awt.Color(0, 230, 118));
        fastslot2.setToolTipText("");
        fastslot2.setBorder(null);
        fastslot2.setBorderPainted(false);
        fastslot2.setContentAreaFilled(false);
        fastslot2.setFocusPainted(false);
        fastslot2.setFocusable(false);
        fastpanel.add(fastslot2);

        fastslot3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        fastslot3.setForeground(new java.awt.Color(0, 230, 118));
        fastslot3.setBorder(null);
        fastslot3.setBorderPainted(false);
        fastslot3.setContentAreaFilled(false);
        fastslot3.setFocusPainted(false);
        fastslot3.setFocusable(false);
        fastpanel.add(fastslot3);

        ControlPanel.add(fastpanel);
        fastpanel.setBounds(40, 33, 90, 442);

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridLayout(5, 2, 30, 40));

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setBorder(null);
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton2.setBorder(null);
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setFocusPainted(false);
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton3.setBorder(null);
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setFocusPainted(false);
        jButton3.setFocusable(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton4.setBorder(null);
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setFocusPainted(false);
        jButton4.setFocusable(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton5.setBorder(null);
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.setFocusPainted(false);
        jButton5.setFocusable(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5);

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton6.setBorder(null);
        jButton6.setBorderPainted(false);
        jButton6.setContentAreaFilled(false);
        jButton6.setFocusPainted(false);
        jButton6.setFocusable(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6);

        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton7.setBorder(null);
        jButton7.setBorderPainted(false);
        jButton7.setContentAreaFilled(false);
        jButton7.setFocusPainted(false);
        jButton7.setFocusable(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton7);

        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton8.setBorder(null);
        jButton8.setBorderPainted(false);
        jButton8.setContentAreaFilled(false);
        jButton8.setFocusPainted(false);
        jButton8.setFocusable(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton8);

        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton9.setBorder(null);
        jButton9.setBorderPainted(false);
        jButton9.setContentAreaFilled(false);
        jButton9.setFocusPainted(false);
        jButton9.setFocusable(false);
        jPanel1.add(jButton9);

        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton10.setBorder(null);
        jButton10.setBorderPainted(false);
        jButton10.setContentAreaFilled(false);
        jButton10.setFocusPainted(false);
        jButton10.setFocusable(false);
        jPanel1.add(jButton10);

        ControlPanel.add(jPanel1);
        jPanel1.setBounds(613, 135, 312, 462);

        normalcharge1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        normalcharge1.setForeground(new java.awt.Color(41, 182, 246));
        normalcharge1.setBorder(null);
        normalcharge1.setBorderPainted(false);
        normalcharge1.setContentAreaFilled(false);
        normalcharge1.setFocusPainted(false);
        normalcharge1.setFocusable(false);
        ControlPanel.add(normalcharge1);
        normalcharge1.setBounds(40, 505, 90, 150);

        nxtfastbtn.setBorder(null);
        nxtfastbtn.setBorderPainted(false);
        nxtfastbtn.setContentAreaFilled(false);
        ControlPanel.add(nxtfastbtn);
        nxtfastbtn.setBounds(330, 540, 140, 60);

        nxtnormalbtn.setBorder(null);
        nxtnormalbtn.setBorderPainted(false);
        nxtnormalbtn.setContentAreaFilled(false);
        ControlPanel.add(nxtnormalbtn);
        nxtnormalbtn.setBounds(330, 450, 140, 70);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridLayout(4, 1, 10, 25));

        normalcharge2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        normalcharge2.setForeground(new java.awt.Color(41, 182, 246));
        normalcharge2.setBorder(null);
        normalcharge2.setBorderPainted(false);
        normalcharge2.setContentAreaFilled(false);
        normalcharge2.setFocusPainted(false);
        normalcharge2.setFocusable(false);
        jPanel2.add(normalcharge2);

        normalcharge3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        normalcharge3.setForeground(new java.awt.Color(41, 182, 246));
        normalcharge3.setBorder(null);
        normalcharge3.setBorderPainted(false);
        normalcharge3.setContentAreaFilled(false);
        normalcharge3.setFocusPainted(false);
        normalcharge3.setFocusable(false);
        jPanel2.add(normalcharge3);

        normalcharge4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        normalcharge4.setForeground(new java.awt.Color(41, 182, 246));
        normalcharge4.setBorder(null);
        normalcharge4.setBorderPainted(false);
        normalcharge4.setContentAreaFilled(false);
        normalcharge4.setFocusPainted(false);
        normalcharge4.setFocusable(false);
        jPanel2.add(normalcharge4);

        normalcharge5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        normalcharge5.setForeground(new java.awt.Color(41, 182, 246));
        normalcharge5.setBorder(null);
        normalcharge5.setBorderPainted(false);
        normalcharge5.setContentAreaFilled(false);
        normalcharge5.setFocusPainted(false);
        normalcharge5.setFocusable(false);
        jPanel2.add(normalcharge5);

        ControlPanel.add(jPanel2);
        jPanel2.setBounds(203, 20, 90, 630);

        queIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/ControlQe.png"))); // NOI18N
        ControlPanel.add(queIcon);
        queIcon.setBounds(0, -70, 1010, 750);

        jTabbedPane1.addTab("Queue Control", ControlPanel);

        add(jTabbedPane1);
        jTabbedPane1.setBounds(0, 50, 1020, 700);

        datetimeStaff.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        datetimeStaff.setForeground(new java.awt.Color(255, 255, 255));
        datetimeStaff.setText("10:44 AM 17 August, Sunday");
        add(datetimeStaff);
        datetimeStaff.setBounds(820, 40, 170, 20);

        labelStaff.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelStaff.setForeground(new java.awt.Color(255, 255, 255));
        // Get the logged-in username from the Admin frame
        String username = getLoggedInUsername();
        labelStaff.setText(username + "!");
        add(labelStaff);
        labelStaff.setBounds(870, 10, 70, 30);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Hello,");
        add(jLabel3);
        jLabel3.setBounds(820, 10, 50, 30);

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

    // Grid button action listeners - placeholder methods for future functionality
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {}
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {}
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {}
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {}
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {}
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {}
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {}
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Baybutton;
    private javax.swing.JLabel Charging;
    private javax.swing.JPanel ControlPanel;
    private javax.swing.JLabel MainIcon;
    private javax.swing.JLabel Paid;
    private javax.swing.JLabel Waitings;
    private javax.swing.JButton businessbutton;
    private javax.swing.JLabel datetimeStaff;
    private javax.swing.JButton exitlogin;
    private javax.swing.JPanel fastpanel;
    private javax.swing.JButton fastslot1;
    private javax.swing.JButton fastslot2;
    private javax.swing.JButton fastslot3;
    private javax.swing.JButton historybutton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelStaff;
    private javax.swing.JButton normalcharge1;
    private javax.swing.JButton normalcharge2;
    private javax.swing.JButton normalcharge3;
    private javax.swing.JButton normalcharge4;
    private javax.swing.JButton normalcharge5;
    private javax.swing.JButton nxtfastbtn;
    private javax.swing.JButton nxtnormalbtn;
    private javax.swing.JPanel panelLists;
    private javax.swing.JLabel queIcon;
    private javax.swing.JTable queTab;
    private javax.swing.JButton staffbutton;
    // End of variables declaration//GEN-END:variables
    
    private String getLoggedInUsername() {
        try {
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (window instanceof cephra.Frame.Admin) {
                // Use reflection to get the loggedInUsername field
                java.lang.reflect.Field usernameField = window.getClass().getDeclaredField("loggedInUsername");
                usernameField.setAccessible(true);
                return (String) usernameField.get(window);
            }
        } catch (Exception e) {
            System.err.println("Error getting logged-in username: " + e.getMessage());
        }
        return "Admin"; // Fallback
    }
}
