package Controllers.editPanel;

import java.net.URL;
import java.util.ResourceBundle;

import Controllers.Controller;
import Utils.ColorConvertor;
import Utils.Serialize;
import com.sun.jndi.ldap.Connection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class ColorsController extends Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox colorBox;

    @FXML
    private Label colorBoldLabel;

    @FXML
    private Label colorLabel;

    @FXML
    private Label percentLabel;

    @FXML
    private Slider colorSlider;
    @FXML
    void save(ActionEvent event) {
        settings.colorIntense = colorSlider.getValue()/100;
        Serialize.saveSettings(settings);
    }
    @FXML
    void initialize() {
        colorSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double value = (double) newValue;
                double alfa = value/100;
                colorBox.setOpacity(alfa);
                percentLabel.setText((int)value+" %");

                if(alfa> ColorConvertor.BORDER){
                    colorLabel.setTextFill(Color.WHITE);
                    colorBoldLabel.setTextFill(Color.WHITE);
                    percentLabel.setTextFill(Color.WHITE);
                }else{
                    colorLabel.setTextFill(Color.BLACK);
                    colorBoldLabel.setTextFill(Color.BLACK);
                    percentLabel.setTextFill(Color.BLACK);
                }
            }
        });
        colorSlider.setValue(settings.colorIntense*100);

    }
}
