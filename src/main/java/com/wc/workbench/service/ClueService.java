package com.wc.workbench.service;

import com.wc.vo.PageVo;
import com.wc.workbench.domain.*;

import java.util.List;
import java.util.Map;

public interface ClueService {

    boolean saveClue(Clue clue);

    Clue getClueById(String id);

    List<Activity> getRelatedActivityByClueId(String id);

    boolean delRelationByClueIdAndActivityId(Map<String, String> map);

    List<Activity> getAllNotBundedActivities();

    List<Activity> getAllActivitiesByNameAndNotRelatedWithClue(Map<String, String> map);

    boolean relateClueAndActivities(String clueId, String[] actIds);

    List<Activity> searchActivityByName(String aname);

    boolean convert(String clueId, Tran transaction, String createBy);

    PageVo<Clue> page(Map<String, Object> map);

    boolean removeCluesByIds(String[] ids);

    Map<String, Object> getUserListAndClue(String id);

    boolean updateClue(Clue clue);

    boolean saveRemark(ClueRemark clueRemark);

    Map<String, Object> showClueRemarkList(String clueId);

    boolean editClueRemarkContentByRemarkId(ClueRemark clueRemark);

    boolean removeClueRemarkByRemarkId(String id);

    List<Activity> getNotBundedActivitiesByName(String aname);

    boolean bundActs(List<ClueActivityRelation> list);

    List<Activity> getAllRelatedActsByClueId(String clueId);

    boolean unbund(Map<String, Object> map);
}
