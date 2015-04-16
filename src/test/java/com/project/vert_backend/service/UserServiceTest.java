package com.project.vert_backend.service;

import com.project.vert_backend.auth.BasicAuth;
import com.project.vert_backend.auth.PasswordHasher;
import com.project.vert_backend.database.UserDAO;
import com.project.vert_backend.model.User;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.exceptions.base.MockitoException;

/**
 * @author Selwyn Lehmann
 */
public class UserServiceTest {

    User user1;
    User user1Update;
    String[] loginInfo1;

    UserDAO database;
    UserService userService;

    @Before
    public void setup() throws InvalidKeySpecException, NoSuchAlgorithmException {
        ///Mock database
        database = mock(UserDAO.class);

        /// Inject the mock object (database accessor) into the user service
        userService = new UserService(database);

        /// Create reusable test objects
        loginInfo1 = new String[]{"john.doe", "p@$$word"};
        user1 = new User(
                "John Doe",
                "jdoe@gmail.com",
                loginInfo1[0],
                PasswordHasher.createHashedPassword(loginInfo1[1]),
                BasicAuth.encode(loginInfo1)
        );
        user1Update = new User(
                "John Doe Jr.",
                "jdoe@yahoo.com",
                loginInfo1[0],
                PasswordHasher.createHashedPassword(loginInfo1[1]),
                BasicAuth.encode(loginInfo1)
        );
        user1Update.setId(user1.getId());
    }

    @Test
    public void readExistingUserTest() {

        /// Setup return value for the mocked instance
        when(database.findById(user1.getId())).thenReturn(user1);

        /// Execute our test on the read method
        User result = userService.read(user1.getId());

        /// Verify certain actions were executed in the read method
        verify(database).findById(user1.getId());

        /// Validate our test case result
        Assert.assertNotNull(result);
        Assert.assertEquals(user1, result);
    }

    @Test
    public void readNonExistingUserTest() {

        /// Setup return value for the mocked instance
        /// In this case we return null because the empty userid is not found
        when(database.findById("")).thenReturn(null);
        when(database.findById(null)).thenReturn(null);

        /// Execute our test on the read method
        User result1 = userService.read("");
        /// Verify certain actions were executed in the read method
        verify(database).findById("");
        /// Validate our test case result
        Assert.assertNull("Read empty id", result1);

        /// Execute our test on the read method
        User result2 = userService.read(null);
        /// Verify certain actions were executed in the read method
        verify(database).findById(null);
        /// Validate our test case result
        Assert.assertNull("Read null id", result2);
    }

    @Test
    public void authenticationValidTest() {
        /// Setup return value for the mocked instance
        when(database.findByUsername(loginInfo1[0]))
                .thenReturn(user1);

        /// Execute our test on the authenticate method with correct password
        User result = userService.authenticate(loginInfo1[0], loginInfo1[1]);

        /// Verify certain actions were executed in the authenticate method
        verify(database).findByUsername(loginInfo1[0]);

        /// Validate our test case result
        Assert.assertNotNull(result);
        Assert.assertEquals(user1, result);
    }

    @Test
    public void authenticationInvalidTest() {
        /// Setup return values for the mocked instance
        when(database.findByUsername(loginInfo1[0]))
                .thenReturn(user1);
        when(database.findByUsername("no-exist"))
                .thenReturn(null);

        /// Execute our test on the authenticate method with wrong password
        User result1 = userService.authenticate(loginInfo1[0], "password");
        /// Verify certain actions were executed in the authenticate method
        verify(database).findByUsername(loginInfo1[0]);
        /// Validate our test case result
        Assert.assertNull("Wrong password", result1);

        /// Execute our test on the authenticate method with wrong username
        User result2 = userService.authenticate("no-exist", loginInfo1[1]);
        /// Verify certain actions were executed in the authenticate method
        verify(database).findByUsername("no-exist");
        /// Validate our test case result
        Assert.assertNull("Wrong username", result2);
    }

