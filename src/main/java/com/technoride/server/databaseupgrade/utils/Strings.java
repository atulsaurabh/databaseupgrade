package com.technoride.server.databaseupgrade.utils;

import com.technoride.server.databaseupgrade.dto.Parameter;

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


    public static Parameter compile(String line)
    {
        String [] splits = line.split(":");
        String comment = splits[2];
        String [] typeAndDescription = comment.split("#");
        Parameter parameter = new Parameter();
        parameter.setPropertyName(splits[0]);
        parameter.setDescription(typeAndDescription[1]);
        parameter.setPropertyType(typeAndDescription[0]);
        parameter.setPropertyValue(splits[1]);
        return parameter;
    }

}
