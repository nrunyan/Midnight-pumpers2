package FXDrivers;

import IOPort.IOServer;
import Util.PortAddresses;

public class Nozzle {
    private IOServer server;

    public Nozzle() {
        server = new IOServer(PortAddresses.HOSE_PORT);
    }

    public void sendConnectionInfo(String connectedInfo){
        server.send(connectedInfo);
    }

}
