import Controllers.MainController;
import Controllers.widgets.transactionTable.TransactionTable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    private static final String MAIN_FXML_PATH = "/fxml/Main.fxml";

    private static final String[] STYLE_PATHS = {
            "style/field.css",
            "style/transactionTable.css",
            "style/historyPosition.css",
            "style/search-box.css",
            "style/input-panel.css"
    };

    public static void main(String[] args) {
        launch(args);
    }

    private void loadStyle(Scene scene, String... paths) {
        for (String path : paths
        ) {
            scene.getStylesheets().add(Main.class.getResource(path).toExternalForm());
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(MAIN_FXML_PATH));
        StackPane stackPane = loader.load();
        MainController mainController = loader.getController();
        Scene scene = new Scene(stackPane);
        loadStyle(scene, STYLE_PATHS);
        primaryStage.setScene(scene);
        TransactionTable.getTagScene().getStylesheets().add(Main.class.getResource("style/search-box.css").toExternalForm());
        TransactionTable.getSourceScene().getStylesheets().add(Main.class.getResource("style/input-panel.css").toExternalForm());

        mainController.setPrimaryStage(primaryStage);
        primaryStage.show();
    }
}
