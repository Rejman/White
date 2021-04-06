package Controllers.widgets;

import Controllers.Controller;
import Models.Model;
import Utils.ColorConvertor;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;

public class SearchBox<T extends Model> extends VBox {
    public static double oppacity = 1;
    protected TextField searchTextField = new TextField();
    protected ListView<T> listView = new ListView<T>();
    private MultipleSelectionModel<T> selectionModel;
    private HashSet<T> allItems = null;
    private HashSet<T> dedicatedItems = null;
    private boolean dedicated = false;
    public void setColor(String color){
        listView.setStyle("-fx-selection-bar:"+color);
    }
    public void setDedicatedItems(HashSet<T> dedicatedItems) {
        this.dedicatedItems = dedicatedItems;
        listView.getItems().setAll(dedicatedItems);
        dedicated = true;
    }
    public void clearDedicatedItems() {
        dedicated = false;
        searchTextField.setText("?");
        searchTextField.setText("");
    }

    public TextField getSearchTextField() {
        return searchTextField;
    }

    public ListView<T> getListView() {
        return listView;
    }

    public MultipleSelectionModel<T> getSelectionModel() {
        return selectionModel;
    }

    public void refresh() {
        listView.refresh();
    }
    protected void addHover(Cell<T> cell){

    }
    public SearchBox() {
        getStyleClass().add("search-box");
        listView.setFocusTraversable(false);
        setVgrow(listView, Priority.ALWAYS);
        searchTextField.setPromptText("\uD83D\uDD0D");
        searchTextField.setAlignment(Pos.CENTER_LEFT);

        this.getChildren().add(searchTextField);
        this.getChildren().add(listView);
        selectionModel = listView.getSelectionModel();
        searchTextField.textProperty();

        listView.setCellFactory(lv -> {
            ListCell<T> cell = new ListCell<T>() {
                @Override
                public void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null ) {
                        setStyle("-fx-background-color: white");
                        setTextFill(Color.BLACK);
                        setText(null);
                    } else {
                        setText(item.toString());
                        String itemColor = item.getColor();
                        System.out.println(itemColor);
                        if(itemColor!=null){
                            String color = ColorConvertor.toHEXString(itemColor, Controller.settings.colorIntense);
                            setStyle("-fx-background-color: "+ color);
                            setTextFill(ColorConvertor.getContrastColor(color));
                        }

                    }
                }
            };
            addHover(cell);

            return cell ;
        });
        /*listView.setCellFactory(param -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null ) {
                    setStyle("-fx-background-color: white");
                    setText(null);
                } else {
                    setText(item.toString());
                    String itemColor = item.getColor();
                    System.out.println(itemColor);
                    if(itemColor!=null){
                        String color = ColorConvertor.toHEXString(itemColor, oppacity);
                        setStyle("-fx-background-color: "+ color);
                        setTextFill(ColorConvertor.getContrastColor(color));
                    }

                }

            }

        });*/

/*        getSelectionModel().selectedItemProperty().addListener(new ChangeListener<T>() {

            @Override
            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
                // Your action here
                System.out.println("Selected item: " + newValue);
                getStyleClass().add("test");
            }
        });*/

    }

    public <T> void addData(HashSet<T> list) {
        ObservableList<T> observableList = (ObservableList<T>) listView.getItems();
        //observableList.clear();
        observableList.setAll(list);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList tempList = new ArrayList();
            if (newValue.equals("") && dedicated) {
                listView.getItems().setAll(dedicatedItems);
            } else {
                for (T item : list
                ) {
                    int length = newValue.length();
                    String name = item.toString();
                    if (name.length() >= length) {
                        if (name.toLowerCase().contains(newValue.toLowerCase())) {
                            tempList.add(item);
                        }
                    }
                }
                listView.getItems().setAll(tempList);
            }
        });

        searchTextField.addEventFilter(KeyEvent.KEY_PRESSED, event ->{

            switch (event.getCode()) {
                case DOWN:
                    System.out.println("DOWN");
                    listView.requestFocus();
                    listView.getSelectionModel().selectFirst();
                    break;
                case UP:
                    listView.requestFocus();
                    listView.getSelectionModel().selectLast();
                    break;
            }
        });
        listView.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            switch (event.getCode()) {
                case UP:
                    if (listView.getSelectionModel().getSelectedIndex() == 0) {
                        searchTextField.requestFocus();
                        listView.getFocusModel().focus(-1);
                        listView.getSelectionModel().clearSelection();

                    }
                    break;
                case DOWN:
                    System.out.println("teraz");
                    int id = listView.getItems().size() - 1;
                    if (listView.getSelectionModel().getSelectedIndex() >= id) {
                        System.out.println("ok");
                        searchTextField.requestFocus();
                        listView.getFocusModel().focus(-1);
                        listView.getSelectionModel().clearSelection();
                        System.out.println(listView.getSelectionModel().getSelectedIndex());
                        System.out.println(listView.getFocusModel().getFocusedIndex());

                    }
                    break;
            }
        });

    }
}
