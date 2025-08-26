package cephra.Admin;

import java.util.ArrayList;

public class StaffData {
    // Each staff: Name, Username, Email, Status, Password
    public static final ArrayList<String[]> staffList = new ArrayList<>();

    public static void addStaff(String name, String username, String email, String password) {
        staffList.add(new String[]{name, username, email, "", password});
    }
}