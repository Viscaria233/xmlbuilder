package com.haochen.xmlbuilder.xmlutil;

/**
 * Created by Haochen on 2017/1/7.
 */
public class EmptyUtil extends XMLUtil {
    EmptyUtil() {
        super(null);
    }

    @Override
    public String xmlNodeString() {
        return null;
    }

    @Override
    public Object xmlObject() {
        return null;
    }
}
