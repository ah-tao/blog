package com.taotao.blog.service;

import com.taotao.blog.model.Comment;

import java.util.List;

/**
 * @author Taotao Ma
 */
public interface CommentService {

    List<Comment> listCommentByPostId(Long postId);

    Comment saveComment(Comment comment);
}
