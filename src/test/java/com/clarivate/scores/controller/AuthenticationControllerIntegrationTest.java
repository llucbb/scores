package com.clarivate.scores.controller;

import com.clarivate.scores.model.AuthRequest;
import com.clarivate.scores.service.SessionKeyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionKeyService jwtSessionKeyService;

    @Test
    public void testLoginAccepted() throws Exception {
        AuthRequest authRequest = new AuthRequest("user1", "changeit1");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(authRequest);

        MvcResult mvcResult = mockMvc.perform(
                post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isAccepted()).andReturn();

        String sessionKey = mvcResult.getResponse().getContentAsString();
        assertNotNull(sessionKey);
        UserDetails userDetails = new User(authRequest.getUsername(), authRequest.getPassword(), new ArrayList<>());
        assertTrue(jwtSessionKeyService.isSessionKeyValid(sessionKey, userDetails));
    }

    @Test
    public void testLoginUnauthorized() throws Exception {
        AuthRequest authRequest = new AuthRequest("user1", "wrong");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(authRequest);

        MvcResult mvcResult = mockMvc.perform(
                post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isUnauthorized()).andReturn();

        String sessionKey = mvcResult.getResponse().getContentAsString();
        assertTrue(StringUtils.isBlank(sessionKey));
    }
}
