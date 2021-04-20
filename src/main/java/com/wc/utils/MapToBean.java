package com.wc.utils;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class MapToBean {
    /**
     * 将map的值注入到javabean中
     * @param map:待封装的map
     * @param javabean:指定封装到那个javabean中
     * @return 封装成功返回传入的javabean；否则返回null
     */
    public static <T> T copyMapToBean(Map map, T javabean) {
        try {
            BeanUtils.populate(javabean, map);
            return javabean;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换字符串成整形
     * @param str-带转换的字符串
     * @param defaultValue-字符串格式错误后的默认值
     * @return 如果字符串格式正确，直接返回结果；否则返回默认值
     */
    public static int parseInt(String str, int defaultValue) {
        try {
            //如果字符串格式正确，直接返回结果
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        //如果字符串格式不正确，返回默认值
        return defaultValue;
    }
}
