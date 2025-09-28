package SecondLevel;

import IOPort.IOPort;
import Util.GasTypeEnum;
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
        String msg = gasStationClient.read();
        if(msg != null){
            String[] messages = msg.split(":");
            if(messages[0].equals("1")){
                receiveInfo(msg);
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
     * @param info
     */
    public void receiveInfo(String info) {
        LinkedList<Double> infoList = new LinkedList<>();
        String[] stings = info.split(":");
        for (int i = 1; i < stings.length-1;i++){
            infoList.add(Double.parseDouble(stings[i]));
        }
        this.onOff = false;
        if(stings[stings.length-1].equals("ON")){
            this.onOff = true;
        }
        this.prices = infoList;
    }

    /**
     * Sends the transaction made to the gas station
     */
    public void sendTransactionInfo (double dollarAmount, double volume,
                                     GasTypeEnum gasType){
        int gasT = gasType.intVersion;
        gasStationClient.send(String.format("1:%.2f:"+gasT+":%.2f",
                dollarAmount,volume));
    }
}
