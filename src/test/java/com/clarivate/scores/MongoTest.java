package com.clarivate.scores;

import com.clarivate.scores.model.User;
import com.clarivate.scores.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataMongoTest
public class MongoTest {

    private String collectionName;
    private User user;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void before() {
        collectionName = "users";
        user = new User();
        user.setUserName("user1");
        user.setPassword("chnageit1");
    }

    @After
    public void after() {
        mongoTemplate.dropCollection(collectionName);
    }

    @Test
    public void checkMongoTemplate() {
        assertNotNull(mongoTemplate);
        mongoTemplate.createCollection(collectionName);
        assertTrue(mongoTemplate.collectionExists(collectionName));
    }

    @Test
    public void checkDocumentAndQuery() {
        mongoTemplate.save(user, collectionName);
        Query query = new Query(Criteria.where("userName").is(user.getUserName()));

        User user = mongoTemplate.findOne(query, User.class, collectionName);
        assertNotNull(user);
    }

    @Test
    public void checkLogRepository() {
        assertNotNull(userRepository);
        User userSaved = userRepository.save(user);
        assertNotNull(userRepository.findByUserName(userSaved.getUserName()));
    }

}
