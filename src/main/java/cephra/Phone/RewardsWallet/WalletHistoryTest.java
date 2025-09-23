package cephra.Phone.RewardsWallet;

import javax.swing.*;

public class WalletHistoryTest {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel
                // Set default look and feel - comment out if not available
                // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Create test frame
            JFrame frame = new JFrame("Wallet History Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 800);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            
            // Create and add WalletHistory panel
            WalletHistory walletHistory = new WalletHistory();
            frame.add(walletHistory);
            
            // Show the frame
            frame.setVisible(true);
            
            System.out.println("WalletHistory Test Frame Created Successfully!");
            System.out.println("The panel now uses ChargeHistory-style logic with:");
            System.out.println("- Template-based transaction item creation");
            System.out.println("- Professional detail panel with modal overlay");
            System.out.println("- Improved scrolling and layout");
            System.out.println("- Sample transaction data for testing");
        });
    }
}
