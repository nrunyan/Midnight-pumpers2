package Tests;

import SecondLevel.PaymentServices;

/**
 * Another test main, handleMessages after starting the GUIs THIS IS LEGACY, THIS WON'T WORK ANYMORE
 */
public class TestPaymentControl {
    /**
     * starts
     * @param args no cmd args
     */

    public static boolean on = true;
    public static void main (String [] args) {
        PaymentServices paymentControlTest = new PaymentServices();

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
