/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package cephra;

/**
 *
 * @author usher
 */
public class PhoneFrame extends javax.swing.JFrame {

    public PhoneFrame() {
        initComponents();
        setSize(350, 750);
        setResizable(false);
        
        // Set application icon
        try {
            java.net.URL iconUrl = getClass().getClassLoader().getResource("cephra/Photos/lod.png");
            if (iconUrl != null) {
                java.awt.Image icon = javax.imageio.ImageIO.read(iconUrl);
                setIconImage(icon);
            } else {
                System.err.println("Icon not found: cephra/Photos/lod.png");
            }
        } catch (Exception e) {
            System.err.println("Error loading icon: " + e.getMessage());
        }
        
        // Add ESC key listener to close the application
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        });
        setFocusable(true);
        
        // Make the frame draggable
        makeDraggable();
    }
    
    private void makeDraggable() {
        final java.awt.Point[] dragPoint = new java.awt.Point[1];
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                dragPoint[0] = e.getPoint();
            }
        });
        
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                if (dragPoint[0] != null) {
                    java.awt.Point currentLocation = getLocation();
                    setLocation(currentLocation.x + e.getX() - dragPoint[0].x,
                               currentLocation.y + e.getY() - dragPoint[0].y);
                }
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        phonePanel = new cephra.PhonePanels.Splashscreen();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(350, 750));
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(phonePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(phonePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setUndecorated(true);
        
        // Add curved edges to the frame - very large radius for maximum rounded corners
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 350, 750, 120, 120));
        
        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void switchPanel(javax.swing.JPanel newPanel) {
        getContentPane().removeAll();
        getContentPane().add(newPanel);
        revalidate();
        repaint();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel phonePanel;
    // End of variables declaration//GEN-END:variables
}
