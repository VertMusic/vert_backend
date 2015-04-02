package com.project.vert_backend.controller;

import com.project.vert_backend.model.Playlist;
import com.project.vert_backend.model.Song;
import com.project.vert_backend.model.User;
import com.project.vert_backend.service.PlaylistService;
import com.project.vert_backend.service.SongService;
import com.project.vert_backend.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
 *
 * This needs to happen for playlist, user, and song, and any other models we use.
 */
@Path("/data")
public class DataController {

    ///Services to retrieve and create specific model objects
    PlaylistService playlistService = new PlaylistService();
    UserService userService = new UserService();
    SongService songService = new SongService();

    /**
     * Retrieves a single playlist based on an identifier.
     * @param id        Identifier for the playlist which is being requested
     * @param request   Context of request that should have a User that is authenticated
     * @return          Returns a json object with information regarding a single playlist.
     *
     * The returned Json is formatted as follows:
     *      {
     *          playlist: {
     *              name:"Road Trip", date:"11-24-2014", author:"d2h", visibility:"private", likes:0
     *          }
     */
    @GET
    @Path("/playlists/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Playlist> getPlaylist(@PathParam("id") String id, @Context HttpServletRequest request) {
        System.out.println("DataController: Received GET playlist:" + id + " request from " + request.getAttribute(User.LOGGED_USER) + "...");
        Playlist playlist = playlistService.read(id);
        playlist.setSongs(songService.songIDs(playlist.getId()));

        Map<String, Playlist> playlistMap = new HashMap();
        playlistMap.put("playlist", playlist);
        System.out.println("DataController: Result - " + playlistMap);
        return playlistMap;
    }

    /**
     * Retrieves a set of all playlists currently in existence.
     * TODO: Allow query parameters to be passed to specify and filter on only certain playlist criteria
     * @param request   Context of request that should have a User that is authenticated
     * @return          Returns json that contains a list of playlist objects.
     *
     *  This endpoint returns Json formatted as follows:
     *      {
     *          playlists: [
     *              {name:"Road Trip", date:"11-24-2014", author:"d2h", visibility:"private", likes:0},
     *              {name:"CMT Top 30", date:"03-10-2010", author:"fmrsTan92", visibility:"private", likes:0},
     *              {name:"Rock Anthem", date:"02-24-1999", author:"def_cat", visibility:"private", likes:0}
     *          ]
     *      }
     */
    @GET
    @Path("/playlists")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<Playlist>> getPlaylists(@Context HttpServletRequest request) {
        System.out.println("DataController: Received GET playlists request from " + request.getAttribute(User.LOGGED_USER) + "...");

        Map filter = new HashMap();
        User user = (User) request.getAttribute(User.LOGGED_USER);
        filter.put("author", user.getUsername());

        List<Playlist> playlists = playlistService.list(filter);
        for (Playlist playlist : playlists) {
            playlist.setSongs(songService.songIDs(playlist.getId()));
        }

        Map<String, List<Playlist>> playlistsMap = new HashMap();
        playlistsMap.put("playlists", playlists);

        System.out.println("DataController: Result - " + playlistsMap);
        return playlistsMap;
    }

    /**
     * Creates a new empty playlist.
     * @param request   Context of request that should have a User that is authenticated
     * @return          Returns json that contains the creates playlist object.
     *
     * This endpoint accepts Json formatted in the following way:
     *      {
     *          playlist: {
     *              name:"Road Trip",
     *              date:"11-24-2014",
     *              author:"d2h",
     *              visibility: "private"
     *          }
     *      }
     *  This endpoint returns Json formatted as follows:
     *      {
     *          playlist: {
     *              id:"123-adb-1243",
     *              name:"Road Trip",
     *              date:"11-24-2014",
     *              author:"d2h",
     *              visibility: "private",
     *              likes: 0
     *          }
     *      }
     */
    @POST
    @Path("/playlists")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Playlist> createPlaylist(Map playlistMap, @Context HttpServletRequest request) {
        System.out.println("DataController: Received POST playlists request from " + request.getAttribute(User.LOGGED_USER) + "...");

        Playlist playlist = playlistService.create((Map) playlistMap.get("playlist"));
        Map<String, Playlist> playlistsMap = new HashMap();
        playlistsMap.put("playlist", playlist);

        System.out.println("DataController: Result - " + playlistsMap);
        return playlistsMap;
    }

