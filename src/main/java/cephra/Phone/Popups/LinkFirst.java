package cephra.Phone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LinkFirst extends javax.swing.JPanel { 
 
    private static LinkFirst currentInstance = null;
    private static String currentTicketId = null;
    private static boolean isShowing = false;    
    private static final int POPUP_WIDTH = 280;
    private static final int POPUP_HEIGHT = 200;
    private static final int PHONE_WIDTH = 350; 
    private static final int PHONE_HEIGHT = 750;   
  
    public static boolean isShowingForTicket(String ticketId) {
        return isShowing && ticketId != null && ticketId.equals(currentTicketId);
    }
    
    public static boolean canShowPayPop(String ticketId, String customerUsername) { return true; }
   
    public static boolean showPayPop(String ticketId, String customerUsername) {
        System.out.println("=== PayPop.showPayPop() called ===");
        
        if (!canShowPayPop(ticketId, customerUsername)) {
            return false;
        }
        
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof cephra.Frame.Phone) {
                cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                showCenteredPayPop(phoneFrame, ticketId);
                return true;
            }
        }
        return false;
    }
    
    private static void showCenteredPayPop(cephra.Frame.Phone phoneFrame, String ticketId) {
        SwingUtilities.invokeLater(() -> {
            currentInstance = new LinkFirst();
            currentTicketId = ticketId;
            isShowing = true;
            
            int containerW = phoneFrame.getContentPane() != null ? phoneFrame.getContentPane().getWidth() : 0;
            int containerH = phoneFrame.getContentPane() != null ? phoneFrame.getContentPane().getHeight() : 0;
            if (containerW <= 0) containerW = PHONE_WIDTH;
            if (containerH <= 0) containerH = PHONE_HEIGHT;
            int x = (containerW - POPUP_WIDTH) / 2;
            int y = (containerH - POPUP_HEIGHT) / 2;
            
            currentInstance.setBounds(x, y, POPUP_WIDTH, POPUP_HEIGHT);
            
            JLayeredPane layeredPane = phoneFrame.getRootPane().getLayeredPane();
            layeredPane.add(currentInstance, JLayeredPane.MODAL_LAYER);
            layeredPane.moveToFront(currentInstance);
            
            currentInstance.setVisible(true);
            phoneFrame.repaint();
        });
    }
    
   
    public static void hidepop() {
    if (currentInstance != null && isShowing) {
        
        LinkFirst instance = currentInstance;

        SwingUtilities.invokeLater(() -> {
            if (instance.getParent() != null) {
                instance.getParent().remove(instance);
            }
            currentInstance = null;
            currentTicketId = null;
            isShowing = false;

            for (Window window : Window.getWindows()) {
                if (window instanceof cephra.Frame.Phone) {
                    window.repaint();
                    break;
                }
            }
        });
    }
}

    public LinkFirst() {
        initComponents();
        initializePayPop();
    }

    private void initializePayPop() {
       
        setPreferredSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
        setSize(POPUP_WIDTH, POPUP_HEIGHT);
        setOpaque(false);
        setupCloseButton();
        
        
        
       
    }

    private void setupCloseButton() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    hidepop();
                }
            }
        });
        
        // Request focus so key events work
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    // All computation-related methods removed; this popup is informational only
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Golink = new javax.swing.JButton();
        notLink = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        Golink.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Golink.setToolTipText("Go to car link");
        Golink.setBorder(null);
        Golink.setBorderPainted(false);
        Golink.setContentAreaFilled(false);
        Golink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GolinkActionPerformed(evt);
            }
        });
        add(Golink);
        Golink.setBounds(80, 140, 110, 40);

        notLink.setBorderPainted(false);
        notLink.setContentAreaFilled(false);
        notLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notLinkActionPerformed(evt);
            }
        });
        add(notLink);
        notLink.setBounds(245, 3, 20, 20);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Plslink.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 280, 200);
    }// </editor-fold>//GEN-END:initComponents

    private void notLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notLinkActionPerformed
        hidepop();
        
    }//GEN-LAST:event_notLinkActionPerformed

    
    private void GolinkActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("LinkFirst Ok button clicked - navigating to link panel");
        
        // Hide this popup first
        hidepop();
        
        // Navigate to link.java panel
        SwingUtilities.invokeLater(() -> {
            java.awt.Window[] windows = java.awt.Window.getWindows();
            for (java.awt.Window window : windows) {
                if (window instanceof cephra.Frame.Phone) {
                    cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                    phoneFrame.switchPanel(new cephra.Phone.link());
                    break;
                }
            }
        });
    }

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Golink;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton notLink;
    // End of variables declaration//GEN-END:variables
}
