package Controllers.widgets.transactionTable;

import Controllers.Controller;
import Models.Name;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ReciptTable extends TransactionTable{
    public ReciptTable() {
        super();

        showTags();
    }

    void deleteSelected(){
        ObservableList<TransactionItem> items = getItems();
        ArrayList<TransactionItem> rows = new ArrayList<>(items);

        rows.forEach(row ->{
            if(row.isActive()){
                getItems().remove(row);
            }


        });
        ReceiptItem.clearNews();

        refresh();
    }
    void deleteTag(){
        ObservableList<TransactionItem> items = getItems();
        ArrayList<TransactionItem> rows = new ArrayList<>(items);

        rows.forEach(row ->{
            if(row.isActive()){
                row.setTag(null);
            }
        });
        unselectAll();
        refresh();
    }
    void setTag(){
        ObservableList<TransactionItem> items = getItems();
        ArrayList<TransactionItem> rows = new ArrayList<>(items);

        rows.forEach(row ->{
            if(row.isActive()){
                //row.setTag("test235245");
            }
        });
        unselectAll();
        refresh();
    }

}
