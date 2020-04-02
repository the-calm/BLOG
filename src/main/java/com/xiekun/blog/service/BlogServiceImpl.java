package com.xiekun.blog.service;
import	java.util.HashMap;
import	java.util.ArrayList;

import com.xiekun.blog.NotFoundException;
import com.xiekun.blog.dao.BlogRepository;
import com.xiekun.blog.po.Blog;
import com.xiekun.blog.po.Type;
import com.xiekun.blog.query.BlogQuery;
import com.xiekun.blog.util.MarkDownUtils;
import com.xiekun.blog.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    @Override
    public Page<Blog> listblog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"),tagId);
            }
        }, pageable);
    }

    @Override
    public Map<String, List<Blog>> fileBlog() {
        List<String> years = blogRepository.findGroupYeas();
        Map<String,List<Blog>> map = new HashMap<> ();
        for(String year : years) {
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Override
    public Page<Blog> listblog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<> ();
                if(!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicate.add(cb.like(root.<String>get("title"), "%"+blog.getTitle()+"%" ));
                }
                if(blog.getTypeId() != null) {
                    predicate.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId() ));
                }
                if(blog.isRecommend()) {
                    predicate.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend() ));
                }
                cq.where(predicate.toArray(new Predicate[predicate.size()]));
                return null;
            }
        }, pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null)
        {
            blog.setViews(0);
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
        } else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getOne(id);
        if (b == null) {
            throw new NotFoundException("该博客文章不存在！");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Page<Blog> listblog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable = PageRequest.of(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Page<Blog> listblog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if(blog == null) {
            throw new NotFoundException("该博客不存在！");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkDownUtils.markdownToHtmlExtensions(content));
        blogRepository.updateViews(id);
        return b;
    }
}
