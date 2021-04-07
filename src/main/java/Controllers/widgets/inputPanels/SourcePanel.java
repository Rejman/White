package Controllers.widgets.inputPanels;

import Controllers.Controller;
import Controllers.widgets.ColorChooser.ColorChooser;
import Models.Source;
import Utils.ModelStructure;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;

public class SourcePanel extends Panel<Source> {
    public static final String iconURL = "icons/shop-24.png";
    private StackPane containerForLogo;
    private VBox newSourcePanel = new VBox();
    private ColorPicker colorPicker = new ColorPicker();
    private TextArea textArea = new TextArea();
    private Button saveSourceButton = new Button("Save source");
    private Label sourceNameLabel = new Label();
    private Source newSource = null;
    private FlowPane flowPane = new FlowPane();
    BooleanProperty isImage = new SimpleBooleanProperty();
    private ImageEntry imageEntry;
    public Source getNewSource() {
        return newSource;
    }

    public void setNewSource(Source newSource) {
        this.newSource = newSource;
    }
    private ColorChooser colorChooser = new ColorChooser();
    public SourcePanel(ModelStructure modelStructure, StackPane containerForLogo) throws IOException {
        super("Source:",new Image(iconURL), modelStructure);
        this.containerForLogo = containerForLogo;


        selectBox.addData(modelStructure.getSources());

        newSourcePanel.getChildren().add(sourceNameLabel);
        newSourcePanel.getChildren().add(colorChooser);

        newSourcePanel.getChildren().add(textArea);
        newSourcePanel.getChildren().add(saveSourceButton);
        this.getChildren().add(newSourcePanel);
        newSourcePanel.setSpacing(5);

        newSourcePanel.setVisible(false);
        newSourcePanel.setDisable(true);
        this.disable();


        selectBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                Source selectedSource = (Source) selectBox.getSelectedItem();
                if (selectedSource == null) {
                    sourceNameLabel.setText("Set color & description for " + selectBox.getText());
                    sourceNameLabel.setGraphic(new ImageView(new Image(iconURL)));
                    changeVisibility(startView,newSourcePanel);

                    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/widgets/ImageEntry.fxml"));
                    try {

                        containerForLogo.getChildren().add(2,loader.load());
                        imageEntry = loader.getController();
                        imageEntry.setImageEntrySize(Controller.settings.logoWidth, Controller.settings.logoHeight);
                        isImage = imageEntry.isImage;
                        /*isImage.addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                                if(newValue){
                                    ArrayList<Color> colors = imageEntry.findDominatedColors(20);
                                    colorChooser.addCustomColors(colors);
                                }
                            }
                        });*/

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    saveSourceButton.requestFocus();
                } else {
                    externalLabel.setText(selectedSource.getName());
                    showNextPanel();
                }
            }
        }));

        saveSourceButton.setOnAction(event -> {
            containerForLogo.getChildren().remove(2);
            String name = selectBox.getText();
            String description = textArea.getText();
            String color = colorPicker.getValue().toString();

            if (newSource != null) {
                selectBox.getListView().getItems().remove(newSource);
            }
            newSource = new Source(name, description, color);
            selectBox.setSelectedItem(newSource);
            modelStructure.replaceNewSource(newSource);

            selectBox.refresh();
            if (externalLabel != null) {
                externalLabel.setText(name);
               // Color selectColor = colorPicker.getValue();
                //externalLabel.setTextFill(ColorConvertor.getContrastColor(selectColor));

                //externalLabel.setStyle("-fx-background-color:"+ColorConvertor.toHexString(selectColor)+";");

            }
            showNextPanel();
        });



    }

    @Override
    public void reset() {
        super.reset();
        textArea.setText("");
        colorPicker.setValue(Color.BLACK);
        selectBox.addData(modelStructure.getSources());
        changeVisibility(newSourcePanel,startView);
    }
}
