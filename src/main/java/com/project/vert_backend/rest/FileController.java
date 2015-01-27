package com.project.vert_backend.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author Selwyn Lehmann
 *
 * The FileController is the RESTful endpoint to which songs are uploaded and streamed.
 */
@Path("/file")
public class FileController {

    @POST
    @Path("/upload")
    public Response upload() {
        System.out.println("FileController: Received upload request...");
        return Response.ok().build();
    }

    @GET
    @Path("/stream")
    public Response stream() {
        System.out.println("FileController: Received stream request...");
        return Response.ok().build();
    }

    @GET
    @Path("/download")
    public Response download() {
        System.out.println("FileController: Received download request...");
        return Response.ok().build();
    }
}
