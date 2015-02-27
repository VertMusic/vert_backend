package com.project.vert_backend.model;

/**
 * @author Selwyn Lehmann
 */
public class Playlist extends GuidModel {

    private String name;
    private String author;
    private String date;
    private String visibility;
    private int likes;

    public Playlist() {
        super();
        name = "";
        author = "";
        date = "";
        visibility = "";
        likes = 0;
    }

    public Playlist(String aName, String anAuthor, String aDate) {
        super();
        name = aName;
        author = anAuthor;
        date = aDate;
        ///TODO: add these as constructor parameters once needed
        visibility = "private";
        likes = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String anAuthor) {
        author = anAuthor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String aDate) {
        date = aDate;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String aVisibility) {
        visibility = aVisibility;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int aLikes) {
        likes = aLikes;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("<");
        result.append("name: ").append(name)
                .append(", author: ").append(author)
                .append(", date: ").append(date)
                .append(", visibility: ").append(visibility)
                .append(", likes: ").append(likes)
                .append(", id: ").append(super.getId())
                .append(">");

        return result.toString();
    }
}
