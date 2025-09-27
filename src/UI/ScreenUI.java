package UI;


import FXDrivers.Screen;
import Util.MarkdownConstants;
import Util.MarkdownLanguage;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;



/**
 * Joel Villarreal
 * The Screen GUI
 */
public class ScreenUI extends Application {
    // Constants:
    private final double HORZ     =   15; // the horizontal gap of the grid
    private final double VERT     =   2; // the vertical gap of the grid
    private final double DISP_W   = 500; // the GUI's initial width
    private final double DISP_H   = 350; // the GUI's initial height
    private final int  NUM_COLS   =   4; // number of columns on the grid
    private final int  NUM_ROWS   =   5; // number of columns on the grid
    private final int  BTN_FACTOR =   8; /* buttons take up an eighth of the
                                             horizontal space */
    // Fonts
    private final Font SMALL_FNT = new Font(18);
    private final Font MED_FNT   = new Font(20);
    private final Font LRG_FNT   = new Font(32);

    // The Screen Object that this GUI is displaying
    private Screen screen;

    // The Toggle Group
    private final ToggleGroup TOGGLE_GROUP; // For mutually exclusive buttons

    // The grid pane
    private final GridPane GRID_PANE;




    /**
     * launches JavaFX application
     * @param args No expected arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * The screen constructor
     */
    public ScreenUI() {
        GRID_PANE = new GridPane(HORZ, VERT);
        this.screen = new Screen();
        TOGGLE_GROUP = new ToggleGroup();
        resetGrid();
    }

    private void clearGP() {
        GRID_PANE.getChildren().clear(); // clear the grid pane
    }
    /**
     * Set up the grid
     */
    private void resetGrid() {
        clearGP();

        setGridConstraints();
        setUpToggleGroup();

        setBlank();
    }
    /**
     * Make the display blank
     */
    public void setBlank() {
        clearGP();
        GRID_PANE.getStyleClass().add("black_background");
    }

