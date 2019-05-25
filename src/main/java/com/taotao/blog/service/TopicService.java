package com.taotao.blog.service;

import com.taotao.blog.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Taotao Ma
 */
public interface TopicService {

    Topic saveTopic(Topic topic);

    Topic getTopic(Long id);

    Topic getTopicByName(String name);

    Topic updateTopic(Long id, Topic topic);

    Page<Topic> listTopic(Pageable pageable);

    void deleteTopic(Long id);
}
