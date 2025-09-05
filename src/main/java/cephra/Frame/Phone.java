package cephra.Frame;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

public class Phone extends javax.swing.JFrame {

    private Point dragStartPoint;
    private JLabel Iphoneframe;
    public Phone() {

        setUndecorated(true);
        initComponents();
        setSize(350, 750);
        setResizable(false);
        setAppIcon();
        addEscapeKeyListener();
        makeDraggable();
        
        // Add curved edges to the frame
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 350, 750, 120, 120));
        
        // Refresh ticket counters when Phone frame is created
        try {
            cephra.Phone.QueueFlow.refreshCountersFromDatabase();
        } catch (Exception e) {
            System.err.println("Error refreshing ticket counters: " + e.getMessage());
        }

        // Create and setup kenji label to always appear on top
        PhoneFrame();
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
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStartPoint = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStartPoint != null) {
                    Point currentLocation = getLocation();
                    setLocation(
                        currentLocation.x + e.getX() - dragStartPoint.x,
                        currentLocation.y + e.getY() - dragStartPoint.y
                    );
                }
            }
        });
    }

    private void PhoneFrame() {
        // Create the kenji label
        Iphoneframe = new JLabel();
         Iphoneframe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/red.png")));
        Iphoneframe.setBounds(-27, 0, 401, 750); // center top
        Iphoneframe.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Add to the root pane's layered pane to ensure it's always on top
        getRootPane().getLayeredPane().add(Iphoneframe, JLayeredPane.POPUP_LAYER);
        
        // Make sure it's visible and on top
        Iphoneframe.setVisible(true);
        getRootPane().getLayeredPane().moveToFront(Iphoneframe);
    }

    public void switchPanel(javax.swing.JPanel newPanel) {
        getContentPane().removeAll();
        getContentPane().add(newPanel);
        revalidate();
        repaint();
        
        // Ensure kenji label stays on top after panel switch
        if (Iphoneframe != null) {
            getRootPane().getLayeredPane().moveToFront(Iphoneframe);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        phonePanel = new cephra.Phone.Splashscreen();

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

        // pack(); // Removed to prevent frame from becoming displayable before setUndecorated
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel phonePanel;
    // End of variables declaration//GEN-END:variables
}
