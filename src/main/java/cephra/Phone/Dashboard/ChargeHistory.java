package cephra.Phone.Dashboard;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ChargeHistory extends javax.swing.JPanel implements cephra.Phone.Utilities.HistoryManager.HistoryUpdateListener {
    
    
    
   
    private String currentUsername;
    private cephra.Phone.Utilities.HistoryManager.HistoryEntry currentHistoryEntry;

    public ChargeHistory() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        setupLabelPosition(); // Set label position
        makeDraggable();
        
        // Your historyPanel will be created by NetBeans initComponents()
        // No need to create fallback - we'll use your designed panel
        
        // Register as listener for history updates
        cephra.Phone.Utilities.HistoryManager.addHistoryUpdateListener(this);
        
        // Setup custom functionality (design-safe - works with any NetBeans changes!)
        setupCustomCode();
        
        // Setup the scrollable history content similar to rewards
        setupScrollableHistoryContent();
        
        // Add component listener to refresh when panel becomes visible
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshHistoryDisplay();
            }
        });
    }
    
    
    /**
     * Creates an exact copy of your designed panel (works with ANY layout you use!)
     */
    private JPanel clonePanel(JPanel originalPanel) {
        // Create new panel with same properties as your original
        JPanel clonedPanel = new JPanel();
        clonedPanel.setBackground(originalPanel.getBackground());
        clonedPanel.setPreferredSize(originalPanel.getPreferredSize());
        clonedPanel.setMaximumSize(originalPanel.getMaximumSize());
        clonedPanel.setMinimumSize(originalPanel.getMinimumSize());
        clonedPanel.setOpaque(originalPanel.isOpaque());
        
        // Create new components (clones of your original ones)
        JLabel newTime = createClonedLabel(time, "Loading...");
        JLabel newPrice = createClonedLabel(price, "₱0.00");
        JLabel newDate = createClonedLabel(date, "No Date");
        JLabel newType = createClonedLabel(type, "No Service");
        JLabel newChargetime = createClonedLabel(chargetime, "0 mins");
        JButton newDetails = createClonedButton(details, "");
        
        // Store references in the panel for easy access
        clonedPanel.putClientProperty("timeLabel", newTime);
        clonedPanel.putClientProperty("priceLabel", newPrice);
        clonedPanel.putClientProperty("dateLabel", newDate);
        clonedPanel.putClientProperty("typeLabel", newType);
        clonedPanel.putClientProperty("chargetimeLabel", newChargetime);
        clonedPanel.putClientProperty("detailsButton", newDetails);
        
        // Handle ANY layout type you use in NetBeans!
        java.awt.LayoutManager originalLayout = originalPanel.getLayout();
        
        if (originalLayout == null) {
            // NULL LAYOUT (Absolute positioning) - most common custom layout
            clonedPanel.setLayout(null);
            
            // Copy exact positions from your original components
            if (time != null) {
                newTime.setBounds(time.getBounds());
                clonedPanel.add(newTime);
            }
            if (price != null) {
                newPrice.setBounds(price.getBounds());
                clonedPanel.add(newPrice);
            }
            if (date != null) {
                newDate.setBounds(date.getBounds());
                clonedPanel.add(newDate);
            }
            if (type != null) {
                newType.setBounds(type.getBounds());
                clonedPanel.add(newType);
            }
            if (chargetime != null) {
                newChargetime.setBounds(chargetime.getBounds());
                clonedPanel.add(newChargetime);
            }
            if (details != null) {
                newDetails.setBounds(details.getBounds());
                clonedPanel.add(newDetails);
            }
            
            System.out.println("✓ Cloned panel with NULL layout (absolute positioning)");
            
        } else if (originalLayout instanceof javax.swing.GroupLayout) {
            // GROUP LAYOUT - copy the exact layout structure
            javax.swing.GroupLayout clonedLayout = new javax.swing.GroupLayout(clonedPanel);
            clonedPanel.setLayout(clonedLayout);
            
            // Use your current GroupLayout structure (copying from your latest design)
            clonedLayout.setHorizontalGroup(
                clonedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(clonedLayout.createSequentialGroup()
                    .addGroup(clonedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(clonedLayout.createSequentialGroup()
                            .addGap(34, 34, 34)
                            .addGroup(clonedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(newTime)
                                .addComponent(newDate))
                            .addGap(46, 46, 46)
                            .addGroup(clonedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(newPrice)
                                .addComponent(newType)))
                        .addGroup(clonedLayout.createSequentialGroup()
                            .addGap(43, 43, 43)
                            .addComponent(newChargetime)
                            .addGap(18, 18, 18)
                            .addComponent(newDetails)))
                    .addContainerGap(127, Short.MAX_VALUE))
            );
            clonedLayout.setVerticalGroup(
                clonedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(clonedLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(clonedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(newTime)
                        .addComponent(newPrice))
                    .addGroup(clonedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(clonedLayout.createSequentialGroup()
                            .addGap(29, 29, 29)
                            .addComponent(newType))
                        .addGroup(clonedLayout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(newDate)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(clonedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(newChargetime)
                        .addComponent(newDetails))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            
            System.out.println("✓ Cloned panel with GroupLayout");
            
        } else {
            // OTHER LAYOUT TYPES (BorderLayout, FlowLayout, etc.)
            try {
                // Try to create the same layout type
                java.awt.LayoutManager clonedLayout = originalLayout.getClass().newInstance();
                clonedPanel.setLayout(clonedLayout);
                
                // Add components (positions will depend on the layout type)
                clonedPanel.add(newTime);
                clonedPanel.add(newPrice);
                clonedPanel.add(newDate);
                clonedPanel.add(newType);
                clonedPanel.add(newChargetime);
                clonedPanel.add(newDetails);
                
                System.out.println("✓ Cloned panel with " + originalLayout.getClass().getSimpleName());
                
            } catch (Exception e) {
                // Fallback to null layout if we can't clone the layout
                System.out.println("Could not clone layout, using absolute positioning");
                clonedPanel.setLayout(null);
                
                // Try to get positions if possible
                if (time != null && time.getBounds() != null) {
                    newTime.setBounds(time.getBounds());
                    clonedPanel.add(newTime);
                }
                // Add other components with default positions if bounds not available
                if (newTime.getBounds().equals(new java.awt.Rectangle())) {
                    newTime.setBounds(10, 10, 100, 20);
                    newPrice.setBounds(120, 10, 100, 20);
                    newDate.setBounds(10, 35, 100, 20);
                    newType.setBounds(120, 35, 100, 20);
                    newChargetime.setBounds(10, 60, 100, 20);
                    newDetails.setBounds(120, 60, 80, 25);
                }
                clonedPanel.add(newPrice);
                clonedPanel.add(newDate);
                clonedPanel.add(newType);
                clonedPanel.add(newChargetime);
                clonedPanel.add(newDetails);
            }
        }
        
        return clonedPanel;
    }
    
    /**
     * Creates a cloned label with same properties as original
     */
    private JLabel createClonedLabel(JLabel original, String defaultText) {
        JLabel cloned = new JLabel(defaultText);
        if (original != null) {
            cloned.setFont(original.getFont());
            cloned.setForeground(original.getForeground());
            cloned.setBackground(original.getBackground());
            cloned.setOpaque(original.isOpaque());
            cloned.setHorizontalAlignment(original.getHorizontalAlignment());
            cloned.setVerticalAlignment(original.getVerticalAlignment());
        }
        return cloned;
    }
    
    /**
     * Creates a cloned button with same properties as original
     */
    private JButton createClonedButton(JButton original, String defaultText) {
        JButton cloned = new JButton(defaultText);
        if (original != null) {
            cloned.setFont(original.getFont());
            cloned.setForeground(original.getForeground());
            cloned.setBackground(original.getBackground());
            cloned.setOpaque(original.isOpaque());
            cloned.setBorder(original.getBorder());
            cloned.setBorderPainted(original.isBorderPainted());
            cloned.setContentAreaFilled(original.isContentAreaFilled());
            cloned.setFocusPainted(original.isFocusPainted());
        }
        return cloned;
    }
    
    /**
     * Updates the labels in a cloned panel with history entry data
     */
    private void updatePanelLabels(JPanel panel, final cephra.Phone.Utilities.HistoryManager.HistoryEntry entry) {
        // Get the stored components and update their text
        JLabel timeLabel = (JLabel) panel.getClientProperty("timeLabel");
        JLabel priceLabel = (JLabel) panel.getClientProperty("priceLabel");
        JLabel dateLabel = (JLabel) panel.getClientProperty("dateLabel");
        JLabel typeLabel = (JLabel) panel.getClientProperty("typeLabel");
        JLabel chargetimeLabel = (JLabel) panel.getClientProperty("chargetimeLabel");
        JButton detailsButton = (JButton) panel.getClientProperty("detailsButton");
        
        // Populate with history data
        if (timeLabel != null) timeLabel.setText(entry.getFormattedTime());
        if (priceLabel != null) priceLabel.setText(entry.getTotal());
        if (dateLabel != null) dateLabel.setText(entry.getFormattedDate());
        if (typeLabel != null) typeLabel.setText(entry.getServiceType());
        if (chargetimeLabel != null) chargetimeLabel.setText(entry.getChargingTime());
        
        // Add details button functionality for this specific entry
        if (detailsButton != null) {
            // Remove any existing listeners
            for (java.awt.event.ActionListener listener : detailsButton.getActionListeners()) {
                detailsButton.removeActionListener(listener);
            }
            // Add listener for this entry
            detailsButton.addActionListener(e -> showHistoryDetails(entry));
        }
    }
    
    /**
     * Setup scrollable history content using your designed pink history panel with labels
     */
    private void setupScrollableHistoryContent() {
        // Use SwingUtilities.invokeLater to ensure everything is ready after initComponents
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Your pink history panel is already set up in initComponents with your labels:
                // time, price, date, type, chargetime
                if (history != null) {
                    System.out.println("Using your designed pink 'history' panel with labels from NetBeans");
                    
                    // Your panel is already in the scroll pane thanks to initComponents
                    // Keep your exact design and layout - don't change anything!
                    
                } else {
                    System.err.println("ERROR: Your designed 'history' panel not found!");
                    return;
                }
                
                // Configure scroll pane behavior (keep scrollbars hidden like rewards)
                historyScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
                historyScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                historyScrollPane.getVerticalScrollBar().setUnitIncrement(16);
                
                // Hide scrollbars completely
                if (historyScrollPane.getVerticalScrollBar() != null) {
                    historyScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
                }
                if (historyScrollPane.getHorizontalScrollBar() != null) {
                    historyScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
                }
                
                historyScrollPane.setWheelScrollingEnabled(true);
                historyScrollPane.putClientProperty("JScrollPane.fastWheelScrolling", Boolean.TRUE);
                
                // Force a repaint to ensure everything displays correctly
                historyScrollPane.revalidate();
                historyScrollPane.repaint();
                this.revalidate();
                this.repaint();
                
                System.out.println("History scroll pane setup completed using your designed pink panel");
                
                // Load history entries now that the panel is ready
                loadHistoryEntries();
                
            } catch (Exception e) {
                System.err.println("Error setting up history scroll pane content: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    private void createHistoryPanel() {
        // This method is now handled by setupScrollableHistoryContent
        // Keep for backward compatibility but functionality moved to setupScrollableHistoryContent
    }
    
    private void loadHistoryEntries() {
        // Get current user's history
        currentUsername = cephra.Database.CephraDB.getCurrentUsername();
        System.out.println("PhoneHistory: Loaded history for user: " + currentUsername);
        refreshHistoryDisplay();
    }
    
    public void refreshHistoryDisplay() {
        // Check if your designed panels are initialized before using them
        if (history == null || history1 == null) {
            System.out.println("PhoneHistory: Your designed panels are null, will be loaded when setup completes");
            return;
        }
        
        System.out.println("PhoneHistory: Refreshing history display with your pink panel ready");
        
        // Get current user's history (now includes admin history entries)
        List<cephra.Phone.Utilities.HistoryManager.HistoryEntry> entries = cephra.Phone.Utilities.HistoryManager.getUserHistory(currentUsername);
        
        // Debug information
        System.out.println("PhoneHistory: Found " + entries.size() + " history entries");
        
        if (entries.isEmpty()) {
            // Show "No history" message in your designed labels
            time.setText("No Data");
            price.setText("₱0.00");
            date.setText("No Date");
            type.setText("No Service");
            chargetime.setText("0 mins");
            currentHistoryEntry = null;  // No entry to show details for
            
            // Debug: Check if there are any charging history records in database
            try {
                List<Object[]> dbHistory = cephra.Database.CephraDB.getChargingHistoryForUser(currentUsername);
                System.out.println("PhoneHistory: Database has " + dbHistory.size() + " charging history records for user: " + currentUsername);
                if (!dbHistory.isEmpty()) {
                    System.out.println("PhoneHistory: First record - Ticket: " + dbHistory.get(0)[0] + ", Service: " + dbHistory.get(0)[2]);
                }
            } catch (Exception e) {
                System.err.println("PhoneHistory: Error checking database history: " + e.getMessage());
            }
        } else {
            // Clear the container first to add all history entries
            history.removeAll();
            
            // Create a copy of your history1 panel for each entry
            for (int i = 0; i < entries.size(); i++) {
                cephra.Phone.Utilities.HistoryManager.HistoryEntry entry = entries.get(i);
                
                // Create a panel that looks like your history1 panel
                JPanel entryPanel = createHistoryPanelLikeHistory1(entry);
                history.add(entryPanel);
                
                // Add spacing between entries (except for the last one)
                if (i < entries.size() - 1) {
                    history.add(Box.createRigidArea(new Dimension(0, 8)));
                }
            }
            
            // Store the first entry for details button
            currentHistoryEntry = entries.get(0);
            
            System.out.println("PhoneHistory: Created " + entries.size() + " history panels with separators");
        }
        
        // Ensure proper repaint
        SwingUtilities.invokeLater(() -> {
            history1.revalidate();
            history1.repaint();
            history.revalidate();
            history.repaint();
        });
    }
    
    @Override
    public void onHistoryUpdated(String username) {
        System.out.println("PhoneHistory: onHistoryUpdated called for username: " + username + ", currentUsername: " + currentUsername);
        if (username != null && username.equals(currentUsername)) {
            System.out.println("PhoneHistory: Username matches, refreshing history display");
            SwingUtilities.invokeLater(this::refreshHistoryDisplay);
        } else {
            System.out.println("PhoneHistory: Username does not match, ignoring update");
        }
    }
    
    /**
     * Creates a panel that looks exactly like your designed history1 panel
     */
    private JPanel createHistoryPanelLikeHistory1(final cephra.Phone.Utilities.HistoryManager.HistoryEntry entry) {
        // Create panel with EXACT same properties as your history1
        JPanel panel = new JPanel();
        
        // Copy all properties from your history1 panel
        panel.setLayout(history1.getLayout());
        panel.setBackground(history1.getBackground());
        panel.setBorder(history1.getBorder());
        panel.setPreferredSize(history1.getPreferredSize());
        panel.setMaximumSize(history1.getMaximumSize());
        panel.setMinimumSize(history1.getMinimumSize());
        
        // Clone each component from history1 with the same bounds and properties
        
        // Clone time label
        if (time != null) {
            JLabel timeClone = new JLabel(entry.getFormattedTime());
            timeClone.setBounds(time.getBounds());
            timeClone.setFont(time.getFont());
            timeClone.setForeground(time.getForeground());
            timeClone.setBackground(time.getBackground());
            timeClone.setOpaque(time.isOpaque());
            panel.add(timeClone);
        }
        
        // Clone price label
        if (price != null) {
            JLabel priceClone = new JLabel(entry.getTotal());
            priceClone.setBounds(price.getBounds());
            priceClone.setFont(price.getFont());
            priceClone.setForeground(price.getForeground());
            priceClone.setBackground(price.getBackground());
            priceClone.setOpaque(price.isOpaque());
            panel.add(priceClone);
        }
        
        // Clone date label
        if (date != null) {
            JLabel dateClone = new JLabel(entry.getFormattedDate());
            dateClone.setBounds(date.getBounds());
            dateClone.setFont(date.getFont());
            dateClone.setForeground(date.getForeground());
            dateClone.setBackground(date.getBackground());
            dateClone.setOpaque(date.isOpaque());
            panel.add(dateClone);
        }
        
        // Clone type label
        if (type != null) {
            JLabel typeClone = new JLabel(entry.getServiceType());
            typeClone.setBounds(type.getBounds());
            typeClone.setFont(type.getFont());
            typeClone.setForeground(type.getForeground());
            typeClone.setBackground(type.getBackground());
            typeClone.setOpaque(type.isOpaque());
            panel.add(typeClone);
        }
        
        // Clone chargetime label
        if (chargetime != null) {
            JLabel chargetimeClone = new JLabel(entry.getChargingTime());
            chargetimeClone.setBounds(chargetime.getBounds());
            chargetimeClone.setFont(chargetime.getFont());
            chargetimeClone.setForeground(chargetime.getForeground());
            chargetimeClone.setBackground(chargetime.getBackground());
            chargetimeClone.setOpaque(chargetime.isOpaque());
            panel.add(chargetimeClone);
        }
        
        // Clone Separ label

        
        // Clone details button
        if (details != null) {
            JButton detailsClone = new JButton();
            detailsClone.setBounds(details.getBounds());
            detailsClone.setBorderPainted(details.isBorderPainted());
            detailsClone.setContentAreaFilled(details.isContentAreaFilled());
            detailsClone.setFocusPainted(details.isFocusPainted());
            detailsClone.addActionListener(evt -> {
                currentHistoryEntry = entry;
                showHistoryDetails(entry);
            });
            panel.add(detailsClone);
        }
        
        return panel;
    }
    
    /**
     * Groups history entries by their date (LocalDate)
     */
    private Map<LocalDate, List<cephra.Phone.Utilities.HistoryManager.HistoryEntry>> groupEntriesByDate(
            List<cephra.Phone.Utilities.HistoryManager.HistoryEntry> entries) {
        Map<LocalDate, List<cephra.Phone.Utilities.HistoryManager.HistoryEntry>> entriesByDate = new LinkedHashMap<>();
        
        for (cephra.Phone.Utilities.HistoryManager.HistoryEntry entry : entries) {
            LocalDate entryDate = entry.getTimestamp().toLocalDate();
            
            entriesByDate.computeIfAbsent(entryDate, k -> new ArrayList<>()).add(entry);
        }
        
        return entriesByDate;
    }
    
    /**
     * Creates and adds a date header panel
     */
    private void addDateHeader(LocalDate date) {
        JPanel dateHeaderPanel = new JPanel(new BorderLayout());
        dateHeaderPanel.setBackground(new Color(248, 248, 248)); // Light gray background
        dateHeaderPanel.setOpaque(true);
        dateHeaderPanel.setBorder(new EmptyBorder(8, 15, 8, 15));
        dateHeaderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Set fixed size for date header
        int headerHeight = 35;
        int headerWidth = 285; // Match the scroll pane width
        dateHeaderPanel.setPreferredSize(new Dimension(headerWidth, headerHeight));
        dateHeaderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, headerHeight));
        dateHeaderPanel.setMinimumSize(new Dimension(headerWidth, headerHeight));
        
        // Create date label using the same formatting as HistoryEntry
        cephra.Phone.Utilities.HistoryManager.HistoryEntry tempEntry = new cephra.Phone.Utilities.HistoryManager.HistoryEntry(
            "", "", "", "", "", date.atStartOfDay()
        );
        
        JLabel dateLabel = new JLabel(tempEntry.getFormattedDate());
        dateLabel.setFont(new Font("Arial", Font.BOLD, 13));
        dateLabel.setForeground(new Color(60, 60, 60));
        
        dateHeaderPanel.add(dateLabel, BorderLayout.WEST);
        
        history.add(dateHeaderPanel);
    }
    
    /**
     * Adds a history item without the date label (since date is shown in header)
     * Styled as a card similar to rewards system
     */
    private void addHistoryItemWithoutDate(final cephra.Phone.Utilities.HistoryManager.HistoryEntry entry) {
        // Create a card-style panel for a single history item
        JPanel historyItemPanel = new JPanel();
        historyItemPanel.setLayout(new BorderLayout(8, 5));
        historyItemPanel.setBackground(new Color(250, 250, 250)); // Light gray background
        historyItemPanel.setOpaque(true);
        historyItemPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        historyItemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set cursor to hand to indicate clickable
        historyItemPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Set a fixed preferred and maximum height for each history item panel
        int itemHeight = 80; // Increased height for card-style
        int itemWidth = 300; // Full width of container
        historyItemPanel.setPreferredSize(new Dimension(itemWidth, itemHeight));
        historyItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));
        historyItemPanel.setMinimumSize(new Dimension(itemWidth, itemHeight));

        // Left panel for details
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setOpaque(false);

        // Time
        JLabel timeLabel = new JLabel(entry.getFormattedTime());
        timeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        timeLabel.setForeground(new Color(70, 70, 70));
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Service type
        JLabel serviceLabel = new JLabel(entry.getServiceType());
        serviceLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        serviceLabel.setForeground(new Color(50, 100, 150));
        serviceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Charging time
        JLabel chargingTimeLabel = new JLabel("Charged: " + entry.getChargingTime());
        chargingTimeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        chargingTimeLabel.setForeground(Color.GRAY);
        chargingTimeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(timeLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        leftPanel.add(serviceLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        leftPanel.add(chargingTimeLabel);

        // Right panel for total amount
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setOpaque(false);

        // Total amount (prominently displayed)
        JLabel totalLabel = new JLabel(entry.getTotal());
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(0, 150, 0));
        totalLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(totalLabel);
        rightPanel.add(Box.createVerticalGlue());

        historyItemPanel.add(leftPanel, BorderLayout.WEST);
        historyItemPanel.add(rightPanel, BorderLayout.EAST);
        
        // Add hover effect
        historyItemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showHistoryDetails(entry);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                historyItemPanel.setBackground(new Color(245, 245, 245));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                historyItemPanel.setBackground(new Color(250, 250, 250));
            }
        });

        history.add(historyItemPanel);
        history.add(Box.createRigidArea(new Dimension(0, 8))); // Spacing between cards
    }
    
    private void addHistoryItem(final cephra.Phone.Utilities.HistoryManager.HistoryEntry entry) {
        // Create a panel for a single history item
        JPanel historyItemPanel = new JPanel();
        historyItemPanel.setLayout(new BorderLayout(5, 0));
        historyItemPanel.setBackground(Color.WHITE);
        historyItemPanel.setBorder(null);
        historyItemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Set cursor to hand to indicate clickable
        historyItemPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Set a fixed preferred and maximum height for each history item panel
        int itemHeight = 70; // Increased height for better visibility
        int itemWidth = 290; // Width of the scroll pane
        historyItemPanel.setPreferredSize(new Dimension(itemWidth, itemHeight));
        historyItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));

        // Date label at top
        JLabel dateLabel = new JLabel(entry.getFormattedDate());
        dateLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dateLabel.setForeground(new Color(70, 70, 70));

        // Left panel for details
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);

        // Time
        JLabel timeLabel = new JLabel(entry.getFormattedTime());
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        timeLabel.setForeground(Color.DARK_GRAY);
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Service type
        JLabel serviceLabel = new JLabel(entry.getServiceType());
        serviceLabel.setFont(new Font("Arial", Font.BOLD, 11));
        serviceLabel.setForeground(new Color(50, 100, 150));
        serviceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Total amount
        JLabel totalLabel = new JLabel("Total: " + entry.getTotal());
        totalLabel.setFont(new Font("Arial", Font.BOLD, 11));
        totalLabel.setForeground(new Color(0, 100, 0));
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(timeLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        leftPanel.add(serviceLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        leftPanel.add(totalLabel);

        // Right panel for ticket and reference
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(0, 0, 0, 15)); // Add 15px right margin

        // Ticket ID
        JLabel ticketLabel = new JLabel("Ticket: " + entry.getTicketId());
        ticketLabel.setFont(new Font("Arial", Font.BOLD, 11));
        ticketLabel.setForeground(new Color(50, 50, 50));
        ticketLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Change to LEFT_ALIGNMENT

        // Reference number
        String refNumber = entry.getReferenceNumber();
        
        // If reference number is empty or null, try to get it from QueueBridge
        if (refNumber == null || refNumber.trim().isEmpty() || refNumber.equals("null")) {
            refNumber = cephra.Admin.QueueBridge.getTicketRefNumber(entry.getTicketId());
        }
        
        JLabel referenceLabel = new JLabel("Ref: " + refNumber);
        referenceLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        referenceLabel.setForeground(Color.GRAY);
        referenceLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Change to LEFT_ALIGNMENT

        rightPanel.add(ticketLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        rightPanel.add(referenceLabel);

        historyItemPanel.add(dateLabel, BorderLayout.NORTH);
        historyItemPanel.add(leftPanel, BorderLayout.WEST);
        historyItemPanel.add(rightPanel, BorderLayout.EAST);
        
        // Add click listener to show details when clicked
        historyItemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showHistoryDetails(entry);
            }
        });

        history.add(historyItemPanel);
    }
