package com.wc.workbench.dao;

import com.wc.settings.domain.User;
import com.wc.workbench.domain.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    List<User> queryAllUsers();

    boolean saveActivity(Activity activity);

    List<Activity> queryActivityPage(Map<String, Object> map);

    int queryActivityPageCount(Map<String, Object> map);

    int deleteActivity(@Param("ids") String[] ids);

//    int editActivity(String aid);

    Activity queryActivityByActivityId(String aid);

    boolean updateActivity(Activity activity);

    Activity queryActivityDetail(String id);

    List<Activity> queryNotBundedActivityByName(String aname);

    List<Activity> getAllRelatedActsByClueId(String clueId);
}
