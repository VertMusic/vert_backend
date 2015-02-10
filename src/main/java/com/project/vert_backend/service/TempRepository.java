package com.project.vert_backend.service;

import com.project.vert_backend.auth.BasicAuth;
import com.project.vert_backend.auth.PasswordHasher;
import com.project.vert_backend.model.GuidModel;
import com.project.vert_backend.model.Playlist;
import com.project.vert_backend.model.User;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Selwyn Lehmann
 * Temporary place to store data until we have a database. Singleton class.
 */
public class TempRepository<T extends GuidModel> {

    /// TODO: Move to database.
    private volatile ConcurrentLinkedQueue<Playlist> playlists;
    private volatile ConcurrentHashMap<String, User> users;

    private static TempRepository instance;

    private TempRepository() {
        ///Insert placeholder test playlists
        playlists = new ConcurrentLinkedQueue();
        playlists.add(new Playlist("Road Trip", "d2h", "11-5-2013"));
        playlists.add(new Playlist("Rock Anthem", "def_cat", "02-24-1992"));
        playlists.add(new Playlist("Hip Hop Party", "d2h", "08-05-2014"));
        playlists.add(new Playlist("CMT Top 30", "fmrsTan92", "01-29-2015"));

        /// Insert placeholder test users "dev" and "admin"
        users = new ConcurrentHashMap();
        try {
            String[] devLogin = {"dev", "password"};
            String[] adminLogin = {"admin", "1234"};

            User dev = new User(
                    "John Doe",
                    "jDoe@yahoo.com",
                    devLogin[0],
                    PasswordHasher.createHashedPassword(devLogin[1]),
                    BasicAuth.encode(devLogin));

            User admin = new User(
                    "Tom Jones",
                    "tom.jones@gmail.com",
                    adminLogin[0],
                    PasswordHasher.createHashedPassword(adminLogin[1]),
                    BasicAuth.encode(adminLogin));

            users.put(devLogin[0], dev);
            users.put(adminLogin[0], admin);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.out.println("TempRepository Exception: " + ex);
        }
    }

    /**
     * Retrieves Singleton instance - thread safe.
     * @return
     */
    public static TempRepository getInstance() {
        ///Uses double-checked locking to provide thread safe instance that only blocks when
        // the instance has not yet been created (to prevent creating more than one instance)
        if (instance == null) {
            synchronized (TempRepository.class) {
                if (instance == null) {
                    instance = new TempRepository();
                }
            }
        }
        return instance;
    }

    public synchronized T read(T type, String id) {
        System.out.println("TempRepository: Reading " + id + " from persistence store...");

        if (type instanceof Playlist) {
            System.out.println("TempRepository: Type - Playlist");
            for (Playlist p : playlists) {
                if (p.getId().equalsIgnoreCase(id)) {
                    return (T) p;
                }
            }
        } else if (type instanceof User) {
            System.out.println("TempRepository: Type - User");
            for (User u : users.values()) {
                if (u.getId().equalsIgnoreCase(id)) {
                    return (T) u;
                }
            }
        }
        System.out.println("TempRepository: Not found - " + id);
        return null;
    }

    public synchronized T create(T model) {
        System.out.println("TempRepository: Create instance of " + model);

        if (model instanceof Playlist) {
            System.out.println("TempRepository: Type - Playlist");
            Playlist p = (Playlist) model;
            playlists.add(p);
        } else if (model instanceof User) {
            System.out.println("TempRepository: Type - User");
            User u = (User) model;
            users.put(u.getUsername(), u);
        }
        return model;
    }

    public synchronized T update(String guid, T model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public synchronized T delete(T type, String guid) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public synchronized List<T> list(T type, Map<String, Object> filters) {
        System.out.println("TempRepository: List instance of " + type + "(filters: " + filters + ")");

        if (type instanceof Playlist) {
            System.out.println("TempRepository: Type - Playlist");
            List<Playlist> list = new ArrayList();
            for (Playlist p : playlists) {
                list.add(p);
            }
            return (List<T>) list;
        } else if (type instanceof User) {
            System.out.println("TempRepository: Type - User");
            List<User> list = new ArrayList();
            for (User u : users.values()) {
                list.add(u);
            }
            return (List<T>) list;
        } else {
            System.out.println("TempRepository: ERROR - Unsupported type " + type);
            return null;
        }
    }

    public void clearAll() {
        users.clear();
        playlists.clear();
        System.out.println("TempRepository: All repos cleared");
    }
}