/////
   
    private void showHistoryDetails(cephra.Phone.Utilities.HistoryManager.HistoryEntry entry) {
        System.out.println("showHistoryDetails called with entry: " + entry.getTicketId());
        
        // Update the designed detailpanel with the entry information
        if (detailpanel != null) {
            // Update ticket information - just the value
            if (ticket != null) {
                ticket.setText(entry.getTicketId());
            }
            
            // Update customer information - just the value
            if (Customer != null) {
                Customer.setText(cephra.Database.CephraDB.getCurrentUsername());
            }
            
            // Update service type - just the value
            if (typed != null) {
                typed.setText(entry.getServiceType());
            }
            
            // Update kWh information (get from admin history record) - just the value
            String kWhValue = "32.80"; // Default value
            try {
                List<Object[]> adminRecords = cephra.Admin.HistoryBridge.getRecordsForUser(cephra.Database.CephraDB.getCurrentUsername());
                if (adminRecords != null) {
                    for (Object[] record : adminRecords) {
                        if (record.length >= 7 && entry.getTicketId().equals(String.valueOf(record[0]))) {
                            kWhValue = String.valueOf(record[2]); // KWh is at index 2
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error getting kWh from admin history: " + e.getMessage());
            }
            if (kwh != null) {
                kwh.setText(kWhValue);
            }
            
            // Update charging time - just the value
            if (Chargingtime != null) {
                Chargingtime.setText(entry.getChargingTime());
            }
            
            // Update total price - just the value
            if (totalprice != null) {
                totalprice.setText(entry.getTotal());
            }
            
            // Update served by information - just the value
            String servedBy = "Admin"; // Default fallback
            try {
                List<Object[]> adminRecords = cephra.Admin.HistoryBridge.getRecordsForUser(cephra.Database.CephraDB.getCurrentUsername());
                if (adminRecords != null) {
                    for (Object[] record : adminRecords) {
                        if (record.length >= 5 && entry.getTicketId().equals(String.valueOf(record[0]))) {
                            String servedByValue = String.valueOf(record[4]); // Served By is at index 4
                            if (servedByValue != null && !servedByValue.equals("null")) {
                                servedBy = servedByValue;
                            }
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error getting served by from admin history: " + e.getMessage());
            }
            if (server != null) {
                server.setText(servedBy);
            }
            
            // Update date - just the value
            if (dated != null) {
                dated.setText(entry.getFormattedDate());
            }
            
            // Update time - just the value
            if (timed != null) {
                timed.setText(entry.getFormattedTime());
            }
            
            // Update reference number - just the value
            String refNumber = entry.getReferenceNumber();
            if (refNumber == null || refNumber.trim().isEmpty() || refNumber.equals("null")) {
                refNumber = cephra.Admin.QueueBridge.getTicketRefNumber(entry.getTicketId());
            }
            if (ref != null) {
                ref.setText(refNumber);
            }
            
            // Setup OK button action if not already done
            if (ok != null) {
                // Remove any existing listeners first
                for (java.awt.event.ActionListener listener : ok.getActionListeners()) {
                    ok.removeActionListener(listener);
                }
                // Add action listener to close the details
                ok.addActionListener(e -> closeDetailsPanel());
                ok.setText("OK");
            }
            
            // Create modal overlay to block all clicks except on detailpanel
            if (modalOverlay == null) {
                modalOverlay = new javax.swing.JPanel();
                modalOverlay.setBackground(new java.awt.Color(0, 0, 0, 100)); // Semi-transparent black
                modalOverlay.setOpaque(false);
                
                // Add mouse listener to block all clicks on the overlay
                modalOverlay.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        // Block all clicks - do nothing
                        e.consume();
                    }
                });
                
                // Add mouse motion listener to block drag events too
                modalOverlay.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                    @Override
                    public void mouseDragged(java.awt.event.MouseEvent e) {
                        e.consume();
                    }
                });
                
                add(modalOverlay);
            }
            
            // Position and show the modal overlay to cover everything
            modalOverlay.setBounds(0, 0, getWidth(), getHeight());
            modalOverlay.setVisible(true);
            System.out.println("Created and shown modal overlay");
            
            // Position the detailpanel in the center of the history area as a popup
            detailpanel.setBounds(75, 200, 230, 330); // Centered in the history area
            System.out.println("Positioned detailpanel at (75, 200, 230, 330)");
            detailpanel.setVisible(true);
            System.out.println("Set detailpanel visible to true");
            
            // Make sure detailpanel is on top of the modal overlay
            setComponentZOrder(modalOverlay, 1); // Behind detailpanel
            setComponentZOrder(detailpanel, 0);   // On top
            System.out.println("Set detailpanel to front layer with modal overlay behind");
        }
        
        // Make sure the background stays behind
        if (jLabel1 != null) {
            setComponentZOrder(jLabel1, getComponentCount() - 1);
        }
        
        revalidate();
        repaint();
    }
    
    private void closeDetailsPanel() {
        // Hide the designed detailpanel
        if (detailpanel != null) {
            detailpanel.setVisible(false);
        }
        
        // Hide the modal overlay to restore click functionality
        if (modalOverlay != null) {
            modalOverlay.setVisible(false);
            System.out.println("Hidden modal overlay - clicks restored");
        }
        
        revalidate();
        repaint();
    }
    
    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BorderLayout(15, 0));
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelComponent = new JLabel(label + ":");
        labelComponent.setFont(new Font("Arial", Font.BOLD, 13));
        labelComponent.setForeground(new Color(70, 70, 70));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 13));
        valueComponent.setForeground(new Color(30, 30, 30));
        
        // Special formatting for certain fields
        if (label.equals("Total")) {
            valueComponent.setFont(new Font("Arial", Font.BOLD, 13));
            valueComponent.setForeground(new Color(0, 100, 0));
        } else if (label.equals("Service")) {
            valueComponent.setForeground(new Color(50, 100, 150));
        } else if (label.equals("Reference")) {
            valueComponent.setFont(new Font("Arial", Font.BOLD, 12));
            valueComponent.setForeground(new Color(100, 50, 150));
        }
        
        rowPanel.add(labelComponent, BorderLayout.WEST);
        rowPanel.add(valueComponent, BorderLayout.CENTER);
        
        panel.add(rowPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
    }
    ////
    private void makeDraggable() {
        final Point[] dragPoint = {null};

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragPoint[0] = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragPoint[0] != null) {
                    java.awt.Window window = SwingUtilities.getWindowAncestor(ChargeHistory.this);
                    if (window != null) {
                        Point currentLocation = window.getLocation();
                        window.setLocation(
                            currentLocation.x + e.getX() - dragPoint[0].x,
                            currentLocation.y + e.getY() - dragPoint[0].y
                        );
                    }
                }
            }
        });
    }

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainHistoryContainer = new javax.swing.JPanel();
        historyScrollPane = new javax.swing.JScrollPane();
        history = new javax.swing.JPanel();
        profilebutton = new javax.swing.JButton();
        charge = new javax.swing.JButton();
        homebutton = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        history1 = new javax.swing.JPanel();
        time = new javax.swing.JLabel();
        price = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        type = new javax.swing.JLabel();
        chargetime = new javax.swing.JLabel();
        details = new javax.swing.JButton();
        detailpanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        ticket = new javax.swing.JLabel();
        Customer = new javax.swing.JLabel();
        typed = new javax.swing.JLabel();
        kwh = new javax.swing.JLabel();
        Chargingtime = new javax.swing.JLabel();
        totalprice = new javax.swing.JLabel();
        server = new javax.swing.JLabel();
        dated = new javax.swing.JLabel();
        timed = new javax.swing.JLabel();
        ref = new javax.swing.JLabel();
        ok = new javax.swing.JToggleButton();

        setMaximumSize(new java.awt.Dimension(370, 750));
        setPreferredSize(new java.awt.Dimension(370, 750));
        setLayout(null);

        mainHistoryContainer.setBackground(new java.awt.Color(255, 255, 255));
        mainHistoryContainer.setOpaque(false);

        historyScrollPane.setBackground(new java.awt.Color(255, 255, 255));
        historyScrollPane.setBorder(null);
        historyScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        historyScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        historyScrollPane.setOpaque(false);

        history.setBackground(new java.awt.Color(255, 255, 255));
        history.setLayout(new javax.swing.BoxLayout(history, javax.swing.BoxLayout.Y_AXIS));
        historyScrollPane.setViewportView(history);

        javax.swing.GroupLayout mainHistoryContainerLayout = new javax.swing.GroupLayout(mainHistoryContainer);
        mainHistoryContainer.setLayout(mainHistoryContainerLayout);
        mainHistoryContainerLayout.setHorizontalGroup(
            mainHistoryContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainHistoryContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(historyScrollPane)
                .addContainerGap())
        );
        mainHistoryContainerLayout.setVerticalGroup(
            mainHistoryContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainHistoryContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(historyScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(mainHistoryContainer);
        mainHistoryContainer.setBounds(20, 130, 330, 530);

        profilebutton.setBorder(null);
        profilebutton.setBorderPainted(false);
        profilebutton.setContentAreaFilled(false);
        profilebutton.setFocusPainted(false);
        profilebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilebuttonActionPerformed(evt);
            }
        });
        add(profilebutton);
        profilebutton.setBounds(260, 680, 40, 40);

        charge.setBorder(null);
        charge.setBorderPainted(false);
        charge.setContentAreaFilled(false);
        charge.setFocusPainted(false);
        charge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeActionPerformed(evt);
            }
        });
        add(charge);
        charge.setBounds(30, 680, 50, 50);

        homebutton.setBorder(null);
        homebutton.setBorderPainted(false);
        homebutton.setContentAreaFilled(false);
        homebutton.setFocusPainted(false);
        homebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homebuttonActionPerformed(evt);
            }
        });
        add(homebutton);
        homebutton.setBounds(150, 680, 40, 40);

        linkbutton.setBorder(null);
        linkbutton.setBorderPainted(false);
        linkbutton.setContentAreaFilled(false);
        linkbutton.setFocusPainted(false);
        linkbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkbuttonActionPerformed(evt);
            }
        });
        add(linkbutton);
        linkbutton.setBounds(90, 680, 60, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/HISTORY - if none.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-15, 0, 398, 750);

        history1.setBackground(new java.awt.Color(255, 255, 255));
        history1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        history1.setLayout(null);

        time.setText("time");
        history1.add(time);
        time.setBounds(10, 20, 80, 16);

        price.setText("Price");
        history1.add(price);
        price.setBounds(220, 30, 70, 16);

        date.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        date.setText("Date");
        history1.add(date);
        date.setBounds(10, 0, 120, 20);

        type.setText("jLabel2");
        history1.add(type);
        type.setBounds(10, 40, 210, 16);

        chargetime.setBackground(new java.awt.Color(0, 0, 0));
        chargetime.setText("jLabel2");
        history1.add(chargetime);
        chargetime.setBounds(10, 60, 220, 16);

        details.setBorderPainted(false);
        details.setContentAreaFilled(false);
        details.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailsActionPerformed(evt);
            }
        });
        history1.add(details);
        details.setBounds(70, -30, 310, 80);

        add(history1);
        history1.setBounds(400, 160, 310, 80);

        detailpanel.setBackground(new java.awt.Color(255, 255, 255));
        detailpanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setText("Charging Details");

        ticket.setText("jLabel3");

        Customer.setText("jLabel3");

        typed.setText("jLabel3");

        kwh.setText("jLabel3");

        Chargingtime.setText("jLabel3");

        totalprice.setText("jLabel3");

        server.setText("jLabel3");

        dated.setText("jLabel3");

        timed.setText("jLabel3");

        ref.setText("jLabel3");

        ok.setText("jToggleButton1");

        javax.swing.GroupLayout detailpanelLayout = new javax.swing.GroupLayout(detailpanel);
        detailpanel.setLayout(detailpanelLayout);
        detailpanelLayout.setHorizontalGroup(
            detailpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailpanelLayout.createSequentialGroup()
                .addGroup(detailpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detailpanelLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel2))
                    .addGroup(detailpanelLayout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addGroup(detailpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Customer)
                            .addComponent(ticket)
                            .addComponent(typed)
                            .addComponent(kwh)
                            .addComponent(Chargingtime)
                            .addComponent(totalprice)
                            .addComponent(server)
                            .addComponent(dated)
                            .addComponent(timed)
                            .addComponent(ref)))
                    .addGroup(detailpanelLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(ok)))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        detailpanelLayout.setVerticalGroup(
            detailpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(27, 27, 27)
                .addComponent(ticket)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Customer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(typed)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kwh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Chargingtime)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalprice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(server)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dated)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timed)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ref)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ok)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        add(detailpanel);
        detailpanel.setBounds(430, 310, 230, 330);
    }// </editor-fold>//GEN-END:initComponents

    // ===== CUSTOM CODE SECTION - DO NOT REMOVE =====
    // IMPORTANT: After making changes in NetBeans Designer, make sure:
    // 1. Call setupCustomCode() in constructor after initComponents()
    // 2. Keep the detailsActionPerformed method below
    // 3. This will automatically fix any issues caused by NetBeans regeneration
    
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(0, 0, 370, 750);
        }
    }
    
    // CUSTOM CODE - DESIGN-SAFE SETUP (Works no matter how you change your NetBeans design!)
    private void setupCustomCode() {
        try {
            System.out.println("=== Setting up custom functionality (design-safe) ===");
            
            // 1. ALWAYS fix history1 panel size (your pink panel)
            if (history1 != null) {
                history1.setPreferredSize(new java.awt.Dimension(320, 80));
                history1.setMaximumSize(new java.awt.Dimension(320, 80));
                history1.setMinimumSize(new java.awt.Dimension(320, 80));
                System.out.println("✓ Fixed history1 panel size");
            }
            
            // 2. ALWAYS set proper initial text for labels (if they exist)
            if (time != null) time.setText("Loading...");
            if (price != null) price.setText("₱0.00");
            if (date != null) date.setText("No Date");
            if (type != null) type.setText("No Service");
            if (chargetime != null) chargetime.setText("0 mins");
            
         
            // 2.6. Adjust chargetime position to avoid overlapping separator
            if (chargetime != null) {
                chargetime.setBounds(10, 62, 220, 16); // Moved down to y=62 to avoid separator at y=45
                System.out.println("✓ Adjusted chargetime position");
            }
          
            System.out.println("✓ Set initial label text");
            
            // 3. ALWAYS add details button functionality (if button exists)
            if (details != null) {
                // Remove any existing listeners first (to prevent duplicates)
                for (java.awt.event.ActionListener listener : details.getActionListeners()) {
                    details.removeActionListener(listener);
                }
                // Add our custom action listener
                details.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        detailsActionPerformed(evt);
                    }
                });
                System.out.println("✓ Added details button functionality");
            }
            
            // 4. Move your designed history1 panel into the visible area
            if (history1 != null && history != null) {
                // Remove history1 from its current position outside the visible area
                remove(history1);
                
                // Position your history1 panel in the visible scroll area
                history1.setBounds(0, 0, 310, 80);
                history1.setVisible(true);
                history1.setOpaque(true);
                
                // Clear the scroll container and add your designed history1 panel
                history.removeAll();
                history.add(history1);
                
            
                
                history.revalidate();
                history.repaint();
                
                // Debug info
                System.out.println("✓ history1 panel setup:");
                System.out.println("  - Bounds: " + history1.getBounds());
                System.out.println("  - Visible: " + history1.isVisible());
                System.out.println("  - Component count: " + history1.getComponentCount());
                System.out.println("✓ Moved your designed history1 panel (with Separ) into visible area");
            }
            
            // 5. Initially hide the detailpanel (it will be shown when details button is clicked)
            if (detailpanel != null) {
                detailpanel.setVisible(false);
                System.out.println("✓ Initially hid detailpanel (will show when details clicked)");
            }
            
            System.out.println("=== Custom setup completed successfully! ===");
            
        } catch (Exception e) {
            System.err.println("Error in setupCustomCode: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * CALL THIS METHOD AFTER CHANGING YOUR DESIGN IN NETBEANS!
     * This will fix everything automatically so your customizations work again.
     * Just add this line to your constructor after initComponents():
     * setupCustomCode();
     */
    public void fixAfterDesignChange() {
        SwingUtilities.invokeLater(() -> {
            setupCustomCode();
            if (currentUsername != null) {
                refreshHistoryDisplay();
            }
        });
    }

    private void chargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.ChargingOption());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_chargeActionPerformed

    private void profilebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilebuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.Profile());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_profilebuttonActionPerformed

    private void homebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homebuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(cephra.Phone.Dashboard.Home.getAppropriateHomePanel());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_homebuttonActionPerformed

    private void linkbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkbuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.LinkConnect());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_linkbuttonActionPerformed

    private void detailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailsActionPerformed
        // Show details of the currently displayed history entry
        System.out.println("Details button clicked!");
        if (currentHistoryEntry != null) {
            System.out.println("Current history entry exists: " + currentHistoryEntry.getTicketId());
            showHistoryDetails(currentHistoryEntry);
        } else {
            System.out.println("No history entry to show details for");
        }
    }//GEN-LAST:event_detailsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Chargingtime;
    private javax.swing.JLabel Customer;
    private javax.swing.JButton charge;
    private javax.swing.JLabel chargetime;
    private javax.swing.JLabel date;
    private javax.swing.JLabel dated;
    private javax.swing.JPanel detailpanel;
    private javax.swing.JButton details;
    private javax.swing.JPanel history;
    private javax.swing.JPanel history1;
    private javax.swing.JScrollPane historyScrollPane;
    private javax.swing.JButton homebutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel kwh;
    private javax.swing.JButton linkbutton;
    private javax.swing.JPanel mainHistoryContainer;
    private javax.swing.JToggleButton ok;
    private javax.swing.JLabel price;
    private javax.swing.JButton profilebutton;
    private javax.swing.JLabel ref;
    private javax.swing.JLabel server;
    private javax.swing.JLabel ticket;
    private javax.swing.JLabel time;
    private javax.swing.JLabel timed;
    private javax.swing.JLabel totalprice;
    private javax.swing.JLabel type;
    private javax.swing.JLabel typed;
    // End of variables declaration//GEN-END:variables
    
    // Custom modal overlay to block clicks
    private javax.swing.JPanel modalOverlay;

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition();
                // Ensure background stays behind controls
                if (jLabel1 != null) {
                    setComponentZOrder(jLabel1, getComponentCount() - 1);
                }
            }
        });
    }
    
    @Override
    public void removeNotify() {
        // Unregister as listener when panel is removed
        cephra.Phone.Utilities.HistoryManager.removeHistoryUpdateListener(this);
        super.removeNotify();
    }
}
