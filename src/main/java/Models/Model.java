package Models;


public abstract class Model {
    protected long id;

    public Model(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getColor() {
        return null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return id == model.id;
    }

    /*    @Override
        public int hashCode() {
            return Objects.hash(id);
        }*/
    @Override
    public int hashCode() {
        return 0;
    }
}
