package com.wc.workbench.dao;

import com.wc.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer queryCustomerByName(String companyName);

    boolean save(Customer company);

    List<String> queryCustomerNames(String name);
}
