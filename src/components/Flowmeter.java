package components;

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
        this.gas = null;
        this.gasFlow = 0;
        this.start();
    }

    public void sendFullMessage(){
        tankFull=true;
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
                    if(gasFlow>7.4){ //TODO:DONT HARDCODE THIS
                        ioServer.send(CommunicationString.TURN_OFF);
                    }else{
                        gasFlow += 0.02;
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

