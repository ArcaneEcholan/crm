package com.wc.workbench.service;

import com.wc.settings.domain.User;
import com.wc.vo.PageVo;
import com.wc.workbench.domain.Activity;
import com.wc.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    List<User> getUserList();

    boolean createActivity(Activity activity);

    PageVo<Activity> page(Map<String, Object> map);

    int queryTotalCount(Map<String, Object> map);

    boolean deleteActivity(String[] ids);

//    boolean editActivity(String aid);

    Activity queryActivity(String aid);

    boolean editActivity(Activity activity);

    Activity showDetail(String id);

    List<ActivityRemark> getRemarksByActivityId(String id);

    boolean deleteRemarkById(String id);

    boolean saveRemark(ActivityRemark activityRemark);

    boolean updateRemark(ActivityRemark activityRemark);
}
