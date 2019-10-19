package com.clarivate.scores.repository;

import com.clarivate.scores.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUserName(String userName);
}
