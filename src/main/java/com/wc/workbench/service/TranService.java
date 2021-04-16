package com.wc.workbench.service;

import com.wc.workbench.domain.Tran;
import com.wc.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {

    boolean createTran(Tran tran, String customerName);

    Tran getTranById(String id);

    List<TranHistory> queryHistoriesByTranId(String tranId);

    boolean change(Tran tran);

    Map<String, Object> getCharts();

}
