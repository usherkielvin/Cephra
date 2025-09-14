package cephra.Admin;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import cephra.Frame.Monitor;

public class Queue extends javax.swing.JPanel {
    private static Monitor monitorInstance;
    private JButton[] gridButtons;
    private int buttonCount = 0;
    
    // Static notification instance to allow updates
    private static cephra.Phone.UnifiedNotification staticNotification = null;

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
        // Delay setup to ensure table model is ready
        SwingUtilities.invokeLater(() -> {
            setupActionColumn();
            setupPaymentColumn();
        });
        jPanel1.setOpaque(false);
        
        // Create a single instance of Monitor
        if (monitorInstance == null) {
           // monitorInstance = new cephra.Frame.Monitor();
           // monitorInstance.setVisible(true);
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

        // Listen to table changes to keep counters in sync and auto-remove paid rows
        queTab.getModel().addTableModelListener(e -> {
            updateStatusCounters();
            // Auto-remove rows when payment status becomes "Paid"
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                checkAndRemovePaidRows();
            }
        });
        updateStatusCounters();
        
        // Initialize grid displays with maintenance status from BayManagement
        initializeGridDisplays();
        
        // Initialize waiting grid from database
        initializeWaitingGridFromDatabase();
        
        // Register this Queue instance with BayManagement for real-time updates
        cephra.Admin.BayManagement.registerQueueInstance(this);

        // Add hover effects to navigation buttons
        ButtonHoverEffect.addHoverEffect(Baybutton);
        ButtonHoverEffect.addHoverEffect(businessbutton);
        ButtonHoverEffect.addHoverEffect(exitlogin);
        ButtonHoverEffect.addHoverEffect(staffbutton);
        ButtonHoverEffect.addHoverEffect(historybutton);
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

        // Renderer: show Proceed only on rows that have a real ticket AND are not paid
        queTab.getColumnModel().getColumn(actionCol).setCellRenderer(new TableCellRenderer() {
            private final JButton button = createFlatButton();
            private final JLabel empty = new JLabel("");

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Object ticketVal = table.getValueAt(row, getColumnIndex("Ticket"));
                boolean hasTicket = ticketVal != null && String.valueOf(ticketVal).trim().length() > 0;
                
                // Also check payment status - don't show proceed button for paid tickets
                int paymentCol = getColumnIndex("Payment");
                boolean isPaid = false;
                if (paymentCol >= 0 && hasTicket) {
                    Object paymentVal = table.getValueAt(row, paymentCol);
                    String paymentStatus = paymentVal == null ? "" : String.valueOf(paymentVal).trim();
                    isPaid = "Paid".equalsIgnoreCase(paymentStatus);
                }
                
                // Only show proceed button if there's a ticket AND it's not paid
                if (hasTicket && !isPaid) {
                    button.setText("Proceed");
                    button.setForeground(new java.awt.Color(255, 255, 255)); // Ensure white text color
                    button.setBackground(new java.awt.Color(0, 120, 215)); // Ensure visible background
                    button.setOpaque(true); // Ensure button is opaque
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
        public Component getTableCellEditorComponent(javax.swing.JTable table, Object value, boolean isSelected, int row, int column) {
            editingRow = row;
            Object ticketVal = queTab.getValueAt(row, 0);
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
            stopCellEditing();
            if (editingRow < 0) return;
            String ticket = String.valueOf(queTab.getValueAt(editingRow, 0));
            int paymentCol = getColumnIndex("Payment");
            String status = String.valueOf(queTab.getValueAt(editingRow, statusColumnIndex));

            // Crash-recovery: if payment not Paid, force/reset to Pending so flow can continue
            try {
                String payVal = paymentCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, paymentCol)) : "";
                if ("Complete".equalsIgnoreCase(status)
                    && ticket != null && !ticket.trim().isEmpty()
                    && (payVal == null || payVal.trim().isEmpty()
                        || "Pending".equalsIgnoreCase(payVal)
                        || "TopupRequired".equalsIgnoreCase(payVal))) {
                    ensurePaymentPending(ticket);
                    if (paymentCol >= 0) {
                        queTab.setValueAt("Pending", editingRow, paymentCol);
                    }
                }
            } catch (Exception ignore) {}

            int customerCol = Math.min(1, queTab.getColumnCount() - 1);
            String customer = String.valueOf(queTab.getValueAt(editingRow, customerCol));

            // Preserve existing flow below
            try {
                if ("Pending".equalsIgnoreCase(status)) {
                    // Promote Pending -> Waiting on first proceed
                    int statusCol = getColumnIndex("Status");
                    if (statusCol >= 0) {
                        queTab.setValueAt("Waiting", editingRow, statusCol);
                        try {
                            cephra.CephraDB.updateQueueTicketStatus(ticket, "Waiting");
                            // Also place into waiting grid
                            int svcCol = getColumnIndex("Service");
                            String serviceName = svcCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, svcCol)) : "";
                            String customerName = String.valueOf(queTab.getValueAt(editingRow, Math.min(1, queTab.getColumnCount()-1)));
                            int battery = cephra.CephraDB.getUserBatteryLevel(customerName);
                            cephra.Admin.BayManagement.addTicketToWaitingGrid(ticket, customerName, serviceName, battery);
                            // Refresh waiting grid view
                            initializeWaitingGridFromDatabase();
                            
                            // Trigger notification for the customer
                            triggerNotificationForCustomer(customerName, "TICKET_WAITING", ticket, null);
                        } catch (Throwable ignore) {}
                    }
                    // Do not open PayPop yet when still Waiting
                    updateStatusCounters();
                    return;
                }
                if ("Waiting".equalsIgnoreCase(status)) {
                    // Try to assign to a charging bay; if assigned, status becomes Charging
                    boolean assigned = false;
                    try { assigned = tryAssignToAnyAvailableBay(ticket); } catch (Throwable t) { System.err.println("Queue: assign attempt failed: " + t.getMessage()); }
                    if (assigned) {
                        setTableStatusToChargingByTicket(ticket);
                    }
                    updateStatusCounters();
                    return;
                }
                if ("Charging".equalsIgnoreCase(status)) {
                    boolean canShowPayPop = false;
                    try {
                        canShowPayPop = cephra.Phone.PayPop.canShowPayPop(ticket, customer);
                        System.out.println("Queue: Checking if PayPop can be shown for ticket " + ticket + " and customer " + customer + ": " + canShowPayPop);
                    } catch (Throwable t) {
                        System.err.println("Error checking PayPop availability: " + t.getMessage());
                        canShowPayPop = false;
                    }
                    if (!canShowPayPop) {
                        System.out.println("Queue: Cannot show PayPop for ticket " + ticket + " - setting payment to Pending");
                        try { ensurePaymentPending(ticket); } catch (Exception ex) { System.err.println("Failed to set pending: " + ex.getMessage()); }
                        return;
                    }
                    System.out.println("Queue: PayPop can be shown - completing charging for ticket " + ticket);
                    queTab.setValueAt("Complete", editingRow, statusColumnIndex);
                    
                    // Trigger FULL_CHARGE notification for the customer
                    triggerNotificationForCustomer(customer, "FULL_CHARGE", ticket, null);
                }
                if ("Complete".equalsIgnoreCase(status) || "Charging".equalsIgnoreCase(status)) {
                    try {
                        boolean success = cephra.Phone.PayPop.showPayPop(ticket, customer);
                        if (!success) { System.err.println("Queue: PayPop failed to open for ticket " + ticket); }
                    } catch (Throwable t) {
                        System.err.println("Error showing PayPop: " + t.getMessage());
                    }
                } else if ("Complete".equalsIgnoreCase(status)) {
                
                    // If paid, move to History and remove from Queue
                    String payment = paymentCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, paymentCol)) : "";
                    System.out.println("Queue: Processing Complete ticket " + ticket + " with payment status: " + payment);
                    
