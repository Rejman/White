package Controllers.widgets.transactionTable;

import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class TransactionMenu extends EditMenu{
    private MenuItem editDate = new MenuItem("date");
    private MenuItem price = new MenuItem("unit/price");
    private MenuItem editSource = new MenuItem("source");
    public TransactionMenu(IntegerProperty selectedNumber) {
        super(selectedNumber);
        Menu edit = new Menu("edit");

        edit.setDisable(true);

        edit.getItems().add(price);
        edit.getItems().add(editDate);
        edit.getItems().add(editSource);
        conditionalItems.add(edit);

        getItems().add(edit);




    }
}
