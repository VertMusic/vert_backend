package com.project.vert_backend.auth;

import javax.xml.bind.DatatypeConverter;

/**
 * @author Selwyn Lehmann
 */
public class BasicAuth {

    /**
     * Decodes the Basic authorization from Base64.
     * @param auth String with format "Basic base_64", where base_64 is
     *             a Base64 representation of a String "username:password"
     * @return     String[] containing [user_name , password]
     */
    public static String[] decode(String rawAuth) {

        /// Return null if rawAuth  does not exist.
        if (rawAuth == null || rawAuth.equalsIgnoreCase("")) {
            System.out.println("BasicAuth: No authorization headers found");
            return null;
        }

        /// Remove prefix "Basic" from auth string
        String auth = rawAuth.replaceFirst("[B|b]asic", "");

        /// Decode remaining Base64 string into a byte array, then check for failure.
        byte[] authBytes = DatatypeConverter.parseBase64Binary(auth);
        if (authBytes == null || authBytes.length == 0) {
            System.out.println("BasicAuth: Unable to decode authorization Base64");
            return null;
        }

        /// Convert byte array into String
        String authString = new String(authBytes);

        /// Split string on semi-colon to separate username and password
        String[] authTokens = authString.split(":", 2);

        ///Should contain 2 parts the user name and the password.
        if (authTokens.length != 2) {
            return null;
        }

        /// Return array where username is at position 0, and password at position 1
        return authTokens;
    }

    /**
     * Encodes an array with authentication credentials into an Basic Authorization token.
     * @param credentials   Where username is at position 0, and password is at position 1.
     * @return              String formated "Basic base64" where base64 is the base64 representation of
     *                      "username:password"
     */
    public static String encode(String[] credentials) {

        if (credentials == null || credentials.length != 2
                || credentials[0] == null || credentials[0].equalsIgnoreCase("") || credentials[0].contains(":")
                || credentials[1] == null || credentials[1].equalsIgnoreCase("")) {
            return null;
        }

        String basic = "Basic ";
        String authString = credentials[0] + ":" + credentials[1];

        /// Convert credential string to a byte array and encode with Base64
        byte[] authBytes = authString.getBytes();
        String base64 = DatatypeConverter.printBase64Binary(authBytes);

        return basic + base64;
    }
}
