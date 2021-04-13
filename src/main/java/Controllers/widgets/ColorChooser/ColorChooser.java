package Controllers.widgets.ColorChooser;

import Controllers.widgets.ColorField;
import Utils.ColorConvertor;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

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
    private static final int lvl = 3;
    private static final int tabPaneHeaderHeight = 53;
    private Label selected = new Label();
    private ColorField redValue = new ColorField();
    private ColorField greenValue = new ColorField();
    private ColorField blueValue = new ColorField();
    private EditPane editPane;
    private TabPane tabPane = new TabPane();
    private StackPane box = new StackPane();

    public ColorChooser() {
        //initialize
        setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: white;");



        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setSide(Side.BOTTOM);

        getChildren().add(box);

        autoSize(60);


        getChildren().add(tabPane);
        EditPane editPane = new EditPane();
        tabPane.getTabs().add(buildPaletteTab(editPane));
        tabPane.getTabs().add(buildEditTab(editPane));

        /*getTabs().add(buildEditTab());
        getTabs().add(buildPaletteTab());



        box.setStyle("-fx-background-color: black;");
        box.getChildren().add(selected);
        this.getChildren().add(box);
        this.getChildren().add(tabPane);
        this.autosize();*/

/*        selected.textProperty().addListener((observable, oldValue, newValue) -> {
            box.setStyle("-fx-background-color:" + newValue + ";");

            Color contrast = ColorConvertor.getContrastColor(Color.web(newValue));
            selected.setTextFill(contrast);
        });*/


    }

    private void autoSize(int colorBoxHeight) {
        int width = 13 * (1 + ColorRectangle.size);
        box.setMinHeight(colorBoxHeight);
        box.setMaxHeight(colorBoxHeight);
        int height = (((lvl * 2) + 1) * (1 + ColorRectangle.size) + colorBoxHeight) + tabPaneHeaderHeight;
        this.setMinWidth(width);
        this.setMaxWidth(width);
        this.setMinHeight(height);
        this.setMaxHeight(height);
    }

    private StackPane buildColorBox() {
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(selected);
        stackPane.setStyle("-fx-background-color: red;");
        selected.textProperty().addListener((observable, oldValue, newValue) -> {
            stackPane.setStyle("-fx-background-color:" + newValue + ";");

            Color contrast = ColorConvertor.getContrastColor(Color.web(newValue));
            selected.setTextFill(contrast);
        });
        return stackPane;
    }

    public Tab buildPaletteTab(EditPane editPane) {
        Tab tab = new Tab("Palette");
/*        ImageView icon = new ImageView(new Image("icons/paint-palette-24.png"));
        icon.setFitHeight(24);
        icon.setFitWidth(24);
        tab.setGraphic(icon);*/

        tab.setContent(buildBasicColorsPane(lvl, editPane));
        return tab;
    }

    public String pullColor() {
        int red = Integer.parseInt(redValue.getValue());
        int green = Integer.parseInt(greenValue.getValue());
        int blue = Integer.parseInt(blueValue.getValue());

        return ColorConvertor.toHEXString(red, green, blue);
    }

    public Tab buildEditTab(EditPane editPane) {

        Tab tab = new Tab("Edit");
/*        ImageView icon = new ImageView(new Image("icons/tune-48.png"));
        icon.setFitHeight(24);
        icon.setFitWidth(24);
        tab.setGraphic(icon);*/

        selected = editPane.getSelected();
        box.getChildren().add(selected);
        //box.setStyle("-fx-background-color: red;");
        selected.textProperty().addListener((observable, oldValue, newValue) -> {
            box.setStyle("-fx-background-color:" + newValue + ";");

            Color contrast = ColorConvertor.getContrastColor(Color.web(newValue));
            selected.setTextFill(contrast);
        });

        tab.setContent(editPane);
        return tab;
    }

    private GridPane buildCustomColorsPane(int width, ArrayList<Color> colors) {

        GridPane gridPane = new GridPane();
        gridPane.setHgap(1);
        gridPane.setVgap(1);
        int column = 0;
        int row = 0;

        for (int i = 0; i < colors.size(); i++) {
            StackPane rect = new ColorRectangle(ColorConvertor.toHEXString(colors.get(i)), editPane);
            gridPane.add(rect, column, row);
            column++;
            if (column % width == 0) {
                row++;
                column = 0;
            }
        }
        return gridPane;
    }

    public String getColor() {
        return selected.getText();
    }

    public void setColor(Color color) {
        if (editPane != null) {
            editPane.setColor(color);
        }

    }

    private GridPane buildBasicColorsPane(int levels, EditPane editPane) {

        levels++;
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: white;");
        gridPane.setPadding(new Insets(1,0,0,0));
        gridPane.setHgap(1);
        gridPane.setVgap(1);

        int colorNumber = basicColors.length;

        int column = 0;
        int row = 0;

        double alfaStep = (1 / (double) levels);
        double alfa = alfaStep;
        System.out.println("test" + alfaStep);

        for (int i = 0; i < levels * colorNumber; i++) {
            StackPane rect = new ColorRectangle(ColorConvertor.toHEXString(basicColors[column], alfa, Color.BLACK), editPane);
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
            StackPane rect = new ColorRectangle(ColorConvertor.toHEXString(basicColors[column], alfa), editPane);
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
            StackPane rect = new ColorRectangle(ColorConvertor.toHEXString("#000000", alfa, Color.web(grey)), editPane);
            gridPane.add(rect, colorNumber, i);
            alfa -= alfaStep;
        }

        gridPane.add(new ColorRectangle(grey, editPane), colorNumber, levels - 1);

        alfa = 1;
        for (int i = (levels * 2) - 2; i > levels - 1; i--) {
            System.out.println(alfa);
            ColorRectangle rect = new ColorRectangle(ColorConvertor.toHEXString("#FFFFFF", alfa, Color.web(grey)), editPane);
            gridPane.add(rect, colorNumber, i);
            alfa -= alfaStep;
        }

        return gridPane;
    }

    public void addCustomColors(ArrayList<Color> colors) {

        GridPane customColors = buildCustomColorsPane(5, colors);
        getChildren().add(customColors);
    }
}
