package com.taotao.blog.service;

import com.taotao.blog.dao.PostRepository;
import com.taotao.blog.handler.NotFoundException;
import com.taotao.blog.model.Post;
import com.taotao.blog.util.MarkdownUtils;
import com.taotao.blog.util.MyBeanUtils;
import com.taotao.blog.util.PostSearchCriteria;
import com.taotao.blog.util.PostSpecification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Taotao Ma
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository repository;

    @Override
    public Post getPost(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Post getAndConvertPost(Long id) {
        Post post = repository.findById(id).orElse(null);
        if (post == null) {
            throw new NotFoundException("No such post exist");
        }
        Post p = new Post();
        BeanUtils.copyProperties(post, p);
        String convertedContent = MarkdownUtils.markdownToHtmlExtensions(post.getContent());
        p.setContent(convertedContent);
        return p;
    }

    @Override
    public Page<Post> listPost(Pageable pageable, PostSearchCriteria criteria) {
        PostSpecification postSpecification = new PostSpecification(criteria);
        return repository.findAll(postSpecification, pageable);
    }

    @Override
    public Page<Post> listPost(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Post> listPost(Pageable pageable, String query) {
        return repository.findByQuery(pageable, query);
    }

    @Override
    public Page<Post> listPost(Pageable pageable, Long tagId) {
        return repository.findAll((Specification<Post>) (root, query, builder) -> {
            Join join = root.join("tags");
            return builder.equal(join.get("id"), tagId);
        }, pageable);
    }

    @Override
    public Map<String, List<Post>> mapPostByYear() {
        List<String> years = repository.findGroupYear();
        Map<String, List<Post>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, repository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long getPostCount() {
        return repository.count();
    }

    @Transactional
    @Override
    public Post savePost(Post post) {
        if (post.getId() == null) {
            post.setCreated(new Date());
        }
        post.setUpdated(new Date());
        return repository.save(post);
    }

    @Transactional
    @Override
    public Post updatePost(Long id, Post post) {
        Post p = repository.findById(id).orElse(null);
        if (p == null) {
            throw new NotFoundException("The post does not exist.");
        }
        BeanUtils.copyProperties(post, p, MyBeanUtils.getNullPropertyNames(post));
        return repository.save(p);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePost(Long id) {
        repository.deleteById(id);
    }
}
