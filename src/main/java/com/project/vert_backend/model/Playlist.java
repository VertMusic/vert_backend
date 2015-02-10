package com.project.vert_backend.model;

/**
 * @author Selwyn Lehmann
 */
public class Playlist extends GuidModel {

    private String name;
    private String author;
    private String date;

    public Playlist() {
        super();
        name = "";
        author = "";
        date = "";
    }

    public Playlist(String aName, String anAuthor, String aDate) {
        super();
        name = aName;
        author = anAuthor;
        date = aDate;
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("<");
        result.append("name: ").append(name)
                .append(", author: ").append(author)
                .append(", date: ").append(date)
                .append(", id: ").append(super.getId())
                .append(">");

        return result.toString();
    }
}
