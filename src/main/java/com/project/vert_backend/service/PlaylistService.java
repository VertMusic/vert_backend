package com.project.vert_backend.service;

import com.project.vert_backend.database.PlaylistDAO;
import com.project.vert_backend.model.Playlist;
import java.util.List;
import java.util.Map;

/**
 * @author Selwyn Lehmann
 * Service that interacts with the database regarding all Playlist related functionality. This includes creating, removing,
 * editing the name of, and enumerating them.
 *
 * NOTE: This service does not add songs to playlists.
 *
 */
public class PlaylistService extends GuidModelService<Playlist> {

    private final PlaylistDAO database;

    public PlaylistService() {
        database = new PlaylistDAO();
    }

    @Override
    public Playlist create(Map model) {
        ///TODO ensure we check if the playlist already exists
        Playlist playlist = new Playlist(
                (String) model.get("name"),
                (String) model.get("author"),
                (String) model.get("date"));

        return database.create(playlist);
    }

    @Override
    public Playlist read(String id) {
        return database.findById(id);
    }

    @Override
    public Playlist update(String id, Map model) {
        Playlist updatedPlaylist = new Playlist();
        updatedPlaylist.setId(id);
        updatedPlaylist.setName((String) model.get("name"));
        updatedPlaylist.setAuthor((String) model.get("author"));
        updatedPlaylist.setDate((String) model.get("date"));
        ///TODO: uncomment these lines when the like and sharing features are implemented.
//        updatedPlaylist.setVisibility((String) model.get("visibility"));
//        updatedPlaylist.setLikes((int) model.get("likes"));

        return database.update(id, updatedPlaylist);
    }

    @Override
    public void delete(String id) {
        database.delete(id);
    }

    @Override
    public List<Playlist> list(Map<String, Object> filter) {
        ///TODO: Implement filter here
        return database.findAll();
    }

}
