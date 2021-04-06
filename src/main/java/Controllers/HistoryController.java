package Controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;

import Controllers.widgets.HistoryPosition;

import Controllers.widgets.transactionTable.TransactionTable;
import Models.*;
import Utils.Blocker;
import Utils.DateConvertor;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;

import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HistoryController extends Controller{
    private TransactionTable transactionTable = new TransactionTable();

    public TransactionTable getTransactionTable() {
        return transactionTable;
    }

    private ArrayList<Transaction> transactions = new ArrayList<>();
    @FXML
    private VBox labelBox;
    @FXML
    private VBox groupVBox;
    @FXML
    private VBox rightBox;
    @FXML
    private VBox leftBox;
    @FXML
    private VBox tableBox;
    @FXML
    private SplitPane splitPane;
    private ScrollPane scrolPane;
    private Service buildHistory = new Service() {
        @Override
        protected Task createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {

                    transactions = daoContainer.getTransactionDao().selectAllSorted();
                    System.out.println(transactions);
                    if(transactions.size()>0){
                        ArrayList<Transaction> list = null;
                        String oldGroupId = "-";
                        String groupId;
                        BigDecimal total = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
                        int numberOfSteps = transactions.size();
                        int step = 1;
                        for (Transaction transaction:transactions
                        ) {
                            groupId = ""+transaction.getDate()+transaction.getSource();
                            System.out.println("goupId "+groupId);
                            BigDecimal total_price = transaction.getTotal();

                            if(groupId.equals(oldGroupId)){
                                list.add(transaction);
                                total = total.add(total_price);
                            }else{
                                if(list!=null){
                                    Transaction first = list.get(0);
                                    addHistoryGroup(list,first.getDate(),total);
                                }
                                list = new ArrayList<>();
                                total = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
                                total = total.add(total_price);
                                list.add(transaction);
                                oldGroupId=groupId;
                            }
                            updateProgress(step, numberOfSteps);
                            step++;
                        }
                        Transaction first = list.get(0);
                        addHistoryGroup(list,first.getDate(),total);
                    }

                    return null;
                }
                @Override
                protected void failed() {
                    getException().printStackTrace();
                    Blocker.error();
                }

                @Override
                protected void succeeded() {
                    groupVBox.getChildren().clear();
                    String lastDate = "-";
                    for(int i=0;i<historyPositions.size();i++){
                        HistoryPosition position = historyPositions.get(i);
                        //when next date - add label
                        String date = position.getDate();
                        if(!lastDate.equals(date)){
                            Label dateLabel = new Label(date);
                            //dateLabel.setPadding(new Insets(5));
                            //dateLabel.setStyle("-fx-font-weight: bold");
                            groupVBox.getChildren().add(dateLabel);
                        }
                        HBox.setHgrow(position,Priority.ALWAYS);
                        //VBox.setVgrow(position,Priority.ALWAYS);
                        groupVBox.getChildren().add(position);
                        lastDate = date;
                    }
                    //Blocker.unbind();
                }
            };
        }
    };
    @FXML
    private VBox tagBox;
    @FXML
    void initialize() throws ParseException {

        Controller.setAutoSize(splitPane, 100, 100);
        HistoryPosition.setTargetBox(labelBox);

        HistoryPosition.setHistoryController(this);

        VBox.setVgrow(transactionTable,Priority.ALWAYS);

        tableBox.getChildren().add(transactionTable);
        splitPane.setDividerPosition(0,0.7);

        //Blocker.bind(init.progressProperty());
        //tagBox.getChildren().add(transactionTable.getSelectTagBox());

        buildHistory.reset();
        buildHistory.start();

    }
    public void addToList(Transaction transaction){
        if(transaction.getTag()!=null) transactionTable.showTags();
        transactionTable.addTransaction(transaction);

    }
    private ArrayList<HistoryPosition> historyPositions = new ArrayList<>();
    private void addHistoryGroup(ArrayList<Transaction> group, long date, BigDecimal total) throws ParseException {
        //add date label
        String dateString = DateConvertor.toString(date);
/*        Label dateLabel = new Label(dateString);
        dateLabel.setPadding(new Insets(5));
        dateLabel.setStyle("-fx-font-weight: bold");*/
        historyPositions.add(new HistoryPosition(group, dateString, total));
    }
/*    private void addHistoryPositions() throws ParseException {
        String lastDate = "0";
        groupVBox.getChildren().clear();
        for(int i=0;i<groups.size();i++){
            Transaction first = (Transaction) groups.get(i).get(0);
            String date = DateConvertor.toString(first.getDate());
            Label dateLabel = new Label(date);
            dateLabel.setPadding(new Insets(5));
            dateLabel.setStyle("-fx-font-weight: bold");
            if(!lastDate.equals(date)) groupVBox.getChildren().add(dateLabel);

            HistoryPosition historyPosition = new HistoryPosition(groups.get(i), groupTotals.get(i));

            groupVBox.getChildren().add(historyPosition);
            lastDate = date;
        }
    }*/
    private void transactionGrouping() throws ParseException {
        ArrayList<Transaction> list = null;
        String oldGroupId = "-";
        String groupId;
        BigDecimal total = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

        for (Transaction transaction:transactions
        ) {
            groupId = ""+transaction.getDate()+transaction.getSource();
            System.out.println("goupId "+groupId);
            BigDecimal total_price = transaction.getTotal();

            if(groupId.equals(oldGroupId)){
                list.add(transaction);
                total = total.add(total_price);
            }else{
                if(list!=null){
                    Transaction first = list.get(0);
                    addHistoryGroup(list,first.getDate(),total);
                }
                list = new ArrayList<>();
                total = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
                total = total.add(total_price);
                list.add(transaction);
                oldGroupId=groupId;
            }

        }
        Transaction first = list.get(0);
        addHistoryGroup(list,first.getDate(),total);

    }
    /*@FXML
    void deleteSelected(ActionEvent event) {

        //ObservableList<TranPos> selectedItems = expenseTableView.getSelectionModel().getSelectedItems();
        //ArrayList<TranPos> rows = new ArrayList<>(selectedItems);

        rows.forEach(row ->{
            Transaction transaction = row.getTransaction();
            Expense expense = transaction.getExpense();
            Source source = transaction.getSource();
            Name name = expense.getName();
            Unit unit = expense.getUnit();
            Category category = name.getCategory();

            if(transactionDao.delete(row.getTransaction())){

                if(sourceDao.delete(source)){
                    modelStructure.deleteSource(source);
                }
                if(expenseDao.delete(expense)){
                    modelStructure.deleteExpense(expense);
                    if(unitDao.delete(unit)){
                        modelStructure.deleteUnit(unit);
                    }
                    if(nameDao.delete(name)){
                        modelStructure.deleteName(name);
                        if(categoryDao.delete(category)){
                            modelStructure.deleteCategory(category);
                        }
                    }
                }
                //uwaga
                HistoryPosition.selected.getTransactions().remove(row.getTransaction());
                transactions.remove(row.getTransaction());
                expenseTableView.getItems().remove(row);

            }

        });
        //update total label
        System.out.println("Update total");
        System.out.println(HistoryPosition.selected.getTransactions());
        HistoryPosition.selected.updateTotal();

    }*/

}
