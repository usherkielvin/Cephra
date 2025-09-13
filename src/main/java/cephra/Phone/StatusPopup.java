package cephra.Phone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StatusPopup extends javax.swing.JPanel {
    private static StatusPopup currentInstance = null;
    private static boolean isShowing = false;

    private static final int POPUP_WIDTH = 280;
    private static final int POPUP_HEIGHT = 140;
    private static final int PHONE_WIDTH = 350;
    private static final int PHONE_HEIGHT = 750;

    private javax.swing.JButton okButton;
    private javax.swing.JLabel imageLabel;

    public static void show(String imageResourcePath, String okText) {
        SwingUtilities.invokeLater(() -> {
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof cephra.Frame.Phone) {
                    cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                    showCentered(phoneFrame, imageResourcePath, okText);
                    return;
                }
            }
        });
    }

    private static void showCentered(cephra.Frame.Phone phoneFrame, String imageResourcePath, String okText) {
        if (isShowing) closePopup();
        currentInstance = new StatusPopup(imageResourcePath, okText);
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
    }

    public static void closePopup() {
        if (currentInstance != null && isShowing) {
            StatusPopup instance = currentInstance;
            SwingUtilities.invokeLater(() -> {
                if (instance.getParent() != null) {
                    instance.getParent().remove(instance);
                }
                currentInstance = null;
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

    private StatusPopup(String imageResourcePath, String okText) {
        initComponents();
        setPreferredSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
        setSize(POPUP_WIDTH, POPUP_HEIGHT);
        setOpaque(false);
        setupCloseButton();
        if (okText != null && !okText.isEmpty()) okButton.setText(okText);
        if (imageResourcePath != null) setScaledIcon(imageResourcePath);
    }

    private void setupCloseButton() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) { StatusPopup.closePopup(); }
            }
        });
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    private void initComponents() {
        okButton = new javax.swing.JButton();
        imageLabel = new javax.swing.JLabel();

        setLayout(null);

        okButton.setFont(new java.awt.Font("Segoe UI", 1, 18));
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onOk(evt);
            }
        });
        add(okButton);
        okButton.setBounds(80, 90, 100, 30);

        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        add(imageLabel);
        imageLabel.setBounds(0, 0, 280, 140);
    }

    private void onOk(java.awt.event.ActionEvent evt) { StatusPopup.closePopup(); }

    private void setScaledIcon(String resourcePath) {
        try {
            java.net.URL url = getClass().getResource(resourcePath);
            if (url == null) return;
            ImageIcon icon = new ImageIcon(url);
            int iw = icon.getIconWidth();
            int ih = icon.getIconHeight();
            if (iw <= 0 || ih <= 0) { imageLabel.setIcon(icon); return; }
            double sx = (double) POPUP_WIDTH / iw;
            double sy = (double) POPUP_HEIGHT / ih;
            double scale = Math.min(sx, sy); // contain: show whole image, no crop
            int tw = (int) Math.round(iw * scale);
            int th = (int) Math.round(ih * scale);
            Image scaled = icon.getImage().getScaledInstance(tw, th, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception ignore) {
            // fallback silently
        }
    }
}


