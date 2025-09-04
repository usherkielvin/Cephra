package cephra.Admin;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class jtableDesign {
    
    public static void apply(JTable table) {
        table.setRowHeight(30);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(200, 200, 200)); // Light gray horizontal lines
        table.setIntercellSpacing(new Dimension(0, 1));

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(new Color(50, 50, 50));
        table.setOpaque(false); // make table transparent
        table.setBackground(new Color(0, 0, 0, 0)); // fully transparent background
        table.setSelectionBackground(new Color(6, 163, 185));
        table.setSelectionForeground(Color.WHITE);

        // Keep header opaque and with normal background
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        header.setOpaque(true);
        header.setBackground(new Color(240, 240, 240)); // light gray or your preferred color
        header.setForeground(Color.BLACK);

        // Cell renderer to make cells transparent except when selected
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (c instanceof JLabel) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                    }
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    ((JComponent) c).setOpaque(true);
                } else {
                   if (value != null && !value.toString().trim().isEmpty()) {
                    c.setBackground(Color.WHITE);
                    ((JComponent) c).setOpaque(true);
                    } else {
                       ((JComponent) c).setOpaque(false);
                       c.setBackground(new Color(0,0,0));
                   }
                }
                return c;
            }
        });

        table.setBorder(BorderFactory.createEmptyBorder());
    }

    // Call this method if your JTable is inside a JScrollPane to make viewport transparent
    public static void makeScrollPaneTransparent(JScrollPane scrollPane) {
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
    }
}



