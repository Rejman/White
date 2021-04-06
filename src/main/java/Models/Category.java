package Models;

public class Category extends Model implements Searched, Colored {
    private String name;
    private String color;

    public Category(String name, String color) {
        super(-1);
        this.name = name;
        this.color = color;
    }

    public Category(long id, String name, String color) {
        super(id);
        this.name = name;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return name;
    }

}
