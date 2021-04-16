package com.wc.settings.service.Impl;

import com.wc.settings.dao.DicTypeDao;
import com.wc.settings.dao.DicValueDao;
import com.wc.settings.domain.DicType;
import com.wc.settings.domain.DicValue;
import com.wc.settings.domain.User;
import com.wc.settings.service.DicService;
import com.wc.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    /**
     * 查询数据库中所有的数据字典
     * @return 返回数据库中所有类型的数据字典的map
     */
    public Map<String, List<DicValue>> getAllDic() {
        //获取数据字典类型
        List<DicType> dicTypeList = dicTypeDao.getAllDicTypes();

        Map<String,  List<DicValue>> dicMap = new HashMap<String, List<DicValue>>();

        //依据每个类型查询相应的字典值列表
        for (DicType dicType : dicTypeList) {
            String dicTypeCode = dicType.getCode();
            List<DicValue> dicValueList = dicValueDao.getAllDicValues(dicTypeCode);
            dicMap.put(dicTypeCode, dicValueList);
        }

        //返回字典map
        return dicMap;
    }

}
