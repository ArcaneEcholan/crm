package com.wc.workbench.controller;

import com.wc.settings.domain.User;
import com.wc.utils.*;
import com.wc.vo.PageVo;
import com.wc.workbench.domain.Activity;
import com.wc.workbench.domain.ActivityRemark;
import com.wc.workbench.service.ActivityService;
import com.wc.workbench.service.Impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class ActivityServlet extends BaseServlet{


    /**
     * 获取数据库中所有user信息
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void getUserList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        //获取数据库中user列表
        List<User> userList= activityService.getUserList();

        //将user返回前端
        PrintJson.printJsonObj(resp, userList);
    }

    /**
     * 保存活动
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void createActivity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取表单参数
        String id = UUIDUtil.getUUID();
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");

        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) req.getSession().getAttribute("user")).getName();

        Activity activity = new Activity(id, owner, name, startDate, endDate, cost, description, createTime, createBy, null, null);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        //保存到数据库中
        boolean success = activityService.createActivity(activity);

        //返回是否成功
        PrintJson.printJsonFlag(resp, success);
    }


    protected void page(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取分页条件
        int pageNo = Integer.parseInt(req.getParameter("pageNo"));
        int pageSize = Integer.parseInt(req.getParameter("pageSize"));
        int skipCount =  (pageNo - 1) * pageSize;

        //获取查询条件
        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String startTime = req.getParameter("startTime");
        String endTime = req.getParameter("endTime");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        //将查询参数封装成map
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);

        //前端需要数据：act列表，总记录数，总页数
        PageVo<Activity> pageVo = activityService.page(map);

        //将数据返回前端
        PrintJson.printJsonObj(resp, pageVo);
    }

    protected void deleteActivity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String[] ids = req.getParameterValues("id");

//        Map<String, String[]> parameterMap = req.getParameterMap();
//        Set<String> strings = parameterMap.keySet();
//        for (String key:
//             strings) {
//            System.out.print(key + ":");
//            String[] strings1 = parameterMap.get(key);
//            System.out.println(Arrays.toString(strings1));
//        }
//
//        System.out.println(Arrays.toString(ids));

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean success = activityService.deleteActivity(ids);

        System.out.println(success);

        PrintJson.printJsonFlag(resp, success);
    }

    protected void openUpdateModal(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String aid = req.getParameter("id");

        ActivityService activityService = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        List<User> userList = activityService.getUserList();
        System.out.println("======userList=====>>>" + userList);
        Activity activity = activityService.queryActivity(aid);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uList", userList);
        map.put("activity", activity);

        //测试获取的数据
        for (int i = 0; i < userList.size(); i++) {
            System.out.println(userList.get(i));
        }
        System.out.println(activity);

        PrintJson.printJsonObj(resp, map);
    }

    protected void editActivity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //				"action":"createActivity",
        //				"owner" : $.trim($("#create-owner").val()),
        //					"name" : $.trim($("#create-name").val()),
        //					"startDate" : $.trim($("#create-startDate").val()),
        //					"endDate" : $.trim($("#create-endDate").val()),
        //					"cost" : $.trim($("#create-cost").val()),
        //					"description" : $.trim($("#create-description").val())


        //    private String id;  //主键
        //    private String owner;   //所有者 外键 关联tbl_user
        //    private String name;    //市场活动名称
        //    private String startDate;   //开始日期 年月日
        //    private String endDate; //结束日期 年月日
        //    private String cost;    //成本
        //    private String description; //描述
        //    private String createTime;  //创建时间 年月日时分秒
        //    private String createBy;    //创建人
        //    private String editTime;    //修改时间 年月日时分秒
        //    private String editBy;  //修改人

        String id = req.getParameter("id");
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");

        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User) req.getSession().getAttribute("user")).getName();

        //String id, String owner, String name, String startDate, String endDate, String cost, String description, String createTime, String createBy, String editTime, String editBy
        Activity activity = new Activity(id, owner, name, startDate, endDate, cost, description, null, null, editTime, editBy);

        System.out.println(activity);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean success = activityService.editActivity(activity);

        System.out.println(success);

        PrintJson.printJsonFlag(resp, success);
    }

    protected void showDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity activity = activityService.showDetail(id);

        req.setAttribute("a", activity);

        req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req,resp);
    }

    protected void getRemarksById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<ActivityRemark> activityRemarkList = activityService.getRemarksByActivityId(id);

        System.out.println(activityRemarkList);

        PrintJson.printJsonObj(resp, activityRemarkList);
    }

    protected void deleteRemarkById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean success = activityService.deleteRemarkById(id);

        System.out.println(success);

        PrintJson.printJsonFlag(resp, success);
    }

    protected void saveRemark(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = UUIDUtil.getUUID();
        String noteContent = req.getParameter("noteContent");
        String createTime = DateTimeUtil.getSysTime();
        String createBy =( (User)req.getSession().getAttribute("user")).getName();
        String editFlag = "0";
        String activityId = req.getParameter("activityId");

        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setActivityId(activityId);
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateBy(createBy);
        activityRemark.setCreateTime(createTime);
        activityRemark.setEditFlag(editFlag);


        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean success = activityService.saveRemark(activityRemark);

        System.out.println(success);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("success", success);
        map.put("activityRemark", activityRemark);

        System.out.println(map);

        PrintJson.printJsonObj(resp, map);
    }

    protected void updateRemark(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");
        String noteContent = req.getParameter("noteContent");
        String editTime= DateTimeUtil.getSysTime();
        String editBy =( (User)req.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateBy(editBy);
        activityRemark.setCreateTime(editTime);
        activityRemark.setEditFlag(editFlag);


        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean success = activityService.updateRemark(activityRemark);

        System.out.println(success);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("success", success);
        map.put("activityRemark", activityRemark);

        System.out.println(map);

        PrintJson.printJsonObj(resp, map);
    }






}
