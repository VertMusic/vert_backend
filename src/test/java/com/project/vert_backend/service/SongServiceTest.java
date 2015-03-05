package com.project.vert_backend.service;

import com.project.vert_backend.database.SongDAO;
import com.project.vert_backend.model.Song;
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
public class SongServiceTest {
    Song song1;
    Song song2;
    Song song3;
    List<Song> songs;

    SongDAO database;
    SongService songService;

    @Before
    public void setup() {
        ///Mock database
        database = mock(SongDAO.class);

        /// Inject the mock object (database accessor) into the song service
        songService = new SongService(database);

        /// Create reusable test objects
        song1 = new Song(
                "Fake It",
                "Seether",
                "2:43",
                "abc-123"
        );

        song2 = new Song(
                "Unholy",
                "Kiss",
                "3:17",
                "abc-123"
        );

        song3 = new Song(
                "Don't Stop Believing",
                "Journey",
                "3:05",
                "abc-123"
        );

        songs = new ArrayList();
        songs.add(song1);
        songs.add(song2);
        songs.add(song3);
    }

    @Test
    public void readExistingSongTest() {
        /// Setup return value for the mocked instance
        when(database.findById(song1.getId())).thenReturn(song1);

        /// Execute our test on the read method
        Song result = songService.read(song1.getId());

        /// Verify certain actions were executed in the read method
        verify(database).findById(song1.getId());

        /// Validate our test case result
        Assert.assertNotNull(result);
        Assert.assertEquals(song1, result);
    }

    @Test
    public void readNonExistingSongTest() {
        /// Setup return value for the mocked instance
        /// In this case we return null because the empty song id is not found
        when(database.findById("")).thenReturn(null);
        when(database.findById(null)).thenReturn(null);

        /// Execute our test on the read method
        Song result1 = songService.read("");
        /// Verify certain actions were executed in the read method
        verify(database).findById("");
        /// Validate our test case result
        Assert.assertNull("Read empty id", result1);

        /// Execute our test on the read method
        Song result2 = songService.read(null);
        /// Verify certain actions were executed in the read method
        verify(database).findById(null);
        /// Validate our test case result
        Assert.assertNull("Read null id", result2);
    }

    @Test
    public void readMultipleSongsTest() {
        when(database.findById(song1.getId())).thenReturn(song1);
        when(database.findById(song2.getId())).thenReturn(song2);
        when(database.findById(song3.getId())).thenReturn(song3);

        List<String> ids = new ArrayList();
        ids.add(song1.getId());
        ids.add(song2.getId());
        ids.add(song3.getId());

        List<Song> result = songService.readMultiple(ids);
        verify(database).findById(song1.getId());
        verify(database).findById(song2.getId());
        verify(database).findById(song3.getId());

        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() == songs.size());
        Assert.assertTrue(result.containsAll(songs));
    }

    @Test
    public void readMultipleNonExistantSongsTest() {
        List<Song> result1 = songService.readMultiple(null);
        List<Song> result2 = songService.readMultiple(new ArrayList());

        Assert.assertNull(result1);
        Assert.assertNull(result2);
    }

    @Test
    public void deleteSongTest() {
        when(database.delete(song1.getId())).thenReturn(true);

        boolean result = songService.delete(song1.getId());
        Assert.assertNotNull(result);
        Assert.assertTrue(result);
    }

    @Test
    public void deleteNonExistingSongTest() {
        when(database.delete(null)).thenReturn(false);
        when(database.delete("")).thenReturn(false);

        boolean result1 = songService.delete(null);
        Assert.assertNotNull(result1);
        Assert.assertFalse("Null id for delete", result1);

        boolean result2 = songService.delete("");
        Assert.assertNotNull(result2);
        Assert.assertFalse("Empty id for delete", result2);
    }

    @Test
    public void createNewSongTest() {
        /// Due to a unique id being assigned in the Song constructor we have to match
        /// on "any" user being passed to the database create method.
        when(database.create(any(Song.class)))
                .thenReturn(song1);

        Map newSongMap = new HashMap();
        newSongMap.put("title", "Fake It");
        newSongMap.put("artist", "Seether");
        newSongMap.put("duration", "2:43");
        newSongMap.put("playlistId", "abc-123");
        newSongMap.put("filename", "song1.mp3");

        Song result = songService.create(newSongMap);

        verify(database).create(any(Song.class));
        Assert.assertNotNull(result);
        Assert.assertEquals(song1, result);
    }

    @Test
    public void createSongWithNoFieldsTest() {
        Song result1 = songService.create(null);
        ///Check that the database is never used when a null map with song data is passed to create()
        verify(database, never()).create(any(Song.class));
        Assert.assertNull("Create from null", result1);

        Song result2 = songService.create(new HashMap());
        ///Check that the database is never used when an empty map with song data is passed to create()
        verify(database, never()).create(any(Song.class));
        Assert.assertNull("Create from empty", result2);
    }

    @Test
    public void listAllSongsTest() {
        when(database.findAll()).thenReturn(songs);

        List<Song> result1 = songService.list(null);
        List<Song> result2 = songService.list(new HashMap());

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertEquals(songs, result1);
        Assert.assertEquals(songs, result2);
    }

    @Test
    public void listSongIDsTest() {
        List<String> ids = new ArrayList();
        for (Song song : songs) {
            ids.add(song.getId());
        }

        when(database.findIDsByPlaylist("abc-123")).thenReturn(ids);

        List<String> result = songService.songIDs("abc-123");
        Assert.assertNotNull(result);
        Assert.assertEquals(ids, result);
    }
}
