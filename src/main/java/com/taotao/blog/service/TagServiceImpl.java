package com.taotao.blog.service;

import com.taotao.blog.dao.TagRepository;
import com.taotao.blog.handler.NotFoundException;
import com.taotao.blog.model.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Taotao Ma
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository repository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return repository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Tag getTagByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = repository.findById(id).orElse(null);
        if (t == null) {
            throw new NotFoundException("No such record exist.");
        }
        BeanUtils.copyProperties(tag, t);
        return repository.save(t);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return repository.findAll();
    }

    @Override
    public List<Tag> listTag(String ids) {
        List<Long> list = new ArrayList<>();
        if (ids != null && !"".equals(ids)) {
            String[] idarray = ids.split(",");
            for (String s : idarray) {
                list.add(new Long(s));
            }
        }
        return repository.findAllById(list);
    }

    @Override
    public List<Tag> listTopTag(Integer size) {
        Pageable pageable = PageRequest.of(0, size, Sort.Direction.DESC, "posts.size");
        return repository.findTop(pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTag(Long id) {
        repository.deleteById(id);
    }

}
