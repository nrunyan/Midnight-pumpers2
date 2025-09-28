package UI;

import FXDrivers.GasStationServer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.LinkedList;

public class GasStationGui extends Application {
    private AnchorPane anchorPane;
    private GasStationServer gasStationServer;
    private Image image = new Image(getClass().getResource(
            "/pumpStation.png").toExternalForm());
    private ImageView stationPic = new ImageView(image);
    private Text moneyMade;
    private GasSHelper gasSHelper;
    private LinkedList<Text> gasPrices;
    /**
     * This is just used to make the image and how it looks. If you want to
     * make changes to an object based off changes to GasStation, hand
     * whatever object you want to change to GasStationGUI and update it there
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        anchorPane = new AnchorPane();
        gasStationServer = new GasStationServer();
        gasPrices = new LinkedList<>();
        gasSHelper = new GasSHelper(gasStationServer);
        Group root = new Group();
        makeAnchor();

        root.getChildren().add(anchorPane);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Station");
        primaryStage.show();
        Thread thread = new Thread(gasSHelper);
        thread.start();
    }

    /**
     * Make the anAnchor pain and hands the Texts to GasSHelper
     * If you want to add an object to the pane and have the intent to make
     * changes to it after the gui has been made, make sure you give that
     * object to GasSHelper and change it there in the handleMessages method
     */
    private void makeAnchor() {
        stationPic.setFitHeight(400);
        stationPic.setFitWidth(500);
        moneyMade =
                new Text(String.format("%.2f",gasStationServer.getTotalMoney()));
        moneyMade.setStroke(Color.WHITE);
        moneyMade.setFill(Color.WHITE);
        moneyMade.setFont(Font.font(20));
        moneyMade.setY(150);
        moneyMade.setX(400);

        Text moneyShower = new Text("MONEY");
        moneyShower.setStroke(Color.WHITE);
        moneyShower.setFill(Color.WHITE);
        moneyShower.setFont(Font.font(15));
        moneyShower.setX(400);
        moneyShower.setY(130);
        gasSHelper.setMoneyMade(moneyMade);

        anchorPane.getChildren().add(stationPic);
        anchorPane.getChildren().add(moneyMade);
        anchorPane.getChildren().add(moneyShower);
        makeGasPricesText();

    }

    /**
     * Makes all the texts for gasGrades,gasPrices,gasVolumes
     */
    private void makeGasPricesText(){
        double x = 0;
        for(int i=0;i<gasStationServer.getGasPrices().length;i++){
            Text gasPrice = new Text(40+x,75,String.format("%.2f",
                    gasStationServer.getGasPrices()[i]));
            Text gasVol = new Text(40+x,100,String.format("%.2f",
                    gasStationServer.getGasGallons()[i]));
            Text gasGrade = new Text(40+x,50,"Grade_"+(i+1));
            gasGrade.setFont(Font.font(15));
            gasGrade.setFill(Color.WHITE);
            gasGrade.setStroke(Color.WHITE);

            gasPrice.setFont(Font.font(20));
            gasPrice.setStroke(Color.WHITE);
            gasPrice.setFill(Color.WHITE);

            gasVol.setFont(Font.font(20));
            gasVol.setFill(Color.WHITE);
            gasVol.setStroke(Color.WHITE);

            gasPrices.add(gasPrice);
            gasSHelper.addGasPrice(gasPrice);
            gasSHelper.addGasVolume(gasVol);
            anchorPane.getChildren().add(gasGrade);
            anchorPane.getChildren().add(gasPrice);
            anchorPane.getChildren().add(gasVol);
            x += 70;
        }
        Text price = new Text(5,75,"Price");
        price.setStroke(Color.WHITE);
        price.setFill(Color.WHITE);
        price.setFont(Font.font(10));

        Text volume = new Text(5,100,"Volume");
        volume.setStroke(Color.WHITE);
        volume.setFill(Color.WHITE);
        volume.setFont(Font.font(10));

        anchorPane.getChildren().add(price);
        anchorPane.getChildren().add(volume);
    }
}