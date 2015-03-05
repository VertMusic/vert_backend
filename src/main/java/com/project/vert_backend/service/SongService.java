package com.project.vert_backend.service;

import com.project.vert_backend.database.SongDAO;
import com.project.vert_backend.model.Song;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Selwyn Lehmann
 * Service that interacts with the database regarding all Song functionality.
 */
public class SongService extends GuidModelService<Song> {

    private final SongDAO database;

    public SongService() {
        database = new SongDAO();
    }

    public SongService(SongDAO databaseAccessor) {
        database = databaseAccessor;
    }

    @Override
    public Song create(Map model) {

        System.out.println("SongService: create - " + model);

        if (model == null || model.size() != 5) {
            return null;
        }

        String filename = (String) model.get("filename");
        List parts = Arrays.asList(filename.split("\\."));
        String extension = (String) parts.get(parts.size() - 1);

        Song song = new Song(
                (String) model.get("title"),
                (String) model.get("artist"),
                (String) model.get("duration"),
                (String) model.get("playlistId")
        );

        song.setFilepath(song.getId() + "." + extension);
        return database.create(song);
    }

    @Override
    public Song read(String id) {
        return database.findById(id);
    }

    /**
     * Reads as many songs by id as specified in the ids list.
     * @param ids   The ids of all the songs to read.
     * @return      List of Song objects.
     */
    public List<Song> readMultiple(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return null;
        }

        List<Song> songs = new ArrayList();
        for (String id : ids) {
            songs.add(this.read(id));
        }
        return songs;
    }

    @Override
    public Song update(String id, Map model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(String id) {
        ///TODO: delete from filesystem too
        return database.delete(id);
    }

    @Override
    public List<Song> list(Map<String, Object> filter) {
        if (filter == null || filter.isEmpty()) {
            return database.findAll();
        }
        ///TODO: Implement filter: return value based on filer then update unit tests
        return null;
    }

    /**
     * Retrieve the ids from the songs in the playlist specified by the given id.
     * @param playlistId    The id to specify a playlist from whihc to retrieve songs.
     * @return              A list of ids linked to songs in the playlist.
     */
    public List<String> songIDs(String playlistId) {
        System.out.println("Getting songs ids for playlist: " + playlistId);
        return database.findIDsByPlaylist(playlistId);
    }
}
