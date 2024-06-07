package com.conectopia.conectopia.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String displayName;
    private String PlayToken;

    // singleton
    private static LoggedInUser instance = new LoggedInUser("","");

    public LoggedInUser(String PlayToken, String displayName) {
        this.PlayToken = PlayToken;
        this.displayName = displayName;
    }

    public String getPlayToken() {
        return PlayToken;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static LoggedInUser getInstance() {
        return instance;
    }
    public static void setInstance(LoggedInUser user) {
        instance = user;
    }
}