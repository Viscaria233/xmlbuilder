package com.haochen.xmlbuilder;

import com.haochen.xmlbuilder.exception.IllegalFileFormatException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;

/**
 * Created by Haochen on 2017/1/2.
 */
public class XMLReader {

    public Object read(File file) throws IllegalFileFormatException {
        if (!file.getName().endsWith(".xml")) {
            throw new IllegalFileFormatException();
        }
        Object obj = null;

        SAXReader reader = new SAXReader(DocumentFactory.getInstance());
        try {
            Document document = reader.read(file);
            Element root = document.getRootElement();
            String className = root.attributeValue("class");
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return obj;
    }

//    private Object getObject(Element element) {
//        Class clazz = Class.forName(element.attributeValue("class"));
//        Object obj = clazz.newInstance();
//        return obj;
//    }

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
