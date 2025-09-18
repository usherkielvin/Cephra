package cephra.Phone.Utilities;

import javax.swing.JLabel;
import java.awt.FontMetrics;

public class BalanceManager {
    private static JLabel rewardLabel;
    private static JLabel pesoLabel;

    // Attach your labels once (from your panel's init)
    public static void setLabels(JLabel reward, JLabel peso) {
        rewardLabel = reward;
        pesoLabel = peso;
    }

    // Call this to update both balances and auto-resize labels
    public static void updateBalances(String reward, String peso) {
        if (rewardLabel != null) {
            updateLabel(rewardLabel, reward);
        }
        if (pesoLabel != null) {
            updateLabel(pesoLabel, peso);
        }
    }

    private static void updateLabel(JLabel label, String text) {
        label.setText(text);
        FontMetrics fm = label.getFontMetrics(label.getFont());
        int width = Math.max(30, fm.stringWidth(text));
        int height = fm.getHeight();
        label.setSize(width, height);
        label.revalidate();
        label.repaint();
    }
}
