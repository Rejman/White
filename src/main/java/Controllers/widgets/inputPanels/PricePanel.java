package Controllers.widgets.inputPanels;

import Controllers.widgets.FloatField;
import Controllers.widgets.NumberField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class PricePanel extends VBox implements Called{
    private GridPane gridPane = new GridPane();
    private FloatField unitPrice = new FloatField(2, 2);
    private FloatField quantityFloat = new FloatField(3, null);
    private NumberField quantityInt = new NumberField(true, null, null, "0");
    private Label unitName = new Label();
    private Button button = new Button("Add to list");
    public static final String iconURL = "icons/paper-money-24.png";
    private boolean integer;

    public PricePanel(String curencyCode) {
        getStyleClass().add("input-panel");
        getStyleClass().add("price-panel");
        //style
        gridPane.setPadding(new Insets(10,0,10,0));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);
        //row 0
        gridPane.add(unitPrice, 0, 0);
        gridPane.add(new Label(curencyCode),1,0);
        //row 1
        gridPane.add(quantityInt, 0, 1);
        gridPane.add(quantityFloat, 0, 1);
        gridPane.add(unitName, 1, 1);
        //row 2
        gridPane.add(button,0,2);
        Label header = new Label("Enter quantity & price:");
        header.setGraphic(new ImageView(new Image(iconURL)));
        this.getChildren().add(header);
        this.getChildren().add(gridPane);
        this.getChildren().add(button);

        //this.getChildren().add(gridPane);
        setQuantityInt();
        clear();

        disable();
    }
    public void enable(){
        Panel.actualVisiblePanel = this;
        this.setVisible(true);
        this.setDisable(false);
        unitPrice.requestFocus();
    }
    public void disable(){
        this.setVisible(false);
        this.setDisable(true);
    }
    public void setUnitName(String name){
        unitName.setText(name);
    }
    public void clear(){
        unitPrice.clear();
        quantityFloat.clear();
        quantityInt.clear();
    }
    public void setQuantityFlaot(){
        integer = false;
        quantityFloat.setVisible(true);
        quantityFloat.setDisable(false);
        quantityInt.setDisable(true);
        quantityInt.setVisible(false);
    }
    public void setQuantityInt(){
        integer = true;
        quantityFloat.setVisible(false);
        quantityFloat.setDisable(true);
        quantityInt.setDisable(false);
        quantityInt.setVisible(true);
    }
    public void setQuantityFieldType(boolean realNumber) {
        if (realNumber) {
            setQuantityFlaot();
        } else {
            setQuantityInt();
        }
    }
    public String getPrice(){
        return unitPrice.getText();
    }
    public String getQuantity(){
        if(integer){
            return quantityInt.getText();
        }else{
            return quantityFloat.getText();
        }
    }
    public Button getButton() {
        return button;
    }
    @Override
    public void reset() {
        quantityFloat.clear();
        quantityInt.clear();
        unitPrice.clear();
    }
}
