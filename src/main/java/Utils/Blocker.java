package Utils;

import Controllers.Controller;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class Blocker {

    private static ProgressBar progressBar = null;

    private static List<Region> elementsToBlock = null;

    public static ProgressBar getProgressBar() {
        return progressBar;
    }

    public static void setProgressBar(ProgressBar progressBar) {
        Blocker.progressBar = progressBar;
    }

    public static List<Region> getElementsToBlock() {
        return elementsToBlock;
    }

    public static void setElementsToBlock(List<Region> elementsToBlock) {
        Blocker.elementsToBlock = elementsToBlock;
    }

    public static void block(){
        for (Region elem:elementsToBlock
        ) {
            elem.setDisable(true);
        }
    }
    public static void unblock(){
        for (Region elem:elementsToBlock
        ) {
            elem.setDisable(false);
        }
    }

    public static void bind(ReadOnlyDoubleProperty progressProperty){
        block();
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(progressProperty);
        progressBar.setVisible(true);
    }
    public static void error(){
        progressBar.setStyle("-fx-accent: red");
    }
    public static void unbind(){
        progressBar.setVisible(false);
        unblock();
    }

    public static void blockButton(Button button, TextField textField, HashSet<String> forbiddenWords, Callable<String> func) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!forbiddenWords.contains(newValue) || newValue.equals(func.call())) {
                    button.setDisable(false);
                } else {
                    button.setDisable(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
