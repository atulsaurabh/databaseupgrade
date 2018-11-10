package com.technoride.server.databaseupgrade.utils;

import com.technoride.server.databaseupgrade.dto.Parameter;
import com.technoride.server.databaseupgrade.dto.VersionAndCurrentFile;
import com.technoride.server.databaseupgrade.setting.VersionAndFile;

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
               if (parameter == null)
                   break;
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

    public VersionAndCurrentFile getVesionAndCurrentFile()
    {
        try
        {
          File file = new File(getClass().getResource("/config/mysqldump.config").getPath());
          Scanner scanner = new Scanner(new FileInputStream(file));
          VersionAndCurrentFile versionAndCurrentFile = new VersionAndCurrentFile();
          while (scanner.hasNextLine())
          {
             String line = scanner.nextLine();
             String [] splits  = line.split(":");
             if (splits[0].equals(VersionAndFile.CURRENT_FILE))
             {
                versionAndCurrentFile.setCurrentFile(splits[1]);
             }
             else
                 if (splits[0].equals(VersionAndFile.VERSION))
                 {
                     versionAndCurrentFile.setVersion(Long.parseLong(splits[1]));
                 }

          }
          return versionAndCurrentFile;
        }
        catch (FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        }
        return null;
    }

    public void saveVersionAndCurrentFile(VersionAndCurrentFile versionAndCurrentFile)
    {
        try
        {
            File file = new File(getClass().getResource("/config/mysqldump.config").getPath());
            PrintWriter writer = new PrintWriter(new FileOutputStream(file));
            String currentFile = VersionAndFile.CURRENT_FILE+":"+versionAndCurrentFile.getCurrentFile();
            String version = VersionAndFile.VERSION+":"+versionAndCurrentFile.getVersion();
            writer.println(currentFile);
            writer.println(version);
            writer.flush();
        }
        catch (FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        }
    }

}
