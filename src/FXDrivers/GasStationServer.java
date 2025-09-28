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
     * "1:transtationAmount" adds transtion to total money
     * "NEW_PRICES" tells GasStationSever to sendPrices
     * "ON?" GasStation2L is asking if the station is on
     * "2:x:y" x = gastype y=gasAmount
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
                setTotalMoney(Double.parseDouble(message[1])); //adds the
                // transaction fee to money made
                makePrices();// every transaction we change the price I
                // guess, if we want we can change when we update prices
            } else if (msg.equals("NEW_PRICES")) {
                sendOutPrices();
            } else if (msg.equals("ON?")) {
                sendIsON();
            } else if(message[0].equals("2")){// will be a message with how
                // much gas was used for some gastype of string form "2:x:y"
                // where
                // x = gastype y=gasAmount
                int x = Integer.parseInt(message[1]); // This will be "x"
                double y = Double.parseDouble(message[2]); // This will be "y"
                gasUsed(x,y);
            }
        }

    }

    public void sendIsON() {
        if(ON){
            ioServer.send("ON");
        }else {
            ioServer.send("OFF");
        }
    }
    public void sendMoney(){
        ioServer.send(String.valueOf(String.format("2:%.2f",
                totalMoney)));
        //String form "2:totalMoney" 2 is used for handling again we can
        // change how we want to handle this string so dont be afraid to make
        // changes if its easier to handle it a different way
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
    private void setTotalMoney(double money){
        totalMoney += money;
    }

    /**
     * Updates the gas prices
     * @param index which gas, each gas corresponds to an index -1
     * @param newAmount the new price for the gas
     */
    public void updateGasPrices(int index, double newAmount){
        gasPrices[index]=newAmount;
    }
    public void sendOutPrices(){
        //dont know if we want this but I thought it might be easy to split
        // up the string into an array of string but I might be dumb
        //String Form "1:p1:p2:p3:p4:p5" p_i = price of grade i the 1 lets
        // us know how we should handle the string
        ioServer.send(String.format("1:%.2f:%.2f:%.2f:%.2f:%.2f",
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
        while (true){
            try {
                makePrices();
                handleMessage();
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
