package Controllers.widgets.ColorChooser;

import Controllers.widgets.ColorField;
import Utils.ColorConvertor;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class EditPane extends StackPane {
    private Label selected = new Label();

    public Label getSelected() {
        return selected;
    }

    private ColorField redValue = new ColorField();
    private ColorField greenValue = new ColorField();
    private ColorField blueValue = new ColorField();
    private String getColor(){
        int red = Integer.parseInt(redValue.getValue());
        int green = Integer.parseInt(greenValue.getValue());
        int blue = Integer.parseInt(blueValue.getValue());

        return ColorConvertor.toHEXString(red,green,blue);
    }
    public void setColor(Color color){

        int red = (int) (color.getRed()*255);
        int green = (int) (color.getGreen()*255);
        int blue = (int) (color.getBlue()*255);
        redValue.setValue(""+red);
        greenValue.setValue(""+green);
        blueValue.setValue(""+blue);
    }
    public EditPane() {
        GridPane gridPane = new GridPane();
        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setHalignment(HPos.RIGHT);
        gridPane.getColumnConstraints().add(labelColumn);
        for(int i=1;i<2;i++){
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.ALWAYS);
            columnConstraints.setHalignment(HPos.CENTER);
            gridPane.getColumnConstraints().add(columnConstraints);
        }
        gridPane.add(new Label("Red"),0,0);
        gridPane.add(redValue,1,0);

        redValue.valueProperty().addListener((observable, oldValue, newValue) -> {

            selected.setText(getColor());

        });
        greenValue.valueProperty().addListener((observable, oldValue, newValue) -> {

            selected.setText(getColor());

        });
        blueValue.valueProperty().addListener((observable, oldValue, newValue) -> {

            selected.setText(getColor());

        });

        gridPane.add(new Label("Green"),0,1);
        gridPane.add(greenValue,1,1);

        gridPane.add(new Label("Blue"),0,2);
        gridPane.add(blueValue,1,2);
        gridPane.setHgap(5);
        gridPane.setHgap(5);
        //gridPane.setGridLinesVisible(true);
        //gridPane.setMaxWidth(0);
        gridPane.setPadding(new Insets(5));
        getChildren().add(gridPane);
    }
}
