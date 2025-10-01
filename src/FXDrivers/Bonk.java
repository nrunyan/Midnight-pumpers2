package FXDrivers;

import IOPort.IOServer;
import UI.BankGUI;
import Util.CommunicationString;
import Util.PortAddresses;
import javafx.application.Platform;

import java.util.Random;

/**
 * A very basic bank implementation, just blindly accepts and even cc#
 * and denies any odd cc#
 * I know this is a little odd that its in the driver class, when it has a main
 * but that is the only thing about it that doesn't make it like our other 'drivers'
 */
public class Bonk {
    private IOServer server;
    private Random random=new Random(2);
    private BankGUI bankGUI = null;
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
                if (bankGUI != null) {
                    // Update GUI if it exists
                    Platform.runLater(() -> {
                        bankGUI.moneyTransfer(msg);
                    });
                }
            }else{
                // int ccNum=Integer.parseInt(msg); doesnt work lmao hdahahhqmsdsdjugigi
                if(random.nextInt()%2==0){
                    if (bankGUI != null) {
                        // Update GUI if it exists
                        Platform.runLater(() -> {
                            bankGUI.displayApproved(msg);
                        });
                    }
                    server.send(CommunicationString.APPROVED);
                }else{
                    if (bankGUI != null) {
                        // Update GUI if it exists
                        Platform.runLater(() -> {
                            bankGUI.displayDenied(msg);
                        });
                    }
                    server.send(CommunicationString.DENIED);
                }

            }



        }

    }

    /**
     * approve or deny the following message
     * This is for testing purposes
     * @param msg
     */
    public void ApproveOrDeny(String msg) {
        if(msg!=null){
            if(msg.startsWith("$")){
                moneyMade+=Double.valueOf(msg.substring(1));
                if (bankGUI != null) {
                    // Update GUI if it exists
                    Platform.runLater(() -> {
                        bankGUI.moneyTransfer(msg);
                    });
                }
            }else{
                // int ccNum=Integer.parseInt(msg); doesnt work lmao hdahahhqmsdsdjugigi
                if(random.nextInt()%2==0){
                    if (bankGUI != null) {
                        // Update GUI if it exists
                        Platform.runLater(() -> {
                            bankGUI.displayApproved(msg);
                        });
                    }
//                    server.send(CommunicationString.APPROVED);
                }else{
                    if (bankGUI != null) {
                        // Update GUI if it exists
                        Platform.runLater(() -> {
                            bankGUI.displayDenied(msg);
                        });
                    }
//                    server.send(CommunicationString.DENIED);
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

//    public static void main(String args[]){
//        Bonk bonk=new Bonk();
//        while(true){
//            try {
//                bonk.ApproveOrDeny();
//                Thread.sleep(1000);
//
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//    }

    /**
     * Set this Bank's Bank Gui
     * @param bankGUI the bank gui to update
     */
    public void setBankGUI(BankGUI bankGUI) {
        this.bankGUI = bankGUI;
    }

    /**
     * Gang correct me if im wrong but the bonk just has to respond to msgs right?
     */
}
