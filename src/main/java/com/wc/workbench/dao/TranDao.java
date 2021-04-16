package com.wc.workbench.dao;

import com.wc.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    boolean createTran(Tran transaction);

    Tran queryTranById(String id);

    boolean updateTranStage(Tran tran);

    int queryCount();

    List<Map<String, Object>> queryCharts();
}
