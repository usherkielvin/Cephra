package cephra.AdminPanels;

public class AdminLogin extends javax.swing.JPanel {

    public AdminLogin() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        try {
            // Try different images that might exist
            java.net.URL imageUrl = getClass().getClassLoader().getResource("cephra/Photos/bigs.png");
            if (imageUrl == null) {
                imageUrl = getClass().getClassLoader().getResource("cephra/Photos/emp.png");
            }
            if (imageUrl == null) {
                imageUrl = getClass().getClassLoader().getResource("cephra/Photos/lod.png");
            }
            if (imageUrl == null) {
                imageUrl = getClass().getClassLoader().getResource("cephra/Photos/pho.png");
            }
            
            if (imageUrl != null) {
                javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imageUrl);
                // Scale the image to fit the panel size
                java.awt.Image img = icon.getImage();
                java.awt.Image scaledImg = img.getScaledInstance(1000, 750, java.awt.Image.SCALE_SMOOTH);
                jLabel1.setIcon(new javax.swing.ImageIcon(scaledImg));
                jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel1.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
                System.out.println("Successfully loaded image: " + imageUrl.getPath());
            } else {
                System.err.println("No images found in cephra/Photos/ directory");
                // Set a colored background as fallback
                jLabel1.setBackground(java.awt.Color.BLUE);
                jLabel1.setOpaque(true);
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            // Set a colored background as fallback
            jLabel1.setBackground(java.awt.Color.RED);
            jLabel1.setOpaque(true);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
