package com.wc.workbench.controller;

import com.wc.settings.domain.User;
import com.wc.settings.service.Impl.UserServiceImpl;
import com.wc.settings.service.UserService;
import com.wc.utils.*;
import com.wc.vo.PageVo;
import com.wc.workbench.domain.Activity;
import com.wc.workbench.domain.Clue;
import com.wc.workbench.domain.Tran;
import com.wc.workbench.service.ClueService;
import com.wc.workbench.service.Impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServlet extends BaseServlet {

    protected void getUserList(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList = userService.getUserList();

        PrintJson.printJsonObj(resp, userList);
    }


    protected void saveClue(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fullname = req.getParameter("fullname");
        String appellation = req.getParameter("appellation");
        String owner = req.getParameter("owner");
        String company = req.getParameter("company");
        String job = req.getParameter("job");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String website = req.getParameter("website");
        String mphone = req.getParameter("mphone");
        String state = req.getParameter("state");
        String source = req.getParameter("source");
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");
        String address = req.getParameter("address");

        String id = UUIDUtil.getUUID();
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();

        //id
        //fullname
        //appellation
        //owner
        //company
        //job
        //email
        //phone
        //website
        //mphone
        //state
        //source
        //createBy
        //createTime
        //editBy
        //editTime
        //description
        //contactSummary
        //nextContactTime
        //address

        Clue clue = new Clue(id, fullname, appellation, owner, company, job, email, phone, website
        ,mphone, state, source, createBy, createTime, null, null, description, contactSummary, nextContactTime, address);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean success = clueService.saveClue(clue);

        PrintJson.printJsonFlag(resp,success );
    }

    protected void page(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //接收查询参数，可有可无
        String fullname = req.getParameter("fullname");
        String company = req.getParameter("company");
        String phone = req.getParameter("phone");
        String source = req.getParameter("source");
        String owner = req.getParameter("owner");
        String mphone = req.getParameter("mphone");
        String state = req.getParameter("state");

        //接收分页参数
        int pageNo = Integer.parseInt(req.getParameter("pageNo"));
        int pageSize = Integer.parseInt(req.getParameter("pageSize"));
        int skipCount = (pageNo - 1) * pageSize;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fullname", fullname);
        map.put("company", company);
        map.put("phone", phone);
        map.put("source", source);
        map.put("owner", owner);
        map.put("mphone", mphone);
        map.put("state", state);
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        map.put("skipCount", skipCount);



        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        //前端需要数据：总页数，总记录数，每页数据
        PageVo<Clue> pageVo= clueService.page(map);

        //将数据返回前端
        PrintJson.printJsonObj(resp, pageVo);
    }

    protected void showDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //clue的id
        String id = req.getParameter("id");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue clue = clueService.getClueById(id);

        req.setAttribute("clue", clue);

        req.getRequestDispatcher("/workbench/clue/detail.jsp").forward(req,resp);
    }

    protected void showRelatedActivity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //clue的id
        String id = req.getParameter("id");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        List<Activity> activityList = clueService.getRelatedActivityByClueId(id);

        PrintJson.printJsonObj(resp, activityList);
    }

    protected void unbund(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clueId = req.getParameter("clueId");
        String activityId = req.getParameter("activityId");

        Map<String, String> map = new HashMap<String, String>();
        map.put("clueId",clueId);
        map.put("activityId",activityId);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean success = clueService.delRelationByClueIdAndActivityId(map);

        PrintJson.printJsonFlag(resp, success);
    }

    protected void getAllActivitys(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        List<Activity> activityList = clueService.getAllActivities();

        PrintJson.printJsonObj(resp, activityList);
    }

    protected void getAllActivitiesByNameAndNotRelatedWithClue(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String actName = req.getParameter("actName");
        String clueId = req.getParameter("clueId");
        Map<String, String> map = new HashMap<String, String>();
        map.put("actName", actName);
        map.put("clueId", clueId);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        List<Activity> activityList = clueService.getAllActivitiesByNameAndNotRelatedWithClue(map);

        PrintJson.printJsonObj(resp, activityList);
    }

    protected void relateClueAndActivities(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String clueId = req.getParameter("clueId");
        String[] actIds = req.getParameterValues("actId");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

       boolean success = clueService.relateClueAndActivities(clueId, actIds);

        PrintJson.printJsonFlag(resp, success);
    }

    protected void searchActivityByName(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String aname = req.getParameter("aname");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        List<Activity> activities = clueService.searchActivityByName(aname);

        PrintJson.printJsonObj(resp, activities);
    }

    protected void convert(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String flag = req.getParameter("flag");
        String clueId = req.getParameter("clueId");

        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        Tran transaction = null;
        //如果是提交表单发起的请求,则需要添加交易
        if("a".equals(flag)) {
            transaction = new Tran();

            String money = req.getParameter("money");
            String name = req.getParameter("name");
            String expectedDate = req.getParameter("expectedDate");
            String stage = req.getParameter("stage");
            String activityId = req.getParameter("activityId");
            String createTime = DateTimeUtil.getSysTime();
            String id = UUIDUtil.getUUID();

            transaction.setMoney(money);
            transaction.setName(name);
            transaction.setExpectedDate(expectedDate);
            transaction.setStage(stage);
            transaction.setActivityId(activityId);
            transaction.setCreateBy(createBy);
            transaction.setCreateTime(createTime);
            transaction.setId(id);
        }

        ClueService clueService = (ClueService)ServiceFactory.getService(new ClueServiceImpl());

        boolean success = clueService.convert(clueId, transaction, createBy);


        //如果转换成功，转发到开始页面
        if(success) {
            req.getRequestDispatcher("/workbench/clue/index.jsp").forward(req
            ,resp);
        }
    }


    protected void removeClues(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取删除id参数
        String[] ids = req.getParameterValues("id");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        //删除记录
        boolean success = clueService.removeCluesByIds(ids);

        //返回结果标记
        PrintJson.printJsonFlag(resp, success);
    }

}
