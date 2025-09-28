package Tests;

import SecondLevel.PumpAssembly;
import Util.CommunicationString;
import Util.GasTypeEnum;

/**
 * Just a basic test main, handleMessages me last after starting the guis
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
               pumpAssemblyTest.pumpOn(GasTypeEnum.GAS_TYPE_1);
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