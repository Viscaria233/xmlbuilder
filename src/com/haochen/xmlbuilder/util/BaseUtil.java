package com.haochen.xmlbuilder.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Haochen on 2017/1/7.
 */
public abstract class BaseUtil {
    protected Map<Class, List<Object>> map = new HashMap<>();

    protected String xmlHead() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    }

    public String xmlString(Object obj) {
        return xmlString(obj, obj.getClass().getSimpleName());
    }

    public abstract String xmlString(Object obj, String rootTagName);
    public abstract Object xmlObject(String xmlString);
}
