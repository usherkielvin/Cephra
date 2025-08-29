package cephra;

import java.util.ArrayList;
import java.util.Random;

public class CephraDB {

    // Inner class to represent a user
    private static class User {
        String username;
        String email;
        String password;

        public User(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }

    private static String generatedOTP;
    
    // Our "database" which is a static ArrayList of User objects
    private static ArrayList<User> userDatabase;
    
    // Current logged-in user
    private static User currentUser;
    
    // Battery percentage storage per user
    private static java.util.HashMap<String, Integer> userBatteryLevels = new java.util.HashMap<>();
    
    // Active ticket tracking per user
    private static java.util.HashMap<String, String> userActiveTickets = new java.util.HashMap<>();

    // Method to initialize the database with some dummy data
    public static void initializeDatabase() {
        if (userDatabase == null) {
            userDatabase = new ArrayList<>();
            // Add some test users
            userDatabase.add(new User("dizon", "dizon@cephra.com", "1234"));
            userDatabase.add(new User("testuser", "test@cephra.com", "1234"));
            System.out.println("Cephra database initialized with dummy users.");
        }
    }

    // Method to check if the given credentials are valid
    public static boolean validateLogin(String username, String password) {
        if (userDatabase == null) {
            initializeDatabase();
        }
        for (User user : userDatabase) {
            if (user.username.equals(username) && user.password.equals(password)) {
                currentUser = user; // Set the current user when login is successful
                return true;
            }
        }
        return false;
    }
    
    // Method to get the current logged-in username
    public static String getCurrentUsername() {
        return currentUser != null ? currentUser.username : "";
    }
    
    // Method to get the current logged-in user's email
    public static String getCurrentEmail() {
        return currentUser != null ? currentUser.email : "";
    }

    // Method to add a new user to the database
    public static boolean addUser(String username, String email, String password) {
        if (userDatabase == null) {
            initializeDatabase();
        }
        // Check if the username or email already exists
        for (User user : userDatabase) {
            if (user.username.equals(username) || user.email.equals(email)) {
                return false; // User already exists, registration failed
            }
        }
        userDatabase.add(new User(username, email, password));
        return true; // User added successfully
    }
    
    // Method to find a user by email
    public static User findUserByEmail(String email) {
        if (userDatabase == null) {
            initializeDatabase();
        }
        for (User user : userDatabase) {
            if (user.email.equals(email)) {
                return user;
            }
        }
        return null;
    }
    
    // Method to update a user's password
    public static boolean updateUserPassword(String email, String newPassword) {
        if (userDatabase == null) {
            initializeDatabase();
        }
        for (User user : userDatabase) {
            if (user.email.equals(email)) {
                user.password = newPassword;
                return true;
            }
        }
        return false;
    }
    
    // Method to generate and store a new 6-digit OTP
    public static String generateAndStoreOTP() {
        Random random = new Random();
        generatedOTP = String.format("%06d", random.nextInt(1000000));
        System.out.println("Generated OTP: " + generatedOTP);
        return generatedOTP;
    }

    // Method to get the stored OTP
    public static String getGeneratedOTP() {
        return generatedOTP;
    }
    
    // Battery management methods
    public static int getUserBatteryLevel(String username) {
        if (userBatteryLevels.containsKey(username)) {
            return userBatteryLevels.get(username);
        }
        // Generate random battery level (15-50%) for new users
        java.util.Random random = new java.util.Random();
        int batteryLevel = 15 + random.nextInt(36); // 15 to 50
        userBatteryLevels.put(username, batteryLevel);
        return batteryLevel;
    }
    
    public static void setUserBatteryLevel(String username, int batteryLevel) {
        userBatteryLevels.put(username, batteryLevel);
    }
    
    public static void chargeUserBatteryToFull(String username) {
        userBatteryLevels.put(username, 100);
    }
    
    // Active ticket management methods
    public static boolean hasActiveTicket(String username) {
        return userActiveTickets.containsKey(username) && userActiveTickets.get(username) != null;
    }
    
    public static void setActiveTicket(String username, String ticketId) {
        userActiveTickets.put(username, ticketId);
    }
    
    public static void clearActiveTicket(String username) {
        userActiveTickets.remove(username);
    }
    
    public static String getActiveTicket(String username) {
        return userActiveTickets.get(username);
    }
}