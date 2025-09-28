package Tests;

import SecondLevel.PaymentControl;

public class TestThreadRemoval {
    /**
     * Testing for payment control without threads
     * @param args no arguments
     */

    public static void main(String[] args) {
        PaymentControl paymentControlTest = new PaymentControl();
        while(true){
            paymentControlTest.run();
            try {
                Thread.sleep(1000); // long wait time that will be reflected in testing
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
