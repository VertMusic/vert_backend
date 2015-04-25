package com.project.vert_backend.database;

import com.project.vert_backend.controller.PropertiesController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Selwyn Lehmann
 */
public class DatabaseConnection {

//    private static final String DB_CLASS = "com.mysql.jdbc.Driver";
//    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/";
//    private static final String DB_NAME = "VertMusicDB";
//    private static final String DB_USER = "root";//"java-user";
//    private static final String DB_PASSWORD = "";//"java-password1234";
    private static final String DB_CLASS = PropertiesController.getInstance().getProperty("database.driverClass");
    private static final String DB_URL = PropertiesController.getInstance().getProperty("database.url");
    private static final String DB_NAME = PropertiesController.getInstance().getProperty("database.name");
    private static final String DB_USER = PropertiesController.getInstance().getProperty("database.auth.username");
    private static final String DB_PASSWORD = PropertiesController.getInstance().getProperty("database.auth.password");

    /**
     * Creates a connection to the MySQL database. E.g "<database.url>/<database.name>"
     * @return  Connection to required MySQL database
     */
    public static Connection getDatabaseConnection() {
        try {
            Class.forName(DB_CLASS);
            return DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("DatabaseConnection - OpenDatabaseConnection Exception: " + ex);
            return null;
        }
    }

    /**
     * Creates connection to general SQL prompt. E.g. "<database.url>"
     * @return Connection to MySQL
     */
    public static Connection getConnection() {
        try {
            Class.forName(DB_CLASS);
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Connection - OpenConnection Exception: " + ex);
            return null;
        }
    }

    /**
     * Check if database exist for the given database name.
     * @param databaseName  Name of database to check
     * @return              True if exists, false otherwise.
     */
    public static boolean databaseExists(String databaseName) {
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema=?";
        Connection connection = DatabaseConnection.getConnection();

        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, DB_NAME);
            ResultSet resultSet = pStatement.executeQuery();

            resultSet.next();
            return resultSet.getInt("COUNT(*)") > 0;
        } catch (SQLException ex) {
            System.out.println("DatabaseConnection - databaseExists Exception: " + ex);
        }

        DatabaseConnection.closeConnection(connection);
        return false;
    }

    /**
     * Handles closing of the MySQL database connection.
     * @param connection    Connection to the database needed closing.
     */
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.println("DatabaseConnection - Close Exception: " + ex);
        }
    }
}
