package com.wc.workbench.service.Impl;

import com.wc.utils.SqlSessionUtil;
import com.wc.workbench.dao.CustomerDao;
import com.wc.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public List<String> queryCustomerNames(String name) {

        return customerDao.queryCustomerNames(name);
    }
}
