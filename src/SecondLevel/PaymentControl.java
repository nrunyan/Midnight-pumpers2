package SecondLevel;

import IOPort.IOPort;
import Util.CommunicationString;
import Util.PortAddresses;

/**
 * API class for BONK sever and CC reader
 * We can make this a runnable, please let me know what you think asap
 */
public class PaymentControl implements Runnable{
    private IOPort bonkClient;
    private IOPort CCReaderClient;
    private boolean approved=false;  //THIS IS JUST FOR TESTING, THIS should be changed to a state enum

    /**
     * Creates client ioports to talk to the bank and ccReader. Bank shouldn't say anything other
     * than approved or denied, and that should be the only thing the outside world knows
     * the ccreader and bank send messages back and forth but that shouldn't be known outside of this
     * class
     */
    public PaymentControl(){
        bonkClient=new IOPort(PortAddresses.CARD_COMPANY_PORT);
        CCReaderClient=new IOPort(PortAddresses.CARD_READER_PORT);
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        while (true){
            try {
                String bonkMessage=bonkClient.get();
                String readerMessage=CCReaderClient.get();
                if(bonkMessage==null){
                    System.out.println("Bank says: NULL");
                }else{
                    System.out.println("Bank says: "+bonkMessage);
                    handleBankMessage(bonkMessage);
                }
                if (readerMessage==null){
                    System.out.println("Credit card reader says NULL");

                }else{
                    handleReaderMessage(readerMessage);

                }
                //THIS IS JUST LEFT IN FOR TESTING I PROMISE QUEENS
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                System.out.println("Some issue in payment control ");
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }
    }

    /**
     * So this should be the only thing seen to the outside world
     * This should be a state enum eventually
     * @return true if approved, false else
     */
    public boolean getApprovmentStatus(){
        return approved;
    }

    private void handleBankMessage(String bonkMessage){
        CCReaderClient.send(bonkMessage);
        if(bonkMessage.equals(CommunicationString.APPROVED)){
            //Eventually we want to abstract this to an enum state thing
            approved=true;
        }else{
            approved=false;
        }

    }
    private void handleReaderMessage(String readerMessage){
        bonkClient.send(readerMessage);
        //I don't think anything else needs to happen here
    }
}
