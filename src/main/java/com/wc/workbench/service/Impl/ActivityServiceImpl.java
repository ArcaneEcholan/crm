package com.wc.workbench.service.Impl;

import com.wc.settings.domain.User;
import com.wc.utils.SqlSessionUtil;
import com.wc.vo.PageVo;
import com.wc.workbench.dao.ActivityDao;
import com.wc.workbench.dao.ActivityRemarkDao;
import com.wc.workbench.domain.Activity;
import com.wc.workbench.domain.ActivityRemark;
import com.wc.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    ActivityRemarkDao activityRemarkDao = null;

    public List<User> getUserList() {
        return activityDao.queryAllUsers();
    }

    public boolean createActivity(Activity activity) {
        return activityDao.saveActivity(activity);
    }

    public PageVo<Activity> page(Map<String, Object> map) {
        //查询当页记录和总的记录数
        List<Activity> pageAct = activityDao.queryActivityPage(map);    //查询当页所有活动
        int totalActCount = activityDao.queryActivityPageCount(map);    //查询所有符合查询条件的活动数量
        //计算总页数
        Integer pageSize = (Integer)map.get("pageSize");
        int totalPages = totalActCount % pageSize ==0 ?  totalActCount / pageSize : totalActCount / pageSize + 1;

        //int totalCount, int totalPages, List<T> list
        return new PageVo<Activity>(totalActCount, totalPages, pageAct);
    }

    public int queryTotalCount(Map<String, Object> map) {

        System.out.println("<<<<<<<dao queryActivityPageCount start>>>>>>>>>>>");

        //每次执行service方法时都更新dao对象到当前连接

        int total = activityDao.queryActivityPageCount(map);

        System.out.println("<<<<<<<dao queryActivityPageCount end>>>>>>>>>>>");

        return total;
    }
    // //page start
    //    //null
    //    //{owner=wenchao, name=卖火柴, pageSize=2, startTime=2021-02-10, endTime=2021-10-10, skipCount=0}
    //    //===============================sqlsession set===============================
    //    //org.apache.ibatis.session.defaults.DefaultSqlSession@1941bf90
    //    //===============================sqlsession set===============================
    //    //<<<<<<<service:page start>>>>>>>>>>>
    //    //<<<<<<<dao queryActivityPage start>>>>>>>>>>>
    //    //2021-04-04 14:54:19,864 [http-apr-8080-exec-82] DEBUG [com.wc.workbench.dao.ActivityDao.queryActivityPage] - ==>  Preparing: SELECT a.`name` owner, b.`name`, b.`startDate`, b.`endDate` FROM `tbl_user` a JOIN `tbl_activity` b ON a.id = b.owner WHERE a.name = ? and b.name = ? and b.startDate >= ? and b.endDate <= ?
    //    //2021-04-04 14:54:19,865 [http-apr-8080-exec-82] DEBUG [com.wc.workbench.dao.ActivityDao.queryActivityPage] - ==> Parameters: wenchao(String), 卖火柴(String), 2021-02-10(String), 2021-10-10(String)
    //    //2021-04-04 14:54:19,867 [http-apr-8080-exec-82] DEBUG [com.wc.workbench.dao.ActivityDao.queryActivityPage] - <==      Total: 5
    //    //<<<<<<<dao queryActivityPage end>>>>>>>>>>>
    //    //<<<<<<<service:page end>>>>>>>>>>>
    //    //-----session commit------
    //    //===============================sqlsession destroy===============================
    //    //org.apache.ibatis.session.defaults.DefaultSqlSession@1941bf90
    //    //===============================sqlsession destroy===============================
    //    //null
    //    //===============================sqlsession set===============================
    //    //org.apache.ibatis.session.defaults.DefaultSqlSession@67a7ca03
    //    //===============================sqlsession set===============================
    //    //<<<<<<<service:queryTotalCount start>>>>>>>>>>>
    //    //<<<<<<<dao queryActivityPageCount start>>>>>>>>>>>
    //    //2021-04-04 14:54:19,870 [http-apr-8080-exec-82] DEBUG [com.wc.workbench.dao.ActivityDao.queryActivityPageCount] - ==>  Preparing: SELECT count(*) FROM `tbl_user` a JOIN `tbl_activity` b ON a.id = b.owner WHERE a.name = ? and b.name = ? and b.startDate >= ? and b.endDate <= ?
    //    //2021-04-04 14:54:19,870 [http-apr-8080-exec-82] DEBUG [com.wc.workbench.dao.ActivityDao.queryActivityPageCount] - ==> Parameters: wenchao(String), 卖火柴(String), 2021-02-10(String), 2021-10-10(String)
    //    //2021-04-04 14:54:19,872 [http-apr-8080-exec-82] DEBUG [com.wc.workbench.dao.ActivityDao.queryActivityPageCount] - <==      Total: 1
    //    //<<<<<<<dao queryActivityPageCount end>>>>>>>>>>>
    //    //<<<<<<<service:queryTotalCount end>>>>>>>>>>>
    //    //-----session commit------
    //    //===============================sqlsession destroy===============================
    //    //org.apache.ibatis.session.defaults.DefaultSqlSession@67a7ca03
    //    //===============================sqlsession destroy===============================
    //    //5
    //    //[com.wc.workbench.domain.Activity@2160975e, com.wc.workbench.domain.Activity@32da7d89, com.wc.workbench.domain.Activity@612491f8, com.wc.workbench.domain.Activity@1a90f41c, com.wc.workbench.domain.Activity@3f6f5a77]
    //    //page end

    public boolean deleteActivity(String[] ids) {
        activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

        boolean flag = true;

        int count1 = activityRemarkDao.getDeleteActivityRemarkCount(ids);
        int count2 = activityRemarkDao.deleteActivityRemark(ids);
        if(count1 != count2) {
            flag = false;
        }

        int count3 = activityDao.deleteActivity(ids);
        if(count3 != ids.length) {
            flag = false;
        }
        return flag;
    }

//    public boolean editActivity(String aid) {
//        activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
//
//        if(activityDao.editActivity(aid) == 1) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    public Activity queryActivity(String aid) {
        activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        return activityDao.queryActivityByActivityId(aid);

    }

    public boolean editActivity(Activity activity) {
        activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        return activityDao.updateActivity(activity);
    }

    public Activity showDetail(String id) {
        activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        return activityDao.queryActivityDetail(id);

    }

    public List<ActivityRemark> getRemarksByActivityId(String id) {
        activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        return activityRemarkDao.queryRemarksByActivityId(id);
    }

    public boolean deleteRemarkById(String id) {
        activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        return activityRemarkDao.deleteRemarkById(id);
    }

    public boolean saveRemark(ActivityRemark activityRemark) {

        activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        return activityRemarkDao.saveRemark(activityRemark);
    }

    public boolean updateRemark(ActivityRemark activityRemark) {
        activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        return activityRemarkDao.updateRemark(activityRemark);


    }
}
