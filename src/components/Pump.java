package components;

import IOPort.*;
import javafx.scene.control.ProgressBar;
import javafx.scene.shape.Rectangle;
import sim.Gas;
import Util.*;

import java.io.IOException;

/**
 * This class is a device that turns on and off the gas flow based of the
 * messages it recieves from main
 * Author: Danny Thompson
 */
public class Pump implements Runnable {
    private Rectangle rectangle;
    private Gas gas;
    private IOServer ioServer;
    private int gasType = 1;
    private ProgressBar progressBar;
    public Pump(){
        this.ioServer = new IOServer(PortAddresses.PUMP_PORT);
        this.gas = null;
        this.rectangle = null;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
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
        setProgressBarColor();
    }
    public void setProgressBarColor(){
        if(gasType == 1) {
            progressBar.setStyle("-fx-accent: brown;");
        }else if (gasType == 2) {
            progressBar.setStyle("-fx-accent: yellow;");
        }else{
            progressBar.setStyle("-fx-accent: green;");
        }
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
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

