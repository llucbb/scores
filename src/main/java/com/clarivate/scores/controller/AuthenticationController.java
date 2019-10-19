package com.clarivate.scores.controller;

import com.clarivate.scores.model.AuthRequest;
import com.clarivate.scores.model.AuthResponse;
import com.clarivate.scores.service.SessionKeyService;
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
     * @return The {@link AuthResponse} entity with the sessionKey and the required 202 (Accepted) HTTP status code.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUserName(),
                authRequest.getPassword()));
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUserName());
        String sessionKey = jwtSessionKeyService.generateSessionKey(userDetails);
        return new ResponseEntity<>(new AuthResponse(sessionKey), new HttpHeaders(), HttpStatus.ACCEPTED);
    }
}
