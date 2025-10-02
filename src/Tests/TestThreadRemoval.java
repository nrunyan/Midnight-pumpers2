package Tests;

import SecondLevel.PaymentServices;

public class TestThreadRemoval {
    /**
     * Testing for payment control without threads
     * @param args no arguments
     */

    public static void main(String[] args) {
        PaymentServices paymentControlTest = new PaymentServices();
        while(true){
            paymentControlTest.handleMessages();
            try {
                Thread.sleep(1000); // long wait time that will be reflected in testing
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
