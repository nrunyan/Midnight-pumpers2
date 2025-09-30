package Main;

import SecondLevel.Customer;
import SecondLevel.GasStation;
import SecondLevel.PaymentControl;
import SecondLevel.PumpAssembly;
import Util.CreditCardEnum;
import Util.GasTypeEnum;
import Util.ScreenStatus;
import Util.Timer;

import java.sql.SQLOutput;
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
    private CreditCardEnum Credit_Card_Status;
    private double unitPrice;
    private double gasVolume;
    private double dollarAmount;
    private int gasIndex;
    private double totalPrice;

    public PumpManager(PaymentControl paymentControl, Customer customer, GasStation gasStation, PumpAssembly pumpAssembly) {
        this.paymentControl = paymentControl;
        this.customer = customer;
        this.gasStation = gasStation;
        this.pumpAssembly = pumpAssembly;
        ON = true;
        handleSystem();
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
    private void off() {
        In_Use_Price_List = null;
        New_Price_List = null;
        customer.setPumpUnavailable();
        do {
            gasStation.handleMessage();
            System.out.println("Taking out this print statement seems to make it stall");
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
            if (checkOff()) {
                return;
            }
            gasStation.handleMessage();
            setNew_Price_List(gasStation.getNewPrices());
        } while (New_Price_List == null);
        welcomeScreen();
    }

    /**
     * prompts screen to show welcome screen
     */
    private void welcomeScreen() {
        customer.setWelcome();
        In_Use_Price_List = New_Price_List;
        do {
            if (checkOff()) {
                return;
            }
            paymentControl.handleMessages();
        } while ((Credit_Card_Status = paymentControl.getVerificationStatus()) == CreditCardEnum.NotPresent);

        if (Credit_Card_Status == CreditCardEnum.WaitingOnBonk) {
            pendingAuthorization();
        }
        if (Credit_Card_Status == CreditCardEnum.Accepted) {
            selectFuel();

        } else {
            cardDeclined();
        }
    }

    /**
     * only called if waiting on the bank for authorization
     */
    private void pendingAuthorization() {
        customer.setWaitingAuthorization();
        do {
            if (checkOff()) {
                return;
            }
            paymentControl.handleMessages();
        } while ((Credit_Card_Status = paymentControl.getVerificationStatus()) == CreditCardEnum.WaitingOnBonk);

    }

    /**
     * displays card declined
     * resets system to welcome screen
     */
    private void cardDeclined() {
        customer.setCardDeclined();
        Timer timer = new Timer(5);
        while (!timer.timeout()) {
            if (checkOff()) {
                return;
            }
        }
        standBy();
    }

    /**
     * Prompts the user to select a fuel grade
     */
    private void selectFuel() {
        customer.setSelectGrade(In_Use_Price_List);
        Timer timer = new Timer(120);
        do {
            // Gas_Grade_Selection=GasTypeEnum.GAS_TYPE_1; WE NEED A RECHECK HERE

            if (timer.timeout()) {
                standBy();
                return;
            }
            if (checkOff()) {
                return;
            }
        } while (Gas_Grade_Selection == GasTypeEnum.NO_SELECTION);
        idle();
    }


    /**
     * This is the idle stage, sets start pumping times out for two min
     */
    private void idle() {
        System.out.println("here sir");
        seti();
        ScreenStatus screenStatus;
        customer.setStartPumping(In_Use_Price_List.get(gasIndex));
        Timer timer = new Timer(120);
        do {
            if (timer.timeout()) {
                standBy();
                return;
            }
            if (checkOff()) {
                return;
            }
            screenStatus = customer.getStatus();
            if (screenStatus == ScreenStatus.CANCEL) {
                standBy();
                return;
            }
        } while (!(screenStatus == ScreenStatus.START && pumpAssembly.getHoseConnected()));
        fueling();
    }

    /**
     *
     */
    private void fueling() {
        pumpAssembly.pumpOn(Gas_Grade_Selection);
        ScreenStatus status;
        boolean pause;
        boolean stop;
        boolean tankfull;
        do {
            if (checkOff()) {
                return;
            }
            setUVD();
            customer.setFueling(unitPrice, gasVolume, totalPrice);
            status = customer.getStatus();
            pause = pauseCondtion(status);
            stop = stopCondition(status);
            tankfull = pumpAssembly.isTankFull();
        } while (!(pause || stop || tankfull));
        if (pause) {
            pause();
        } else {
            goodBye();
        }

    }

    /**
     * active if pause button pressed
     * updates paused screen
     */
    private void pause() {
        pumpAssembly.pumpOff();
        Timer timer = new Timer(120);
        setUVD();
        customer.setCharging(unitPrice, gasVolume, totalPrice);
        ScreenStatus screenStatus = ScreenStatus.NO_INPUT;
        do {
            if (timer.timeout() || screenStatus == ScreenStatus.END) {
                goodBye();
                return;
            }
            if (checkOff()) {return;}
            screenStatus = customer.getStatus();
        } while (!(pumpAssembly.getHoseConnected() && (screenStatus == ScreenStatus.RESUME)));
        if (screenStatus == ScreenStatus.RESUME) {
            fueling();
        }
    }

    /**
     * goodbye displays a goodbye message, sends transaction info to the bank and gas station
     */
    private void goodBye() {
        customer.setGoodBye();
        Timer timer = new Timer(10);
        setUVD();
        paymentControl.sendTransactionInfo(totalPrice);
        gasStation.sendTransactionInfo(totalPrice, gasVolume, Gas_Grade_Selection);
        while(timer.timeout()) {
            standBy();
        }
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

    /**
     * Helper for idle
     *
     * @return an index for the In_Use_Price_List
     */
    private void seti() {
        switch (Gas_Grade_Selection) {
            case GasTypeEnum.GAS_TYPE_1 -> gasIndex = 0;
            case GasTypeEnum.GAS_TYPE_2 -> gasIndex = 1;
            case GasTypeEnum.GAS_TYPE_3 -> gasIndex = 2;
            case GasTypeEnum.GAS_TYPE_4 -> gasIndex = 3;
            case GasTypeEnum.GAS_TYPE_5 -> gasIndex = 4;
            default -> {
                System.out.println("invalid Gas_Grade_Selection");
                gasIndex = 5;
            }
        }
    }

    /**
     * Sets the unit price
     */
    private void setUVD(){
        seti();
        unitPrice = In_Use_Price_List.get(gasIndex);
        gasVolume = pumpAssembly.getGasVolume();
        totalPrice = calculateTotalPrice();

    }


    /**
     * @param status customer status
     * @return true if customer pauses or removes the nozzle
     */
    private boolean pauseCondtion(ScreenStatus status) {
        return (status == ScreenStatus.PAUSE || !pumpAssembly.getHoseConnected());
    }

    /**
     * @param status customer status
     * @return true if customer ends or the tank is full
     */
    private boolean stopCondition(ScreenStatus status) {
        return status == ScreenStatus.END || pumpAssembly.isTankFull();
        //tank full
    }

    /**
     * /**
     * checks if the gas station is off
     * calls off to reset the pump manager
     *
     * @return true if off
     */
    private boolean checkOff() {
        gasStation.handleMessage();
        if (!gasStation.checkPower()) {
            off();
            return true;
        }
        return false;
    }

    /**
     * @return
     */
    private double calculateTotalPrice() {
        return unitPrice * gasVolume;
    }

}