package cephra.AdminPanels;

public class AdminLogin extends javax.swing.JPanel {

    private java.awt.Image backgroundImage;

    public AdminLogin() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);
        // Load a background image proactively so panel never renders white
        loadBackgroundImage();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        try {
            // Try different images that might exist (load from classpath root)
            java.net.URL imageUrl = getClass().getResource("/cephra/Photos/bigs.png");
            if (imageUrl == null) {
                imageUrl = getClass().getResource("/cephra/Photos/emp.png");
            }
            if (imageUrl == null) {
                imageUrl = getClass().getResource("/cephra/Photos/lod.png");
            }
            if (imageUrl == null) {
                imageUrl = getClass().getResource("/cephra/Photos/pho.png");
            }

            if (imageUrl != null) {
                javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imageUrl);
                // Scale the image to fit the panel size
                java.awt.Image img = icon.getImage();
                java.awt.Image scaledImg = img.getScaledInstance(1000, 750, java.awt.Image.SCALE_SMOOTH);
                jLabel1.setIcon(new javax.swing.ImageIcon(scaledImg));
                jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel1.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel1.setPreferredSize(new java.awt.Dimension(1000, 750));
                System.out.println("Successfully loaded image: " + imageUrl.toString());
            } else {
                System.err.println("No images found in cephra/Photos/ directory");
                // Set a colored background as fallback so panel isn't white
                jLabel1.setBackground(java.awt.Color.BLUE);
                jLabel1.setOpaque(true);
                jLabel1.setPreferredSize(new java.awt.Dimension(1000, 750));
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            // Set a colored background as fallback so panel isn't white
            jLabel1.setBackground(java.awt.Color.RED);
            jLabel1.setOpaque(true);
            jLabel1.setPreferredSize(new java.awt.Dimension(1000, 750));
        }

        // Use BorderLayout to ensure label fills the entire panel
        setLayout(new java.awt.BorderLayout());
        add(jLabel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    private void loadBackgroundImage() {
        try {
            java.net.URL url = getClass().getResource("/cephra/Photos/emp.png");
            if (url == null) url = getClass().getResource("/cephra/Photos/bigs.png");
            if (url == null) url = getClass().getResource("/cephra/Photos/lod.png");
            if (url == null) url = getClass().getResource("/cephra/Photos/pho.png");
            if (url != null) {
                backgroundImage = new javax.swing.ImageIcon(url).getImage();
            }
        } catch (Exception ignored) {}
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
