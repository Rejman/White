package Controllers;

import Controllers.widgets.ToolPane;
import Dao.DaoContainer;
import Models.Expense;
import Models.Source;
import Models.Tag;
import Utils.*;
import Utils.Settings.Settings;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.sqlite.SQLiteConfig;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;

public class MainController extends Controller {

    private final FileChooser fileChooser = new FileChooser();
    @FXML
    private Menu openRecentMenu;
    @FXML
    public Label statusLabel;
    private static Stage primaryStage;
    @FXML
    private MenuButton dataBaseButton;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private StackPane containerWithMenus;
    @FXML
    private Pane viewsContainer;
    @FXML
    private ToggleButton receiptButton;
    @FXML
    private ToggleButton editButton;
    @FXML
    private ToggleButton statisticButton;
    @FXML
    private ToggleButton historyButton;
    @FXML
    private ToggleButton settingsButton;
    @FXML
    private ToolBar toolBar;
    private String dataBaseUrl;

    @FXML
    void exit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    private Service loadData = new Service() {
        @Override
        protected Task createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() {
                    daoContainer = new DaoContainer(dataBaseUrl);
                    HashSet<Source> sources = daoContainer.getSourceDao().selectAll();
                    HashSet<Expense> expenses = daoContainer.getExpenseDao().selectAll();
                    HashSet<Tag> tags = daoContainer.getTagDao().selectAll();

                    modelStructure = new ModelStructure(daoContainer, expenses, sources, tags);
                    return null;
                }

                @Override
                protected void failed() {
                    getException().printStackTrace();
                    Blocker.error();
                }

                @Override
                protected void succeeded() {
                    //DBPosition dbPosition = new DBPosition(dataBaseUrl);
                    String lastDBUrl = settings.recentDBs.getLast();

                    if(lastDBUrl==null || !lastDBUrl.equals(dataBaseUrl)){
                        settings.recentDBs.add(dataBaseUrl);
                        updateOpenRecentMenu();
                        Serialize.saveSettings(settings);
                    }
                    dataBaseButton.setText("File ["+Settings.cutPath(dataBaseUrl)+"]");
                    statusLabel.setText(dataBaseUrl + " loaded");
                    activeAllToggleButtons();
                    Blocker.unbind();
                    loadNewView("History","History");

                }
            };
        }
    };
    private Service createDatabase = new Service() {
        @Override
        protected Task createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() {
                    int numberOfSteps = 8;
                    updateProgress(1, numberOfSteps);
                    SQLiteConfig config = new SQLiteConfig();
                    config.enforceForeignKeys(true);
                    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dataBaseUrl, config.toProperties());
                         Statement stmt = conn.createStatement()) {
                        updateProgress(2, numberOfSteps);
                        stmt.execute(DBManager.SOURCES);
                        updateProgress(1, 6);
                        System.out.println(DBManager.SOURCES);
                        updateProgress(3, numberOfSteps);
                        stmt.execute(DBManager.CATEGORIES);
                        System.out.println(DBManager.CATEGORIES);
                        updateProgress(4, numberOfSteps);
                        stmt.execute(DBManager.NAMES);
                        System.out.println(DBManager.NAMES);
                        updateProgress(5, numberOfSteps);
                        stmt.execute(DBManager.UNITS);
                        System.out.println(DBManager.UNITS);
                        updateProgress(6, numberOfSteps);
                        stmt.execute(DBManager.EXPENSES);
                        System.out.println(DBManager.EXPENSES);
                        updateProgress(7, numberOfSteps);
                        stmt.execute(DBManager.TAGS);
                        System.out.println(DBManager.TAGS);
                        updateProgress(8, numberOfSteps);
                        stmt.execute(DBManager.TRANSACTIONS);
                        System.out.println(DBManager.TRANSACTIONS);

                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.err.println("Error: create database");
                        Blocker.unbind();
                    }
                    return null;
                }

                @Override
                protected void failed() {
                    getException().printStackTrace();
                    Blocker.error();
                }

                @Override
                protected void succeeded() {
                    Blocker.bind(loadData.progressProperty());
                    loadData.reset();
                    loadData.start();
                }
            };
        }
    };

    public static void setPrimaryStage(Stage primaryStage) {
        MainController.primaryStage = primaryStage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    private void unselectAllToggleButtons() {
        receiptButton.setSelected(false);
        editButton.setSelected(false);
        statisticButton.setSelected(false);
        historyButton.setSelected(false);
        settingsButton.setSelected(false);
    }
    private void activeAllToggleButtons() {
        receiptButton.setDisable(false);
        //editButton.setDisable(false);
        //statisticButton.setDisable(false);
        historyButton.setDisable(false);
        settingsButton.setDisable(false);
    }

    private void loadNewView(String url, String title) {
        viewsContainer.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/" + url + ".fxml"));
        try {
            viewsContainer.getChildren().add(loader.load());
            if (primaryStage != null) {
                primaryStage.setTitle(title);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: load " + url);
        }
    }
    @FXML
    void fullscreen(ActionEvent event) {
        System.out.println("test");
        if(primaryStage!=null){
            if(!primaryStage.isFullScreen()){
                primaryStage.setFullScreen(true);
            }else{
                primaryStage.setFullScreen(false);
            }
        }

    }

    @FXML
    void initialize() {

        settings = Serialize.loadSettings();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("database files", "*.db"),
                new FileChooser.ExtensionFilter("All files", "*"));


        Blocker.setProgressBar(this.progressBar);
        Blocker.setElementsToBlock(Arrays.asList(containerWithMenus, toolBar));

        setAutoWidth(progressBar, 100);
        Controller.widthProperty.bind(containerWithMenus.widthProperty());
        Controller.heightProperty.bind(containerWithMenus.heightProperty());
        updateOpenRecentMenu();
        String lastDB = settings.recentDBs.getLast();

        if(lastDB!=null){
            dataBaseUrl = lastDB;
            Blocker.bind(loadData.progressProperty());
            loadData.reset();
            loadData.start();
        }
        Pane pane = new Pane();


        ToolPane toolPane = new ToolPane();

        toolPane.setMinWidth(100);
        toolPane.setMinHeight(100);
        //window.onMo
        pane.getChildren().add(toolPane);
        toolPane.setStyle("-fx-background-color: red");
        pane.setStyle("-fx-background-color: rgba(255,255,255,.5)");
        containerWithMenus.getChildren().add(pane);
    }
    public void updateOpenRecentMenu(){
        openRecentMenu.getItems().clear();
        for (String url:settings.recentDBs
             ) {
            if(url!=null){
                MenuItem menuItem = new MenuItem(Settings.cutPath(url));
                menuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        dataBaseUrl = url;
                        Blocker.bind(loadData.progressProperty());
                        loadData.reset();
                        loadData.start();
                    }
                });
                openRecentMenu.getItems().add(menuItem);
            }else{
                break;
            }
        };

    }
    @FXML
    void edit(ActionEvent event) {
        loadNewView("editPanel/EditPanel", "Edit");
        unselectAllToggleButtons();
        editButton.setSelected(true);
    }

    @FXML
    void history(ActionEvent event) {
        loadNewView("History", "History");
        unselectAllToggleButtons();
        historyButton.setSelected(true);
    }

    @FXML
    void newReceipt(ActionEvent event) {
        loadNewView("NewReceipt", "New Receipt");
        unselectAllToggleButtons();
        receiptButton.setSelected(true);
    }

    @FXML
    void statistics(ActionEvent event) {
        loadNewView("Statistics", "Statistics");
        unselectAllToggleButtons();
        statisticButton.setSelected(true);
    }

    @FXML
    void settings(ActionEvent event) {
        loadNewView("Settings", "Settings");
        unselectAllToggleButtons();
        settingsButton.setSelected(true);
    }

    @FXML
    void newDatabase(ActionEvent event) {
        File selectedFile = fileChooser.showSaveDialog(null);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("database files", "*.db"));
        String path = selectedFile.getPath();

        daoContainer = new DaoContainer(path);

        dataBaseUrl = path;
        //dataBaseName = selectedFile.getName();
        System.out.println("create: " + path);
        Blocker.bind(createDatabase.progressProperty());
        createDatabase.reset();
        createDatabase.start();

    }

    @FXML
    void openFile(ActionEvent event) {
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        String path = selectedFile.getPath();
        //dataBaseName = selectedFile.getName();
        //dataBaseName = dataBaseName.substring(0, dataBaseName.length() - 3);
        //fileNameControl(path);
        this.dataBaseUrl = path;

        System.out.println("open: " + dataBaseUrl);
        Blocker.bind(loadData.progressProperty());
        loadData.reset();
        loadData.start();

        System.out.println("start");
    }

    private String fileNameControl(String path) {
        int length = path.length();
        if (length <= 3) return path + ".db";
        String end = path.substring(length - 3);
        if (end.equals(".db")) return path;
        return path + ".db";
    }

}
