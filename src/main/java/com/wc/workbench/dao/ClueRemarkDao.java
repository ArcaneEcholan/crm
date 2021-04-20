package com.wc.workbench.dao;

import com.wc.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> queryRemarkByClueId(String clueId);

    boolean delByClueId(String clueId);

    boolean saveClueRemark(ClueRemark clueRemark);
}
