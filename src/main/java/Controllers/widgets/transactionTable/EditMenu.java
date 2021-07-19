package Controllers.widgets.transactionTable;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import java.util.ArrayList;

public class EditMenu extends ContextMenu {
    protected ArrayList<MenuItem> conditionalItems = new ArrayList<>();
    protected IntegerProperty selectedNumber;
    private void enableConditionalItems(){
        for (MenuItem item:conditionalItems
        ) {
            item.setDisable(false);
        }
    }
    private void disableConditionalItems(){
        for (MenuItem item:conditionalItems
        ) {
            item.setDisable(true);
        }
    }
    public void setOnDelete(EventHandler<ActionEvent> eventEventHandler){
        delete.setOnAction(eventEventHandler);
    }
    public void setOnDeleteTag(EventHandler<ActionEvent> eventEventHandler){
        deleteTag.setOnAction(eventEventHandler);
    }
    public void setOnSetTag(EventHandler<ActionEvent> eventEventHandler){
        setTag.setOnAction(eventEventHandler);
    }
    public void setOnSelectAll(EventHandler<ActionEvent> eventEventHandler){
        selectAll.setOnAction(eventEventHandler);
    }
    public void setOnSelectNone(EventHandler<ActionEvent> eventEventHandler){
        selectNone.setOnAction(eventEventHandler);
    }

    private MenuItem delete = new MenuItem("delete");
    private MenuItem deleteTag = new MenuItem("delete");
    private MenuItem setTag = new MenuItem("add/set");
    private MenuItem selectAll = new MenuItem("all");
    private MenuItem selectNone = new MenuItem("none");

    public EditMenu(IntegerProperty selectedNumber) {
        super();

        getStyleClass().add("context");

        Menu select = new Menu("select");
        Menu tag = new Menu("tag");
        conditionalItems.add(tag);
        tag.setDisable(true);

        conditionalItems.add(delete);
        delete.setDisable(true);

        selectNone.setDisable(true);
        select.getItems().add(selectAll);
        select.getItems().add(selectNone);

        tag.getItems().add(setTag);
        tag.getItems().add(deleteTag);

        getItems().add(select);
        getItems().add(delete);
        getItems().add(new SeparatorMenuItem());
        //if (getClass().equals(TransactionTable.class)) contextMenu.getItems().add(edit);
        getItems().add(tag);
        //contextMenu.getItems().add(rabat);

        //setContextMenu(contextMenu);
        this.selectedNumber = selectedNumber;
        this.selectedNumber.addListener((observable, oldValue, newValue) -> {

            if (newValue.equals(0)) {
                select.setText("select");
                disableConditionalItems();
                selectNone.setDisable(true);
            } else {
                enableConditionalItems();
                select.setText("select [" + (int) newValue + "]");
                selectNone.setDisable(false);
            }
        });
        /*editDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showDateWindow();
            }
        });
        rabat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showRabatWindow();
            }
        });

        editSource.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSourceWindow();
            }
        });
        */
    }
}
