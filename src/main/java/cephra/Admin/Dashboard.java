package cephra.Admin;

public class Dashboard extends javax.swing.JPanel {

    private java.awt.Image dashboardImage;
   
    public Dashboard() {
        initComponents();
         setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);
        loadDashboardImage();
        updateLabelIcon();
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                updateLabelIcon();
            }
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        staffbutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        Baybutton = new javax.swing.JButton();
        quebutton = new javax.swing.JButton();
        exitlogin = new javax.swing.JButton();
        toggleSwitch = new cephra.Admin.ToggleSwitch();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1000, 750));
        setMinimumSize(new java.awt.Dimension(1000, 750));
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setLayout(null);

        staffbutton.setBorder(null);
        staffbutton.setBorderPainted(false);
        staffbutton.setContentAreaFilled(false);
        add(staffbutton);
        staffbutton.setBounds(500, 10, 110, 40);
        staffbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Dashboard.this);
                if (w instanceof cephra.Frame.Admin) {
                    ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.StaffRecord());
                }
            }
        });

        historybutton.setBorder(null);
        historybutton.setBorderPainted(false);
        historybutton.setContentAreaFilled(false);
        add(historybutton);
        historybutton.setBounds(430, 10, 60, 40);
        historybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Dashboard.this);
                if (w instanceof cephra.Frame.Admin) {
                    ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.History());
                }
            }
        });

        Baybutton.setBorder(null);
        Baybutton.setBorderPainted(false);
        Baybutton.setContentAreaFilled(false);
        add(Baybutton);
        Baybutton.setBounds(380, 10, 40, 40);
        Baybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Dashboard.this);
                if (w instanceof cephra.Frame.Admin) {
                    ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Bay());
                }
            }
        });

        quebutton.setBorder(null);
        quebutton.setBorderPainted(false);
        quebutton.setContentAreaFilled(false);
        add(quebutton);
        quebutton.setBounds(270, 10, 100, 40);
        quebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quebuttonActionPerformed(evt);
            }
        });

        exitlogin.setBorder(null);
        exitlogin.setBorderPainted(false);
        exitlogin.setContentAreaFilled(false);
        add(exitlogin);
        exitlogin.setBounds(930, 0, 70, 60);
        exitlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Dashboard.this);
                if (w instanceof cephra.Frame.Admin) {
                    ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Login());
                }
            }
        });
        add(toggleSwitch);
        toggleSwitch.setBounds(577, 600, 60, 30);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/Business.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1240, 750);

        // Ensure controls are above background
        setComponentZOrder(jLabel1, getComponentCount() - 1);
        setComponentZOrder(staffbutton, 0);
        setComponentZOrder(historybutton, 0);
        setComponentZOrder(Baybutton, 0);
        setComponentZOrder(quebutton, 0);
        setComponentZOrder(exitlogin, 0);
        setComponentZOrder(toggleSwitch, 0);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Baybutton;
    private javax.swing.JButton exitlogin;
    private javax.swing.JButton historybutton;
    private javax.swing.JLabel jLabel1;
    private cephra.Admin.ToggleSwitch toggleSwitch;
    private javax.swing.JButton quebutton;
    private javax.swing.JButton staffbutton;
    // End of variables declaration//GEN-END:variables

    private void quebuttonActionPerformed(java.awt.event.ActionEvent evt) {
        java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Dashboard.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Queue());
        }
    }

    private void loadDashboardImage() {
        try {
            java.net.URL url = getClass().getResource("/cephra/Photos/Business.png");
            if (url == null) url = getClass().getResource("/cephra/Photos/res.png");
            if (url == null) url = getClass().getResource("/cephra/Photos/emp.png");
            if (url == null) url = getClass().getResource("/cephra/Photos/bigs.png");
            if (url == null) url = getClass().getResource("/cephra/Photos/logi.png");
            if (url == null) url = getClass().getResource("/cephra/Photos/pho.png");
            if (url != null) {
                dashboardImage = new javax.swing.ImageIcon(url).getImage();
            }
        } catch (Exception ignored) {}
    }

    private void updateLabelIcon() {
        if (dashboardImage == null) {
            jLabel1.setIcon(null);
            return;
        }
        int w = Math.max(1, getWidth());
        int h = Math.max(1, getHeight());
        java.awt.Image scaled = dashboardImage.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
        jLabel1.setIcon(new javax.swing.ImageIcon(scaled));
    }
}
