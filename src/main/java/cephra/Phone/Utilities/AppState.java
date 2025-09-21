package cephra.Phone.Utilities;

public final class AppState {
    private AppState() {}

    public static boolean isCarLinked = false;
    
    /**
     * Initialize the car linking state based on database battery level
     * Car is considered linked if user has battery data and battery is not 100%
     * When battery is 100%, car is considered "unlinked" since no charging is needed
     */
    public static void initializeCarLinkingState() {
        try {
            String username = cephra.Database.CephraDB.getCurrentUsername();
            if (username != null && !username.isEmpty()) {
                int batteryLevel = cephra.Database.CephraDB.getUserBatteryLevel(username);
                // Car is linked if battery exists and is not 100% (meaning it needs charging)
                isCarLinked = (batteryLevel != -1 && batteryLevel < 100);
                // If battery is 100%, ensure car is considered unlinked
                if (batteryLevel == 100) {
                    isCarLinked = false;
                }
            }
        } catch (Exception e) {
            System.err.println("Error initializing car linking state: " + e.getMessage());
            isCarLinked = false; // Default to not linked on error
        }
    }
}
