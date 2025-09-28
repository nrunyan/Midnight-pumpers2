package SecondLevel;

import IOPort.IOPort;
import Util.MarkdownConstants;
import Util.MarkdownLanguage;
import Util.PortAddresses;
import Util.ScreenStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The customer API provides the functionality for retrieving information from the screen.
 */
public class Customer extends Thread{ //TODO: this should not be a thread (for testing purposes)
    // int for tracking screen
    private int screenNum = -1;
    // the current gas prices
    private List<Double> inUseGas = null;
    //For splitting input from button presses
    private final String REGEX = ":";

    //The IO Port from Customer to Screen
    private IOPort screenClient;

    /**
     * Creates client IOPort to talk to the Screen. Customer receives button
     * presses from Screen, and sets the screen based on input from Main.
     */
    public Customer(){
        screenClient =new IOPort(PortAddresses.SCREEN_PORT);
        this.start();
    }

    //TODO
    /**
     * Runs this operation.
     * This is completely for testing purposes at this moment
     */
    @Override
    public void run() {
        int indx = 1;
//        setSelectGrade(new ArrayList<Double>(Arrays.asList(2.49, 2.69, 3.01, 3.29, 3.33, 3.50)));
//        setCharging(1, 3.00, 10.01, 30.03);
//        setStartPumping(1, 3.00);
        setFueling(1, 3.00, 10.01, 30.03);
        while(true){
            // Sleeping for test purposes
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            test(indx);
            ScreenStatus screenStatus = getStatus();
            System.out.println("screen Status: " + screenStatus);
//            if (indx > 7) {
//                indx = 1;
//            } else {
//                indx ++;
//            }
        }
    }

    /**
     * A private method to demonstarte screen setting
     * @param screenNum the screen number
     */
    private void test(int screenNum) {
        switch (screenNum) {
            case 1 -> setPumpUnavailable();
            case 2 -> setWelcome();
            case 3 -> setWaitingAuthorization();
            case 4 -> setCardDeclined();
            case 5 -> setSelectGrade(new ArrayList<Double>(Arrays.asList(2.49, 2.69, 3.01, 3.29, 3.33, 3.50)));
            case 6 -> setCharging(1, 3.00, 10.01, 30.03);
            case 7 -> setFueling(1, 3.00, 10.01, 30.03);
            case 8 -> setGoodBye();
        }
    }

    /**
     * Returns where in the transaction the customer is null,cancel,pause,resume,start.
     * @return should be some kind of message, putting object in right now as a placeholder
     *         return null when
     */
    public ScreenStatus getStatus(){
        String btnCode = screenClient.get();
        System.out.println("screenNum: " + screenNum);

        if (btnCode != null) {
            switch (screenNum) {
                case 1 -> {
                    // Pump unavailable screen

                }
                case 2 -> {
                    // Welcome screen

                }
                case 3 -> {
                    // Waiting Authorization screen

                }
                case 4 -> {
                    // Card Declined Screen

                }
                case 5 -> {
                    // Select Grade Screen

                }
                case 6 -> {
                    // Start Pumping Screen (Start, Cancel Transaction)
                    if (btnCode.charAt(0) == '8') {
                        // Start fueling button pressed
                        return ScreenStatus.START;
                    } else if (btnCode.charAt(0) == '9') {
                        // Cancel Transaction
                        return ScreenStatus.CANCEL;
                    }
                }
                case 7 -> {
                    // Charging Screen (Resume or End Transaction)
                    if (btnCode.charAt(0) == '8') {
                        // Resume fueling btn pressed
                        return ScreenStatus.RESUME;
                    } else if (btnCode.charAt(0) == '9') {
                        // End Transaction btn pressed
                        return ScreenStatus.END;
                    }
                }
                case 8 -> {
                    // Fueling Screen (Pause or End Transaction)
                    if (btnCode.charAt(0) == '8') {
                        // Pause fueling btn pressed
                        return ScreenStatus.PAUSE;
                    } else if (btnCode.charAt(0) == '9') {
                        // End Transaction btn pressed
                        return ScreenStatus.END;
                    }
                }
                default -> {
                    // Goodbye Screen
                }
            }
        }
        return ScreenStatus.NO_INPUT;
    }

