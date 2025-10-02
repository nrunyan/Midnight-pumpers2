package Main;

import SecondLevel.Customer;
import SecondLevel.GasStation;
import SecondLevel.PaymentServices;
import SecondLevel.PumpAssembly;
/**
 * Just a basic test main, run me last after starting the guis
 */

public class Main {
    /**
     * starts the bank, the cc machine, and the hose
     * @param args no cmd args
     */
    public static void main(String[] args) {
        PaymentServices pc = new PaymentServices();
        Customer cust = new Customer();
        GasStation gasStation = new GasStation();
        PumpAssembly pump = new PumpAssembly();
        PumpManager pm = new PumpManager(pc, cust, gasStation,pump);
        pm.handleSystem();
    }
}