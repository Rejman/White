package Dao;

import Models.Unit;

import java.sql.*;
import java.util.HashSet;
import java.util.Properties;


public class UnitDao implements ModelDao<Unit> {
    public static final String INSERT_SQL = "INSERT INTO units (name, shortcut, real_number) VALUES(?,?,?)";
    public static final String UPDATE_SQL = "UPDATE units SET name=?, shortcut=?, real_number=? WHERE id=?";
    public static final String SELECT_SQL = "SELECT * FROM units;";
    public static final String DELETE_SQL = "DELETE FROM units WHERE id=?;";


    @Override
    public Unit insert(Unit model, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(INSERT_SQL);
        ps.setString(1,model.getName());
        ps.setString(2, model.getShortcut());
        short value = 0;
        if(model.isRealNumber()) value = 1;
        ps.setInt(3, value);
        ps.execute();
        long id =  Dao.getLastId(ps);
        model.setId(id);
        return model;
    }

    @Override
    public Unit insertOne(Unit model) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DaoContainer.getDataBaseUrl());
            insert(model,conn);
            conn.close();
            return model;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Unit model) {
        Properties properties = new Properties();
        properties.setProperty("PRAGMA foreign_keys", "ON");
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DaoContainer.getDataBaseUrl(),properties);
            Dao.enableForeignKeys(conn);
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
    public void update(Unit model) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DaoContainer.getDataBaseUrl());
            PreparedStatement ps = conn.prepareStatement(UPDATE_SQL);
            ps.setString(1,model.getName());
            ps.setString(2,model.getShortcut());
            int value = 0;
            if(model.isRealNumber()) value = 1;
            ps.setInt(3,value);
            ps.setInt(4, (int) model.getId());
            ps.execute();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public HashSet<Unit> selectAll() {
        HashSet<Unit> units = new HashSet<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DaoContainer.getDataBaseUrl());
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_SQL);
            while(rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String shortcut = rs.getString("shortcut");
                boolean real_number = false;
                if(rs.getInt("real_number")==1) real_number = true;
                units.add(new Unit(id,name,shortcut, real_number));
            }
            conn.close();
            return units;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
