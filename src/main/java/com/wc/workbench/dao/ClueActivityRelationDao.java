package com.wc.workbench.dao;


import com.wc.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    List<ClueActivityRelation> queryRelationByClueId(String clueId);

    boolean delByClueId(String clueId);
}
