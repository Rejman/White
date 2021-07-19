package Controllers.widgets.transactionTable;

import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Menu;

public class ReceiptMenu extends EditMenu{

    public ReceiptMenu(IntegerProperty selectedNumber) {
        super(selectedNumber);

        Menu rabat = new Menu("rabat");

        rabat.setDisable(true);

        conditionalItems.add(rabat);

        getItems().add(rabat);
    }
}
