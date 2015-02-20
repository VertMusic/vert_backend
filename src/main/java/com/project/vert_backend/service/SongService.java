package com.project.vert_backend.service;

import com.project.vert_backend.model.Song;
import java.util.List;
import java.util.Map;

/**
 * @author Selwyn Lehmann
 * Service that interacts with the database regarding all Song functionality.
 */
public class SongService extends GuidModelService<Song> {

    @Override
    public Song create(Map model) {
        throw new UnsupportedOperationException("Not supported yet.");
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
