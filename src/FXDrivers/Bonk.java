package FXDrivers;

import IOPort.IOServer;
import Util.CommunicationString;
import Util.PortAddresses;

import java.util.Random;

/**
 * A very basic bank implementation, just blindly accepts and even cc#
 * and denies any odd cc#
 * I know this is a little odd that its in the driver class, when it has a main
 * but that is the only thing about it that doesn't make it like our other 'drivers'
 */
public class Bonk {
    private IOServer server;
    private Random random=new Random(19);
    private double moneyMade=0.0;

    /**
     * So even though this isn't a GUI, and may never be, it still should be treated
     * like one of the interfaces rn for testing
     */
    public Bonk(){
        server=new IOServer(PortAddresses.CARD_COMPANY_PORT);
        System.out.println("Bonk online");
    }

    /**
     * Very basic approval system, any odd card gets denied any even gets approved
     */
    public void ApproveOrDeny(){
        String msg=server.get();
        if(msg!=null){
            if(msg.startsWith("$")){
                moneyMade+=Double.valueOf(msg.substring(1));
            }else{
                // int ccNum=Integer.parseInt(msg); doesnt work lmao hdahahhqmsdsdjugigi
                if(random.nextInt()%2==0){
                    server.send(CommunicationString.APPROVED);
                }else{
                    server.send(CommunicationString.DENIED);
                }

            }


        }else{
            System.out.println("bonk gets null msg");
        }

    }

    /**
     * Waits for 1 second between each test to see if its ioport has heard anything
     * @param args
     */

    public static void main(String args[]){
        Bonk bonk=new Bonk();
        while(true){
            try {
                bonk.ApproveOrDeny();
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
    /**
     * Gang correct me if im wrong but the bonk just has to respond to msgs right?
     */
}
