package Controllers.widgets.ColorChooser;


import Utils.ColorConvertor;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class ColorRectangle extends StackPane {
    private EditPane editPane = null;

    public EditPane getEditPane() {
        return editPane;
    }


    public static int size = 10;
    private String color = "#FFFFFF";

    public String getColor() {
        return color;
    }

    public ColorRectangle(String color, EditPane editPane) {
        //super(size,size);

        super();
        this.editPane = editPane;
        this.setMinHeight(size);
        /*this.setMinHeight(size);
        this.setMinWidth(size);*/
        this.color = color;
        //setFill(Color.web(color));
        System.out.println(color);

        setStyle("-fx-background-color: "+color+";");
        setCursor(Cursor.HAND);
        setOnMouseClicked(event -> {
            if(editPane!=null){
                System.out.println("Test");
                Color temp = Color.web(getColor());

                editPane.setColor(temp);
            }
        });
    }
}
