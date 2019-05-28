package com.taotao.blog.util;

/**
 * @author Taotao Ma
 */
public class PostSearchCriteria {
    private String title;
    private Long topicId;

    public PostSearchCriteria() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
}
