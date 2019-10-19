package com.clarivate.scores.controller;

import com.clarivate.scores.model.AuthRequest;
import com.clarivate.scores.service.SessionKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication endpoint in order to provide unique session key valid for use for a defined period by other endpoints.
 */
@RestController
@CrossOrigin
public class AuthenticationController {

    private final static Logger LOG = LoggerFactory.getLogger(ScoresController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SessionKeyService jwtSessionKeyService;

    @Autowired
    private UserDetailsService userService;

    /**
     * Returns a unique session key which is valid for use for a defined period by other endpoints.
     *
     * @param authRequest The {@link AuthRequest} with the username and password
     * @return The session key (HTTP status code 202-Accepted)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()));
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String sessionKey = jwtSessionKeyService.generateSessionKey(userDetails);
        return new ResponseEntity<>(sessionKey, new HttpHeaders(), HttpStatus.ACCEPTED);
    }
}
