package com.taotao.blog.service;

import com.taotao.blog.model.Post;
import com.taotao.blog.util.PostSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Taotao Ma
 */
public interface PostService {

    Post getPost(Long id);

    Page<Post> listPost(Pageable pageable, PostSearchCriteria criteria);

    Page<Post> listPost(Pageable pageable);

    Post savePost(Post post);

    Post updatePost(Long id, Post post);

    void deletePost(Long id);
}
