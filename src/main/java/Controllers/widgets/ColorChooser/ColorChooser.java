package Controllers.widgets.ColorChooser;

import Controllers.widgets.ColorField;
import Controllers.widgets.NumberField;
import Utils.ColorConvertor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Stack;

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
    public Tab buildPaletteTab(){
        Tab palette = new Tab();
        ImageView icon = new ImageView(new Image("icons/paint-palette-24.png"));
        icon.setFitHeight(24);
        icon.setFitWidth(24);
        palette.setGraphic(icon);

        /*StackPane stackPane = new StackPane();

        stackPane.getChildren().add(buildBasicColorsPane(5));*/
        palette.setContent(buildBasicColorsPane(4));
        return palette;
    }
    private ColorField redValue = new ColorField();
    private ColorField greenValue = new ColorField();
    private ColorField blueValue = new ColorField();
    public String pullColor(){
        int red = Integer.parseInt(redValue.getValue());
        int green = Integer.parseInt(greenValue.getValue());
        int blue = Integer.parseInt(blueValue.getValue());

        return ColorConvertor.toHEXString(red,green,blue);
    }
    private EditPane editPane;
    public Tab buildEditTab(){
        Tab edit = new Tab();

        ImageView icon = new ImageView(new Image("icons/tune-48.png"));
        icon.setFitHeight(24);
        icon.setFitWidth(24);
        edit.setGraphic(icon);


        editPane = new EditPane();
        selected = editPane.getSelected();
        /*ColorRectangle.setTarget(editPane);*/
        editPane.setMinHeight(300);
        edit.setContent(editPane);
        return edit;
    }
    private TabPane tabPane = new TabPane();
    public ColorChooser() {

        setStyle("-fx-border-color: black; -fx-border-width: 2");

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setSide(Side.BOTTOM);

        tabPane.getTabs().add(buildEditTab());
        tabPane.getTabs().add(buildPaletteTab());


        StackPane box = new StackPane();
        box.setStyle("-fx-background-color: black;");
        box.getChildren().add(selected);
        this.getChildren().add(box);
        this.getChildren().add(tabPane);
        this.autosize();

        selected.textProperty().addListener((observable, oldValue, newValue) -> {
            box.setStyle("-fx-background-color:" + newValue + ";");

            Color contrast = ColorConvertor.getContrastColor(Color.web(newValue));
            selected.setTextFill(contrast);
        });


    }

    private GridPane buildCustomColorsPane(int width, ArrayList<Color> colors) {

        GridPane gridPane = new GridPane();
        gridPane.setHgap(1);
        gridPane.setVgap(1);
        int column = 0;
        int row = 0;

        for (int i = 0; i < colors.size(); i++) {
            StackPane rect = new ColorRectangle(ColorConvertor.toHEXString(colors.get(i)),editPane);
            gridPane.add(rect, column, row);
            column++;
            if (column % width == 0) {
                row++;
                column = 0;
            }
        }
        return gridPane;
    }
    public String getColor(){
        return selected.getText();
    }
    public void setColor(Color color){
        if(editPane!=null){
            editPane.setColor(color);
        }

    }
    private GridPane buildBasicColorsPane(int levels) {
        levels++;
        GridPane gridPane = new GridPane();
        gridPane.setHgap(1);
        gridPane.setVgap(1);
        for(int i=0;i<=basicColors.length;i++){

            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(column);

        }
        for(int i=0;i<=(levels-1)*2;i++){

            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(row);

        }
        int colorNumber = basicColors.length;
/*        gridPane.setHgap(1);
        gridPane.setVgap(1);*/
        int column = 0;
        int row = 0;

        double alfaStep = (1 / (double) levels);
        double alfa = alfaStep;
        System.out.println("test" + alfaStep);

        for (int i = 0; i < levels * colorNumber; i++) {
            StackPane rect = new ColorRectangle(ColorConvertor.toHEXString(basicColors[column], alfa, Color.BLACK),editPane);
            gridPane.add(rect, column, row);
            column++;
            if (column % colorNumber == 0) {
                row++;
                column = 0;
                alfa += alfaStep;
            }
            if (alfa > 1) alfa = 1;
        }

        column = 0;
        row--;
        alfa = 1;
        for (int i = 0; i < levels * colorNumber; i++) {
            StackPane rect = new ColorRectangle(ColorConvertor.toHEXString(basicColors[column], alfa),editPane);
            gridPane.add(rect, column, row);
            column++;
            if (column % colorNumber == 0) {
                row++;
                column = 0;
                alfa -= alfaStep;
            }
            if (alfa < 0) alfa = 0;
        }
        alfa = 1;
        for (int i = 0; i < levels - 1; i++) {
            StackPane rect = new ColorRectangle(ColorConvertor.toHEXString("#000000", alfa, Color.web(grey)),editPane);
            gridPane.add(rect, colorNumber, i);
            alfa -= alfaStep;
        }

        gridPane.add(new ColorRectangle(grey,editPane), colorNumber, levels - 1);

        alfa = 1;
        for (int i = (levels * 2) - 2; i > levels - 1; i--) {
            System.out.println(alfa);
            ColorRectangle rect = new ColorRectangle(ColorConvertor.toHEXString("#FFFFFF", alfa, Color.web(grey)),editPane);
            gridPane.add(rect, colorNumber, i);
            alfa -= alfaStep;
        }
        //gridPane.setMaxWidth(0);
        int height = ((levels*2)+1)*ColorRectangle.size;
        this.setMinHeight(height);

        return gridPane;
    }

    public void addCustomColors(ArrayList<Color> colors) {

        GridPane customColors = buildCustomColorsPane(5, colors);
        getChildren().add(customColors);
    }
}
