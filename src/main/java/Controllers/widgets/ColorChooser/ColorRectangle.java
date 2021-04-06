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
    private static Label target = null;

    public static Label getTarget() {
        return target;
    }

    public static void setTarget(Label target) {
        ColorRectangle.target = target;
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
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(target!=null) target.setText(color);
            }
        });
    }
}
