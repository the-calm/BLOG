package com.xiekun.blog.service;

import com.xiekun.blog.po.User;

public interface UserService {
    User checkUser(String username, String password);
}
