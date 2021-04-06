package Dao;

import Models.Category;

import java.sql.*;
import java.util.HashSet;
import java.util.Properties;

public class CategoryDao implements ModelDao<Category> {
    public static final String INSERT_SQL = "INSERT INTO categories (name, color) VALUES(?,?)";
    public static final String UPDATE_SQL = "UPDATE categories SET name=?, color=? WHERE id=?";
    public static final String SELECT_SQL = "SELECT n.id AS name_id, n.name AS name_name, c.id AS category_id, c.name AS category_name, c.color AS category_color FROM names n INNER JOIN categories c ON c.id=n.category_id";
    public static final String DELETE_SQL = "DELETE FROM categories WHERE id=?;";
    private String url;
    public CategoryDao(String url) {
        this.url = url;
    }

    @Override
    public Category insert(Category model, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(INSERT_SQL);
        ps.setString(1,model.getName());
        ps.setString(2, model.getColor());
        ps.execute();
        long id =  Dao.getLastId(ps);
        model.setId(id);
        return model;
    }

    @Override
    public Category insertOne(Category model) {
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
    public boolean delete(Category model) {
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
    public void update(Category model) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url);
            PreparedStatement ps = conn.prepareStatement(UPDATE_SQL);
            ps.setString(1,model.getName());
            ps.setString(2,model.getColor());
            ps.setLong(3,model.getId());
            ps.execute();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public HashSet<Category> selectAll() {
        HashSet<Category> categories = new HashSet<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_SQL);
            while(rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String color = rs.getString("color");
                categories.add(new Category(id,name,color));
            }
            conn.close();
            return categories;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
