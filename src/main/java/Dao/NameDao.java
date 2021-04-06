package Dao;

import Models.Category;
import Models.Name;

import java.sql.*;
import java.util.HashSet;
import java.util.Properties;

public class NameDao implements ModelDao<Name> {
    public static final String INSERT_SQL = "INSERT INTO names (name, category_id) VALUES(?,?)";
    public static final String UPDATE_SQL = "UPDATE names SET name=?, category_id=? WHERE id=?";
    public static final String SELECT_SQL = "SELECT n.id AS name_id, n.name AS name_name, c.id AS category_id, c.name AS category_name, c.color AS category_color FROM names n INNER JOIN categories c ON c.id=n.category_id";
    public static final String DELETE_SQL = "DELETE FROM names WHERE id=?;";
    private String url;
    public NameDao(String url) {
        this.url = url;
    }

    @Override
    public Name insert(Name model, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(INSERT_SQL);
        ps.setString(1,model.getName());
        ps.setInt(2, (int) model.getCategory().getId());
        ps.execute();
        long id =  Dao.getLastId(ps);
        model.setId(id);
        return model;
    }

    @Override
    public Name insertOne(Name model) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url);
            insert(model, conn);
            conn.close();
            return model;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Name model) {
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
    public void update(Name model) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url);
            PreparedStatement ps = conn.prepareStatement(UPDATE_SQL);
            ps.setString(1,model.getName());
            ps.setInt(2, (int) model.getCategory().getId());
            ps.setInt(3, (int) model.getId());
            ps.execute();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public HashSet<Name> selectAll() {
        HashSet<Name> names = new HashSet<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_SQL);
            while(rs.next()) {
                long name_id = rs.getLong("name_id");
                long category_id = rs.getLong("category_id");

                String name_name = rs.getString("name_name");
                String category_name = rs.getString("category_name");

                String category_color = rs.getString("category_color");
                Category category = new Category(category_id, category_name, category_color);
                names.add(new Name(name_id,name_name,category));
            }
            conn.close();
            return names;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
