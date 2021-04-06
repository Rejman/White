package Controllers.widgets;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.HBox;

public class FloatField extends HBox {
    private NumberField prefix = new NumberField(true, null, null, "0");
    private NumberField sufix;
    private StringProperty textProperty = new SimpleStringProperty();

    @Override
    public void requestFocus() {
        prefix.requestFocus();
    }

    public String getText() {
        return textProperty.get();
    }

    public void clear() {
        prefix.clear();
        sufix.clear();
    }

    public void hide() {
        prefix.setDisable(true);
        sufix.setDisable(true);
        this.setVisible(false);
    }

    public void show() {
        prefix.setDisable(false);
        sufix.setDisable(false);
        this.setVisible(true);
    }

    public StringProperty textProperty() {
        return textProperty;
    }

    public FloatField(Integer maxLength, Integer minLength) {
        sufix = new NumberField(false, maxLength, minLength, "0");
        prefix.getStyleClass().add("prefix");
        sufix.getStyleClass().add("sufix");
        if(maxLength!=null){
            switch (maxLength){
                case 2:
                    prefix.getStyleClass().add("prefix-double");
                    sufix.getStyleClass().add("sufix-double");
                    break;
                case 3:
                    prefix.getStyleClass().add("prefix-triple");
                    sufix.getStyleClass().add("sufix-triple");
                    break;
            }
        }
        sufix.clear();
        prefix.clear();
        this.getChildren().add(prefix);
        this.getChildren().add(sufix);

        setPropertyAutoUpdate(prefix);
        setPropertyAutoUpdate(sufix);
    }

    private void setPropertyAutoUpdate(NumberField field) {
        field.textProperty().addListener(((observable, oldValue, newValue) -> {
            String prefixText = prefix.getText();
            String sufixText = sufix.getText();
            if (prefixText.length() == 0) prefixText = "0";
            if (sufixText.length() == 0) sufixText = "0";
            textProperty.setValue(prefixText + "." + sufixText);
        }));
    }
}
