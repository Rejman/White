package Controllers.widgets;

import Models.Model;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Cell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class SelectBox<T extends Model> extends SearchBox<T> {
    BooleanProperty selected = new SimpleBooleanProperty(false);
    Model selectedItem = null;

    public boolean isSelected() {
        return selected.get();
    }

    public void unselect() {
        searchTextField.clear();
        selected.setValue(false);
        selectedItem = null;
    }

    @Override
    protected void addHover(Cell<T> cell) {
        cell.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered && ! cell.isEmpty()) {
                //System.out.println("enter");
                //listView.getSelectionModel().clearSelection();
                listView.getSelectionModel().select(cell.getItem());
            } else {
                listView.getSelectionModel().select(-1);
            }
        });
    }

    public String getText(){
        return searchTextField.getText();
    }
    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelectedItem(Model selectedItem) {
        selected.setValue(true);
        this.selectedItem = selectedItem;
    }

    public Model getSelectedItem() {
        return selectedItem;
    }

    public SelectBox() {
        super();
        searchTextField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String name = searchTextField.getText();
                boolean unknown = true;
                for (Model item : listView.getItems()
                ) {
                    if (item.toString().toLowerCase().equals(name.toLowerCase())) {
                        selectedItem = item;
                        selected.set(true);
                        unknown = false;
                        break;
                    } else {
                        unknown = true;
                    }
                }
                if (searchTextField.getText().length() > 0 && unknown) {
                    selectedItem = null;
                    selected.set(true);
                }
            }
        });
        listView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Model item = listView.getSelectionModel().getSelectedItem();
                if (item != null) {
                    selectedItem = item;
                    selected.set(true);
                } else {
                    selectedItem = null;
                    selected.set(false);
                }
            }
        });

        listView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() >= 1) {
                Model item = listView.getSelectionModel().getSelectedItem();
                if (item != null) {
                    selectedItem = item;
                    selected.set(true);
                } else {
                    selectedItem = null;
                    selected.set(false);
                }
            }
        });
    }
}
