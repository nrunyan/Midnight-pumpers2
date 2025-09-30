package IOPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class is used to communicate Main -> JavaFX
 * This class instantiates a server socket
 */
public class IOServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private int portNumber;
    public volatile boolean ON = false;
    public volatile boolean CONNECTED = false;
    /**
     *  This enables the differentation between read and get.
     *  It stores any message received in this variable, and clears it when
     *  get is called, returns it when read is called.
     */
    public volatile String message;
    /**
     * Creates an IOPort.IOPort instance, the IOPort.IOPort automatically stores any messages
     * sent to it. USE THIS ONLY FOR JAVAFX management.
     * @param portNumber The connection, see portAddresses for more info
     */
    public IOServer(int portNumber) {
        this.portNumber = portNumber;
        try {
            serverSocket = new ServerSocket(portNumber);
            //clientSocket = serverSocket.accept();
            //in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //out = new PrintWriter(clientSocket.getOutputStream(), true);
            ON = true;
        } catch (IOException e) {
            System.out.println("Trouble Connecting to " + portNumber);
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            while(true){
                try {
                    clientSocket = serverSocket.accept();
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    CONNECTED = true;
                    String msg; //THIS IS WHY WE NEED THREADS
                    //THIS WILL HANG ALL OUR STUFF IF WE DON'T HAVE THREADS
                    while ((msg = in.readLine()) != null) {
                        message=msg;

                    }
                } catch (IOException e) {
                    System.out.println("Issue ");
                }

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
     *
     * @return String containing the message.
     */
    public synchronized String get() {
        String temp = message;
        message = null;
        return temp;
    }

    /**
     * @return String message
     * @throws IOException
     */
    public String read() throws IOException {
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
            System.out.println("Could not close socket");
            throw new RuntimeException(e);
        }
    }
}