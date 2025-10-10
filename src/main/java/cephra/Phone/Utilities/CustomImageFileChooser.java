package cephra.Phone.Utilities;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.io.*;

public class CustomImageFileChooser {
   
    public static File selectImageFile(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
            "Image Files (*.jpg, *.jpeg, *.png, *.gif, *.bmp, *.webp)", 
            "jpg", "jpeg", "png", "gif", "bmp", "webp"
        );
        fileChooser.setFileFilter(imageFilter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogTitle("Select Image");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        File picturesDir = new File(System.getProperty("user.home"), "Pictures");
        if (picturesDir.exists() && picturesDir.isDirectory()) {
            fileChooser.setCurrentDirectory(picturesDir);
        }
        
        int result = fileChooser.showOpenDialog(parent); 
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            if (!validateImageFile(selectedFile, parent)) {
                return null;
            }        
            return selectedFile;
        }    
        return null;
    }

    private static boolean validateImageFile(File file, Component parent) {
      
        if (!file.exists()) {
            JOptionPane.showMessageDialog(parent, "The selected file does not exist.", 
                "File not found", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(parent, 
                "Please select a valid image file (JPG, PNG, GIF, BMP, or WebP).", 
                "Invalid File Type", JOptionPane.ERROR_MESSAGE);
            return false;
        }      
        return true;
    }
}