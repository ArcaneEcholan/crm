package com.wc.workbench.controller;

import com.wc.settings.domain.User;
import com.wc.settings.service.Impl.UserServiceImpl;
import com.wc.settings.service.UserService;
import com.wc.utils.DateTimeUtil;
import com.wc.utils.PrintJson;
import com.wc.utils.ServiceFactory;
import com.wc.utils.UUIDUtil;
import com.wc.workbench.domain.*;
import com.wc.workbench.service.CustomerService;
import com.wc.workbench.service.Impl.CustomerServiceImpl;
import com.wc.workbench.service.Impl.TranServiceImpl;
import com.wc.workbench.service.TranService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServlet extends BaseServlet {

    protected void getUserList(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList = userService.getUserList();

        req.setAttribute("uList", userList);

        req.getRequestDispatcher("/workbench/transaction/save.jsp").forward(req,resp);
    }

    protected void getCustomerNames(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        System.out.println("hello");
        String name = req.getParameter("name");
        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> nameList = customerService.queryCustomerNames(name);

        PrintJson.printJsonObj(resp, nameList);
    }

    protected void createTran(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String id = UUIDUtil.getUUID();

        String owner = req.getParameter("owner");
        String money = req.getParameter("money");
        String name = req.getParameter("name");
        String expectedDate = req.getParameter("expectedDate");
        String stage = req.getParameter("stage");
        String type = req.getParameter("type");
        String source = req.getParameter("source");
        String activityId = req.getParameter("activityId");
        String contactsId = req.getParameter("contactsId");
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");
        String customerName = req.getParameter("customerName");

        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)req.getSession().getAttribute("user")).getName();

        Tran tran = new Tran();

        tran.setId(id);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setType(type);
        tran.setSource(source);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);
        tran.setCreateBy(createBy);
        tran.setCreateTime(createTime);

        System.out.println(tran);

        TranService tranService = (TranService)ServiceFactory.getService(new TranServiceImpl());

        boolean success = tranService.createTran(tran, customerName);

        if(success){
            resp.sendRedirect(req.getContextPath() + "/workbench/transaction/index.jsp");
        }
    }


    protected void showDetail(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String id = req.getParameter("id");

        TranService tranService = (TranService)ServiceFactory.getService(new TranServiceImpl());
        Tran tran = tranService.getTranById(id);

        String stage = tran.getStage();
        Map<String, String> possibilityMap = (Map<String, String>)getServletContext().getAttribute("possibilityMap");
        String possibility = possibilityMap.get(stage);


        req.setAttribute("tran",tran);
        req.setAttribute("possibility",possibility);

        System.out.println(tran);

        req.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(req,resp);
    }

    protected void showTranHistoryList(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        //交易id
        String tranId = req.getParameter("tranId");
        TranService tranService = (TranService)ServiceFactory.getService(new TranServiceImpl());

        List<TranHistory> tranHistoryList = tranService.queryHistoriesByTranId(tranId);

        //另外为可能性赋值
        Map<String, String> possibilityMap = (Map<String, String>)getServletContext().getAttribute("possibilityMap");
        for (TranHistory tranHistory:tranHistoryList) {
            String stage = tranHistory.getStage();
            String possibility = possibilityMap.get(stage);
            tranHistory.setPossibility(possibility);
        }

        for(TranHistory tranHistory:tranHistoryList) {
            System.out.println(tranHistory);
        }

        PrintJson.printJsonObj(resp, tranHistoryList);
    }


    protected void changeStage(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String tranId = req.getParameter("tranId");
        String targetStage = req.getParameter("targetStage");
        String money = req.getParameter("money");
        String expectedDate = req.getParameter("expectedDate");

        TranService tranService = (TranService)ServiceFactory.getService(new TranServiceImpl());

        Tran tran = new Tran();

        tran.setId(tranId);
        tran.setStage(targetStage);
        tran.setMoney(money);
        tran.setExpectedDate(expectedDate);
        tran.setEditBy(((User)req.getSession().getAttribute("user")).getName());
        tran.setEditTime(DateTimeUtil.getSysTime());

        Map<String, String> possibilityMap = (Map<String, String>)getServletContext().getAttribute("possibilityMap");
        String possibility = possibilityMap.get(targetStage);
        tran.setPossibility(possibility);

        boolean success = tranService.change(tran);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", success);
        map.put("tran",tran);

        PrintJson.printJsonObj(resp, map);
    }


    protected void getCharts(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        TranService tranService = (TranService)ServiceFactory.getService(new TranServiceImpl());

        //返回map（一个个数，一个集合）
        Map<String,Object> map = tranService.getCharts();

        PrintJson.printJsonObj(resp, map);
    }


}
