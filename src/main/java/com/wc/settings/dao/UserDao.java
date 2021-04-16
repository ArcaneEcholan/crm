package com.wc.settings.dao;

import com.wc.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    public User queryUserByUsernameAndPassword(Map<String, String> map);

    List<User> getUserList();
}
