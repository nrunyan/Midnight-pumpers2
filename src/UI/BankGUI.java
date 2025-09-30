package UI;

import FXDrivers.Bonk;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Visualization of the Bank
 */
public class BankGUI extends Application {
    // Some constants
    int DISP_W = 250;
    int DISP_H = 300;
    int TEXT_X = 20;
    int TEXT_Y = 250;

    // Background Image
    private Image image = new Image(getClass().getResource(
            "/bank.png").toExternalForm());
    private ImageView bankPic = new ImageView(image);

    // The anchor pane
    private final AnchorPane anchorPane = new AnchorPane();

    /**
     * Create this Bank GUI
     */
    public BankGUI() {
        resetBackground();
    }

    /**
     * Indicate money transfers
     * @param moneyTransferred the money being transferred
     */
    public void moneyTransfer(String moneyTransferred) {
        resetBackground();

        // Display the money being transferred
        Text accepted = new Text(TEXT_X,TEXT_Y,moneyTransferred  + " Transferred");
        accepted.getStyleClass().add("bank-text"); //using css file
        accepted.setFill(Color.WHITE);
        anchorPane.getChildren().add(accepted);
    }

    /**
     * Display the latest card, and that it was approved
     * @param cardNum the card number
     */
    public void displayApproved(String cardNum) {
        // reset the background
        resetBackground();

        // Display card accepted
        Text accepted = new Text(TEXT_X,TEXT_Y,cardNum  + ", ACCEPTED");
        accepted.getStyleClass().add("bank-text"); //using css file
        accepted.setFill(Color.LIGHTGREEN);
        anchorPane.getChildren().add(accepted);
    }

    /**
     * Display the latest card, and that it was declined
     * @param cardNum the card number
     */
    public void displayDenied(String cardNum) {
        // reset the background
        resetBackground();

        // Display card denied
        Text accepted = new Text(TEXT_X,TEXT_Y,cardNum  + ", DECLINED");
        accepted.getStyleClass().add("bank-text"); //using css file
        accepted.setFill(Color.PINK);
        anchorPane.getChildren().add(accepted);
    }

    /**
     * Set the background to the image
     */
    private void resetBackground() {
        // Clear the anchor pane
        anchorPane.getChildren().clear();

        // Setting width and height
        bankPic.setFitWidth(DISP_W);
        bankPic.setFitHeight(DISP_H);


        // Add to anchor pane
        anchorPane.getChildren().add(bankPic);
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
        getStage().show();
    }
    public Stage getStage(){
        Stage primaryStage=new Stage();
        Bonk bank = new Bonk();
        bank.setBankGUI(this);

//        bank.ApproveOrDeny("123443");
//        bank.ApproveOrDeny("24342");
//        bank.ApproveOrDeny("23412");
//        bank.ApproveOrDeny("13424");
//        bank.ApproveOrDeny("4123153");
//        bank.ApproveOrDeny("$233");
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                bank.ApproveOrDeny();

            }
        };
        animationTimer.start();

        //JavaFx
        primaryStage.setTitle("Touch Screen Display");
        primaryStage.setScene(new Scene(this.getScene(), DISP_W, DISP_H));
        primaryStage.getScene().getStylesheets().add(
                getClass().getResource("style.css").toExternalForm()
        );
        return primaryStage;
    }
}
