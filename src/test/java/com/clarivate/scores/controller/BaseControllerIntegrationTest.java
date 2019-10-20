package com.clarivate.scores.controller;

import com.clarivate.scores.config.MongoConfig;
import com.clarivate.scores.model.AuthRequest;
import com.clarivate.scores.model.User;
import com.clarivate.scores.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(MongoConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Ignore
public class BaseControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    void saveUser(String userName, String password) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    String getSessionKey(String userName, String password) throws Exception {
        AuthRequest authRequest = new AuthRequest(userName, password);
        String jsonAuthRequest = new ObjectMapper().writeValueAsString(authRequest);
        MvcResult mvcResult = mockMvc.perform(
                post("/login").contentType(MediaType.APPLICATION_JSON).content(jsonAuthRequest))
                .andExpect(status().isAccepted()).andReturn();
        return mvcResult.getResponse().getContentAsString();
    }
}
