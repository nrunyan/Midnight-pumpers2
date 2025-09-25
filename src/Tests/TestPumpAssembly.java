package Tests;

import SecondLevel.PumpAssembly;
import Util.CommunicationString;

/**
 * Just a basic test main, run me last after starting the guis
 */

public class TestPumpAssembly {
    /**
     * starts the bank, the cc machine, and the hose
     * @param args no cmd args
     */
    public static void main(String[] args) {
        PumpAssembly pumpAssemblyTest=new PumpAssembly();

       while(true){
           try {

               Thread.sleep(2000);
               pumpAssemblyTest.pumpOn(CommunicationString.GAS1_SELECTED);
               System.out.println("Trying to turn gas on");
               Thread.sleep(2000);
               System.out.println(pumpAssemblyTest.getGasPumped());
               System.out.println("trying to get volume");
               Thread.sleep(2000);
               pumpAssemblyTest.pumpOff();
               System.out.println("trying to turn gas off");

           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }
       }
    }
}