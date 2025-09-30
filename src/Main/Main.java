package Main;

import FXDrivers.GasStationServer;
import IOPort.IOPort;
import SecondLevel.Customer;
import SecondLevel.GasStation;
import SecondLevel.PaymentControl;
import SecondLevel.PumpAssembly;
import Util.CommunicationString;
import Util.GasTypeEnum;
import Util.PortAddresses;
import Main.PumpManager;

/**
 * Just a basic test main, run me last after starting the guis
 */

public class Main {
    /**
     * starts the bank, the cc machine, and the hose
     * @param args no cmd args
     */
    public static void main(String[] args) {
        PaymentControl pc = new PaymentControl();
        Customer cust = new Customer();
        GasStation gasStationServer = new GasStation();
        PumpAssembly pump = new PumpAssembly();
        PumpManager pm = new PumpManager(pc, cust,gasStationServer,pump);
    }
}