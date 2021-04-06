package Controllers.widgets.inputPanels;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import Utils.ColorConvertor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ImageEntry {

    public BooleanProperty isImage = new SimpleBooleanProperty(false);
    public void setImageEntrySize(double width, double height){
        imageImageView.setFitWidth(width);
        imageImageView.setFitHeight(height);
    }
    private Image image;
    @FXML
    private ImageView imageImageView;

    @FXML
    private ImageView imageFileImageView;

    @FXML
    private ImageView fileImage;

    @FXML
    private ImageView pasteImage;

    @FXML
    void initialize() {
        pasteImage.setOpacity(0.5);
        fileImage.setOpacity(0.5);
    }
    @FXML
    void clipboardEnter(MouseEvent event) {
        pasteImage.setOpacity(1);
    }

    @FXML
    void clipboardExit(MouseEvent event) {
        pasteImage.setOpacity(0.5);
    }

    @FXML
    void pasteFromClipboard(MouseEvent event) {
        image = Clipboard.getSystemClipboard().getImage();
        imageImageView.setImage(image);
        isImage.setValue(true);
        //findDominatedColors(10);

    }

    public ArrayList<Color> findDominatedColors(int number){
        ArrayList<Color> colors = new ArrayList<>();
        PixelReader pr = image.getPixelReader();
        Map<Color, Long> colCount = new HashMap<>();

        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                final Color col = pr.getColor(x, y);
                if(colCount.containsKey(col)) {
                    colCount.put(col, colCount.get(col) + 1);
                } else {
                    colCount.put(col, 1L);
                }
            }
        }

        for(int i =0;i<number;i++){
            Color dominant = colCount.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
            colors.add(dominant);
            colCount.remove(dominant);
            System.out.println(ColorConvertor.toHEXString(dominant));
            if(colCount.size()==0) break;
        }
        return colors;

    }
}
