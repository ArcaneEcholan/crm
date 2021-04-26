package com.wc.settings.service.Impl;

import com.wc.settings.dao.DicTypeDao;
import com.wc.settings.dao.DicValueDao;
import com.wc.settings.domain.DicType;
import com.wc.settings.domain.DicValue;
import com.wc.settings.service.DicService;
import com.wc.utils.SortUtil;
import com.wc.utils.SqlSessionUtil;

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
            List<DicValue> dicValues = dicValueDao.getAllDicValuesByTypeCode(type);
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

    @Override
    public DicType getDicTypeByCode(String code) {
        return dicTypeDao.queryDicTypeByCode(code);
    }

    @Override
    public void editDicType(DicType dicType, String originalCode) {

        String code = dicType.getCode();            //修改后的code
        Map<String, Object> map = new HashMap<>();
        map.put("dicType",dicType);
        map.put("originalCode",originalCode);
        if(code.equals(originalCode)) {                                      //如果没有改动编号，直接修改
            if(!dicTypeDao.updateDicType1(dicType)) {
                throw new RuntimeException("codeErr");
            }
        } else if(0 < dicTypeDao.countTypeByCodeExceptOriginalOne(code)) {   //如果改动了编号并且编号已存在，修改失败
            throw new RuntimeException("codeExists");
        } else {
            if(!dicTypeDao.updateDicTypeByOriginalCode(map)) {                  //如果编号修改合法，执行修改
                throw new RuntimeException("CodeErr:updateTypeFail");
            }
            if(!dicValueDao.updateTypeCodeByOriginalCode(map)) {                  //如果编号修改合法，修改value表中对应的typeCode
                throw new RuntimeException("codeErr:updateValueFail");
            }
        }
    }

    @Override
    public boolean delDicTypesByCodes(String[] codes) {
        boolean flag = true;

        if(!dicTypeDao.delDicTypesByCodes(codes)) {
            flag=false;
        }

        if(!dicValueDao.delDicValuesByCodes(codes)) {
            flag=false;
        }

        return flag;
    }

    @Override
    public List<DicValue> getAllDicValues() {
        List<DicValue> allDicValues = dicValueDao.getAllDicValues();

        SortUtil.quickSortDicValues(allDicValues,0,allDicValues.size() - 1);

        return allDicValues;
    }

    @Override
    public List<String> getAllDicTypeCode() {
        return dicTypeDao.queryAllDicTypeCode();
    }
}
