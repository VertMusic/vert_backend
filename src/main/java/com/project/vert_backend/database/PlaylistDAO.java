package com.project.vert_backend.database;

import com.project.vert_backend.model.Playlist;
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
public class PlaylistDAO {

    /**
     * Retrieves all playlists from the database. TODO: needs to be modified to not return password hash nor access token
     * if not necessary
     * @return  List of Playlist objects
     */
    public List<Playlist> findAll() {
        String sql = "SELECT * FROM Playlists";
        List<Playlist> list = new ArrayList();
        Connection connection = DatabaseConnection.getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(processRow(resultSet));
            }
        } catch (SQLException ex) {
            System.out.println("PlaylistDAO - findAll Exception: " + ex);
        }

        DatabaseConnection.closeConnection(connection);
        return list;
    }

    /**
     * Persist new Playlist to database.
     * @param playlist  The Playlist prepared to be persisted to the database (must have a GUID generated)
     * @return      The persisted Playlist.
     */
    public Playlist create(Playlist playlist) {

        if (playlist == null) {
            return null;
        }

        String sql = "INSERT INTO Playlists (ID, Name, UserID, Date, Likes, Visibility) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = DatabaseConnection.getConnection();

        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, playlist.getId());
            pStatement.setString(2, playlist.getName());
            pStatement.setString(3, playlist.getAuthor());
            pStatement.setString(4, playlist.getDate());
            pStatement.setInt(5, playlist.getLikes());
            pStatement.setString(6, playlist.getVisibility());
            pStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("PlaylistDAO - create Exception: " + ex);
            playlist = null;
        }
        DatabaseConnection.closeConnection(connection);
        return playlist;
    }

    /**
     * Update a current Playlist's information in the database;
     * @param playlist  The new Playlist object to replace the current one.
     * @return      The updated Playlist object.
     */
    public Playlist update(String id, Playlist playlist) {

        if (playlist == null || id == null || id.equalsIgnoreCase("")) {
            return null;
        }

        String sql = "UPDATE Playlists SET Name=?, UserID=?, Date=?, Likes=?, Visibility=? WHERE ID=?";
        Connection connection = DatabaseConnection.getConnection();

        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, playlist.getName());
            pStatement.setString(2, playlist.getAuthor());
            pStatement.setString(3, playlist.getDate());
            pStatement.setInt(4, playlist.getLikes());
            pStatement.setString(5, playlist.getVisibility());
            pStatement.setString(6, id);
            pStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("PlaylistDAO - update Exception: " + ex);
            playlist = null;
        }
        DatabaseConnection.closeConnection(connection);
        return playlist;
    }

    /**
     * Removes a Playlist with the given ID from the database.
     * @param id    The ID that identifies the playlist to be deleted.
     */
    public boolean delete(String id) {

        if (id == null || id.equalsIgnoreCase("")) {
            return false;
        }

        String sql = "DELETE FROM Playlists WHERE ID=?";
        Connection connection = DatabaseConnection.getConnection();

        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, id);
            pStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("PlaylistDAO - delete Exception: " + ex);
        }
        DatabaseConnection.closeConnection(connection);

        return true;
    }

    public List<Playlist> findAuthorizedPlaylistsForUser(String authorUsername) {
        String sql = "SELECT * FROM Playlists WHERE UserID=? OR Visibility='public'";
        List<Playlist> list = new ArrayList();
        Connection connection = DatabaseConnection.getConnection();

        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, authorUsername);
            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                list.add(processRow(resultSet));
            }
        } catch (SQLException ex) {
            System.out.println("PlaylistDAO - findAuthorizedPlaylistsForUser Exception: " + ex);
        }

        DatabaseConnection.closeConnection(connection);
        return list;
    }

    /**
     * Returns one playlist from the database. TODO: Also needs to be modified eventually for security reasons.
     * @param id    The id of the Playlist to find
     * @return      An Playlist object
     */
    public Playlist findById(String id) {

        if (id == null || id.equalsIgnoreCase("")) {
            return null;
        }

        String sql = "SELECT * FROM Playlists WHERE ID=?";
        Playlist playlist = null;
        Connection connection = DatabaseConnection.getConnection();

        try {
            ///Use PreparedStatement to insert "id" for "?" in sql string.
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, id);

            ResultSet resultSet = pStatement.executeQuery();
            if (resultSet.next()) {
                playlist = processRow(resultSet);
            }
        } catch (SQLException ex) {
            System.out.println("PlaylistDAO - findById Exception: " + ex);
        }
        DatabaseConnection.closeConnection(connection);
        return playlist;
    }

    /**
     * Grabs data from a table row by their column name
     * @param resultSet         A SQL Table row.
     * @return                  An Playlist object.
     * @throws                  SQLException
     */
    private Playlist processRow(ResultSet resultSet) throws SQLException {
        Playlist playlist = new Playlist();
        playlist.setId(resultSet.getString("ID"));
        playlist.setName(resultSet.getString("Name"));
        playlist.setAuthor(resultSet.getString("UserID"));
        playlist.setDate(resultSet.getString("Date"));
        playlist.setLikes(resultSet.getInt("Likes"));
        playlist.setVisibility(resultSet.getString("Visibility"));

        return playlist;
    }
}
