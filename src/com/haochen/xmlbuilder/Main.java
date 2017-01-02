package com.haochen.xmlbuilder;

import com.haochen.xmlbuilder.exception.IllegalFileFormatException;

import java.io.File;

/**
 * Created by Haochen on 2017/1/2.
 */
public class Main {
    public static void main(String[] args) {
        try {
            File file = new File("E://test.xml");
            new XMLWriter().write(new Test(), file);
            System.out.println(new XMLReader().xmlString(file));
//            new XMLReader().read(file);
        } catch (IllegalFileFormatException e) {
            e.printStackTrace();
        }
    }
}
