package Controllers.widgets.transactionTable;

import Controllers.Controller;
import Models.*;
import Utils.ColorConvertor;
import Utils.UnitConverter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.math.BigDecimal;

public class TransactionItem {

    private final BooleanProperty active = new SimpleBooleanProperty();

    private Transaction transaction;
    private String total;
    protected String color;
    protected String lightColor;

    public TransactionItem(Transaction transaction) {
        this.transaction=transaction;
        Expense expense = transaction.getExpense();

        this.total = UnitConverter.toPrice(transaction.getUnit_price().multiply(transaction.getNumber()));

        this.color = ColorConvertor.toHEXString(expense.getName().getCategory().getColor());
        this.lightColor = ColorConvertor.toHEXString(this.color, Controller.settings.colorIntense);

    }
    public TransactionItem(){
    }

    public String getColor() {
        return color;
    }

    public String getLightColor() {
        return lightColor;
    }

    public boolean isActive() {
        return active.get();
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public BooleanProperty getActiveProperty() {
        return active;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Tag getTag() {
/*        Tag tag = transaction.getTag();
        if(tag==null) return new Tag("");*/
        return transaction.getTag();
    }

    public void setTag(Tag tag) {
        transaction.setTag(tag);
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getShortcut() {
        Unit unit = transaction.getExpense().getUnit();
        String shortuct = unit.getShortcut();
        if(shortuct==null) return unit.getName();
        return shortuct;
    }

    public void setShortcut(String shortcut) {
        transaction.getExpense().getUnit().setShortcut(shortcut);
    }

    public Name getName() {
        return transaction.getExpense().getName();
    }

    public void setName(Name name) {
        transaction.getExpense().setName(name);
    }

    public Unit getUnit() {
        return transaction.getExpense().getUnit();
    }

    public void setUnit(Unit unit) {
        transaction.getExpense().setUnit(unit);
    }

    public String getUnitPrice() {
        return UnitConverter.toPrice(transaction.getUnit_price());
    }

    /*public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }*/

    public String getQuantity() {

        if(!transaction.getExpense().getUnit().isRealNumber()){
            String quantity = String.valueOf(transaction.getNumber());
            int dot = quantity.indexOf(".");
            quantity = quantity.substring(0,dot);
            return quantity;
        }
        return String.valueOf(transaction.getNumber());
    }

    /*public void setQuantity(String quantity) {
        this.quantity = quantity;
    }*/

}
