package Controllers.editPanel;

import java.net.URL;
import java.util.ResourceBundle;

import Controllers.Controller;
import Utils.Serialize;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class LogoController extends Controller {


    @FXML
    private StackPane logoArea;

    @FXML
    private Rectangle logoRectangle;

    @FXML
    private Label logoSizeLabel;

    @FXML
    void save(ActionEvent event) {
        settings.logoHeight = (int) logoRectangle.getHeight();
        settings.logoWidth = (int) logoRectangle.getWidth();
        Serialize.saveSettings(settings);
    }

    @FXML
    void setLogoSize(MouseEvent event) {
        double ratio = 0.1;
        int width = countLength((int) logoArea.getWidth(),event.getX(),200);
        int min = (int)(width*ratio);
        if(min<100) min = 50;
        int height = countLength((int) logoArea.getHeight(),event.getY(), min);
        logoTextSize = (int) (width*ratio);

        logoSizeLabel.setMaxHeight(height);
        logoRectangle.setWidth(width);
        logoRectangle.setHeight(height);
        logoSizeLabel.setText(width+"x"+height);
        logoSizeLabel.setStyle("-fx-font-size: "+logoTextSize);
    }
    private int logoTextSize = 0;
    @FXML
    void initialize() {
        logoArea.setMaxWidth(800);
        logoArea.setMinWidth(800);
        logoArea.setMaxHeight(500);
        logoArea.setMinHeight(500);
        logoRectangle.setHeight(settings.logoHeight);
        logoRectangle.setWidth(settings.logoWidth);
        logoTextSize = settings.logoTextSize;
        logoSizeLabel.setText(settings.logoWidth+"x"+ settings.logoHeight);
        logoSizeLabel.setStyle("-fx-font-size: "+logoTextSize);
    }
    private int countLength(int length, double point, int min){
        int value = (int) Math.abs(((length/2)-point)*2);
        if(value<min){
            return min;
        }else if(value>length){
            return length;
        }
        return value;
    }
}
