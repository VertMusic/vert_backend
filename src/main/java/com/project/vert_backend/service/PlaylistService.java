package com.project.vert_backend.service;

import com.project.vert_backend.model.Playlist;
import java.util.ArrayList;
import java.util.Collections;
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

    /// TODO: Move to database.
    private final List<Playlist> playlists;

    public PlaylistService() {
        playlists = new ArrayList();
        playlists.add(new Playlist("Road Trip", "d2h", "11-5-2013"));
        playlists.add(new Playlist("Rock Anthem", "def_cat", "02-24-1992"));
        playlists.add(new Playlist("Hip Hop Party", "d2h", "08-05-2014"));
        playlists.add(new Playlist("CMT Top 30", "fmrsTan92", "01-29-2015"));
    }

    @Override
    public Playlist create(Playlist model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Playlist read(String id) {
        /// TODO: Perform database query that finds a Playlist with the id
        for (Playlist playlist : playlists) {
            if (id.equalsIgnoreCase(playlist.getId())) {
                return playlist;
            }
        }
        return null;
    }

    @Override
    public Playlist update(Playlist model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Playlist delete(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Playlist> list(Map<String, Object> filter) {
        if (filter == null || filter.isEmpty()) {
            return Collections.unmodifiableList(playlists);
        } else {
            /// TODO: Handle filtering capability on all Playlist fields (e.g. {"author":"d2h"} returns playlists by d2h)
            return Collections.unmodifiableList(playlists);
        }
    }

}
