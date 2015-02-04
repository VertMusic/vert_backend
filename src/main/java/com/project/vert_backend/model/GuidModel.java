package com.project.vert_backend.model;

import java.util.UUID;

/**
 * @author Selwyn Lehmann
 */
public abstract class GuidModel {

    private final String id;

    public GuidModel() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }
}
