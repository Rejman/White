package Controllers.widgets;

import Controllers.MainController;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Cursor;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ToolPane extends StackPane {

    private double x;
    private double y;
    private double maxX;
    private double maxY;
    private double minWidth;
    private double minHeight;

    private StackPane content = new StackPane();
    private Position resizeType;

    public ToolPane() {

        setPadding(new Insets(1));
        setEffect(buildDropShadow());
        setStyle("-fx-background-color: grey");

        VBox containerWithBar = new VBox();

        ToolBar headerBar = buildHeaderBar();
        containerWithBar.getChildren().add(headerBar);
        containerWithBar.getChildren().add(content);

        getChildren().add(containerWithBar);

        setOnMouseMoved(event -> setCursor(event));
        setOnMouseDragged(event -> onResize(event));

        content.setOnMouseEntered(event -> {
            setCursor(Cursor.DEFAULT);
        });
    }

    private Effect buildDropShadow() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.GRAY);
        dropShadow.setHeight(30);
        dropShadow.setWidth(30);
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        return dropShadow;
    }

    private ToolBar buildHeaderBar() {
        ToolBar headerBar = new ToolBar();
        headerBar.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        Image deleteImage = new Image("icons/delete-24.png");
        ImageView imageView = new ImageView(deleteImage);
        imageView.setOpacity(0.5);

        Pane closeButton = new Pane();
        closeButton.getChildren().add(imageView);

        closeButton.setOnMouseEntered(event -> {
            imageView.setOpacity(1);
        });
        closeButton.setOnMouseExited(event -> {
            imageView.setOpacity(0.5);
        });
        closeButton.setOnMouseClicked(event -> {
            close();
        });

        headerBar.getItems().add(closeButton);

        headerBar.setOnMouseEntered(event -> {
            setCursor(Cursor.DEFAULT);
        });
        headerBar.setOnMousePressed(event -> {

            x = headerBar.getWidth() - event.getX();
            y = event.getY();
            maxX = getScene().getWidth() - getWidth();
            maxY = getScene().getHeight() - headerBar.getHeight();

        });
        headerBar.setOnMouseDragged(event -> {
            double sceneX = event.getSceneX();
            double sceneY = event.getSceneY();
            double newX = sceneX - x;
            double newY = sceneY - y;
            setLocation(newX, newY);
        });
        return headerBar;
    }

    public void onAutoLocation() {
        getScene().widthProperty().addListener((observable, oldValue, newValue) -> {
            setLayoutX((newValue.doubleValue() - getWidth()) / 2);
        });

        getScene().heightProperty().addListener((observable, oldValue, newValue) -> {
            setLayoutY((newValue.doubleValue() - getHeight()) / 2);
        });

    }

    private void setLocation(double x, double y) {

        if (x < 0) x = 0;
        if (y < 0) y = 0;

        if (x >= maxX) x = maxX;
        if (y >= maxY) y = maxY;

        setLayoutX(x);
        setLayoutY(y);
    }

    private void onResize(javafx.scene.input.MouseEvent event) {
        System.out.println(resizeType + " - " + event.getX() + " x " + event.getY());
        double height = event.getY();
        double width = event.getX();
        switch (resizeType) {
            case S:

                if (height > minHeight) {
                    setMinHeight(height);
                    setMaxHeight(height);
                }

                break;
            case E:

                if (width > minWidth) {
                    setMinWidth(width);
                    setMaxWidth(width);
                }
                break;
            case SE:
                if (height > minHeight) {
                    setMinHeight(height);
                    setMaxHeight(height);
                }
                if (width > minWidth) {
                    setMinWidth(width);
                    setMaxWidth(width);
                }
                break;

        }
    }

    private void setCursor(javafx.scene.input.MouseEvent event) {
        int hMargin = 3;
        int vMargin = 3;
        double width = getWidth();
        double height = getHeight();
        x = event.getX();
        y = event.getY();
        boolean west = false;
        boolean north = false;
        boolean east = false;
        boolean south = false;

        if (x < hMargin) west = true;
        else if (x > width - hMargin) east = true;
        if (y < vMargin) north = true;
        else if (y > height - vMargin) south = true;
        else {
            south = false;
            north = false;
        }

        if (north) {
            if (east) resizeType = Position.NE;
            else if (west) resizeType = Position.NW;
            else resizeType = Position.N;
        } else if (south) {
            if (east) resizeType = Position.SE;
            else if (west) resizeType = Position.SW;
            else resizeType = Position.S;
        } else if (east) resizeType = Position.E;
        else if (west) resizeType = Position.W;
        else resizeType = Position.NONE;


        switch (resizeType) {
            case W:
            case E:
                setCursor(Cursor.H_RESIZE);
                break;
            case N:
            case S:
                setCursor(Cursor.V_RESIZE);
                break;
            case NE:
            case SW:
                setCursor(Cursor.NE_RESIZE);
                break;
            case NW:
            case SE:
                setCursor(Cursor.NW_RESIZE);
                break;
            default:
                setCursor(Cursor.DEFAULT);

        }
    }
    public void show(double x, double y){
        MainController.getBlockingArea().getChildren().add(this);
        setLayoutY(y);
        setLayoutX(x);

        onAutoLocation();

        MainController.getBlockingArea().toFront();
    }
    public void close() {
        MainController.getBlockingArea().getChildren().clear();
        MainController.getBlockingArea().toBack();
    }

    public void setContent(Region node) {
        content.getChildren().clear();
        content.getChildren().add(node);
    }

    enum Position {
        N,
        S,
        W,
        E,
        NW,
        NE,
        SE,
        SW,
        NONE
    }


}
