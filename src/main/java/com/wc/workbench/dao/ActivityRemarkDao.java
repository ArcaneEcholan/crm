package com.wc.workbench.dao;

import com.wc.settings.domain.User;
import com.wc.workbench.domain.Activity;
import com.wc.workbench.domain.ActivityRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityRemarkDao {

    int getDeleteActivityRemarkCount(@Param("ids") String[] ids);

    int deleteActivityRemark(@Param("ids") String[] ids);

    List<ActivityRemark> queryRemarksByActivityId(String id);

    boolean deleteRemarkById(String id);

    boolean saveRemark(ActivityRemark activityRemark);

    boolean updateRemark(ActivityRemark activityRemark);
}
