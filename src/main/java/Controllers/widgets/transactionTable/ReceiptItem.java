package Controllers.widgets.transactionTable;

import Controllers.Controller;
import Models.Category;
import Models.Name;
import Models.Tag;
import Models.Unit;
import Utils.ColorConvertor;
import Utils.UnitConverter;

import java.math.BigDecimal;
import java.util.HashSet;

public class ReceiptItem extends TransactionItem{
    private static HashSet<Category> newCategories = new HashSet<>();
    private static HashSet<Name> newNames = new HashSet<>();
    private static HashSet<Unit> newUnits = new HashSet<>();
    private static HashSet<Tag> newTags = new HashSet<>();
    private Name name;
    private Unit unit;
    private String unitPrice;
    private String quantity;
    private String shortcut;
    private String total;
    private Tag tag;

    public ReceiptItem(Name name, Unit unit, String unitPrice, String quantity, Tag tag) {
        BigDecimal unitPriceBD = new BigDecimal(unitPrice);
        BigDecimal quantityBD = new BigDecimal(quantity);

        String total = UnitConverter.toPrice(unitPriceBD.multiply(quantityBD));
        if (name.getId() < 0) {
            newNames.add(name);
            Category potentialNewCategory = name.getCategory();
            if (potentialNewCategory.getId() < 0) {
                newCategories.add(potentialNewCategory);
            }
        }
        if (unit.getId() < 0) {
            newUnits.add(unit);
        }

        this.color = ColorConvertor.toHEXString(name.getCategory().getColor());
        this.lightColor = ColorConvertor.toHEXString(this.color, Controller.settings.colorIntense);

        this.name = name;
        this.unit = unit;
        this.shortcut = unit.getShortcut();
        if (this.shortcut == null) this.shortcut = unit.getName();
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.total = total;
        this.tag = tag;
    }

    public static HashSet<Category> getNewCategories() {
        return newCategories;
    }

    public static HashSet<Name> getNewNames() {
        return newNames;
    }

    public static HashSet<Unit> getNewUnits() {
        return newUnits;
    }

    public static HashSet<Tag> getNewTags() {
        return newTags;
    }

    public static void clearNews() {
        newCategories.clear();
        newNames.clear();
        newUnits.clear();
        newTags.clear();
    }

    public Tag getTag() {
        return this.tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getShortcut() {
        /*Unit unit = transaction.getExpense().getUnit();
        String shortuct = unit.getShortcut();
        if(shortuct==null) return unit.getName();*/
        return this.shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public Name getName() {
        return this.name;
    }

    public void setName(Name name) {
        this.name=name;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit=unit;
    }

    public String getUnitPrice() {
        return this.unitPrice;
    }

    /*public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }*/

    public String getQuantity() {

        return this.quantity;
    }
    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    /*public void setQuantity(String quantity) {
        this.quantity = quantity;
    }*/

}
