package UI;

import FXDrivers.Flowmeter;
import FXDrivers.Nozzle;
import Util.CommunicationString;
import Util.GasConstants;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.List;

/**
 * This is the GUI for the hose
 */

public class HoseConGUI extends Application {
    private Nozzle nozzle;  //How it sends the messages
    private int currentIndex;
    private boolean flip=false;
    private boolean connected=false;
    private int getTo=4;
    private int step=1;
    private Flowmeter flowmeter;
    public HoseConGUI(Flowmeter flowmeter){
        nozzle=new Nozzle();
        this.flowmeter=flowmeter;
    }

    public Stage getStage(){
        Stage stage=new Stage();
        System.out.println("Getting to start");
        Rectangle hose = new Rectangle(550,500,50,50);
        hose.setFill(Color.PURPLE);
        hose.setOnMouseClicked(event -> flipConnection());
        StackPane stackPane=new StackPane(hose);

        List<Image> images = new ArrayList<>();
        images.add(new Image(getClass().getResource("/hosecon1.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/hosecon2.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/hosecon3.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/hosecon4.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/hosecon5.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/hosecon6.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/hosecon7.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/hosecon8.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/hosecon9.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/hosecon10.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/hosecon11.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/hosecon12.png").toExternalForm()));

        ImageView imageView = new ImageView(images.get(currentIndex));
        imageView.setFitWidth(400);
        //imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        AnimationTimer animationTimer =new AnimationTimer() {

            @Override
            public void handle(long now) {
                if(flowmeter.getGasFlow()> GasConstants.MAX_FLOW){
                    nozzle.sendTankFull();
                    System.out.println("flowmeter amount "+ flowmeter.getGasFlow());
                    System.out.println("SENDING TANK FULL MESSAGE");
                }

                if(flip){
                    if(now%1000==0){
                        if(currentIndex==getTo){
                            flip=false;
                        }else{
                            currentIndex = (currentIndex + step);
                            imageView.setImage(images.get(currentIndex));
                        }
                    }
                }

            }
        };
        animationTimer.start();


        Rectangle rectButton = new Rectangle(700, 1000, Color.TRANSPARENT);
        rectButton.setStroke(Color.TRANSPARENT);

        // When rectangle it cycles through, so we need a whole circle
        rectButton.setOnMouseClicked(event -> {
            flip=true;
            flipConnection();
            if(currentIndex==0){
                getTo=5;
                step=1;

            }else if(currentIndex==11){
                getTo=5;
                step=-1;
            }else{
                if(step==-1){
                    getTo=0;
                }else {
                    getTo=11;
                }
            }
        });

        StackPane root = new StackPane();
        root.getChildren().addAll(imageView, rectButton);
        root.setStyle("-fx-background-color: #000000;");
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Hose Connection GUI");
        stage.setX(0);
        stage.setY(0);
        return stage;

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
     * @param stage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if something goes wrong <-- Thanks ~_~
     */
    @Override
    public void start(Stage stage) throws Exception {

        getStage().show();

    }

    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Just reverses the connection, lets its driver handle any
     * socket communication
     */

    private void flipConnection(){
        if(connected){
            nozzle.sendConnectionInfo(CommunicationString.NOT_CONNECTED);
        }else{
            nozzle.sendConnectionInfo(CommunicationString.CONNECTED);
        }
        connected=!connected;
    }
}
