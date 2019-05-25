package com.taotao.blog.dao;

import com.taotao.blog.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Taotao Ma
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByName(String name);
}
