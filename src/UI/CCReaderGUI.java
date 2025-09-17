package UI;
import FXDrivers.CCReader;
import Util.CommunicationString;
import javafx.application.Application;
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
    private CCReader ccReader=new CCReader();
    private Pane root = new Pane();
    private final double width=600;
    private final double height=400;

    @Override
    public void start(Stage stage) {

        root.setPrefSize(width, height);
        Rectangle noImNotExplaingThis=new Rectangle(width,height);
        noImNotExplaingThis.setFill(Color.TRANSPARENT);

        Image creditCard=new Image(getClass().getResource("/CC.png").toExternalForm());
        Image creditCardMachine=new Image(getClass().getResource("/CCReader.png").toExternalForm());
        ImageView creditCardView =new ImageView(creditCard);
        ImageView creditcardMachineView=new ImageView(creditCardMachine);
        creditcardMachineView.setFitWidth(600);
        creditCardView.setFitWidth(200);
        creditCardView.setPreserveRatio(true);
        creditcardMachineView.setPreserveRatio(true);
        creditCardView.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            mouseOffsetX = e.getSceneX() - creditCardView.getX();
            mouseOffsetY = e.getSceneY() - creditCardView.getY();
           // creditCardView.setCursor(javafx.scene.Cursor.MOVE); // WHAT SO YOURE TELLING ME
        });

        creditCardView.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            double newX = e.getSceneX() - mouseOffsetX;
            double newY = e.getSceneY() - mouseOffsetY;
            creditCardView.setX(newX);
            creditCardView.setY(newY);

        });

        creditCardView.addEventHandler(MouseEvent.MOUSE_RELEASED,e->{
            //so this is when they put it down, so its when we check if it's in frame
            //but i didn't feel like doing that so i didn't
            //ccReader.sendCCInfo();
            //then maybe wait
            noImNotExplaingThis.setFill(handleResponce(CommunicationString.APPROVED));


        });


        root.getChildren().addAll(creditcardMachineView,noImNotExplaingThis,creditCardView);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Credit card reader");
        stage.show();
    }

    /**
     * Guys guys please here me out, returning a color really is the best way
     * of handling this guys please you don't understand, guys please
     * @param bankResponce
     */
    private Color handleResponce(String bankResponce){
        //ahah if null--then nothing ooooorr wait, ahah
        Color color;
        if(bankResponce==null){
            //maybe wait guys i dont know
            color=new Color(0,0,0,0);
        } else if (bankResponce.equals(CommunicationString.APPROVED)) {
            color=new Color(0,1,0,.5);
        }else{
            color=new Color(1,0,0,.5);
        }


        return color;

    }

    public static void main(String[] args) {
        launch(args);
    }
}
