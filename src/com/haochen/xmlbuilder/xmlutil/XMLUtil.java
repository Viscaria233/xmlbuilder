package com.haochen.xmlbuilder.xmlutil;

import com.haochen.xmlbuilder.XMLReader;
import com.haochen.xmlbuilder.XMLWriter;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Haochen on 2017/1/7.
 */
public class XMLUtil {
    public static String xmlString(Object obj) {
        return xmlString(obj, obj.getClass().getSimpleName());
    }

    public static String xmlString(Object obj, String rootTagName) {
        BaseUtil util = null;
        if (obj instanceof Map) {
            util = new MapUtil();
        } else if (obj instanceof List) {
            util = new ListUtil();
        } else if (!(obj instanceof Collection)) {
            util = new BeanUtil();
        } else {
            util = new EmptyUtil();
        }
        return util.xmlString(obj, rootTagName);
    }

    public static Object xmlObject(String xmlString) {
        File file = new File("xml_temp_" + new Date().getTime() + ".xml");
        new XMLWriter().write(xmlString, file);
        Object obj = new XMLReader().read(file);
        file.delete();
        return obj;
    }
}
