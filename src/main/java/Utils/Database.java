package Utils;

public class Database /*extends Task<Void>*/ {
    /*public static ProgressBar progressBar;
    private Date day;
    private Source source;
    private List<TranPos> transactionList;
    private ModelStructure modelStructure;

    public Database(Date day, Source source, List<TranPos> transactionList) throws CloneNotSupportedException {
        this.modelStructure = (ModelStructure) modelStructure.clone();
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(progressProperty());
        this.day = day;
        this.source = source;
        this.transactionList = transactionList;
    }

    @Override
    protected Void call() throws Exception {

        progressBar.setVisible(true);
        System.out.println(transactionList);
        int number = transactionList.size();

        int step = 0;

        for (TranPos rI:transactionList
             ) {
            //TranPos rI = transactionList.get(i);
            Unit unit = rI.getUnit();
            Name name = rI.getName();
            float unitPrice = Float.parseFloat(rI.getUnitPrice());
            float quantity = Float.parseFloat(rI.getQuantity());
            Expense selectedExpense = null;
            for (Expense expense : Controller.modelStructure.getExpenses()
            ) {
                if (expense.getUnit().equals(unit) && expense.getName().equals(name)) {
                    selectedExpense = expense;
                    break;
                }
            }
            if (selectedExpense == null) {
                selectedExpense = .expenseDao.insertOne(new Expense(name, unit));
                Controller.modelStructure.getExpenses().add(selectedExpense);

            }

            Controller.transactionDao.insertOne(new Transaction(DateConvertor.toLong(day), source, selectedExpense, quantity, unitPrice));
            System.out.println("zapisuje");

            System.out.println("step:"+step+"/"+number);
            updateProgress(step,number);
        }
        for(int i=0;i<number;i++){
            *//*TranPos rI = transactionList.get(i);
            Unit unit = rI.getUnit();
            Name name = rI.getName();
            float unitPrice = Float.parseFloat(rI.getUnitPrice());
            float quantity = Float.parseFloat(rI.getQuantity());
            Expense selectedExpense = null;
            for (Expense expense : Controller.modelStructure.getExpenses()
            ) {
                if (expense.getUnit().equals(unit) && expense.getName().equals(name)) {
                    selectedExpense = expense;
                    break;
                }
            }
            if (selectedExpense == null) {
                selectedExpense = Controller.expenseDao.insertOne(new Expense(name, unit));
                Controller.modelStructure.getExpenses().add(selectedExpense);

            }

            Controller.transactionDao.insertOne(new Transaction(DateConvertor.toLong(day), source, selectedExpense, quantity, unitPrice));
            System.out.println("zapisuje");

            System.out.println("step:"+(i+1)+"/"+number);
            updateProgress((i+1),number);*//*
        }

        //progressBar.progressProperty().unbind();
       // progressBar.setVisible(false);

        return null;
    }
    @Override
    protected void failed() {
        getException().printStackTrace();
        System.out.println("failed");
    }

    @Override
    protected void succeeded() {
        System.out.println("succes");
    }*/
}