    /**
     * Updates an existing playlist with new field values.
     * @param request   Context of request that should have a User that is authenticated
     * @return          Returns json that contains the creates playlist object.
     *
     * This endpoint accepts Json formatted in the following way:
     *      {
     *          playlist: {
     *              name:"Road Trip",
     *              date:"11-24-2014",
     *              author:"d2h",
     *              visibility: "private",
     *              likes: 0
     *          }
     *      }
     *  This endpoint returns Json formatted as follows:
     *      {
     *          playlist: {
     *              id:"123-adb-1243",
     *              name:"Road Trip",
     *              date:"11-24-2014",
     *              author:"d2h",
     *              visibility: "private",
     *              likes: 0
     *          }
     *      }
     */
    @PUT
    @Path("/playlists/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Playlist> cupdatePlaylist(@PathParam("id") String id, Map playlistMap, @Context HttpServletRequest request) {
        System.out.println("DataController: Received PUT playlists: " + id + " request from " + request.getAttribute(User.LOGGED_USER) + "...");

        Playlist playlist = playlistService.update(id, (Map) playlistMap.get("playlist"));
        Map<String, Playlist> playlistsMap = new HashMap();
        playlistsMap.put("playlist", playlist);

        System.out.println("DataController: Result - " + playlistsMap);
        return playlistsMap;
    }

    /**
     * Retrieves a single song based on an identifier.
     * @param id        Identifier for the song which is being requested
     * @param request   Context of request that should have a User that is authenticated
     * @return          Returns a json object with information regarding a single song.
     *
     * The returned Json is formatted as follows:
     *      {
     *          song: {
     *              artist:"Kiss",title:"Unholy",duration:"2:38",playlistId;"123-abc",filepath:"/songs/789-higk.mp3"
     *          }
     */
    @GET
    @Path("/songs/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Song> getSong(@PathParam("id") String id, @Context HttpServletRequest request) {
        System.out.println("DataController: Received GET song:" + id + " request from " + request.getAttribute(User.LOGGED_USER) + "...");
        Song song = songService.read(id);

        Map<String, Song> songMap = new HashMap();
        songMap.put("song", song);
        System.out.println("DataController: Result - " + songMap);
        return songMap;
    }

    /**
     * Retrieves a set of all songs that are identified by the query params.
     * @param ids       List of song ids to be retrieved.
     * @param request   Context of request that should have a User that is authenticated
     * @return          Returns json that contains a list of song objects.
     *
     *  This endpoint returns Json formatted as follows:
     *      {
     *          songs: [
     *              {artist:"Kiss",title:"Unholy",duration:"2:38",playlistId;"123-abc",filepath:"/songs/789-higk.mp3"},
     *              {artist:"Drowning Pool",title:"Sinner",duration:"3:47",playlistId;"123-abc",filepath:"/songs/981-hkl.mp3"},
     *              {artist:"Avenged Sevenfold",title:"Scream",duration:"4:16",playlistId;"123-abc",filepath:"/songs/128-alt.mp3"}
     *          ]
     *      }
     */
    @GET
    @Path("/songs")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<Song>> getSongs(@QueryParam("ids[]") final List<String> ids, @Context HttpServletRequest request) {
        System.out.println("DataController: Received GET songs request from " + request.getAttribute(User.LOGGED_USER) + "...");
        System.out.println("DataController: Query parameters - " + ids);

        List<Song> songs;
        if (ids == null || ids.isEmpty()) {
            songs = songService.list(null);
        } else {
            songs = songService.readMultiple(ids);
        }

        Map<String, List<Song>> songsMap = new HashMap();
        songsMap.put("songs", songs);

        System.out.println("DataController: Result - " + songsMap);
        return songsMap;
    }

