package Util;

public enum ScreenStatus {
    // No input from the screen
    NO_INPUT,
    // The Customer canceled the transaction
    CANCEL,
    // The Customer Paused fueling
    PAUSE,
    // The Customer Resumed fueling
    RESUME,
    // Customer Starts fueling
    START;
}
