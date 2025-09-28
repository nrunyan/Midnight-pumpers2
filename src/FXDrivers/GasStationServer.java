package FXDrivers;

import IOPort.IOServer;
import Util.*;

import java.util.Random;

public class GasStationServer extends Thread{
    private double[] gasPrices;
    private IOServer ioServer;
    private double[] gasGallons;
    private double totalMoney;
    private boolean ON;
    public GasStationServer(){
        gasPrices=new double[]{1,2,3,4,5};
        gasGallons=new double[]{100,100,100,100,100};
        ioServer = new IOServer(PortAddresses.GAS_STATION_PORT);
        totalMoney = 0;
        this.start();
    }

    /**
     * Send out strings Forms
     * "1:p1:p2:p3:p4:p5" sends gas prices
     *
     * "ON" gasStation on
     * "OFF" gasStation off
     */

    /**
     * Messages Getting
     * "1:transtationAmount:x:y" x = gastype y=gasAmount adds transtion to
     * total money
     * "NEW_PRICES" tells GasStationSever to sendPrices
     * "ON?" GasStation2L is asking if the station is on
     */

    /**
    In my mind im seeing the messages coming in the form "id:Message" where id
    is used to know how to handle the message
     */
    private void handleMessage(){
        String msg = ioServer.get();
        if(msg != null){
            String[] message = msg.split(":");
            //1:transtationAmount
            if(message[0].equals("1")){ // how much money made from transaction
                receiveInfo(Double.parseDouble(message[1]),
                        Integer.parseInt(message[2]),
                        Double.parseDouble(message[3]));
                sendOutInfo();
            }
        }

    }

    public double getTotalMoney() {
        return totalMoney;
    }

    private void gasUsed(int gasG, double gasV){
        gasGallons[gasG-1] -= gasV;
        if(gasGallons[gasG-1] < 0){
            gasPrices[gasG-1] = 0;
        }
    }
    private void receiveInfo(double money,int gasType, double volume){
        totalMoney += money;
        gasUsed(gasType,volume);
    }

    /**
     * Updates the gas prices
     * @param index which gas, each gas corresponds to an index -1
     * @param newAmount the new price for the gas
     */
    public void updateGasPrices(int index, double newAmount){
        gasPrices[index]=newAmount;
    }
    public void sendOutInfo(){
        //dont know if we want this but I thought it might be easy to split
        // up the string into an array of string but I might be dumb
        //String Form "1:p1:p2:p3:p4:p5:ON_OFF" p_i = price of grade i the 1
        // lets
        // us know how we should handle the string
        //
        String on = "OFF";
        if(ON){
            on = "ON";
        }
        ioServer.send(String.format("1:%.2f:%.2f:%.2f:%.2f:%.2f:"+on,
                gasPrices[0],gasPrices[1],gasPrices[2],gasPrices[3],
                gasPrices[4]));
    }
    private void makePrices(){
        //makes a random price from 1 to 4
        Random random = new Random();
        double min = 1.0;
        double max = 4.0;
        double newPrice = min + (random.nextDouble() * (max - min));
        for(int i = 0;i < gasPrices.length;i++){
            if(!(gasPrices[i] < 1)){ // if the gasPrice of any gasType is 
                                     // less than one that means there is no 
                                     // more of that type
                updateGasPrices(i,newPrice);
            }
            newPrice += .30; // each grade will be a little bit more money
        }
    }

    /**
     *
     * @return The array of gas prices.
     */

    public double[] getGasPrices() {
        return gasPrices;
    }

    public double[] getGasGallons() {
        return gasGallons;
    }

    @Override
    public void run() {

        ON =true;
        sendOutInfo();
        while (true){
            try {
                makePrices();
                handleMessage();
                if(!ON){
                    sendOutInfo();
                }
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
