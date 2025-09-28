package Main;

import FXDrivers.GasStationServer;
import SecondLevel.Customer;
import SecondLevel.GasStation;
import SecondLevel.PaymentControl;
import SecondLevel.PumpAssembly;

/**
 * Authors: The Midnight Pumpers
 */

public class Main {
    /**
     * starts the bank, the cc machine, and the hose
     * @param args no cmd args
     */
    public static void main(String[] args) {
        PaymentControl pc = new PaymentControl();
        Customer cust = new Customer();
        GasStation gasStation = new GasStation();
        PumpAssembly pump = new PumpAssembly();
        PumpManager pm = new PumpManager(pc, cust, gasStation ,pump);
    }
}