package com.haochen.xmlbuilder.util;

import com.haochen.xmlbuilder.XmlNode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Haochen on 2017/1/7.
 */
public class BeanUtil extends BaseUtil {
    @Override
    public String xmlString(Object obj, String rootTagName) {
        Class clazz = obj.getClass();
        this.map = new HashMap<>();
        StringBuilder b = new StringBuilder(xmlHead());

        //tag start
        b.append("<").append(rootTagName).append(" class=\"").append(clazz.getName()).append("\" id=\"0\">\n");
        List<Object> list = new ArrayList<>();
        list.add(obj);
        map.put(clazz, list);

        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                b.append(xmlNode(obj, field, 1));
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            this.map.clear();
            this.map = null;
        }

        //tag end
        b.append("</").append(rootTagName).append(">\n");
        return b.toString();
    }

    @Override
    public Object xmlObject(String xmlString) {
        return null;
    }

    protected String xmlNode(Object obj, Field field, int inset) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
        field.setAccessible(true);
        if (field.isAnnotationPresent(XmlNode.class) && field.get(obj) != null) {
            String tab = "";
            for (int i = 0; i < inset; ++i) {
                tab += "\t";
            }
            XmlNode node = field.getAnnotation(XmlNode.class);
            String name = "".equals(node.name()) ? field.getName() : node.name();

            Class fieldType = field.getType();

            //tag start
            StringBuilder b = new StringBuilder(tab + "<" + name);
            b.append("\n").append(tab).append("\t\tclass=\"").append(fieldType.getName()).append("\"")
                    .append("\n").append(tab).append("\t\tfield=\"").append(field.getName()).append("\"");

            //tag content
            if (fieldType.isPrimitive()) {
                b.append(">\n").append(tab).append("\t");

                String uName = Character.toUpperCase(fieldType.getSimpleName().charAt(0))
                        + fieldType.getSimpleName().substring(1);
                Method get = Field.class.getMethod("get" + uName, Object.class);
                Class primitiveClass = Class.forName("java.lang." + (
                        "Char".equals(uName) ? "Character" : (
                                "Int".equals(uName) ? "Integer" : uName
                        )));
                Method value = primitiveClass.getMethod(fieldType.getSimpleName() + "Value");

                b.append(value.invoke(get.invoke(field, obj))).append("\n");
            } else if (fieldType == String.class) {
                b.append(">\n").append(tab).append("\t").append(field.get(obj)).append("\n");
            } else {
                Object value = field.get(obj);
                boolean exists = true;

                List<Object> list = this.map.get(value.getClass());
                if (list == null) {
                    list = new ArrayList<>();
                    this.map.put(value.getClass(), list);
                    exists = false;
                }

                int id = list.indexOf(value);
                if (id == -1) {
                    id = list.size();
                    list.add(value);
                    exists = false;
                }


                b.append("\n").append(tab).append("\t\tid=\"").append(id).append("\">\n");

                if (!exists) {
                    Field[] fields = fieldType.getDeclaredFields();
                    for (Field f : fields) {
                        b.append(xmlNode(value, f, inset + 1));
                    }
                }
            }

            //tag end
            b.append(tab).append("</").append(name).append(">\n");
            return b.toString();
        } else {
            return "";
        }
    }
}
