package Controllers;

import Utils.ColorConvertor;
import Utils.Serialize;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class SettingsController extends Controller{
    private final Node databaseIcon = new ImageView(
            new Image("icons/database-24.png")
    );
    private final Node eyeIcon = new ImageView(
            new Image("icons/eye-24.png")
    );
    private final Node sourceIcon = new ImageView(
            new Image("icons/shop-24.png")
    );
    private final Node expenseIcon = new ImageView(
            new Image("icons/open-parcel-24.png")
    );
    private final Node categorizeIcon = new ImageView(
            new Image("icons/categorize-24.png")
    );
    private final Node unitsIcon = new ImageView(
            new Image("icons/scales-24.png")
    );
    private int logoTextSize = 0;
    @FXML
    private TreeView<String> menuTreeView;
    @FXML
    private Label logoSizeLabel;
    @FXML
    private TabPane tabPane;
    @FXML
    private Slider colorSlider;
    @FXML
    private Rectangle logoRectangle;
    @FXML
    void save(ActionEvent event) {

        settings.logoTextSize = logoTextSize;
        settings.colorIntense = colorSlider.getValue()/100;
        settings.logoHeight = (int) logoRectangle.getHeight();
        settings.logoWidth = (int) logoRectangle.getWidth();
        Serialize.saveSettings(settings);

    }
    @FXML
    private Pane containerPane;
    private void loadView(String url){
        containerPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/" + url + ".fxml"));
        try {
            containerPane.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: load " + url);
        }
    }
    private void buildTree(){
        TreeItem<String> root = new TreeItem<>("Settings");
        menuTreeView.setRoot(root);
        //menuTreeView.setShowRoot(false);
        TreeItem<String> anInterfaceItem = new TreeItem<>("interface", eyeIcon);
        TreeItem<String> colorsItem = new TreeItem<>("colors");
        TreeItem<String> logoItem = new TreeItem<>("logo");
        TreeItem<String> fontItme = new TreeItem<>("fonts");
        anInterfaceItem.getChildren().add(colorsItem);
        anInterfaceItem.getChildren().add(logoItem);
        anInterfaceItem.getChildren().add(fontItme);

        TreeItem<String> database = new TreeItem<>("database", databaseIcon);
        TreeItem<String> sourcesItem = new TreeItem<>("sources", sourceIcon);
        TreeItem<String> categoriesItem = new TreeItem<>("categories", categorizeIcon);
        TreeItem<String> expensesItem = new TreeItem<>("expenses", expenseIcon);
        TreeItem<String> unitsItem = new TreeItem<>("units", unitsIcon);
        database.getChildren().add(sourcesItem);
        database.getChildren().add(categoriesItem);
        database.getChildren().add(expensesItem);
        database.getChildren().add(unitsItem);

        root.getChildren().add(anInterfaceItem);
        root.getChildren().add(database);

        menuTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && newValue != oldValue){
                System.out.println(newValue);
                switch (newValue.getValue()){
                    case "sources":
                        loadView("editPanel/SourcesView");
                        break;
                    case "expenses":
                        loadView("editPanel/ExpensesView");
                        break;
                    case "categories":
                        loadView("editPanel/CategoriesView");
                        break;
                    case "units":
                        loadView("editPanel/UnitsView");
                        break;
                    case "colors":
                        loadView("editPanel/ColorsView");
                        break;
                    case "logo":
                        loadView("editPanel/LogoView");
                        break;
                }
            }
        });
    }
    @FXML
    private Pane mainPane;
    @FXML
    private HBox container;
    @FXML
    void initialize() {
        //Controller.setAutoSize(container,100,100);
        buildTree();
    }

}
