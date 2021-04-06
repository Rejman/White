package Controllers.widgets;

import javafx.scene.control.TextField;

public class NumberField extends TextField {
    private boolean clearZero;
    private Integer maxLength;
    private Integer minLength;
    private String startValue;

    public void hide(){
        this.setDisable(true);
        this.setVisible(false);
    }
    public void show(){
        this.setDisable(false);
        this.setVisible(true);
    }
    public void clear(){
        this.setText(startValue);
    }
    public NumberField(boolean clearZero, Integer maxLength, Integer minLength, String startValue) {
        this.getStyleClass().add("int");
        this.startValue = startValue;
        this.clearZero = clearZero;
        this.maxLength = maxLength;
        this.minLength = minLength;
        this.setText(startValue);

        this.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                this.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if(maxLength!=null && newValue.length()>maxLength){
                this.setText(oldValue);
            }
        }));
        this.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            String text = this.getText();
            if(oldValue){
                if(this.getText().length()==0){
                    this.setText("0");
                }
                if(text.length()>1 && clearZero){
                    while(text.charAt(0)=='0'){
                        text = text.replaceFirst("0","");
                    }
                    this.setText(text);
                }
                if(minLength!=null && text.length()<minLength){
                    while(text.length()!=minLength){
                        text+="0";
                    }
                    this.setText(text);
                }
            }
        }));
    }
}
