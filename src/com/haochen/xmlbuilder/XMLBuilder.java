package com.haochen.xmlbuilder;

import com.haochen.xmlbuilder.annotation.XMLBean;
import com.haochen.xmlbuilder.annotation.XMLNode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Haochen on 2017/1/2.
 */
public class XMLBuilder {

    protected Map<Class, List<Object>> map;

    public String build(Object obj) {
        Class clazz = obj.getClass();
        if (clazz.isAnnotationPresent(XMLBean.class)) {
            XMLBean bean = (XMLBean) clazz.getAnnotation(XMLBean.class);
            this.map = new HashMap<>();
            
            StringBuilder b = new StringBuilder(xmlHead());
            String name = "".equals(bean.name()) ? clazz.getSimpleName() : bean.name();

            //tag start
            b.append("<").append(name).append(" class=\"").append(clazz.getName()).append("\" id=\"0\">\n");
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
            b.append("</").append(name).append(">\n");
            return b.toString();
        } else {
            return "";
        }
    }

    protected String xmlHead() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    }

    protected String xmlNode(Object obj, Field field, int inset) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
        field.setAccessible(true);
        if (field.isAnnotationPresent(XMLNode.class) && field.get(obj) != null) {
            String tab = "";
            for (int i = 0; i < inset; ++i) {
                tab += "\t";
            }
            XMLNode node = field.getAnnotation(XMLNode.class);
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
                int[] val = getIDAndExists(value);

                b.append("\n").append(tab).append("\t\tid=\"").append(val[0]).append("\">\n");

                if (val[1] == 0) {
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

    /**
     *
     * @param obj
     * @return int[2] val;
     *          val[0] : the id (index of obj)
     *          val[1] == 0 : did not exists, but added it
     *          val[1] == 1 : already exists
     */
    protected int[] getIDAndExists(Object obj) {
        if (!this.map.containsKey(obj.getClass())) {
            this.map.put(obj.getClass(), new ArrayList<>());
        }
        List<Object> list = this.map.get(obj.getClass());

        int[] val = {0, 0};
        for (int i = 0; i < list.size(); ++i) {
            if (obj == list.get(i)) {
                val[0] = i;
                val[1] = 1;
                break;
            }
        }
        if (val[1] == 0) {
            val[0] = list.size();
            list.add(obj);
        }
        return val;
    }
}
