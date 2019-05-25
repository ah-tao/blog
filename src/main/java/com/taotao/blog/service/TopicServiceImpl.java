package com.taotao.blog.service;

import com.taotao.blog.dao.TopicRepository;
import com.taotao.blog.handler.NotFoundException;
import com.taotao.blog.model.Topic;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Taotao Ma
 */
@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicRepository repository;

    @Transactional
    @Override
    public Topic saveTopic(Topic topic) {
        return repository.save(topic);
    }

    @Transactional
    @Override
    public Topic getTopic(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Topic getTopicByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    @Override
    public Topic updateTopic(Long id, Topic topic) {
        Topic t = repository.findById(id).orElse(null);
        if (t == null) {
            throw new NotFoundException("No such record exist.");
        }
        BeanUtils.copyProperties(topic, t);
        return repository.save(t);
    }

    @Transactional
    @Override
    public Page<Topic> listTopic(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTopic(Long id) {
        repository.deleteById(id);
    }

}
