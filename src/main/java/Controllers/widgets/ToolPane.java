package Controllers.widgets;

import javafx.beans.property.ObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class ToolPane extends HBox {
    private double x;
    private double y;
    double maxWidth;
    double maxHeight;
    public void resetPosition(){

        setLayoutX((getScene().getWidth()-getWidth())/2);
        setLayoutY((getScene().getHeight()-getHeight())/2);
    }
    public ToolPane() {
        setOnMousePressed(event -> {
            System.out.println("click");
            x = event.getX();
            y = event.getY();
            maxWidth = getScene().getWidth()-getWidth();
            maxHeight = getScene().getHeight()-getHeight();
        });
        setOnMouseDragged(event -> {
            /*double maxWidth = getScene().getWidth();
            double maxHeight = getScene().getHeight();*/

            double sceneX = event.getSceneX();
            double sceneY = event.getSceneY();
            double newX = sceneX-x;
            double newY = sceneY-y;

            if(newX<0) newX = 0;
            if(newY<0) newY = 0;

            if(newX>=maxWidth) newX = maxWidth;
            if(newY>=maxHeight) newY = maxHeight;

            setLayoutX(newX);
            setLayoutY(newY);
        });
        /*setOnMouseDragged(event -> {
            double x = event.getX();
            double y = event.getY();
            System.out.println(x+" x "+y);
            *//*setLayoutX(x);
            setLayoutY(y);*//*
            //area.getChildren().se
        });*/
    }



}
