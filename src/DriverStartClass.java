import FXDrivers.GasStationServer;
import UI.*;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;

import static javafx.application.Application.launch;

public class DriverStartClass extends Application  {

    public static void main(String[] args) {
        launch(args);
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
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Starts all the GUIS
        new PumpAndFlowGUI().getStage().show();
        new GasStationGui().getStage().show();
        new CCReaderGUI().getStage().show();
        new ScreenUI().getStage().show();
        new BankGUI().getStage().show();


    }
}
