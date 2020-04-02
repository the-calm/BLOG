package com.xiekun.blog.service;

import com.xiekun.blog.po.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment savaComment(Comment comment);

}
