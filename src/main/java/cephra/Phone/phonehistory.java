
package cephra.Phone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class phonehistory extends javax.swing.JPanel implements UserHistoryManager.HistoryUpdateListener {
    
   
    private String currentUsername;

    public phonehistory() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
        setupLabelPosition(); // Set label position
        makeDraggable();
        
        JScrollPane scrollPane = new JScrollPane(historyPanel);

// Hide scrollbars but keep scrolling possible
scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));



     scrollPane.getVerticalScrollBar().setPreferredSize(new java.awt.Dimension(0, 0));
        // Create and add history panel with scroll pane
        createHistoryPanel();
        
        // Register as listener for history updates
        UserHistoryManager.addHistoryUpdateListener(this);
        
        // Load history entries
        loadHistoryEntries();
    }
    
    private void createHistoryPanel() {
        historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        historyPanel.setBackground(Color.WHITE);
        
        
        // Use the existing scroll pane if it exists, otherwise create a new one
        if (historyScrollPane == null) {
            historyScrollPane = new JScrollPane(historyPanel);
            historyScrollPane.setBorder(null);
            historyScrollPane.setBounds(30, 150, 290, 450);
            add(historyScrollPane);
        } else {
            // Just update the viewport with the new history panel
            historyScrollPane.setViewportView(historyPanel);
        }
    }
    
    private void loadHistoryEntries() {
        // Get current user's history
        currentUsername = cephra.CephraDB.getCurrentUsername();
        refreshHistoryDisplay();
    }
    
    public void refreshHistoryDisplay() {
        // Clear existing history items
        historyPanel.removeAll();
        
        // Get current user's history (now includes admin history entries)
        List<UserHistoryManager.HistoryEntry> entries = UserHistoryManager.getUserHistory(currentUsername);
        
        if (entries.isEmpty()) {
            // Show "No history" message
            JPanel noHistoryPanel = new JPanel(new BorderLayout());
            noHistoryPanel.setBackground(Color.WHITE);
            JLabel noHistoryLabel = new JLabel("No charging history found");
            noHistoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
            noHistoryLabel.setForeground(Color.GRAY);
            noHistoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noHistoryPanel.add(noHistoryLabel, BorderLayout.CENTER);
            historyPanel.add(noHistoryPanel);
        } else {
            // Add history entries
            for (UserHistoryManager.HistoryEntry entry : entries) {
                addHistoryItem(entry);
            }
        }
        
        // Ensure scroll position is at the top to show newest entries
        SwingUtilities.invokeLater(() -> {
            if (historyScrollPane != null && historyScrollPane.getVerticalScrollBar() != null) {
                historyScrollPane.getVerticalScrollBar().setValue(0);
            }
            historyPanel.revalidate();
            historyPanel.repaint();
        });
    }
    
    @Override
    public void onHistoryUpdated(String username) {
        if (username != null && username.equals(currentUsername)) {
            SwingUtilities.invokeLater(this::refreshHistoryDisplay);
        }
    }
    
    private void addHistoryItem(UserHistoryManager.HistoryEntry entry) {
        // Create a panel for a single history item
        JPanel historyItemPanel = new JPanel();
        historyItemPanel.setLayout(new BorderLayout(5, 0));
        historyItemPanel.setBackground(Color.WHITE);
        historyItemPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        // Set a fixed preferred and maximum height for each history item panel
        int itemHeight = 50; // Smaller fixed height for each item
        int itemWidth = 290; // Width of the scroll pane
        historyItemPanel.setPreferredSize(new Dimension(itemWidth, itemHeight));
        historyItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));

        JLabel dateLabel = new JLabel(entry.getFormattedDate());
        dateLabel.setFont(new Font("Arial", Font.BOLD, 11));
        dateLabel.setForeground(Color.GRAY);

        JPanel timeServiceDetailsPanel = new JPanel();
        timeServiceDetailsPanel.setLayout(new BoxLayout(timeServiceDetailsPanel, BoxLayout.Y_AXIS));
        timeServiceDetailsPanel.setBackground(Color.WHITE);

        JLabel timeLabelItem = new JLabel(entry.getFormattedTime());
        timeLabelItem.setFont(new Font("Arial", Font.PLAIN, 10));
        timeLabelItem.setForeground(Color.DARK_GRAY);
        timeLabelItem.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Clean up service name to remove any price information (like 32.80)
        String serviceType = entry.getServiceType();
        if (serviceType != null && serviceType.contains("32.80")) {
            serviceType = serviceType.replaceAll("\\s*32\\.80\\s*", "");
        }
        
        JLabel serviceInfoLabel = new JLabel("Service: " + serviceType + " | Time: " + entry.getChargingTime());
        serviceInfoLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        serviceInfoLabel.setForeground(Color.DARK_GRAY);
        serviceInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        timeServiceDetailsPanel.add(timeLabelItem);
        timeServiceDetailsPanel.add(serviceInfoLabel);

        JLabel ticketLabel = new JLabel("Ticket: " + entry.getTicketId());
        ticketLabel.setFont(new Font("Arial", Font.BOLD, 10));
        ticketLabel.setForeground(new Color(50, 50, 50));
        ticketLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        historyItemPanel.add(dateLabel, BorderLayout.NORTH);
        historyItemPanel.add(timeServiceDetailsPanel, BorderLayout.WEST);
        historyItemPanel.add(ticketLabel, BorderLayout.EAST);

        historyPanel.add(historyItemPanel);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(new Color(220, 220, 220));
        separator.setPreferredSize(new Dimension(itemWidth, 1));
        historyPanel.add(separator);
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(phonehistory.this);
                    if (window != null) {
                        Point currentLocation = window.getLocation();
                        window.setLocation(
                            currentLocation.x + e.getX() - dragPoint[0].x,
                            currentLocation.y + e.getY() - dragPoint[0].y
                        );
                    }
                }
            }
        });
    }

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        historyScrollPane = new javax.swing.JScrollPane();
        historyPanel = new javax.swing.JPanel();
        profilebutton = new javax.swing.JButton();
        charge = new javax.swing.JButton();
        homebutton = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(350, 750));
        setPreferredSize(new java.awt.Dimension(350, 750));
        setLayout(null);

        historyPanel.setLayout(new javax.swing.BoxLayout(historyPanel, javax.swing.BoxLayout.LINE_AXIS));
        historyScrollPane.setViewportView(historyPanel);

        add(historyScrollPane);
        historyScrollPane.setBounds(10, 170, 320, 180);

        profilebutton.setBorder(null);
        profilebutton.setBorderPainted(false);
        profilebutton.setContentAreaFilled(false);
        profilebutton.setFocusPainted(false);
        profilebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilebuttonActionPerformed(evt);
            }
        });

        charge.setBorder(null);
        charge.setBorderPainted(false);
        charge.setContentAreaFilled(false);
        charge.setFocusPainted(false);
        charge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeActionPerformed(evt);
            }
        });
        add(charge);
        charge.setBounds(30, 680, 50, 50);

        homebutton.setBorder(null);
        homebutton.setBorderPainted(false);
        homebutton.setContentAreaFilled(false);
        homebutton.setFocusPainted(false);
        homebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homebuttonActionPerformed(evt);
            }
        });
        add(homebutton);
        homebutton.setBounds(160, 680, 60, 40);

        linkbutton.setBorder(null);
        linkbutton.setBorderPainted(false);
        linkbutton.setContentAreaFilled(false);
        linkbutton.setFocusPainted(false);
        linkbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkbuttonActionPerformed(evt);
            }
        });
        add(linkbutton);
        linkbutton.setBounds(90, 680, 60, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/HISTORY.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-15, 0, 398, 750);
    }// </editor-fold>//GEN-END:initComponents

    // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(-15, 0, 398, 750);
        }
    }

    private void chargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.serviceoffered());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_chargeActionPerformed

    private void profilebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilebuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Profile());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_profilebuttonActionPerformed

    private void homebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homebuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.home());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_homebuttonActionPerformed

    private void linkbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkbuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.link());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_linkbuttonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton charge;
    private javax.swing.JPanel historyPanel;
    private javax.swing.JScrollPane historyScrollPane;
    private javax.swing.JButton homebutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton linkbutton;
    private javax.swing.JButton profilebutton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition();
                // Ensure background stays behind controls
                if (jLabel1 != null) {
                    setComponentZOrder(jLabel1, getComponentCount() - 1);
                }
            }
        });
    }
    
    @Override
    public void removeNotify() {
        // Unregister as listener when panel is removed
        UserHistoryManager.removeHistoryUpdateListener(this);
        super.removeNotify();
    }
}
