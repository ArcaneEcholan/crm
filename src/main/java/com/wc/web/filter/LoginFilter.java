package com.wc.web.filter;

import com.wc.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        //获取请求资源路径名
        String servletPath = req.getServletPath();

        //检查是否登录
        User user = (User)req.getSession().getAttribute("user");

        //如果直接请求登录页面，应该直接放行，否则会循环卡死
        if("/login.jsp".equals(servletPath)) {
            filterChain.doFilter(req, servletResponse);
        }
        //如果请求其他页面，应该判断是否登录
        else {
            //如果没有登录，重定向到登录
            if(user == null) {
                resp.sendRedirect(req.getContextPath() + "/login.jsp");
            //如果登录成功，直接放行
            }else {
                filterChain.doFilter(req, servletResponse);
            }
        }
    }

    public void destroy() {

    }
}
