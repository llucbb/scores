package com.clarivate.scores.service.impl;

import com.clarivate.scores.model.Score;
import com.clarivate.scores.repository.ScoresRepository;
import com.clarivate.scores.service.ScoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultScoresService implements ScoresService {

    @Autowired
    private ScoresRepository scoresRepository;

    @Override
    public void addLevelScore(String userName, int level, int scoreValue) {
        Score score = new Score();
        score.setUserName(userName);
        score.setLevel(level);
        score.setScore(scoreValue);

        scoresRepository.save(score);
    }

    @Override
    public List<Score> getHighestScores(int level) {
//        BasicQuery query = new BasicQuery("{ age : { $lt : 50 }, accounts.balance : { $gt : 1000.00 }}");
        return scoresRepository.findByLevel(level);
    }
}
