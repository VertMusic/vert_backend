package com.project.vert_backend.model;

/**
 * @author Selwyn Lehmann
 */
public class User extends GuidModel {

    public static final String LOGGED_USER = "logged_user";
    private String username;
    private String name;
    private String email;
    private String passwordHash;
    private String credentialToken; /// Base64 of "Basic username:password" for Basic auth filtering

    public User() {
        super();
        name = "";
        email = "";
        username = "";
        passwordHash = "";
        credentialToken = "";
    }

    public User(String aName, String anEmail, String aUsername, String aPasswordHash, String aCredentialToken) {
        super();
        name = aName;
        email = anEmail;
        username = aUsername;
        passwordHash = aPasswordHash;
        credentialToken = aCredentialToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String anUsername) {
        username = anUsername;
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

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String anEmail) {
        email = anEmail;
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
