package com.haochen.xmlbuilder;

import com.haochen.xmlbuilder.exception.IllegalFileFormatException;

import java.io.*;

/**
 * Created by Haochen on 2017/1/2.
 */
public class XMLWriter {
    public void write(Object obj, File file) throws IllegalFileFormatException {
        if (!file.getName().endsWith(".xml")) {
            throw new IllegalFileFormatException();
        }
        writeStringToFile(new XMLBuilder().build(obj), file);
    }

    public void write(String xmlString, File file) throws IllegalFileFormatException {
        if (!file.getName().endsWith(".xml")) {
            throw new IllegalFileFormatException();
        }
        writeStringToFile(xmlString, file);
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
