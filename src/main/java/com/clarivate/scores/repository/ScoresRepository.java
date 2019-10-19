package com.clarivate.scores.repository;

import com.clarivate.scores.model.Score;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ScoresRepository extends MongoRepository<Score, String> {

    List<Score> findByLevel(int level);

}
