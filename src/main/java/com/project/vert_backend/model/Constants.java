package com.project.vert_backend.model;

import com.project.vert_backend.controller.PropertiesController;

/**
 *
 * @author Selwyn Lehmann
 */
public class Constants {
    public static final String IP_ADDRESS = PropertiesController.getInstance().getProperty("server.url");
}
