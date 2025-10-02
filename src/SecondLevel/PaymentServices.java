package SecondLevel;

import IOPort.IOPort;
import Util.CommunicationString;
import Util.CreditCardEnum;
import Util.PortAddresses;

/**
 * API class for BONK sever and CC reader
 * We can make this a runnable, please let me know what you think asap
 */
public class PaymentServices {
    private IOPort bonkClient;
    private IOPort CCReaderClient;
    private CreditCardEnum creditCardState=CreditCardEnum.NotPresent;

    /**
     * Creates client ioports to talk to the bank and ccReader. Bank shouldn't say anything other
     * than approved or denied, and that should be the only thing the outside world knows
     * the ccreader and bank send messages back and forth but that shouldn't be known outside of this
     * class
     */
    public PaymentServices(){
        bonkClient=new IOPort(PortAddresses.CARD_COMPANY_PORT);
        CCReaderClient=new IOPort(PortAddresses.CARD_READER_PORT);
    }

    /**
     * set the credit card state to not present
     */
    public void resetCreditCardState() {
        this.creditCardState = CreditCardEnum.NotPresent;
    }

    /**
     * Runs this operation.
     */

    public void handleMessages() {
        try {
            String bonkMessage=bonkClient.get();
            String readerMessage=CCReaderClient.get();
            if(bonkMessage!=null){
                System.out.println("Bank says: "+bonkMessage);
                handleBankMessage(bonkMessage);
            }
            if (readerMessage!=null){
                System.out.println("Credit card reader says "+readerMessage);
                handleReaderMessage(readerMessage);

            }
            Thread.sleep(500);

        } catch (InterruptedException e) {
            System.out.println("Some issue in payment control ");
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }

    /**
     * Sends the transaction info,
     * TODO:talk to team about this, where is it getting the transaction info? Pressumably the pump assembly
     *  how are we passing that in? How does the pump assembly have a reference to payment control?
     *   If its passed through main, then this should take args no?
     */
    public void sendTransactionInfo(double dollarAmount){
        String stringRep = String.format("%.2f", dollarAmount);
        bonkClient.send("$"+stringRep);

    }

    /**
     * So this should be the only thing seen to the outside world
     * This should be a state enum eventually
     * @return true if approved, false else
     */
    public CreditCardEnum getVerificationStatus(){
        return creditCardState;
    }

    /**
     * Records bank message and sends it to ccreader
     * @param bonkMessage approved, or denied, no handling for if the bank
     *                    sends you a null/ no card message
     */

    private void handleBankMessage(String bonkMessage){
        CCReaderClient.send(bonkMessage);
        if(bonkMessage.equals(CommunicationString.APPROVED)){
            //Eventually we want to abstract this to an enum state thing
            creditCardState=CreditCardEnum.Accepted;
        }else if(bonkMessage.equals(CommunicationString.DENIED)){
            creditCardState=CreditCardEnum.Declined;
        }

    }

    /**
     * deals with the credit card messages.
     * @param readerMessage what the ccreader says
     */
    private void handleReaderMessage(String readerMessage){
        creditCardState=CreditCardEnum.WaitingOnBonk;
        bonkClient.send(readerMessage);
        //I don't think anything else needs to happen here
    }
}
