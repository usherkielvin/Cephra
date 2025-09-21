package cephra.Admin.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ToggleSwitch extends JComponent {

    private boolean selected;
    private float knobPosition;
    private float targetPosition;
    private final Timer animationTimer;

    public ToggleSwitch() {
        setPreferredSize(new Dimension(60, 30));
        setSize(60, 30);
        setOpaque(true);

        knobPosition = 0f;
        targetPosition = 0f;

        animationTimer = new Timer(15, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stepAnimation();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleSelected();
            }
        });

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    toggleSelected();
                }
            }
        });
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        setSelected(selected, true);
    }

    public void setSelected(boolean selected, boolean animate) {
        boolean old = this.selected;
        this.selected = selected;
        firePropertyChange("selected", old, this.selected);
        targetPosition = this.selected ? 1f : 0f;
        if (animate) {
            if (!animationTimer.isRunning()) {
                animationTimer.start();
            }
        } else {
            knobPosition = targetPosition;
            repaint();
        }
    }

    private void toggleSelected() {
        setSelected(!selected, true);
    }

    private void stepAnimation() {
        float speed = 0.18f; // higher = faster
        if (Math.abs(knobPosition - targetPosition) < 0.01f) {
            knobPosition = targetPosition;
            animationTimer.stop();
            repaint();
            return;
        }
        knobPosition += (targetPosition - knobPosition) * speed;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("ToggleSwitch paintComponent called, selected: " + selected);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int arc = height; // full rounded
            int padding = Math.max(2, Math.round(height * 0.08f));

            // Track
            Color trackColor = selected ? new Color(76, 217, 100) : new Color(200, 200, 200);
            g2.setColor(trackColor);
            g2.fillRoundRect(0, 0, width, height, arc, arc);

            // Optional subtle border
            g2.setColor(new Color(0, 0, 0, 25));
            g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);

            // Knob
            int knobDiameter = height - padding * 2;
            int knobMinX = padding;
            int knobMaxX = width - padding - knobDiameter;
            int knobX = Math.round(knobMinX + (knobMaxX - knobMinX) * knobPosition);
            int knobY = padding;

            // Knob shadow
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillOval(knobX, knobY + 1, knobDiameter, knobDiameter);

            // Knob face
            g2.setColor(Color.WHITE);
            g2.fillOval(knobX, knobY, knobDiameter, knobDiameter);

            // Knob outline
            g2.setColor(new Color(0, 0, 0, 40));
            g2.drawOval(knobX, knobY, knobDiameter, knobDiameter);

        } finally {
            g2.dispose();
        }
    }
}


