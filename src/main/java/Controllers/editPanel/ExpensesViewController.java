package Controllers.editPanel;

import java.util.HashSet;
import java.util.concurrent.Callable;

import Controllers.Controller;
import Controllers.MainController;
import Controllers.widgets.SearchBox;
import Dao.NameDao;
import Models.Category;
import Models.Name;
import Utils.Blocker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ExpensesViewController extends Controller {

    private SearchBox<Name> searchBox = new SearchBox<>();

    private Name selected = null;

    @FXML
    private HBox hBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private ChoiceBox<Category> categoryChoiceBox;

    @FXML
    private Button updateButton;
    @FXML
    private VBox editPanel;
    @FXML
    void update(ActionEvent event) {

        String newName = nameTextField.getText();
        Category newCategory = categoryChoiceBox.getValue();
        HashSet<String> forbiddenNames = modelStructure.getExpenseNames();
        boolean changed = false;
        if(!selected.getName().equals(newName)){
            forbiddenNames.remove(selected.getName());
            forbiddenNames.add(newName);
            selected.setName(newName);
            changed=true;
        }
        if(!selected.getCategory().equals(newCategory)){
            selected.setCategory(newCategory);
            changed=true;
        }
        if(changed){
            NameDao nameDao = daoContainer.getNameDao();
            nameDao.update(selected);
            searchBox.refresh();
        }
    }

    @FXML
    void initialize() {
        System.out.println("inicjalizuje ExpenseView");
        searchBox.addData(modelStructure.getNames());
        hBox.getChildren().add(0,searchBox);
        //reaction on select model from searchBox
        searchBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                editPanel.setDisable(false);
                updateButton.setDisable(false);
                selected = newSelection;
                nameTextField.setText(newSelection.getName());
                categoryChoiceBox.getSelectionModel().select(newSelection.getCategory());
            } else {
                editPanel.setDisable(true);
               // updateButton.setDisable(true);
            }
        });
        //block button when forbidden name
        Blocker.blockButton(updateButton, nameTextField, modelStructure.getExpenseNames(), new Callable<String>() {
            @Override
            public String call() throws Exception {
                return selected.getName();
            }
        });
        //add categories to choiceBox
        categoryChoiceBox.getItems().setAll(MainController.modelStructure.getCategories());
    }


}
