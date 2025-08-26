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
                return true;
            }
        }
        return false;
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
}