package Controllers.widgets.ColorChooser;

import Controllers.widgets.ColorField;
import Utils.ColorConvertor;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

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
    public void setColor(int red, int green, int blue){
        System.out.println(red+" "+green+" "+blue);
        redValue.setValue(""+red);
        greenValue.setValue(""+green);
        blueValue.setValue(""+blue);
    }
    public EditPane() {
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("R"),0,0);
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

        gridPane.add(new Label("G"),0,1);
        gridPane.add(greenValue,1,1);

        gridPane.add(new Label("B"),0,2);
        gridPane.add(blueValue,1,2);
        gridPane.setMaxWidth(300);
        getChildren().add(gridPane);
    }
}
