package com.project.vert_backend.controller;

import com.project.vert_backend.model.Song;
import com.project.vert_backend.model.User;
import com.project.vert_backend.service.FileService;
import com.project.vert_backend.service.SongService;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Selwyn Lehmann
 *
 * FileController handles all file requests such as songs and images.
 * TODO: These methods still need additional parameter and url information such as specific ID's
 */
@Path("/file")
public class FileController {

    private static final String SONG_BASE_PATH = "songs/";
    private static final String USER_IMAGE_BASE_PATH = "images/users/";

    private static final int BUFFER_SIZE = 4096; //4KB

    FileService fileService = new FileService();
    SongService songService = new SongService();

    @POST
    @Path("/song")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Map<String, Object> uploadSong(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("filename") FormDataBodyPart fileName,
            @FormDataParam("title") FormDataBodyPart title,
            @FormDataParam("artist") FormDataBodyPart artist,
            @FormDataParam("playlistId") FormDataBodyPart playlist,
            @Context HttpServletRequest servletRequest
    ) {

        System.out.println("FileController: Received song upload from " + servletRequest.getAttribute(User.LOGGED_USER));
        Map songModel = new HashMap();
        songModel.put("title", title.getValue());
        songModel.put("artist", artist.getValue());
        songModel.put("playlistId", playlist.getValue());
        songModel.put("filename", fileName.getValue());

        ///TODO: Calculate duration
        songModel.put("duration", "");

        Song song = songService.create(songModel);
        fileService.writeToFile(fileInputStream, SONG_BASE_PATH + song.getFilepath());

        songModel.put("id", song.getId());
        Map<String, Object> container = new HashMap();
        container.put("song", songModel);

        System.out.println("FileController: song upload successfull - " + container);

        return container;
    }

    @GET
    @Path("/song/{id}")
    public void downloadSong(@PathParam("id") String id, @Context HttpServletRequest servletRequest, @Context HttpServletResponse servletResponse) {

        System.out.println("FileController: Received GET song file request from " + servletRequest.getAttribute(User.LOGGED_USER) + "...");

        Song song = songService.read(id);
        System.out.println("FileController: Song filename - " + song.getFilepath());
        servletResponse.setHeader("Content-Type", "audio/mpeg");

        FileInputStream in = null;
        OutputStream out = null;

        try {
            out = servletResponse.getOutputStream();
            in = new FileInputStream(SONG_BASE_PATH + song.getFilepath());
            byte[] buffer = new byte[BUFFER_SIZE];
            int read = -1;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

        } catch (IOException ex) {
            System.out.println("FileController: File download exception - " + ex);
        } finally {
            try {
                out.close();
                out.flush();
                in.close();
            } catch (IOException ex) {
                System.out.println("FileController: File close exception - " + ex);
            }
        }
    }

    @GET
    @Path("/image/{id}")
    public Response downloadImage(@PathParam("id") String id, @Context HttpServletRequest servletRequest) {
        System.out.println("FileController: Received GET image request from " + servletRequest.getAttribute(User.LOGGED_USER) + "...");
        return Response.ok().build();
    }

    @POST
    @Path("/image")
    public Response uploadImage(@Context HttpServletRequest servletRequest) {
        System.out.println("FileController: Received POST image request from " + servletRequest.getAttribute(User.LOGGED_USER) + "...");
        return Response.ok().build();
    }
}
