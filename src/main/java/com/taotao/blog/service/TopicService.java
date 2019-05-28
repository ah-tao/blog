package com.taotao.blog.service;

import com.taotao.blog.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Taotao Ma
 */
public interface TopicService {

    Topic saveTopic(Topic topic);

    Topic getTopic(Long id);

    Topic getTopicByName(String name);

    Topic updateTopic(Long id, Topic topic);

    Page<Topic> listTopic(Pageable pageable);

    List<Topic> listTopic();

    void deleteTopic(Long id);
}
