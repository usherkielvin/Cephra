package cephra.PhonePanels;

import javax.swing.SwingUtilities;

public class Splashscreen extends javax.swing.JPanel {
   
    public Splashscreen() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
    }
     
      private void navigateToPhoneLogin() {
        SwingUtilities.invokeLater(() -> {
            for (java.awt.Window window : java.awt.Window.getWindows()) {
                if (window instanceof cephra.PhoneFrame phoneFrame) {
                    phoneFrame.switchPanel(new cephra.PhonePanels.Phonelogin());
                    break;
                }
            }
        });
    } 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startbutton = new javax.swing.JButton();
        t = new javax.swing.JLabel();

        setLayout(null);
        
        // Match Register/Phonelogin transparency
        setOpaque(false);
        setBackground(new java.awt.Color(0, 0, 0, 0));

        // Image label configuration (match bounds and size exactly)
        t.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/GET STARTED.png"))); // NOI18N
        t.setPreferredSize(new java.awt.Dimension(400, 814));
        t.setOpaque(false);
        add(t);
        t.setBounds(-14, -32, 400, 814);

        // Invisible overlay button covering the whole image area
        startbutton.setOpaque(false);
        startbutton.setContentAreaFilled(false);
        startbutton.setBorderPainted(false);
        startbutton.setFocusPainted(false);
        startbutton.setText("");
        startbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startbuttonActionPerformed(evt);
            }
        });
        add(startbutton);
        startbutton.setBounds(-14, -32, 400, 814);
    }// </editor-fold>//GEN-END:initComponents

    private void startbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        navigateToPhoneLogin();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton startbutton;
    private javax.swing.JLabel t;
    // End of variables declaration//GEN-END:variables
}
