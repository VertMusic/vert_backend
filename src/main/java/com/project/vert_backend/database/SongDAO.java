package com.project.vert_backend.database;

import com.project.vert_backend.model.Song;
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
public class SongDAO {

    /**
     * Retrieves all songs from the database.
     * @return  List of Song objects
     */
    public List<Song> findAll() {
        String sql = "SELECT * FROM Songs";
        List<Song> list = new ArrayList();
        Connection connection = DatabaseConnection.getDatabaseConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(processRow(resultSet));
            }
        } catch (SQLException ex) {
            System.out.println("SongDAO - findAll Exception: " + ex);
        }

        DatabaseConnection.closeConnection(connection);
        return list;
    }

    /**
     * Retrieves all fields of songs that match the playlist id.
     * @return  List of Song objects
     */
    public List<Song> findByPlaylist(String id) {
        String sql = "SELECT * FROM Songs WHERE PlaylistID=?";
        List<Song> list = new ArrayList();
        Connection connection = DatabaseConnection.getDatabaseConnection();

        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, id);
            ResultSet resultSet = pStatement.executeQuery();
            while (resultSet.next()) {
                list.add(processRow(resultSet));
            }
        } catch (SQLException ex) {
            System.out.println("SongDAO - findByPlaylist Exception: " + ex);
        }

        DatabaseConnection.closeConnection(connection);
        return list;
    }

    /**
     * Gets songs that have the playlist id, but only return the value of attribute specified by the selector.
     * @param id        The ID of the playlist
     * @param selector  The String attribute to grab from the database
     * @return          List of String attribute values of Songs in the playlist specified
     */
    public List<String> findIDsByPlaylist(String id) {

        String sql = "SELECT * FROM Songs WHERE PlaylistID=?";

        List<String> list = new ArrayList();
        Connection connection = DatabaseConnection.getDatabaseConnection();

        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, id);
            ResultSet resultSet = pStatement.executeQuery();
            while (resultSet.next()) {
                list.add(resultSet.getString("ID"));
            }
        } catch (SQLException ex) {
            System.out.println("SongDAO - findIDsByPlaylist Exception: " + ex);
        }

        DatabaseConnection.closeConnection(connection);
        return list;
    }

    /**
     * Persist new Song to database.
     * @param user  The Song prepared to be persisted to the database (must have a GUID generated)
     * @return      The persisted Song.
     */
    public Song create(Song song) {
        String sql = "INSERT INTO Songs (ID, Title, Artist, PlaylistID, Duration, Filepath) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = DatabaseConnection.getDatabaseConnection();

        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, song.getId());
            pStatement.setString(2, song.getTitle());
            pStatement.setString(3, song.getArtist());
            pStatement.setString(4, song.getPlaylistId());
            pStatement.setString(5, song.getDuration());
            pStatement.setString(6, song.getFilepath());
            pStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SongDAO - create Exception: " + ex);
            song = null;
        }
        DatabaseConnection.closeConnection(connection);
        return song;
    }

    /**
     * Returns one playlist from the database. TODO: Also needs to be modified eventually for security reasons.
     * @param id    The id of the Playlist to find
     * @return      An Playlist object
     */
    public Song findById(String id) {
        String sql = "SELECT * FROM Songs WHERE ID=?";
        Song song = null;
        Connection connection = DatabaseConnection.getDatabaseConnection();

        try {
            ///Use PreparedStatement to insert "id" for "?" in sql string.
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, id);

            ResultSet resultSet = pStatement.executeQuery();
            if (resultSet.next()) {
                song = processRow(resultSet);
            }
        } catch (SQLException ex) {
            System.out.println("SongDAO - findById Exception: " + ex);
        }
        DatabaseConnection.closeConnection(connection);
        return song;
    }

    /**
     * Removes a Song with the given ID from the database.
     * @param id    The ID that identifies the song to be deleted.
     */
    public boolean delete(String id) {

        if (id == null || id.equalsIgnoreCase("")) {
            return false;
        }

        String sql = "DELETE FROM Songs WHERE ID=?";
        Connection connection = DatabaseConnection.getDatabaseConnection();

        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, id);
            pStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SongDAO - delete Exception: " + ex);
        }
        DatabaseConnection.closeConnection(connection);

        return true;
    }

    /**
     * Grabs data from a table row by their column name
     * @param resultSet         A SQL Table row.
     * @return                  An Song object.
     * @throws                  SQLException
     */
    private Song processRow(ResultSet resultSet) throws SQLException {
        Song song = new Song();
        song.setId(resultSet.getString("ID"));
        song.setTitle(resultSet.getString("Title"));
        song.setArtist(resultSet.getString("Artist"));
        song.setPlaylistId(resultSet.getString("PlaylistID"));
        song.setDuration(resultSet.getString("Duration"));
        song.setFilepath(resultSet.getString("Filepath"));

        return song;
    }
}
