package Models;

public class Name extends Model implements Searched, Colored {
    private String name;
    private Category category;

    public Name(String name, Category category) {
        super(-1);
        this.name = name;
        this.category = category;
    }

    public Name(long id, String name, Category category) {
        super(id);
        this.name = name;
        this.category = category;
    }

    @Override
    public String getColor() {
        if (category != null) return category.getColor();
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return id == name.id;
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
