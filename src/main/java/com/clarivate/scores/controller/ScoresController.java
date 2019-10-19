package com.clarivate.scores.controller;

import com.clarivate.scores.model.HighestScore;
import com.clarivate.scores.service.ScoresService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Scores endpoints in order to add scores per user/level and retrieves a list of scores filtered (user highest scores
 * per level)
 */
@RestController
@CrossOrigin
public class ScoresController {

    private final static Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private ScoresService defaultScoresService;

    /**
     * Adds a new level score for the authenticated user
     *
     * @param level          The level of the new score
     * @param score          The new user score per level
     * @param authentication The user of the new score
     * @return Nothing (HTTP status code 204-No Content)
     */
    @PutMapping("/level/{level}/score/{score}")
    public ResponseEntity addLevelScore(@PathVariable int level, @PathVariable int score, Authentication authentication) {
        LOG.debug(String.format("-> addLevelScore: level=%d, score=%d", level, score));
        // At this point, authentication must be performed at AuthRequestFilter, although it's better to be defensive
        if (authentication != null) {
            defaultScoresService.addLevelScore(authentication.getName(), level, score);
            LOG.debug("<- addLevelScore");
        } else {
            LOG.error("Authentication is null");
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves a list of scores filtered by:
     * * highestscore: Highest user scores per level
     *
     * @param level  The level of the scores to apply the filter
     * @param filter The type of filter to be applied
     * @return The list of the scores filtered depending on the filter parameter (HTTP status code 200-OK)
     */
    @GetMapping("/level/{level}/score")
    public ResponseEntity<?> getScoresFiltered(@PathVariable int level, @RequestParam String filter) {
        LOG.debug(String.format("-> getScoresFiltered: level=%d, filter=%s", level, filter));
        List<HighestScore> scores = new ArrayList<>();
        if (filter.equals("highestscore")) {
            scores = defaultScoresService.getHighestScoresPerLevel(level);
        }
        LOG.debug(String.format("<- getScoresFiltered: %s", scores));
        return new ResponseEntity<>(scores, new HttpHeaders(), HttpStatus.OK);
    }
}
