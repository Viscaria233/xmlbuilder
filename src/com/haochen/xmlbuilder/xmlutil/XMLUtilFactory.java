package com.haochen.xmlbuilder.xmlutil;

import com.haochen.xmlbuilder.annotation.XMLBean;
import com.haochen.xmlbuilder.annotation.XMLList;
import com.haochen.xmlbuilder.annotation.XMLMap;

/**
 * Created by Haochen on 2017/1/7.
 */
public class XMLUtilFactory {
    public static XMLUtil getXMLUtil(Object obj) {
        Class clazz = obj.getClass();
        if (clazz.isAnnotationPresent(XMLMap.class)) {
            return new XMLMapUtil(obj);
        } else if (clazz.isAnnotationPresent(XMLList.class)) {
            return new XMLListUtil(obj);
        } else if (clazz.isAnnotationPresent(XMLBean.class)) {
            return new XMLBeanUtil(obj);
        } else {
            return new EmptyUtil();
        }
    }
}
