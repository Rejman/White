package Models;

public class Expense extends Model {
    private Name name;
    private Unit unit;

    public Expense(Name name, Unit unit) {
        super(-1);
        this.name = name;
        this.unit = unit;
    }

    public Expense(long id, Name name, Unit unit) {
        super(id);
        this.name = name;
        this.unit = unit;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return id == expense.id;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", name=" + name +
                ", unit=" + unit +
                '}';
    }
}
