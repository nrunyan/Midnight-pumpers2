package Tests;

import SecondLevel.GasStation;
import Util.GasTypeEnum;

public class TestGasStationControl {
    public static void main(String[] args) throws InterruptedException {
        GasStation gasStation = new GasStation();
        GasTypeEnum gasGrade = GasTypeEnum.GAS_TYPE_1;
        while (true){
            gasStation.handleMessage();
            gasStation.sendTransactionInfo(10,5, gasGrade);
            for (int i = 0; i < gasStation.getNewPrices().size();i++){
                System.out.print(gasStation.getNewPrices().get(i)+" ");
                if(gasStation.getNewPrices().get(0) < 1){
                    gasGrade = GasTypeEnum.GAS_TYPE_2;
                }
                if(gasStation.getNewPrices().get(1) < 1){
                    gasGrade = GasTypeEnum.GAS_TYPE_3;
                }
                if(gasStation.getNewPrices().get(2) < 1){
                    gasGrade = GasTypeEnum.GAS_TYPE_4;
                }
                if(gasStation.getNewPrices().get(3) < 1){
                    gasGrade = GasTypeEnum.GAS_TYPE_5;
                }
            }
            System.out.print("\n");
            System.out.println(gasStation.checkPower());
            Thread.sleep(1000);
        }
    }
}
