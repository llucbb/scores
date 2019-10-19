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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Scores endpoints in order to provide unique session key valid for use for a defined period by other endpoints.
 */
@RestController
@CrossOrigin
public class ScoresController {

    private final static Logger LOG = LoggerFactory.getLogger(ScoresController.class);

    @Autowired
    private ScoresService defaultScoresService;

    @PutMapping("/level/{level}/score/{score}")
    public ResponseEntity addLevelScore(@PathVariable int level, @PathVariable int score) {
        LOG.debug(String.format("-> addLevelScore: level=%d, score=%d", level, score));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        defaultScoresService.addLevelScore(authentication.getName(), level, score);
        LOG.debug("<- addLevelScore");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

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
