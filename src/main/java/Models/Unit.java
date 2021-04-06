package Models;

public class Unit extends Model implements Searched {
    private String name;
    private String shortcut;
    private boolean realNumber;

    public Unit(String name, String shortcut, boolean realNumber) {
        super(-1);
        this.name = name;
        this.shortcut = shortcut;
        this.realNumber = realNumber;
    }

    public Unit(long id, String name, String shortcut, boolean realNumber) {
        super(id);
        this.name = name;
        this.shortcut = shortcut;
        this.realNumber = realNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public boolean isRealNumber() {
        return realNumber;
    }

    public void setRealNumber(boolean realNumber) {
        this.realNumber = realNumber;
    }


/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return id == unit.id;
    }

    @Override
    public int hashCode() {
        return 0;
    }*/

    @Override
    public String toString() {
        return name;
    }
}
