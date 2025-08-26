package cephra.Frame;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JButton;

public class Monitor extends javax.swing.JFrame {
 private JButton[] displayButtons;

    public Monitor() {
        setUndecorated(true);
        initComponents();
        setSize(1000, 750);
        setResizable(false);
        setAppIcon();
        addEscapeKeyListener();
        makeDraggable();
        setLocationByPlatform(false);
       // positionTopLeft();
         displayButtons = new JButton[] {
            jButton1, jButton2, jButton3, jButton4,
            jButton5, jButton6, jButton7, jButton8
        };
        
        // Initially hide all buttons
        for (JButton button : displayButtons) {
            button.setVisible(false);
        }
       
    }
    
        public void updateDisplay(String[] buttonTexts) {
        for (int i = 0; i < 8; i++) {
            if (i < buttonTexts.length && buttonTexts[i] != null && !buttonTexts[i].isEmpty()) {
                displayButtons[i].setText(buttonTexts[i]);
                displayButtons[i].setVisible(true);
            } else {
                displayButtons[i].setText("");
                displayButtons[i].setVisible(false);
            }
        }
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
/*
    private void positionTopLeft() {
        java.awt.Rectangle bounds = java.awt.GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration()
            .getBounds();
        setLocation(bounds.x, bounds.y);
    } */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 750));
        jPanel1.setLayout(null);

        jPanel2.setLayout(new java.awt.GridLayout(4, 2));

        jButton1.setText("jButton1");
        jPanel2.add(jButton1);

        jButton2.setText("jButton2");
        jPanel2.add(jButton2);

        jButton3.setText("jButton3");
        jPanel2.add(jButton3);

        jButton4.setText("jButton4");
        jPanel2.add(jButton4);

        jButton5.setText("jButton5");
        jPanel2.add(jButton5);

        jButton6.setText("jButton6");
        jPanel2.add(jButton6);

        jButton7.setText("jButton7");
        jPanel2.add(jButton7);

        jButton8.setText("jButton8");
        jPanel2.add(jButton8);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(350, 130, 380, 380);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/QUEUEING - SCREEN SIZE.gif"))); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(-3, -4, 1000, 760);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
