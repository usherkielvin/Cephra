package cephra.Phone.Utilities;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Modern, beautiful file chooser with enhanced UI for image selection
 */
public class ModernImageFileChooser {
    
    /**
     * Opens a modern, styled file chooser dialog for image selection
     * 
     * @param parent The parent component for the dialog
     * @return File object of selected image, or null if cancelled
     */
    public static File selectImageFile(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        
        // Apply modern styling
        applyModernStyling(fileChooser);
        
        // Set up comprehensive file filter for images only
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
            "🖼️ Image Files (*.jpg, *.jpeg, *.png, *.gif, *.bmp, *.webp)", 
            "jpg", "jpeg", "png", "gif", "bmp", "webp"
        );
        fileChooser.setFileFilter(imageFilter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        // Set dialog properties
        fileChooser.setDialogTitle("📸 Select Your Profile Picture");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // Set initial directory to Pictures folder
        File picturesDir = new File(System.getProperty("user.home"), "Pictures");
        if (picturesDir.exists() && picturesDir.isDirectory()) {
            fileChooser.setCurrentDirectory(picturesDir);
        }
        
        // Add custom file view with modern styling
        fileChooser.setFileView(new ModernImageFileView());
        
        // Customize buttons with modern styling
        customizeModernButtons(fileChooser);
        
        int result = fileChooser.showOpenDialog(parent);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            // Enhanced validation
            if (!validateImageFile(selectedFile, parent)) {
                return null;
            }
            
            return selectedFile;
        }
        
