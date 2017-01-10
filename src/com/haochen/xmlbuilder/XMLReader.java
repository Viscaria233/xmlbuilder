package com.haochen.xmlbuilder;

import com.haochen.xmlbuilder.exception.IllegalFileFormatException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Haochen on 2017/1/2.
 */
public class XMLReader {

    protected Map<Class, Map<Integer, Object>> map;

    public Object read(File file) throws IllegalFileFormatException {
        if (!file.getName().endsWith(".xml")) {
            throw new IllegalFileFormatException();
        }
        this.map = new HashMap<>();
        Object obj = null;

        SAXReader reader = new SAXReader(DocumentFactory.getInstance());
        try {
            Document document = reader.read(file);
            Element root = document.getRootElement();
            obj = getObject(root);
        } catch (DocumentException | ClassNotFoundException | IllegalAccessException | NoSuchFieldException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        } finally {
            this.map.clear();
            this.map = null;
        }
        return obj;
    }

    public Object read(String xmlString) throws IllegalFileFormatException {
        File file = new File("xml_temp_" + new Date().getTime() + ".xml");
        new XMLWriter().write(xmlString, file);
        Object obj = new XMLReader().read(file);
        file.delete();
        return obj;
    }

    protected Object getObject(Element element) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        String className = element.attributeValue("class");
        Object obj = null;
        if ("java.lang.String".equals(className)) {
            return element.getStringValue().replaceAll("[\\n|\\t]", "");
        } else if (className.contains(".")) {
            Class clazz = Class.forName(className);
            if (!this.map.containsKey(clazz)) {
                this.map.put(clazz, new HashMap<Integer, Object>());
            }

            Map<Integer, Object> objectMap = this.map.get(clazz);
            int id = Integer.parseInt(element.attributeValue("id"));
            if (objectMap.containsKey(id)) {
                obj = objectMap.get(id);
            } else {
                obj = clazz.newInstance();
                objectMap.put(id, obj);

                Iterator iterator = element.elementIterator();
                while (iterator.hasNext()) {
                    Element e = (Element) iterator.next();
                    Object value = getObject(e);
                    String fieldName = e.attributeValue("field");
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(obj, value);
                }
            }
        } else {
            if ("char".equals(className)) {
                return element.getStringValue().replaceAll("[\\n|\\t]", "").charAt(0);
            } else {
                String uName = Character.toUpperCase(className.charAt(0))
                        + className.substring(1);
                Class primitiveClass = Class.forName("java.lang." + (
                                "Int".equals(uName) ? "Integer" : uName
                        ));
                Method parse = primitiveClass.getDeclaredMethod("parse" + uName, String.class);
                String str = element.getStringValue();
                return parse.invoke(null, str.replaceAll("[\\n|\\t]", ""));
            }
        }
        return obj;
    }

    public String xmlString(File file) throws IllegalFileFormatException {
        if (!file.getName().endsWith(".xml")) {
            throw new IllegalFileFormatException();
        }
        Reader reader = null;
        StringBuilder b = new StringBuilder();
        try {
            reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            char[] buf = new char[128];
            while (true) {
                int n = reader.read(buf);
                if (n > 0) {
                    b.append(buf, 0, n);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return b.toString();
    }
}
