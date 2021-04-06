package Models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Transaction extends Model {
    private long date;
    private Source source;
    private Expense expense;
    private BigDecimal number;
    private BigDecimal unit_price;
    private Tag tag;

    public Transaction(long date, Source source, Expense expense, float number, float unit_price, Tag tag) {
        super(-1);
        this.date = date;
        this.source = source;
        this.expense = expense;
        this.number = new BigDecimal(number);
        this.unit_price = new BigDecimal(unit_price);
        this.tag = tag;
    }

    public Transaction(long id, long date, Source source, Expense expense, float number, float unit_price, Tag tag) {
        super(id);
        this.date = date;
        this.source = source;
        this.expense = expense;
        this.number = new BigDecimal(number).setScale(2, RoundingMode.HALF_UP);
        this.unit_price = new BigDecimal(unit_price).setScale(2, RoundingMode.HALF_UP);
        //if(tag==null) tag = "";
        this.tag = tag;
    }

    public BigDecimal getTotal() {
        BigDecimal newTotal = unit_price.multiply(number);
        System.out.println(unit_price + "*" + number + "=" + newTotal);

        return newTotal;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(BigDecimal unit_price) {
        this.unit_price = unit_price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Transaction that = (Transaction) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
