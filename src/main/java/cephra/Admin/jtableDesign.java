package cephra.Admin;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class jtableDesign {
    
    public static void apply(JTable table) {
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(new Color(50, 50, 50));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(6, 163, 185));
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        header.setOpaque(false);
        header.setForeground(Color.BLACK);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(245, 245, 245));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });

        table.setBorder(BorderFactory.createEmptyBorder());
    }
    
}
