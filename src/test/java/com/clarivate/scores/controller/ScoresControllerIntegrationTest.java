package com.clarivate.scores.controller;

import com.clarivate.scores.config.AuthRequestFilter;
import com.clarivate.scores.model.HighestScore;
import com.clarivate.scores.model.Score;
import com.clarivate.scores.repository.ScoresRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ScoresControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Autowired
    private ScoresRepository scoresRepository;

    @Test
    public void testAddLevelScore() throws Exception {
        String username = "user1";
        String sessionKey = getSessionKey(username, "changeit1");
        int level = 3;
        int scoreValue = 333;

        MvcResult mvcResult = mockMvc.perform(
                put(String.format("/level/%s/score/%s", level, scoreValue)).header(AuthRequestFilter.SESSION_KEY, sessionKey))
                .andExpect(status().isNoContent()).andReturn();
        Score score = scoresRepository.findByLevelUserAndScore(level, username, scoreValue).get(0);
        assertTrue(StringUtils.isBlank(mvcResult.getResponse().getContentAsString()));
        assertNotNull(score);
        assertEquals(level, score.getLevel());
        assertEquals(username, score.getUserName());
        assertEquals(scoreValue, score.getScore());
    }

    @Test
    public void testAddLevelScoreUnauthorized() throws Exception {
        String username = "user1";
        int level = 3;
        int scoreValue = 0;

        MvcResult mvcResult = mockMvc.perform(
                put(String.format("/level/%s/score/%s", level, scoreValue)))
                .andExpect(status().isUnauthorized()).andReturn();

        assertTrue(StringUtils.isBlank(mvcResult.getResponse().getContentAsString()));
        List<Score> score = scoresRepository.findByLevelUserAndScore(level, username, scoreValue);
        assertTrue(score.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetScoresFiltered() throws Exception {
        int level = 3;

        MvcResult mvcResult = mockMvc.perform(
                get(String.format("/level/%s/score", level)).param("filter", "highestscore"))
                .andExpect(status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assert.assertNotNull(response);
        //TODO unfinished
    }

}
