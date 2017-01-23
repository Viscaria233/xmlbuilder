package com.haochen.xmlbuilder;

import com.haochen.xmlbuilder.util.XmlUtil;

import java.io.*;

/**
 * Created by Haochen on 2017/1/2.
 */
public class XmlWriter {
    public void write(Object obj, File file) {
        writeStringToFile(XmlUtil.xmlString(obj), file);
    }

    public void write(String xmlString, File file) {
        File target = file.getName().endsWith(".xml") ?
            file : new File(file.getParent(), file.getName() + ".xml");

        writeStringToFile(xmlString, target);
    }

    protected void writeStringToFile(String str, File file) {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            writer.write(str);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
