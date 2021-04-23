package com.wc.workbench.dao;


import com.wc.workbench.domain.Activity;
import com.wc.workbench.domain.ClueActivityRelation;
import org.apache.ibatis.annotations.Param;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public interface ClueActivityRelationDao {

    List<ClueActivityRelation> queryRelationByClueId(String clueId);

    boolean delByClueId(String clueId);

    boolean insertRelations(List<ClueActivityRelation> list);

    boolean test(@Param("ajsld") ArrayList arrayList);
}
