package com.project.vert_backend.service;

import com.project.vert_backend.auth.BasicAuth;
import com.project.vert_backend.auth.PasswordHasher;
import com.project.vert_backend.model.User;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Selwyn Lehmann
 *
 * Service that interacts with the database for all User related functionality, such as
 * authentication, creating and editing of user information
 */
public class UserService extends GuidModelService<User> {

    ///TODO replace with database driver
    private final TempRepository repository;

    public UserService() {
        repository = TempRepository.getInstance();
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
        User user = findByUsername(username);
        if (user == null) {
            return null;
        }

        /// Check password for match against User's stored password hash
        boolean valid = false;
        try {
            valid = PasswordHasher.validatePasswordAgainstHash(password, user.getPasswordHash());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.out.println("UserService: Exception - " + ex);
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
     * Adds a new User to database. Password and username are required fields.
     * @param aGuidModel    The new User to create
     * @return              The new User
     */
    @Override
    public User create(Map model) {
        User user;
        try {
            ///TODO ensure we check if the username already exists
            String[] loginInfo = {(String) model.get("username"), (String) model.get("password")};
            user = new User(
                    (String) model.get("name"),
                    (String) model.get("email"),
                    (String) model.get("username"),
                    (String) PasswordHasher.createHashedPassword((String) model.get("password")),
                    (String) BasicAuth.encode(loginInfo));

            return (User) repository.create(user);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger("UserService: Exception - " + ex);
            user = null;
        }
        return user;
    }

    /**
     * Retrieves User information from database. NOTE: This method takes a User's ID as parameter.
     * For finding user by username, use UserService::findByUsername().
     * @param id    The id of the User
     * @return      A User
     */
    @Override
    public User read(String id) {
        return (User) repository.read(new User(), id);
    }

    /**
     * Retrieves a User from the database based on their username.
     * @param username
     * @return
     */
    User findByUsername(String username) {
        System.out.println("UserService: Find " + username);

        ///TODO: Once filters are implemented, apply a filter for the required username to this list operation.
        List<User> users = repository.list(new User(), null);
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }

        System.out.println("UserService: ERROR - User not found");
        return null;
    }

    /**
     * TODO: Implement Update user method. Retrieves current user information and makes changes that are present
     *       in the new User object.
     * @param aGuidModel    The new User information.
     * @return              The updated User
     */
    @Override
    public User update(String id, Map model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * TODO: Implement Delete user method. Removes the User from the database.
     * @param id    The id of the User to remove.
     * @return      The deleted User
     */
    @Override
    public User delete(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * TODO: Implement List to list the users in the database according to the filter criteria.
     * @param filter    A parameter on which to filter current users
     * @return          All users that match the filter
     */
    @Override
    public List<User> list(Map<String, Object> filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
