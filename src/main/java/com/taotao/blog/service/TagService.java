package com.taotao.blog.service;

import com.taotao.blog.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Taotao Ma
 */
public interface TagService {

    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Tag updateTag(Long id, Tag tag);

    Page<Tag> listTag(Pageable pageable);

    void deleteTag(Long id);
}
