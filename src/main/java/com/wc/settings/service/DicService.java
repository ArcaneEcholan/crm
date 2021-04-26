package com.wc.settings.service;

import com.wc.settings.domain.DicType;
import com.wc.settings.domain.DicValue;
import com.wc.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface DicService {
    Map<String, List<DicValue>> getAllDic();

    boolean saveDicType(DicType dicType);

    List<DicType> getAllDicTypes();
}
