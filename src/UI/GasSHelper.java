package UI;

import FXDrivers.GasStationServer;
import javafx.scene.text.Text;

import java.util.LinkedList;

public class GasSHelper implements Runnable {
    private GasStationServer gasStationServer;
    private Text moneyMade;
    private LinkedList<Text> gasPrices;
    public GasSHelper(GasStationServer gasStationServer){
        this.gasStationServer = gasStationServer;
    }
    public void addGasPrice(Text gasPrice){
        gasPrices.add(gasPrice);
    }
    @Override
    public void run() {
        while(true){
            for(int i = 0; i<gasPrices.size();i++){
                gasPrices.get(i).setText(String.format("%.2f",
                        gasStationServer.getGasPrices()[i]));
            }
            moneyMade.setText(String.format("%.2f",
                    gasStationServer.getTotalMoney()));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
