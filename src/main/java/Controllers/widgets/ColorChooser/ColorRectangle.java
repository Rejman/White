package Controllers.widgets.ColorChooser;


import Utils.ColorConvertor;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class ColorRectangle extends Rectangle {
    private static EditPane editPane = null;

    public static EditPane getEditPane() {
        return editPane;
    }

    public static void setTarget(EditPane editPane) {
        ColorRectangle.editPane = editPane;
    }

    public static int size = 20;
    private String color = "#FFFFFF";

    public String getColor() {
        return color;
    }

    public ColorRectangle(String color) {
        super(size,size);
        this.color = color;
        setFill(Color.web(color));
        setCursor(Cursor.HAND);
        setOnMouseClicked(event -> {
            if(editPane!=null){

                Color temp = Color.web(getColor());
                int red = (int) (temp.getRed()*255);
                int green = (int) (temp.getGreen()*255);
                int blue = (int) (temp.getBlue()*255);
                editPane.setColor(red,green,blue);
            }
        });
    }
}
