package cephra.Frame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Monitor extends javax.swing.JFrame {
 private JButton[] displayButtons;
//dis means display ok
        public Monitor() {
        System.out.println("Monitor constructor called");
        setUndecorated(true);
        initComponents();
        setAppIcon();
        addEscapeKeyListener();
        makeDraggable();
        
        // Initialize displayButtons array AFTER initComponents() so buttons exist
        displayButtons = new JButton[] {
            jButton1, jButton2, jButton3, jButton4,
            jButton5, jButton6, jButton7, jButton8,jButton9,jButton10
        };
        
        // Initially hide all buttons
        for (JButton button : displayButtons) {
            button.setVisible(false);
        }
        System.out.println("Monitor constructor completed - displayButtons initialized");
        
        // Use only NetBeans positioning - no override
    }
    

    
                 public void updateDisplay(String[] buttonTexts) {
         System.out.println("Monitor updateDisplay called with " + buttonTexts.length + " texts");
         System.out.println("Monitor window location: " + getLocation());
         System.out.println("jPanel2 bounds: " + WaitingGrid.getBounds());
         
         if (displayButtons == null) {
             System.out.println("ERROR: displayButtons array is null!");
             return;
         }
         for (int i = 0; i < 10; i++) { // Changed from 8 to 10 to match all buttons
             if (displayButtons[i] == null) {
                 System.out.println("ERROR: displayButtons[" + i + "] is null!");
                 continue;
             }
             if (i < buttonTexts.length && buttonTexts[i] != null && !buttonTexts[i].isEmpty()) {
                 System.out.println("Setting button " + i + " to: " + buttonTexts[i]);
                 displayButtons[i].setText(buttonTexts[i]);
                 displayButtons[i].setVisible(true);
             } else {
                 System.out.println("Hiding button " + i);
                 displayButtons[i].setText("");
                 displayButtons[i].setVisible(false);
             }
         }
         
         // Monitor only displays the 10 grid buttons with ticket information
         System.out.println("Monitor display update completed");
     }
     
     // Update fast charge grid display
     public void updateFastGrid(String[] fastTickets) {
         System.out.println("Monitor updateFastGrid called with " + fastTickets.length + " texts");
         
         // Update fast charge buttons (f1, f2, f3)
         if (fastTickets.length >= 1 && fastTickets[0] != null && !fastTickets[0].isEmpty()) {
             f1.setText(fastTickets[0]);
             f1.setVisible(true);
         } else {
             f1.setText("");
             f1.setVisible(false);
         }
         
         if (fastTickets.length >= 2 && fastTickets[1] != null && !fastTickets[1].isEmpty()) {
             f2.setText(fastTickets[1]);
             f2.setVisible(true);
         } else {
             f2.setText("");
             f2.setVisible(false);
         }
         
         if (fastTickets.length >= 3 && fastTickets[2] != null && !fastTickets[2].isEmpty()) {
             f3.setText(fastTickets[2]);
             f3.setVisible(true);
         } else {
             f3.setText("");
             f3.setVisible(false);
         }
         
         System.out.println("Monitor fast grid update completed");
     }
     
     // Update normal charge grid display
     public void updateNormalGrid(String[] normalTickets) {
         System.out.println("Monitor updateNormalGrid called with " + normalTickets.length + " texts");
         
         // Update normal charge buttons (b1, b2, b3, b4, b5)
         if (normalTickets.length >= 1 && normalTickets[0] != null && !normalTickets[0].isEmpty()) {
             b1.setText(normalTickets[0]);
             b1.setVisible(true);
         } else {
             b1.setText("");
             b1.setVisible(false);
         }
         
         if (normalTickets.length >= 2 && normalTickets[1] != null && !normalTickets[1].isEmpty()) {
             b2.setText(normalTickets[1]);
             b2.setVisible(true);
         } else {
             b2.setText("");
             b2.setVisible(false);
         }
         
         if (normalTickets.length >= 3 && normalTickets[2] != null && !normalTickets[2].isEmpty()) {
             b3.setText(normalTickets[2]);
             b3.setVisible(true);
         } else {
             b3.setText("");
             b3.setVisible(false);
         }
         
         if (normalTickets.length >= 4 && normalTickets[3] != null && !normalTickets[3].isEmpty()) {
             b4.setText(normalTickets[3]);
             b4.setVisible(true);
         } else {
             b4.setText("");
             b4.setVisible(false);
         }
         
         if (normalTickets.length >= 5 && normalTickets[4] != null && !normalTickets[4].isEmpty()) {
             b5.setText(normalTickets[4]);
             b5.setVisible(true);
         } else {
             b5.setText("");
             b5.setVisible(false);
         }
         
         System.out.println("Monitor normal grid update completed");
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
    

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Monitor = new javax.swing.JPanel();
        WaitingGrid = new javax.swing.JPanel();
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
        jPanel2 = new javax.swing.JPanel();
        b2 = new javax.swing.JButton();
        b3 = new javax.swing.JButton();
        b4 = new javax.swing.JButton();
        b5 = new javax.swing.JButton();
        b1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        f1 = new javax.swing.JButton();
        f2 = new javax.swing.JButton();
        f3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1000, 750));
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setResizable(false);

        Monitor.setMaximumSize(new java.awt.Dimension(1000, 750));
        Monitor.setPreferredSize(new java.awt.Dimension(1000, 750));
        Monitor.setLayout(null);

        WaitingGrid.setOpaque(false);
        WaitingGrid.setLayout(new java.awt.GridLayout(5, 2, 20, 15));

        jButton1.setBorder(null);
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        WaitingGrid.add(jButton1);

        jButton2.setBorder(null);
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        WaitingGrid.add(jButton2);

        jButton3.setBorder(null);
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        WaitingGrid.add(jButton3);

        jButton4.setBorder(null);
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        WaitingGrid.add(jButton4);

        jButton5.setBorder(null);
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        WaitingGrid.add(jButton5);

        jButton6.setBorder(null);
        jButton6.setBorderPainted(false);
        jButton6.setContentAreaFilled(false);
        WaitingGrid.add(jButton6);

        jButton7.setBorder(null);
        jButton7.setBorderPainted(false);
        jButton7.setContentAreaFilled(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        WaitingGrid.add(jButton7);

        jButton8.setBorder(null);
        jButton8.setBorderPainted(false);
        jButton8.setContentAreaFilled(false);
        WaitingGrid.add(jButton8);

        jButton9.setBorder(null);
        jButton9.setBorderPainted(false);
        jButton9.setContentAreaFilled(false);
        WaitingGrid.add(jButton9);

        jButton10.setBorder(null);
        jButton10.setBorderPainted(false);
        jButton10.setContentAreaFilled(false);
        WaitingGrid.add(jButton10);

        Monitor.add(WaitingGrid);
        WaitingGrid.setBounds(20, 320, 310, 370);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridLayout(1, 4, 20, 20));

        b2.setBorder(null);
        b2.setBorderPainted(false);
        b2.setContentAreaFilled(false);
        b2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b2ActionPerformed(evt);
            }
        });
        jPanel2.add(b2);

        b3.setBorder(null);
        b3.setBorderPainted(false);
        b3.setContentAreaFilled(false);
        jPanel2.add(b3);

        b4.setBorder(null);
        b4.setBorderPainted(false);
        b4.setContentAreaFilled(false);
        b4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b4ActionPerformed(evt);
            }
        });
        jPanel2.add(b4);

        b5.setBorder(null);
        b5.setBorderPainted(false);
        b5.setContentAreaFilled(false);
        jPanel2.add(b5);

        Monitor.add(jPanel2);
        jPanel2.setBounds(400, 540, 550, 160);

        b1.setBorder(null);
        b1.setBorderPainted(false);
        b1.setContentAreaFilled(false);
        Monitor.add(b1);
        b1.setBounds(830, 310, 120, 180);

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridLayout(1, 3, 15, 20));

        f1.setBorder(null);
        f1.setBorderPainted(false);
        f1.setContentAreaFilled(false);
        jPanel1.add(f1);

        f2.setBorder(null);
        f2.setBorderPainted(false);
        f2.setContentAreaFilled(false);
        jPanel1.add(f2);

        f3.setBorder(null);
        f3.setBorderPainted(false);
        f3.setContentAreaFilled(false);
        jPanel1.add(f3);

        Monitor.add(jPanel1);
        jPanel1.setBounds(400, 310, 410, 180);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/TV SCREEn.png"))); // NOI18N
        Monitor.add(jLabel1);
        jLabel1.setBounds(-10, -10, 1000, 750);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Monitor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Monitor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
       
    }//GEN-LAST:event_jButton7ActionPerformed

    private void b2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b2ActionPerformed
      
    }//GEN-LAST:event_b2ActionPerformed

    private void b4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b4ActionPerformed
       
    }//GEN-LAST:event_b4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       
    }//GEN-LAST:event_jButton1ActionPerformed

    // Grid buttons are display-only, no action listeners needed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Monitor;
    private javax.swing.JPanel WaitingGrid;
    private javax.swing.JButton b1;
    private javax.swing.JButton b2;
    private javax.swing.JButton b3;
    private javax.swing.JButton b4;
    private javax.swing.JButton b5;
    private javax.swing.JButton f1;
    private javax.swing.JButton f2;
    private javax.swing.JButton f3;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
