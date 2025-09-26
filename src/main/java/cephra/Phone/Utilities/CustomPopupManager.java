package cephra.Phone.Popups;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Manages custom popup panels (ProceedBayPOP and WaitingBayPOP) 
 * to replace the default ProceedToBay dialog
 */
public class CustomPopupManager {
    
    // Static state management to prevent multiple instances
    private static JPanel currentPopup = null;
    private static boolean isShowing = false;
    private static String currentTicketId = null;
    private static boolean userConfirmed = false;
    private static Runnable currentCallback = null;
    
    // Popup dimensions (centered in phone frame)
    private static final int POPUP_WIDTH = 320;
    private static final int POPUP_HEIGHT = 370;
    private static final int PHONE_WIDTH = 350; // fallback if frame size not yet realized
    private static final int PHONE_HEIGHT = 750; // fallback if frame size not yet realized
    
    /**
     * Shows ProceedBayPOP for when a bay is assigned (informational only)
     * @param ticketId the ticket ID
     * @param bayNumber the bay number
     * @param username the username
     * @return true if user confirmed (OK), false if cancelled
     */
    public static boolean showProceedBayPopup(String ticketId, String bayNumber, String username) {
        return showCustomPopup(ticketId, bayNumber, username, true);
    }
    
    /**
     * Shows ProceedBayPOP for when a bay is assigned (informational only, no return value needed)
     * @param ticketId the ticket ID
     * @param bayNumber the bay number
     * @param username the username
     */
    public static void showProceedBayPopupInfo(String ticketId, String bayNumber, String username) {
        // Validate user is logged in
        if (!cephra.Database.CephraDB.isUserLoggedIn()) {
            return;
        }
        
        // Get and validate current user
        String currentUser = cephra.Database.CephraDB.getCurrentUsername();
        if (currentUser == null || currentUser.trim().isEmpty()) {
            return;
        }
        
        // Validate user matches ticket owner
        if (!currentUser.trim().equals(username.trim())) {
            return;
        }
        
        // Hide any existing popup
        if (isShowing && currentPopup != null) {
            hideCustomPopup();
        }
        
        // Find Phone frame
        Window[] windows = Window.getWindows();
        cephra.Frame.Phone phoneFrame = null;
        
        for (Window window : windows) {
            if (window instanceof cephra.Frame.Phone) {
                phoneFrame = (cephra.Frame.Phone) window;
                break;
            }
        }
        
        if (phoneFrame == null) {
            return;
        }
        
        // Create ProceedBayPOP panel
        currentPopup = new ProceedBayPOP();
        setupProceedBayPopupInfo((ProceedBayPOP) currentPopup, ticketId, bayNumber);
        
        // Show the popup (informational only, no return value)
        showCenteredPopup(phoneFrame, currentPopup);
        
        currentTicketId = ticketId;
        isShowing = true;
        // Don't set userConfirmed for informational popup
    }
    
    /**
     * Shows WaitingBayPOP for when waiting for bay assignment
     * @param ticketId the ticket ID
     * @param username the username
     * @param callback callback to execute when user responds
     */
    public static void showWaitingBayPopup(String ticketId, String username, Runnable callback) {
        // Validate user is logged in
        if (!cephra.Database.CephraDB.isUserLoggedIn()) {
            return;
        }
        
        // Get and validate current user
        String currentUser = cephra.Database.CephraDB.getCurrentUsername();
        if (currentUser == null || currentUser.trim().isEmpty()) {
            return;
        }
        
        // Validate user matches ticket owner
        if (!currentUser.trim().equals(username.trim())) {
            return;
        }
        
        // Hide any existing popup
        if (isShowing && currentPopup != null) {
            hideCustomPopup();
        }
        
        // Find Phone frame
        Window[] windows = Window.getWindows();
        cephra.Frame.Phone phoneFrame = null;
        
        for (Window window : windows) {
            if (window instanceof cephra.Frame.Phone) {
                phoneFrame = (cephra.Frame.Phone) window;
                break;
            }
        }
        
        if (phoneFrame == null) {
            return;
        }
        
        // Create WaitingBayPOP panel
        currentPopup = new WaitingBayPOP();
        setupWaitingBayPopupWithCallback((WaitingBayPOP) currentPopup, ticketId, callback);
        
        // Show the popup
        showCenteredPopup(phoneFrame, currentPopup);
        
        currentTicketId = ticketId;
        isShowing = true;
        userConfirmed = false;
    }
    
