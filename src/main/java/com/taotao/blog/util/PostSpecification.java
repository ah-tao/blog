package com.taotao.blog.util;

import com.taotao.blog.model.Post;
import com.taotao.blog.model.Topic;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Taotao Ma
 */
public class PostSpecification implements Specification<Post> {

    private PostSearchCriteria criteria;

    public PostSpecification(PostSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getTitle() != null && !"".equals(criteria.getTitle())) {
            predicates.add(builder.like(root.<String>get("title"), "%"+criteria.getTitle()+"%"));
        }
        if (criteria.getTopicId() != null) {
            predicates.add(builder.equal(root.<Topic>get("topic").get("id"), criteria.getTopicId()));
        }
        query.where(predicates.toArray(new Predicate[0]));

        return null;
    }
}
