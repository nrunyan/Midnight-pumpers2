package UI;

import FXDrivers.Bonk;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Visualization of the Bank
 */
public class BankGUI extends Application {
    // Some constants
    int DISP_W = 250;
    int DISP_H = 200;

    // Background Image
    private Image image = new Image(getClass().getResource(
            "/pumpStation.png").toExternalForm());
    private ImageView stationPic = new ImageView(image);

    // The anchor pane
    private final AnchorPane anchorPane = new AnchorPane();

    public BankGUI() {
        setBackground();
    }

    /**
     * Update displayed revenue
     * @param moneyMade
     */
    public void updateRevenue(double moneyMade) {
    }

    /**
     * Display the latest card, and that it was approved
     * @param cardNum the card number
     */
    public void displayApproved(String cardNum) {
    }

    /**
     * Display the latest card, and that it was declined
     * @param cardNum the card number
     */
    public void displayDenied(String cardNum) {
    }

    /**
     * Set the background to the image
     */
    private void setBackground() {
        anchorPane.getChildren().add(stationPic);
    }
    /**
     * get the scene
     * @return this scene
     */
    private Parent getScene() {
        return anchorPane;
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
        Bonk bank = new Bonk();
        bank.setBankGUI(this);

        //JavaFx
        primaryStage.setTitle("Touch Screen Display");
        primaryStage.setScene(new Scene(this.getScene(), DISP_W, DISP_H));
        primaryStage.getScene().getStylesheets().add(
                getClass().getResource("style.css").toExternalForm()
        );
        primaryStage.show();
    }
}
