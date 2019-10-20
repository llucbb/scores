package com.clarivate.scores.controller;

import com.clarivate.scores.model.AuthRequest;
import com.clarivate.scores.service.SessionKeyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Autowired
    private SessionKeyService jwtSessionKeyService;

    @Test
    public void testLoginAccepted() throws Exception {
        final String userName = "user1";
        final String password = "changeit1";
        saveUser(userName, password);

        String sessionKey = getSessionKey(userName, password);

        assertNotNull(sessionKey);
        UserDetails userDetails = new User(userName, password, new ArrayList<>());
        assertTrue(jwtSessionKeyService.isSessionKeyValid(sessionKey, userDetails));
        assertEquals(userName, jwtSessionKeyService.getUsernameFromSessionKey(sessionKey));
    }

    @Test
    public void testLoginUnauthorized() throws Exception {
        final String userName = "user5";
        final String password = "changeit5";
        AuthRequest authRequest = new AuthRequest(userName, password);
        String jsonAuthRequest = new ObjectMapper().writeValueAsString(authRequest);

        MvcResult mvcResult = mockMvc.perform(
                post("/login").contentType(MediaType.APPLICATION_JSON).content(jsonAuthRequest))
                .andExpect(status().isUnauthorized()).andReturn();

        String sessionKey = mvcResult.getResponse().getContentAsString();
        assertTrue(StringUtils.isBlank(sessionKey));
    }
}
