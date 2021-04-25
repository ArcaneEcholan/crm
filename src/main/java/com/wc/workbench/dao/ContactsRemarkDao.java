package com.wc.workbench.dao;

import com.wc.workbench.domain.ContactsRemark;

import java.util.List;

public interface ContactsRemarkDao {
    boolean saveContactsRemarks(List<ContactsRemark> contactsRemarks);
}
