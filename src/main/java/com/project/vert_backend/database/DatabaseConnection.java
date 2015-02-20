package com.project.vert_backend.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Selwyn Lehmann
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private static final String DB_CLASS = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/VertMusicDB";
    private static final String DB_USER = "root";//"java-user";
    private static final String DB_PASSWORD = "";//"java-password1234";

    /**
     * Creates a connection to the MySQL database, initializes singleton if necessary.
     * @return  Connection to MySQL database
     */
    public static Connection getConnection() {
        try {
            Class.forName(DB_CLASS);
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("DatabaseConnection - OpenConnection Exception: " + ex);
            return null;
        }
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