    @Test
    public void createNewUserTest() {
        try {
            /// Due to a unique id being assigned in the User constructor we have to match
            /// on "any" user being passed to the database create method.
            when(database.create(any(User.class)))
                    .thenReturn(user1);

            Map newUserMap = new HashMap();
            newUserMap.put("name", "John Doe");
            newUserMap.put("email", "j.doe@gmail.com");
            newUserMap.put("username", "john.doe");
            newUserMap.put("password", "p@$$word");

            User result = userService.create(newUserMap);

            verify(database).create(any(User.class));
            Assert.assertNotNull(result);
            Assert.assertEquals(user1, result);
        } catch (Exception ex) {
            Logger.getLogger(UserServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void createEmptyUserTest() {
        try {
            User result1 = userService.create(null);
            ///Check that the database is never used when a null map with user data is passed to create()
            verify(database, never()).create(any(User.class));
            Assert.assertNull("Create from null", result1);

            User result2 = userService.create(new HashMap());
            ///Check that the database is never used when a empty map with user data is passed to create()
            Assert.assertNull("Create from empty", result2);
        } catch (Exception ex) {
            Logger.getLogger(UserServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test(expected = MockitoException.class)
    public void createNewUserException() {
        //Throw an exception to test the try-catch surrounding the creation of a new user
        when(database.create(any(User.class))).thenThrow(new NoSuchAlgorithmException());

        Map newUserMap = new HashMap();
        newUserMap.put("name", "John Doe");
        newUserMap.put("email", "j.doe@gmail.com");
        newUserMap.put("username", loginInfo1[0]);
        newUserMap.put("password", loginInfo1[1]);

        User result = null;
        try {
            result = userService.create(newUserMap);
        } catch (Exception ex) {
            Logger.getLogger(UserServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        ///Result is null when exception is thrown
        Assert.assertNull(result);
    }

    @Test
    public void findByValidUsernameTest() {
        /// Setup return value for the mocked instance
        when(database.findByUsername(loginInfo1[0]))
                .thenReturn(user1);

        /// Execute our test on the findByUsername method
        User result = userService.findByUsername(loginInfo1[0]);

        /// Verify certain actions were executed in the findByUsername method
        verify(database).findByUsername(loginInfo1[0]);

        /// Validate our test case result
        Assert.assertNotNull(result);
        Assert.assertEquals(user1, result);
    }

    @Test
    public void findByInvalidUsernameTest() {
        /// Setup return value for the mocked instance
        when(database.findByUsername(null))
                .thenReturn(null);

        when(database.findByUsername(""))
                .thenReturn(null);

        /// Execute our test on the findByUsername method
        User result1 = userService.findByUsername(null);
        /// Verify certain actions were executed in the findByUsername method
        verify(database).findByUsername(null);
        /// Validate our test case result
        Assert.assertNull("Null username", result1);

        /// Execute our test on the findByUsername method
        User result2 = userService.findByUsername("");
        /// Verify certain actions were executed in the findByUsername method
        verify(database).findByUsername("");
        /// Validate our test case result
        Assert.assertNull("Empty username", result2);
    }

    @Test
    public void validUpdateUserTest() {

        Map update = new HashMap();
        update.put("name", "John Doe Jr.");
        update.put("email", "j.doe@yahoo.com");
        update.put("username", loginInfo1[0]);
        update.put("password", loginInfo1[1]);

        when(database.update(any(String.class), any(User.class)))
                .thenReturn(user1Update);

        User result = userService.update(user1.getId(), update);
        Assert.assertNotNull(result);
        Assert.assertEquals(user1Update, result);
    }

    @Test
    public void invalidUpdateUserTest() {
        ///Complete and valid update
        Map update = new HashMap();
        update.put("name", "John Doe Jr.");
        update.put("email", "j.doe@yahoo.com");
        update.put("username", loginInfo1[0]);
        update.put("password", loginInfo1[1]);

        ///Incomplete update, not all user fields are present
        Map incompleteUpdate = new HashMap();
        incompleteUpdate.put("name", "John Doe Jr.");
        incompleteUpdate.put("email", "j.doe@yahoo.com");

        ///Incomplete update data
        User result1 = userService.update(user1.getId(), incompleteUpdate);
        Assert.assertNull("Update fields missing", result1);

        ///Empty user name
        User result2 = userService.update("", update);
        Assert.assertNull("Empty username", result2);

        ///Null user name
        User result3 = userService.update(null, update);
        Assert.assertNull("Null username", result3);

        ///Null update data
        User result4 = userService.update(user1.getId(), null);
        Assert.assertNull("Null update", result4);
    }

    @Test
    public void validDeleteTest() {
        when(database.delete(user1.getId())).thenReturn(true);

        boolean result = userService.delete(user1.getId());
        Assert.assertNotNull(result);
        Assert.assertTrue(result);
    }

    @Test
    public void invalidDeleteTest() {
        when(database.delete(null)).thenReturn(false);
        when(database.delete("")).thenReturn(false);

        boolean result1 = userService.delete(null);
        Assert.assertNotNull(result1);
        Assert.assertFalse("Null id for delete", result1);

        boolean result2 = userService.delete("");
        Assert.assertNotNull(result2);
        Assert.assertFalse("Empty id for delete", result2);
    }

    @Test
    public void listAllUsersTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] loginInfo = new String[]{"dev", "p@$$word"};
        User user2 = new User(
                "Jane Doe",
                "jdoe@gmail.com",
                loginInfo1[0],
                PasswordHasher.createHashedPassword(loginInfo[1]),
                BasicAuth.encode(loginInfo)
        );
        User user3 = new User(
                "Bob Doe",
                "bdoe@gmail.com",
                loginInfo1[0],
                PasswordHasher.createHashedPassword(loginInfo[1]),
                BasicAuth.encode(loginInfo)
        );

        List userList = new ArrayList();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        when(database.findAll()).thenReturn(userList);

        List<User> result1 = userService.list(null);
        List<User> result2 = userService.list(new HashMap());

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertEquals(userList, result1);
        Assert.assertEquals(userList, result2);
    }
}
