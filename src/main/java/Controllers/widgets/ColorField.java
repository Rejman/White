package Controllers.widgets;

import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

public class ColorField extends HBox {
    private NumberField colorField = new NumberField(false, 3, 1, "0");
    private Slider slider = new Slider();
    private StringProperty value;

    public String getValue() {
        return value.get();
    }
    public void setValue(String value) {
        this.value.setValue(value);
    }

    public StringProperty valueProperty() {
        return value;
    }

    public ColorField() {

        colorField.getStyleClass().add("color");
        this.setAlignment(Pos.CENTER);
        slider.setMax(255);
        value = colorField.textProperty();
        value.addListener((observable, oldValue, newValue) -> {
            slider.setValue(Integer.parseInt(newValue));
            try {
                if(Integer.parseInt(newValue)>255){
                    colorField.setText("255");
                }
            }catch (Exception e){

            }
        });
        /*colorField.textProperty().addListener(((observable, oldValue, newValue) -> {
            slider.setValue(Integer.parseInt(newValue));
            try {
                if(Integer.parseInt(newValue)>255){
                    colorField.setText("255");
                }
            }catch (Exception e){

            }

        }));*/
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {

            value.setValue(""+newValue.intValue());
            try {
                value.setValue(""+(int)newValue);
            }catch (Exception e){

            }

        });
        getChildren().add(slider);
        getChildren().add(colorField);
    }
}
