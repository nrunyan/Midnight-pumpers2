import FXDrivers.GasStationServer;
import SecondLevel.Customer;
import SecondLevel.PaymentControl;
import SecondLevel.PumpAssembly;
import Util.GasTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the controller for the system.
 * Uses the gas pump API to emulate the finite state machine
 * Authors: Valerie Barker, Youssef Amin
 */
public class PumpManager {
    private PaymentControl paymentControl;
    private Customer customer;
    private GasStationServer gasStationServer;
    private PumpAssembly pumpAssembly;
    private Boolean ON;
    private List<Double> New_Price_List;
    private List<Double> In_Use_Price_List;
    private GasTypeEnum Gas_Grade_Selection;
    public PumpManager(PaymentControl paymentControl, Customer customer, GasStationServer gasStationServer, PumpAssembly pumpAssembly) {
        this.paymentControl = paymentControl;
        this.customer = customer;
        this.gasStationServer = gasStationServer;
        this.pumpAssembly = pumpAssembly;
        ON = true;
    }

    /**
     * This is the finite state machine
     * The flow of the program
     */
    public void handleSystem() {
        while (ON) {
            // DO STUFF
        }
    }

    /**
     * allows us to set the price list at the start of each transaction
     * @param new_Price_List The list of prices from Gas Station GUI
     */
    private void setNew_Price_List(List<Double> new_Price_List){
        this.New_Price_List = new_Price_List;
    }

    /**
     * This method is used to set the in use price list for a transaction
     */
    private void update_In_Use_Price_List(){
        this.In_Use_Price_List = New_Price_List;
    }

    /**
     * Allows us to set the gas grade selection retrieved from customer
     * @param gas_Grade_Selection the level of gas the user has selected
     */
    private void setGas_Grade_Selection(GasTypeEnum gas_Grade_Selection){
        this.Gas_Grade_Selection = gas_Grade_Selection;
    }


}