    /**
     * Establishes multi-select behavior
     */
    private void setUpToggleGroup() {
        TOGGLE_GROUP.selectedToggleProperty().addListener(
                (observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                // Set selected button color
                ((ToggleButton) newToggle).getStyleClass().setAll("selected_btn");
            }
            if (oldToggle != null) {
                // Set unselected button color
                ((ToggleButton) oldToggle).getStyleClass().setAll("unselected_btn");
            }
        });
    }

    /**
     * Make Display more Readable, by setting column and row constraints to
     * adjust with the screen size
     */
    private void setGridConstraints() {
        /* the constraints */
        ColumnConstraints col;
        RowConstraints    row;

        /* the percent of the screen a button, text box, and row take up */
        double btnPercent, txtPercent, rowPercent;

        rowPercent = 100.0 / NUM_ROWS;
        btnPercent = 100.0 / BTN_FACTOR;
        txtPercent = (100.0 - btnPercent) / (NUM_COLS - 2);

        for (int i = 0; i < NUM_COLS; i++) {
            /* For each column, set the column constraints to a percent of the
               screen width */
            col = new ColumnConstraints();
            GRID_PANE.getColumnConstraints().add(col);
            if(i != 0 && i != (NUM_COLS - 1)) {
                // Text columns have less width
                col.setPercentWidth(txtPercent);
            } else {
                // Button columns have more width
                col.setPercentWidth(btnPercent);
            }
        }

        for (int i = 0; i < NUM_ROWS; i++) {
            /* For each row, set the row constraints to a percent of the screen
               height*/
            row = new RowConstraints();
            GRID_PANE.getRowConstraints().add(row);
            row.setPercentHeight(rowPercent);
        }

    }

    /**
     * Add this label to the grid
     * @param row the row of the grid to add the label
     * @param col the column of the grid to add the label
     * @param span the number of columns this textbox spans
     * @return the created label
     */
    private Label createLbl(int row, int col, int span) {
        Label lbl = new Label("lorem ipsum");
        HBox hBox = new HBox(0, lbl);

        //Some stuff for prettiness
        lbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(lbl, Priority.ALWAYS);
        hBox.setAlignment(Pos.CENTER);

        GRID_PANE.add(hBox, col, row);
        GridPane.setColumnSpan(hBox, span);
        return lbl;
    }

    /**
     * Create the label at these text field positions associated with the grid
     * @param txtFieldNums the text field positions
     * @return the label created at that position
     */
    private Label createLbl(int[] txtFieldNums) {
        Label lbl = new Label();
        int rowI = txtFieldNums[0] / 2;

        switch (txtFieldNums.length) {
            case 1 -> {
                // text only spans one field
                if (txtFieldNums[0] % 2 == 0) {
                    // Even numbers are on the left of the display
                    lbl = createLbl(rowI, 1, 1);

                } else {
                    // Odd numbers on the right of the display
                    lbl = createLbl(rowI, 2, 1);
                    GridPane.setHalignment(lbl, HPos.RIGHT); // Align label to the right
                }
            }
            case 2 -> {
                // text spans two fields
                if ((txtFieldNums[0] + 1) != txtFieldNums[1]) {
                    // Miss-input, throw error
                    System.out.println("ERROR: text must span two adjacent fields");
                    sendErrorMsg();
                } else {
                    // text spans two fields
                    lbl = createLbl(rowI, 1, 2);
                }
            }
            default -> {
                // ERROR, unexpected number of field numbers
                System.out.println("ERROR: unexpected number of field numbers");
                sendErrorMsg();
            }

        }

        return lbl;
    }

    /**
     Create a label with all of this dynamics
     * @param txtFields the field(s) this text spans
     * @param fontSize font size
     * @param fontType font type
     * @param backColor background color
     * @param str the string of text to display
     */
    public void createLbl(int[] txtFields, int fontSize, int fontType,
                            int backColor, String str) {
        //TODO make this an object rather than a bunch of primitive types
        Label lbl = createLbl(txtFields);
        lbl.setText(str);
        lbl.getStyleClass().add("label");
        switch (fontSize) {
            case 0 ->
                // Small
                    lbl.setFont(SMALL_FNT);
            case 2 ->
                // Large
                    lbl.setFont(LRG_FNT);
            default ->
                // Medium
                    lbl.setFont(MED_FNT);
        }
        switch (fontType) {
            case 0 ->
                // Italic
                    lbl.getStyleClass().add("italic");
            case 2 ->
                // Bold
                    lbl.getStyleClass().add("bold");
            default -> {
            }
            // Regular font
        }
        switch (backColor) {
            //TODO other background colors
            case 0:
                // Dark Purple
                lbl.getStyleClass().add("black_background");
            case 2:
                // Dark Blue
                lbl.getStyleClass().add("black_background");
            default:
                // Black
                lbl.getStyleClass().add("black_background");
        }
    }

    /**
     Create a button at this row and this column in the display
     * @param row, the row to create a button at
     * @param col, the column to create a button at
     * @return that button
     */
    private ToggleButton createBtn(int row, int col) {
        ToggleButton btn = new ToggleButton();
        HBox hBox = new HBox(0, btn);

        //Resizability
        btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(btn, Priority.ALWAYS);
        hBox.setAlignment(Pos.CENTER);

        //Coloring
        btn.getStyleClass().add("unselected_btn");

        GRID_PANE.add(hBox, col, row);
        return btn;
    }

    /**
     * Create a button at this location in display
     * @param btnNum the number that signifies this button
     * @return the created button
     */
    private ToggleButton createBtn(int btnNum) {
        ToggleButton btn;
        int rowI = btnNum / 2;

        if(btnNum % 2 == 0) {
            // Even numbers are on the left of the display
            btn = createBtn(rowI, 0);

        } else {
            // Odd numbers on the right of the display
            btn = createBtn(rowI, 3);
        }
        return btn;
    }
    /**
     * Create a button at this number, of this type
     * @param btnNumber the button location id
     * @param btnType the type of this button
     */
    public void createBtn(int btnNumber, ButtonType btnType) {
        ToggleButton btn = createBtn(btnNumber);
        if (btnType == ButtonType.RESPONSIVE) {
            // Responsive Button
            // style
            btn.getStyleClass().add("responsive_btn");
            btn.setOnMouseClicked(event -> {
                // Notify the Communicator
                notifyScreen(btnNumber, true);
            });
        } else {
            // Multi-Select Button, add it to the toggle group
            btn.setToggleGroup(TOGGLE_GROUP);

            btn.setOnMouseClicked(event -> {
                // Track the number associated with this button
                notifyScreen(btnNumber, false);
            });
        }
    }

    /**
     Notify the screen that a button was pressed
     * @param btnNumber the button that was pressed
     * @param exclusiveSelect was the button an exclusive select button?
     */
    private void notifyScreen(int btnNumber, boolean exclusiveSelect) {
        screen.notify(btnNumber, exclusiveSelect);
    }

    /**
     * Notify Main System that an error happened
     */
    private void sendErrorMsg() {
        notifyScreen(-1, false);
    }



    /**
     * Get the scene of this ScreenUI object
     * @return the grid pane (for display purposes)
     */
    private Parent getScene() {
        return GRID_PANE;
    }


    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but
     *                     they will not be primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Screen scr = new Screen();
        screen=new Screen(this);
        screen.startServer();

        primaryStage.setTitle("Touch Screen Display");
        primaryStage.setScene(new Scene(this.getScene(), DISP_W, DISP_H));
        primaryStage.getScene().getStylesheets().add(
                getClass().getResource("style.css").toExternalForm()
        );
        primaryStage.show();
    }
}