    /**
     * Method endpoint that allows a new user to be created.
     * @param userMap   Map that contains user information
     * @param request   Context of request that should not have a User since one is being created
     * @return          Returns an object that contains a user with access token and user id, for
     *                  future authentication.
     *
     * This endpoint accepts Json formatted in the following way:
     *      {
     *          user: {
     *              name: "John Doe",
     *              username: "jDoe89",
     *              email: "john.doe@gmail.com",
     *              password: "mypass1234"
     *          }
     *      }
     * This endpoint returns Json formatted in the following way:
     *      {
     *          user: {
     *              id: "1245-df-453-isdfdsl",
     *              name: "John Doe",
     *              username: "jDoe89",
     *              email: "john.doe@gmail.com",
     *              password: "",
     *              accessToken: "3209864hffif=adgh32k869999uoi"
     *          }
     *      }
     */
    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Map> createUser(Map userMap, @Context HttpServletRequest request) {
        System.out.println("DataController: Creating - " + userMap);

        User newUser = userService.create((Map) userMap.get("user"));

        ///Object containing the user's id and access token
        Map user = new HashMap();
        user.put("id", newUser.getId());
        user.put("accessToken", newUser.getCredentialToken());
        user.put("password", "");
        user.put("name", newUser.getName());
        user.put("username", newUser.getUsername());
        user.put("email", newUser.getEmail());

        //// Label the apiKey object to meet Ember convention
        Map resultMap = new HashMap();
        resultMap.put("user", user);

        System.out.println("DataController: Returning - " + resultMap);

        return resultMap;
    }

    /**
     * Retrieves information for a single user by using its identifier
     * @param id        Identification number of the user who's information is requested.
     * @param request   Context of request that should  have a User that is authenticated
     * @return          Returns a json object containing the users information
     *
     *  This endpoint returns Json formatted as follows:
     *      {
     *          user: {
     *              id: "1245-df-453-isdfdsl",
     *              name: "John Doe",
     *              username: "jDoe89",
     *              email: "john.doe@gmail.com",
     *              password: "",
     *              accessToken: ""
     *          }
     *      }
     */
    @GET
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, User> getUser(@PathParam("id") String id, @Context HttpServletRequest request) {
        System.out.println("DataController: Received POST users request from " + request.getAttribute(User.LOGGED_USER) + "...");
        System.out.println("DataController: Reading user - " + id);

        User user = userService.read(id);
        if (user == null) {
            System.out.println("DataController: ERROR - user not found");
        }

        Map resultsMap = new HashMap();
        resultsMap.put("user", user);

        return resultsMap;
    }

    /**
     * Method endpoint that allows a user to authenticate.
     * @param loginMap  A Json object containing a username and password
     * @param request   Context of request that should not have a User since they are
     *                  attempting to authenticate.
     * @return          Returns a json object that contains session information.
     *
     * This endpoint accepts Json formatted in the following way:
     *      {
     *          session : {
     *              username: "jDoe89",
     *              password: "mypass1234"
     *          }
     *      }
     * This endpoint returns Json formatted in the following way:
     *      {
     *          session : {
     *              id: "1245-df-453-isdfdsl",
     *              accessToken: "3209864hffif=adgh32k869999uoi"
     *          }
     *      }
     */
    @POST
    @Path("/session")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Map> loginUser(Map sessionMap, @Context HttpServletRequest request) {
        Map session = new HashMap();

        Map loginInfo = (Map) sessionMap.get("session");
        if (loginInfo != null) {
            User loginUser = userService.authenticate(
                    (String) loginInfo.get("username"),
                    (String) loginInfo.get("password"));

            if (loginUser != null) {
                session.put("userId", loginUser.getId());
                session.put("accessToken", loginUser.getCredentialToken());
                System.out.println("DataController: Login info found for " + loginUser.getId());

                /// We have valid login information to start a new session
                Map resultMap = new HashMap();
                resultMap.put("session", session);
                System.out.println("DataController: Valid session - " + resultMap);
                return resultMap;
            } else {
                System.out.println("DataController: ERROR - Login info does not correlate to user in system.");
            }
        } else {
            System.out.println("DataController: ERROR - Login info not found in session");
        }

        /// When we get here it means that we do not have valid login information
        System.out.println("DataController: Throw unauthorized exception (401) - " + sessionMap);
        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
}
