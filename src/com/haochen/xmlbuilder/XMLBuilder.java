package com.haochen.xmlbuilder;

import com.haochen.xmlbuilder.annotation.XMLBean;
import com.haochen.xmlbuilder.annotation.XMLNode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Haochen on 2017/1/2.
 */
public class XMLBuilder {

    public String build(Object obj) {
        Class clazz = obj.getClass();
        if (clazz.isAnnotationPresent(XMLBean.class)) {
            XMLBean bean = (XMLBean) clazz.getAnnotation(XMLBean.class);
            
            StringBuilder b = new StringBuilder(xmlHead());
            String name = "".equals(bean.name()) ? clazz.getSimpleName() : bean.name();

            //tag start
            b.append("<").append(name).append(" class=\"").append(clazz.getName()).append("\">\n");

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                try {
                    b.append(xmlNode(obj, field, 1));
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            //tag end
            b.append("</").append(name).append(">\n");
            return b.toString();
        } else {
            return "";
        }
    }

    private String xmlHead() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    }

    private String xmlNode(Object obj, Field field, int inset) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
        field.setAccessible(true);
        if (field.isAnnotationPresent(XMLNode.class) && field.get(obj) != null) {
            String tab = "";
            for (int i = 0; i < inset; ++i) {
                tab += "\t";
            }
            XMLNode node = field.getAnnotation(XMLNode.class);
            String name = "".equals(node.name()) ? field.getName() : node.name();

            Class clazz = field.getType();

            //tag start
            StringBuilder b = new StringBuilder(tab + "<" + name);
            b.append("\n").append(tab).append("\tclass=\"").append(clazz.getName()).append("\"")
                    .append("\n").append(tab).append("\tfield=\"").append(field.getName()).append("\">\n");
            if (clazz.isPrimitive()) {
                b.append(tab).append("\t");
                Class fClass = Field.class;
                String uName = Character.toUpperCase(clazz.getSimpleName().charAt(0))
                        + clazz.getSimpleName().substring(1);
                Method get = fClass.getMethod("get" + uName, Object.class);
                Class bigClass = Class.forName(uName);
                b.append(clazz.cast(get.invoke(field, obj)));
                b.append("\n");
            }
//            if (clazz == int.class) {
//                b.append(tab).append("\t").append(field.getInt(obj)).append("\n");
//            } else if (clazz == double.class) {
//                b.append(tab).append("\t").append(field.getDouble(obj)).append("\n");
//            } else if (clazz == float.class) {
//                b.append(tab).append("\t").append(field.getFloat(obj)).append("\n");
//            } else if (clazz == char.class) {
//                b.append(tab).append("\t").append(field.getChar(obj)).append("\n");
//            } else if (clazz == byte.class) {
//                b.append(tab).append("\t").append(field.getByte(obj)).append("\n");
//            } else if (clazz == boolean.class) {
//                b.append(tab).append("\t").append(field.getBoolean(obj)).append("\n");
//            } else if (clazz == String.class) {
//                b.append(tab).append("\t").append(field.get(obj)).append("\n");
//            }
            else {
                Object value = field.get(obj);
                Field[] fields = clazz.getDeclaredFields();
                for (Field f : fields) {
                    b.append(xmlNode(value, f, inset + 1));
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
