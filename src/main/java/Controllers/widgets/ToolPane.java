package Controllers.widgets;

import Controllers.MainController;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ToolPane extends StackPane {
    Position resizeType;
    private double x;
    private double y;
    private double maxWidth;
    private double maxHeight;
    private StackPane mainStackPane = new StackPane();
    private StackPane content = new StackPane();

    public ToolPane() {
        mainStackPane.setPadding(new Insets(1));

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.GRAY);
        dropShadow.setHeight(40);
        dropShadow.setWidth(40);
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        setEffect(dropShadow);
        setStyle("-fx-background-color:grey");

        VBox vBox = new VBox();
        ToolBar bar = new ToolBar();
        bar.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        Image delete = new Image("icons/delete-24.png");
        bar.getItems().add(new ImageView(delete));

        vBox.getChildren().add(bar);
        vBox.getChildren().add(content);
        mainStackPane.getChildren().add(vBox);

        bar.setOnMouseEntered(event -> {
            setCursor(Cursor.DEFAULT);
        });
        bar.setOnMousePressed(event -> {
            System.out.println("click");
            x = event.getX();
            y = event.getY();
            maxWidth = getScene().getWidth() - mainStackPane.getWidth();
            maxHeight = getScene().getHeight() - mainStackPane.getHeight();
        });
        bar.setOnMouseDragged(event -> {
            /*double maxWidth = getScene().getWidth();
            double maxHeight = getScene().getHeight();*/

            double sceneX = event.getSceneX();
            double sceneY = event.getSceneY();
            double newX = sceneX - x;
            double newY = sceneY - y;

            if (newX < 0) newX = 0;
            if (newY < 0) newY = 0;

            if (newX >= maxWidth) newX = maxWidth;
            if (newY >= maxHeight) newY = maxHeight;

            setLayoutX(newX);
            setLayoutY(newY);
        });
        //setOnMouseEntered(event -> setCursor(event));
        mainStackPane.setOnMouseMoved(event -> setCursor(event));
        bar.setOnMouseEntered(event -> {
            setCursor(Cursor.DEFAULT);
        });
        content.setOnMouseEntered(event -> {
            setCursor(Cursor.DEFAULT);
        });
        getChildren().add(mainStackPane);
    }
    private void resize(javafx.scene.input.MouseEvent event) {

       /* switch (resizeType){
            case W:
                setWidth(x);

        }*/
    }
    private void setCursor(javafx.scene.input.MouseEvent event) {
        int hMargin = 3;
        int vMargin = 3;
        double width = mainStackPane.getWidth();
        double height = mainStackPane.getHeight();
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
        else{
            south=false;
            north=false;
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


        switch (resizeType){
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

    public void close() {
        MainController.getBlockingArea().getChildren().clear();
        MainController.getBlockingArea().toBack();
    }

    public void setContent(Node node) {
        content.getChildren().clear();
        content.getChildren().add(node);
    }

    public void resetPosition() {

        System.out.println("reset: " + this.getWidth() + "x" + this.getHeight());
        setLayoutX((getScene().getWidth() / 2 - getWidth() / 2));
        setLayoutY((getScene().getHeight() / 2 - getHeight() / 2));
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
