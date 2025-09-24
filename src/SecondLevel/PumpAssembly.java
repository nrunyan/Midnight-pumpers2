package SecondLevel;

import IOPort.IOPort;
import Util.CommunicationString;
import Util.PortAddresses;

public class PumpAssembly extends Thread {
    private IOPort clientHose;
    private IOPort flowmeterClient;
    private IOPort pumpClient;
    private boolean connected=false;
    private double volumePumped=0.0;
    private boolean tankFull=false;
    private volatile boolean gasOn=false;

    public PumpAssembly(){
        clientHose =new IOPort(PortAddresses.HOSE_PORT);
        flowmeterClient = new IOPort(PortAddresses.FLOW_METER_PORT);
        pumpClient = new IOPort(PortAddresses.PUMP_PORT);
        this.start();

    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {

        while(true){
            String hoseMessage=clientHose.get();
            String flowMeterMessage= flowmeterClient.get();
            String pumpMessage=pumpClient.get(); //should pump really say anything
            try{
                if(flowMeterMessage!=null){
                    System.out.println("flowMeter says: "+flowMeterMessage);
                    handleflowMeterMessage(flowMeterMessage);
                }
                if(hoseMessage!=null){
                    System.out.println("Hose says: "+hoseMessage);
                    handleHoseMessage(hoseMessage);
                }
                Thread.sleep(50);


            }catch(Exception e){
                System.out.println("Some issue in pump assembly");
                e.printStackTrace();

                try {
                    throw e;
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
    }

    public boolean getPumpingStatus(){
        return gasOn;
    }

    public double getGasPumped() {
        return volumePumped;
    }

    public void pumpOn(String gasType){
        pumpClient.send(gasType);
        gasOn=true;
    }

    public void pumpOff(){
        pumpClient.send(CommunicationString.TURN_OFF);
        gasOn=false;

    }



    public void reset(){
        connected=false;
        volumePumped=0.0;
        tankFull=false;
    }





    /**
     * THe only flow message we should ever get is tank full and volume? which means
     * turn off
     */
    private void handleflowMeterMessage(String flowMeter){
        if(flowMeter.equals(CommunicationString.TURN_OFF)){
            pumpClient.send(CommunicationString.TURN_OFF);
            gasOn=false;
        }else{
            volumePumped=Double.parseDouble(flowMeter);
        }


    }

    private void handleHoseMessage(String hoseMessage){
        if(hoseMessage.equals(CommunicationString.CONNECTED)){
            System.out.println("connected");
            connected=true;
        }else{
            connected=false;
        }
    }
}
