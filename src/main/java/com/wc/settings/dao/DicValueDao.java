package com.wc.settings.dao;

import com.wc.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicValueDao {
    List<DicValue> getAllDicValuesByTypeCode(String dicTypeCode);

    boolean updateTypeCodeByOriginalCode(Map<String, Object> map);

    boolean delDicValuesByCodes(String[] codes);

    List<DicValue> getAllDicValues();

    boolean saveDicValue(DicValue dicValue);

    boolean delDicValuesByIds(String[] ids);

    DicValue queryDicValueById(String id);

    boolean editDicValue(DicValue dicValue);
}
