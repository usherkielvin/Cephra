package cephra.Phone.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;

/**
 * Ultra-simple drag and drop zone that can be embedded anywhere
 * Just drag an image file and it calls your callback function
 */
public class SimpleImageDropZone extends JPanel {
    
    private Consumer<File> onImageSelected;
    private JLabel dropLabel;
    private boolean isDragOver = false;
    
    public SimpleImageDropZone(Consumer<File> onImageSelected) {
        this.onImageSelected = onImageSelected;
        initComponents();
        setupDragAndDrop();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 150));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        setBackground(new Color(250, 250, 250));
        
        dropLabel = new JLabel("<html><center>📁<br><br><b>Drop Image Here</b><br><br>or click to browse</center></html>");
        dropLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dropLabel.setForeground(Color.GRAY);
        dropLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dropLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        add(dropLabel, BorderLayout.CENTER);
        
        // Add click listener
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                openFileChooser();
            }
        });
    }
    
    private void setupDragAndDrop() {
        new DropTarget(this, new DropTargetAdapter() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                isDragOver = true;
                updateAppearance();
            }
            
            @Override
            public void dragExit(DropTargetEvent dtde) {
                isDragOver = false;
                updateAppearance();
            }
            
            @Override
            public void drop(DropTargetDropEvent dtde) {
                isDragOver = false;
                updateAppearance();
                
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    @SuppressWarnings("unchecked")
                    List<File> files = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    
                    if (files != null && !files.isEmpty()) {
                        File file = files.get(0);
                        if (isValidImageFile(file)) {
                            onImageSelected.accept(file);
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(SimpleImageDropZone.this,
                        "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private void updateAppearance() {
        if (isDragOver) {
            setBackground(new Color(230, 245, 255));
            setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 3));
            dropLabel.setText("<html><center>📁<br><br><b>Drop Now!</b><br><br>Release to select</center></html>");
            dropLabel.setForeground(new Color(70, 130, 180));
        } else {
            setBackground(new Color(250, 250, 250));
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
            dropLabel.setText("<html><center>📁<br><br><b>Drop Image Here</b><br><br>or click to browse</center></html>");
            dropLabel.setForeground(Color.GRAY);
        }
    }
    
    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Image");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        javax.swing.filechooser.FileNameExtensionFilter imageFilter = 
            new javax.swing.filechooser.FileNameExtensionFilter(
                "Image Files (*.jpg, *.jpeg, *.png, *.gif, *.bmp, *.webp)",
                "jpg", "jpeg", "png", "gif", "bmp", "webp"
            );
        fileChooser.setFileFilter(imageFilter);
        
        File picturesDir = new File(System.getProperty("user.home"), "Pictures");
        if (picturesDir.exists() && picturesDir.isDirectory()) {
            fileChooser.setCurrentDirectory(picturesDir);
        }
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (isValidImageFile(file)) {
                onImageSelected.accept(file);
            }
        }
    }
    
    private boolean isValidImageFile(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }
        
        String fileName = file.getName().toLowerCase();
        String[] validExtensions = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};
        
        for (String ext : validExtensions) {
            if (fileName.endsWith("." + ext)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Create a simple drop zone and add it to your panel
     * @param parentPanel The panel to add the drop zone to
     * @param onImageSelected Callback function when image is selected
     */
    public static void addToPanel(JPanel parentPanel, Consumer<File> onImageSelected) {
        SimpleImageDropZone dropZone = new SimpleImageDropZone(onImageSelected);
        parentPanel.add(dropZone);
    }
}
