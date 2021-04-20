package com.wc.workbench.dao;


import com.wc.workbench.domain.Activity;
import com.wc.workbench.domain.Clue;
import com.wc.workbench.domain.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public interface ClueDao {
    boolean saveClue(Clue clue);

    List<Clue> queryClueListByClue(Clue clue);

    Clue queryClueById(String id);

    List<Activity> queryRelatedActivityByClueId(String id);

    boolean delRelationByClueIdAndActivityId(Map<String, String> map);

    List<Activity> queryAllActivities();

    List<Activity> queryAllActivitiesByNameAndNotRelatedWithClue(Map<String, String> map);

    boolean insertRelation(ClueActivityRelation clueActivityRelation);

    List<Activity> queryActivityByName(String aname);

    Clue queryClueById2(String clueId);

    boolean delById(String clueId);

    List<Clue> queryPageCluesByConditions(Map<String, Object> map);

    int queryTotalClueCountByConditions(Map<String, Object> map);

    boolean removeClueById(String id);
}
