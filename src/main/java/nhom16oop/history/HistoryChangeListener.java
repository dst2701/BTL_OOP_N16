package nhom16oop.history;

/**
 * Listener interface for handling changes in game history.
 */
public interface HistoryChangeListener {
    /**
     * Called when the game history (e.g., undo stack) changes.
     */
    void onHistoryChanged();
}
