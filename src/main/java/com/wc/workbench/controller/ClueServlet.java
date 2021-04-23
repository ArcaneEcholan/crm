package com.wc.workbench.controller;

import com.wc.settings.domain.User;
import com.wc.settings.service.Impl.UserServiceImpl;
import com.wc.settings.service.UserService;
import com.wc.utils.*;
import com.wc.vo.PageVo;
import com.wc.workbench.domain.*;
import com.wc.workbench.service.ClueService;
import com.wc.workbench.service.Impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.IconUIResource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    /**
     * 前端需要数据：用户列表，所需线索
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void openEditModal(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //需要线索的id
        String id = req.getParameter("id");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        //返回用户列表和线索
        Map<String, Object> map = clueService.getUserListAndClue(id);

        //返回结果标记
        PrintJson.printJsonObj(resp, map);
    }

    protected void editClue(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Clue clue = MapToBean.copyMapToBean(req.getParameterMap(),new Clue());

        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)req.getSession().getAttribute("user")).getName();

        clue.setEditTime(editTime);
        clue.setEditBy(editBy);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean success = clueService.updateClue(clue);
        //返回结果标记
        PrintJson.printJsonFlag(resp, success);
    }

    protected void saveRemark (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //将参数封装成javabean
        ClueRemark clueRemark = MapToBean.copyMapToBean(req.getParameterMap(),new ClueRemark());

        String createTime = DateTimeUtil.getSysTime();                                 //创建时间为现在时间
        String createBy = ((User)req.getSession().getAttribute("user")).getName();  //创建人为当前登录用户

        clueRemark.setId(UUIDUtil.getUUID());       //id是随机生成
        clueRemark.setEditFlag("0");                //由于第一次生成，所以没有修改，修改标志设置为0
        clueRemark.setCreateTime(createTime);
        clueRemark.setCreateBy(createBy);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        //调用业务层执行保存操作
        boolean success = clueService.saveRemark(clueRemark);

        //返回结果标记
        PrintJson.printJsonFlag(resp, success);
    }

    /**
     * 后端需要什么：一个线索id
     * 后端需要返回什么：该线索对应的备注表，以及线索对象
     */
    protected void showClueRemarkList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取线索id
        String clueId = req.getParameter("clueId");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        //前端需要线索和关联的备注表
        Map<String, Object> map = clueService.showClueRemarkList(clueId);

        //返回结果
        PrintJson.printJsonObj(resp, map);
    }

    /**
     * 后端需要什么：一个线索id
     * 后端需要返回什么：该线索对应的备注表，以及线索对象
     */
    protected void editClueRemarkContentByRemarkId(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取线索id和内容
        ClueRemark clueRemark = MapToBean.copyMapToBean(req.getParameterMap(), new ClueRemark());

        clueRemark.setEditTime(DateTimeUtil.getSysTime());
        clueRemark.setEditBy((String) req.getSession().getAttribute("username"));
        clueRemark.setEditFlag("1");

        System.out.println(clueRemark);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        //前端需要线索和关联的备注表
        boolean success = clueService.editClueRemarkContentByRemarkId(clueRemark);
        System.out.println(success);
        //返回结果
        PrintJson.printJsonFlag(resp, success);
    }

    /**
     * 后端需要什么：备注id号
     * 后端需要做什么：按删除备注
     */
    protected void removeClueRemarkByRemarkId(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取线索id和内容
        String id = req.getParameter("id");
        System.out.println(id);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        //前端需要线索和关联的备注表
        boolean success = clueService.removeClueRemarkByRemarkId(id);
        //返回结果
        PrintJson.printJsonFlag(resp, success);
    }

    /**
     * 后端需要做什么：返回所有活动
     */
    protected void getAllNotBundedActivities(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        //前端需要所有活动
        List<Activity> activityList = clueService.getAllNotBundedActivities();
        //返回结果
        PrintJson.printJsonObj(resp, activityList);
    }

    /**
     * 后端需要做什么：返回符合条件的所有活动
     */
    protected void getNotBundedActivitiesByName (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String aname = req.getParameter("aname");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        //前端需要所有活动
        List<Activity> activityList = clueService.getNotBundedActivitiesByName(aname);
        //返回结果
        PrintJson.printJsonObj(resp, activityList);
    }

    /**
     * 后端需要做什么：往关联表中加记录(插入clueId和activityId)
     */
    protected void bundActs (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] activityIds = req.getParameterValues("id");
        String clueId = req.getParameter("clueId");
        List<ClueActivityRelation> list = new ArrayList<ClueActivityRelation>();
        ClueActivityRelation clueActivityRelation = null;
        for(int i = 0; i < activityIds.length; i++) {
            clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(UUIDUtil.getUUID());
            clueActivityRelation.setClueId(clueId);
            clueActivityRelation.setActivityId(activityIds[i]);
            list.add(clueActivityRelation);
        }

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        //前端需要所有活动
        boolean success = clueService.bundActs(list);
        //返回结果
        PrintJson.printJsonFlag(resp, success);
    }

    /**
     * 后端需要做什么：获取所有关联的市场活动（id 名称 开始日期	结束日期	所有者）
     */
    protected void getAllRelatedActsByClueId (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clueId = req.getParameter("clueId");
        System.out.println(clueId);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        //前端需要所有活动
        List<Activity> activityList = clueService.getAllRelatedActsByClueId(clueId);
        //返回结果
        PrintJson.printJsonObj(resp, activityList);
    }

//    protected void unbund(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String clueId = req.getParameter("clueId");
//        String activityId = req.getParameter("activityId");
//
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("clueId",clueId);
//        map.put("activityId",activityId);
//
//        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
//
//        boolean success = clueService.delRelationByClueIdAndActivityId(map);
//
//        PrintJson.printJsonFlag(resp, success);
//    }

    /**
     * 后端需要什么：关联的线索id和活动id
     * 后端需要做什么：删除关联表tbl_clue_activity_relation中的一条记录
     */
    protected void unbund (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clueId = req.getParameter("clueId");
        String activityId = req.getParameter("activityId");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("clueId", clueId);
        map.put("activityId", activityId);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean success = clueService.unbund(map);
        System.out.println(success);
        //返回结果
        PrintJson.printJsonFlag(resp, success);
    }

}
