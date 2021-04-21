package Controllers.widgets;

import Controllers.HistoryController;
import Controllers.widgets.transactionTable.TransactionTable;
import Models.Source;
import Models.Transaction;
import Utils.UnitConverter;
import Utils.ColorConvertor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;

public class HistoryPosition extends HBox {

    public static final double OPACITY = 0.3;


    private HistoryPosition getDummy(){
        return new HistoryPosition(sourceLabel2,totalLabel2, backColor);
    }

    public static HistoryPosition selected = null;
    private Color contrast;
    private String backColor;
    private String lightBackColor;
    private String date;
    private ArrayList<Transaction> transactions;
    private Label sourceLabel = new Label();
    private Label sourceLabel2 = new Label();
    private Label totalLabel = new Label();
    private Label totalLabel2 = new Label();
    private Label[] labels = {sourceLabel,totalLabel,sourceLabel2,totalLabel2};

    private static HistoryController historyController = null;
    public static void setHistoryController(HistoryController historyController) {
        HistoryPosition.historyController = historyController;
    }
    private static VBox targetBox = null;
    public static void setTargetBox(VBox targetBox) {
        HistoryPosition.targetBox = targetBox;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void updateTotal(){
        BigDecimal newTotal = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
        System.out.println("sprawdzane trans:"+transactions);
        for (Transaction transaction:transactions
        ) {
            System.out.println("->"+newTotal);
            newTotal = newTotal.add(transaction.getTotal());
        }
        System.out.println("zaakutalizowano na "+newTotal);

        totalLabel.setText(UnitConverter.toPrice(newTotal) +" zł");
        totalLabel2.setText(UnitConverter.toPrice(newTotal) +" zł");
    }

    public String getDate() {
        return date;
    }

    private Source source;

    private HistoryPosition(Label sourceLabel, Label totalLabel, String color){
        getStyleClass().add("historyPosition");
        setStyle("-fx-background-color:"+color+";");
        HBox sourceHBox = new HBox();
        sourceHBox.getChildren().add(sourceLabel);
        HBox.setHgrow(sourceHBox,Priority.ALWAYS);
        getChildren().add(sourceHBox);
        getChildren().add(totalLabel);

    }
    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }
    public void deleteTransaction(Transaction transaction){
        transactions.remove(transaction);
    }
    public HistoryPosition(ArrayList<Transaction> transactions, String date, BigDecimal total) throws ParseException {

        getStyleClass().add("historyPosition");

        this.date = date;
        this.transactions = transactions;
        this.totalLabel.setText(UnitConverter.toPrice(total) +" zł");
        this.totalLabel2.setText(UnitConverter.toPrice(total) +" zł");

        source = transactions.get(0).getSource();
        sourceLabel.setText(source.getName());
        sourceLabel2.setText(source.getName());

        for (Label label: labels
             ) {
            //label.setPadding(new Insets(5));
        }

        HBox sourceHBox = new HBox();
        sourceHBox.getChildren().add(sourceLabel);
        HBox.setHgrow(sourceHBox,Priority.ALWAYS);

        //set colors
        Color color = Color.web(source.getColor());

        contrast = ColorConvertor.getContrastColor(color);
        backColor = ColorConvertor.toHEXString(color);
        lightBackColor = ColorConvertor.toRGBAString(color,OPACITY);
        setStyle("-fx-background-color:"+lightBackColor+";");

        addHover();

        getChildren().add(sourceHBox);
        getChildren().add(totalLabel);

        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if(selected!=null) selected.setHighlight(false);
            setHighlight(true);
            selected = this;

            System.out.println(transactions);
            TransactionTable tableView = historyController.getTransactionTable();
            tableView.hideTags();
            tableView.getItems().clear();
            for (Transaction transaction: transactions
            ) {
                historyController.addToList(transaction);
            }
            tableView.refresh();
            targetBox.getChildren().clear();

            Label dateLabel = new Label(date);
            dateLabel.getStyleClass().add("historyContent");
            targetBox.getChildren().add(dateLabel);
            targetBox.getChildren().add(getDummy());


        });
    }
    private void setHighlight(boolean value){
        if(!value){
            setStyle("-fx-background-color:"+lightBackColor+";");
            for (Label label:labels
                 ) {
                label.setTextFill(Color.BLACK);
                label.setStyle("-fx-font-weight:normal");
            }
            //totalLabel.setTextFill(contrast);
        }
        else{
            setStyle("-fx-background-color:"+backColor+";");
            for (Label label:labels
            ) {
                label.setTextFill(contrast);
                label.setStyle("-fx-font-weight:bold");
            }
            //totalLabel.setTextFill(Color.BLACK);
        }
    }
    private void addHover(){
        addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            setHighlight(true);
        });
        addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            if(this!=selected) setHighlight(false);
        });
    }
}
