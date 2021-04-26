package com.wc.settings.dao;


import com.wc.settings.domain.DicType;

import java.util.List;

public interface DicTypeDao {
    List<DicType> getAllDicTypes();

    boolean saveDicType(DicType dicType);
}
