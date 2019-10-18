package com.clarivate.scores.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface SessionKeyService {

    String generateSessionKey(UserDetails userDetails);

    String getUsernameFromSessionKey(String sessionKey);

    boolean isSessionKeyValid(String sessionKey, UserDetails userDetails);
}
