package com.wc.workbench.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 所有servlet的公共父类，提供相同的post方法
 */
public class BaseServlet extends HttpServlet {
    /**
     * 所有servlet的公共代码，用于接收请求，并调用相应的方法
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("j30uj038j-083u408jf-083u98ut98hjfg8hj-2yh5978ghpwadjfhj-08j48fjhjpdfj8-3j4p9f8hj-9843-");
        String action = req.getParameter("action");
        System.out.println("<<======================================================" + action + " start=========================================================================================>>");

        try {
            Method method = this.getClass().getDeclaredMethod(action, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, req,resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 所有servlet的公共代码，用于接收请求，并调用相应的方法
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
