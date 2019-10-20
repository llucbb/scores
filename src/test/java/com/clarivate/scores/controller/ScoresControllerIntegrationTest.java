package com.clarivate.scores.controller;

import com.clarivate.scores.config.AuthRequestFilter;
import com.clarivate.scores.model.HighestScore;
import com.clarivate.scores.model.Score;
import com.clarivate.scores.repository.ScoresRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ScoresControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Autowired
    private ScoresRepository scoresRepository;

    @Test
    public void testAddLevelScore() throws Exception {
        String userName = "user1";
        String password = "changeit1";
        saveUser(userName, password);
        String sessionKey = getSessionKey(userName, password);
        int level = 3;
        int scoreValue = 333;

        MvcResult mvcResult = mockMvc.perform(
                put(String.format("/level/%s/score/%s", level, scoreValue)).header(AuthRequestFilter.SESSION_KEY, sessionKey))
                .andExpect(status().isNoContent()).andReturn();

        assertTrue(StringUtils.isBlank(mvcResult.getResponse().getContentAsString()));
        List<Score> scores = scoresRepository.findByLevelUserAndScore(level, userName, scoreValue);
        assertNotNull(scores);
        assertEquals(1, scores.size());
        Score score = scores.get(0);
        assertEquals(level, score.getLevel());
        assertEquals(userName, score.getUserName());
        assertEquals(scoreValue, score.getScore());
    }

    @Test
    public void testAddLevelScoreSessionKeyExpired() throws Exception {
        String userName = "user1";
        String password = "changeit1";
        saveUser(userName, password);
        String sessionKey = getSessionKey(userName, password);
        // Wait just a bit more than sessionKeyExpiration property
        Thread.sleep(2500);
        int level = 3;
        int scoreValue = 333;

        MvcResult mvcResult = mockMvc.perform(
                put(String.format("/level/%s/score/%s", level, scoreValue)).header(AuthRequestFilter.SESSION_KEY, sessionKey))
                .andExpect(status().isUnauthorized()).andReturn();

        assertTrue(StringUtils.isBlank(mvcResult.getResponse().getContentAsString()));
        List<Score> scores = scoresRepository.findByLevelUserAndScore(level, userName, scoreValue);
        assertNotNull(scores);
        assertEquals(0, scores.size());
    }

    @Test
    public void testAddLevelScoreUnauthorized() throws Exception {
        String userName = "user1";
        String password = "changeit1";
        saveUser(userName, password);
        int level = 3;
        int scoreValue = 0;

        MvcResult mvcResult = mockMvc.perform(
                put(String.format("/level/%s/score/%s", level, scoreValue)))
                .andExpect(status().isUnauthorized()).andReturn();

        assertTrue(StringUtils.isBlank(mvcResult.getResponse().getContentAsString()));
        List<Score> score = scoresRepository.findByLevelUserAndScore(level, userName, scoreValue);
        assertTrue(score.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetScoresFiltered() throws Exception {
        scoresRepository.save(new Score(1, "user1", 1));
        scoresRepository.save(new Score(1, "user1", 5));
        scoresRepository.save(new Score(1, "user2", 2));
        scoresRepository.save(new Score(1, "user2", 6));
        scoresRepository.save(new Score(1, "user3", 1));
        scoresRepository.save(new Score(2, "user1", 10));
        scoresRepository.save(new Score(3, "user3", 10));
        int level = 1;

        MvcResult mvcResult = mockMvc.perform(
                get(String.format("/level/%s/score", level)).param("filter", "highestscore"))
                .andExpect(status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assert.assertNotNull(response);
        HighestScore[] userScores = new ObjectMapper().readValue(response, HighestScore[].class);
        assertEquals(3, userScores.length);
        for (HighestScore score : userScores) {
            switch (score.getUserName()) {
                case "user1":
                    assertEquals(5, score.getScore());
                    break;
                case "user2":
                    assertEquals(6, score.getScore());
                    break;
                case "user3":
                    assertEquals(1, score.getScore());
                    break;
                default:
                    fail();
            }
        }
    }

}
