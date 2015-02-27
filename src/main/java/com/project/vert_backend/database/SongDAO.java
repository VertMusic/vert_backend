package com.project.vert_backend.database;

import com.project.vert_backend.model.Song;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Selwyn Lehmann
 */
public class SongDAO {

    /**
     * Retrieves all users from the database. TODO: needs to be modified to not return password hash nor access token
     * if not necessary
     * @return  List of User objects
     */
    public List<Song> findByPlaylist(String id) {
        String sql = "SELECT * FROM Songs WHERE ID=?";
        List<Song> list = new ArrayList();
        Connection connection = DatabaseConnection.getConnection();

        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, id);
            ResultSet resultSet = pStatement.executeQuery(sql);
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
     * Persist new Song to database.
     * @param user  The Song prepared to be persisted to the database (must have a GUID generated)
     * @return      The persisted Song.
     */
    public Song create(Song song) {
        String sql = "INSERT INTO Songs (ID, Title, Artist, PlaylistID, Duration, Filepath) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = DatabaseConnection.getConnection();

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
