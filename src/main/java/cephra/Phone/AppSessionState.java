package cephra.Phone;

public final class AppSessionState {
    private AppSessionState() {}

    public static String userEmailForReset = null;
    
    // Flag to control when to show OTP notification
    // true = show notification (coming from PasswordForgot or resending)
    // false = don't show notification (coming from other screens)
    public static boolean showOtpNotification = false;
}