        return null;
    }
    
    /**
     * Applies modern styling to the file chooser
     */
    private static void applyModernStyling(JFileChooser fileChooser) {
        try {
            // Modern color scheme
            UIManager.put("FileChooser.background", new Color(248, 250, 252));
            UIManager.put("FileChooser.listViewBackground", Color.WHITE);
            UIManager.put("FileChooser.detailsViewBackground", Color.WHITE);
            UIManager.put("FileChooser.listViewBorder", BorderFactory.createLineBorder(new Color(229, 231, 235)));
            UIManager.put("FileChooser.detailsViewBorder", BorderFactory.createLineBorder(new Color(229, 231, 235)));
            
            // Modern fonts
            Font modernFont = new Font("Segoe UI", Font.PLAIN, 13);
            UIManager.put("FileChooser.font", modernFont);
            UIManager.put("FileChooser.listFont", modernFont);
            UIManager.put("FileChooser.detailsFont", modernFont);
            
            // Button styling
            UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 12));
            UIManager.put("Button.background", new Color(59, 130, 246));
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.border", BorderFactory.createEmptyBorder(8, 16, 8, 16));
            
            // ComboBox styling
            UIManager.put("ComboBox.font", modernFont);
            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("ComboBox.border", BorderFactory.createLineBorder(new Color(209, 213, 219)));
            
            // TextField styling
            UIManager.put("TextField.font", modernFont);
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("TextField.border", BorderFactory.createLineBorder(new Color(209, 213, 219)));
            
        } catch (Exception e) {
            System.err.println("Could not apply modern styling: " + e.getMessage());
        }
    }
    
    /**
     * Customizes buttons with modern styling
     */
    private static void customizeModernButtons(JFileChooser fileChooser) {
        // Find and customize buttons
        customizeButtonsRecursively(fileChooser);
    }
    
    private static void customizeButtonsRecursively(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                String text = button.getText();
                
                if (text != null) {
                    if (text.toLowerCase().contains("open") || text.toLowerCase().contains("select")) {
                        // Approve button styling
                        button.setText("✓ Select Image");
                        button.setBackground(new Color(34, 197, 94));
                        button.setForeground(Color.WHITE);
                        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
                        button.setFocusPainted(false);
                        button.setBorderPainted(false);
                        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                        
                        // Add hover effect
                        button.addMouseListener(new java.awt.event.MouseAdapter() {
                            @Override
                            public void mouseEntered(java.awt.event.MouseEvent e) {
                                button.setBackground(new Color(22, 163, 74));
                            }
                            
                            @Override
                            public void mouseExited(java.awt.event.MouseEvent e) {
                                button.setBackground(new Color(34, 197, 94));
                            }
                        });
                        
                    } else if (text.toLowerCase().contains("cancel")) {
                        // Cancel button styling
                        button.setText("✗ Cancel");
                        button.setBackground(new Color(239, 68, 68));
                        button.setForeground(Color.WHITE);
                        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
                        button.setFocusPainted(false);
                        button.setBorderPainted(false);
                        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                        
                        // Add hover effect
                        button.addMouseListener(new java.awt.event.MouseAdapter() {
                            @Override
                            public void mouseEntered(java.awt.event.MouseEvent e) {
                                button.setBackground(new Color(220, 38, 38));
                            }
                            
                            @Override
                            public void mouseExited(java.awt.event.MouseEvent e) {
                                button.setBackground(new Color(239, 68, 68));
                            }
                        });
                    }
                }
            } else if (comp instanceof Container) {
                customizeButtonsRecursively((Container) comp);
            }
        }
    }
    
    /**
     * Enhanced file validation
     */
    private static boolean validateImageFile(File file, Component parent) {
        if (!file.exists()) {
            showModernErrorMessage(parent, "❌ File Not Found", 
                "The selected file does not exist.");
            return false;
        }
        
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (file.length() > maxSize) {
            showModernErrorMessage(parent, "📏 File Too Large", 
                "Image file is too large. Please select an image smaller than 10MB.");
            return false;
        }
        
        if (file.length() < 1024) {
            showModernErrorMessage(parent, "⚠️ Invalid File", 
                "Image file appears to be corrupted or too small.");
            return false;
        }
        
        String fileName = file.getName().toLowerCase();
        String[] validExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"};
        boolean validExtension = false;
        for (String ext : validExtensions) {
            if (fileName.endsWith(ext)) {
                validExtension = true;
                break;
            }
        }
        
        if (!validExtension) {
            showModernErrorMessage(parent, "🚫 Invalid File Type", 
                "Please select a valid image file (JPG, PNG, GIF, BMP, or WebP).");
            return false;
        }
        
        return true;
    }
    
    /**
     * Shows a modern styled error message
     */
    private static void showModernErrorMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Modern file view with beautiful icons and styling
     */
    private static class ModernImageFileView extends FileView {
        private final Map<String, Icon> iconCache = new HashMap<>();
        
        @Override
        public Icon getIcon(File f) {
            String fileName = f.getName().toLowerCase();
            
            if (iconCache.containsKey(fileName)) {
                return iconCache.get(fileName);
            }
            
            Icon icon = createModernFileIcon(fileName);
            iconCache.put(fileName, icon);
            
            return icon;
        }
        
        @Override
        public String getName(File f) {
            if (isImageFile(f)) {
                return super.getName(f);
            }
            return null;
        }
        
        @Override
        public String getDescription(File f) {
            if (isImageFile(f)) {
                return super.getDescription(f);
            }
            return null;
        }
        
        @Override
        public Boolean isTraversable(File f) {
            if (f.isDirectory()) {
                return true;
            }
            return isImageFile(f) ? true : null;
        }
        
        private boolean isImageFile(File f) {
            if (f.isDirectory()) {
                return true;
            }
            
            String fileName = f.getName().toLowerCase();
            String[] validExtensions = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};
            for (String ext : validExtensions) {
                if (fileName.endsWith("." + ext)) {
                    return true;
                }
            }
            return false;
        }
        
        private Icon createModernFileIcon(String fileName) {
            if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                return createModernIcon(new Color(239, 68, 68), "JPG", "📷");
            } else if (fileName.endsWith(".png")) {
                return createModernIcon(new Color(59, 130, 246), "PNG", "🖼️");
            } else if (fileName.endsWith(".gif")) {
                return createModernIcon(new Color(34, 197, 94), "GIF", "🎬");
            } else if (fileName.endsWith(".bmp")) {
                return createModernIcon(new Color(245, 158, 11), "BMP", "🖼️");
            } else if (fileName.endsWith(".webp")) {
                return createModernIcon(new Color(168, 85, 247), "WEBP", "🌐");
            }
            
            return UIManager.getIcon("FileView.fileIcon");
        }
        
        private Icon createModernIcon(Color color, String text, String emoji) {
            return new Icon() {
                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Draw modern rounded rectangle background
                    g2d.setColor(color);
                    g2d.fillRoundRect(x, y, 24, 24, 6, 6);
                    
                    // Draw subtle border
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
                    g2d.drawRoundRect(x, y, 24, 24, 6, 6);
                    
                    // Draw emoji or text
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(emoji);
                    int textHeight = fm.getHeight();
                    g2d.drawString(emoji, x + (24 - textWidth) / 2, y + (24 + textHeight) / 2 - 2);
                    
                    g2d.dispose();
                }
                
                @Override
                public int getIconWidth() { return 24; }
                
                @Override
                public int getIconHeight() { return 24; }
            };
        }
    }
}
