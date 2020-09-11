package pl.wsb.Database;

import java.sql.*;
import java.util.Properties;

// Establishing database connection using JDBS driver and postgerSQL
public class Connector {
    static final String DB_URL = "jdbc:postgresql://localhost:5433/cardealertycoon";
    static final String USER = "postgres";
    static final String PASS = "JavaIsTheBest";
    static private Connection CONNECTION;

    public static void connect() throws SQLException, ClassNotFoundException {
        Properties props = new Properties();
        props.setProperty("user", USER);
        props.setProperty("password", PASS);
        CONNECTION = DriverManager.getConnection(DB_URL, props);
        System.out.println("Connected");
    }

    public static ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = CONNECTION.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet;
    }

    public static Integer getNumRows(String table) throws SQLException {
        String sql = "select count(*) from " + table;
        Statement statement = CONNECTION.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        return resultSet.getInt(1);
    }
}
