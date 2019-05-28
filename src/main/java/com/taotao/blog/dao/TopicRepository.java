package com.taotao.blog.dao;

import com.taotao.blog.model.Topic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Taotao Ma
 */
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Topic findByName(String name);

    @Query("select t from Topic t")
    List<Topic> findTop(Pageable pageable);

}
