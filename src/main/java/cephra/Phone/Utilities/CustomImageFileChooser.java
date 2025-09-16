package cephra.Phone.Utilities;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom file chooser with enhanced UI for image selection
 */
public class CustomImageFileChooser {
    
    /**
     * Opens a customized file chooser dialog for image selection
     * 
     * @param parent The parent component for the dialog
     * @return File object of selected image, or null if cancelled
     */
    public static File selectImageFile(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        
        // Customize the file chooser appearance
        customizeFileChooser(fileChooser);
        
        // Set up file filter for images only
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
            "Image Files (*.jpg, *.jpeg, *.png, *.gif, *.bmp, *.webp)", 
            "jpg", "jpeg", "png", "gif", "bmp", "webp"
        );
        fileChooser.setFileFilter(imageFilter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        // Set dialog properties
        fileChooser.setDialogTitle("📸 Select Your Profile Picture");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // Set initial directory to Pictures folder if it exists
        File picturesDir = new File(System.getProperty("user.home"), "Pictures");
        if (picturesDir.exists() && picturesDir.isDirectory()) {
            fileChooser.setCurrentDirectory(picturesDir);
        }
        
        // Add custom file view for better image preview
        fileChooser.setFileView(new ImageFileView());
        
        // Customize buttons
        customizeButtons(fileChooser);
        
        int result = fileChooser.showOpenDialog(parent);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            // Enhanced file validation
            if (!validateImageFile(selectedFile, parent)) {
                return null;
            }
            
            return selectedFile;
        }
        
        return null;
    }
    
    /**
     * Customizes the file chooser appearance
     */
    private static void customizeFileChooser(JFileChooser fileChooser) {
        try {
            // Customize colors
            UIManager.put("FileChooser.background", new Color(248, 249, 250));
            UIManager.put("FileChooser.listViewBackground", Color.WHITE);
            UIManager.put("FileChooser.detailsViewBackground", Color.WHITE);
            
            // Set custom font
            Font customFont = new Font("Segoe UI", Font.PLAIN, 12);
            UIManager.put("FileChooser.font", customFont);
            
        } catch (Exception e) {
            System.err.println("Could not set custom look and feel: " + e.getMessage());
        }
    }
    
    /**
     * Customizes the file chooser buttons
     */
    private static void customizeButtons(JFileChooser fileChooser) {
        // Get the approve button and customize it
        JButton approveButton = null;
        JButton cancelButton = null;
        
        // Find buttons in the file chooser
        for (Component comp : fileChooser.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component subComp : panel.getComponents()) {
                    if (subComp instanceof JButton) {
                        JButton button = (JButton) subComp;
                        String text = button.getText();
                        if (text != null) {
                            if (text.toLowerCase().contains("open") || text.toLowerCase().contains("select")) {
                                approveButton = button;
                            } else if (text.toLowerCase().contains("cancel")) {
                                cancelButton = button;
                            }
                        }
                    }
                }
            }
        }
        
        // Customize approve button
        if (approveButton != null) {
            approveButton.setText("✓ Select Image");
            approveButton.setBackground(new Color(34, 197, 94));
            approveButton.setForeground(Color.WHITE);
            approveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            approveButton.setFocusPainted(false);
        }
        
        // Customize cancel button
        if (cancelButton != null) {
            cancelButton.setText("✗ Cancel");
            cancelButton.setBackground(new Color(239, 68, 68));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            cancelButton.setFocusPainted(false);
        }
    }
    
    /**
     * Enhanced file validation
     */
    private static boolean validateImageFile(File file, Component parent) {
        // Check if file exists
        if (!file.exists()) {
            showErrorMessage(parent, "File not found", "The selected file does not exist.");
            return false;
        }
        
        // Check file size (max 10MB for better quality)
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (file.length() > maxSize) {
            showErrorMessage(parent, "File Too Large", 
                "Image file is too large. Please select an image smaller than 10MB.");
            return false;
        }
        
        // Check minimum file size (at least 1KB)
        if (file.length() < 1024) {
            showErrorMessage(parent, "File Too Small", 
                "Image file appears to be corrupted or too small.");
            return false;
        }
        
        // Check file extension
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
            showErrorMessage(parent, "Invalid File Type", 
                "Please select a valid image file (JPG, PNG, GIF, BMP, or WebP).");
            return false;
        }
        
        return true;
    }
    
    /**
     * Shows a styled error message
     */
    private static void showErrorMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Custom file view for better image file display
     */
    private static class ImageFileView extends FileView {
        private final Map<String, Icon> iconCache = new HashMap<>();
        
        @Override
        public Icon getIcon(File f) {
            String fileName = f.getName().toLowerCase();
            
            // Return cached icon if available
            if (iconCache.containsKey(fileName)) {
                return iconCache.get(fileName);
            }
            
            // Create custom icon based on file extension
            Icon icon = createFileIcon(fileName);
            iconCache.put(fileName, icon);
            
            return icon;
        }
        
        private Icon createFileIcon(String fileName) {
            // Create a simple colored icon based on file type
            if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                return createColoredIcon(Color.RED, "JPG");
            } else if (fileName.endsWith(".png")) {
                return createColoredIcon(Color.BLUE, "PNG");
            } else if (fileName.endsWith(".gif")) {
                return createColoredIcon(Color.GREEN, "GIF");
            } else if (fileName.endsWith(".bmp")) {
                return createColoredIcon(Color.ORANGE, "BMP");
            } else if (fileName.endsWith(".webp")) {
                return createColoredIcon(Color.MAGENTA, "WEBP");
            }
            
            return UIManager.getIcon("FileView.fileIcon");
        }
        
        private Icon createColoredIcon(Color color, String text) {
            return new Icon() {
                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Draw colored background
                    g2d.setColor(color);
                    g2d.fillRoundRect(x, y, 16, 16, 3, 3);
                    
                    // Draw text
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.BOLD, 8));
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getHeight();
                    g2d.drawString(text, x + (16 - textWidth) / 2, y + (16 + textHeight) / 2 - 2);
                    
                    g2d.dispose();
                }
                
                @Override
                public int getIconWidth() { return 16; }
                
                @Override
                public int getIconHeight() { return 16; }
            };
        }
    }
}
