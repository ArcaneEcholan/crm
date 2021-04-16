package com.wc.settings.controller;

import com.wc.settings.domain.User;
import com.wc.settings.service.Impl.UserServiceImpl;
import com.wc.settings.service.UserService;
import com.wc.utils.PrintJson;
import com.wc.utils.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserServlet extends BaseServlet {

    protected void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("login start");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        //获取用户登录ip
        String ip = req.getRemoteHost();
        try {
            //登录，如果登录成功，不抛出任何异常
            User user = userService.login(username, password, ip);
            //将用户存入session域对象中
            req.getSession().setAttribute("user", user);

            System.out.println("login end");

            PrintJson.printJsonFlag(resp, true);
        } catch (Exception e) {
            //如果登录失败，抛出异常，并返回错误信息
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("msg", e.getMessage());
            map.put("success", false);
            System.out.println("login end");
            PrintJson.printJsonObj(resp, map);
        }


    }
}
