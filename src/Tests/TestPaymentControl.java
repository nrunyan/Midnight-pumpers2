package Tests;

import SecondLevel.PaymentControl;

/**
 * Another test main, run after starting the GUIs
 */
public class TestPaymentControl {
    /**
     * starts
     * @param args no cmd args
     */

    public static boolean on = true;
    public static void main (String [] args) {
        PaymentControl paymentControlTest = new PaymentControl();

        while(on) {
            try {

                Thread.sleep(2000);
                //Test some payment control stuff?

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
