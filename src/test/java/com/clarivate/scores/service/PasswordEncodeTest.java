package com.clarivate.scores.service;

import io.jsonwebtoken.lang.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordEncodeTest {

    private final static Logger LOG = LoggerFactory.getLogger(PasswordEncodeTest.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testEncode() {
        final String password = "changeit1";

        String encoded = passwordEncoder.encode(password);

        Assert.notNull(encoded);
        LOG.info("testEncode: " + encoded);
    }
}
