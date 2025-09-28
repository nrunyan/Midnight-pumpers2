package Util;

public enum ScreenStatus {
    // No input from the screen
    NO_INPUT("no input"),
    // The Customer canceled the transaction
    CANCEL("cancel"),
    // The Customer Paused fueling
    PAUSE("pause"),
    // The Customer Resumed fueling
    RESUME("resume"),
    // Customer Starts fueling
    START("start"),
    // Customer Has Ended the transaction
    END("end"),
    // Error, used a getter in incorrect context
    ERROR("error");
    private String string; // string representation of this Screen Status
    ScreenStatus(String statusStr) {
        string = statusStr;
    }

    /**
     * Get the screen representation of this screen status
     * @return the screen representation of this screen status
     */
    @Override
    public String toString() {
        return string;
    }
}
