package com.haochen.xmlbuilder.util;

import com.haochen.xmlbuilder.XmlReader;
import com.haochen.xmlbuilder.XmlWriter;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Haochen on 2017/1/7.
 */
public class XmlUtil {
    public static String xmlString(Object obj) {
        return xmlString(obj, obj.getClass().getSimpleName());
    }

    public static String xmlString(Object obj, String rootTagName) {
        BaseUtil util;
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
        BaseUtil util;

        File file = new File("xml_temp_" + new Date().getTime() + ".xml");
        new XmlWriter().write(xmlString, file);
        Object obj = new XmlReader().read(file);
        file.delete();
        return obj;
    }
}
