package com.project.vert_backend.service;

import com.project.vert_backend.auth.BasicAuth;
import com.project.vert_backend.auth.PasswordHasher;
import com.project.vert_backend.model.User;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Selwyn Lehmann
 *
 * Service that interacts with the database for all User related functionality, such as
 * authentication, creating and editing of user information
 */
public class UserService extends GuidModelService<User> {

    /// TODO: Move to database eventually
    final Map<String, User> users;

    public UserService() {
        this.users = new HashMap();

        /// Insert placeholder test users "dev" and "admin"
        try {
            String[] dev = {"dev", "password"};
            String[] admin = {"admin", "1234"};

            this.users.put(dev[0],
                    new User(dev[0], PasswordHasher.createHashedPassword(dev[1]), BasicAuth.encode(dev)));
            this.users.put(admin[0],
                    new User(admin[0], PasswordHasher.createHashedPassword(admin[1]), BasicAuth.encode(dev)));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a User based on provided username and password combination.
     * @param username  login credential to identify a User
     * @param password  login credential to authenticate a User
     * @return
     */
    public User authenticate(String username, String password) {

        if (username == null || password == null) {
            return null;
        }

        /// Retrieve user by username provided
        User user = this.users.get(username);
        if (user == null) {
            return null;
        }

        /// Check password for match against User's stored password hash
        boolean valid = false;
        try {
            valid = PasswordHasher.validatePasswordAgainstHash(password, user.getPasswordHash());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        /// Return the user if the password provided is a match
        if (valid) {
            return user;
        } else {
            return null;
        }
    }

    /**
     * TODO: Implement Create user method. Adds User to database.
     * @param aGuidModel    The new User to create
     * @return
     */
    @Override
    public User create(User model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * TODO: Implement Read user method. Retrieves User information from database.
     * @param id    The id of the User
     * @return
     */
    @Override
    public User read(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * TODO: Implement Update user method. Retrieves current user information and makes changes that are present
     *       in the new User object.
     * @param aGuidModel    The new User information.
     * @return
     */
    @Override
    public User update(User model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * TODO: Implement Delete user method. Removes the User from the database.
     * @param id    The id of the User to remove.
     * @return
     */
    @Override
    public User delete(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * TODO: Implement List to list the users in the database according to the filter criteria.
     * @param filter
     * @return
     */
    @Override
    public List<User> list(Map<String, Object> filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
