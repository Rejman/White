package Dao;

import Models.Source;

import java.sql.*;
import java.util.HashSet;

public class SourceDao implements ModelDao<Source> {
    public static final String INSERT_SQL = "INSERT INTO sources (name, description, color) VALUES(?,?,?)";
    public static final String UPDATE_SQL = "UPDATE sources SET name=?, description=?, color=? WHERE id=?";
    public static final String SELECT_SQL = "SELECT * FROM sources;";
    public static final String DELETE_SQL = "DELETE FROM sources WHERE id=?;";

    @Override
    // insert to database & return with new id
    // can by use for many object in loop
    // do not close connection
    public Source insert(Source model, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(INSERT_SQL);
        ps.setString(1, model.getName());
        ps.setString(2, model.getDescription());
        ps.setString(3, model.getColor());
        ps.execute();
        long id = Dao.getLastId(ps);
        model.setId(id);
        return model;
    }

    @Override
    // insert one object to database & close connection
    public Source insertOne(Source model) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DaoContainer.getDataBaseUrl());
            insert(model, conn);
            conn.close();
            return model;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    // delete one object from database & close connection
    public boolean delete(Source model) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DaoContainer.getDataBaseUrl(), Dao.getPropertiesWithForeignKeys());
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
    public void update(Source model) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DaoContainer.getDataBaseUrl());
            PreparedStatement ps = conn.prepareStatement(UPDATE_SQL);
            ps.setString(1, model.getName());
            ps.setString(2, model.getDescription());
            ps.setString(3, model.getColor());
            ps.setInt(4, (int) model.getId());
            ps.execute();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public HashSet<Source> selectAll() {
        HashSet<Source> sources = new HashSet<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DaoContainer.getDataBaseUrl());
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_SQL);
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String color = rs.getString("color");
                sources.add(new Source(id, name, description, color));
            }
            conn.close();
            return sources;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
