package com.clarivate.scores.model;

import java.io.Serializable;

public class AuthRequest implements Serializable {

    private String username;
    private String password;

    public AuthRequest(String userName, String password) {
        this.username = userName;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
