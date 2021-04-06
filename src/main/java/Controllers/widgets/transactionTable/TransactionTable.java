package Controllers.widgets.transactionTable;

import Controllers.Controller;
import Controllers.MainController;
import Controllers.widgets.HistoryPosition;
import Controllers.widgets.SelectBox;
import Controllers.widgets.inputPanels.SourcePanel;
import Dao.DaoContainer;
import Models.*;
import Utils.ColorConvertor;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class TransactionTable extends TableView<TransactionItem> {
    private static StackPane tagPane = new StackPane();
    private static Scene tagScene = new Scene(tagPane);
    private Stage tagStage = new Stage();

    private SelectBox<Tag> selectTagBox = new SelectBox<>();

    private ContextMenu contextMenu;

    private TableColumn<String, TransactionItem> nameTableColumn = new TableColumn<>("name");
    private TableColumn<String, TransactionItem> quantityTableColumn = new TableColumn<>("number");
    private TableColumn<String, TransactionItem> unitTableColumn = new TableColumn<>("unit");
    private TableColumn<String, TransactionItem> unitPriceTableColumn = new TableColumn<>("price");
    private TableColumn<String, TransactionItem> totalPriceTableColumn = new TableColumn<>("TOTAL");
    private TableColumn<String, TransactionItem> tagTableColumn = new TableColumn<>("tag");
    private TableColumn<TransactionItem, Boolean> checkTableColumn = new TableColumn<>("");


    private Service deleteSelected = new Service() {
        @Override
        protected Task createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {

                    ObservableList<TransactionItem> items = getItems();
                    ArrayList<TransactionItem> rows = new ArrayList<>(items);

                    rows.forEach(row -> {
                        if (row.isActive()) {
                            Transaction transaction = row.getTransaction();
                            Expense expense = transaction.getExpense();
                            Source source = transaction.getSource();
                            Name name = expense.getName();
                            Unit unit = expense.getUnit();
                            Category category = name.getCategory();
                            Tag tag = transaction.getTag();

                            if (Controller.daoContainer.getTransactionDao().delete(row.getTransaction())) {
                                if (Controller.daoContainer.getTagDao().delete(tag)) {
                                    Controller.modelStructure.deleteTag(tag);
                                }
                                if (Controller.daoContainer.getSourceDao().delete(source)) {
                                    Controller.modelStructure.deleteSource(source);
                                }
                                if (Controller.daoContainer.getExpenseDao().delete(expense)) {
                                    Controller.modelStructure.deleteExpense(expense);
                                    if (Controller.daoContainer.getUnitDao().delete(unit)) {
                                        Controller.modelStructure.deleteUnit(unit);
                                    }
                                    if (Controller.daoContainer.getNameDao().delete(name)) {
                                        Controller.modelStructure.deleteName(name);
                                        if (Controller.daoContainer.getCategoryDao().delete(category)) {
                                            Controller.modelStructure.deleteCategory(category);
                                        }
                                    }
                                }
                                HistoryPosition.selected.getTransactions().remove(row.getTransaction());
                                getItems().remove(row);
                            }
                        }
                    });

                    return null;
                }

                @Override
                protected void failed() {
                    getException().printStackTrace();

                }

                @Override
                protected void succeeded() {
                    //add to view

                    refresh();
                    System.out.println("Update total");
                    System.out.println(HistoryPosition.selected.getTransactions());
                    HistoryPosition.selected.updateTotal();

                }
            };
        }
    };

    private Service deleteTag = new Service() {
        @Override
        protected Task createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {

                    ObservableList<TransactionItem> items = getItems();
                    ArrayList<TransactionItem> rows = new ArrayList<>(items);

                    rows.forEach(row -> {
                        if (row.isActive()) {
                            Transaction transaction = row.getTransaction();
                            Tag tag = transaction.getTag();
                            if(tag!=null){

                                System.out.println(tag+" - "+tag.getId());
                                Controller.daoContainer.getTransactionDao().setTag(transaction,null);
                                System.out.println("usnięto tag: "+tag);
                                if(Controller.daoContainer.getTagDao().delete(tag)){
                                    System.out.println(" bezpowrotnie");
                                    Controller.modelStructure.deleteTag(tag);

                                }

                            }
                            row.setTag(null);
                            transaction.setTag(null);
                        }
                    });

                    return null;
                }

                @Override
                protected void failed() {
                    getException().printStackTrace();

                }

                @Override
                protected void succeeded() {
                    //add to view
                    selectTagBox.unselect();
                    selectTagBox.refresh();
                    unselectAll();
                }
            };
        }
    };
    private Service updateTag = new Service() {
        @Override
        protected Task createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {

                    ObservableList<TransactionItem> items = getItems();
                    ArrayList<TransactionItem> rows = new ArrayList<>(items);
                    /*if (selectTagBox.getSelectedItem() == null) {
                        Tag unknown = Controller.daoContainer.getTagDao().insertOne(new Tag(selectTagBox.getText()));
                        selectTagBox.setSelectedItem(unknown);
                        Controller.modelStructure.addNewTag(unknown);
                    }*/
                    rows.forEach(row -> {
                        if (row.isActive()) {

                            Transaction transaction = row.getTransaction();
                            Tag tag = transaction.getTag();

                            if(tag!=null){
                                long oldTagId = tag.getId();
                                Tag oldTag = new Tag(oldTagId,"");
                                Controller.daoContainer.getTransactionDao().setTag(transaction,null);
                                if(Controller.daoContainer.getTagDao().delete(oldTag)){
                                    System.out.println(" bezpowrotnie");
                                    Controller.modelStructure.deleteTag(oldTag);
                                }
                            }
                            System.out.println(Controller.modelStructure.getTags());
                            Tag newTag = (Tag) selectTagBox.getSelectedItem();
                            System.out.println(newTag);
                            row.setTag(newTag);
                            transaction.setTag(newTag);
                            Controller.daoContainer.getTransactionDao().setTag(transaction,newTag);
                        }
                    });
                    return null;
                }

                @Override
                protected void failed() {
                    getException().printStackTrace();

                }

                @Override
                protected void succeeded() {
                    //add to view
                    selectTagBox.unselect();
                    selectTagBox.refresh();

                    unselectAll();
                }
            };
        }
    };

    public TransactionTable() {
        getStyleClass().add("transactionTable");
        setFocusTraversable(false);
        setEditable(true);

        TableColumn[] columns = buildColumns();
        getColumns().addAll(columns);

        constructRows();

        contextMenu = buildContextMenu();
        selectTagBox = buildSelectTagBox();

    }

    public static Scene getTagScene() {
        return tagScene;
    }

    private ContextMenu buildContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getStyleClass().add("context");
        MenuItem deleteSelected = new MenuItem("delete selected");
        MenuItem selectAll = new MenuItem("select all");
        MenuItem unselectAll = new MenuItem("unselect all");
        MenuItem deleteTag = new MenuItem("delete tag");
        MenuItem setTag = new MenuItem("set tag");

        contextMenu.getItems().add(selectAll);
        contextMenu.getItems().add(unselectAll);
        contextMenu.getItems().add(deleteSelected);
        contextMenu.getItems().add(deleteTag);
        contextMenu.getItems().add(setTag);

        setContextMenu(contextMenu);
        deleteSelected.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteSelected();
            }
        });
        deleteTag.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteTag();
            }
        });
        setTag.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showTagWindow();
            }
        });
        selectAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectAll();
            }
        });
        unselectAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                unselectAll();
            }
        });
        return contextMenu;
    }

    private TableColumn[] buildColumns() {
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tagTableColumn.setVisible(false);

        totalPriceTableColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalPriceTableColumn.getStyleClass().add("right-col");
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameTableColumn.getStyleClass().add("center-col");
        quantityTableColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityTableColumn.getStyleClass().add("right-col");
        unitTableColumn.setCellValueFactory(new PropertyValueFactory<>("shortcut"));
        unitTableColumn.getStyleClass().add("left-col");
        unitPriceTableColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        unitPriceTableColumn.getStyleClass().add("right-col");
        tagTableColumn.setCellValueFactory(new PropertyValueFactory<>("tag"));
        tagTableColumn.getStyleClass().add("center-col");
        checkTableColumn.setCellValueFactory(cellData -> cellData.getValue().getActiveProperty());
        checkTableColumn.getStyleClass().add("center-col");

        TableColumn[] columns = {tagTableColumn, nameTableColumn, quantityTableColumn, unitTableColumn, unitPriceTableColumn, totalPriceTableColumn, checkTableColumn};
        checkTableColumn.setMaxWidth(35);
        checkTableColumn.setMinWidth(35);
        checkTableColumn.setCellFactory(param -> new CheckBoxTableCell<>());
        checkTableColumn.setEditable(false);
        Image black = new Image("icons/black-checkmark-24.png");
        Image white = new Image("icons/white-checkmark-24.png");
        checkTableColumn.setCellFactory(col -> new TableCell<TransactionItem, Boolean>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitWidth(24);
                imageView.setFitHeight(24);
                setGraphic(imageView);
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                String id = getTableRow().getId();
                if (empty || item == null) {
                    // no image for empty cells
                    imageView.setImage(null);
                } else {
                    if (id != null) {
                        if (id.equals("white")) {
                            imageView.setImage(item ? white : null);
                        } else if (id.equals("black")) {
                            imageView.setImage(item ? black : null);
                        }
                    }
                }
            }
        });
        return columns;
    }

    private void constructRows() {
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setRowFactory(tv -> {
            TableRow<TransactionItem> row = new TableRow<TransactionItem>() {
                @Override
                public void updateItem(TransactionItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setStyle("");
                    } else {
                        /*if(item.getTag()!=null){
                            tagTableColumn.setVisible(true);
                        }*/
                        if (!item.isActive()) {
                            setColor(item.getLightColor(), this);
                        } else {
                            setColor(item.getColor(), this);
                        }
                    }
                }
            };

            row.setOnMouseEntered(event -> {
                TransactionItem item = row.getItem();
                if (item != null) setColor(item.getColor(), row);
            });
            row.setOnMouseExited(event -> {
                TransactionItem item = row.getItem();
                if (item != null && !item.isActive()) setColor(item.getLightColor(), row);
            });
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    TransactionItem item = row.getItem();

                    if (item.isActive()) {
                        setColor(item.getLightColor(), row);
                        item.setActive(false);
                    } else {
                        setColor(item.getColor(), row);
                        item.setActive(true);
                    }
                }
            });
            return row;
        });
    }

    public SelectBox<Tag> buildSelectTagBox() {

        //selectTagBox.addData(Controller.modelStructure.getTags());
        selectTagBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            boolean transactions = getClass().equals(TransactionTable.class);
            if (newValue) {

                if (selectTagBox.getSelectedItem() == null) {

                    if(transactions){
                        Tag unknown = Controller.daoContainer.getTagDao().insertOne(new Tag(selectTagBox.getText()));
                        selectTagBox.setSelectedItem(unknown);
                        Controller.modelStructure.addNewTag(unknown);
                    }else{
                        Tag unknown = new Tag(selectTagBox.getText());
                        selectTagBox.setSelectedItem(unknown);
                        Controller.modelStructure.addNewTag(unknown);
                    }


                }
                if(transactions){

                    updateTag.reset();
                    updateTag.start();
                }else{
                    ObservableList<TransactionItem> items = getItems();
                    ArrayList<TransactionItem> rows = new ArrayList<>(items);


                    rows.forEach(row -> {
                        if (row.isActive()) {

                            row.setTag((Tag)selectTagBox.getSelectedItem());
                        }
                    });

                    selectTagBox.unselect();
                    selectTagBox.refresh();

                    unselectAll();
                }
                tagStage.close();
                this.refresh();
            }
        }));
        //
        tagStage.setTitle("Set tag for selected transaction");
        tagStage.setScene(tagScene);
        tagPane.getChildren().add(selectTagBox);
        tagStage.initModality(Modality.APPLICATION_MODAL);

        Stage primaryStage = MainController.getPrimaryStage();
        tagStage.initOwner(primaryStage);
        return selectTagBox;
    }

    private void setColor(String bgColor, TableRow tableRow) {

        Color contrast = ColorConvertor.getContrastColor(bgColor);
        tableRow.setStyle("-fx-background-color: " + bgColor);
        if (Color.WHITE.equals(contrast)) {
            tableRow.setId("white");
        } else if (Color.BLACK.equals(contrast)) {
            tableRow.setId("black");
        }
    }

    public void hideTags() {
        tagTableColumn.setVisible(false);
    }

    public void showTags() {
        tagTableColumn.setVisible(true);
    }

    public void addTransaction(Transaction transaction) {

        TransactionItem transactionItem = new TransactionItem(transaction);
        getItems().add(transactionItem);
    }

    void unselectAll() {
        for (TransactionItem item : getItems()
        ) {
            item.setActive(false);
        }
        refresh();
    }

    void selectAll() {
        for (TransactionItem item : getItems()
        ) {
            item.setActive(true);
        }
        refresh();
    }

    void deleteSelected() {
        deleteSelected.reset();
        deleteSelected.start();
    }

    void deleteTag() {
        deleteTag.reset();
        deleteTag.start();
    }

    void showTagWindow() {
        tagStage = new Stage();
        //!!!

        selectTagBox.addData(Controller.modelStructure.getTags());
        tagStage.setTitle("Set tag for selected transaction");

        tagStage.setScene(tagScene);

        tagStage.show();

    }

}
