package UI;

import FXDrivers.GasStationServer;
import javafx.scene.text.Text;

import java.util.LinkedList;

public class GasSHelper implements Runnable {
    private GasStationServer gasStationServer;
    private Text moneyMade;
    private LinkedList<Text> gasPrices;
    private LinkedList<Text> gasVolumes;

    /**
     * This just handles the changes in GasStation GUI and updates it
     * @param gasStationServer
     */
    public GasSHelper(GasStationServer gasStationServer){
        this.gasStationServer = gasStationServer;
        gasPrices = new LinkedList<>();
        gasVolumes = new LinkedList<>();
    }

    /**
     * adds the gasPrices texts to a linked list
     * @param gasPrice
     */
    public void addGasPrice(Text gasPrice){
        gasPrices.add(gasPrice);
    }

    /**
     * adds the gasVolume texts to a linked list
     * @param gasVolume
     */
    public void addGasVolume(Text gasVolume){
        gasVolumes.add(gasVolume);
    }

    /**
     * sets the text that shows the amount of money that the gasStation has made
     * @param moneyMade
     */
    public void setMoneyMade(Text moneyMade) {
        this.moneyMade = moneyMade;
    }

    /**
     * Handles all changes made to GasStation and updates the GUI
     */
    @Override
    public void run() {
        while(true){
            //Loop is used to update the gasPrices and gasVolumes
            for(int i = 0; i<gasPrices.size();i++){
                gasPrices.get(i).setText(String.format("%.2f",
                        gasStationServer.getGasPrices()[i]));
                gasVolumes.get(i).setText(String.format("%.1f",
                        gasStationServer.getGasGallons()[i]));
            }
            //This is where we update the Text for money  made
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
