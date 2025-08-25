package cephra;

import java.util.ArrayList;

public class CephraDB {

    // Inner class to represent a user
    private static class User {
        String username;
        String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    // Our "database" which is a static ArrayList of User objects
    private static ArrayList<User> userDatabase;

    // Method to initialize the database with some dummy data
    public static void initializeDatabase() {
        if (userDatabase == null) {
            userDatabase = new ArrayList<>();
            // Add some test users
            userDatabase.add(new User("dizon", "1234"));
            userDatabase.add(new User("testuser", "1234"));
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
    public static boolean addUser(String username, String password) {
        if (userDatabase == null) {
            initializeDatabase();
        }
        // Check if the username already exists
        for (User user : userDatabase) {
            if (user.username.equals(username)) {
                return false; // User already exists, registration failed
            }
        }
        userDatabase.add(new User(username, password));
        return true; // User added successfully
    }
}