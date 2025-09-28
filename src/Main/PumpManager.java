package Main;

import SecondLevel.Customer;
import SecondLevel.GasStation;
import SecondLevel.PaymentControl;
import SecondLevel.PumpAssembly;
import Util.CreditCardEnum;
import Util.GasTypeEnum;

import java.util.List;

/**
 * This class is the controller for the system.
 * Uses the gas pump API to emulate the finite state machine
 * Authors: Valerie Barker, Youssef Amin
 */
public class PumpManager {
    private PaymentControl paymentControl;
    private Customer customer;
    private GasStation gasStation;
    private PumpAssembly pumpAssembly;
    private Boolean ON;
    private List<Double> New_Price_List;
    private List<Double> In_Use_Price_List;
    private GasTypeEnum Gas_Grade_Selection;

    public PumpManager(PaymentControl paymentControl, Customer customer, GasStation gasStation, PumpAssembly pumpAssembly) {
        this.paymentControl = paymentControl;
        this.customer = customer;
        this.gasStation = gasStation;
        this.pumpAssembly = pumpAssembly;
        ON = true;
    }

    /**
     * This is the finite state machine
     * The flow of the program
     */
    public void handleSystem() {
        while (ON) {
            off();
        }
    }

    /**
     * This metho is the startup sequence
     * Theta on the finite state machine
     */
    private void off(){
        In_Use_Price_List = null;
        New_Price_List = null;
        customer.setPumpUnavailable();
        do{
            gasStation.handleMessage();
        } while (!gasStation.checkPower());
        standBy();
    }

    /**
     * standby waits until gasStation sets the price list
     * starts welcome screen
     */
    private void standBy() {
        pumpAssembly.reset();
        Gas_Grade_Selection = GasTypeEnum.NO_SELECTION;
        do {
            gasStation.handleMessage();
            setNew_Price_List(gasStation.getNewPrices());
        } while (New_Price_List == null);
        welcomeScreen();
    }

    /**
     * prompts screen to show welcome screen
     */
    private void welcomeScreen(){
        customer.setWelcome();
        In_Use_Price_List = New_Price_List;

        do{
            paymentControl.handleMessages();
        } while (paymentControl.getVerificationStatus() == CreditCardEnum.NotPresent);
    }


    /**
     * allows us to set the price list at the start of each transaction
     *
     * @param new_Price_List The list of prices from Gas Station GUI
     */
    private void setNew_Price_List(List<Double> new_Price_List) {
        this.New_Price_List = new_Price_List;
    }

    /**
     * This method is used to set the in use price list for a transaction
     */
    private void update_In_Use_Price_List() {
        this.In_Use_Price_List = New_Price_List;
    }

    /**
     * Allows us to set the gas grade selection retrieved from customer
     *
     * @param gas_Grade_Selection the level of gas the user has selected
     */
    private void setGas_Grade_Selection(GasTypeEnum gas_Grade_Selection) {
        this.Gas_Grade_Selection = gas_Grade_Selection;
    }


}