    /**
     * Internal method to show custom popup
     * @param ticketId the ticket ID
     * @param bayNumber the bay number (or "TBD" for waiting)
     * @param username the username
     * @param isProceedBay true for ProceedBayPOP, false for WaitingBayPOP
     * @return true if user confirmed (OK), false if cancelled
     */
    private static boolean showCustomPopup(String ticketId, String bayNumber, String username, boolean isProceedBay) {
        // Validate user is logged in
        if (!cephra.Database.CephraDB.isUserLoggedIn()) {
            return false;
        }
        
        // Get and validate current user
        String currentUser = cephra.Database.CephraDB.getCurrentUsername();
        if (currentUser == null || currentUser.trim().isEmpty()) {
            return false;
        }
        
        // Validate user matches ticket owner
        if (!currentUser.trim().equals(username.trim())) {
            return false;
        }
        
        // Hide any existing popup
        if (isShowing && currentPopup != null) {
            hideCustomPopup();
        }
        
        // Find Phone frame
        Window[] windows = Window.getWindows();
        cephra.Frame.Phone phoneFrame = null;
        
        for (Window window : windows) {
            if (window instanceof cephra.Frame.Phone) {
                phoneFrame = (cephra.Frame.Phone) window;
                break;
            }
        }
        
        if (phoneFrame == null) {
            return false;
        }
        
        // Create the appropriate popup panel
        if (isProceedBay) {
            currentPopup = new ProceedBayPOP();
            setupProceedBayPopup((ProceedBayPOP) currentPopup, ticketId, bayNumber);
        } else {
            currentPopup = new WaitingBayPOP();
            setupWaitingBayPopup((WaitingBayPOP) currentPopup, ticketId);
        }
        
        // Show the popup
        showCenteredPopup(phoneFrame, currentPopup);
        
        currentTicketId = ticketId;
        isShowing = true;
        userConfirmed = false;
        
        return userConfirmed;
    }
    
    /**
     * Sets up ProceedBayPOP with dynamic content
     */
    private static void setupProceedBayPopup(ProceedBayPOP popup, String ticketId, String bayNumber) {
        // Update bay number display using reflection since field is now private
        try {
            java.lang.reflect.Field bayField = ProceedBayPOP.class.getDeclaredField("BayNumber");
            bayField.setAccessible(true);
            javax.swing.JLabel bayLabel = (javax.swing.JLabel) bayField.get(popup);
            if (bayNumber != null && !bayNumber.equals("TBD")) {
                bayLabel.setText("BAY " + bayNumber);
            } else {
                bayLabel.setText("BAY TBD");
            }
        } catch (Exception e) {
            System.err.println("Error setting bay number: " + e.getMessage());
        }
        
        // Update ticket number using reflection since field is now private
        try {
            java.lang.reflect.Field ticketField = ProceedBayPOP.class.getDeclaredField("TicketNumber");
            ticketField.setAccessible(true);
            javax.swing.JLabel ticketLabel = (javax.swing.JLabel) ticketField.get(popup);
            if (ticketId != null) {
                ticketLabel.setText(ticketId);
            }
        } catch (Exception e) {
            System.err.println("Error setting ticket number: " + e.getMessage());
        }
        
        // Keep button transparent but ensure it's clickable using reflection
        try {
            java.lang.reflect.Field buttonField = ProceedBayPOP.class.getDeclaredField("OKBTN");
            buttonField.setAccessible(true);
            javax.swing.JButton button = (javax.swing.JButton) buttonField.get(popup);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setOpaque(false);
            button.setFocusable(true);
            button.setEnabled(true);
        } catch (Exception e) {
            System.err.println("Error setting button properties: " + e.getMessage());
        }
    }
    
    /**
     * Sets up ProceedBayPOP with dynamic content (informational only)
     */
    private static void setupProceedBayPopupInfo(ProceedBayPOP popup, String ticketId, String bayNumber) {
        // Update bay number display using reflection since field is now private
        try {
            java.lang.reflect.Field bayField = ProceedBayPOP.class.getDeclaredField("BayNumber");
            bayField.setAccessible(true);
            javax.swing.JLabel bayLabel = (javax.swing.JLabel) bayField.get(popup);
            if (bayNumber != null && !bayNumber.equals("TBD")) {
                bayLabel.setText("BAY " + bayNumber);
            } else {
                bayLabel.setText("BAY TBD");
            }
        } catch (Exception e) {
            System.err.println("Error setting bay number: " + e.getMessage());
        }
        
        // Update ticket number using reflection since field is now private
        try {
            java.lang.reflect.Field ticketField = ProceedBayPOP.class.getDeclaredField("TicketNumber");
            ticketField.setAccessible(true);
            javax.swing.JLabel ticketLabel = (javax.swing.JLabel) ticketField.get(popup);
            if (ticketId != null) {
                ticketLabel.setText(ticketId);
            }
        } catch (Exception e) {
            System.err.println("Error setting ticket number: " + e.getMessage());
        }
        
        // Keep button transparent but ensure it's clickable using reflection
        try {
            java.lang.reflect.Field buttonField = ProceedBayPOP.class.getDeclaredField("OKBTN");
            buttonField.setAccessible(true);
            javax.swing.JButton button = (javax.swing.JButton) buttonField.get(popup);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setOpaque(false);
            button.setFocusable(true);
            button.setEnabled(true);
        } catch (Exception e) {
            System.err.println("Error setting button properties: " + e.getMessage());
        }
    }
    
