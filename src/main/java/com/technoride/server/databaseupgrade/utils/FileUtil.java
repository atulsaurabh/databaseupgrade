package com.technoride.server.databaseupgrade.utils;

import com.technoride.server.databaseupgrade.dto.Parameter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileUtil
{
    public Map<String,Parameter> populateProperty()
    {
        Map<String,Parameter> parameterMap = new HashMap<>();
        try {

            File file = new File(getClass().
                    getResource("/config/dvm.cfg").getPath());
            FileInputStream fileInputStream = new FileInputStream(file);
            Scanner scanner = new Scanner(fileInputStream);
            while (scanner.hasNextLine())
            {
               String line = scanner.nextLine();
               Parameter parameter=Strings.compile(line);
               parameterMap.put(parameter.getPropertyName(),parameter);
            }
        }
        catch (FileNotFoundException fnf)
        {
            fnf.printStackTrace();
        }

        return parameterMap;

    }

    public void saveParameter(Map<String,Parameter> parameterMap)
    {
        try {
            File file = new File(getClass().
                    getResource("/config/dvm.cfg").getPath());
            PrintWriter printWriter=new PrintWriter(
                    new FileOutputStream(file),true);
            parameterMap.forEach((s, parameter) -> {
                String line = "";
                line += s+":"+parameter.getValue();
                line += ":"+parameter.getPropertyType();
                line += "#"+parameter.getDescription();
                printWriter.println(line);
                printWriter.flush();
            });
        }
        catch (FileNotFoundException fnf)
        {
           fnf.printStackTrace();
        }
    }
}
