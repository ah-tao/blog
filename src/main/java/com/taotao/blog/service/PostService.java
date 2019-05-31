package com.taotao.blog.service;

import com.taotao.blog.model.Post;
import com.taotao.blog.util.PostSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author Taotao Ma
 */
public interface PostService {

    Post getPost(Long id);

    Post getAndConvertPost(Long id);

    Page<Post> listPost(Pageable pageable, PostSearchCriteria criteria);

    Page<Post> listPost(Pageable pageable);

    Page<Post> listPost(Pageable pageable, String query);

    Page<Post> listPost(Pageable pageable, Long tagId);

    Map<String, List<Post>> mapPostByYear();

    Long getPostCount();

    Post savePost(Post post);

    Post updatePost(Long id, Post post);

    void deletePost(Long id);
}
