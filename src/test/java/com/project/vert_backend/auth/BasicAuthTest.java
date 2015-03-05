package com.project.vert_backend.auth;

import javax.xml.bind.DatatypeConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Selwyn Lehmann
 */
public class BasicAuthTest {

    @Before
    public void setup() {

    }

    @Test
    public void decodeNullTest() {
        String[] result = BasicAuth.decode(null);
        Assert.assertNull(result);
    }

    @Test
    public void decodeEmptyTest() {
        String[] result = BasicAuth.decode("");
        Assert.assertNull(result);
    }

    @Test
    public void decodeNoAuthBytesTest() {
        String[] result = BasicAuth.decode("Basic");
        Assert.assertNull(result);
    }

    @Test
    public void decodeNoSemiColonTest() {
        String authUsername = "username";
        String authPassword = "password";
        String auth = authUsername + "-anyting-but-a-semi-colon-" + authPassword;

        String[] result = BasicAuth.decode(convertToBasicAuth(auth));
        Assert.assertNull(result);
    }

    @Test
    public void decodeValidAuthTest() {
        //Basic
        String authUsername1 = "username";
        String authPassword1 = "password";
        String auth1 = authUsername1 + ":" + authPassword1;

        String[] basicResult = BasicAuth.decode(convertToBasicAuth(auth1));
        Assert.assertNotNull("Basic result", basicResult);
        Assert.assertTrue("Basic username", basicResult[0].equals(authUsername1));
        Assert.assertTrue("Basic password", basicResult[1].equals(authPassword1));

        //Complex
        String authUsername2 = "my_u$3r ;";
        String authPassword2 = "P@$$w0rd: (6-%~=+_)";
        String auth2 = authUsername2 + ":" + authPassword2;

        String[] complexResult = BasicAuth.decode(convertToBasicAuth(auth2));
        Assert.assertNotNull("Complex result", complexResult);
        Assert.assertTrue("Complex username", complexResult[0].equals(authUsername2));
        Assert.assertTrue("Complex password", complexResult[1].equals(authPassword2));
    }

    @Test
    public void encodeNullCrentialsTest() {
        String result = BasicAuth.encode(null);
        Assert.assertNull(result);
    }

    @Test
    public void encodeNullUsernameTest() {
        String result = BasicAuth.encode(new String[]{null, "password"});
        Assert.assertNull(result);
    }

    @Test
    public void encodeNullPasswordTest() {
        String result = BasicAuth.encode(new String[]{"username", null});
        Assert.assertNull(result);
    }

    @Test
    public void encodeEmptyUsernameTest() {
        String result = BasicAuth.encode(new String[]{"", "password"});
        Assert.assertNull(result);
    }

    @Test
    public void encodeEmptyPasswordTest() {
        String result = BasicAuth.encode(new String[]{"username", ""});
        Assert.assertNull(result);
    }

    @Test
    public void encodeNotEnoughCrendentialsTest() {
        String result = BasicAuth.encode(new String[]{"username"});
        Assert.assertNull("One credential", result);

        String result1 = BasicAuth.encode(new String[]{});
        Assert.assertNull("No credential", result1);
    }

    @Test
    public void encodeTooManyCredentialsTest() {
        String result = BasicAuth.encode(new String[]{"username", "password", "extra"});
        Assert.assertNull("Third credential is non-empty string", result);

        String result2 = BasicAuth.encode(new String[]{"username", "password", ""});
        Assert.assertNull("Third credential is empty string", result2);

        String result3 = BasicAuth.encode(new String[]{"username", "password", null});
        Assert.assertNull("Third credential is null", result3);

        String result4 = BasicAuth.encode(new String[]{"username", "password", "extra1", "extra2", "extra3"});
        Assert.assertNull("Five credentials", result4);
    }

    @Test
    public void encodeUsernameContainColonTest() {
        String result1 = BasicAuth.encode(new String[]{"username:colon", "password"});
        Assert.assertNull("Colon in middle", result1);

        String result2 = BasicAuth.encode(new String[]{"usernamecolon:", "password"});
        Assert.assertNull("Colon at end", result2);

        String result3 = BasicAuth.encode(new String[]{":usernamecolon", "password"});
        Assert.assertNull("Colon in front", result3);

        String result4 = BasicAuth.encode(new String[]{":username:colon:", "password"});
        Assert.assertNull("Colon multiple", result4);
    }

    @Test
    public void encodeAndDecodePasswordContainColonTest() {
        String username = "username";
        String password = "password:colon";

        String result1 = BasicAuth.encode(new String[]{username, password});
        Assert.assertNotNull("Colon in middle", result1);

        String[] result2 = BasicAuth.decode(result1);
        Assert.assertNotNull("Decode of colon in password", result2);
        Assert.assertTrue(result2[0].equalsIgnoreCase(username));
        Assert.assertTrue(result2[1].equalsIgnoreCase(password));
    }

    @Test
    public void encodeValidAuthTest() {
        //Basic
        String username1 = "username";
        String password1 = "password";

        String basicResult = BasicAuth.encode(new String[]{username1, password1});
        String basicExpected = convertToBasicAuth(username1 + ":" + password1);
        System.out.println("Basic result: " + basicResult);
        Assert.assertNotNull("Basic result null", basicResult);
        Assert.assertTrue("Basic result same", basicResult.equals(basicExpected));

        //Complex
        String username2 = "my_u$3r ;";
        String password2 = "P@$$w0rd: (6-%~=+_)";

        String complexResult = BasicAuth.encode(new String[]{username2, password2});
        String complexExpected = convertToBasicAuth(username2 + ":" + password2);
        Assert.assertNotNull("Complex result null", complexResult);
        Assert.assertTrue("Complex result same", complexResult.equals(complexExpected));
    }

    /**
     * Convert value to Basic authentication token
     */
    String convertToBasicAuth(String value) {
        return "Basic " + DatatypeConverter.printBase64Binary(value.getBytes());
    }
}
