
package cephra.Phone;

import javax.swing.SwingUtilities;

public class link extends javax.swing.JPanel {

    public link() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
        setupLabelPosition(); // Set label position
        
        // Check if car is already linked and user has a battery level
        if (cephra.Phone.AppState.isCarLinked) {
            try {
                String username = cephra.CephraDB.getCurrentUsername();
                if (username != null && !username.isEmpty()) {
                    int batteryLevel = cephra.CephraDB.getUserBatteryLevel(username);
                    if (batteryLevel != -1) {
                        // Car is linked and battery is initialized - go to Porsche
                        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                java.awt.Window[] windows = java.awt.Window.getWindows();
                                for (java.awt.Window window : windows) {
                                    if (window instanceof cephra.Frame.Phone) {
                                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                                        phoneFrame.switchPanel(new cephra.Phone.PorscheTaycan());
                                        break;
                                    }
                                }
                            }
                        });
                    }
                }
            } catch (Exception e) {
                System.err.println("Error checking battery level in link constructor: " + e.getMessage());
            }
        }
    }

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Linkcar = new javax.swing.JButton();
        charge = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        profilebutton = new javax.swing.JButton();
        homebutton = new javax.swing.JButton();
        termscondition = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(350, 750));
        setPreferredSize(new java.awt.Dimension(350, 750));
        setLayout(null);

        Linkcar.setBorder(null);
        Linkcar.setBorderPainted(false);
        Linkcar.setContentAreaFilled(false);
        Linkcar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LinkcarActionPerformed(evt);
            }
        });
        add(Linkcar);
        Linkcar.setBounds(50, 560, 240, 50);

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
        charge.setBounds(40, 670, 50, 60);

        historybutton.setBorder(null);
        historybutton.setBorderPainted(false);
        historybutton.setContentAreaFilled(false);
        historybutton.setFocusPainted(false);
        historybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historybuttonActionPerformed(evt);
            }
        });
        add(historybutton);
        historybutton.setBounds(230, 653, 50, 100);

        profilebutton.setBorder(null);
        profilebutton.setBorderPainted(false);
        profilebutton.setContentAreaFilled(false);
        profilebutton.setFocusPainted(false);
        profilebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilebuttonActionPerformed(evt);
            }
        });
        add(profilebutton);
        profilebutton.setBounds(290, 660, 80, 70);

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
        homebutton.setBounds(150, 680, 40, 40);

        termscondition.setBackground(new java.awt.Color(255, 255, 255));
        termscondition.setText("By linking, I agree to the Terms & Conditions ");
        termscondition.setBorder(null);
        termscondition.setContentAreaFilled(false);
        termscondition.setFocusPainted(false);
        termscondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                termsconditionActionPerformed(evt);
            }
        });
        add(termscondition);
        termscondition.setBounds(40, 620, 310, 30);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/ConnectCar.gif"))); // NOI18N
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

    private void historybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historybuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.phonehistory());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_historybuttonActionPerformed

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

    private void LinkcarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LinkcarActionPerformed
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Generate and set battery level for current user when linking car
                String username = cephra.CephraDB.getCurrentUsername();
                if (username != null && !username.isEmpty()) {
                            // Initialize battery to random level (15-50%) only if not already initialized
        int currentBattery = cephra.CephraDB.getUserBatteryLevel(username);
        if (currentBattery == -1) {
            // No battery initialized yet - create new random battery
            java.util.Random random = new java.util.Random();
            int batteryLevel = 15 + random.nextInt(36); // 15 to 50
            cephra.CephraDB.setUserBatteryLevel(username, batteryLevel);
            System.out.println("Link: Initialized battery level for " + username + " to " + batteryLevel + "% when linking car");
                } else {
            // Battery already exists - keep the current level
            System.out.println("Link: Battery level for " + username + " already exists: " + currentBattery + "% - keeping current level");
        }
                    
                    // Ensure database update is complete before switching panels
                    try {
                        Thread.sleep(100); // Small delay to ensure database update is complete
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                
                cephra.Phone.AppState.isCarLinked = true;
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        // Go directly to PorscheTaycan instead of serviceoffered
                        phoneFrame.switchPanel(new cephra.Phone.PorscheTaycan());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_LinkcarActionPerformed

    private void termsconditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_termsconditionActionPerformed
        if (termscondition.isSelected()) {
            // Show Terms and Conditions in a dialog
            showTermsAndConditions();
        }
    }//GEN-LAST:event_termsconditionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Linkcar;
    private javax.swing.JButton charge;
    private javax.swing.JButton historybutton;
    private javax.swing.JButton homebutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton profilebutton;
    private javax.swing.JCheckBox termscondition;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition();
            }
        });
    }

    private void showTermsAndConditions() {
        String termsText = getLinkTermsAndConditionsText();

        String safeText = termsText
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
        String html = "<html><head><style>" +
                "body{font-family:'Segoe UI',sans-serif;"
                + "font-size:12px;"
                + "line-height:1.5;"
                + "text-align:justify;margin:0;}" +
                ".container{padding:0 4px;}" +
                "</style></head><body><div class='container'>" +
                safeText.replace("\n", "<br/>") +
                "</div></body></html>";

        javax.swing.JEditorPane editorPane = new javax.swing.JEditorPane("text/html", html);
        editorPane.setEditable(false);
        editorPane.setOpaque(true);
        editorPane.setFocusable(false);
        editorPane.setHighlighter(null);
        editorPane.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        if (editorPane.getCaret() != null) {
            editorPane.getCaret().setVisible(false);
            editorPane.getCaret().setSelectionVisible(false);
        }

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(editorPane);
        scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));
        scrollPane.setBorder(null);
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        java.awt.Window owner = javax.swing.SwingUtilities.getWindowAncestor(this);
        final javax.swing.JDialog dialog = new javax.swing.JDialog(owner, "Terms and Conditions", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(javax.swing.JDialog.DO_NOTHING_ON_CLOSE);

        javax.swing.JPanel content = new javax.swing.JPanel(new java.awt.BorderLayout());
        content.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        content.add(scrollPane, java.awt.BorderLayout.CENTER);

        javax.swing.JButton ok = new javax.swing.JButton("OK");
        ok.setFocusPainted(false);
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialog.dispose();
            }
        });
        javax.swing.JPanel buttons = new javax.swing.JPanel();
        buttons.setBackground(java.awt.Color.WHITE); 
        buttons.add(ok);
        content.add(buttons, java.awt.BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.getContentPane().setBackground(java.awt.Color.WHITE); 
        dialog.setBackground(java.awt.Color.WHITE);
        dialog.setSize(320, 600);
        if (owner != null) {
            dialog.setLocationRelativeTo(owner);
            java.awt.Point current = dialog.getLocation();
            dialog.setLocation(current.x - 3, current.y);
        }
        try {
            editorPane.setCaretPosition(0);
        } catch (Exception ignore) {}
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try { editorPane.setCaretPosition(0); } catch (Exception ignore) {}
                javax.swing.JViewport vp = scrollPane.getViewport();
                if (vp != null) {
                    vp.setViewPosition(new java.awt.Point(0, 0));
                }
            }
        });
        dialog.setVisible(true);
    }

    private String getLinkTermsAndConditionsText() {
        return "CEPHRA EV LINKING TERMS AND CONDITIONS\n" +
               "Effective Date: " + java.time.LocalDate.now() + "\n" +
               "Version: 1.0\n\n" +
               "1. ACCEPTANCE OF TERMS\n" +
               "By linking your electric vehicle (EV) to the Cephra app (the \"Service\"), you agree to these Terms.\n\n" +
               "2. LINKING PURPOSE\n" +
               "Linking enables ticketing, charging session history, and status updates within the app.\n\n" +
               "3. DATA COLLECTED\n" +
               "Vehicle identifiers, session timestamps, kWh consumed, payment references, and diagnostic status necessary for Service delivery.\n\n" +
               "4. USER RESPONSIBILITIES\n" +
               "You confirm you are authorized to link the vehicle and will keep your account secure.\n\n" +
               "5. CONSENT TO COMMUNICATIONS\n" +
               "You consent to in-app notifications and transactional emails about charging and tickets.\n\n" +
               "6. PRIVACY\n" +
               "We process data per our Privacy Policy. Data is retained only as long as needed for the Service.\n\n" +
               "7. LIMITATIONS\n" +
               "The Service provides information \"as is\" and availability may vary by station and network conditions.\n\n" +
               "8. SECURITY\n" +
               "We employ reasonable safeguards, but you acknowledge inherent risks in networked systems.\n\n" +
               "9. UNLINKING\n" +
               "You may unlink your vehicle at any time from the app; some records may be retained for compliance.\n\n" +
               "10. LIABILITY\n" +
               "To the maximum extent permitted by law, Cephra is not liable for indirect or consequential damages.\n\n" +
               "11. GOVERNING LAW\n" +
               "These Terms are governed by the laws of Pasay City, Philippines.\n\n" +
               "12. CONTACT\n" +
               "Cephra Support — support@cephra.com | +63 2 8XXX XXXX\n\n" +
               "By checking \"I agree\", you confirm you have read and accept these Terms.";
    }
}
