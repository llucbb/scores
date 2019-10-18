package com.clarivate.scores.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Generates an open standard JSON Web Token (JWT). JWT defines a compact and self-contained way to securely
 * generate the required unique-session-key. Contains a header, a payload, and a signature. The header defines the
 * type (JWT) and the hashing algorithm for the signature. The payload defines the issuer, the subject (username), when
 * has been issued and when expires. Finally, the signature is the hash of the header and the payload using the
 * hash algorithm, which can be verified and trusted because it is digitally signed.
 */
@Service
public class JwtSessionKeyService implements SessionKeyService {

    private final static Logger LOG = LoggerFactory.getLogger(JwtSessionKeyService.class);

    private static final long SESSION_KEY_EXPIRATION_MS = 600000; // 10min
    private static final String JWT = "JWT";
    private static final String ALG = "HS512";
    private static final String ISSUER = "clarivate";

    @Value("${jwt.signingKey}")
    private String signingKey;

    /**
     * Generate the unique session key, a JSON Web Token (JWT) in the form of a string.
     *
     * @param userDetails The {@link UserDetails} with the username
     * @return The session key, JWT compacted and URL-safe in string format
     */
    @Override
    public String generateSessionKey(UserDetails userDetails) {
        JwsHeader<?> header = Jwts.jwsHeader();
        header.setType(JWT);
        header.setAlgorithm(ALG);
        return Jwts.builder().setClaims(new HashMap<>())
                .setHeader((Map<String, Object>) header)
                .setSubject(userDetails.getUsername())
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SESSION_KEY_EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();
    }

    /**
     * Retrieve username for the given session key. Will be referenced with the score provided when adding scores per
     * level.
     *
     * @param sessionKey The session key, JWT compacted and URL-safe in string format
     * @return The username in string format
     */
    @Override
    public String getUsernameFromSessionKey(String sessionKey) {
        return getClaimFromSessionKey(sessionKey, Claims::getSubject);
    }

    private <T> T getClaimFromSessionKey(String sessionKey, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(sessionKey).getBody();
        return claimsResolver.apply(claims);
    }

    /**
     * Checks the expiration of the the given session key and validates if key is related with the given username.
     *
     * @param sessionKey  The session key, JWT compacted and URL-safe in string format
     * @param userDetails The {@link UserDetails} with the username
     * @return true if the session key is valid, false otherwise
     */
    @Override
    public boolean isSessionKeyValid(String sessionKey, UserDetails userDetails) {
        String userName = getUsernameFromSessionKey(sessionKey);
        return userName.equals(userDetails.getUsername()) && !isSessionKeyExpired(sessionKey);
    }

    private boolean isSessionKeyExpired(String sessionKey) {
        // Retrieve expiration date from JWT session key
        Date expiration = getClaimFromSessionKey(sessionKey, Claims::getExpiration);
        // And check if the session key has expired
        return expiration.before(new Date());
    }
}