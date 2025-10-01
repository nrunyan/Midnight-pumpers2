package UI;

import FXDrivers.CCReader;
import Util.CommunicationString;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * This is the GUI for the credit card reader, so how it works is basically
 * you can move the credit card around and then (at current moment) whenever you
 * put it down it sends the info to the bank, based on the banks responce it
 * loads a different colored rectangle over the screen
 */
public class CCReaderGUI extends Application {
    private double mouseOffsetX;
    private double mouseOffsetY;
    private CCReader ccReader = new CCReader();
    private Pane root = new Pane();
    private final double width = 600;
    private final double height = 400;

    /**
     * basic gui for ccreader, move card arround and when you put it
     * down it will send ifo to bank
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set.
     *              Applications may create other stages, if needed, but they will not be
     *              primary stages.
     */
    @Override
    public void start(Stage stage) {
        getStage().show();


    }

    public Stage getStage() {
        Stage stage = new Stage();
        root.setPrefSize(width, height);
        Rectangle noImNotExplaingThis = new Rectangle(width, height);
        noImNotExplaingThis.setFill(Color.TRANSPARENT);

        Image creditCard = new Image(getClass().getResource("/CC.png").toExternalForm());
        Image creditCardMachine = new Image(getClass().getResource("/CCReader.png").toExternalForm());
        ImageView creditCardView = new ImageView(creditCard);
        ImageView creditcardMachineView = new ImageView(creditCardMachine);
        creditcardMachineView.setFitWidth(600);
        creditCardView.setFitWidth(200);
        creditCardView.setPreserveRatio(true);
        creditcardMachineView.setPreserveRatio(true);
        creditCardView.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            mouseOffsetX = e.getSceneX() - creditCardView.getX();
            mouseOffsetY = e.getSceneY() - creditCardView.getY();
            creditCardView.setCursor(javafx.scene.Cursor.MOVE); // WHAT SO YOURE TELLING ME
        });

        creditCardView.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            double newX = e.getSceneX() - mouseOffsetX;
            double newY = e.getSceneY() - mouseOffsetY;
            creditCardView.setX(newX);
            creditCardView.setY(newY);
            creditCardView.setCursor(Cursor.OPEN_HAND);

        });
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                String msg = ccReader.getBankRespnse();
                if (msg != null) {
                    noImNotExplaingThis.setFill(handleResponce(msg));
                }


            }
        };
        animationTimer.start();

        //Todo: something is weird with sending multiple
        creditCardView.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {

            System.out.println("sending cc info");
            double x = e.getSceneX();
            double y = e.getSceneY();
            if ((x > width / 2 - 40) && (x < width / 2 + 40) && (y > height / 2 - 50) && (y < height / 2 + 50)) {
                ccReader.sendCCInfo();
            }
            creditCardView.setCursor(Cursor.CLOSED_HAND);

        });


        root.getChildren().addAll(creditcardMachineView, noImNotExplaingThis, creditCardView);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Credit card reader");
        return stage;
    }

    /**
     * Guys guys please here me out, returning a color really is the best way
     * of handling this guys please you don't understand, guys please <- no one read this but me and you -Youssef
     *
     * @param bankResponce approved or denied based on bank
     */
    private Color handleResponce(String bankResponce) {
        //ahah if null--then nothing ooooorr wait, ahah
        Color color;
        if (bankResponce == null) {
            //maybe wait guys i dont know
            color = new Color(0, 0, 0, 0);
        } else if (bankResponce.equals(CommunicationString.APPROVED)) {
            color = new Color(0, 1, 0, .5);
        } else {
            color = new Color(1, 0, 0, .5);
        }
        return color;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
