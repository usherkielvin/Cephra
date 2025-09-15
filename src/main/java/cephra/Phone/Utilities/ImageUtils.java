package cephra.Phone.Utilities;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.Base64;

/**
 * Utility class for handling image operations including file selection,
 * cropping, and base64 conversion for profile pictures.
 */
public class ImageUtils {
    
    /**
     * Opens a file chooser dialog to select an image file.
     * Only accepts common image formats: JPG, JPEG, PNG, GIF
     * 
     * @param parent The parent component for the dialog
     * @return File object of selected image, or null if cancelled
     */
    public static File selectImageFile(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        
        // Set up file filter for images only
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
            "Image Files (*.jpg, *.jpeg, *.png, *.gif)", 
            "jpg", "jpeg", "png", "gif"
        );
        fileChooser.setFileFilter(imageFilter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        // Set dialog properties
        fileChooser.setDialogTitle("Select Profile Picture");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int result = fileChooser.showOpenDialog(parent);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            // Validate file size (max 5MB)
            if (selectedFile.length() > 5 * 1024 * 1024) {
                JOptionPane.showMessageDialog(parent, 
                    "Image file is too large. Please select an image smaller than 5MB.", 
                    "File Too Large", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            
            return selectedFile;
        }
        
        return null;
    }
    
    /**
     * Opens the image cropping dialog for the selected image file.
     * 
     * @param parent The parent component for the dialog
     * @param imageFile The image file to crop
     * @return BufferedImage of the cropped result, or null if cancelled
     */
    public static BufferedImage openCropDialog(Component parent, File imageFile) {
        try {
            BufferedImage originalImage = ImageIO.read(imageFile);
            if (originalImage == null) {
                JOptionPane.showMessageDialog(parent, 
                    "Unable to read the selected image file.", 
                    "Invalid Image", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            
            // Open the crop dialog
            ImageCropDialog cropDialog = new ImageCropDialog(
                (Window) SwingUtilities.getWindowAncestor(parent), originalImage);
            cropDialog.setVisible(true);
            
            return cropDialog.getCroppedImage();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, 
                "Error reading image file: " + e.getMessage(), 
                "Image Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    /**
     * Converts a BufferedImage to a Base64 encoded string for database storage.
     * 
     * @param image The image to convert
     * @param format The image format (png, jpg, etc.)
     * @return Base64 encoded string of the image
     */
    public static String imageToBase64(BufferedImage image, String format) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, format, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            System.err.println("Error converting image to Base64: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Converts a Base64 encoded string back to a BufferedImage.
     * 
     * @param base64String The Base64 encoded image string
     * @return BufferedImage object, or null if conversion fails
     */
    public static BufferedImage base64ToImage(String base64String) {
        try {
            if (base64String == null || base64String.trim().isEmpty()) {
                return null;
            }
            
            byte[] imageBytes = Base64.getDecoder().decode(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            return ImageIO.read(bais);
            
        } catch (Exception e) {
            System.err.println("Error converting Base64 to image: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Resizes an image to fit within the specified dimensions while maintaining aspect ratio.
     * 
     * @param image The original image
     * @param maxWidth Maximum width
     * @param maxHeight Maximum height
     * @return Resized BufferedImage
     */
    public static BufferedImage resizeImage(BufferedImage image, int maxWidth, int maxHeight) {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        
        // Calculate new dimensions maintaining aspect ratio
        double aspectRatio = (double) originalWidth / originalHeight;
        int newWidth = maxWidth;
        int newHeight = maxHeight;
        
        if (originalWidth > originalHeight) {
            newHeight = (int) (maxWidth / aspectRatio);
        } else {
            newWidth = (int) (maxHeight * aspectRatio);
        }
        
        // Ensure we don't exceed the max dimensions
        if (newWidth > maxWidth) {
            newWidth = maxWidth;
            newHeight = (int) (maxWidth / aspectRatio);
        }
        if (newHeight > maxHeight) {
            newHeight = maxHeight;
            newWidth = (int) (maxHeight * aspectRatio);
        }
        
        // Create the resized image
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        
        return resizedImage;
    }
    
    /**
     * Creates a circular profile picture from a square image.
     * 
     * @param image The source image (should be square)
     * @param size The diameter of the circular image
     * @return BufferedImage with circular mask applied
     */
    public static BufferedImage createCircularImage(BufferedImage image, int size) {
        // First resize to square
        BufferedImage squareImage = resizeToSquare(image, size);
        
        // Create circular mask
        BufferedImage circularImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = circularImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw circular clip
        g2d.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
        g2d.drawImage(squareImage, 0, 0, null);
        g2d.dispose();
        
        return circularImage;
    }
    
    /**
     * Resizes an image to a square format.
     * 
     * @param image The original image
     * @param size The size of the square
     * @return Square BufferedImage
     */
    private static BufferedImage resizeToSquare(BufferedImage image, int size) {
        BufferedImage squareImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = squareImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        // Calculate dimensions to center the image in the square
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        int minDimension = Math.min(originalWidth, originalHeight);
        
        // Crop to square from center
        int x = (originalWidth - minDimension) / 2;
        int y = (originalHeight - minDimension) / 2;
        
        g2d.drawImage(image.getSubimage(x, y, minDimension, minDimension), 
                      0, 0, size, size, null);
        g2d.dispose();
        
        return squareImage;
    }
}
