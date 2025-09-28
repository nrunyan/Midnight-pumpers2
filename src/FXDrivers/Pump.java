package FXDrivers;

import IOPort.*;
import sim.Gas;
import Util.*;

/**
 * This class is a device that turns on and off the gas flow based of the
 * messages it recieves from main
 * Author: Danny Thompson
 */
public class Pump extends Thread {
    private Gas gas;
    private IOServer ioServer;
    private int gasType = 1;
    public Pump(){
        this.ioServer = new IOServer(PortAddresses.PUMP_PORT);
        this.gas = null;
        this.start();
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
            String message=ioServer.get();
            if(message!=null){
                System.out.println("Pump gets message: "+message);
                switch(message){
                    case "Customer wants gas 1":
                        System.out.println("turning gas on");
                        setGasType(1);
                        turnOn();
                        break;
                    case "Customer wants gas 2":
                        System.out.println("turning gas on");
                        setGasType(2);
                        turnOn();
                        break;
                    case "Customer wants gas 3":
                        System.out.println("turning gas on");
                        setGasType(3);
                        turnOn();
                        break;
                    case "Customer wants gas 4":
                        System.out.println("turning gas on");
                        setGasType(4);
                        turnOn();
                        break;
                    case "Customer wants gas 5":
                        System.out.println("turning gas on");
                        setGasType(5);
                        turnOn();
                        break;
                    case "Turn OFF pump":
                        turnOf();
                        break;
                    default:
                        System.out.println("Turning off");
                        turnOf();

                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else{
                //System.out.println("Null message at pump");
            }
        }
    }
}

