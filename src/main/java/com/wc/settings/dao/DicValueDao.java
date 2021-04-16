package com.wc.settings.dao;

import com.wc.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getAllDicValues(String dicTypeCode);
}
