package com.technoride.server.databaseupgrade.utils;

public class Strings {

    public static String replaceAll(String text,char oldChar,char newChar)
    {
        String newText="";
        for (int i=0;i<text.length();i++)
        {
            newText=text.replace(oldChar,newChar);
        }

        return newText;
    }

}
