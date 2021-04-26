package com.wc.settings.dao;


import com.wc.settings.domain.DicType;
import com.wc.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicTypeDao {
    List<DicType> getAllDicTypes();

    boolean saveDicType(DicType dicType);

    DicType queryDicTypeByCode(String code);

    boolean updateDicTypeByOriginalCode(Map<String, Object> map);

    boolean updateDicType1(DicType dicType);

    int countTypeByCodeExceptOriginalOne(String code);

    boolean delDicTypesByCodes(String[] codes);

}
