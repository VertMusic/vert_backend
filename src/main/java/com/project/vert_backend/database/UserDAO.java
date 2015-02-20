package com.project.vert_backend.database;

import com.project.vert_backend.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Selwyn Lehmann
 */
public class UserDAO {

    /**
     * Returns all users from the database. TODO: needs to be modified to not return password hash nor access token
     * if not necessary
     * @return
     */
    public List<User> findAll() {
        String sql = "SELECT * FROM Users";
        List<User> list = new ArrayList();
        Connection connection = DatabaseConnection.getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(processRow(resultSet));
            }
        } catch (SQLException ex) {
            System.out.println("UserDAO - findAll Exception: " + ex);
        }

        DatabaseConnection.closeConnection(connection);
        return list;
    }

    /**
     * Returns one user from the database. TODO: Also needs to be modified eventually for security reasons.
     * @param id    The id of the User to find
     * @return      An User object
     */
    public User findById(String id) {
        String sql = "SELECT * FROM Users WHERE ID=?";
        User user = null;
        Connection connection = DatabaseConnection.getConnection();

        try {
            ///Use PreparedStatement to insert "id" for "?" in sql string.
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, id);

            ResultSet resultSet = pStatement.executeQuery();
            if (resultSet.next()) {
                user = processRow(resultSet);
            }
        } catch (SQLException ex) {
            System.out.println("UserDAO - findById Exception: " + ex);
        }
        DatabaseConnection.closeConnection(connection);
        return user;
    }

    /**
     * Returns one user from the database. TODO: Also needs to be modified eventually for security reasons.
     * @param id    The id of the User to find
     * @return      An User object
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE Username=?";
        User user = null;
        Connection connection = DatabaseConnection.getConnection();

        try {
            ///Use PreparedStatement to insert "id" for "?" in sql string.
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, username);

            ResultSet resultSet = pStatement.executeQuery();
            if (resultSet.next()) {
                user = processRow(resultSet);
            }
        } catch (SQLException ex) {
            System.out.println("UserDAO - findByUsername Exception: " + ex);
        }
        DatabaseConnection.closeConnection(connection);
        return user;
    }

    /**
     * Persist new User to database.
     * @param user  The User prepared to be persisted to the database (must have a GUID generated)
     * @return      The persisted User.
     */
    public User create(User user) {
        String sql = "INSERT INTO Users (ID, Name, Username, Email, AuthToken, PasswordHash) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = DatabaseConnection.getConnection();

        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, user.getId());
            pStatement.setString(2, user.getName());
            pStatement.setString(3, user.getUsername());
            pStatement.setString(4, user.getEmail());
            pStatement.setString(5, user.getCredentialToken());
            pStatement.setString(6, user.getPasswordHash());
            pStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("UserDAO - create Exception: " + ex);
            user = null;
        }
        DatabaseConnection.closeConnection(connection);
        return user;
    }

    /**
     * Update a current User's information in the database;
     * @param user  The new User object to replace the current one.
     * @return      The updated User object.
     */
    public User update(String id, User user) {
        String sql = "UPDATE Users SET Name=?, Username=?, Email=?, AuthToken=?, PasswordHash=? WHERE ID=?";
        Connection connection = DatabaseConnection.getConnection();

        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, user.getName());
            pStatement.setString(2, user.getUsername());
            pStatement.setString(3, user.getEmail());
            pStatement.setString(4, user.getCredentialToken());
            pStatement.setString(5, user.getPasswordHash());
            pStatement.setString(6, id);
            pStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("UserDAO - update Exception: " + ex);
            user = null;
        }
        DatabaseConnection.closeConnection(connection);
        return user;
    }

    /**
     * Removes a User with the given ID from the database.
     * @param id    The ID that identifies the user to be deleted.
     */
    public void delete(String id) {
        String sql = "DELETE FROM Users WHERE ID=?";
        Connection connection = DatabaseConnection.getConnection();

        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, id);
            pStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("UserDAO - delete Exception: " + ex);
        }
        DatabaseConnection.closeConnection(connection);
    }

    /**
     * Grabs data from a table row by their column name
     * @param resultSet         A SQL Table row.
     * @return                  An User object.
     * @throws                  SQLException
     */
    private User processRow(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getString("ID"));
        user.setName(resultSet.getString("Name"));
        user.setUsername(resultSet.getString("Username"));
        user.setEmail(resultSet.getString("Email"));
        user.setPasswordHash(resultSet.getString("PasswordHash"));
        user.setCredentialToken(resultSet.getString("AuthToken"));

        return user;
    }

}
