package Util;

/**
 * Author: Valerie Barker
 */
public class Timer {
    private final long startTime;
    private final long timeout;

    /**
     * Makes a new timer with a set time out
     * @param timeOut how long the timer takes before timing out in seconds
     */
    public Timer(int timeOut) {
        this.startTime = System.currentTimeMillis();
        this.timeout = ((long)timeOut * 1000) + startTime;
    }

    /**
     * @return true if the timer has timed out
     */
    public boolean timeout() {
        return System.currentTimeMillis() > timeout;
    }

}
