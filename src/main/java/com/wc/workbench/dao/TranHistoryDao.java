package com.wc.workbench.dao;

import com.wc.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    boolean save(TranHistory tranHistory);

    List<TranHistory> queryHistoriesByTranId(String tranId);
}
