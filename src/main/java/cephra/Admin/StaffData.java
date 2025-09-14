package cephra.Admin;

import java.util.ArrayList;
import java.util.List;

public class StaffData {
    // Each staff: Name, Username, Email, Status, Password
    
    public static void addStaff(String name, String username, String email, String password) {
        // Add to database instead of in-memory list
        cephra.db.CephraDB.addStaff(name, username, email, password);
    }
    
    public static List<String[]> getStaffList() {
        // Get from database instead of in-memory list
        List<Object[]> dbStaff = cephra.db.CephraDB.getAllStaff();
        List<String[]> staffList = new ArrayList<>();
        
        for (Object[] staff : dbStaff) {
            staffList.add(new String[]{
                (String) staff[0], // name
                (String) staff[1], // username
                (String) staff[2], // email
                (String) staff[3], // status
                (String) staff[4]  // password
            });
        }
        
        return staffList;
    }
    
    // Legacy method for backward compatibility
    public static final ArrayList<String[]> staffList = new ArrayList<>();
}