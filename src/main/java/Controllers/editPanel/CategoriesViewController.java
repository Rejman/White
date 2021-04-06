package Controllers.editPanel;

import Controllers.Controller;

import Controllers.widgets.SearchBox;
import Models.Category;
import Dao.CategoryDao;

import Utils.Blocker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.concurrent.Callable;

public class CategoriesViewController extends Controller {

    private SearchBox<Category> searchBox = new SearchBox<>();

    private Category selected = null;

    @FXML
    private HBox hBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Button updateButton;
    @FXML
    private VBox editPanel;

    @FXML
    void update(ActionEvent event) {
        String newName = nameTextField.getText();
        String newColor = colorPicker.getValue().toString();
        HashSet<String> forbiddenNames = modelStructure.getCategoryNames();
        boolean changed = false;
        if (!selected.getName().equals(newName)) {
            forbiddenNames.remove(selected.getName());
            forbiddenNames.add(newName);
            selected.setName(newName);
            changed = true;
        }
        if (!selected.getColor().equals(newColor)) {
            selected.setColor(newColor);
            changed = true;
        }
        if (changed) {
            CategoryDao categoryDao = daoContainer.getCategoryDao();
            categoryDao.update(selected);
            searchBox.refresh();
        }
    }

    @FXML
    void initialize() {

        searchBox.addData(modelStructure.getCategories());
        hBox.getChildren().add(0,searchBox);
        //reaction on select model from searchBox
        searchBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                editPanel.setDisable(false);
                updateButton.setDisable(false);
                selected = newSelection;
                nameTextField.setText(newSelection.getName());
                colorPicker.setValue(Color.web(newSelection.getColor()));
            } else {
                editPanel.setDisable(true);
                //updateButton.setDisable(true);
                //this.selected = null;
            }
        });
        //block button when forbidden name
        Blocker.blockButton(updateButton, nameTextField, modelStructure.getCategoryNames(), new Callable<String>() {
            @Override
            public String call() throws Exception {
                return selected.getName();
            }
        });
    }

}
