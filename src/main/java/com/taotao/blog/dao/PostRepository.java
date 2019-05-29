package com.taotao.blog.dao;

import com.taotao.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Taotao Ma
 */
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    @Query("select p from Post p where p.title like ?1 or p.content like ?1")
    Page<Post> findByQuery(Pageable pageable, String query);
}
