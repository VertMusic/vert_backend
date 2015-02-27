package com.project.vert_backend.model;

/**
 * @author Selwyn Lehmann
 */
public class Song extends GuidModel {

    private String title;
    private String artist;
    private String duration;
    private String playlistId;
    private String filepath;

    public Song() {
        title = "";
        artist = "";
        duration = "";
        playlistId = "";
        filepath = "";
    }

    public Song(String aTitle, String anArtist, String aDuration, String aPlaylistId, String aFilename) {
        super();
        title = aTitle;
        artist = anArtist;
        duration = aDuration;
        playlistId = aPlaylistId;
        filepath = aFilename;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String aDuration) {
        duration = aDuration;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String aPlaylistId) {
        playlistId = aPlaylistId;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String aFilename) {
        filepath = aFilename;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("<");
        result.append("title: ").append(title)
                .append(", artist: ").append(artist)
                .append(", filepath").append(filepath)
                .append(", playlistId: ").append(playlistId)
                .append(", id: ").append(super.getId())
                .append(">");

        return result.toString();
    }
}
