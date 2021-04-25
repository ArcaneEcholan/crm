package com.wc.workbench.dao;

import com.wc.workbench.domain.CustomerRemark;

import java.util.List;

public interface CustomerRemarkDao {
    boolean saveCustomerRemarks(List<CustomerRemark> customerRemarks);
}
