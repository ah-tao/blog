package com.taotao.blog.dao;

import com.taotao.blog.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Taotao Ma
 */
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Topic findByName(String name);
}
