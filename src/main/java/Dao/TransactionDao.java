package Dao;

import Models.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;


public class TransactionDao implements ModelDao<Transaction>{

    public static final String INSERT_SQL = "INSERT INTO transactions (date, source_id, expense_id, number, unit_price, tag_id) VALUES(?,?,?,?,?,?)";
    public static final String UPDATE_TAG_SQL = "UPDATE transactions SET tag_id=? WHERE id=?";
    public static final String SET_TAG_SQL = "UPDATE transactions SET tag_id=? WHERE id=?";
    public static final String DELETE_TAG_SQL = "UPDATE transactions SET tag_id=NULL WHERE id=?";
    public static final String SELECT_SQL = " SELECT t.id, t.date, source_id, s.name as source_name, s.color as source_color, expense_id, name_id, n.name as expense_name, number, unit_price, unit_id, u.name as unit_name, u.shortcut, u.real_number, category_id, c.name as category_name, c.color as category_color, tags.id as tag_id, tags.name as tag_name FROM transactions t JOIN sources s ON s.id = source_id JOIN expenses e ON e.id = t.expense_id JOIN names n ON n.id = e.name_id JOIN units u ON u.id = e.unit_id JOIN categories c ON c.id=n.category_id LEFT JOIN tags ON tags.id=t.tag_id ORDER BY t.date DESC, s.name;";
    public static final String DELETE_SQL = "DELETE FROM transactions WHERE id=?;";

    private String url;
    public TransactionDao(String url) {
        this.url = url;
    }
    @Override
    public Transaction insertOne(Transaction model) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url);
            insert(model,conn);
            conn.close();
            return model;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Transaction model) {
        Properties properties = new Properties();
        properties.setProperty("PRAGMA foreign_keys", "ON");
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url, properties);
            PreparedStatement ps = conn.prepareStatement(DELETE_SQL);
            ps.setLong(1,model.getId());
            ps.executeUpdate();
            conn.close();
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    @Override
    public void update(Transaction model) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url);
            PreparedStatement ps = conn.prepareStatement(UPDATE_TAG_SQL);
            ps.setLong(1,model.getTag().getId());
            ps.setLong(2,model.getId());
            ps.execute();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void setTag(Transaction model, Tag tag){
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url);
            PreparedStatement ps = conn.prepareStatement(SET_TAG_SQL);
            if(tag==null){
                ps.setNull(1,0);
            }else{
                ps.setLong(1,tag.getId());
            }
            ps.setLong(2,model.getId());

            ps.execute();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void deleteTag(Transaction model){
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url);
            PreparedStatement ps = conn.prepareStatement(DELETE_TAG_SQL);
            ps.setLong(1,model.getId());
            ps.execute();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public HashSet<Transaction> selectAll() {
        return null;
    }


    public ArrayList<Transaction> selectAllSorted() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_SQL);
            while(rs.next()) {
                long id = rs.getLong("id");
                long date = rs.getLong("date");
                long source_id = rs.getLong("source_id");
                long expense_id = rs.getLong("expense_id");
                float number = rs.getFloat("number");
                float unit_price = rs.getFloat("unit_price");
                String source_name = rs.getString("source_name");
                String name_of_name = rs.getString("expense_name");
                String color = rs.getString("source_color");
                long name_id = rs.getLong("name_id");
                long unit_id = rs.getLong("unit_id");
                long category_id = rs.getLong("category_id");
                String unit_name = rs.getString("unit_name");
                String shortcut = rs.getString("shortcut");
                boolean is_real = rs.getBoolean("real_number");
                String category_name = rs.getString("category_name");
                String category_color = rs.getString("category_color");
                long tag_id = rs.getLong("tag_id");
                String tag_name = rs.getString("tag_name");

                Source source = new Source(source_id,source_name,"",color);
                Category category = new Category(category_id,category_name,category_color);
                Name name = new Name(name_id,name_of_name,category);
                Unit unit = new Unit(unit_id, unit_name, shortcut, is_real);
                Expense expense = new Expense(expense_id,name,unit);
                Tag tag = new Tag(tag_id,tag_name);

                transactions.add(new Transaction(id,date,source,expense,number,unit_price,tag));
            }
            conn.close();
            return transactions;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Transaction insert(Transaction model, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(INSERT_SQL);
        ps.setLong(1, model.getDate());
        ps.setLong(2, model.getSource().getId());
        ps.setLong(3, model.getExpense().getId());
        BigDecimal number = model.getNumber();
        BigDecimal unit_price = model.getUnit_price();
        ps.setFloat(4, number.floatValue());
        ps.setFloat(5, unit_price.floatValue());
        Tag tag = model.getTag();
        if(tag!=null){
            ps.setLong(6,tag.getId());
        }else{
            ps.setNull(6,0);
        }
        ps.execute();
        long id =  Dao.getLastId(ps);
        model.setId(id);
        return model;
    }
}
