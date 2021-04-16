package com.wc.settings.service.Impl;

import com.wc.settings.dao.UserDao;
import com.wc.settings.domain.User;
import com.wc.settings.service.UserService;
import com.wc.utils.DateTimeUtil;
import com.wc.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    //持有dao对象
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User login(String username, String password, String ip) {
        //将账号和密码封装成map便于传入dao中查询
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("password", password);

        //去数据库中查询用户
        User user = userDao.queryUserByUsernameAndPassword(map);

        //如果用户为空，说明用户名或密码错误
        if(user == null) {
            RuntimeException e = new RuntimeException("账号或者密码错误");
            throw e;
        }
        //如果用户的登录ip不在允许范围内
        if(!user.getAllowIps().contains(ip)){
            throw new RuntimeException("ip不允许访问");
        }

        //判断账号是否是超过时效
        if(user.getExpireTime().compareTo(DateTimeUtil.getSysTime()) < 0){
            throw new RuntimeException("账号已失效");
        }

        //判断账号是否已经锁定
        if("1".equals(user.getLockState())){
            throw new RuntimeException("账号已锁定");
        }

        return user;
    }

    public List<User> getUserList() {
        return userDao.getUserList();
    }
}
