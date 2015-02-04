package com.project.vert_backend.controller;

import com.project.vert_backend.model.Playlist;
import com.project.vert_backend.model.User;
import com.project.vert_backend.service.PlaylistService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * @author Selwyn Lehmann
 *
 * The DataController is the RESTful endpoint to which JSON data is read, edited, and saved. This controller
 * is the main point of interaction with Ember Data to get models to back its templates.
 *
 * This Controller need to support the following methods to be compliant with the Ember Data Store REST Adapter.
 *      Method:     HTTP:   URL Path:
 *      Find        GET     /playlists/123
 *      Find All	GET     /playlists
 *      Update      PUT     /playlists/123
 *      Create      POST	/playlists
 *      Delete      DELETE	/playlists/123
 * This needs to happen for playlist, user, and song, and any other models we use.
 */
@Path("/data")
public class DataController {

    PlaylistService playlistService = new PlaylistService();

    @GET
    @Path("/playlists/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Playlist getPlaylist(@PathParam("id") String id, @Context HttpServletRequest request) {
        System.out.println("FileController: Received GET playlist:" + id + " request from " + request.getAttribute(User.LOGGED_USER) + "...");
        Playlist playlist = playlistService.read(id);

        System.out.println("FileController: Result - " + playlist);
        return playlist;
    }

    @GET
    @Path("/playlists")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<Playlist>> getPlaylists(@Context HttpServletRequest request) {
        System.out.println("FileController: Received GET playlists request from " + request.getAttribute(User.LOGGED_USER) + "...");

        List<Playlist> playlists = playlistService.list(null);
        Map<String, List<Playlist>> playlistsMap = new HashMap();
        playlistsMap.put("playlists", playlists);

        System.out.println("FileController: Result - " + playlistsMap);
        return playlistsMap;
    }
}
