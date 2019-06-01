package com.taotao.blog.dao;

import com.taotao.blog.model.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Taotao Ma
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostIdAndOriginalCommentNull(Long postId, Sort sort);
}
