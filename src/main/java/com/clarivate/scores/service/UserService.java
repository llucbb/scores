package com.clarivate.scores.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if ("user1".equals(userName)) {
            return new User("user1", "$2a$10$9CE7PP/9WdO.ZtwDgkWHsuvFFf.8t3amDEb6dBASkXxX/wTfo62Qi",
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with userName: " + userName);
        }
    }
}
