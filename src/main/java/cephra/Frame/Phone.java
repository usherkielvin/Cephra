package cephra.Frame;
import java.awt.*;
import java.awt.event.*;

public class Phone extends javax.swing.JFrame {

    private Point dragStartPoint;

    public Phone() {
        initComponents();
         setSize(350, 750);
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

        setUndecorated(true);
        
        // Add curved edges to the frame
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 350, 750, 120, 120));       
        pack();
    }

    public void switchPanel(javax.swing.JPanel newPanel) {
        getContentPane().removeAll();
        getContentPane().add(newPanel);
        revalidate();
        repaint();
    }
    private javax.swing.JPanel phonePanel;
}