    /**
     * Sets up WaitingBayPOP with dynamic content
     */
    private static void setupWaitingBayPopup(WaitingBayPOP popup, String ticketId) {
        // Update ticket number using reflection since field is now private
        try {
            java.lang.reflect.Field ticketField = WaitingBayPOP.class.getDeclaredField("TicketNumber");
            ticketField.setAccessible(true);
            javax.swing.JLabel ticketLabel = (javax.swing.JLabel) ticketField.get(popup);
            if (ticketId != null) {
                ticketLabel.setText(ticketId);
            }
        } catch (Exception e) {
            System.err.println("Error setting ticket number: " + e.getMessage());
        }
        
        // Keep button transparent but ensure it's clickable using reflection
        try {
            java.lang.reflect.Field buttonField = WaitingBayPOP.class.getDeclaredField("OKBTN");
            buttonField.setAccessible(true);
            javax.swing.JButton button = (javax.swing.JButton) buttonField.get(popup);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setOpaque(false);
            button.setFocusable(true);
            button.setEnabled(true);
        } catch (Exception e) {
            System.err.println("Error setting button properties: " + e.getMessage());
        }
    }
    
    /**
     * Sets up WaitingBayPOP with dynamic content and callback
     */
    private static void setupWaitingBayPopupWithCallback(WaitingBayPOP popup, String ticketId, Runnable callback) {
        // Update ticket number using reflection since field is now private
        try {
            java.lang.reflect.Field ticketField = WaitingBayPOP.class.getDeclaredField("TicketNumber");
            ticketField.setAccessible(true);
            javax.swing.JLabel ticketLabel = (javax.swing.JLabel) ticketField.get(popup);
            if (ticketId != null) {
                ticketLabel.setText(ticketId);
            }
        } catch (Exception e) {
            System.err.println("Error setting ticket number: " + e.getMessage());
        }
        
        // Keep button transparent but ensure it's clickable using reflection
        try {
            java.lang.reflect.Field buttonField = WaitingBayPOP.class.getDeclaredField("OKBTN");
            buttonField.setAccessible(true);
            javax.swing.JButton button = (javax.swing.JButton) buttonField.get(popup);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setOpaque(false);
            button.setFocusable(true);
            button.setEnabled(true);
        } catch (Exception e) {
            System.err.println("Error setting button properties: " + e.getMessage());
        }
        
        // Store callback for the action performed method to use
        currentCallback = callback;
    }
    
    /**
     * Shows popup centered on the Phone frame
     */
    private static void showCenteredPopup(cephra.Frame.Phone phoneFrame, JPanel popup) {
        SwingUtilities.invokeLater(() -> {
            // Set popup properties
            popup.setOpaque(false);
            popup.setPreferredSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
            popup.setSize(POPUP_WIDTH, POPUP_HEIGHT);
            
            // Determine phone content size (fallback to constants if not realized yet)
            int containerW = phoneFrame.getContentPane() != null ? phoneFrame.getContentPane().getWidth() : 0;
            int containerH = phoneFrame.getContentPane() != null ? phoneFrame.getContentPane().getHeight() : 0;
            if (containerW <= 0) containerW = PHONE_WIDTH;
            if (containerH <= 0) containerH = PHONE_HEIGHT;
            
            // Center the popup on the phone frame
            int x = (containerW - POPUP_WIDTH) / 2;
            int y = (containerH - POPUP_HEIGHT) / 2;
            
            popup.setBounds(x, y, POPUP_WIDTH, POPUP_HEIGHT);
            
            // Disable the background panel
            Component contentPane = phoneFrame.getContentPane();
            if (contentPane != null) {
                contentPane.setEnabled(false);
                // Disable all child components recursively
                disableAllComponents(contentPane);
            }
            
            // Add to layered pane so it appears on top of current panel
            JLayeredPane layeredPane = phoneFrame.getRootPane().getLayeredPane();
            layeredPane.add(popup, JLayeredPane.MODAL_LAYER);
            layeredPane.moveToFront(popup);
            
            popup.setVisible(true);
            
            // Setup ESC key support
            setupEscapeKeySupport(popup);
            
            phoneFrame.repaint();
        });
    }
    
