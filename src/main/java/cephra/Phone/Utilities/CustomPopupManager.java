package cephra.Phone.Utilities;

import cephra.Phone.Popups.Bay_Number;
import cephra.Phone.Popups.Charge_Now;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Manages custom popup panels (Bay_Number and Charge_Now) 
 for bay assignment and waiting notifications
 */
public class CustomPopupManager {
    
    // Static state management to prevent multiple instances
    private static JPanel currentPopup = null;
    private static boolean isShowing = false;
    private static String currentTicketId = null;
    private static boolean userConfirmed = false;
    private static Runnable currentCallback = null;
    private static volatile boolean isExecutingCallback = false;
    
    // Popup dimensions (centered in phone frame)
    private static final int POPUP_WIDTH = 320;
    private static final int POPUP_HEIGHT = 370;
    private static final int PHONE_WIDTH = 350; // fallback if frame size not yet realized
    private static final int PHONE_HEIGHT = 750; // fallback if frame size not yet realized
    
    /**
     * Shows Bay_Number for when a bay is assigned (informational only)
     * @param ticketId the ticket ID
     * @param bayNumber the bay number
     * @param username the username
     * @return true if user confirmed (OK), false if cancelled
     */
    public static boolean showProceedBayPopup(String ticketId, String bayNumber, String username) {
        return showCustomPopup(ticketId, bayNumber, username, true);
    }
    
    /**
     * Updates the current Bay_Number popup with a new bay number
     * @param bayNumber the new bay number to display
     */
    public static void updateCurrentBayNumber(String bayNumber) {
        if (currentPopup != null && currentPopup instanceof Bay_Number) {
            Bay_Number bayPopup = (Bay_Number) currentPopup;
            setupProceedBayPopupInfo(bayPopup, currentTicketId, bayNumber);
        }
    }
    
    /**
     * Shows Bay_Number popup directly without complex validation (for debugging)
     * @param ticketId the ticket ID
     * @param bayNumber the bay number
     */
    public static void showBayNumberPopupDirect(String ticketId, String bayNumber) {
        System.out.println("showBayNumberPopupDirect: Called with ticketId=" + ticketId + ", bayNumber=" + bayNumber);
        
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
            System.err.println("showBayNumberPopupDirect: Phone frame not found");
            return;
        }
        
        // Create Bay_Number panel
        currentPopup = new Bay_Number();
        setupProceedBayPopupInfo((Bay_Number) currentPopup, ticketId, bayNumber);
        
        // Show the popup
        System.out.println("showBayNumberPopupDirect: About to call showCenteredPopup");
        showCenteredPopup(phoneFrame, currentPopup);
        System.out.println("showBayNumberPopupDirect: showCenteredPopup completed");
        
