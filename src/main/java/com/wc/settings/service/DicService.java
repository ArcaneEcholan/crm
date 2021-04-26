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

    DicType getDicTypeByCode(String code);

    /**
     * 如果修改编码导致重复，修改失败；否则执行修改，同时修改所有关联的值的编码外键
     * @param dicType
     * @param originalCode
     */
    void editDicType(DicType dicType, String originalCode);

    boolean delDicTypesByCodes(String[] codes);


    List<DicValue> getAllDicValues();

    List<String> getAllDicTypeCode();

}
