package Controllers.widgets.inputPanels;

import Controllers.widgets.SelectBox;
import Models.Tag;
import Models.Unit;
import Utils.Blocker;
import Utils.ModelStructure;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TagPanel extends VBox{
    private SelectBox<Tag> selectBox = new SelectBox<Tag>();
    private ModelStructure modelStructure;
    public TagPanel(ModelStructure modelStructure) {
        getStyleClass().add("input-panel");
        VBox.setVgrow(selectBox, Priority.ALWAYS);
        this.setSpacing(10);
        this.modelStructure = modelStructure;
        selectBox.addData(modelStructure.getTags());
        this.getChildren().add(selectBox);
        System.out.println("testtest");
    }


}
