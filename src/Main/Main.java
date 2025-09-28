import FXDrivers.GasStationServer;
import IOPort.IOPort;
import SecondLevel.Customer;
import SecondLevel.PaymentControl;
import SecondLevel.PumpAssembly;
import Util.CommunicationString;
import Util.GasTypeEnum;
import Util.PortAddresses;

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
        GasStationServer gasStationServer = new GasStationServer();
        PumpAssembly pump = new PumpAssembly();
        PumpManager pm = new PumpManager(pc, cust,gasStationServer,pump);
    }
}