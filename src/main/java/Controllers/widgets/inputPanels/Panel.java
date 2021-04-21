package Controllers.widgets.inputPanels;

import Controllers.widgets.SelectBox;
import Controllers.widgets.ToolPane;
import Models.Model;
import Utils.ModelStructure;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Panel<T extends Model> extends StackPane implements Called{

    public static StringProperty color = new SimpleStringProperty();
    public static Called actualVisiblePanel = null;

    protected SelectBox<T> selectBox = new SelectBox<>();
    protected ModelStructure modelStructure;
    protected VBox startView = new VBox();
    protected Label externalLabel = null;
    protected Called nextPanel = null;
    protected Label header = new Label();
    protected ImageView imageView = new ImageView();

    public SelectBox<T> getSelectBox() {
        return selectBox;
    }

    public T getSelected(){
        return (T) selectBox.getSelectedItem();
    }

    public void setNextPanel(Called nextPanel) {
        this.nextPanel = nextPanel;
    }

    public void setExternalLabel(Label externalLabel) {
        this.externalLabel = externalLabel;
    }

    public Panel(String headerText, Image hederImage, ModelStructure modelStructure) {
        getStyleClass().add("input-panel");
        //header.getStyleClass().add("input-panel");

        VBox.setVgrow(selectBox,Priority.ALWAYS);
        startView.setSpacing(10);
        this.modelStructure = modelStructure;
        if(headerText!=null){
            imageView.setImage(hederImage);
            this.header.setGraphic(imageView);
        }
        this.header.setText(headerText);

        startView.getChildren().add(this.header);
        startView.getChildren().add(selectBox);
        this.getChildren().add(startView);
        disable();
    }

    public void enable(){
        actualVisiblePanel = this;
        this.setVisible(true);
        this.setDisable(false);
        selectBox.getSearchTextField().requestFocus();
    }

    public void disable(){
        this.setVisible(false);
        this.setDisable(true);
    }
    public void reset(){
        selectBox.unselect();
        selectBox.getSearchTextField().clear();
    }
    protected void setTextInExternalLabel(String text){
        if(externalLabel!=null) externalLabel.setText(text);
    }
    protected void changeVisibility(Pane oldView, Pane newView){
        oldView.setDisable(true);
        oldView.setVisible(false);
        newView.setDisable(false);
        newView.setVisible(true);
    }
    protected void showNextPanel(){
        actualVisiblePanel.disable();
        if(nextPanel!=null){
            nextPanel.enable();
        }

    }
}
