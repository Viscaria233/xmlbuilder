package com.haochen.xmlbuilder.xmlutil;

/**
 * Created by Haochen on 2017/1/7.
 */
public abstract class XMLUtil {
    protected Object obj;

    XMLUtil(Object obj) {
        this.obj = obj;
    }

    public abstract String xmlNodeString();
    public abstract Object xmlObject();
}
