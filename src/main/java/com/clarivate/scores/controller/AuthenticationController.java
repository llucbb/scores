package com.clarivate.scores.controller;

import com.clarivate.scores.model.AuthRequest;
import com.clarivate.scores.model.AuthResponse;
import com.clarivate.scores.service.JwtSessionKeyService;
import com.clarivate.scores.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication endpoint in order to provide unique sessionKey valid for use for a defined period by other endpoints.
 */
@RestController
@CrossOrigin
public class AuthenticationController {

    public static final String AUTH_PATH = "/login";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtSessionKeyService jwtSessionKeyService;

    @Autowired
    private UserService userDetailsService;

    /**
     * Returns a unique session key which is valid for use for a defined period by other endpoints.
     *
     * @param authRequest The {@link AuthRequest} with the username and password
     * @return The {@link AuthResponse} entity with the sessionKey and the required 202 (Accepted) HTTP status code.
     */
    @PostMapping(value = AUTH_PATH)
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUserName(),
                authRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUserName());
        String sessionKey = jwtSessionKeyService.generateSessionKey(userDetails);
        return new ResponseEntity<>(new AuthResponse(sessionKey), new HttpHeaders(), HttpStatus.ACCEPTED);
    }
}
