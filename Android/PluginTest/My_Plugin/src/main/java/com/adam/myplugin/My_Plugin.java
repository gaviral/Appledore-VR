package com.adam.myplugin;


public class My_Plugin
{
    int counter = 0;

    public static String getMessage() {
        return "This is a test message!";
    }

    public int getNextNumber() {
        counter++;
        return counter % 500;
    }
}
