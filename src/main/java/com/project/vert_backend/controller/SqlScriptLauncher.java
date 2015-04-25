package com.project.vert_backend.controller;

import com.project.vert_backend.database.DatabaseConnection;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Ensures a database is setup and configured properly for the application.
 * @author dev
 */
class SqlScriptLauncher {
    private static final String DB_NAME = PropertiesController.getInstance().getProperty("database.name");
    private static final String DB_SCRIPT = PropertiesController.getInstance().getProperty("files.scripts.db.setup");

    public SqlScriptLauncher() { }

    /**
     * Determine whether the database for the application exists and execute the sql script if it does not.
     */
    public void run() {
        if (DatabaseConnection.databaseExists(DB_NAME)) {
            System.out.println("SqlScriptLauncher: Database '" + DB_NAME + "' already exists");
        } else {
            System.out.println("SqlScriptLauncher: Database '" + DB_NAME + "' does not yet exist");
            System.out.println("SqlScriptLauncher: Launching '" + DB_SCRIPT + "' to create database...");
            executeSQLScript(DB_SCRIPT);
            System.out.println("SqlScriptLauncher: Script execution complete...");
        }
    }

    /**
     * Parse and execute commands from the sql script specified.
     * NOTE: Does not handle comments in the sql script
     * @param filename  Name of sql script file to be executed
     */
    private void executeSQLScript(String filename) {
        /// Delimiter
        String delimiter = ";";

        /// Get file from resources folder and setup scanner
        Scanner scanner;
        File file = new File(getClass().getClassLoader().getResource(filename).getFile());
        try {
            scanner = new Scanner(file).useDelimiter(delimiter);
        } catch (FileNotFoundException e) {
            System.out.println("SqlScriptLauncher: Scanner Exception - " + e);
            return;
        }

        // Loop through the SQL file statements
        // TODO: THIS DOES NOT SUPPORT COMMENTS!
        Connection conn = DatabaseConnection.getConnection();
        Statement currentStatement = null;
        while (scanner.hasNext()) {

            // Get statement
            String rawStatement = scanner.next() + delimiter;
            try {
                // Execute statement
                currentStatement = conn.createStatement();
                currentStatement.execute(rawStatement);
            } catch (SQLException e) {
                System.out.println("SqlScriptLauncher: Execute Exception - " + e);
            } finally {
                // Release resources
                if (currentStatement != null) {
                    try {
                        currentStatement.close();
                    } catch (SQLException e) {
                        System.out.println("SqlScriptLauncher: Release Exception - " + e);
                    }
                }
                currentStatement = null;
            }
        }
        DatabaseConnection.closeConnection(conn);
    }
}
