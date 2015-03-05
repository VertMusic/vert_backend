package com.project.vert_backend.service;

import com.project.vert_backend.database.PlaylistDAO;
import com.project.vert_backend.model.Playlist;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Selwyn Lehmann
 */
public class PlaylistServiceTest {

    Playlist playlist1;
    Playlist playlist1Update;

    PlaylistDAO database;
    PlaylistService playlistService;

    @Before
    public void setup() {
        ///Mock database
        database = mock(PlaylistDAO.class);

        /// Inject the mock object (database accessor) into the playlist service
        playlistService = new PlaylistService(database);

        /// Create reusable test objects
        playlist1 = new Playlist(
                "Roadtrip Top 30",
                "john.doe",
                "02-02-2015",
                "private",
                0
        );

        playlist1Update = new Playlist(
                "Roadtrip Top 30",
                "john.doe",
                "02-02-2015",
                "public",
                0
        );
        playlist1Update.setLikes(1);

    }

    @Test
    public void readExistingPlaylistTest() {
        /// Setup return value for the mocked instance
        when(database.findById(playlist1.getId())).thenReturn(playlist1);

        /// Execute our test on the read method
        Playlist result = playlistService.read(playlist1.getId());

        /// Verify certain actions were executed in the read method
        verify(database).findById(playlist1.getId());

        /// Validate our test case result
        Assert.assertNotNull(result);
        Assert.assertEquals(playlist1, result);
    }

    @Test
    public void readNonExistingPlaylistTest() {
        /// Setup return value for the mocked instance
        /// In this case we return null because the empty playlist id is not found
        when(database.findById("")).thenReturn(null);
        when(database.findById(null)).thenReturn(null);

        /// Execute our test on the read method
        Playlist result1 = playlistService.read("");
        /// Verify certain actions were executed in the read method
        verify(database).findById("");
        /// Validate our test case result
        Assert.assertNull("Read empty id", result1);

        /// Execute our test on the read method
        Playlist result2 = playlistService.read(null);
        /// Verify certain actions were executed in the read method
        verify(database).findById(null);
        /// Validate our test case result
        Assert.assertNull("Read null id", result2);
    }

    @Test
    public void deletePlaylistTest() {
        when(database.delete(playlist1.getId())).thenReturn(true);

        boolean result = playlistService.delete(playlist1.getId());
        Assert.assertNotNull(result);
        Assert.assertTrue(result);
    }

    @Test
    public void deleteNonExistingPlaylistTest() {
        when(database.delete(null)).thenReturn(false);
        when(database.delete("")).thenReturn(false);

        boolean result1 = playlistService.delete(null);
        Assert.assertNotNull(result1);
        Assert.assertFalse("Null id for delete", result1);

        boolean result2 = playlistService.delete("");
        Assert.assertNotNull(result2);
        Assert.assertFalse("Empty id for delete", result2);
    }

    @Test
    public void createNewPlaylistTest() {
        /// Due to a unique id being assigned in the Playlist constructor we have to match
        /// on "any" user being passed to the database create method.
        when(database.create(any(Playlist.class)))
                .thenReturn(playlist1);

        Map newPlaylistMap = new HashMap();
        newPlaylistMap.put("name", "Roadtrip Top 30");
        newPlaylistMap.put("author", "john.doe");
        newPlaylistMap.put("date", "02-02-2015");
        newPlaylistMap.put("visibility", "private");
        newPlaylistMap.put("likes", 0);

        Playlist result = playlistService.create(newPlaylistMap);

        verify(database).create(any(Playlist.class));
        Assert.assertNotNull(result);
        Assert.assertEquals(playlist1, result);
    }

    @Test
    public void createPlaylistWithNoFieldsTest() {
        Playlist result1 = playlistService.create(null);
        ///Check that the database is never used when a null map with playlist data is passed to create()
        verify(database, never()).create(any(Playlist.class));
        Assert.assertNull("Create from null", result1);

        Playlist result2 = playlistService.create(new HashMap());
        ///Check that the database is never used when an empty map with playlist data is passed to create()
        verify(database, never()).create(any(Playlist.class));
        Assert.assertNull("Create from empty", result2);
    }

    @Test
    public void validUpdatePlaylistTest() {
        Map update = new HashMap();
        update.put("title", "Roadtrip Top 30");
        update.put("author", "john.doe");
        update.put("date", "02-02-2015");
        update.put("visibility", "public");
        update.put("likes", 1);

        when(database.update(any(String.class), any(Playlist.class)))
                .thenReturn(playlist1Update);

        Playlist result = playlistService.update(playlist1.getId(), update);
        Assert.assertNotNull(result);
        Assert.assertEquals(playlist1Update, result);
    }

    @Test
    public void invalidUpdatePlaylistTest() {
        ///Complete and valid update
        Map update = new HashMap();
        update.put("title", "Roadtrip Top 30");
        update.put("author", "john.doe");
        update.put("date", "02-02-2015");
        update.put("visibility", "public");
        update.put("likes", 1);

        ///Incomplete update, not all playlist fields are present
        Map incompleteUpdate = new HashMap();
        incompleteUpdate.put("visibility", "public");
        incompleteUpdate.put("likes", 1);

        ///Incomplete update data
        Playlist result1 = playlistService.update(playlist1.getId(), incompleteUpdate);
        Assert.assertNull("Update fields missing", result1);

        ///Empty playlist id
        Playlist result2 = playlistService.update("", update);
        Assert.assertNull("Empty id", result2);

        ///Null playlist id
        Playlist result3 = playlistService.update(null, update);
        Assert.assertNull("Null id", result3);

        ///Null update data
        Playlist result4 = playlistService.update(playlist1.getId(), null);
        Assert.assertNull("Null update", result4);
    }

    @Test
    public void listAllPlaylistsTest() {

        Playlist playlist2 = new Playlist(
                "Heavy Metal Rush",
                "john.doe",
                "02-10-2015",
                "private",
                0
        );

        Playlist playlist3 = new Playlist(
                "Slap Dem Beatz",
                "john.doe",
                "02-19-2015",
                "public",
                0
        );

        List playlists = new ArrayList();
        playlists.add(playlist1);
        playlists.add(playlist2);
        playlists.add(playlist3);

        when(database.findAll()).thenReturn(playlists);

        List<Playlist> result1 = playlistService.list(null);
        List<Playlist> result2 = playlistService.list(new HashMap());

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertEquals(playlists, result1);
        Assert.assertEquals(playlists, result2);
    }
}
