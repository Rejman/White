package Controllers.widgets.inputPanels;

import Controllers.Controller;
import Controllers.widgets.ColorChooser.ColorChooser;
import Models.Source;
import Utils.ColorConvertor;
import Utils.ModelStructure;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

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
        /*GridPane gridPane = new GridPane();

        *//*gridPane.setStyle("-fx-border-color: black; -fx-border-width: 1");
        gridPane.setMinHeight(100);*//*
        gridPane.setHgap(1);
        gridPane.setVgap(1);

        String[] basicColors = {
                "#FF0000",
                "#FF7F00",
                "#FFFF00",
                "#7FFF00",
                "#00FF00",
                "#00FF7F",
                "#00FFFF",
                "#007FFF",
                "#0000FF",
                "#7F00FF",
                "#FF00FF",
                "#FF007F",
        };
*//*        String[] basicColors = {
                "#FF7F00",
                "#FF0000",
                "#FF007F",
                "#FF00FF",
                "#7F00FF",
                "#0000FF",
                "#007FFF",
                "#00FFFF",
                "#00FF7F",
                "#00FF00",
                "#7FFF00",
                "#FFFF00"

        };*//*
        int row = 0;
        int column = 1;
        double alfa = 1;
        *//*for(int i=0;i<10;i++){
            Rectangle color = new Rectangle();
            color.setWidth(20);
            color.setHeight(20);
            System.out.println("dodaje kolor");

            color.setFill(Color.web(ColorConvertor.toHEXString(Color.BLACK),alfa));
            gridPane.add(color,column,row);
            column++;
            alfa-=0.1;
        }*//*
        row=1;
        column=0;
        alfa = 0.1;
        for (int i=0;i<120;i++){

            Rectangle color = new Rectangle();
            color.setWidth(20);
            color.setHeight(20);
            System.out.println("dodaje kolor");

            color.setFill(Color.web(ColorConvertor.toHEXString(basicColors[column],alfa,Color.BLACK)));
            gridPane.add(color,column,row);
            column++;
            if(column%12==0){
                row++;
                column = 0;
                alfa+=0.1;
            }

        }
        row=10;
        column=0;
        alfa = 1;
        for (int i=0;i<120;i++){

            Rectangle color = new Rectangle();
            color.setWidth(20);
            color.setHeight(20);
            System.out.println("dodaje kolor");

            color.setFill(Color.web(ColorConvertor.toHEXString(basicColors[column],alfa)));
            gridPane.add(color,column,row);
            column++;
            if(column%12==0){
                row++;
                column = 0;
                alfa-=0.1;
            }

        }*/
        //viewsContainer.getChildren().add(new Button("test"));

        //newSourcePanel.getChildren().add(colorPicker);
        flowPane.setPrefWidth(100);
        flowPane.setPrefHeight(100);
        newSourcePanel.getChildren().add(flowPane);
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
                        isImage.addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                                if(newValue){
                                    ArrayList<Color> colors = imageEntry.findDominatedColors(20);
                                    colorChooser.addCustomColors(colors);
                                }
                            }
                        });

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
