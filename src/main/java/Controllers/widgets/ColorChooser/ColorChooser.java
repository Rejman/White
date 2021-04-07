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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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

        StackPane stackPane = new StackPane();

        stackPane.getChildren().add(buildBasicColorsPane(3));
        palette.setContent(stackPane);
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
    public Tab buildEditTab(){
        Tab edit = new Tab();

        ImageView icon = new ImageView(new Image("icons/tune-48.png"));
        icon.setFitHeight(24);
        icon.setFitWidth(24);
        edit.setGraphic(icon);

        /*GridPane gridPane = new GridPane();
        gridPane.add(new Label("R"),0,0);
        gridPane.add(redValue,1,0);
        redValue.valueProperty().addListener((observable, oldValue, newValue) -> {

            selected.setText(pullColor());

        });
        greenValue.valueProperty().addListener((observable, oldValue, newValue) -> {

            selected.setText(pullColor());

        });
        blueValue.valueProperty().addListener((observable, oldValue, newValue) -> {

            selected.setText(pullColor());

        });

        gridPane.add(new Label("G"),0,1);
        gridPane.add(greenValue,1,1);

        gridPane.add(new Label("B"),0,2);
        gridPane.add(blueValue,1,2);
        gridPane.setMaxWidth(300);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(gridPane);*/
        EditPane editPane = new EditPane();
        selected = editPane.getSelected();
        ColorRectangle.setTarget(editPane);
        edit.setContent(editPane);
        return edit;
    }
    private TabPane tabPane = new TabPane();
    public ColorChooser() {

        setStyle("-fx-border-color: black; -fx-border-width: 2");

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setSide(Side.BOTTOM);


        tabPane.getTabs().add(buildPaletteTab());
        tabPane.getTabs().add(buildEditTab());

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
            Rectangle rect = new ColorRectangle(ColorConvertor.toHEXString(colors.get(i)));
            gridPane.add(rect, column, row);
            column++;
            if (column % width == 0) {
                row++;
                column = 0;
            }
        }
        return gridPane;
    }

    private GridPane buildBasicColorsPane(int levels) {
        levels++;
        GridPane gridPane = new GridPane();

        int colorNumber = basicColors.length;
        /*gridPane.setHgap(1);
        gridPane.setVgap(1);*/
        int column = 0;
        int row = 0;

        double alfaStep = (1 / (double) levels);
        double alfa = alfaStep;
        System.out.println("test" + alfaStep);

        for (int i = 0; i < levels * colorNumber; i++) {
            Rectangle rect = new ColorRectangle(ColorConvertor.toHEXString(basicColors[column], alfa, Color.BLACK));
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
            Rectangle rect = new ColorRectangle(ColorConvertor.toHEXString(basicColors[column], alfa));
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
            Rectangle rect = new ColorRectangle(ColorConvertor.toHEXString("#000000", alfa, Color.web(grey)));
            gridPane.add(rect, colorNumber, i);
            alfa -= alfaStep;
        }

        gridPane.add(new ColorRectangle(grey), colorNumber, levels - 1);

        alfa = 1;
        for (int i = (levels * 2) - 2; i > levels - 1; i--) {
            System.out.println(alfa);
            ColorRectangle rect = new ColorRectangle(ColorConvertor.toHEXString("#FFFFFF", alfa, Color.web(grey)));
            gridPane.add(rect, colorNumber, i);
            alfa -= alfaStep;
        }
        gridPane.setMaxWidth(0);
        double height = (ColorRectangle.size+1)*3*2;
        //UWAGA UWAGA UWAGA
        System.out.println("TUTAJ: "+height);
        this.setMinHeight(height);
        return gridPane;
    }

    public void addCustomColors(ArrayList<Color> colors) {

        GridPane customColors = buildCustomColorsPane(5, colors);
        getChildren().add(customColors);
    }
}
