package com.clarivate.scores.repository;

import com.clarivate.scores.model.HighestScore;
import com.clarivate.scores.model.Score;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ScoresRepository extends MongoRepository<Score, String> {

    @Query("{ level : ?0, userName : ?1, score : ?2 }")
    List<Score> findByLevelUserAndScore(int level, String userName, int score);

    @Aggregation(pipeline = {"{ $match : { level : ?0 } }", "{ $group : { _id : $userName, score : { $max : $score } } }"})
    List<HighestScore> getHighestScoresPerLevel(int level);

}