                    if ("Paid".equalsIgnoreCase(payment)) {
                        // Check if payment has already been processed to prevent duplicates
                        boolean alreadyProcessed = cephra.CephraDB.isPaymentAlreadyProcessed(ticket);
                        System.out.println("Queue: Checking if payment already processed for ticket " + ticket + ": " + alreadyProcessed);
                        
                        if (alreadyProcessed) {
                            System.out.println("Queue: Payment already processed for ticket " + ticket + ", removing from queue directly");
                            // Stop cell editing immediately
                            stopCellEditing();
                            
                            // Use QueueBridge.removeTicket() for consistent removal
                            try {
                                cephra.Admin.QueueBridge.removeTicket(ticket);
                                System.out.println("Queue: Successfully removed ticket " + ticket + " via QueueBridge");
                                
                                // Hard refresh entire panel after payment
                                hardRefreshTable();
                            } catch (Throwable t) {
                                System.err.println("Error removing ticket via QueueBridge: " + t.getMessage());
                            }
                            return; // Exit early since payment was already processed
                        }
                        
                        System.out.println("Queue: Payment not yet processed, proceeding with payment transaction for ticket " + ticket);
                        
                        final String customerName = customerCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, customerCol)) : "";
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
                                int userBatteryLevel = cephra.CephraDB.getUserBatteryLevel(customerName);
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
                                ticket, customerName, serviceName, 
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
                                
                                // Stop cell editing immediately
                                stopCellEditing();
                                
                                // Use QueueBridge.removeTicket() for consistent removal
                                try {
                                    cephra.Admin.QueueBridge.removeTicket(ticket);
                                    System.out.println("Queue: Successfully removed ticket " + ticket + " via QueueBridge");
                                    
                                    // Refresh entire panel after payment
                                    refreshEntirePanel();
                                } catch (Throwable t) {
                                    System.err.println("Error removing ticket via QueueBridge: " + t.getMessage());
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
            } catch (Throwable t) {
                System.err.println("Queue: Error in proceed action: " + t.getMessage());
            }
            // Keep counters in sync after any action
            updateStatusCounters();
        }
    }

    private void ensurePaymentPending(String ticket) {
        try {
            int paymentCol = getColumnIndex("Payment");
            int ticketCol = getColumnIndex("Ticket");
            if (paymentCol < 0 || ticketCol < 0) return;
            for (int row = 0; row < queTab.getRowCount(); row++) {
                Object v = queTab.getValueAt(row, ticketCol);
                if (v != null && ticket.equals(String.valueOf(v).trim())) {
                    queTab.setValueAt("Pending", row, paymentCol);
                    break;
                }
            }
        } catch (Throwable t) {
            System.err.println("Queue: ensurePaymentPending failed for ticket " + ticket + ": " + t.getMessage());
        }
    }

    private static JButton createFlatButton() {
        JButton b = new JButton();
        b.setBorderPainted(false);
        b.setContentAreaFilled(true); // Make sure content area is filled
        b.setFocusPainted(false);
        b.setOpaque(true); // Make button opaque so it's visible
        b.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b.setForeground(new java.awt.Color(255, 255, 255)); // Set text color to white
        b.setBackground(new java.awt.Color(0, 120, 215)); // Set a visible background color
        b.setText("Proceed");
        b.setPreferredSize(new java.awt.Dimension(80, 25)); // Set preferred size
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
    
    /**
     * Checks for rows with "Paid" payment status and automatically removes them
     */
    private void checkAndRemovePaidRows() {
        // Add a small delay to ensure payment processing is complete
        SwingUtilities.invokeLater(() -> {
            // Use a timer to delay the check slightly
            javax.swing.Timer timer = new javax.swing.Timer(500, _ -> {
                int paymentCol = getColumnIndex("Payment");
                int ticketCol = getColumnIndex("Ticket");
                
                if (paymentCol < 0 || ticketCol < 0) return;
                
                // Check all rows for "Paid" status
                for (int i = queTab.getRowCount() - 1; i >= 0; i--) {
                    Object paymentVal = queTab.getValueAt(i, paymentCol);
                    String paymentStatus = paymentVal == null ? "" : String.valueOf(paymentVal).trim();
                    
                    if ("Paid".equalsIgnoreCase(paymentStatus)) {
                        // Get ticket information before removing
                        Object ticketVal = queTab.getValueAt(i, ticketCol);
                        String ticket = ticketVal == null ? "" : String.valueOf(ticketVal).trim();
                        
                        System.out.println("Queue: Auto-removing row with Paid status for ticket: " + ticket);
                        
                        // Use QueueBridge.removeTicket() for consistent removal
                        if (!ticket.isEmpty()) {
                            try {
                                cephra.Admin.QueueBridge.removeTicket(ticket);
                                System.out.println("Queue: Successfully auto-removed ticket " + ticket + " via QueueBridge");
                                
                                // Hard refresh entire panel after payment
                                hardRefreshTable();
                                
                            } catch (Throwable t) {
                                System.err.println("Error auto-removing ticket via QueueBridge: " + t.getMessage());
                            }
                        }
                    }
                }
            });
            timer.setRepeats(false); // Only run once
            timer.start();
        });
    }
    
    
    
    /**
     * Refreshes the entire queue panel when a ticket is paid
     */
    public void refreshEntirePanel() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Queue: Refreshing entire panel after payment");
            
            // Force complete panel refresh
            this.repaint();
            this.revalidate();
            
            // Force table refresh
            queTab.repaint();
            queTab.revalidate();
            
            // Force scroll pane refresh
            if (jScrollPane1 != null) {
                jScrollPane1.repaint();
                jScrollPane1.revalidate();
            }
            
            // Force parent container refresh
            if (this.getParent() != null) {
                this.getParent().repaint();
                this.getParent().revalidate();
            }
            
            // Update status counters
            updateStatusCounters();
            
            System.out.println("Queue: Complete panel refresh completed");
        });
    }
    
    /**
     * Performs a hard refresh by completely reloading the table from database
     */
    public void hardRefreshTable() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Queue: Performing HARD refresh - reloading table from database");
            
            try {
                // Stop any active cell editing safely
                if (queTab.isEditing() && queTab.getRowCount() > 0) {
                    try {
                        queTab.getCellEditor().stopCellEditing();
                    } catch (Exception e) {
                        // Ignore cell editing errors during refresh
                        System.out.println("Queue: Ignored cell editing error during refresh: " + e.getMessage());
                    }
                }
                
                // Clear the current table completely
                ((DefaultTableModel) queTab.getModel()).setRowCount(0);
                
                // Force QueueBridge to reload from database
                cephra.Admin.QueueBridge.reloadFromDatabase();
                
                // Force complete UI refresh
                queTab.clearSelection();
                queTab.repaint();
                queTab.revalidate();
                
                // Force table to refresh all cell renderers
                queTab.updateUI();
                
                // Force complete renderer refresh by temporarily changing table model
                DefaultTableModel currentModel = (DefaultTableModel) queTab.getModel();
                queTab.setModel(new DefaultTableModel());
                queTab.setModel(currentModel);
                
                // Re-setup action column to ensure renderers are fresh
                setupActionColumn();
                
                // Force scroll pane refresh
                jScrollPane1.repaint();
                jScrollPane1.revalidate();
                
                // Force panel refresh
                this.repaint();
                this.revalidate();
                
                // Update status counters
                updateStatusCounters();
                
                System.out.println("Queue: HARD refresh completed - table reloaded from database");
                
            } catch (Exception e) {
                System.err.println("Queue: Error during hard refresh: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }


    
    private void setupNextButtons() {
        nxtnormalbtn.addActionListener(_ -> nextNormalTicket());
        nxtfastbtn.addActionListener(_ -> nextFastTicket());
    }
    
    private void nextNormalTicket() {
        String ticket = findNextTicketByType("NCH");
        if (ticket != null) {
            // Assign via BayManagement so grids update correctly
            boolean assigned = assignToNormalSlot(ticket);
            if (assigned) {
                setTableStatusToChargingByTicket(ticket);
                removeTicketFromGrid(ticket);
                
                // Get customer name and bay number for notification
                String customerName = cephra.CephraDB.getCustomerByTicket(ticket);
                String bayNumber = cephra.Admin.BayManagement.getBayNumberByTicket(ticket);
                if (customerName != null) {
                    triggerNotificationForCustomer(customerName, "MY_TURN", ticket, bayNumber);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Normal Charge Bays 1-5 are full!\nTicket " + ticket + " remains in waiting queue.",
                    "Normal Charge Bays Full",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
        updateStatusCounters();
    }
    
    private void nextFastTicket() {
        String ticket = findNextTicketByType("FCH");
        if (ticket != null) {
            // Assign via BayManagement so grids update correctly
            boolean assigned = assignToFastSlot(ticket);
            if (assigned) {
                setTableStatusToChargingByTicket(ticket);
                removeTicketFromGrid(ticket);
                
                // Get customer name and bay number for notification
                String customerName = cephra.CephraDB.getCustomerByTicket(ticket);
                String bayNumber = cephra.Admin.BayManagement.getBayNumberByTicket(ticket);
                if (customerName != null) {
                    triggerNotificationForCustomer(customerName, "MY_TURN", ticket, bayNumber);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Fast Charge Bays 1-3 are full!\nTicket " + ticket + " remains in waiting queue.",
                    "Fast Charge Bays Full",
                    JOptionPane.INFORMATION_MESSAGE);
            }
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
                // Remove from database waiting grid
                try (java.sql.Connection conn = cephra.db.DatabaseConnection.getConnection();
                     java.sql.PreparedStatement pstmt = conn.prepareStatement(
                         "UPDATE waiting_grid SET ticket_id = NULL, username = NULL, service_type = NULL, initial_battery_level = NULL, position_in_queue = NULL WHERE ticket_id = ?")) {
                    pstmt.setString(1, ticket);
                    pstmt.executeUpdate();
                    System.out.println("Queue: Removed ticket " + ticket + " from waiting grid database");
                } catch (Exception e) {
                    System.err.println("Error removing ticket from waiting grid database: " + e.getMessage());
                }
                
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
            // Get grid display data from BayManagement (includes maintenance status)
            String[] fastTickets = cephra.Admin.BayManagement.getFastChargingGridTexts();
            java.awt.Color[] fastColors = cephra.Admin.BayManagement.getFastChargingGridColors();
            
            // Update Monitor with BayManagement data
             monitorInstance.updateFastGrid(fastTickets);
            
            // Update local buttons with BayManagement data
            updateLocalFastButtons(fastTickets, fastColors);
        }
    }
    
    /**
     * Updates local fast charging buttons with BayManagement data
     */
    private void updateLocalFastButtons(String[] texts, java.awt.Color[] colors) {
        JButton[] fastButtons = {fastslot1, fastslot2, fastslot3};
        
        for (int i = 0; i < fastButtons.length && i < texts.length; i++) {
            if (texts[i] != null && !texts[i].isEmpty()) {
                // Bay has a ticket or is offline
                fastButtons[i].setText(texts[i]);
                fastButtons[i].setVisible(true);
                if (colors != null && i < colors.length) {
                    fastButtons[i].setForeground(colors[i]);
                }
            } else {
                // Bay is available but empty - show empty button
                fastButtons[i].setText("");
                fastButtons[i].setVisible(true);
                fastButtons[i].setForeground(new java.awt.Color(0, 230, 118)); // Green for available
            }
         }
     }
     
     private void updateMonitorNormalGrid() {
         if (monitorInstance != null) {
            // Get grid display data from BayManagement (includes maintenance status)
            String[] normalTickets = cephra.Admin.BayManagement.getNormalChargingGridTexts();
            java.awt.Color[] normalColors = cephra.Admin.BayManagement.getNormalChargingGridColors();
            
            // Update Monitor with BayManagement data
             monitorInstance.updateNormalGrid(normalTickets);
            
            // Update local buttons with BayManagement data
            updateLocalNormalButtons(normalTickets, normalColors);
        }
    }
    
    /**
     * Updates local normal charging buttons with BayManagement data
     */
    private void updateLocalNormalButtons(String[] texts, java.awt.Color[] colors) {
        JButton[] normalButtons = {normalcharge1, normalcharge2, normalcharge3, normalcharge4, normalcharge5};
        
        for (int i = 0; i < normalButtons.length && i < texts.length; i++) {
            if (texts[i] != null && !texts[i].isEmpty()) {
                // Bay has a ticket or is offline
                normalButtons[i].setText(texts[i]);
                normalButtons[i].setVisible(true);
                if (colors != null && i < colors.length) {
                    normalButtons[i].setForeground(colors[i]);
                }
            } else {
                // Bay is available but empty - show empty button
                normalButtons[i].setText("");
                normalButtons[i].setVisible(true);
                normalButtons[i].setForeground(new java.awt.Color(0, 230, 118)); // Green for available
            }
        }
    }

    private boolean assignToNormalSlot(String ticket) {
        System.out.println("Queue: assignToNormalSlot called for ticket " + ticket);
        // Check if there's capacity for normal charging
        if (!cephra.Admin.BayManagement.hasChargingCapacity(false)) {
            System.out.println("Queue: No available normal charging bays (all offline or occupied). Cannot assign ticket " + ticket);
            return false;
        }
        
        // Find next available normal charging bay (skips offline bays)
        int bayNumber = cephra.Admin.BayManagement.findNextAvailableBay(false); // false = normal charging
        System.out.println("Queue: Found available normal charging bay: " + bayNumber + " for ticket " + ticket);
        
        if (bayNumber > 0) {
            // Move ticket from waiting grid to charging bay
            if (cephra.Admin.BayManagement.moveTicketFromWaitingToCharging(ticket, bayNumber)) {
                // Update local display with fresh data from database
                updateLocalNormalButtons(cephra.Admin.BayManagement.getNormalChargingGridTexts(), 
                                       cephra.Admin.BayManagement.getNormalChargingGridColors());
                updateMonitorNormalGrid();
                System.out.println("Queue: Ticket " + ticket + " assigned to Bay-" + bayNumber + " and showing on grid");
                return true;
            }
        } else {
            System.out.println("Queue: No available normal charging bays for ticket " + ticket);
        }
        return false;
    }

    private boolean assignToFastSlot(String ticket) {
        System.out.println("Queue: assignToFastSlot called for ticket " + ticket);
        // Check if there's capacity for fast charging
        if (!cephra.Admin.BayManagement.hasChargingCapacity(true)) {
            System.out.println("Queue: No available fast charging bays (all offline or occupied). Cannot assign ticket " + ticket);
            return false;
        }
        
        // Find next available fast charging bay (skips offline bays)
        int bayNumber = cephra.Admin.BayManagement.findNextAvailableBay(true); // true = fast charging
        System.out.println("Queue: Found available fast charging bay: " + bayNumber + " for ticket " + ticket);
        
        if (bayNumber > 0) {
            // Move ticket from waiting grid to charging bay
            if (cephra.Admin.BayManagement.moveTicketFromWaitingToCharging(ticket, bayNumber)) {
                // Update local display with fresh data from database
                updateLocalFastButtons(cephra.Admin.BayManagement.getFastChargingGridTexts(), 
                                     cephra.Admin.BayManagement.getFastChargingGridColors());
                updateMonitorFastGrid();
                System.out.println("Queue: Ticket " + ticket + " assigned to Bay-" + bayNumber + " and showing on grid");
                return true;
            }
        } else {
            System.out.println("Queue: No available fast charging bays for ticket " + ticket);
        }
        return false;
    }

    private boolean tryAssignToAnyAvailableBay(String ticket) {
        String service = null;
        try {
            service = cephra.Admin.QueueBridge.getTicketService(ticket);
        } catch (Throwable ignore) {}

        boolean prefersFast = false;
        if (service != null) {
            prefersFast = service.toLowerCase().contains("fast");
        } else if (ticket != null) {
            prefersFast = ticket.toUpperCase().startsWith("FCH");
        }

        if (prefersFast) {
            if (assignToFastSlot(ticket)) return true;
            return assignToNormalSlot(ticket);
        } else {
            if (assignToNormalSlot(ticket)) return true;
            return assignToFastSlot(ticket);
        }
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
                
                // Get customer name and bay number for MY_TURN notification
                String customerName = cephra.CephraDB.getCustomerByTicket(ticket);
                String bayNumber = cephra.Admin.BayManagement.getBayNumberByTicket(ticket);
                if (customerName != null) {
                    triggerNotificationForCustomer(customerName, "MY_TURN", ticket, bayNumber);
                }
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
                
                // Show button for all payment statuses
                if ("Pending".equalsIgnoreCase(v)) {
                    btn.setText("Pending");
                    btn.setForeground(new java.awt.Color(255, 255, 255)); // White text color
                    btn.setBackground(new java.awt.Color(255, 140, 0)); // Orange background for pending
                    btn.setOpaque(true); // Ensure button is opaque
                    return btn;
                } else if ("Paid".equalsIgnoreCase(v)) {
                    btn.setText("Paid");
                    btn.setBackground(new java.awt.Color(34, 139, 34)); // Green background for paid
                    btn.setForeground(new java.awt.Color(255, 255, 255)); // White text color
                    btn.setOpaque(true); // Ensure button is opaque
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
            
            // Show button for all payment statuses
            if ("Pending".equalsIgnoreCase(v)) {
                btn.setText("Pending");
                btn.setBackground(new java.awt.Color(255, 140, 0)); // Orange background for pending
                btn.setForeground(new java.awt.Color(255, 255, 255)); // White text color
                btn.setOpaque(true); // Ensure button is opaque
                return btn;
            } else if ("Paid".equalsIgnoreCase(v)) {
                btn.setText("Paid");
                btn.setBackground(new java.awt.Color(34, 139, 34)); // Green background for paid
                btn.setForeground(new java.awt.Color(255, 255, 255)); // White text color
                btn.setOpaque(true); // Ensure button is opaque
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
                // Check if charging is complete before allowing payment
                int statusCol = getColumnIndex("Status");
                String status = statusCol >= 0 ? String.valueOf(queTab.getValueAt(editingRow, statusCol)) : "";
                
                if (!"Complete".equalsIgnoreCase(status)) {
                    JOptionPane.showMessageDialog(
                        Queue.this,
                        "Cannot mark as paid until charging is complete.\nCurrent status: " + status,
                        "Payment Not Ready",
                        JOptionPane.WARNING_MESSAGE
                    );
                    stopCellEditing();
                    return;
                }
                
                // Check if payment method is Cash
                int ticketCol = getColumnIndex("Ticket");
                if (ticketCol >= 0) {
                    Object v = queTab.getValueAt(editingRow, ticketCol);
                    String ticket = v == null ? "" : String.valueOf(v).trim();
                    if (!ticket.isEmpty()) {
                        String paymentMethod = cephra.CephraDB.getQueueTicketPaymentMethod(ticket);
                        if (!"Cash".equalsIgnoreCase(paymentMethod)) {
                            JOptionPane.showMessageDialog(
                                Queue.this,
                                "This ticket is not marked for cash payment.\nPayment method: " + paymentMethod + "\nOnly cash payments can be marked as paid by admin.",
                                "Payment Method Mismatch",
                                JOptionPane.WARNING_MESSAGE
                            );
                            stopCellEditing();
                            return;
                        }
                    }
                }
                
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
                    if (ticketCol >= 0) {
                        Object v = queTab.getValueAt(editingRow, ticketCol);
                        String ticket = v == null ? "" : String.valueOf(v).trim();
                        if (!ticket.isEmpty()) {
                            // Check if payment already processed to prevent duplicates
                            boolean alreadyProcessed = cephra.CephraDB.isPaymentAlreadyProcessed(ticket);
                            if (alreadyProcessed) {
                                System.out.println("Queue: Payment already processed for ticket " + ticket + ", removing from queue directly");
                                // Stop cell editing immediately
                                stopCellEditing();
                                
                                // Use QueueBridge.removeTicket() for consistent removal
                                try {
                                    cephra.Admin.QueueBridge.removeTicket(ticket);
                                    System.out.println("Queue: Successfully removed ticket " + ticket + " via QueueBridge");
                                    
                                    // Refresh entire panel after payment
                                    refreshEntirePanel();
                                } catch (Throwable t) {
                                    System.err.println("Error removing ticket via QueueBridge: " + t.getMessage());
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
                                
                                // Stop cell editing immediately
                                stopCellEditing();
                                
                                // Use QueueBridge.removeTicket() for consistent removal
                                try {
                                    cephra.Admin.QueueBridge.removeTicket(ticket);
                                    System.out.println("Queue: Successfully removed ticket " + ticket + " via QueueBridge");
                                    
                                    // Refresh entire panel after payment
                                    refreshEntirePanel();
                                } catch (Throwable t) {
                                    System.err.println("Error removing ticket via QueueBridge: " + t.getMessage());
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
        jLabel1 = new javax.swing.JLabel();
        MainIcon = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1000, 750));
        setLayout(null);

        Baybutton.setForeground(new java.awt.Color(255, 255, 255));
        Baybutton.setText("BAYS");
        Baybutton.setBorder(null);
        Baybutton.setBorderPainted(false);
        Baybutton.setContentAreaFilled(false);
        Baybutton.setFocusPainted(false);
        Baybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BaybuttonActionPerformed(evt);
            }
        });
        add(Baybutton);
        Baybutton.setBounds(385, 26, 33, 15);

        businessbutton.setForeground(new java.awt.Color(255, 255, 255));
        businessbutton.setText("BUSINESS OVERVIEW");
        businessbutton.setBorder(null);
        businessbutton.setBorderPainted(false);
        businessbutton.setContentAreaFilled(false);
        businessbutton.setFocusPainted(false);
        businessbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                businessbuttonActionPerformed(evt);
            }
        });
        add(businessbutton);
        businessbutton.setBounds(617, 26, 136, 15);

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

        staffbutton.setForeground(new java.awt.Color(255, 255, 255));
        staffbutton.setText("STAFF RECORDS");
        staffbutton.setBorder(null);
        staffbutton.setBorderPainted(false);
        staffbutton.setContentAreaFilled(false);
        staffbutton.setFocusPainted(false);
        staffbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffbuttonActionPerformed(evt);
            }
        });
        add(staffbutton);
        staffbutton.setBounds(505, 26, 96, 15);

        historybutton.setForeground(new java.awt.Color(255, 255, 255));
        historybutton.setText("HISTORY");
        historybutton.setBorder(null);
        historybutton.setBorderPainted(false);
        historybutton.setContentAreaFilled(false);
        historybutton.setFocusPainted(false);
        historybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historybuttonActionPerformed(evt);
            }
        });
        add(historybutton);
        historybutton.setBounds(433, 26, 57, 15);

        jTabbedPane1.setBackground(new java.awt.Color(4, 38, 55));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        panelLists.setLayout(null);

        Paid.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        Paid.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Paid.setText("5");
        panelLists.add(Paid);
        Paid.setBounds(40, 520, 120, 70);

        Waitings.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        Waitings.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Waitings.setText("4");
        panelLists.add(Waitings);
        Waitings.setBounds(40, 140, 120, 70);

        Charging.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        Charging.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Charging.setText("4");
        panelLists.add(Charging);
        Charging.setBounds(40, 330, 120, 70);

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
        queTab.setShowHorizontalLines(true);
        queTab.getTableHeader().setResizingAllowed(false);
        queTab.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(queTab);

        panelLists.add(jScrollPane1);
        jScrollPane1.setBounds(210, 55, 770, 597);

        jLabel2.setBackground(new java.awt.Color(4, 38, 55));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/QUEUEtable.png"))); // NOI18N
        panelLists.add(jLabel2);
        jLabel2.setBounds(-10, -90, 1000, 750);

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
        fastpanel.setBounds(57, 33, 90, 442);

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
        jPanel1.setBounds(630, 135, 312, 462);

        normalcharge1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        normalcharge1.setForeground(new java.awt.Color(41, 182, 246));
        normalcharge1.setBorder(null);
        normalcharge1.setBorderPainted(false);
        normalcharge1.setContentAreaFilled(false);
        normalcharge1.setFocusPainted(false);
        normalcharge1.setFocusable(false);
        ControlPanel.add(normalcharge1);
        normalcharge1.setBounds(57, 505, 90, 150);

        nxtfastbtn.setBorder(null);
        nxtfastbtn.setBorderPainted(false);
        nxtfastbtn.setContentAreaFilled(false);
        nxtfastbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nxtfastbtnActionPerformed(evt);
            }
        });
        ControlPanel.add(nxtfastbtn);
        nxtfastbtn.setBounds(350, 530, 140, 60);

        nxtnormalbtn.setBorder(null);
        nxtnormalbtn.setBorderPainted(false);
        nxtnormalbtn.setContentAreaFilled(false);
        nxtnormalbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nxtnormalbtnActionPerformed(evt);
            }
        });
        ControlPanel.add(nxtnormalbtn);
        nxtnormalbtn.setBounds(350, 450, 140, 70);

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
        jPanel2.setBounds(222, 20, 90, 630);

        queIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/ControlQe.png"))); // NOI18N
        ControlPanel.add(queIcon);
        queIcon.setBounds(0, -70, 1010, 750);

        jTabbedPane1.addTab("Queue Control", ControlPanel);

        add(jTabbedPane1);
        jTabbedPane1.setBounds(0, 50, 1020, 700);

        datetimeStaff.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        datetimeStaff.setForeground(new java.awt.Color(255, 255, 255));
        datetimeStaff.setText("10:44 AM 17 August, Sunday");
        add(datetimeStaff);
        datetimeStaff.setBounds(820, 40, 170, 20);

        labelStaff.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelStaff.setForeground(new java.awt.Color(255, 255, 255));
        labelStaff.setText("Admin!");
        add(labelStaff);
        labelStaff.setBounds(870, 10, 70, 30);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Hello,");
        add(jLabel3);
        jLabel3.setBounds(820, 10, 50, 30);

        jLabel1.setForeground(new java.awt.Color(4, 167, 182));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUEUE LIST");
        add(jLabel1);
        jLabel1.setBounds(289, 26, 80, 15);

        MainIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Tab Pane.png"))); // NOI18N
        add(MainIcon);
        MainIcon.setBounds(0, -10, 1000, 770);
    }// </editor-fold>//GEN-END:initComponents

    private void businessbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_businessbuttonActionPerformed
        Window w = SwingUtilities.getWindowAncestor(Queue.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Business_Overview());
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
            ((cephra.Frame.Admin) w).switchPanel(new BayManagement());
        }
    }//GEN-LAST:event_BaybuttonActionPerformed

    private void nxtnormalbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nxtnormalbtnActionPerformed
      
    }//GEN-LAST:event_nxtnormalbtnActionPerformed

    private void nxtfastbtnActionPerformed(java.awt.event.ActionEvent evt) {                                               
    }                                          

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
    private javax.swing.JLabel jLabel1;
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
    
    @SuppressWarnings("unused")
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
    
    /**
     * Initializes grid displays with offline status from BayManagement
     */
    private void initializeGridDisplays() {
        try {
            System.out.println("Queue: Initializing grid displays with database data...");
            
            // Force BayManagement to load data from database first
            cephra.Admin.BayManagement.ensureMaintenanceDisplay();
            
            // Initialize fast charging grid with BayManagement data
            String[] fastTexts = cephra.Admin.BayManagement.getFastChargingGridTexts();
            java.awt.Color[] fastColors = cephra.Admin.BayManagement.getFastChargingGridColors();
            
            System.out.println("Queue: Fast charging texts from database: " + java.util.Arrays.toString(fastTexts));
            System.out.println("Queue: Fast charging colors from database: " + java.util.Arrays.toString(fastColors));
            
            updateLocalFastButtons(fastTexts, fastColors);
            
            // Initialize normal charging grid with BayManagement data
            String[] normalTexts = cephra.Admin.BayManagement.getNormalChargingGridTexts();
            java.awt.Color[] normalColors = cephra.Admin.BayManagement.getNormalChargingGridColors();
            
            System.out.println("Queue: Normal charging texts from database: " + java.util.Arrays.toString(normalTexts));
            System.out.println("Queue: Normal charging colors from database: " + java.util.Arrays.toString(normalColors));
            
            updateLocalNormalButtons(normalTexts, normalColors);
            
            // Update Monitor displays
            if (monitorInstance != null) {
                monitorInstance.updateFastGrid(fastTexts);
                monitorInstance.updateNormalGrid(normalTexts);
                System.out.println("Queue: Monitor displays updated with database data");
            }
            
            System.out.println("Queue: Grid displays initialized with offline status from database");
            
        } catch (Exception e) {
            System.err.println("Error initializing Queue grid displays: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Public method to refresh all grid displays with latest database data
     * Called by BayManagement when tickets are moved to charging
     */
    public void refreshGridDisplays() {
        try {
            System.out.println("Queue: Refreshing all grid displays with latest database data...");
            
            // Refresh waiting grid
            initializeWaitingGridFromDatabase();
            
            // Refresh charging grids with latest data from BayManagement (without triggering refresh cycle)
            String[] fastTexts = cephra.Admin.BayManagement.getFastChargingGridTexts();
            java.awt.Color[] fastColors = cephra.Admin.BayManagement.getFastChargingGridColors();
            updateLocalFastButtons(fastTexts, fastColors);
            
            String[] normalTexts = cephra.Admin.BayManagement.getNormalChargingGridTexts();
            java.awt.Color[] normalColors = cephra.Admin.BayManagement.getNormalChargingGridColors();
            updateLocalNormalButtons(normalTexts, normalColors);
            
            // Update Monitor displays directly (without calling updateMonitorFastGrid/NormalGrid to avoid recursion)
            if (monitorInstance != null) {
                monitorInstance.updateFastGrid(fastTexts);
                monitorInstance.updateNormalGrid(normalTexts);
            }
            
            System.out.println("Queue: All grid displays refreshed successfully");
            
        } catch (Exception e) {
            System.err.println("Error refreshing Queue grid displays: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Initializes waiting grid from database
     */
    private void initializeWaitingGridFromDatabase() {
        try {
            System.out.println("Queue: Initializing waiting grid from database...");
            
            // Get waiting grid tickets from database
            String[] waitingTickets = cephra.Admin.BayManagement.getWaitingGridTickets();
            
            System.out.println("Queue: Waiting grid tickets from database: " + java.util.Arrays.toString(waitingTickets));
            
            // Update waiting grid buttons
            for (int i = 0; i < waitingTickets.length && i < gridButtons.length; i++) {
                if (waitingTickets[i] != null && !waitingTickets[i].isEmpty()) {
                    gridButtons[i].setText(waitingTickets[i]);
                    gridButtons[i].setVisible(true);
                    buttonCount = Math.max(buttonCount, i + 1);
                } else {
                    gridButtons[i].setText("");
                    gridButtons[i].setVisible(false);
                }
            }
            
            // Update Monitor waiting grid
            if (monitorInstance != null) {
                monitorInstance.updateDisplay(waitingTickets);
            }
            
            System.out.println("Queue: Waiting grid initialized with " + buttonCount + " tickets from database");
            
        } catch (Exception e) {
            System.err.println("Error initializing waiting grid from database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
  
    /**
     * Get or create the static unified notification instance
     */
    private static cephra.Phone.UnifiedNotification getOrCreateUnifiedNotification(cephra.Frame.Phone phoneFrame) {
        if (staticNotification == null) {
            staticNotification = new cephra.Phone.UnifiedNotification();
            staticNotification.addToFrame(phoneFrame);
        }
        return staticNotification;
    }
    
    private void triggerNotificationForCustomer(String customer, String notificationType, String ticketId, String bayNumber) {
        if (customer == null || customer.trim().isEmpty()) {
            System.out.println("Queue: Cannot trigger notification - customer is null or empty");
            return;
        }
        
        System.out.println("Queue: Triggering " + notificationType + " notification for customer " + customer + " with ticket " + ticketId);
        
        try {
            // Add notification to history first
            switch (notificationType) {
                case "TICKET_WAITING":
                    cephra.Phone.Utilities.NotificationHistoryManager.addTicketWaitingNotification(customer, ticketId);
                    break;
                case "TICKET_PENDING":
                    cephra.Phone.Utilities.NotificationHistoryManager.addTicketPendingNotification(customer, ticketId);
                    break;
                case "MY_TURN":
                    cephra.Phone.Utilities.NotificationHistoryManager.addMyTurnNotification(customer, ticketId, bayNumber);
                    break;
                case "FULL_CHARGE":
                    cephra.Phone.Utilities.NotificationHistoryManager.addFullChargeNotification(customer, ticketId);
                    break;
                default:
                    System.out.println("Queue: Unknown notification type: " + notificationType);
                    return;
            }
            
            // Show visual notification on phone screen
            showVisualNotification(customer, notificationType, ticketId, bayNumber);
            
        } catch (Exception e) {
            System.err.println("Error triggering notification for customer " + customer + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Shows the actual visual notification on the phone screen
     */
    private void showVisualNotification(String customer, String notificationType, String ticketId, String bayNumber) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Find the phone frame
                java.awt.Window[] windows = java.awt.Window.getWindows();
                cephra.Frame.Phone phoneFrame = null;
                
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        phoneFrame = (cephra.Frame.Phone) window;
                        break;
                    }
                }
                
                if (phoneFrame == null) {
                    System.out.println("Queue: Phone frame not found - cannot show visual notification");
                    return;
                }
                
                // Check if the current user matches the notification recipient
                String currentUser = cephra.CephraDB.getCurrentUsername();
                if (currentUser == null || !currentUser.equals(customer)) {
                    System.out.println("Queue: Current user (" + currentUser + ") doesn't match notification recipient (" + customer + ") - skipping visual notification");
                    return;
                }
                
                System.out.println("Queue: Showing visual notification for " + notificationType + " to user " + customer);
                
                // Get or create the unified notification instance (static to allow updates)
                cephra.Phone.UnifiedNotification unifiedNotif = getOrCreateUnifiedNotification(phoneFrame);
                
                // Update and show the notification (this will override any current notification)
                switch (notificationType) {
                    case "TICKET_WAITING":
                        unifiedNotif.updateAndShowNotification(cephra.Phone.UnifiedNotification.TYPE_WAITING, ticketId, bayNumber);
                        System.out.println("Queue: Updated to TICKET_WAITING notification for ticket " + ticketId);
                        break;
                        
                    case "TICKET_PENDING":
                        unifiedNotif.updateAndShowNotification(cephra.Phone.UnifiedNotification.TYPE_PENDING, ticketId, bayNumber);
                        System.out.println("Queue: Updated to TICKET_PENDING notification for ticket " + ticketId);
                        break;
                        
                    case "MY_TURN":
                        unifiedNotif.updateAndShowNotification(cephra.Phone.UnifiedNotification.TYPE_MY_TURN, ticketId, bayNumber);
                        System.out.println("Queue: Updated to MY_TURN notification for bay " + bayNumber);
                        break;
                        
                    case "FULL_CHARGE":
                        unifiedNotif.updateAndShowNotification(cephra.Phone.UnifiedNotification.TYPE_DONE, ticketId, bayNumber);
                        System.out.println("Queue: Updated to FULL_CHARGE notification using unified system");
                        break;
                        
                    default:
                        System.out.println("Queue: Unknown notification type for visual display: " + notificationType);
                        break;
                }
                
            } catch (Exception e) {
                System.err.println("Error showing visual notification: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    @SuppressWarnings("unused")
    private String determineBayNumber(String ticket, boolean isFast) {
        if (ticket == null || ticket.trim().isEmpty()) {
            return "Unknown";
        }
        
        try {
            if (isFast) {
                // Check fast charging slots
                if (ticket.equals(fastslot1.getText())) return "Bay-1";
                if (ticket.equals(fastslot2.getText())) return "Bay-2";
                if (ticket.equals(fastslot3.getText())) return "Bay-3";
                
                // If not found in current display, try to determine from ticket type
                // Fast charging bays are typically Bay-1, Bay-2, Bay-3
                return "Bay-1"; // Default to first fast charging bay
            } else {
                // Check normal charging slots
                if (ticket.equals(normalcharge1.getText())) return "Bay-4";
                if (ticket.equals(normalcharge2.getText())) return "Bay-5";
                if (ticket.equals(normalcharge3.getText())) return "Bay-6";
                if (ticket.equals(normalcharge4.getText())) return "Bay-7";
                if (ticket.equals(normalcharge5.getText())) return "Bay-8";
                
                // If not found in current display, try to determine from ticket type
                // Normal charging bays are typically Bay-4, Bay-5, Bay-6, Bay-7, Bay-8
                return "Bay-4"; // Default to first normal charging bay
            }
        } catch (Exception e) {
            System.err.println("Error determining bay number for ticket " + ticket + ": " + e.getMessage());
        }
        
        // Default fallback based on ticket type
        return isFast ? "Bay-1" : "Bay-4";
    }
}
