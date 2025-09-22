package components;

import IOPort.*;
import sim.Gas;
import Util.*;

import java.io.IOException;

/**
 * This class is a device that turns on and off the gas flow based of the
 * messages it recieves from main
 * Author: Danny Thompson
 */
public class Pump implements Runnable {
    private Gas gas;
    private IOServer ioServer;
    private int gasType = 1;
    public Pump(){
        this.ioServer = new IOServer(PortAddresses.PUMP_PORT);
        this.gas = null;
    }

    public int getGasType() {
        return gasType;
    }
    public void setGas(Gas gas) {
        this.gas = gas;
    }
    public void turnOn(){
        this.gas.turnOnGas();
    }
    public void turnOf(){
        this.gas.turnOffGas();
    }
    public void setGasType(int gasType){
        this.gasType = gasType;
    }
    /*
        //TODO needs to read it's status so that it can turn on the pump needs a
           proper message to be able to read
         */
    @Override
    public void run() {
        while(true) {
            if (ioServer.ON) {
                String inbox = null;
                try {
                    inbox = this.ioServer.read();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (inbox != null) {
                    String message = null;
                    try {
                        message = this.ioServer.read();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (message.equals("SOMETHING THAT MEANS ON")) {
                        turnOn();
                    } else if (message.equals("SOMETHING THAT MEANS OFF")) {
                        turnOf();
                    } else if (message.equals("SOMETHING THAT MEANS GASTYPE")) {
                        setGasType(message.charAt(1)); // will need update
                    }
                }
            }
        }
    }
}

