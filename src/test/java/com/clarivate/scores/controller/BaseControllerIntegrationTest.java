package com.clarivate.scores.controller;

import com.clarivate.scores.model.AuthRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BaseControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    String getSessionKey(String userName, String password) throws Exception {
        AuthRequest authRequest = new AuthRequest(userName, password);
        String jsonAuthRequest = new ObjectMapper().writeValueAsString(authRequest);
        MvcResult mvcResult = mockMvc.perform(
                post("/login").contentType(MediaType.APPLICATION_JSON).content(jsonAuthRequest))
                .andExpect(status().isAccepted()).andReturn();
        return mvcResult.getResponse().getContentAsString();
    }
}