        currentTicketId = ticketId;
        isShowing = true;
        System.out.println("showBayNumberPopupDirect: Bay_Number popup shown for ticket: " + ticketId);
        System.out.println("showBayNumberPopupDirect: currentTicketId set to: " + currentTicketId);
    }
    
    /**
     * Shows Bay_Number for when a bay is assigned (informational only, no return value needed)
     * @param ticketId the ticket ID
     * @param bayNumber the bay number
     * @param username the username
     */
    public static void showProceedBayPopupInfo(String ticketId, String bayNumber, String username) {
        System.out.println("showProceedBayPopupInfo: Called with ticketId=" + ticketId + ", bayNumber=" + bayNumber + ", username=" + username);
        
        // Validate user is logged in
        if (!cephra.Database.CephraDB.isUserLoggedIn()) {
            System.out.println("showProceedBayPopupInfo: User not logged in, returning");
            return;
        }
        
        // Get and validate current user
        String currentUser = cephra.Database.CephraDB.getCurrentUsername();
        if (currentUser == null || currentUser.trim().isEmpty()) {
            System.out.println("showProceedBayPopupInfo: Current user is null or empty, returning");
            return;
        }
        
        // Validate user matches ticket owner (temporarily disabled for debugging)
        if (!currentUser.trim().equals(username.trim())) {
            System.out.println("showProceedBayPopupInfo: User mismatch - currentUser=" + currentUser + ", username=" + username + ", but continuing anyway for debugging");
            // return; // Temporarily disabled
        }
        
        System.out.println("showProceedBayPopupInfo: All validations passed, proceeding to show popup");
        
        // Prevent duplicate calls for the same ticket
        if (isShowing && ticketId != null && ticketId.equals(currentTicketId)) {
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
        
        // Create Bay_Number panel
        currentPopup = new Bay_Number();
        setupProceedBayPopupInfo((Bay_Number) currentPopup, ticketId, bayNumber);
        
        // Show the popup (informational only, no return value)
        System.out.println("showProceedBayPopupInfo: About to call showCenteredPopup");
        showCenteredPopup(phoneFrame, currentPopup);
        System.out.println("showProceedBayPopupInfo: showCenteredPopup completed");
        
        currentTicketId = ticketId;
        isShowing = true;
        System.out.println("showProceedBayPopupInfo: Bay_Number popup shown for ticket: " + ticketId);
        // Don't set userConfirmed for informational popup
    }
    
    /**
     * Shows Charge_Now for when waiting for bay assignment
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
        
        // Create Charge_Now panel
        currentPopup = new Charge_Now();
        setupWaitingBayPopupWithCallback((Charge_Now) currentPopup, ticketId, callback);
        
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
     * @param isProceedBay true for Bay_Number, false for Charge_Now
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
            currentPopup = new Bay_Number();
            setupProceedBayPopup((Bay_Number) currentPopup, ticketId, bayNumber);
        } else {
            currentPopup = new Charge_Now();
            setupWaitingBayPopup((Charge_Now) currentPopup, ticketId);
        }
        
        // Show the popup
        showCenteredPopup(phoneFrame, currentPopup);
        
        currentTicketId = ticketId;
        isShowing = true;
        userConfirmed = false;
        
        return userConfirmed;
    }
    
    /**
     * Sets up Bay_Number with dynamic content
     */
    private static void setupProceedBayPopup(Bay_Number popup, String ticketId, String bayNumber) {
        // Update bay number display using reflection since field is now private
        try {
            java.lang.reflect.Field bayField = Bay_Number.class.getDeclaredField("BayNumber");
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
            java.lang.reflect.Field ticketField = Bay_Number.class.getDeclaredField("TicketNumber");
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
            java.lang.reflect.Field buttonField = Bay_Number.class.getDeclaredField("OKBTN");
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
     * Sets up Bay_Number with dynamic content (informational only)
     */
    private static void setupProceedBayPopupInfo(Bay_Number popup, String ticketId, String bayNumber) {
        // Update bay number display using reflection since field is now private
        try {
            java.lang.reflect.Field bayField = Bay_Number.class.getDeclaredField("BayNumber");
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
            java.lang.reflect.Field ticketField = Bay_Number.class.getDeclaredField("TicketNumber");
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
            java.lang.reflect.Field buttonField = Bay_Number.class.getDeclaredField("OKBTN");
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
     * Sets up Charge_Now with dynamic content
     */
    private static void setupWaitingBayPopup(Charge_Now popup, String ticketId) {
        // Update ticket number using reflection since field is now private
        try {
            java.lang.reflect.Field ticketField = Charge_Now.class.getDeclaredField("TicketNumber");
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
            java.lang.reflect.Field buttonField = Charge_Now.class.getDeclaredField("OKBTN");
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
     * Sets up Charge_Now with dynamic content and callback
     */
    private static void setupWaitingBayPopupWithCallback(Charge_Now popup, String ticketId, Runnable callback) {
        // Update ticket number using reflection since field is now private
        try {
            java.lang.reflect.Field ticketField = Charge_Now.class.getDeclaredField("TicketNumber");
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
            java.lang.reflect.Field buttonField = Charge_Now.class.getDeclaredField("OKBTN");
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
            // Set popup properties - exactly like Queue_Ticket
            popup.setOpaque(false);
            popup.setPreferredSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
            popup.setSize(POPUP_WIDTH, POPUP_HEIGHT);
            
            // Determine phone content size (fallback to constants if not realized yet)
            int containerW = phoneFrame.getContentPane() != null ? phoneFrame.getContentPane().getWidth() : 0;
            int containerH = phoneFrame.getContentPane() != null ? phoneFrame.getContentPane().getHeight() : 0;
            if (containerW <= 0) containerW = PHONE_WIDTH;
            if (containerH <= 0) containerH = PHONE_HEIGHT;
            
            // Center the popup on the phone frame - exactly like Queue_Ticket
            int x = (containerW - POPUP_WIDTH) / 2;
            int y = (containerH - POPUP_HEIGHT) / 2;
            
            popup.setBounds(x, y, POPUP_WIDTH, POPUP_HEIGHT);
            
            // Disable the background panel - exactly like Queue_Ticket
            Component contentPane = phoneFrame.getContentPane();
            if (contentPane != null) {
                contentPane.setEnabled(false);
                // Disable all child components recursively
                disableAllComponents(contentPane);
            }
            
            // Add to layered pane so it appears on top of current panel - exactly like Queue_Ticket
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
            // Capture a local reference to avoid race conditions - exactly like Queue_Ticket
            JPanel popup = currentPopup;

            SwingUtilities.invokeLater(() -> {
                if (popup.getParent() != null) {
                    popup.getParent().remove(popup);
                }
                
                // Re-enable the background panel - exactly like Queue_Ticket
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
                System.out.println("hideCustomPopup: Clearing currentTicketId from: " + currentTicketId + " to null");
                currentTicketId = null;
                currentCallback = null;
                isExecutingCallback = false;
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
     * Executes the current callback (for Charge_Now)
 This method should be called from the Charge_Now.OKBTNActionPerformed method
 The callback will handle showing Bay_Number and hiding Charge_Now
     */
    public static void executeCallback() {
        // Prevent multiple simultaneous executions
        if (isExecutingCallback) {
            System.out.println("executeCallback: Already executing, ignoring call");
            return;
        }
        
        isExecutingCallback = true;
        try {
            userConfirmed = true;
            
            // Get current user and ticket info BEFORE hiding popup
            String username = null;
            String ticketId = currentTicketId;
            try { 
                username = cephra.Database.CephraDB.getCurrentUsername(); 
                System.out.println("executeCallback: Retrieved username: " + username);
            } catch (Throwable e) {
                System.err.println("executeCallback: Error getting username: " + e.getMessage());
                e.printStackTrace();
            }
            
            System.out.println("executeCallback: ticketId: " + ticketId + ", username: " + username);
            System.out.println("executeCallback: isUserLoggedIn: " + cephra.Database.CephraDB.isUserLoggedIn());
            
            // Hide the current Charge_Now popup but preserve the ticket ID
            System.out.println("executeCallback: Hiding Charge_Now popup");
            System.out.println("executeCallback: currentTicketId before hide: " + currentTicketId);
            
            // Manually hide the popup without clearing currentTicketId
            if (currentPopup != null && isShowing) {
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
                                enableAllComponents(contentPane);
                            }
                            window.repaint();
                            break;
                        }
                    }
                    // Don't clear currentTicketId, currentPopup, isShowing here
                    // They will be set again when we show the Bay_Number popup
                });
                currentPopup = null; // Clear this immediately
                isShowing = false;   // Clear this immediately
                // Keep currentTicketId and currentCallback intact
            }
            
            System.out.println("executeCallback: currentTicketId after manual hide: " + currentTicketId);
            
            // First assign a bay and get the bay number, then show Bay_Number popup
            if (ticketId != null) {
                System.out.println("executeCallback: Attempting bay assignment for ticketId: " + ticketId);
                
                // Try to assign a bay first
                boolean assigned = false;
                String assignedBayNumber = "TBD";
                try {
                    // Find next available bay
                    int availableBay = cephra.Admin.BayManagement.findNextAvailableBay(false); // false for regular charging
                    if (availableBay > 0) {
                        // Assign ticket to the available bay
                        assigned = cephra.Admin.BayManagement.assignTicketToBay(ticketId, username, availableBay);
                        if (assigned) {
                            assignedBayNumber = String.format("%02d", availableBay);
                            System.out.println("executeCallback: Bay assigned successfully - Bay: " + assignedBayNumber);
                        } else {
                            System.out.println("executeCallback: Failed to assign ticket to bay");
                            assignedBayNumber = "Assignment Failed";
                        }
                    } else {
                        System.out.println("executeCallback: No bay available for assignment");
                        assignedBayNumber = "No Bay Available";
                    }
                } catch (Exception e) {
                    System.err.println("executeCallback: Error during bay assignment: " + e.getMessage());
                    e.printStackTrace();
                    assignedBayNumber = "Error";
                }
                
                // Show Bay_Number popup with the actual bay number
                System.out.println("executeCallback: Showing Bay_Number popup with ticketId: " + ticketId + ", bayNumber: " + assignedBayNumber);
                showBayNumberPopupDirect(ticketId, assignedBayNumber);
                System.out.println("executeCallback: Bay_Number popup should now be visible with bay: " + assignedBayNumber);
                
            } else {
                System.err.println("executeCallback: Warning - Could not show Bay_Number popup - missing ticketId");
                System.err.println("executeCallback: ticketId: " + ticketId + ", username: " + username);
            }
            
            // Note: We don't execute the callback here anymore
            // The Bay_Number OK button will handle the charging transition
            
        } finally {
            isExecutingCallback = false;
            System.out.println("executeCallback: Finished execution");
        }
    }
    
    /**
     * Hides the current popup (for informational popups like Bay_Number)
 This method should be called from the Bay_Number.OKBTNActionPerformed method
     */
    public static void hideCurrentPopup() {
        hideCustomPopup();
    }
    
    /**
     * Cancels the current callback (for Charge_Now cancel button)
     * This prevents any further processing when user cancels
     */
    public static void cancelCurrentCallback() {
        currentCallback = null;
        userConfirmed = false;
        isExecutingCallback = false; // Reset execution flag
    }
    
    /**
     * Gets the current ticket ID for the active popup
     * @return the current ticket ID, or null if no popup is active
     */
    public static String getCurrentTicketId() {
        System.out.println("getCurrentTicketId: Returning: " + currentTicketId);
        return currentTicketId;
    }
    
    /**
     * Executes the bay assignment callback that was originally in the Charge_Now flow
     * This method handles bay assignment and charging transition
     * @param ticketId the ticket ID to process
     */
    public static void executeBayAssignmentCallback(String ticketId) {
        try {
            System.out.println("executeBayAssignmentCallback: Processing ticketId: " + ticketId);
            
            // Update database status to Charging
            try {
                System.out.println("executeBayAssignmentCallback: Updating ticket status to Charging");
                boolean dbUpdated = cephra.Database.CephraDB.updateQueueTicketStatus(ticketId, "Charging");
                System.out.println("executeBayAssignmentCallback: Database update result: " + dbUpdated);
            } catch (Exception ex) {
                System.err.println("executeBayAssignmentCallback: Error updating database status: " + ex.getMessage());
                ex.printStackTrace();
            }
            
            // Note: Admin queue table update is handled by the original callback from Queue.java
            
            // Execute the original callback if available (for additional processing)
            if (currentCallback != null) {
                System.out.println("executeBayAssignmentCallback: Executing original callback");
                currentCallback.run();
                System.out.println("executeBayAssignmentCallback: Original callback completed");
            } else {
                System.out.println("executeBayAssignmentCallback: No original callback available");
            }
            
            // Hide the popup
            System.out.println("executeBayAssignmentCallback: Hiding Bay_Number popup");
            hideCustomPopup();
            System.out.println("executeBayAssignmentCallback: Popup hidden successfully");
            
        } catch (Exception ex) {
            System.err.println("executeBayAssignmentCallback error: " + ex.getMessage());
            ex.printStackTrace();
            // Ensure popup is hidden even if there's an error
            hideCustomPopup();
        }
    }
    
}
