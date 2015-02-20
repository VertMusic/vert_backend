package com.project.vert_backend.service;

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

    ///TODO replace with database driver
    private final TempRepository repository;

    public PlaylistService() {
        repository = TempRepository.getInstance();
    }

    @Override
    public Playlist create(Map model) {
        ///TODO ensure we check if the playlist already exists
        Playlist playlist = new Playlist((String) model.get("name"), (String) model.get("author"), (String) model.get("date"));
        return (Playlist) repository.create(playlist);
    }

    @Override
    public Playlist read(String id) {
        return (Playlist) repository.read(new Playlist(), id);
    }

    @Override
    public Playlist update(String id, Map model) {
        return (Playlist) repository.update(new Playlist(), id, model);
    }

    @Override
    public Playlist delete(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Playlist> list(Map<String, Object> filter) {
        return (List<Playlist>) repository.list(new Playlist(), filter);
    }

}
