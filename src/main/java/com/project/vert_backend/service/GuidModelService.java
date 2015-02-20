package com.project.vert_backend.service;

import com.project.vert_backend.model.GuidModel;
import java.util.List;
import java.util.Map;

/**
 * @author Selwyn Lehmann
 */
public abstract class GuidModelService<T extends GuidModel> {

    public abstract T create(Map model);

    public abstract T read(String id);

    public abstract T update(String id, Map model);

    public abstract T delete(String id);

    public abstract List<T> list(Map<String, Object> filter);
}
