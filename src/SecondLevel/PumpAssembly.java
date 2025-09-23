package SecondLevel;

import IOPort.IOPort;
import Util.CommunicationString;
import Util.PortAddresses;

public class PumpAssembly implements Runnable {
    private IOPort clientHose;
    private IOPort flowmeterClient;
    private IOPort pumpClient;
    private boolean connected=false;
    private double volumePumped=0.0;
    private boolean tankFull=false;
    private boolean gasOn=false;

    public PumpAssembly(){
        clientHose =new IOPort(PortAddresses.HOSE_PORT);
        flowmeterClient = new IOPort(PortAddresses.FLOW_METER_PORT);
        pumpClient = new IOPort(PortAddresses.PUMP_PORT);

    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        String hoseMessage=clientHose.get();
        String flowMeterMessage= flowmeterClient.get();
        String pumpMessage=pumpClient.get();
        while(true){
            try{
                if(flowMeterMessage!=null){
                    System.out.println("flowMeter says: "+flowMeterMessage);
                    handleflowMeterMessage();
                }
                if(hoseMessage!=null){
                    System.out.println("Hose says: "+hoseMessage);
                    handleHoseMessage(hoseMessage);
                }


            }catch(Exception e){
                System.out.println("Some issue in pump assembly");
                e.printStackTrace();
                throw e;
            }
        }
    }

    public void turnGasOn(){
        pumpClient.send("Gas on TODO");
        gasOn=false;
    }

    public void turnGasOff(){
        pumpClient.send("Gas OFF TODO");
        gasOn=false;

    }
    //I'm not sure we want this to be a string, it will be sent to us by the screen
    public void setGasSelection(String gasSelection){
        pumpClient.send(gasSelection);

    }
    public void reset(){
        connected=false;
        volumePumped=0.0;
        tankFull=false;
    }

    /**
     * THe only flow message we should ever get is tank full right? which means
     * turn off
     */

    private void handleflowMeterMessage(){
        pumpClient.send("Turn off");
        gasOn=false;

    }

    private void handleHoseMessage(String hoseMessage){
        if(hoseMessage.equals(CommunicationString.CONNECTED)){
            System.out.println("connected");
            connected=false;
        }else{
            connected=true;
        }
    }
}
