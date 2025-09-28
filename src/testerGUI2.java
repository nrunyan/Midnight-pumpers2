import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class testerGUI2 {
    public Stage getStage() {
        Stage stage = new Stage();
        VBox root = new VBox();
        Scene scene = new Scene(root, 300, 200);
        stage.setTitle("Window 1");
        stage.setScene(scene);
        return stage;
    }
}
