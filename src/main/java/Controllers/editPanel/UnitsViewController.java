package Controllers.editPanel;
import java.util.HashSet;
import java.util.concurrent.Callable;

import Controllers.Controller;
import Controllers.widgets.SearchBox;
import Models.Unit;
import Dao.UnitDao;
import Utils.Blocker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static Utils.Blocker.blockButton;

public class UnitsViewController extends Controller {

    private SearchBox<Unit> searchBox = new SearchBox<>();

    private Unit selected = null;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField shortcutTextField;

    @FXML
    private CheckBox realNumberCheckBox;
    @FXML
    private HBox hBox;
    @FXML
    private Button updateButton;
    @FXML
    private VBox editPanel;

    @FXML
    void update(ActionEvent event) {
        String newName = nameTextField.getText();
        String newShortcut = shortcutTextField.getText();
        boolean ralNumber = realNumberCheckBox.isSelected();
        HashSet<String> forbiddenNames = modelStructure.getNamesAndShortcuts();
        boolean changed = false;
        if(!selected.getName().equals(newName)){
            forbiddenNames.remove(selected.getName());
            forbiddenNames.add(newName);
            selected.setName(newName);
            changed=true;
        }
        if(!newShortcut.equals("")){
            String shortcut = selected.getShortcut();
            if(shortcut==null) shortcut="";
            if(!shortcut.equals(newShortcut)){
                forbiddenNames.remove(selected.getShortcut());
                forbiddenNames.add(newShortcut);
                selected.setShortcut(newShortcut);
                changed=true;
            }
        }else{

            selected.setShortcut(null);
            changed=true;
        }
        if(!selected.isRealNumber() == ralNumber){
            selected.setRealNumber(ralNumber);
            changed=true;
        }
        if(changed){
            UnitDao unitDao = daoContainer.getUnitDao();
            unitDao.update(selected);
            searchBox.refresh();
        }
    }

    @FXML
    void initialize() {

        searchBox.addData(modelStructure.getUnits());
        hBox.getChildren().add(0,searchBox);
        //reaction on select model from searchBox
        searchBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                editPanel.setDisable(false);
                updateButton.setDisable(false);
                selected = newSelection;
                nameTextField.setText(newSelection.getName());
                String shortcut = newSelection.getShortcut();
                if(shortcut!=null) shortcutTextField.setText(shortcut);
                else shortcutTextField.clear();
                if (newSelection.isRealNumber()) realNumberCheckBox.setSelected(true);
                else realNumberCheckBox.setSelected(false);
            } else {
                editPanel.setDisable(true);
                //updateButton.setDisable(true);
            }
        });
        //block button when forbidden names
        Blocker.blockButton(updateButton, nameTextField, modelStructure.getNamesAndShortcuts(), new Callable<String>() {
            @Override
            public String call() throws Exception {
                return selected.getName();
            }
        });
        Blocker.blockButton(updateButton, shortcutTextField, modelStructure.getNamesAndShortcuts(), new Callable<String>() {
            @Override
            public String call() throws Exception {
                return selected.getShortcut();
            }
        });
    }
}
