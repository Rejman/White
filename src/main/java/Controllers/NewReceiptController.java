package Controllers;

import Controllers.widgets.inputPanels.*;
import Controllers.widgets.transactionTable.ReceiptItem;
import Controllers.widgets.transactionTable.ReciptTable;
import Controllers.widgets.transactionTable.TransactionItem;
import Models.*;
import Utils.Blocker;
import Utils.DateConvertor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

public class NewReceiptController extends Controller {

    private final ReciptTable reciptTable = new ReciptTable();
    @FXML
    private ImageView logoImageView;
    @FXML
    private SplitPane splitPane;
    @FXML
    private VBox centerSplitBox;
    @FXML
    private DatePicker transactionDatePicker;
    @FXML
    private Label expenseLabel;
    @FXML
    private Label unitLabel;
    @FXML
    private Label sourceLabel;
    @FXML
    private Button saveReciptButton;
    @FXML
    private StackPane stackForInputPanels;

    private SourcePanel sourcePanel;
    private NamePanel namePanel;
    private UnitPanel unitPanel;
    private PricePanel pricePanel;

    private final Service saveTransactions = new Service() {

        @Override
        protected Task createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    //Source newSource = sourcePanel.getNewSource();
                    Source selectedSource = sourcePanel.getSelected();
                    if(selectedSource.getId()<0){
                        selectedSource = daoContainer.getSourceDao().insertOne(selectedSource);
                    }
                    //connect to database
                    /*Connection conn = DriverManager.getConnection("jdbc:sqlite:" + daoContainer.getDataBaseUrl());
                    //when source is new
                    if (newSource != null && newSource.equals(selectedSource))
                        daoContainer.getSourceDao().insert(newSource, conn);
                    //save to database (no transaction)
                    for (Category category : ReceiptItem.getNewCategories()
                    ) {
                        System.out.println(category + " saved");
                        daoContainer.getCategoryDao().insert(category, conn);
                    }
                    for (Name name : ReceiptItem.getNewNames()
                    ) {
                        System.out.println(name + " saved");
                        daoContainer.getNameDao().insert(name, conn);
                    }
                    for (Unit unit : ReceiptItem.getNewUnits()
                    ) {
                        System.out.println(unit + " saved");
                        daoContainer.getUnitDao().insert(unit, conn);
                    }
                    for (Tag tag : ReceiptItem.getNewTags()) {
                        System.out.println(tag + " saved");
                        daoContainer.getTagDao().insert(tag, conn);
                    }
                    conn.close();*/

                    int step = 1;
                    int size = reciptTable.getItems().size();
                    for (TransactionItem rI : reciptTable.getItems()
                    ) {
                        Unit unit = rI.getUnit();
                        Name name = rI.getName();
                        Tag tag = rI.getTag();

                        if(tag!=null){
                            if(tag.getId()<0){
                                tag = daoContainer.getTagDao().insertOne(tag);
                            }
                        }
                        if(name.getId()<0){

                            Category category = name.getCategory();
                            if(category.getId()<0){
                                category = daoContainer.getCategoryDao().insertOne(category);
                            }
                            name.setCategory(category);
                            name = daoContainer.getNameDao().insertOne(name);
                        }

                        if(unit.getId()<0){
                            unit = daoContainer.getUnitDao().insertOne(unit);
                        }

                        float unitPrice = Float.parseFloat(rI.getUnitPrice());
                        float quantity = Float.parseFloat(rI.getQuantity());
                        Expense selectedExpense = null;
                        for (Expense expense : modelStructure.getExpenses()
                        ) {
                            if (expense.getUnit().equals(unit) && expense.getName().equals(name)) {
                                selectedExpense = expense;
                                break;
                            }
                        }
                        if (selectedExpense == null) {
                            selectedExpense = daoContainer.getExpenseDao().insertOne(new Expense(name, unit));
                            modelStructure.getExpenses().add(selectedExpense);

                        }

                        Date day = DateConvertor.convertToDateViaSqlDate(transactionDatePicker.getValue());
                        daoContainer.getTransactionDao().insertOne(new Transaction(DateConvertor.toLong(day), selectedSource, selectedExpense, quantity, unitPrice, tag));
                        updateProgress(step + 1, size);
                        step++;
                        //Thread.sleep(2000);
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

                    sourceLabel.setText("");
                    //sourceLabel.setPadding(new Insets(0));
                    setDatePickerTime();
                    //clear model structure
                    modelStructure.clearNames();
                    modelStructure.clearCategories();
                    modelStructure.clearUnits();
                    ReceiptItem.clearNews();
                    //reset panels

                    sourcePanel.setNewSource(null);
                    namePanel.getNewCategories().clear();
                    namePanel.getNewNames().clear();
                    unitPanel.getNewUnits().clear();
                    sourcePanel.setNextPanel(namePanel);
                    unitPanel.disable();
                    pricePanel.disable();
                    namePanel.disable();
                    sourcePanel.reset();
                    namePanel.reset();
                    unitPanel.reset();
                    sourcePanel.enable();
                    
                    //reset table
                    reciptTable.getItems().clear();

                    System.out.println("succes");
                    Blocker.unbind();

                    System.out.println("Podsumowanie:");
                    for (Name name:modelStructure.getNames()
                         ) {
                        System.out.println(name+" ["+name.getId()+"]");
                    }
                }
            };
        }
    };
    @FXML
    private StackPane conteinerForLogo;
    private final Service buildInputPanels = new Service() {
        @Override
        protected Task createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    int numberOfSteps = 3;

                    //source panel
                    sourcePanel = new SourcePanel(modelStructure, conteinerForLogo);
                    updateProgress(1, numberOfSteps);
                    //name panel
                    namePanel = new NamePanel(modelStructure);
                    updateProgress(2, numberOfSteps);
                    sourcePanel.setNextPanel(namePanel);
                    sourcePanel.setExternalLabel(sourceLabel);
                    //unit panel
                    unitPanel = new UnitPanel(modelStructure);
                    updateProgress(3, numberOfSteps);
                    namePanel.setNextPanel(unitPanel);
                    namePanel.setExternalLabel(expenseLabel);
                    //price panel
                    pricePanel = new PricePanel("zł");
                    updateProgress(4, numberOfSteps);
                    unitPanel.setNextPanel(pricePanel);
                    unitPanel.setPricePanel(pricePanel);
                    unitPanel.setExternalLabel(unitLabel);
                    namePanel.setUnitPanel(unitPanel);
                    //add expense to list
                    pricePanel.getButton().setOnAction(event -> {
                        saveReciptButton.setDisable(false);
                        addToTable();
                        //clear labels
                        namePanel.setNextPanel(unitPanel);
                        unitPanel.setNextPanel(pricePanel);
                        unitLabel.setText("");
                        expenseLabel.setText("");
                        //reset panels
                        pricePanel.reset();
                        namePanel.reset();
                        unitPanel.reset();
                        //show namePanel
                        namePanel.enable();
                    });
                    //show sourcePanel
                    sourcePanel.enable();

                    return null;
                }

                @Override
                protected void failed() {
                    getException().printStackTrace();
                    Blocker.error();

                }

                @Override
                protected void succeeded() {
                    //add to view
                    Node[] panelList = {pricePanel, namePanel, unitPanel, sourcePanel};
                    stackForInputPanels.getChildren().addAll(panelList);
                    Blocker.unbind();
                }
            };
        }
    };


    @FXML
    void onMauseUnitEnter(MouseEvent event) {
        unitLabel.setTextFill(Color.RED);
    }

    @FXML
    void onMauseUnitExit(MouseEvent event) {
        unitLabel.setTextFill(Color.BLACK);
    }

    @FXML
    void onMauseExpenseEnter(MouseEvent event) {
        expenseLabel.setTextFill(Color.RED);
    }

    @FXML
    void onMauseExpenseExit(MouseEvent event) {
        expenseLabel.setTextFill(Color.BLACK);
    }

    @FXML
    void onMauseSourceEnter(MouseEvent event) {
        logoImageView.setOpacity(0.3);
        sourceLabel.setOpacity(0.3);
    }

    @FXML
    void onMauseSourceExit(MouseEvent event) {
        logoImageView.setOpacity(1);
        sourceLabel.setOpacity(1);
    }

    @FXML
    void deleteExpense(MouseEvent event) {
        eraseSelectedModel(expenseLabel, namePanel);
    }

    @FXML
    void deleteUnit(MouseEvent event) {
        eraseSelectedModel(unitLabel, unitPanel);
    }

    @FXML
    void deleteSource(MouseEvent event) {
        eraseSelectedModel(sourceLabel, sourcePanel);
        logoImageView.setVisible(false);
        System.out.println("delete source");
    }

    private void setDatePickerTime() {
        //set date picker
        LocalDate today = LocalDate.now();
        transactionDatePicker.setValue(today);
        //block future days
        transactionDatePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(today) > 0);
            }
        });
    }

    @FXML
    void initialize() throws IOException {

        setDatePickerTime();
        Controller.setAutoSize(splitPane, 100, 100);
        VBox.setVgrow(reciptTable, Priority.ALWAYS);

        logoImageView.setFitHeight(settings.logoHeight);
        logoImageView.setFitWidth(settings.logoWidth);
        conteinerForLogo.setMinWidth(settings.logoWidth);
        conteinerForLogo.setMaxWidth(settings.logoWidth);
        conteinerForLogo.setMinHeight(settings.logoHeight);
        conteinerForLogo.setMaxHeight(settings.logoHeight);
        sourceLabel.setStyle("-fx-font-size: " + settings.logoTextSize);

        //add receiptTable
        centerSplitBox.getChildren().add(reciptTable);

        sourceLabel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                String path = "logos/" + newValue + ".jpg";
                logoImageView.setVisible(true);
                //String path = "logos/"+"test"+".jpg";
                try {
                    System.out.println("found " + path);
                    Image logo = new Image(new FileInputStream(path));
                    sourceLabel.setVisible(false);
                    logoImageView.setImage(logo);

                } catch (FileNotFoundException e) {
                    logoImageView.setImage(null);
                    sourceLabel.setVisible(true);
                    //logoImageView.resize(0,0);
                    System.out.println("not found " + path);
                }
            }
        });
        buildInputPanels.reset();
        buildInputPanels.start();

        splitPane.setDividerPosition(0, 0.1);
        splitPane.setDividerPosition(1, 0.8);
        //splitPane.setDividerPosition(2, 0.1);
    }

    private Source saveModelsToDatabase() throws SQLException {
        //get old/new source
        return null;
    }

    @FXML
    void saveRecipt(ActionEvent event) throws SQLException, ParseException, InterruptedException, CloneNotSupportedException {

        if (reciptTable.getItems().size() > 0) {

            //save to database
            Blocker.bind(saveTransactions.progressProperty());
            saveTransactions.reset();
            saveTransactions.start();

        }

    }

    private void addToTable() {
        Name selectedName = namePanel.getSelected();
        Unit selectedUnit = unitPanel.getSelected();
        String unitPrice = pricePanel.getPrice();
        String quantity = pricePanel.getQuantity();

        if (quantity == "" || quantity == null) quantity = "0";
        if (unitPrice == "" || unitPrice == null) unitPrice = "0.00";

        TransactionItem transactionItem = new ReceiptItem(selectedName, selectedUnit, unitPrice, quantity, null);

        reciptTable.getItems().add(transactionItem);
    }

    private void eraseSelectedModel(Label label, Panel panel) {
        label.setText("");
        //label.setPadding(new Insets(0));
        panel.reset();
        panel.setNextPanel(Panel.actualVisiblePanel);
        Panel.actualVisiblePanel.reset();
        Panel.actualVisiblePanel.disable();
        panel.enable();
    }
}
