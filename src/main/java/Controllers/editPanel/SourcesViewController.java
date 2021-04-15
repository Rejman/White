package Controllers.editPanel;

import Controllers.Controller;
import Controllers.widgets.SearchBox;
import Models.Source;
import Dao.SourceDao;
import Utils.Blocker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.concurrent.Callable;

public class SourcesViewController extends Controller {

    private SearchBox<Source> searchBox = new SearchBox<>();

    private Source selected = null;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button updateButton;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    void update(ActionEvent event) {

        String newName = nameTextField.getText();
        String newColor = colorPicker.getValue().toString();
        String newDesc = descriptionTextArea.getText();
        HashSet<String> forbiddenNames = modelStructure.getSourceNames();
        boolean changed = false;
        if (!selected.getName().equals(newName)) {
            forbiddenNames.remove(selected.getName());
            forbiddenNames.add(newName);
            selected.setName(newName);
            changed = true;
        }
        if (!selected.getDescription().equals(newDesc)) {
            selected.setDescription(newDesc);
            changed = true;
        }
        if (!selected.getColor().equals(newColor)) {
            selected.setColor(newColor);
            changed = true;
        }
        if (changed) {
            SourceDao sourceDao = new SourceDao();
            sourceDao.update(selected);
            searchBox.refresh();
        }

    }

    @FXML
    private HBox hBox;
    @FXML
    private VBox editPanel;
    @FXML
    private StackPane mainStackPane;
    @FXML
    void initialize() {
        VBox.setVgrow(searchBox,Priority.ALWAYS);
        HBox.setHgrow(mainStackPane, Priority.ALWAYS);
        searchBox.addData(modelStructure.getSources());
        hBox.getChildren().add(0,searchBox);
        //reaction on select model from searchBox
        searchBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                editPanel.setDisable(false);
                updateButton.setDisable(false);
                selected = newSelection;
                nameTextField.setText(newSelection.getName());
                descriptionTextArea.setText(newSelection.getDescription());
                colorPicker.setValue(Color.web(newSelection.getColor()));
            } else {
                editPanel.setDisable(true);
                //updateButton.setDisable(true);
            }
        });
        //block button when forbidden name
        Blocker.blockButton(updateButton, nameTextField, modelStructure.getSourceNames(), new Callable<String>() {
            @Override
            public String call() throws Exception {
                return selected.getName();
            }
        });
    }
}
