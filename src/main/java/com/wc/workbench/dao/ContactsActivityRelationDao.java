package com.wc.workbench.dao;

import com.wc.workbench.domain.ClueActivityRelation;
import com.wc.workbench.domain.ContactsActivityRelation;

import java.util.List;

public interface ContactsActivityRelationDao {


    boolean saveContactsActivityRelations(List<ContactsActivityRelation> contactsActivityRelation);
}
