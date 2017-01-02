package com.haochen.xmlbuilder;

import com.haochen.xmlbuilder.exception.IllegalFileFormatException;

import java.beans.Encoder;
import java.io.*;

/**
 * Created by Haochen on 2017/1/2.
 */
public class XMLWriter {
    public void write(Object obj, File file) throws IllegalFileFormatException {
        if (!file.getName().endsWith(".xml")) {
            throw new IllegalFileFormatException();
        }
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");

            String str = new XMLBuilder().build(obj);
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
