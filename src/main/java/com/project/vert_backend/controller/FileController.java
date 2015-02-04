package com.project.vert_backend.controller;

import com.project.vert_backend.model.User;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * @author Selwyn Lehmann
 *
 * FileController handles all file requests such as songs and images.
 * TODO: These methods still need additional parameter and url information such as specific ID's
 */
@Path("/file")
public class FileController {

    @Context
    HttpServletRequest servletRequest;

    @GET
    @Path("/download/song")
    public Response downloadSong() {
        System.out.println("FileController: Received GET song request from " + servletRequest.getAttribute(User.LOGGED_USER) + "...");
        return Response.ok().build();
    }

    @POST
    @Path("/upload/song")
    public Response uploadSong() {
        System.out.println("FileController: Received POST song request from " + servletRequest.getAttribute(User.LOGGED_USER) + "...");
        return Response.ok().build();
    }

    @GET
    @Path("/download/image")
    public Response downloadImage() {
        System.out.println("FileController: Received GET image request from " + servletRequest.getAttribute(User.LOGGED_USER) + "...");
        return Response.ok().build();
    }

    @POST
    @Path("/upload/image")
    public Response uploadImage() {
        System.out.println("FileController: Received POST image request from " + servletRequest.getAttribute(User.LOGGED_USER) + "...");
        return Response.ok().build();
    }
}
