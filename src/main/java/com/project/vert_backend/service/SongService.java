package com.project.vert_backend.service;

import com.project.vert_backend.database.SongDAO;
import com.project.vert_backend.model.Song;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Selwyn Lehmann
 * Service that interacts with the database regarding all Song functionality.
 */
public class SongService extends GuidModelService<Song> {

    SongDAO database = new SongDAO();

    @Override
    public Song create(Map model) {
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Song update(String id, Map model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Song> list(Map<String, Object> filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