    /**
     * Returns which gas type the customer selected, null(for no gas selected),low,mid,high.
     * @return the gas choice
     *         returns -2, for no gas selected
     *         returns -1, when the customer cancels
     *         returns 1 through 5 for gas type
     */
    public int getGasChoice(){
        String btnCode = screenClient.get();
//        System.out.println("screenClient.get() = " + btnCode);
        if (btnCode == null) {
            return -2;
        } else{
            String[] btns = btnCode.split(REGEX);
            if (btns.length == 1) {
                // one button was pressed
                if (btns[0].equals("9")) {
                    // Confirm button pressed, alone, reset the screen
                    setSelectGrade(inUseGas);
                } else  {
                    // Cancel button pressed
                    return -1;
                }
            } else if (btns.length == 2){
                // two buttons pressed
                if (btns[0].equals("8")) {
                    // Cancel button pressed
                    return -1;
                } else {
                    // Gas was selected
                    try {
                        // convert to Double
                        int gasSelection = Integer.parseInt(btns[1]);
                        return  gasSelection - 1;
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format: " + e.getMessage());
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Notify customer that the pump is unavailable
     * Via IO Port
     */
    public void setPumpUnavailable() {
        screenClient.send(getPumpUnavailableString());

        // Track screen number
        screenNum = 1;
    }

    /**
     * Welcome the customer (when this pump is idle/awaiting card input)
     * Via IO Port
     */
    public void setWelcome(){
        screenClient.send(getWelcomeString());

        // Track screen number
        screenNum = 2;
    }
    /**
     * Notify customer that their card is waiting authorization from the bank
     * Via IO Port
     */
    public  void setWaitingAuthorization(){
        screenClient.send(getWaitingAuthorizationString());

        // Track screen number
        screenNum = 3;
    }

    /**
     * Notify the customer their card was declined
     * Via IO Port
     */
    public void setCardDeclined(){
        screenClient.send(getCardDeclinedString());

        // Track screen number
        screenNum = 4;
    }

    /**
     * Give the Customer ability to select fuel grade from the in use price list
     * Via IO Port
     * @param inUsePList the in use price list
     */
    public void setSelectGrade(List<Double> inUsePList){
        //Store locally
        inUseGas =inUsePList;

        // Convert List<Double> to double[] using streams
        double[] pListArray = inUsePList.stream().mapToDouble(Double::doubleValue).toArray();

        //Notify through IO Port
        screenClient.send(getGradeSelectionString(pListArray));

        // Track screen number
        screenNum = 5;
    }

    /**
     * Notify the customer that they can start pumping
     * @param gasSelection the gas selected
     * @param selectionPrice the gas selection price
     */
    public void setStartPumping(int gasSelection, double selectionPrice) {
        screenClient.send(getStartPumpingString(selectionPrice));

        // Track screen number
        screenNum = 6;
    }

    /**
     * Notify the customer of the transaction status (giving them the option to
     * resume fueling)
     * Via IO Port
     * @param gasSelection the gas selected by the user
     * @param selectionPrice the price selected by the user
     * @param volumePumped the volume pumped at this moment
     * @param totalCost the amount of income the bank will make at this moment
     */
    public void setCharging(int gasSelection, double selectionPrice, double volumePumped, double totalCost) {
        //TODO: currently ignoring gas selection, does this matter?
        screenClient.send(getChargingString(selectionPrice, volumePumped, totalCost));

        // Track screen number
        screenNum = 7;
    }

    /**
     * Notify the customer of the transaction status (giving them the option to
     * stop actively fueling)
     * Via the IO Port
     * @param gasSelection the gas selected by the user
     * @param selectionPrice the price selected by the user
     * @param volumePumped the volume pumped at this moment
     * @param totalCost the amount of income the bank will make at this moment
     */
    public void setFueling(int gasSelection, double selectionPrice, double volumePumped, double totalCost) {
        //TODO: currently ignoring gas selection, does this matter?
        screenClient.send(getFuelingString(selectionPrice, volumePumped, totalCost));

        // Track screen number
        screenNum = 8;
    }

    /**
     * Tell the Customer goodbye
     * Via the IO POrt
     */
    public void setGoodBye() {
        screenClient.send(getGoodbyeString());

        // Track screen number
        screenNum = 9;
    }

    /**
     * Get the pump unavailable screen (String representation)
     * @return pump unavailable screen (String representation)
     */
    private String getPumpUnavailableString() {
        // Button and text field commands
        MarkdownLanguage.ButtonCommands bc = new MarkdownLanguage.ButtonCommands();
        MarkdownLanguage.TextFieldCommands tfc= new MarkdownLanguage.TextFieldCommands();

        // Pump Unavailable Message
        MarkdownLanguage.TextFieldCommands.TextField tf = new MarkdownLanguage.TextFieldCommands.TextField("PUMP UNAVAILABLE", 23, MarkdownConstants.Size.Large, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);

        // add commands
        tfc.addFieldCommand(tf);

        // return String representation of the commands
        MarkdownLanguage.Commands cmds = new MarkdownLanguage.Commands(bc, tfc);
        return MarkdownLanguage.getMarkdown(cmds);
    }

    /**
     * Get the welcome screen string representation
     * @return welcome screen string representation
     */
    private String getWelcomeString() {
        // Button and text field commands
        MarkdownLanguage.ButtonCommands bc = new MarkdownLanguage.ButtonCommands();
        MarkdownLanguage.TextFieldCommands tfc= new MarkdownLanguage.TextFieldCommands();

        // Pump Unavailable Message
        MarkdownLanguage.TextFieldCommands.TextField welcome = new MarkdownLanguage.TextFieldCommands.TextField("Welcome", 23, MarkdownConstants.Size.Large, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);
        MarkdownLanguage.TextFieldCommands.TextField side_note = new MarkdownLanguage.TextFieldCommands.TextField("Scan Card to Continue...", 9, MarkdownConstants.Size.Small, MarkdownConstants.Font.Normal, MarkdownConstants.BGColor.White);

        //Add commands
        tfc.addFieldCommand(welcome);
        tfc.addFieldCommand(side_note);

        // return String representation of the commands
        MarkdownLanguage.Commands cmds = new MarkdownLanguage.Commands(bc, tfc);
        return MarkdownLanguage.getMarkdown(cmds);
    }

    /**
     * Get the pump unavailable screen String representation
     * @return pump unavailable screen MDL String
     */
    private String getWaitingAuthorizationString() {
        // Button and text field commands
        MarkdownLanguage.ButtonCommands bc = new MarkdownLanguage.ButtonCommands();
        MarkdownLanguage.TextFieldCommands tfc= new MarkdownLanguage.TextFieldCommands();

        // Pump Unavailable Message
        MarkdownLanguage.TextFieldCommands.TextField tf = new MarkdownLanguage.TextFieldCommands.TextField("Awaiting Authorization..", 23, MarkdownConstants.Size.Medium, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);

        // add commands
        tfc.addFieldCommand(tf);

        // return String representation of the commands
        MarkdownLanguage.Commands cmds = new MarkdownLanguage.Commands(bc, tfc);
        return MarkdownLanguage.getMarkdown(cmds);
    }

    /**
     * Get the card declined Screen
     * @return card declined Screen MDL String
     */
    private String getCardDeclinedString() {
        // Button and text field commands
        MarkdownLanguage.ButtonCommands bc = new MarkdownLanguage.ButtonCommands();
        MarkdownLanguage.TextFieldCommands tfc= new MarkdownLanguage.TextFieldCommands();

        // Text Fields
        MarkdownLanguage.TextFieldCommands.TextField cardDeclined = new MarkdownLanguage.TextFieldCommands.TextField("Card Declined", 23, MarkdownConstants.Size.Large, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);
        MarkdownLanguage.TextFieldCommands.TextField sideNote = new MarkdownLanguage.TextFieldCommands.TextField("Scan Card to Continue...", 9, MarkdownConstants.Size.Small, MarkdownConstants.Font.Normal, MarkdownConstants.BGColor.White);

        // Add Commands
        tfc.addFieldCommand(cardDeclined);
        tfc.addFieldCommand(sideNote);

        // return String representation of the commands
        MarkdownLanguage.Commands cmds = new MarkdownLanguage.Commands(bc, tfc);
        return MarkdownLanguage.getMarkdown(cmds);
    }

    /**
     * Get a gas selection string representation of the screen
     * @param prices the list of prices
     * @return String representation of the screen
     */
    private String getGradeSelectionString(double[] prices) {
        // Button and text field commands
        MarkdownLanguage.ButtonCommands bc = new MarkdownLanguage.ButtonCommands();
        MarkdownLanguage.TextFieldCommands tfc= new MarkdownLanguage.TextFieldCommands();

        // Responsive buttons, and their text fields
        MarkdownLanguage.TextFieldCommands.TextField cancel = new MarkdownLanguage.TextFieldCommands.TextField("CANCEL", 8, MarkdownConstants.Size.Medium, MarkdownConstants.Font.Normal, MarkdownConstants.BGColor.White);
        MarkdownLanguage.ButtonCommands.Button cancelBtn = new MarkdownLanguage.ButtonCommands.Button(8, false, true);
        MarkdownLanguage.TextFieldCommands.TextField confirm = new MarkdownLanguage.TextFieldCommands.TextField("Confirm Selection", 9, MarkdownConstants.Size.Medium, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);
        MarkdownLanguage.ButtonCommands.Button confirmBtn = new MarkdownLanguage.ButtonCommands.Button(9, false, true);

        // Prompt the user
        MarkdownLanguage.TextFieldCommands.TextField prompt = new MarkdownLanguage.TextFieldCommands.TextField("Remove Nozzle, Select Fuel Grade", 11, MarkdownConstants.Size.Medium, MarkdownConstants.Font.Normal, MarkdownConstants.BGColor.White);

        //Loop to create the gas price buttons
        for (int i = 0; i < prices.length && i <= 6; i++) { // Cannot have more than six gas choices
            int fieldNum = i + 2; // where the button goes on the screen

            // i'th button
            MarkdownLanguage.ButtonCommands.Button btn = new MarkdownLanguage.ButtonCommands.Button(fieldNum, true, false);

            // i'th price
            MarkdownLanguage.TextFieldCommands.TextField price = new MarkdownLanguage.TextFieldCommands.TextField("$" + prices[i] + " per gallon", fieldNum, MarkdownConstants.Size.Medium, MarkdownConstants.Font.Normal, MarkdownConstants.BGColor.White);

            //Add commands
            tfc.addFieldCommand(price);
            bc.addButtonCommand(btn);
        }

        // Add Commands
        tfc.addFieldCommand(prompt);
        tfc.addFieldCommand(cancel);
        tfc.addFieldCommand(confirm);
        bc.addButtonCommand(cancelBtn);
        bc.addButtonCommand(confirmBtn);

        // return String representation of the commands
        MarkdownLanguage.Commands cmds = new MarkdownLanguage.Commands(bc, tfc);
        return MarkdownLanguage.getMarkdown(cmds);
    }

    /**
     * Get the string representation of the start pumping screen (awaiting
     * customer to start pumping)
     * @param selectP the selected price per gallon
     * @return the string representation of the start pumping screen
     */
    private String getStartPumpingString(double selectP) {
        int vPumped = 0;
        int netCost = 0;
        // Button and text field commands
        MarkdownLanguage.ButtonCommands bc = new MarkdownLanguage.ButtonCommands();
        MarkdownLanguage.TextFieldCommands tfc= new MarkdownLanguage.TextFieldCommands();

        // Resume button, and its text field
        MarkdownLanguage.TextFieldCommands.TextField stopTxt = new MarkdownLanguage.TextFieldCommands.TextField("Start Fueling", 8, MarkdownConstants.Size.Medium, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);
        MarkdownLanguage.ButtonCommands.Button stopBtn = new MarkdownLanguage.ButtonCommands.Button(8, false, true);

        // Cancel Transaction button, and its text field
        MarkdownLanguage.TextFieldCommands.TextField endTxt = new MarkdownLanguage.TextFieldCommands.TextField("Cancel Transaction", 9, MarkdownConstants.Size.Medium, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);
        MarkdownLanguage.ButtonCommands.Button endBtn = new MarkdownLanguage.ButtonCommands.Button(9, false, true);

        // Gas selection price text
        MarkdownLanguage.TextFieldCommands.TextField perG = new MarkdownLanguage.TextFieldCommands.TextField("$" + selectP + " per gallon", 11, MarkdownConstants.Size.Medium, MarkdownConstants.Font.Normal, MarkdownConstants.BGColor.White);

        // Volume Pumped
        MarkdownLanguage.TextFieldCommands.TextField vTxt = new MarkdownLanguage.TextFieldCommands.TextField(vPumped + " gallons", 23, MarkdownConstants.Size.Large, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);

        // Net Cost
        MarkdownLanguage.TextFieldCommands.TextField costTxt = new MarkdownLanguage.TextFieldCommands.TextField("$" + netCost, 45, MarkdownConstants.Size.Large, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);

        // Add Commands
        tfc.addFieldCommand(perG);
        tfc.addFieldCommand(stopTxt);
        tfc.addFieldCommand(vTxt);
        tfc.addFieldCommand(costTxt);
        tfc.addFieldCommand(endTxt);
        bc.addButtonCommand(stopBtn);
        bc.addButtonCommand(endBtn);

        // return String representation of the commands
        MarkdownLanguage.Commands cmds = new MarkdownLanguage.Commands(bc, tfc);
        return MarkdownLanguage.getMarkdown(cmds);
    }

    /**
     * Get the string representation of the charging screen (when not actively
     * pumping), has a
     * @param selectP the selected price per gallon
     * @param vPumped the volume pumped
     * @param netCost the net cost of the transaction
     * @return the string representation of the charging screen
     */
    private String getChargingString(double selectP, double vPumped, double netCost) {
        // Button and text field commands
        MarkdownLanguage.ButtonCommands bc = new MarkdownLanguage.ButtonCommands();
        MarkdownLanguage.TextFieldCommands tfc= new MarkdownLanguage.TextFieldCommands();

        // Resume button, and its text field
        MarkdownLanguage.TextFieldCommands.TextField stopTxt = new MarkdownLanguage.TextFieldCommands.TextField("Resume Fueling", 8, MarkdownConstants.Size.Medium, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);
        MarkdownLanguage.ButtonCommands.Button stopBtn = new MarkdownLanguage.ButtonCommands.Button(8, false, true);

        // End Transaction button, and its text field
        MarkdownLanguage.TextFieldCommands.TextField endTxt = new MarkdownLanguage.TextFieldCommands.TextField("End Transaction", 9, MarkdownConstants.Size.Medium, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);
        MarkdownLanguage.ButtonCommands.Button endBtn = new MarkdownLanguage.ButtonCommands.Button(9, false, true);

        // Gas selection price text
        MarkdownLanguage.TextFieldCommands.TextField perG = new MarkdownLanguage.TextFieldCommands.TextField("$" + selectP + " per gallon", 11, MarkdownConstants.Size.Medium, MarkdownConstants.Font.Normal, MarkdownConstants.BGColor.White);

        // Volume Pumped
        MarkdownLanguage.TextFieldCommands.TextField vTxt = new MarkdownLanguage.TextFieldCommands.TextField(vPumped + " gallons", 23, MarkdownConstants.Size.Large, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);

        // Net Cost
        MarkdownLanguage.TextFieldCommands.TextField costTxt = new MarkdownLanguage.TextFieldCommands.TextField("$" + netCost, 45, MarkdownConstants.Size.Large, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);

        // Add Commands
        tfc.addFieldCommand(perG);
        tfc.addFieldCommand(stopTxt);
        tfc.addFieldCommand(vTxt);
        tfc.addFieldCommand(costTxt);
        tfc.addFieldCommand(endTxt);
        bc.addButtonCommand(stopBtn);
        bc.addButtonCommand(endBtn);

        // return String representation of the commands
        MarkdownLanguage.Commands cmds = new MarkdownLanguage.Commands(bc, tfc);
        return MarkdownLanguage.getMarkdown(cmds);
    }

    /**
     * Get the string representation of the fueling screen (has a stop fueling
     * button)
     * @param selectP the selected price per gallon
     * @param vPumped the volume pumped
     * @param netCost the net cost of the transaction
     * @return the string representation of the fueling screen
     */
    private String getFuelingString(double selectP, double vPumped, double netCost) {
        // Button and text field commands
        MarkdownLanguage.ButtonCommands bc = new MarkdownLanguage.ButtonCommands();
        MarkdownLanguage.TextFieldCommands tfc= new MarkdownLanguage.TextFieldCommands();

        // Stop button, and its text field
        MarkdownLanguage.TextFieldCommands.TextField stopTxt = new MarkdownLanguage.TextFieldCommands.TextField("PAUSE", 8, MarkdownConstants.Size.Large, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);
        MarkdownLanguage.ButtonCommands.Button stopBtn = new MarkdownLanguage.ButtonCommands.Button(8, false, true);

        // End Transaction button, and its text field
        MarkdownLanguage.TextFieldCommands.TextField endTxt = new MarkdownLanguage.TextFieldCommands.TextField("End Transaction", 9, MarkdownConstants.Size.Medium, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);
        MarkdownLanguage.ButtonCommands.Button endBtn = new MarkdownLanguage.ButtonCommands.Button(9, false, true);

        // Gas selection price text
        MarkdownLanguage.TextFieldCommands.TextField perG = new MarkdownLanguage.TextFieldCommands.TextField("$" + selectP + " per gallon", 11, MarkdownConstants.Size.Medium, MarkdownConstants.Font.Normal, MarkdownConstants.BGColor.White);

        // Volume Pumped
        MarkdownLanguage.TextFieldCommands.TextField vTxt = new MarkdownLanguage.TextFieldCommands.TextField(vPumped + " gallons", 23, MarkdownConstants.Size.Large, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);

        // Net Cost
        MarkdownLanguage.TextFieldCommands.TextField costTxt = new MarkdownLanguage.TextFieldCommands.TextField("$" + netCost, 45, MarkdownConstants.Size.Large, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);

        // Add Commands
        tfc.addFieldCommand(perG);
        tfc.addFieldCommand(stopTxt);
        tfc.addFieldCommand(vTxt);
        tfc.addFieldCommand(costTxt);
        tfc.addFieldCommand(endTxt);
        bc.addButtonCommand(stopBtn);
        bc.addButtonCommand(endBtn);

        // return String representation of the commands
        MarkdownLanguage.Commands cmds = new MarkdownLanguage.Commands(bc, tfc);
        return MarkdownLanguage.getMarkdown(cmds);
    }

    /**
     * Get the string representation of the goodbye string
     * @return the goodbye string screen representation
     */
    private String getGoodbyeString() {
        // Button and text field commands
        MarkdownLanguage.ButtonCommands bc = new MarkdownLanguage.ButtonCommands();
        MarkdownLanguage.TextFieldCommands tfc= new MarkdownLanguage.TextFieldCommands();

        //"thank you" text field
        MarkdownLanguage.TextFieldCommands.TextField thankTxt = new MarkdownLanguage.TextFieldCommands.TextField("Thank you for your purchase,", 23, MarkdownConstants.Size.Medium, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);

        //goodbye text field
        MarkdownLanguage.TextFieldCommands.TextField perG = new MarkdownLanguage.TextFieldCommands.TextField("Goodbye!", 45, MarkdownConstants.Size.Large, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);

        // Add Commands
        tfc.addFieldCommand(thankTxt);
        tfc.addFieldCommand(perG);

        // return String representation of the commands
        MarkdownLanguage.Commands cmds = new MarkdownLanguage.Commands(bc, tfc);
        return MarkdownLanguage.getMarkdown(cmds);
    }
}
