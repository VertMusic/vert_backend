package com.project.vert_backend.controller;

import com.project.vert_backend.auth.BasicAuth;
import com.project.vert_backend.model.User;
import com.project.vert_backend.service.UserService;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;

/**
 * @author Selwyn Lehmann
 *
 * Filters incoming server requests for authorization.
 */
public class AuthFilterController implements ContainerRequestFilter {

    @Context
    private HttpServletRequest servletRequest;
    private UserService userService;

    public AuthFilterController() {
        this.userService = new UserService();
    }

    /**
     * Apply the authentication filter to the incoming ContainerRequest
     * @param request is the incoming ContainerRequest
     * @return
     */
    @Override
    public ContainerRequest filter(ContainerRequest containerRequest) {

        System.out.println("AuthFilterController: Filtering request for Basic authorization....");

        /// Store the request method (e.g. GET, POST, PUT, or DELETE)
        String method = containerRequest.getMethod();

        /// Store the request path (e.g. "/vert/song/upload")
        String path = containerRequest.getPath(true);

        System.out.println("AuthFilterController: Method=" + method + " Path=" + path);

        /// Allow access to the WADL (Web Application Description Language) which
        /// describes the structure of the web application's resources.
        if (method.equals("GET") && (path.equals("application.wadl") || path.equals("application.wadl/xsd0.xsd"))) {
            System.out.println("AuthFilterController: Access granted to WADL");
            return containerRequest;
        } else if (method.equals("POST") && path.endsWith("data/users")) {
            System.out.println("AuthFilterController: Access granted to registering User");
            return containerRequest;
        } else if (method.equals("POST") && path.endsWith("data/session")) {
            System.out.println("AuthFilterController: Access granted to login for user");
            return containerRequest;
        }

        /// Get the authentication passed in the HTTP headers, if it does not
        /// exist then throw an unauthorized exception
        String auth = containerRequest.getHeaderValue("authorization");
        String[] loginInfo = BasicAuth.decode(auth);
        if (loginInfo == null || loginInfo.length != 2) {
            System.out.println("AuthFilterController: Unauthorized access - no login info found");
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }

        /// Retrieve user from the database, and check if one exists under the provided auth info
        User user = userService.authenticate(loginInfo[0], loginInfo[1]);
        if (user == null) {
            System.out.println("AuthFilterController: Unauthorized access - no match for user/password");
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }

        /// Set user context in HttpServletRequest
        servletRequest.setAttribute(User.LOGGED_USER, user);
        System.out.println("AuthFilterController: Authorized access - " + user);
        return containerRequest;
    }

}
