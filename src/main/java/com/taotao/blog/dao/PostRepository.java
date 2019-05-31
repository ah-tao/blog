package com.taotao.blog.dao;

import com.taotao.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Taotao Ma
 */
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    @Query("select p from Post p where p.title like ?1 or p.content like ?1")
    Page<Post> findByQuery(Pageable pageable, String query);

    @Query("select function('date_format', p.updated, '%Y') as year from Post p group by function('date_format', p.updated, '%Y') order by year desc")
    List<String> findGroupYear();

    @Query("select p from Post p where function('date_format', p.updated, '%Y') = ?1")
    List<Post> findByYear(String year);
}
