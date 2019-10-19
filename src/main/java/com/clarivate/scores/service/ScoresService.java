package com.clarivate.scores.service;

import com.clarivate.scores.model.HighestScore;

import java.util.List;

public interface ScoresService {

    void addLevelScore(String userName, int level, int score);

    List<HighestScore> getHighestScoresPerLevel(int level);
}
