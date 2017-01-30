package com.shiyi.service;


import com.shiyi.domain.User;

/**
 * Created by Shiyi on 1/26/2017.
 */
public interface UserService {
    User findByUsername(String username);

    User save(User user);
}
