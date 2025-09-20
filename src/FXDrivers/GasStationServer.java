package FXDrivers;

public class GasStationServer {
    private double[] gasPrices;
    public GasStationServer(){
        gasPrices=new double[]{1,2,3,4,5};

    }

    public void handleMessage(){

    }

    /**
     * Updates the gas prices
     * @param index which gas, each gas corresponds to an index -1
     * @param newAmount the new price for the gas
     */
    public void updateGasPrices(int index, int newAmount){
        gasPrices[index]=newAmount;
    }

    /**
     *
     * @return The array of gas prices.
     */

    public double[] getGasPrices() {
        return gasPrices;
    }
}
