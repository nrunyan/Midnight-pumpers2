package SecondLevel;

import IOPort.IOPort;
import Util.MarkdownConstants;
import Util.MarkdownLanguage;
import Util.PortAddresses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The customer API provides the functionality for retrieving information from the screen.
 */
public class Customer { //TODO: this should not be a thread (for testing purposes)
    // split messages by the ':' character
    private final String REGEX = ":";

    //The IO Port from Customer to Screen
    private IOPort screenClient;

    /**
     * Creates client IOPort to talk to the Screen. Customer receives button
     * presses from Screen, and sets the screen based on input from Main.
     */
    public Customer(){
        screenClient =new IOPort(PortAddresses.SCREEN_PORT);
//        this.start();
    }

    //TODO
    /**
     * Runs this operation.
     * This is completely for testing purposes at this moment
     */
//    @Override
//    public void run() {
//        int indx = 1;
//        setSelectGrade(new ArrayList<Double>(Arrays.asList(2.49, 2.69, 3.01, 3.29, 3.33, 3.50)));
////        setCharging(1, 3.00, 10.01, 30.03);
////        setFueling(1, 3.00, 10.01, 30.03);
//        while(true){
//            // Sleeping for test purposes
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            int gasC = getGasChoice();
//        }
//    }

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
     * Returns where in the transaction the customer is null,cancel,pause,cancel,resume,start.
     * @return should be some kind of message, putting object in right now as a placeholder
     */
    public Object getStatus(){
        return null;
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
        System.out.println("screenClient.get() = " + btnCode);
        if (btnCode == null) {
            return -2;
        } else{
            String[] btns = btnCode.split(REGEX);
//            if (btns.length)
            //TODO: confirm selection, without gas grade, reset gas screen
            //TODO: return gas selection via button codes
        }
        return 0;
    }

    /**
     * Notify customer that the pump is unavailable
     * Via IO Port
     */
    public void setPumpUnavailable() {
        screenClient.send(getPumpUnavailableString());
    }

    /**
     * Welcome the customer (when this pump is idle/awaiting card input)
     * Via IO Port
     */
    public void setWelcome(){
        screenClient.send(getWelcomeString());
    }
    /**
     * Notify customer that their card is waiting authorization from the bank
     * Via IO Port
     */
    public  void setWaitingAuthorization(){
        screenClient.send(getWaitingAuthorizationString());
    }

    /**
     * Notify the customer their card was declined
     * Via IO Port
     */
    public void setCardDeclined(){
        screenClient.send(getCardDeclinedString());
    }

    /**
     * Give the Customer ability to select fuel grade from the in use price list
     * Via IO Port
     * @param inUsePList the in use price list
     */
    public void setSelectGrade(List<Double> inUsePList){
        // Convert List<Double> to double[] using streams
        double[] pListArray = inUsePList.stream().mapToDouble(Double::doubleValue).toArray();

        //Notify through IO Port
        screenClient.send(getGradeSelectionString(pListArray));
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
    }

    /**
     * Tell the Customer goodbye
     * Via the IO POrt
     */
    public void setGoodBye() {
        screenClient.send(getGoodbyeString());
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
        MarkdownLanguage.TextFieldCommands.TextField stopTxt = new MarkdownLanguage.TextFieldCommands.TextField("STOP", 8, MarkdownConstants.Size.Large, MarkdownConstants.Font.Bold, MarkdownConstants.BGColor.White);
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
