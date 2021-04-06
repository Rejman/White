package Dao;

import java.sql.*;
import java.util.Properties;

public interface Dao<T> {

    //Connection connection = null;

    // get the last saved object's id
    static long getLastId(PreparedStatement preparedStatement) throws SQLException {
        ResultSet rs = preparedStatement.getGeneratedKeys();
        return rs.getInt(1);
    }

    // enable foreign keys detection
    static void enableForeignKeys(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("PRAGMA foreign_keys = ON; ");
    }

    // return properties for connection with enable foreign keys detection
    static Properties getPropertiesWithForeignKeys() {
        Properties properties = new Properties();
        properties.setProperty("PRAGMA foreign_keys", "ON");
        return properties;
    }

    // insert object to database
    T insert(T object, Connection conn) throws SQLException;
}