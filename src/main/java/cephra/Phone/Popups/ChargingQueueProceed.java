
package cephra.Phone.Popups;


public class ChargingQueueProceed extends javax.swing.JPanel {

  
    public ChargingQueueProceed() {
        initComponents();
        // Ensure panel has a non-zero preferred size for null layout + pack()
        setPreferredSize(new java.awt.Dimension(270, 390));
        setSize(270, 390);
        // Make panel transparent so only the PNG shows
        setOpaque(false);
        setBackground(new java.awt.Color(0, 0, 0, 0));
        // Ensure children don't paint backgrounds
        if (tickectID != null) tickectID.setOpaque(false);
        if (chargingService != null) chargingService.setOpaque(false);
        if (batteryLevel != null) batteryLevel.setOpaque(false);
        if (Icon != null) Icon.setOpaque(false);
        if (okBTN != null) okBTN.setOpaque(false);
        if (cancelTixBTN != null) cancelTixBTN.setOpaque(false);
    }

    @Override
    public boolean isOpaque() { return false; }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        // do not fill background; keep fully transparent behind child components
        super.paintComponent(g);
    }

    // Show as modal popup and wire up behavior to mimic QueueTicket actions
    public static boolean showPopup() {
        try {
            // Find phone window to own the popup (match AdsPhone approach)
            java.awt.Window owner = null;
            for (java.awt.Window w : java.awt.Window.getWindows()) {
                if (w instanceof cephra.Frame.Phone) { owner = w; break; }
            }
            // Use a transparent JDialog (safe across JDKs)
            final javax.swing.JDialog dialog;
            if (owner instanceof java.awt.Frame) {
                dialog = new javax.swing.JDialog((java.awt.Frame) owner);
            } else if (owner instanceof java.awt.Dialog) {
                dialog = new javax.swing.JDialog((java.awt.Dialog) owner);
            } else {
                dialog = new javax.swing.JDialog();
            }
            dialog.setModal(true);
            dialog.setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
            dialog.setUndecorated(true);
            dialog.setBackground(new java.awt.Color(0, 0, 0, 0));
            ChargingQueueProceed panel = new ChargingQueueProceed();
            // Populate labels based on QueueFlow / DB
            panel.populateFromQueueFlow();
            // OK -> create ticket (QueueFlow.addCurrentToAdminAndStore) and close
            panel.okBTN.addActionListener(_ -> {
                try {
                    cephra.Phone.Utilities.QueueFlow.addCurrentToAdminAndStore(cephra.Database.CephraDB.getCurrentUsername());
                } catch (Throwable t) {
                    System.err.println("ChargingQueueProceed OK error: " + t.getMessage());
                }
                dialog.dispose();
            });
            // Cancel -> clear any active ticket preview and close
            panel.cancelTixBTN.addActionListener(_ -> {
                try {
                    String username = cephra.Database.CephraDB.getCurrentUsername();
                    cephra.Database.CephraDB.clearActiveTicket(username);
                } catch (Throwable t) {}
                dialog.dispose();
            });
            // Also make content pane explicitly transparent by replacing it
            javax.swing.JPanel transparentCP = new javax.swing.JPanel(new java.awt.BorderLayout()) {
                @Override public boolean isOpaque() { return false; }
            };
            transparentCP.setOpaque(false);
            transparentCP.setBackground(new java.awt.Color(0, 0, 0, 0));
            dialog.setContentPane(transparentCP);
            transparentCP.add(panel, java.awt.BorderLayout.CENTER);
            // Size dialog to panel's preferred size
            dialog.pack();
            // Center relative to the Phone frame if present and dim its background
            java.awt.Window centerOn = owner;
            javax.swing.JRootPane root = null;
            if (centerOn instanceof javax.swing.JFrame) {
                root = ((javax.swing.JFrame) centerOn).getRootPane();
            } else if (centerOn instanceof javax.swing.JDialog) {
                root = ((javax.swing.JDialog) centerOn).getRootPane();
            }
            java.awt.Component oldGlass = null;
            FadeGlassPane dimGlass = null;
            // Temporarily disable tooltips to avoid stray bubbles over the dialog
            javax.swing.ToolTipManager ttm = javax.swing.ToolTipManager.sharedInstance();
            int oldInitial = ttm.getInitialDelay();
            int oldDismiss = ttm.getDismissDelay();
            boolean tooltipsWereEnabled = true;
            try { ttm.setEnabled(false); } catch (Throwable ignore) { tooltipsWereEnabled = false; }
            if (root != null) {
                oldGlass = root.getGlassPane();
                dimGlass = new FadeGlassPane();
                root.setGlassPane(dimGlass);
                dimGlass.setVisible(true);
                dimGlass.fadeIn();
            }
            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(centerOn);
            try {
                dialog.setVisible(true);
            } finally {
                // Restore original glass pane / undim
                if (dimGlass != null) {
                    dimGlass.fadeOutAndHide();
                }
                if (root != null && oldGlass != null) {
                    root.setGlassPane(oldGlass);
                }
                if (tooltipsWereEnabled) {
                    try {
                        ttm.setEnabled(true);
                        ttm.setInitialDelay(oldInitial);
                        ttm.setDismissDelay(oldDismiss);
                    } catch (Throwable ignore) {}
                }
                dialog.dispose();
            }
            return true;
        } catch (Throwable t) {
            System.err.println("ChargingQueueProceed.showPopup error: " + t.getMessage());
            return false;
        }
    }

    private void populateFromQueueFlow() {
        try {
            String service = cephra.Phone.Utilities.QueueFlow.getCurrentServiceName();
            String ticket = cephra.Phone.Utilities.QueueFlow.getCurrentTicketId();
            String username = cephra.Database.CephraDB.getCurrentUsername();
            int level = cephra.Database.CephraDB.getUserBatteryLevel(username);

            // Determine display ticket (priority if <20%)
            if (level < 20) {
                if (ticket == null || ticket.isEmpty()) {
                    ticket = cephra.Phone.Utilities.QueueFlow.previewNextPriorityTicketIdForService(service, level);
                } else if (!ticket.contains("P")) {
                    if (ticket.startsWith("FCH")) ticket = ticket.replace("FCH", "FCHP");
                    else if (ticket.startsWith("NCH")) ticket = ticket.replace("NCH", "NCHP");
                }
            } else {
                if (ticket == null || ticket.isEmpty()) {
                    ticket = cephra.Phone.Utilities.QueueFlow.previewNextTicketIdForService(service);
                }
            }

            tickectID.setText(ticket != null && !ticket.isEmpty() ? ticket : tickectID.getText());
            chargingService.setText(service != null && !service.isEmpty() ? service : chargingService.getText());
            batteryLevel.setText(level + "%");
        } catch (Throwable t) {
            System.err.println("ChargingQueueProceed populate error: " + t.getMessage());
        }
    }

   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tickectID = new javax.swing.JLabel();
        chargingService = new javax.swing.JLabel();
        batteryLevel = new javax.swing.JLabel();
        okBTN = new javax.swing.JButton();
        cancelTixBTN = new javax.swing.JButton();
        Icon = new javax.swing.JLabel();

        setOpaque(false);
        setLayout(null);

        tickectID.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        tickectID.setForeground(new java.awt.Color(0, 189, 201));
        tickectID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tickectID.setText("NCH005");
        add(tickectID);
        tickectID.setBounds(17, 20, 230, 50);

        chargingService.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        chargingService.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chargingService.setText("Normal Charging");
        add(chargingService);
        chargingService.setBounds(7, 70, 245, 30);

        batteryLevel.setFont(new java.awt.Font("Segoe UI", 1, 40)); // NOI18N
        batteryLevel.setForeground(new java.awt.Color(0, 189, 201));
        batteryLevel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        batteryLevel.setText("55%");
        add(batteryLevel);
        batteryLevel.setBounds(66, 114, 130, 50);

        okBTN.setBorder(null);
        okBTN.setBorderPainted(false);
        okBTN.setContentAreaFilled(false);
        add(okBTN);
        okBTN.setBounds(20, 282, 220, 39);

        cancelTixBTN.setBorder(null);
        cancelTixBTN.setBorderPainted(false);
        cancelTixBTN.setContentAreaFilled(false);
        add(cancelTixBTN);
        cancelTixBTN.setBounds(20, 335, 220, 35);

        Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/ChargeQueProceed.png"))); // NOI18N
        add(Icon);
        Icon.setBounds(0, 0, 270, 390);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Icon;
    private javax.swing.JLabel batteryLevel;
    private javax.swing.JButton cancelTixBTN;
    private javax.swing.JLabel chargingService;
    private javax.swing.JButton okBTN;
    private javax.swing.JLabel tickectID;
    // End of variables declaration//GEN-END:variables
}

