package cephra.Admin;

import java.util.ArrayList;
import java.util.List;

public class StaffData {
    // Each staff: Name, Username, Email, Status, Password
    
    public static void addStaff(String firstname, String lastname, String username, String email, String password) {
        // Add to database instead of in-memory list
        cephra.Database.CephraDB.addStaff(firstname, lastname, username, email, password);
    }
    
    public static List<String[]> getStaffList() {
        // Get from database instead of in-memory list
        List<Object[]> dbStaff = cephra.Database.CephraDB.getAllStaff();
        List<String[]> staffList = new ArrayList<>();
        
        for (Object[] staff : dbStaff) {
            staffList.add(new String[]{
                (String) staff[0], // name (combined firstname + lastname)
                (String) staff[1], // firstname
                (String) staff[2], // lastname
                (String) staff[3], // username
                (String) staff[4], // email
                (String) staff[5], // status
                (String) staff[6]  // password
            });
        }
        
        return staffList;
    }
    
    // Legacy method for backward compatibility
    public static final ArrayList<String[]> staffList = new ArrayList<>();
}