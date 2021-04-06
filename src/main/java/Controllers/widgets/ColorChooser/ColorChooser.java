package Controllers.widgets.ColorChooser;
import Utils.ColorConvertor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class ColorChooser extends VBox {

    private static final String grey = "#7F7F7F";
    private static final String[] basicColors = {
            "#FF0000",
            "#FF7F00",
            "#FFFF00",
            "#7FFF00",
            "#00FF00",
            "#00FF7F",
            "#00FFFF",
            "#007FFF",
            "#0000FF",
            "#7F00FF",
            "#FF00FF",
            "#FF007F"
    };

    private Label selected = new Label();
    private GridPane buildCustomColorsPane(int width, ArrayList<Color> colors){

        GridPane gridPane = new GridPane();
        gridPane.setHgap(1);
        gridPane.setVgap(1);
        int column = 0;
        int row = 0;

        for(int i=0;i<colors.size();i++){
            Rectangle rect = new ColorRectangle(ColorConvertor.toHEXString(colors.get(i)));
            gridPane.add(rect,column,row);
            column++;
            if(column%width==0){
                row++;
                column = 0;
            }
        }
        return gridPane;
    }
    private GridPane buildBasicColorsPane(int levels){
        levels++;
        GridPane gridPane = new GridPane();
        int colorNumber = basicColors.length;
        gridPane.setHgap(1);
        gridPane.setVgap(1);
        int column = 0;
        int row = 0;

        double alfaStep = (1/(double)levels);
        double alfa = alfaStep;
        System.out.println("test"+alfaStep);

        for(int i=0;i<levels*colorNumber;i++){
            Rectangle rect = new ColorRectangle(ColorConvertor.toHEXString(basicColors[column],alfa,Color.BLACK));
            gridPane.add(rect,column,row);
            column++;
            if(column%colorNumber==0){
                row++;
                column = 0;
                alfa+=alfaStep;
            }
            if(alfa>1) alfa = 1;
        }

        column=0;
        row--;
        alfa = 1;
        for(int i=0;i<levels*colorNumber;i++){
            Rectangle rect = new ColorRectangle(ColorConvertor.toHEXString(basicColors[column],alfa));
            gridPane.add(rect,column,row);
            column++;
            if(column%colorNumber==0){
                row++;
                column = 0;
                alfa-=alfaStep;
            }
            if(alfa<0) alfa = 0;
        }
        alfa = 1;
        for(int i=0;i<levels-1;i++){
            Rectangle rect = new ColorRectangle(ColorConvertor.toHEXString("#000000",alfa,Color.web(grey)));
            gridPane.add(rect,colorNumber,i);
            alfa-=alfaStep;
        }

        gridPane.add(new ColorRectangle(grey),colorNumber,levels-1);

        alfa = 1;
        for(int i=(levels*2)-2;i>levels-1;i--){
            System.out.println(alfa);
            ColorRectangle rect = new ColorRectangle(ColorConvertor.toHEXString("#FFFFFF",alfa, Color.web(grey)));
            gridPane.add(rect,colorNumber,i);
            alfa-=alfaStep;
        }
        return gridPane;
    }
    public void addCustomColors(ArrayList<Color> colors){
        System.out.println("okey");
        GridPane customColors = buildCustomColorsPane(5,colors);
        getChildren().add(customColors);
    }
    public ColorChooser() {

        //setStyle("-fx-border-color: black; -fx-border-width: 2");
        VBox rightBox = new VBox();
        StackPane box = new StackPane();
        box.setMinWidth(100);
        box.setMinHeight(100);
        box.setStyle("-fx-background-color: black;");
        //selected.setMaxWidth(100);
        box.setMaxHeight(100);
        box.getChildren().add(selected);
        VBox.setVgrow(box,Priority.ALWAYS);
        HBox.setHgrow(rightBox,Priority.ALWAYS);
        rightBox.getChildren().add(box);

        selected.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                box.setStyle("-fx-background-color:"+newValue+";");
                Color contrast = ColorConvertor.getContrastColor(Color.web(newValue));
                selected.setTextFill(contrast);
            }
        });
        ColorRectangle.setTarget(selected);
        GridPane basicColors = buildBasicColorsPane(6);
        getChildren().add(basicColors);
        getChildren().add(rightBox);
    }
}