    /**
     * Sets up ESC key support for closing popup
     */
    private static void setupEscapeKeySupport(JPanel popup) {
        popup.setFocusable(true);
        popup.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    userConfirmed = false;
                    currentCallback = null; // Clear callback on ESC
                    hideCustomPopup();
                }
            }
        });
        
        // Request focus so key events work
        SwingUtilities.invokeLater(popup::requestFocusInWindow);
    }
    
    /**
     * Hides the custom popup and cleans up resources
     */
    public static void hideCustomPopup() {
        if (currentPopup != null && isShowing) {
            // Capture a local reference to avoid race conditions
            JPanel popup = currentPopup;

            SwingUtilities.invokeLater(() -> {
                if (popup.getParent() != null) {
                    popup.getParent().remove(popup);
                }
                
                // Re-enable the background panel
                for (Window window : Window.getWindows()) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        Component contentPane = phoneFrame.getContentPane();
                        if (contentPane != null) {
                            contentPane.setEnabled(true);
                            // Re-enable all child components recursively
                            enableAllComponents(contentPane);
                        }
                        window.repaint();
                        break;
                    }
                }
                
                currentPopup = null;
                isShowing = false;
                currentTicketId = null;
                currentCallback = null;
            });
        }
    }
    
    // Method to disable all components recursively
    private static void disableAllComponents(Component component) {
        component.setEnabled(false);
        if (component instanceof Container) {
            Container container = (Container) component;
            for (Component child : container.getComponents()) {
                disableAllComponents(child);
            }
        }
    }
    
    // Method to enable all components recursively
    private static void enableAllComponents(Component component) {
        component.setEnabled(true);
        if (component instanceof Container) {
            Container container = (Container) component;
            for (Component child : container.getComponents()) {
                enableAllComponents(child);
            }
        }
    }
    
    /**
     * Checks if a custom popup is currently showing
     * @return true if popup is showing
     */
    public static boolean isPopupShowing() {
        return isShowing;
    }
    
    /**
     * Checks if the popup is showing for a specific ticket
     * @param ticketId the ticket ID to check
     * @return true if showing for this ticket
     */
    public static boolean isShowingForTicket(String ticketId) {
        return isShowing && ticketId != null && ticketId.equals(currentTicketId);
    }
    
    /**
     * Executes the current callback (for WaitingBayPOP)
     * This method should be called from the WaitingBayPOP.OKBTNActionPerformed method
     * The callback will handle showing ProceedBayPOP and hiding WaitingBayPOP
     */
    public static void executeCallback() {
        userConfirmed = true;
        boolean closedByCallback = false;
        String usernameForNext = null;
        String ticketForNext = currentTicketId;
        try { usernameForNext = cephra.Database.CephraDB.getCurrentUsername(); } catch (Throwable ignore) {}
        if (currentCallback != null) {
            try {
                currentCallback.run();
                // After running, check if popup was closed by callback
                closedByCallback = (currentPopup == null || !isShowing);
                // If popup is closed (Waiting hidden) and we don't yet show ProceedBay, try to show with actual assigned bay
                if (!isPopupShowing()) {
                    try {
                        String assignedBay = cephra.Admin.BayManagement.getBayNumberByTicket(ticketForNext);
                        if (assignedBay != null && usernameForNext != null && !usernameForNext.trim().isEmpty()) {
                            showProceedBayPopupInfo(ticketForNext, assignedBay, usernameForNext);
                            closedByCallback = true;
                        }
                    } catch (Throwable ignore) {}
                }
            } catch (Exception ex) {
                System.err.println("executeCallback error: " + ex.getMessage());
            }
        } else {
            // Fallback: no callback provided. Hide WaitingBayPOP and show ProceedBayPOP with TBD bay.
            try {
                String username = usernameForNext;
                String ticketId = ticketForNext;
                hideCustomPopup();
                if (ticketId != null && username != null && !username.trim().isEmpty()) {
                    String assignedBay = null;
                    try { assignedBay = cephra.Admin.BayManagement.getBayNumberByTicket(ticketId); } catch (Throwable ignore) {}
                    showProceedBayPopupInfo(ticketId, (assignedBay != null ? assignedBay : "TBD"), username);
                    closedByCallback = true;
                }
            } catch (Throwable ignore) {}
        }
        // If still showing, hide to ensure button responds
        if (!closedByCallback) {
            hideCustomPopup();
        }
    }
    
    /**
     * Hides the current popup (for informational popups like ProceedBayPOP)
     * This method should be called from the ProceedBayPOP.OKBTNActionPerformed method
     */
    public static void hideCurrentPopup() {
        System.out.println("hideCurrentPopup() called");
        hideCustomPopup();
    }
    
}
