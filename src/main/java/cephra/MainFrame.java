package cephra;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainFrame extends javax.swing.JFrame {

    public MainFrame() {
        setUndecorated(true);
        initComponents();
        setSize(1000, 750);
        setResizable(false);
        setAppIcon();
        addEscapeKeyListener();
        makeDraggable();
    }
     private void setAppIcon() {
        java.net.URL iconUrl = getClass().getClassLoader().getResource("cephra/Photos/lod.png");
        if (iconUrl != null) {
            setIconImage(new javax.swing.ImageIcon(iconUrl).getImage());
        }
    }

    private void addEscapeKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        });
        setFocusable(true);
    }

    private void makeDraggable() {
        final Point[] dragPoint = {null};

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragPoint[0] = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragPoint[0] != null) {
                    Point currentLocation = getLocation();
                    setLocation(
                        currentLocation.x + e.getX() - dragPoint[0].x,
                        currentLocation.y + e.getY() - dragPoint[0].y
                    );
                }
            }
        });
    }
        // </editor-fold>
    
   
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        cephra.AdminPanels.Splash splashPanel = new cephra.AdminPanels.Splash();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setResizable(false);

        // Use BorderLayout and add the admin panel directly
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(splashPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
 public void switchPanel(JPanel newPanel) {
        getContentPane().removeAll();
        getContentPane().add(newPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    } 
 public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            PhoneFrame phoneFrame = new PhoneFrame();
            TVFrame tvFrame = new TVFrame();

            // Position the frames
            java.awt.Rectangle screenBounds = java.awt.GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();

            // MainFrame at upper-right of the primary screen
            mainFrame.setLocation(
                screenBounds.x + screenBounds.width - mainFrame.getWidth(),
                screenBounds.y
            );

            // Let TVFrame position itself (it defaults to top-left)

            // Show main and TV first
            mainFrame.setVisible(true);
            tvFrame.setVisible(true);

            // Center PhoneFrame on primary screen and bring to front (overlap others)
            phoneFrame.setLocationRelativeTo(null);
            phoneFrame.setVisible(true);
            phoneFrame.toFront();
            phoneFrame.requestFocus();
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
