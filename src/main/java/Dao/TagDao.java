package Dao;

import Models.Source;
import Models.Tag;

import java.sql.*;
import java.util.HashSet;

public class TagDao implements ModelDao<Tag> {
    public static final String INSERT_SQL = "INSERT INTO tags (name) VALUES(?)";
    public static final String UPDATE_SQL = "UPDATE tags SET name=? WHERE id=?";
    public static final String SELECT_SQL = "SELECT id, name FROM tags;";
    public static final String DELETE_SQL = "DELETE FROM tags WHERE id=?;";
    private String url;

    public TagDao(String url) {
        this.url = url;
    }

    @Override
    public Tag insert(Tag model, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(INSERT_SQL);
        ps.setString(1, model.getName());
        ps.execute();
        long id = Dao.getLastId(ps);
        model.setId(id);
        return model;
    }

    @Override
    public Tag insertOne(Tag model) {
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
    public boolean delete(Tag model) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url, Dao.getPropertiesWithForeignKeys());
            Dao.enableForeignKeys(conn);
            PreparedStatement ps = conn.prepareStatement(DELETE_SQL);
            ps.setLong(1, model.getId());
            ps.executeUpdate();
            conn.close();
            return true;

        } catch (SQLException throwables) {
            //throwables.printStackTrace();
            return false;
        }
    }

    @Override
    public void update(Tag model) {

    }

    @Override
    public HashSet<Tag> selectAll() {
        HashSet<Tag> tags = new HashSet<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_SQL);
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                tags.add(new Tag(id, name));
            }
            conn.close();
            return tags;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
