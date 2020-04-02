package com.xiekun.blog.service;
import	java.util.ArrayList;


import com.xiekun.blog.NotFoundException;
import com.xiekun.blog.dao.TagRepository;
import com.xiekun.blog.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag type) {
        Tag t = tagRepository.getOne(id);
        if (t == null) {
            throw new NotFoundException("不存在该类型");
        }

        BeanUtils.copyProperties(type,t);
        return tagRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Tag getTagByname(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public List<Tag> listTag(String ids) {
        return tagRepository.findAllById(convertToList(ids));
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return tagRepository.findTop(pageable);
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<> ();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i<idarray.length; i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }
}
