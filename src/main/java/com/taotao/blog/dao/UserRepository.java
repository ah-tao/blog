package com.taotao.blog.dao;

import com.taotao.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.jws.soap.SOAPBinding;

/**
 * @author Taotao Ma
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * @param username username
     * @param password password
     * @return User
     */
    User findByUsernameAndPassword(String username, String password);
}
