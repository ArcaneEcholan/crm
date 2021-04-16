package com.wc.settings.service;

import com.wc.settings.domain.User;

import java.util.List;

public interface UserService {

    /**
     * 检查登录权限，如果允许登录，不抛出任何异常，如果不符合要求，抛出相应的异常
     * @param username-登录用户名
     * @param password-登录密码
     * @param ip-登录ip地址
     */
    User login(String username, String password, String ip);

    List<User> getUserList();
}
