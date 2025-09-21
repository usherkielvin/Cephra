package cephra.Admin.Utilities;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonHoverEffect {

    // Define the hover color: RGB(4, 167, 182)
    private static final Color HOVER_COLOR = new Color(4, 167, 182);

    public static void addHoverEffect(JButton button) {
        // Store the original text color
        Color originalColor = button.getForeground();

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Change text color to the specified hover color
                button.setForeground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Restore original text color
                button.setForeground(originalColor);
            }
        });
    }
}
