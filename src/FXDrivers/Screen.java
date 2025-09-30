package FXDrivers;


import IOPort.IOServer;
import UI.ButtonType;
import UI.ScreenUI;
import Util.MarkdownConstants;
import Util.MarkdownLanguage;
import Util.PortAddresses;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * Joel Villarreal
 * The touch screen display object, for simulation purposes
 */
//TODO:
// - Connect to IO Port
public class Screen extends Thread{
    // Constants
    private final int NULL_BTN    =  -1; // signifies no button selected

    // Button Tracking
    private int selectedBtn = NULL_BTN; // the fuel grade selection


    // The gui
    private ScreenUI screenUI = null;

    // The IOServer
    private IOServer ioServer;
    /**
     * Make this Screen
     */
    public Screen() {
    }
    /**
     * Make this Screen, given a screenUI
     */
    public Screen(ScreenUI screenUI) {
        this.screenUI = screenUI;
    }
    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        while (true){
            String message = ioServer.get();
            if(message!=null){
//                System.out.println("message in Screen: " + message);
                // Markdown Language Handling
                MarkdownLanguage.Commands cm = MarkdownLanguage.getCommands(message);
                if (cm == null) {
                    //error
                    System.out.println("MarkdownLanguage.getCommands(m) returned null");
                    errorOccurred();
                } else {
                    // Set the screen
                    setScreen(cm);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public void startServer(){
        ioServer=new IOServer(PortAddresses.SCREEN_PORT);
        this.start();
    }

    /**
     * Set the screen based on commands
     * @param cmds the commands
     */
    public void setScreen(MarkdownLanguage.Commands cmds) {
        if (screenUI == null) {
            System.out.println("ERROR: Screen UI not instantiated");
            errorOccurred();
            return;
        }
        // Clear the screen
        Platform.runLater(() -> {
            screenUI.setBlank();
        });

        // Markdown Language Handling
        MarkdownLanguage.ButtonCommands bCmds = cmds.getBCommands();
        MarkdownLanguage.TextFieldCommands tCmds = cmds.getTCommands();

        ArrayList<MarkdownLanguage.ButtonCommands.Button> btns = bCmds.getButtonCommands();
        ArrayList<MarkdownLanguage.TextFieldCommands.TextField> txts = tCmds.getTextFieldCommands();

        for (MarkdownLanguage.ButtonCommands.Button b: btns) {
            // Create buttons
            createBtn(b);
        }
        for (MarkdownLanguage.TextFieldCommands.TextField t: txts) {
            // Create text boxes
            createTxt(t);
        }
    }

    /**
     * Create a text box on the UI
     * @param t the text field command
     */
    private void createTxt(MarkdownLanguage.TextFieldCommands.TextField t) {
        if (screenUI == null) {
            System.out.println("Error: screen not initialized");
            errorOccurred();
        }
        int size, font, color;
        String text = t.getText();
        int fieldRep = t.getField();
        List<Integer> fieldNums = new ArrayList<>(); // Dynamic sized fieldNumss
        switch (fieldRep){
            // Add singular field number
            case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 -> fieldNums.add(fieldRep);
            // Spanning multiple fields
            case 11 -> { // first row, spans both fields
                fieldNums.add(0);
                fieldNums.add(1);
            }
            case 23 -> { // second row, spans both fields
                fieldNums.add(2);
                fieldNums.add(3);
            }
            case 45 -> { // third row, spans both fields
                fieldNums.add(4);
                fieldNums.add(5);
            }
            case 67 -> { // fourth row, spans both fields
                fieldNums.add(6);
                fieldNums.add(7);
            }
            case 89 -> { // fifth row, spans both fields
                fieldNums.add(8);
                fieldNums.add(9);
            }
            default -> fieldNums.add(0); // default to zero
        }
        MarkdownConstants.Size sz = t.getSize();
        MarkdownConstants.Font fnt = t.getFont();
        MarkdownConstants.BGColor bgColor = t.getBGColor();
        switch (sz) {
            // set size
            case Small -> size = 0;
            case Large -> size = 2;
            default -> size = 1;
        }
        switch (fnt) {
            // set font type
            case Italic -> font = 0;
            case Bold -> font = 2;
            default -> font = 1;
        }
        switch (bgColor) {
            // set background color
            default -> color = 1;   //TODO, multiple background colors
        }
        // Converting List<Integer> to int[] (primitive type int array)
        int[] fieldArray = new int[fieldNums.size()];
        for (int i = 0; i < fieldNums.size(); i++) {
            fieldArray[i] = fieldNums.get(i);
        }
        // Create the label on the screen
        Platform.runLater(() -> {
            screenUI.createLbl(fieldArray, size, font, color, text);
        });

    }

    /**
     * Create a button on the UI
     * @param btn the button to create
     */
    private void createBtn(MarkdownLanguage.ButtonCommands.Button btn) {
        if (screenUI == null) {
            System.out.println("Error: screen not initialized");
            errorOccurred();
        }
        int btnNum = btn.getField();
        boolean isResponsive = btn.getResponsive();
        if (isResponsive) {
            Platform.runLater(() -> {
                // Update the screen with a responsive button
                screenUI.createBtn(btnNum, ButtonType.RESPONSIVE);
            });
        } else {
            Platform.runLater(() -> {
                // Update the screen with a mutually exclusive button
                screenUI.createBtn(btnNum, ButtonType.MUTUALLY_EXCLUSIVE);
            });
        }
    }

    /**
     * Notify communicator of an error
     */
    private void errorOccurred() {
        notifyIOPort(NULL_BTN);
    }

    /**
     * Tell this screen a button was pressed
     * @param btnNumber the button that was pressed
     * @param notifyMain notify main?
     */
    public void notify(int btnNumber, boolean notifyMain) {
        if (btnNumber == NULL_BTN) {
            System.out.println("ERROR: gui related");
            errorOccurred();
        } else {
            if (notifyMain) {
                notifyIOPort(btnNumber);
            } else {
                // store the button press
                selectedBtn = btnNumber;
            }
        }
    }
    /**
     * Notify the Main System of the screen button state, via the Communicator
     * IO Port
     */
    private void notifyIOPort(int pressedBtn) {
        if (pressedBtn == NULL_BTN) {
            System.out.println("ERROR");
            return;
        }
        if(screenUI != null) {
            screenUI.setBlank();
        }
        String btnS = btnString(pressedBtn);
        System.out.println(btnS); //TODO delete this
        ioServer.send(btnS);
    }
    /**
     * for communicating with the Main System
     * @param pressedBtn the responsive button responsible for the notify call
     * @return the screens current state
     */
    private String btnString(int pressedBtn) {
        if (pressedBtn == NULL_BTN) {
            // Should never be called
            System.out.println("ERROR: calling btnString() w/ pressedBtn == NULL_BTN");
            errorOccurred();
            return "";
        } else if (selectedBtn == NULL_BTN) {
            // No button selected, only notify of the responsive button press
            return pressedBtn + "";
        } else {
            /* Return fuel grade selection and responsive button, and reset 
            button selection */
            int val = selectedBtn;
            selectedBtn = NULL_BTN;
            return pressedBtn + ":" + val;
        }
    }


}
