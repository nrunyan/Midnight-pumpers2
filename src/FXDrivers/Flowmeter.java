package FXDrivers;

import IOPort.*;
import sim.Gas;
import Util.*;

/**
 * This class is a device that tracks how much gas has been filled and
 * sends messages to main to update the flow value
 * Author: Danny Thompson
 */
public class Flowmeter extends Thread {
    private Gas gas;
    private double gasFlow;
    private IOServer ioServer;
    private boolean tankFull=false;

    public Flowmeter(){
        this.ioServer = new IOServer(PortAddresses.FLOW_METER_PORT);
        this.gas  = new Gas();
        this.gasFlow = 0;
        this.start();
    }

    public void sendFullMessage(){
        tankFull=true; // do we need this??
        ioServer.send(CommunicationString.TURN_OFF);
        gas.turnOnGas();
    }

    public boolean connected(){
        return ioServer.ON;
    }

    public double getGasFlow() {
        return gasFlow;
    }

    public void setGas(Gas gas) {
        this.gas = gas;
    }

    public Gas getGas() {return gas;}

    /*
    //TODO : send proper message for actuator
    This function checks to see if the gas is flowing and updates the flowmeter
     */
    @Override
    public void run() {
        while (true) {
            if (ioServer.ON) {
                if(ioServer.get() != null) {
                    gasFlow = 0;
                }

                if (gas!=null&&gas.isOnOff()) {
                    if(gasFlow>GasConstants.MAX_FLOW){
                        //ioServer.send(CommunicationString.TURN_OFF);
                    }else{
                        gasFlow += GasConstants.FLOW_RATE;
                        ioServer.send(String.valueOf(gasFlow));
                    }

                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

