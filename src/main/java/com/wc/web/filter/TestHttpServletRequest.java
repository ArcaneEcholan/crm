package com.wc.web.filter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class TestHttpServletRequest extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("uri => " + req.getRequestURI());
        System.out.println("url => " + req.getRequestURL());
        System.out.println("项目工程绝对路径 => " + req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/");

        System.out.println("method => " + req.getMethod());
        System.out.println("header(Accept) => " + req.getHeader("Accept"));
    }
    //uri => /crm/testHttpServletRequest
    //url => http://localhost:8080/crm/testHttpServletRequest
    //项目工程绝对路径 => http://localhost:8080/crm/
    //method => GET
    //header(Accept) => text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9

}