package cephra.Phone.Popups;

import java.awt.*;

public class adsPhone extends javax.swing.JPanel {
    private static final int POPUP_WIDTH = 350;
    private static final int POPUP_HEIGHT = 450;
    private static volatile adsPhone currentInstance;

    public adsPhone() {
        initComponents();
        setupCloseAction();
    }

    // Method to disable all components recursively
    private static void disableAllComponents(Component component) {
        component.setEnabled(false);
        if (component instanceof Container) {
            Container container = (Container) component;
            for (Component child : container.getComponents()) {
                disableAllComponents(child);
            }
        }
    }
    
    // Method to enable all components recursively
    private static void enableAllComponents(Component component) {
        component.setEnabled(true);
        if (component instanceof Container) {
            Container container = (Container) component;
            for (Component child : container.getComponents()) {
                enableAllComponents(child);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setOpaque(false);
        setLayout(null);

        jButton1.setBorder(null);
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        add(jButton1);
        jButton1.setBounds(260, 70, 30, 30);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/adsPhone.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(60, 70, 230, 300);
    }// </editor-fold>//GEN-END:initComponents

    private void setupCloseAction() {
        if (jButton1 != null) {
            jButton1.addActionListener(_ -> hidePopup());
        }
    }

    public static void showAfterDelayOnPhone(final int delayMillis) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.Timer timer = new javax.swing.Timer(delayMillis, evt -> {
                ((javax.swing.Timer) evt.getSource()).stop();
                showOnTopOfPhone();
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    public static void showOnTopOfPhone() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            for (java.awt.Window window : java.awt.Window.getWindows()) {
                if (window instanceof cephra.Frame.Phone) {
                    cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;

                    adsPhone instance = new adsPhone();
                    currentInstance = instance;

                    int containerW = phoneFrame.getContentPane() != null ? phoneFrame.getContentPane().getWidth() : 370;
                    int containerH = phoneFrame.getContentPane() != null ? phoneFrame.getContentPane().getHeight() : 750;

                    int x = (containerW - POPUP_WIDTH) / 2;
                    int y = (containerH - POPUP_HEIGHT) / 2;
                    instance.setBounds(x, y, POPUP_WIDTH, POPUP_HEIGHT);

                    // Disable the background panel (like AlreadyTicket and LinkFirst)
                    Component contentPane = phoneFrame.getContentPane();
                    if (contentPane != null) {
                        contentPane.setEnabled(false);
                        // Disable all child components recursively
                        disableAllComponents(contentPane);
                    }

                    javax.swing.JLayeredPane layeredPane = phoneFrame.getRootPane().getLayeredPane();
                    layeredPane.add(instance, javax.swing.JLayeredPane.MODAL_LAYER);
                    layeredPane.moveToFront(instance);
                    instance.setVisible(true);
                    phoneFrame.repaint();
                    break;
                }
            }
        });
    }

    public static void hidePopup() {
        final adsPhone instance = currentInstance;
        if (instance == null) return;
        javax.swing.SwingUtilities.invokeLater(() -> {
            if (instance.getParent() != null) {
                instance.getParent().remove(instance);
            }
            
            // Re-enable the background panel (like AlreadyTicket and LinkFirst)
            for (Window window : Window.getWindows()) {
                if (window instanceof cephra.Frame.Phone) {
                    cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                    Component contentPane = phoneFrame.getContentPane();
                    if (contentPane != null) {
                        contentPane.setEnabled(true);
                        // Re-enable all child components recursively
                        enableAllComponents(contentPane);
                    }
                    window.repaint();
                    break;
                }
            }
            
            currentInstance = null;
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
