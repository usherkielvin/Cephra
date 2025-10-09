import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class bars {
    // Store user progress data
    private static Map<String, UserProgress> userProgressMap = new HashMap<>();
    private static String currentUser = null;
    
    // User progress data class
    static class UserProgress {
        int progress;
        boolean isCharging;
        Timer chargingTimer;
        long chargingStartTime; // When charging started
        int startingProgress;   // Progress when charging started
        
        UserProgress() {
            this.progress = 0;
            this.isCharging = false;
            this.chargingTimer = null;
            this.chargingStartTime = 0;
            this.startingProgress = 0;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
          
            showLoginFrame();
        });
    }
    
    // Login/Register Frame
    private static void showLoginFrame() {
        JFrame loginFrame = new JFrame("Cephra - Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 300);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setLayout(new BorderLayout());
        
        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Title
        JLabel titleLabel = new JLabel("Cephra Charging System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(titleLabel, gbc);
        
        // Username field
        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(userLabel, gbc);
        
        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(usernameField, gbc);
        
        // Password field
        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(passLabel, gbc);
        
        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(passwordField, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        
        // Style buttons
        loginButton.setPreferredSize(new Dimension(100, 30));
        registerButton.setPreferredSize(new Dimension(100, 30));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        registerButton.setBackground(new Color(60, 179, 113));
        registerButton.setForeground(Color.WHITE);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);
        
        // Login button action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Please enter both username and password!", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Simple validation (in real app, you'd check against database)
            if (userExists(username)) {
                currentUser = username;
                loginFrame.dispose();
                showMainFrame();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "User not found! Please register first.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Register button action
        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Please enter both username and password!", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (userExists(username)) {
                JOptionPane.showMessageDialog(loginFrame, "Username already exists! Please choose another.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Register new user
            userProgressMap.put(username, new UserProgress());
            JOptionPane.showMessageDialog(loginFrame, "Registration successful! You can now login.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            usernameField.setText("");
            passwordField.setText("");
        });
        
        // Enter key support
        ActionListener loginAction = e -> loginButton.doClick();
        usernameField.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);
        
        loginFrame.add(mainPanel, BorderLayout.CENTER);
        loginFrame.setVisible(true);
    }
    
    // Main charging frame
    private static void showMainFrame() {
        JFrame mainFrame = new JFrame("Cephra - Charging Station");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(500, 350);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());
        
        // Get current user's progress
        UserProgress userProgress = userProgressMap.get(currentUser);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel statusLabel = new JLabel("Charging Status", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setForeground(new Color(100, 100, 100));
        
        headerPanel.add(welcomeLabel, BorderLayout.NORTH);
        headerPanel.add(statusLabel, BorderLayout.SOUTH);
        
        // Progress panel
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Progress bar
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(userProgress.progress);
        progressBar.setStringPainted(true);
        progressBar.setString(userProgress.progress + "%");
        progressBar.setPreferredSize(new Dimension(400, 30));
        progressBar.setForeground(new Color(76, 175, 80));
        
        // Progress label
        JLabel progressLabel = new JLabel("Battery Level: " + userProgress.progress + "%", SwingConstants.CENTER);
        progressLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        progressPanel.add(progressLabel, BorderLayout.NORTH);
        progressPanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
        progressPanel.add(progressBar, BorderLayout.SOUTH);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton startButton = new JButton("Start Charging");
        JButton logoutButton = new JButton("Logout");
        
        // Style buttons
        startButton.setPreferredSize(new Dimension(140, 40));
        logoutButton.setPreferredSize(new Dimension(140, 40));
        startButton.setBackground(new Color(76, 175, 80));
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setBackground(new Color(244, 67, 54));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        buttonPanel.add(startButton);
        buttonPanel.add(logoutButton);
        
        // Start button action
        startButton.addActionListener(e -> {
            if (userProgress.progress >= 100) {
                JOptionPane.showMessageDialog(mainFrame, "Battery is already fully charged!", 
                                            "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            if (userProgress.isCharging) {
                JOptionPane.showMessageDialog(mainFrame, "Charging is already in progress!", 
                                            "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Start charging process
            userProgress.isCharging = true;
            userProgress.chargingStartTime = System.currentTimeMillis();
            userProgress.startingProgress = userProgress.progress;
            startButton.setText("Charging...");
            startButton.setEnabled(false);
            startButton.setBackground(new Color(150, 150, 150));
            
            // Create charging timer (20 seconds to full charge)
            startCharging(userProgress, progressBar, progressLabel, startButton, mainFrame);
        });
        
        // Logout button action
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(mainFrame, 
                "Are you sure you want to logout?\n" + 
                (userProgress.isCharging ? "Your charging will continue in the background." : ""), 
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
                
            if (choice == JOptionPane.YES_OPTION) {
                mainFrame.dispose();
                currentUser = null;
                showLoginFrame();
            }
        });
        
        // Update UI if already charging
        if (userProgress.isCharging) {
            startButton.setText("Charging...");
            startButton.setEnabled(false);
            startButton.setBackground(new Color(150, 150, 150));
            
            // Resume charging if it was in progress
            resumeCharging(userProgress, progressBar, progressLabel, startButton, mainFrame);
        } else if (userProgress.progress >= 100) {
            startButton.setText("Fully Charged");
            startButton.setEnabled(false);
            progressBar.setString("100% - Fully Charged!");
        }
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(progressPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }
    
    
    // Method to start charging process
    private static void startCharging(UserProgress userProgress, JProgressBar progressBar, 
                                    JLabel progressLabel, JButton startButton, JFrame mainFrame) {
        int remainingProgress = 100 - userProgress.progress;
        int timerDelay = (20 * 1000) / remainingProgress; // milliseconds per 1%
        
        userProgress.chargingTimer = new Timer(timerDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userProgress.progress++;
                progressBar.setValue(userProgress.progress);
                progressBar.setString(userProgress.progress + "%");
                progressLabel.setText("Battery Level: " + userProgress.progress + "%");
                
                if (userProgress.progress >= 100) {
                    // Charging complete
                    userProgress.chargingTimer.stop();
                    userProgress.isCharging = false;
                    startButton.setText("Fully Charged");
                    startButton.setBackground(new Color(76, 175, 80));
                    progressBar.setString("100% - Fully Charged!");
                    
                    JOptionPane.showMessageDialog(mainFrame, 
                        "Charging complete! Your battery is now fully charged.", 
                        "Charging Complete", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        userProgress.chargingTimer.start();
    }
    
    // Method to resume charging when user logs back in
    private static void resumeCharging(UserProgress userProgress, JProgressBar progressBar, 
                                     JLabel progressLabel, JButton startButton, JFrame mainFrame) {
        if (!userProgress.isCharging) return;
        
        // Calculate how much time has passed since charging started
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - userProgress.chargingStartTime;
        
        // Calculate expected progress (20 seconds = 20000ms for full charge from 0%)
        int progressRange = 100 - userProgress.startingProgress;
        long totalChargingTime = 20000; // 20 seconds in milliseconds
        long timePerPercent = totalChargingTime / progressRange;
        
        int expectedProgress = userProgress.startingProgress + (int)(elapsedTime / timePerPercent);
        
        // Update progress to expected value (but don't exceed 100%)
        if (expectedProgress > userProgress.progress && expectedProgress <= 100) {
            userProgress.progress = expectedProgress;
            progressBar.setValue(userProgress.progress);
            progressBar.setString(userProgress.progress + "%");
            progressLabel.setText("Battery Level: " + userProgress.progress + "%");
        }
        
        // If charging should be complete by now
        if (expectedProgress >= 100) {
            userProgress.progress = 100;
            userProgress.isCharging = false;
            progressBar.setValue(100);
            progressBar.setString("100% - Fully Charged!");
            progressLabel.setText("Battery Level: 100%");
            startButton.setText("Fully Charged");
            startButton.setBackground(new Color(76, 175, 80));
            return;
        }
        
        // Continue charging from current point
        int remainingProgress = 100 - userProgress.progress;
        if (remainingProgress > 0) {
            int timerDelay = (int)(timePerPercent); // milliseconds per 1%
            
            userProgress.chargingTimer = new Timer(timerDelay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    userProgress.progress++;
                    progressBar.setValue(userProgress.progress);
                    progressBar.setString(userProgress.progress + "%");
                    progressLabel.setText("Battery Level: " + userProgress.progress + "%");
                    
                    if (userProgress.progress >= 100) {
                        // Charging complete
                        userProgress.chargingTimer.stop();
                        userProgress.isCharging = false;
                        startButton.setText("Fully Charged");
                        startButton.setBackground(new Color(76, 175, 80));
                        progressBar.setString("100% - Fully Charged!");
                        
                        JOptionPane.showMessageDialog(mainFrame, 
                            "Charging complete! Your battery is now fully charged.", 
                            "Charging Complete", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
            
            userProgress.chargingTimer.start();
        }
    }
    
    // Helper method to check if user exists
    private static boolean userExists(String username) {
        return userProgressMap.containsKey(username);
    }
}