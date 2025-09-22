package cephra.Phone.Utilities;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Wallet History Manager utility class for managing wallet transaction updates
 * Similar to HistoryManager but for wallet transactions
 */
public class WalletHistoryManager {
    
    private static final List<WalletHistoryUpdateListener> listeners = new CopyOnWriteArrayList<>();
    
    /**
     * Interface for listening to wallet history updates
     */
    public interface WalletHistoryUpdateListener {
        void onWalletHistoryUpdated(String username);
    }
    
    /**
     * Add a wallet history update listener
     */
    public static void addWalletHistoryUpdateListener(WalletHistoryUpdateListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * Remove a wallet history update listener
     */
    public static void removeWalletHistoryUpdateListener(WalletHistoryUpdateListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Notify all listeners that wallet history has been updated for a user
     */
    public static void notifyWalletHistoryUpdated(String username) {
        for (WalletHistoryUpdateListener listener : listeners) {
            try {
                listener.onWalletHistoryUpdated(username);
            } catch (Exception e) {
                System.err.println("Error notifying wallet history listener: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Notify that wallet history has been updated (called when new transactions occur)
     */
    public static void updateWalletHistory(String username) {
        notifyWalletHistoryUpdated(username);
    }
}
