package com.clarivate.scores.service;

import com.clarivate.scores.model.Score;

import java.util.List;

public interface ScoresService {

    void addLevelScore(String userName, int level, int score);

    List<Score> getHighestScores(int level);

}
