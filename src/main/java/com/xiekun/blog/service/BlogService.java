package com.xiekun.blog.service;


import com.xiekun.blog.po.Blog;
import com.xiekun.blog.query.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog getBlog(Long id);

    Page<Blog> listblog(Pageable pageable, BlogQuery blog);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);

    Page<Blog> listblog(Pageable pageable);

    List<Blog> listRecommendBlogTop(Integer size);

    Page<Blog> listblog(String query,Pageable pageable);

    Blog getAndConvert(Long id);

    Page<Blog> listblog(Long tagId,Pageable pageable);

    Map<String,List<Blog>> fileBlog();

    Long countBlog();


}
