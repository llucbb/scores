package com.clarivate.scores.config;

import com.clarivate.scores.model.AuthRequest;
import com.clarivate.scores.service.SessionKeyService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.clarivate.scores.model.AuthResponse.SESSION_KEY;

/**
 * Single execution per request {@link OncePerRequestFilter} filter. Validates the request header with the session key
 * and performs the spring security user authorization in order to grant access to the secured endpoints.
 */
@Component
public class AuthRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userService;

    @Autowired
    private SessionKeyService jwtSessionKeyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        final String sessionKey = request.getHeader(SESSION_KEY);
        if (!StringUtils.isEmpty(sessionKey)) {
            try {
                String username = jwtSessionKeyService.getUsernameFromSessionKey(sessionKey);
                if (StringUtils.isEmpty(username)) {
                    logger.error(String.format("%s not found in the %s", AuthRequest.USER_NAME, SESSION_KEY));
                } else if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userService.loadUserByUsername(username);
                    authorize(request, sessionKey, userDetails);
                }
            } catch (IllegalArgumentException e) {
                logger.error(String.format("No able to get %s", SESSION_KEY), e);
            } catch (ExpiredJwtException e) {
                logger.error(String.format("%s has expired", SESSION_KEY), e);
            } catch (SignatureException e) {
                logger.error(String.format("%s signature is not valid", SESSION_KEY), e);
            }
        }

        chain.doFilter(request, response);
    }

    private void authorize(HttpServletRequest request, String sessionKey, UserDetails userDetails) {
        if (jwtSessionKeyService.isSessionKeyValid(sessionKey, userDetails)) {
            // If sessionKey is valid configure Spring Security to manually set authentication
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // After setting the Authentication in the context, we specify that the current user is
            // authenticated. So it passes the Spring Security Configurations successfully.
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
}
