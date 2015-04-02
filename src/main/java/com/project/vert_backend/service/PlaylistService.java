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

    public PlaylistService(PlaylistDAO databaseAccessor) {
        database = databaseAccessor;
    }

    @Override
    public Playlist create(Map model) {

        System.out.println("PlaylistService: create - " + model);

        if (model == null || model.size() < 4) {
            return null;
        }

        ///TODO ensure we check if the playlist already exists
        Playlist playlist = new Playlist(
                (String) model.get("name"),
                (String) model.get("author"),
                (String) model.get("date"),
                (String) model.get("visibility"),
                (int) model.get("likes")
        );

        return database.create(playlist);
    }

    @Override
    public Playlist read(String id) {
        return database.findById(id);
    }

    @Override
    public Playlist update(String id, Map model) {

        System.out.println("Playlist: update (" + id + ") - " + model);

        if (id == null || id.equalsIgnoreCase("") || model == null || model.size() != 5) {
            return null;
        }

        Playlist updatedPlaylist = new Playlist();
        updatedPlaylist.setId(id);
        updatedPlaylist.setName((String) model.get("name"));
        updatedPlaylist.setAuthor((String) model.get("author"));
        updatedPlaylist.setDate((String) model.get("date"));
        updatedPlaylist.setVisibility((String) model.get("visibility"));
        updatedPlaylist.setLikes((int) model.get("likes"));

        return database.update(id, updatedPlaylist);
    }

    @Override
    public boolean delete(String id) {
        return database.delete(id);
    }

    @Override
    public List<Playlist> list(Map<String, Object> filter) {
        if (filter == null || filter.isEmpty()) {
            return database.findAll();
        } else {
            // Grab the first key
            String attribute = filter.keySet().iterator().next();

            switch (attribute) {
                case "author":
                    return database.findAuthorizedPlaylistsForUser((String) filter.get("author"));
                default:
                    System.out.println("Playlist: ERROR!!!! No attribute found to filter on");
                    return null;
            }
        }
    }

}
