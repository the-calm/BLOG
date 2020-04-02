package com.xiekun.blog.service;

import com.xiekun.blog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    Tag saveTag(Tag type);

    Tag getTag(Long id);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listTag();

    Tag updateTag(Long id, Tag type);

    void deleteTag(Long id);

    Tag getTagByname(String name);

    List<Tag> listTag(String ids);

    List<Tag> listTagTop(Integer size);

}
