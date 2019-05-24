package com.taotao.blog.service;

import com.taotao.blog.model.User;

/**
 * @author Taotao Ma
 */
public interface UserService {
    /**
     * @param username username
     * @param password password
     * @return User
     */
    User checkUser(String username, String password);
}
