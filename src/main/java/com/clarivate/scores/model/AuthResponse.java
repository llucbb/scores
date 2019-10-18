package com.clarivate.scores.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class AuthResponse implements Serializable {

    public static final String SESSION_KEY = "Session-Key";

    @JsonProperty(SESSION_KEY)
    private final String sessionKey;

    public AuthResponse(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }
}
