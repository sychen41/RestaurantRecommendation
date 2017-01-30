package com.shiyi.service;

import com.shiyi.domain.User;
import com.shiyi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Shiyi on 1/26/2017.
 */
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findOne(username);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
