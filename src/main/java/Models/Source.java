package Models;


public class Source extends Model implements Searched, Colored{
    private String name;
    private String description;
    private String color;

    public Source(String name, String description, String color) {
        super(-1);
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public Source(long id, String name, String description, String color) {
        super(id);
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Source source = (Source) o;
        return this.id == source.id;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
