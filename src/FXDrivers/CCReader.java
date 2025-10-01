package FXDrivers;

import IOPort.IOServer;
import Util.PortAddresses;

import java.util.Random;

/**
 * The credit card readers driver, basically just sends a random cc# when promted
 * to by the user, it also returns the bank responce.Just handles socket communication
 * * so fx class doesn't have to, that way if we ever have to switch up IOPorts
 * * the driver keeps any changes way from the gui code
 * Note about bank responce method:
 * Though it will be technically coming from main, the only reason main has
 * to talk to the ccReader is to pass along a message from the bank.
 */
public class CCReader {
    private IOServer server;
    private Random random = new Random(19);

    /**
     * Creates a server instance on ccreader port
     */
    public CCReader() {
        server = new IOServer(PortAddresses.CARD_READER_PORT);
    }

    /**
     * This sends a random credit card number to our client(main)
     * main should then pass it along to the bank
     */
    public void sendCCInfo() {
        int bounds = 12;
        StringBuilder aDinagaLing = new StringBuilder();
        for (int i = 0; i < bounds; i++) {
            aDinagaLing.append(random.nextInt(10));
        }
        server.send(aDinagaLing.toString());
    }

    /**
     * TODO:Talk to team about this <-- what about a boolean approved?
     * Returns response from the client, this should ONLY be approved/denied
     *
     * @return the servers response, this CAN be null, so maybe we put a wait in?
     */
    public String getBankRespnse() {
        return server.get();
    }
}
