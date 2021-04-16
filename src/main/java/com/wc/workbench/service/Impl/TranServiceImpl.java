package com.wc.workbench.service.Impl;
import com.wc.utils.DateTimeUtil;
import com.wc.utils.SqlSessionUtil;
import com.wc.utils.UUIDUtil;
import com.wc.workbench.dao.*;
import com.wc.workbench.domain.Customer;
import com.wc.workbench.domain.Tran;
import com.wc.workbench.domain.TranHistory;
import com.wc.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public boolean createTran(Tran tran, String customerName) {
        boolean flag = true;

        //获取客户id
        Customer customer = customerDao.queryCustomerByName(customerName);
        if(customer == null) {
            customer = new Customer();

            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setOwner(tran.getOwner());
            customer.setCreateBy(tran.getCreateBy());
            customer.setContactSummary(tran.getContactSummary());
            customer.setDescription(tran.getDescription());
            customer.setCreateTime(DateTimeUtil.getSysTime());

            if(!customerDao.save(customer)) {
                flag = false;
            }
        }

        //写入交易
        tran.setCustomerId(customer.getId());
        if(!tranDao.createTran(tran)) {
            flag = false;
        }

        //写入交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setStage(tran.getStage());
        tranHistory.setTranId(tran.getId());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setCreateTime(tran.getCreateTime());
        tranHistory.setId(UUIDUtil.getUUID());

        if(!tranHistoryDao.save(tranHistory)){
            flag = false;
        }

        return flag;
    }

    public Tran getTranById(String id) {
        return tranDao.queryTranById(id);
    }

    public List<TranHistory> queryHistoriesByTranId(String tranId) {
        return tranHistoryDao.queryHistoriesByTranId(tranId);
    }

    public boolean change(Tran tran) {

        boolean flag = true;

        if(!tranDao.updateTranStage(tran)) {
            flag = false;
        }

        TranHistory tranHistory = new TranHistory();

        tranHistory.setStage(tran.getStage());
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setCreateBy(tran.getEditBy());
        tranHistory.setCreateTime(tran.getEditTime());
        tranHistory.setExpectedDate(tran.getExpectedDate());

        if(!tranHistoryDao.save(tranHistory)) {
            flag = false;
        }

        return flag;
    }

    public Map<String, Object> getCharts() {

        Map<String, Object> map = new HashMap<String, Object>();
        //获取total
        int total = tranDao.queryCount();

        //获取json的集合
        List<Map<String, Object>> valueList = tranDao.queryCharts();

        map.put("total", total);
        map.put("valueList", valueList);
        return map;
    }
}
