package com.clarivate.scores.repository;

import com.clarivate.scores.model.HighestScore;
import com.clarivate.scores.model.Score;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ScoresRepository extends MongoRepository<Score, String> {
//{ $match: { level : $?0 } },
    @Aggregation("{ $group: { _id : $userName, score : { $max : $score } } }")
    List<HighestScore> getHighestScoresPerLevel(int level);

}
