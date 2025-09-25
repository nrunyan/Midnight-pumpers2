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
    private LinkedList<Text> gasPrices;

    @Override
    public void start(Stage primaryStage) throws Exception {
        anchorPane = new AnchorPane();
        gasStationServer = new GasStationServer();
        gasPrices = new LinkedList<>();
        Group root = new Group();
        makeAnchor();

        root.getChildren().add(anchorPane);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Station");
        primaryStage.show();
    }

    private void makeAnchor() {
        stationPic.setFitHeight(400);
        stationPic.setFitWidth(500);
        anchorPane.getChildren().add(stationPic);
        makeGasPricesText();

    }
    private void makeGasPricesText(){
        double x = 0;
        for(int i=0;i<gasStationServer.getGasPrices().length;i++){
            Text gasPrice = new Text(10+x,75,String.format("%.2f",
                    gasStationServer.getGasPrices()[i]));
            Text gasGrade = new Text(10+x,50,"Grade_"+(i+1));
            gasGrade.setFont(Font.font(15));
            gasGrade.setFill(Color.WHITE);
            gasGrade.setStroke(Color.WHITE);
            gasPrice.setFont(Font.font(20));
            gasPrice.setStroke(Color.WHITE);
            gasPrice.setFill(Color.WHITE);
            gasPrices.add(gasPrice);
            anchorPane.getChildren().add(gasGrade);
            anchorPane.getChildren().add(gasPrice);
            x += 70;
        }
    }
}