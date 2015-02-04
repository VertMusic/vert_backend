package com.project.vert_backend.model;

/**
 * @author Selwyn Lehmann
 */
public class User extends GuidModel {

    public static final String LOGGED_USER = "logged_user";
    private final String username;
    private String passwordHash;
    private String credentialToken; /// Base64 of "Basic username:password" for Basic auth filtering

    public User(String aUsername, String aPasswordHash, String aCredentialToken) {
        super();
        username = aUsername;
        passwordHash = aPasswordHash;
        credentialToken = aCredentialToken;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String aPasswordHash) {
        passwordHash = aPasswordHash;
    }

    public String getCredentialToken() {
        return credentialToken;
    }

    public void setCredentialToken(String aCredentialToken) {
        credentialToken = aCredentialToken;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("<");
        result.append("username: ").append(username)
                .append(", id: ").append(super.getId())
                .append(">");

        return result.toString();
    }
}
