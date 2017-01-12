package com.haochen.xmlbuilder.xmlutil;

/**
 * Created by Haochen on 2017/1/11.
 */
public class EmptyUtil extends BaseUtil {
    @Override
    public String xmlString(Object obj, String rootTagName) {
        return "";
    }

    @Override
    public Object xmlObject() {
        return null;
    }
}
