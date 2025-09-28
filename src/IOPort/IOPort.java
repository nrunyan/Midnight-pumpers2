package IOPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class is used to communicate Main -> JavaFX
 * This class instantiates a client socket
 */
public class IOPort {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private int portNumber;
    /**
     *  This enables the differentation between read and get.
     *  It stores any message received in this variable, and clears it when
     *  get is called, returns it when read is called.
     */
    public String message;

    /**
     * Creates an IOPort.IOPort instance, the IOPort.IOPort automatically stores any messages
     * sent to it
     * @param portNumber The connection, see portAddresses for more info
     */
    public IOPort(int portNumber) {
        this.portNumber = portNumber;
        try {
            clientSocket = new Socket("localHost", portNumber);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Trouble Connecting to " + portNumber);
            throw new RuntimeException(e);
        }
        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    message=msg;
                    System.out.println("Server says: " + msg);  //Print statments for testing
                }
            } catch (IOException e) {
                System.out.println("Connection closed.");
            }
        }).start();


    }

    /**
     * Allows for sending
     *
     * @param message String message sent to
     */
    public void send(String message) {
        out.println(message);
    }

    /**
     * returns message. throws message away after.
     * @return String containing the message.
     */
    public String get() {
        String temp = message;
        message = null;
        return temp;
    }

    /**
     *
     * @return

     */
    public String read()  {
        return message;
    }

    /**
     * closes sockets gracefully
     */
    public void close() {
        try {
            clientSocket.close();
            System.out.println("Server closed");
        } catch (IOException e) {
            System.out.println("Could not close client socket");
            throw new RuntimeException(e);
        }
    }
}