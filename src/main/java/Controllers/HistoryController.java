package Controllers;

import Controllers.widgets.HistoryPosition;
import Controllers.widgets.transactionTable.TransactionTable;
import Models.Transaction;
import Utils.Blocker;
import Utils.DateConvertor;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;

public class HistoryController extends Controller {
    private TransactionTable transactionTable = new TransactionTable(this);
    private ArrayList<Transaction> transactions = new ArrayList<>();
    @FXML
    private VBox labelBox;
    @FXML
    private VBox groupVBox;
    @FXML
    private VBox tableBox;
    @FXML
    private SplitPane splitPane;

    private ArrayList<HistoryPosition> historyPositions = new ArrayList<>();
    private Service buildHistory = new Service() {
        @Override
        protected Task createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {

                    transactions = daoContainer.getTransactionDao().selectAllSorted();

/*                    if (transactions.size() > 0) {
                        historyPositions.clear();
                    }*/

                    return null;
                }

                @Override
                protected void failed() {
                    getException().printStackTrace();
                    Blocker.error();
                }

                @Override
                protected void succeeded() {
                    refresh();
                }
            };
        }
    };

    public TransactionTable getTransactionTable() {
        return transactionTable;
    }

    public void refresh() {
        historyPositions.clear();
        try {
            transactionGrouping();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("odświeżam historie");
        groupVBox.getChildren().clear();
        String lastDate = "-";
        System.out.println(historyPositions);
        for (int i = 0; i < historyPositions.size(); i++) {
            HistoryPosition position = historyPositions.get(i);
            //when next date - add label
            String date = position.getDate();
            if (!lastDate.equals(date)) {
                Label dateLabel = new Label(date);
                //dateLabel.setPadding(new Insets(5));
                //dateLabel.setStyle("-fx-font-weight: bold");
                System.out.println("dodaje date label: "+dateLabel.getText());
                groupVBox.getChildren().add(dateLabel);
            }
            HBox.setHgrow(position, Priority.ALWAYS);
            //VBox.setVgrow(position,Priority.ALWAYS);
            groupVBox.getChildren().add(position);
            lastDate = date;
        }
    }

    @FXML
    void initialize() throws ParseException {

        Controller.setAutoSize(splitPane, 100, 100);
        HistoryPosition.setTargetBox(labelBox);

        HistoryPosition.setHistoryController(this);

        VBox.setVgrow(transactionTable, Priority.ALWAYS);

        tableBox.getChildren().add(transactionTable);
        splitPane.setDividerPosition(0, 0.7);

        buildHistory.reset();
        buildHistory.start();

    }

    public void addToList(Transaction transaction) {
        if (transaction.getTag() != null) transactionTable.showTags();
        transactionTable.addTransaction(transaction);

    }

    private void addHistoryGroup(ArrayList<Transaction> group, long date, BigDecimal total) throws ParseException {
        //add date label
        String dateString = DateConvertor.toString(date);

        historyPositions.add(new HistoryPosition(group, dateString, total));
    }

    private void transactionGrouping() throws ParseException {
        ArrayList<Transaction> list = null;
        String oldGroupId = "-";
        String groupId;
        BigDecimal total = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

        for (Transaction transaction : transactions
        ) {
            groupId = "" + transaction.getDate() + transaction.getSource();
            System.out.println("goupId " + groupId);
            BigDecimal total_price = transaction.getTotal();

            if (groupId.equals(oldGroupId)) {
                list.add(transaction);
                total = total.add(total_price);
            } else {
                if (list != null) {
                    Transaction first = list.get(0);
                    addHistoryGroup(list, first.getDate(), total);
                }
                list = new ArrayList<>();
                total = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
                total = total.add(total_price);
                list.add(transaction);
                oldGroupId = groupId;
            }

        }
        Transaction first = list.get(0);
        addHistoryGroup(list, first.getDate(), total);

    }

}
