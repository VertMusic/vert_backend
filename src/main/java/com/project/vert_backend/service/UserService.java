package com.project.vert_backend.service;

import com.project.vert_backend.auth.BasicAuth;
import com.project.vert_backend.auth.PasswordHasher;
import com.project.vert_backend.database.UserDAO;
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

    private final UserDAO database;

    public UserService() {
        database = new UserDAO();
    }

    public UserService(UserDAO databaseAccessor) {
        database = databaseAccessor;
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
    public User create(Map model) throws Exception {

        System.out.println("UserService: create - " + model);

        /// If no data is passed in or the Map is empty, return null
        if (model == null) {
            return null;
        }

        /// Check if username already exists
        if (model.get("username") == null || database.findByUsername((String) model.get("username")) != null) {
            throw new Exception("User already exists or username not defined");
        }

        User user;
        try {
            ///TODO ensure we check if the username already exists
            String[] loginInfo = {(String) model.get("username"), (String) model.get("password")};
            user = new User(
                    (String) model.get("name"),
                    (String) model.get("email"),
                    (String) model.get("username"),
                    PasswordHasher.createHashedPassword((String) model.get("password")),
                    BasicAuth.encode(loginInfo));

            return database.create(user);

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
        System.out.println("UserService: Read " + id);
        return database.findById(id);
    }

    /**
     * Retrieves a User from the database based on their username.
     * @param username
     * @return
     */
    User findByUsername(String username) {
        System.out.println("UserService: Find " + username);
        User user = database.findByUsername(username);

        if (user == null) {
            System.out.println("UserService: ERROR - user not found by username '" + username + "'");
        }
        return user;
    }

    /**
     * Retrieves current user information and makes changes that are present in the new User object.
     * @param aGuidModel    The new User information.
     * @return              The updated User
     */
    @Override
    public User update(String id, Map model) {
        System.out.println("UserService: Update - " + model);

        if (id == null || id.equalsIgnoreCase("") || model == null || model.size() != 4) {
            return null;
        }

        ///Create new User object from new field values (without generating an ID since it already exists)
        User updatedUser = new User();
        updatedUser.setId(id);
        updatedUser.setName((String) model.get("name"));
        updatedUser.setEmail((String) model.get("email"));
        updatedUser.setUsername((String) model.get("username"));

        ///Set the password hash
        String[] loginInfo = {(String) model.get("username"), (String) model.get("password")};
        try {
            updatedUser.setPasswordHash(PasswordHasher.createHashedPassword((String) model.get("password")));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.out.println("UserService: Update User Exception - " + ex);
            return null;
        }

        ///Set credential token
        updatedUser.setCredentialToken(BasicAuth.encode(loginInfo));

        return database.update(id, updatedUser);
    }

    /**
     * Removes the User from the database.
     * @param id    The id of the User to remove.
     * @return      The deleted User
     */
    @Override
    public boolean delete(String id) {
         return database.delete(id);
    }

    /**
     * List the users in the database according to the filter criteria.
     * @param filter    A parameter on which to filter current users
     * @return          All users that match the filter
     */
    @Override
    public List<User> list(Map<String, Object> filter) {

        if (filter == null || filter.isEmpty()) {
            return database.findAll();
        }
        ///TODO: Implement filter: return value based on filer then update unit tests
        return null;
    }
}
