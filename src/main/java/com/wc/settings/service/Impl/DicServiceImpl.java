package com.wc.settings.service.Impl;

import com.wc.settings.dao.DicTypeDao;
import com.wc.settings.dao.DicValueDao;
import com.wc.settings.domain.DicType;
import com.wc.settings.domain.DicValue;
import com.wc.settings.domain.User;
import com.wc.settings.service.DicService;
import com.wc.utils.SqlSessionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    /**
     * 返回数据库中所有类型数据字典值，以”类型-值表“的map形式返回
     * @return 返回数据库中所有类型的数据字典的map
     */
    public Map<String, List<DicValue>> getAllDic() {
        //查询所有字典类型
        List<DicType> allDicTypes = dicTypeDao.getAllDicTypes();
        Map<String, List<DicValue>> map = new HashMap<>();

        //根据每个字典类型查值表
        for (DicType dicType : allDicTypes) {
            String type = dicType.getCode();
            List<DicValue> dicValues = dicValueDao.getAllDicValues(type);
            map.put(type, dicValues);
        }

        //返回类型-值表map
        return map;
    }

    @Override
    public boolean saveDicType(DicType dicType) {
        return dicTypeDao.saveDicType(dicType);
    }

    @Override
    public List<DicType> getAllDicTypes() {
        return dicTypeDao.getAllDicTypes();
    }
}
