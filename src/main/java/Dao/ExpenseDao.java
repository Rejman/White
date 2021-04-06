package Dao;

import Models.Category;
import Models.Expense;
import Models.Name;
import Models.Unit;

import java.sql.*;
import java.util.HashSet;
import java.util.Properties;

public class ExpenseDao implements ModelDao<Expense> {
    public static final String INSERT_SQL = "INSERT INTO expenses (name_id, unit_id) VALUES(?,?)";
    public static final String UPDATE_SQL = "UPDATE expenses SET name_id=?, unit_id=? WHERE id=?";
    public static final String SELECT_SQL = "SELECT e.id AS expense_id, n.id AS name_id, n.name as name_name, c.id AS category_id, c.name AS category_name, c.color AS category_color, u.id AS unit_id, u.name AS unit_name, u.shortcut AS unit_shortcut, u.real_number AS real_number FROM expenses e INNER JOIN names n ON n.id=e.name_id INNER JOIN categories c ON c.id=n.category_id INNER JOIN units u ON u.id=e.unit_id";
    public static final String DELETE_SQL = "DELETE FROM expenses WHERE id=?;";

    private String url;
    public ExpenseDao(String url) {
        this.url = url;
    }

    @Override
    public Expense insert(Expense model, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(INSERT_SQL);
        ps.setLong(1,model.getName().getId());
        ps.setLong(2, model.getUnit().getId());
        ps.execute();
        long id =  Dao.getLastId(ps);
        model.setId(id);
        return model;
    }

    @Override
    public Expense insertOne(Expense model) {
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
    public boolean delete(Expense model) {
        Properties properties = new Properties();
        properties.setProperty("PRAGMA foreign_keys", "ON");
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url,properties);
            Dao.enableForeignKeys(conn);
            PreparedStatement ps = conn.prepareStatement(DELETE_SQL);
            ps.setLong(1,model.getId());
            ps.executeUpdate();
            conn.close();
            return true;

        } catch (SQLException throwables) {
            //throwables.printStackTrace();
            return false;
        }
    }

    @Override
    public void update(Expense model) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url);
            PreparedStatement ps = conn.prepareStatement(UPDATE_SQL);
            ps.setLong(1,model.getName().getId());
            ps.setLong(2,model.getUnit().getId());
            ps.setLong(3,model.getId());
            ps.execute();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public HashSet<Expense> selectAll() {
        HashSet<Expense> expenses = new HashSet<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_SQL);
            while(rs.next()) {
                long expense_id = rs.getLong("expense_id");
                long name_id = rs.getLong("name_id");
                long category_id = rs.getLong("category_id");
                long unit_id = rs.getLong("unit_id");

                String name_name = rs.getString("name_name");
                String unit_name = rs.getString("unit_name");
                String category_name = rs.getString("category_name");

                String category_color = rs.getString("category_color");

                String unit_shortcut = rs.getString("unit_shortcut");
                boolean real_number = false;
                if(rs.getInt("real_number")==1) real_number = true;

                Category category = new Category(category_id,category_name,category_color);
                Name name = new Name(name_id, name_name, category);
                Unit unit = new Unit(unit_id,unit_name,unit_shortcut,real_number);
                Expense expense = new Expense(expense_id,name,unit);

                expenses.add(new Expense(expense_id,name,unit));
            }
            conn.close();
            return expenses;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
