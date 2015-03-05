package com.project.vert_backend.auth;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Selwyn Lehmann
 */
public class PasswordHasherTest {
    @Before
    public void setup() {

    }

    @Test
    public void createHashNullTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String result = PasswordHasher.createHashedPassword(null);
        Assert.assertNull(result);
    }

    @Test
    public void createHashEmptyTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String result = PasswordHasher.createHashedPassword("");
        Assert.assertNull(result);
    }

    @Test
    public void createHashColonTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String result1 = PasswordHasher.createHashedPassword("long:password");
        Assert.assertNotNull("Colon in middle", result1);

        String result2 = PasswordHasher.createHashedPassword(":long:password");
        Assert.assertNotNull("Colon in front", result2);

        String result3 = PasswordHasher.createHashedPassword("long:password:");
        Assert.assertNotNull("Colon at end", result3);
    }

    @Test
    public void createValidHashTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String result1 = PasswordHasher.createHashedPassword("1234567890");
        Assert.assertNotNull("Numbers", result1);

        String result2 = PasswordHasher.createHashedPassword("abcdefghijklmnopqrstuvwxyz");
        Assert.assertNotNull("Letters", result2);

        String result3 = PasswordHasher.createHashedPassword("~`!@#$%^&*()_+-={}|[]:;'<>?,./'\\");
        Assert.assertNotNull("Special chars", result3);

        String result4 = PasswordHasher.createHashedPassword(" ");
        Assert.assertNotNull("Space", result4);
    }

    @Test
    public void validatePasswordNullTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
        boolean result = PasswordHasher.validatePasswordAgainstHash(null, null);
        Assert.assertFalse(result);
    }

    @Test
    public void validatePasswordEmptyTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String hash1 = PasswordHasher.createHashedPassword("");
        boolean result1 = PasswordHasher.validatePasswordAgainstHash("", hash1);
        Assert.assertFalse(result1);
    }

    @Test
    public void validatePasswordMatchTest() throws NoSuchAlgorithmException, InvalidKeySpecException {

        String hash2 = PasswordHasher.createHashedPassword("password");
        boolean result2 = PasswordHasher.validatePasswordAgainstHash("password", hash2);
        Assert.assertTrue(result2);

        String hash3 = PasswordHasher.createHashedPassword("p@ss/r/nw0rd");
        boolean result3 = PasswordHasher.validatePasswordAgainstHash("p@ss/r/nw0rd", hash3);
        Assert.assertTrue(result3);

        String hash4 = PasswordHasher.createHashedPassword("p@ss w0rd:*&%!~-=_+\\;?");
        boolean result4 = PasswordHasher.validatePasswordAgainstHash("p@ss w0rd:*&%!~-=_+\\;?", hash4);
        Assert.assertTrue(result4);
    }

    @Test
    public void validatePasswordMismatchTest() throws NoSuchAlgorithmException, InvalidKeySpecException {

        String hash1 = PasswordHasher.createHashedPassword("");
        boolean result1 = PasswordHasher.validatePasswordAgainstHash(" ", hash1);
        Assert.assertFalse(result1);

        String hash2 = PasswordHasher.createHashedPassword("password");
        boolean result2 = PasswordHasher.validatePasswordAgainstHash("pass word", hash2);
        Assert.assertFalse(result2);

        String hash3 = PasswordHasher.createHashedPassword("p@ss/r/nw0rd");
        boolean result3 = PasswordHasher.validatePasswordAgainstHash("not-p@ss/r/nw0rd", hash3);
        Assert.assertFalse(result3);

        String hash4 = PasswordHasher.createHashedPassword("p@ss w0rd:*&%!~-=_+\\;?");
        boolean result4 = PasswordHasher.validatePasswordAgainstHash("p@ss w0rd:*&%!~-=_+\\;?- not", hash4);
        Assert.assertFalse(result4);
    }
}
