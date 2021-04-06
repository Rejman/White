package Controllers;

import Dao.DaoContainer;
import Utils.ModelStructure;
import Utils.Settings.Settings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Region;

public abstract class Controller {

    private static final int MENUS_HEIGHT = 53 + 31 + 15;
    public static DaoContainer daoContainer = null;
    public static ModelStructure modelStructure = null;
    public static Settings settings = null;
    protected static DoubleProperty widthProperty = new SimpleDoubleProperty();
    protected static DoubleProperty heightProperty = new SimpleDoubleProperty();

    private static double percentToPixels(double length, int percent) {
        return (percent * length) / 100;
    }

    public static void setAutoSize(Region widget, int widthPercent, int heightPercent) {
        setAutoWidth(widget, widthPercent);
        setAutoHeight(widget, heightPercent);
    }

    public static void setAutoWidth(Region widget, int percent) {
        widget.setPrefWidth(percentToPixels(Controller.widthProperty.get(), percent));
        Controller.widthProperty.addListener((observable, oldValue, newValue) ->
                widget.setPrefWidth(percentToPixels((Double) newValue, percent)));
    }

    public static void setAutoHeight(Region widget, int percent) {
        widget.setPrefHeight(percentToPixels(Controller.heightProperty.get() - MENUS_HEIGHT, percent));
        Controller.heightProperty.addListener((observable, oldValue, newValue) ->
                widget.setPrefHeight(percentToPixels((Double) newValue - MENUS_HEIGHT, percent)));
    }
}
