package FXDrivers;

import IOPort.IOServer;
import Util.PortAddresses;

/**
 * This is the 'driver' for the hose, just handles socket communication
 * so fx class doesn't have to, that way if we ever have to switch up IOPorts
 * the driver keeps any changes way from the gui code
 */
public class Nozzle {
    private IOServer server;

    /**
     * Creates a server instance on hose port
     */
    public Nozzle() {
        server = new IOServer(PortAddresses.HOSE_PORT);
    }

    /**
     * When the connection is changed, an update is sent to the client at main
     * main should be reading this, so the connection info is stored
     * @param connectedInfo true/false depending on if the hose is plugged in
     */
    public void sendConnectionInfo(String connectedInfo){
        server.send(connectedInfo);
    }

}
