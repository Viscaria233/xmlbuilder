package com.haochen.xmlbuilder.exception;

/**
 * Created by Haochen on 2017/1/2.
 */
public class IllegalFileFormatException extends Exception {
    public IllegalFileFormatException() {
        super("File format must be \".xml\"");
    }
}
