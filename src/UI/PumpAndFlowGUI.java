package UI;

import FXDrivers.Flowmeter;
import FXDrivers.Pump;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import sim.Gas;

import java.util.Random;

public class PumpAndFlowGUI extends Application {
    private static final AnchorPane anchorPane = new AnchorPane();
    private Flowmeter flowmeter;
    private PFGUIhelper guiHelper;
    private Pump pump;
    private Gas gas;
    private FuelTank fuelTank;
    private Stage stage;

    private Timeline timeline;
    //this is how long it takes to fill, the rate can be changed, this is just the
    // default
    final double durration =20;

    public Stage getStage() {
        Stage primaryStage=new Stage();
        fuelTank = new FuelTank(120, 150, 100, 50,0);
        fuelTank.getTankPane().setBackground(Background.fill(Color.BLACK));
        //fuelTank.getTankPane().setLayoutX(1000);
        HBox fuelroot = new HBox(50, fuelTank.getTankPane());
        fuelroot.setPadding(new Insets(20));
        fuelroot.setBackground(Background.fill(Color.BLACK));

        timeline = new Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(0),
                        new javafx.animation.KeyValue(fuelTank.fuelLevelProperty(), 0)),
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(durration),
                        new javafx.animation.KeyValue(fuelTank.fuelLevelProperty(), 100))
        );
        timeline.setAutoReverse(true);
        timeline.setCycleCount(1);


        flowmeter = new Flowmeter();
        pump = new Pump();
        fuelTank.setTankColor(pump.getGasType());
        guiHelper = new PFGUIhelper();
        this.gas = flowmeter.getGas();
        //Should this be handled in the GUI? Seems like a logical object could help separate parts of this code
        pump.setGas(gas);
        guiHelper.setGas(gas);
        guiHelper.setFlowmeter(flowmeter);
        guiHelper.setPump(pump);
        guiHelper.setTimeline(timeline);
        guiHelper.setFuelTank(fuelTank);
        Group root = new Group();
        anchorPane.setPrefHeight(400);
        anchorPane.setPrefWidth(500);
        anchorPane.setBackground(Background.fill(Color.BLACK));
        fuelroot.setBackground(Background.fill(Color.BLACK));
        makeAnchor();
        root.getChildren().add(anchorPane);
        root.getChildren().add(fuelroot);
        //fuelroot.setAlignment(Pos.CENTER);
        fuelroot.setLayoutX(50);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("PumpFlow");
        anchorPane.prefWidthProperty().bind(scene.widthProperty());
        anchorPane.prefHeightProperty().bind(scene.heightProperty());
        Thread thread2 = new Thread(guiHelper);
        thread2.start();
        stage=primaryStage;
        primaryStage.setX(0);
        primaryStage.setY(350);
        new HoseConGUI(flowmeter).getStage().show();
        return primaryStage;

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        getStage().show();
    }
    private void makeAnchor(){
        double xs = -50;
        double ys = -120;
        double rs = 0;
        double ws =-200;
        double hs =0;
        double fs = 0;
        double xcs = -50;
        double cOffset = -90;
        Rectangle leftFirstPipe = new Rectangle(50+xcs,400+ys,900+ws,100+hs);
        leftFirstPipe.setStroke(Color.BLUE);
        leftFirstPipe.setFill(Color.LIGHTBLUE);

        Rectangle leftScrewBlock = new Rectangle(300+xcs+cOffset,375+ys,50,150+hs);
        leftScrewBlock.setStroke(Color.BLUE);
        leftScrewBlock.setFill(Color.LIGHTBLUE);

        Rectangle rightFirstPipe = new Rectangle(575+xcs,400+ys,375+ws,
                100+hs);
        rightFirstPipe.setStroke(Color.BLUE);
        rightFirstPipe.setFill(Color.LIGHTBLUE);

        Rectangle rightScrewBlock = new Rectangle(650+xcs+cOffset,375+ys,50,
                150+hs);
        rightScrewBlock.setStroke(Color.BLUE);
        rightScrewBlock.setFill(Color.LIGHTBLUE);

        Circle outerCircle = new Circle(500+xcs+cOffset, 450+ys, 100+rs); // centerX,
        // centerY,
        // radius
        outerCircle.setFill(Color.BLUE);

        // Inner circle (the "hole")
        Circle innerCircle = new Circle(500+xcs+cOffset, 450+ys, 75+rs); // centerX,
        // centerY,
        // radius
        innerCircle.setFill(Color.WHITE); // Or any color to represent the hole


        // Subtract the inner circle from the outer circle to create the donut shape
        Shape donut = Shape.subtract(outerCircle, innerCircle);
        donut.setStroke(Color.RED);
        donut.setFill(Color.LIGHTPINK);

        Circle pivot = new Circle(500+xcs+cOffset,450+ys,20+rs);
        pivot.setFill(Color.LIGHTPINK);
        pivot.setStroke(Color.RED);

        Rectangle valvePipe1 = new Rectangle(425+xcs+cOffset,437.5+ys,150,25+hs);
        valvePipe1.setFill(Color.LIGHTPINK);
        valvePipe1.setStroke(Color.RED);

        Rectangle valvePipe2 = new Rectangle(425+xcs+cOffset,437.5+ys,150,25+hs);
        valvePipe2.setFill(Color.LIGHTPINK);
        valvePipe2.setStroke(Color.RED);

        Rotate rotate1 = new Rotate();
        rotate1.setAngle(45);
        rotate1.setPivotX(500+xcs+cOffset);
        rotate1.setPivotY(450+ys);

        Rotate rotate2 = new Rotate();
        rotate2.setAngle(135);
        rotate2.setPivotX(500+xcs+cOffset);
        rotate2.setPivotY(450+ys);

        ProgressBar progressBar = new ProgressBar(.1);
        progressBar.setStyle("-fx-accent: brown;");
        progressBar.setPrefHeight(60+hs);
        progressBar.setPrefWidth(850+ws);
        progressBar.setLayoutX(70+xcs);
        progressBar.setLayoutY(420+ys);


        Text text = new Text(375+xcs+cOffset,300+ys,"00.00");
        text.setFill(Color.WHITE);
        text.setFont(new Font(100+fs));







        guiHelper.setRotate1(rotate1);
        guiHelper.setRotate2(rotate2);
        guiHelper.setProgressBar(progressBar);
        guiHelper.setText(text);

        guiHelper.setProgressBarColor();

        valvePipe1.getTransforms().add(rotate1);
        valvePipe2.getTransforms().add(rotate2);


        anchorPane.getChildren().add(leftFirstPipe);
        //anchorPane.getChildren().add(rightFirstPipe);
        anchorPane.getChildren().add(progressBar);
        anchorPane.getChildren().add(leftScrewBlock);
        anchorPane.getChildren().add(rightScrewBlock);
        anchorPane.getChildren().add(valvePipe1);
        anchorPane.getChildren().add(valvePipe2);
        anchorPane.getChildren().add(donut);
        anchorPane.getChildren().add(pivot);
        anchorPane.getChildren().add(text);

    }
    public void turnON(){
        System.out.println("pump is on");
        pump.turnOn();
        System.out.println(gas.isOnOff());
        System.out.println("on");
        timeline.play();
    }
    public void turnOF(){
        pump.turnOf();
        System.out.println(gas.isOnOff());
        System.out.println("off");
        timeline.stop();
    }
    private void setGasType(){
        Random random = new Random();
        int gasType = random.nextInt(3) + 1;
        pump.setGasType(gasType);
        guiHelper.setProgressBarColor();
    }
    private void nothing(){

    }

    /**
     * This changes the rate of how the tank is filled
     * @param value the new pace the fuel is se at, default is .5
     */
    private void changeRate(double value){
        timeline.setRate(value);
    }

    /**
     * So this class creates the pane and controls the look of the animation
     */
    class FuelTank {
        private final Pane tankPane;
        private final Rectangle tankBody;
        private final Rectangle fuel;
        private final DoubleProperty fuelLevel;
        // 0-100 so we can change this if the thing tells us the percent

        public FuelTank(double width, double height, double arc, double yOffset,int initialValue) {
            fuelLevel = new SimpleDoubleProperty(0);
            tankPane = new Pane();
            tankPane.setBackground(Background.fill(Color.BLACK));

            tankBody = new Rectangle(width, height);
            tankBody.setArcWidth(arc);
            tankBody.setArcHeight(arc);
            tankBody.setFill(Color.RED);
            tankBody.setStroke(Color.MAROON);
            tankBody.setStrokeWidth(4);
            tankBody.setStrokeType(StrokeType.INSIDE);
            tankBody.setY(yOffset);
            fuel = new Rectangle(width - 8, height - 8);
            fuel.setX(4);
            fuel.setY(yOffset + height - 4 - (height - 8));
            fuel.setArcWidth(arc);
            fuel.setArcHeight(arc);

            setTankColor(1);

            Rectangle emptyPart = new Rectangle(width - 8, height - 8);
            emptyPart.setX(4);
            emptyPart.setY(yOffset + 4);
            emptyPart.setArcWidth(arc);
            emptyPart.setArcHeight(arc);
            fuel.setClip(emptyPart);

            fuel.heightProperty().bind(fuelLevel.divide(100).multiply(height - 8));
            fuel.yProperty().bind(tankBody.yProperty()
                    .add(tankBody.heightProperty())
                    .subtract(fuel.heightProperty())
                    .subtract(4));


            Circle cap = new Circle(width / 2, yOffset + 4, 15);
            cap.setFill(Color.MAROON);
            cap.setStroke(Color.BLACK);


            QuadCurve hose = new QuadCurve();
            hose.setStartX(width / 2);
            hose.setStartY(yOffset + 4);
            hose.setControlX(width / 2 - 60);
            hose.setControlY(yOffset - 80);
            hose.setEndX(width / 2 - 30);
            hose.setEndY(yOffset - 30);
            hose.setStroke(Color.valueOf("CDCB1AFF"));
            hose.setStrokeWidth(10);
            hose.setFill(null);

            Circle nozzle = new Circle(hose.getEndX(), hose.getEndY(), 7);
            nozzle.setFill(Color.valueOf("CDCB1AFF"));
            nozzle.setStroke(Color.valueOf("CDCB1AFF"));
            tankPane.getChildren().addAll(hose, tankBody, fuel, cap, nozzle);
        }

        public void setTankColor(int x){
            Color color1;
            Color color2;
            switch (x) {
                // orange and blue, black and purple, pink and white, pink and green
                case 1 -> {
                    color1 = Color.rgb(159, 69, 255);
                    color2= Color.rgb(67, 23, 87);
                }
                case 2 -> {
                    color1 = Color.rgb(200, 96, 33);
                    color2= Color.rgb(36, 96, 192);
                }
                case 3 -> {
                    color1 = Color.rgb(221, 80, 80);
                    color2= Color.rgb(204, 177, 192);
                }
                case 4 -> {
                    color1 = Color.rgb(221, 80, 80);
                    color2= Color.rgb(139, 255, 50);
                }
                default -> {
                    color1= Color.rgb(0, 0, 0);
                    color2= Color.rgb(67, 23, 87);
                }
            };
            fuel.setFill(new LinearGradient(
                    0, 0, 0, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                    new Stop(0, color1), new Stop(1, color2))
            );
        }


        public DoubleProperty fuelLevelProperty() {
            return fuelLevel;
        }

        public void setFuelLevel(double level){
            fuelLevel.set(level);
        }
        public Pane getTankPane() {
            return tankPane;
        }
    }
}

