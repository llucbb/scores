package com.clarivate.scores.service.impl;

import com.clarivate.scores.model.HighestScore;
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
        scoresRepository.save(new Score(level, userName, scoreValue));
    }

    @Override
    public List<HighestScore> getHighestScoresPerLevel(int level) {
        return scoresRepository.getHighestScoresPerLevel(level);
    }
}
