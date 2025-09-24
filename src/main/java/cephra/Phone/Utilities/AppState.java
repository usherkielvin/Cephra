package cephra.Phone.Utilities;

public final class AppState {
    private AppState() {}

    public static boolean isCarLinked = false;
    
    /**
     * Initialize the car linking state based on database user car assignment.
     * A car is considered linked if the user has a non-null car_index in the users table.
     */
    public static void initializeCarLinkingState() {
        try {
            String username = cephra.Database.CephraDB.getCurrentUsername();
            if (username != null && !username.isEmpty()) {
                int carIndex = cephra.Database.CephraDB.getUserCarIndex(username);
                isCarLinked = (carIndex != -1);
            }
        } catch (Exception e) {
            System.err.println("Error initializing car linking state: " + e.getMessage());
            isCarLinked = false; // Default to not linked on error
        }
    }
}