// Simple fading dim glass pane
class FadeGlassPane extends javax.swing.JComponent {
    private float alpha = 0f; // 0..1
    private final java.awt.Color base = new java.awt.Color(0, 0, 0);
    private javax.swing.Timer timer;

    public FadeGlassPane() {
        setOpaque(false);
        enableEvents(java.awt.AWTEvent.MOUSE_EVENT_MASK | java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK | java.awt.AWTEvent.MOUSE_WHEEL_EVENT_MASK | java.awt.AWTEvent.KEY_EVENT_MASK);
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
        g2.setComposite(java.awt.AlphaComposite.SrcOver.derive(alpha));
        g2.setColor(base);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    void fadeIn() {
        startAnim(alpha, 0.47f, 180);
    }

    void fadeOutAndHide() {
        startAnim(alpha, 0f, 160, () -> setVisible(false));
    }

    private void startAnim(float from, float to, int durMs) {
        startAnim(from, to, durMs, null);
    }

    private void startAnim(float from, float to, int durMs, Runnable end) {
        if (timer != null && timer.isRunning()) timer.stop();
        final int frames = Math.max(1, durMs / 16);
        final float delta = (to - from) / frames;
        alpha = from;
        timer = new javax.swing.Timer(16, e -> {
            alpha += delta;
            if ((delta > 0 && alpha >= to) || (delta < 0 && alpha <= to)) {
                alpha = to;
                repaint();
                ((javax.swing.Timer) e.getSource()).stop();
                if (end != null) end.run();
            } else {
                repaint();
            }
        });
        timer.setCoalesce(true);
        timer.start();
    }
}
