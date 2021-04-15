package Dao;

public class DaoContainer {
    private static String dataBaseUrl = null;

    private SourceDao sourceDao = null;
    private NameDao nameDao = null;
    private CategoryDao categoryDao = null;
    private ExpenseDao expenseDao = null;
    private UnitDao unitDao = null;
    private TransactionDao transactionDao = null;
    private TagDao tagDao = null;



    public DaoContainer(String dataBaseUrl) {
        this.dataBaseUrl = dataBaseUrl;
    }

    public static String getDataBaseUrl() {
        return dataBaseUrl;
    }

    public SourceDao getSourceDao() {
        if (sourceDao == null) sourceDao = new SourceDao();
        return sourceDao;
    }
    public TagDao getTagDao() {
        if (tagDao == null) tagDao = new TagDao();
        return tagDao;
    }

    public NameDao getNameDao() {
        if (nameDao == null) nameDao = new NameDao();
        return nameDao;
    }

    public CategoryDao getCategoryDao() {
        if (categoryDao == null) categoryDao = new CategoryDao();
        return categoryDao;
    }

    public ExpenseDao getExpenseDao() {
        if (expenseDao == null) expenseDao = new ExpenseDao();
        return expenseDao;
    }

    public UnitDao getUnitDao() {
        if (unitDao == null) unitDao = new UnitDao();
        return unitDao;
    }

    public TransactionDao getTransactionDao() {
        if (transactionDao == null) transactionDao = new TransactionDao();
        return transactionDao;
    }
}
