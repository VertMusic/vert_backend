package com.project.vert_backend.model;

/**
 * @author Selwyn Lehmann
 */
public class Song extends GuidModel {

    private String title;
    private String artist;
    private String playlistId;
    private String filename;

    public Song(String aTitle, String anArtist, String aPlaylistId, String aFilename) {
        super();
        title = aTitle;
        artist = anArtist;
        playlistId = aPlaylistId;
        filename = aFilename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String aTitle) {
        title = aTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String anArtist) {
        artist = anArtist;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String aPlaylistId) {
        playlistId = aPlaylistId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String aFilename) {
        filename = aFilename;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("<");
        result.append("title: ").append(title)
                .append(", artist: ").append(artist)
                .append(", filename").append(filename)
                .append(", playlistId: ").append(playlistId)
                .append(", id: ").append(super.getId())
                .append(">");

        return result.toString();
    }
}
