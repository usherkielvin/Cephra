
package cephra.Phone.Utilities;



import javax.swing.*;
import java.awt.*;

public class DetailLabelResizer {

    public static void resizeLabelsInPanel(JPanel detailpanel) {
        if (detailpanel == null) return;

        for (Component comp : detailpanel.getComponents()) {
            if (comp instanceof JLabel label) {
                if ("ICON".equals(label.getName())) continue; // skip icons
                resizeLabel(label);
            }
        }

        detailpanel.revalidate();
        detailpanel.repaint();
    }

    private static void resizeLabel(JLabel label) {
        String text = label.getText();
        if (text == null) text = "";

        FontMetrics fm = label.getFontMetrics(label.getFont());
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int width = Math.max(120, textWidth + 5);
        int height = Math.max(20, textHeight); // make sure height is not zero

        int x = label.getX();
        int y = label.getY();

        label.setBounds(x, y, width, height);
    }
}