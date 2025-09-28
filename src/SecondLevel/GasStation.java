package SecondLevel;

import IOPort.IOPort;
import Util.PortAddresses;

import java.util.LinkedList;
import java.util.List;

public class GasStation {
    private IOPort gasStationClient;
    private LinkedList<Double> prices;
    private boolean onOff;
    public GasStation(){
        gasStationClient = new IOPort(PortAddresses.GAS_STATION_PORT);
        prices = new LinkedList<>();
        onOff = false;
    }

    /**
     * Handles the messages that it is getting from gasStationSever
     */
    public void handleMessage(){
        String msg = gasStationClient.get();
        if(msg != null){
            String[] messages = msg.split(":");
            if(messages[0].equals("1")){
                setPrices(msg);
            } else if (msg.equals("ON")) {
                onOff = true;
            } else if(msg.equals("OFF")){
                onOff = false;
            }
        }
    }


    /**
     * Is telling the Pump manager if we should turn on the gas station or not.
     * @return
     */

    public  boolean checkPower(){
        return onOff;
    }


    /**
     * Gets the new prices from the gas station.
     * @return
     */
    public List<Double> getNewPrices(){
        return prices;
    }

    /**
     * Gets set from a string that it gets from the GasStationServer
     * @param priceS
     */
    public void setPrices(String priceS) {
        LinkedList<Double> priceList = new LinkedList<>();
        String[] stings = priceS.split(":");
        for (int i = 1; i < stings.length;i++){
            priceList.add(Double.parseDouble(stings[i]));
        }
        this.prices = priceList;
    }

    /**
     * Sends the transaction made to the gas station
     */
    public void sendTransactionInfo (double dollarAmount, double volume,
                                     int gasType){
        gasStationClient.send(String.format("1:%.2f",dollarAmount));
        gasStationClient.send(String.format("2:x:%.2f",volume));
    }

}
