import IOPort.IOPort;
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
       IOPort clientHose =new IOPort(PortAddresses.HOSE_PORT);
       IOPort CCHose =new IOPort(PortAddresses.CARD_READER_PORT);
       IOPort bankClient=new IOPort(PortAddresses.CARD_COMPANY_PORT);
       while(true){
           try {
               String clientMsg=clientHose.get();
               String ccMsg=CCHose.get();
               String bankMsg=bankClient.get();
               if(clientMsg==null){
                   System.out.println("NO Message Null");
               }else{
                   System.out.println("Client says: "+clientMsg);
               }
               if (ccMsg!=null){
                   bankClient.send(ccMsg);
               }
               if(bankMsg!=null){
                   CCHose.send(bankMsg);
               }
               Thread.sleep(1000);

           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }
       }
    }
}