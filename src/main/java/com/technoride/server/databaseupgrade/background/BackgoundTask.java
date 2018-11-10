package com.technoride.server.databaseupgrade.background;

import com.technoride.server.databaseupgrade.client.RestClient;
import com.technoride.server.databaseupgrade.dto.Parameter;
import com.technoride.server.databaseupgrade.dto.VersionAndCurrentFile;
import com.technoride.server.databaseupgrade.utils.FileUtil;
import com.technoride.server.databaseupgrade.utils.MySQLProcess;
import com.technoride.server.databaseupgrade.utils.TimeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BackgoundTask
{
    @Autowired
    private RestClient restClient;
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private MySQLProcess mySQLProcess;
    public void runInBackground()
    {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        Runnable runnable = () -> {
          Logger.getGlobal().log(Level.INFO,"Update Process Initiated");
          long server_version = restClient.getVersion();
          long client_version = fileUtil.getVesionAndCurrentFile().getVersion();




          if (server_version <= client_version) {
              Logger.getGlobal().log(Level.INFO,"No Update Found");
              return;
          }
          else
          {
              Logger.getGlobal().log(Level.INFO,"Update Found");
              try {
                  Resource resource = restClient.getFile();
                  Scanner scanner = new Scanner(resource.getInputStream());
                  File file=new File(getClass().
                          getResource("/config").getPath()+"/mysqlbackup_"+server_version+".sql");
                  if (!file.exists())
                      file.createNewFile();
                  PrintWriter writer = new PrintWriter(new FileOutputStream(file),true);
                  Logger.getGlobal().log(Level.INFO,"Update Downloading");
                  while (scanner.hasNextLine())
                  {
                      writer.println(scanner.nextLine());
                      writer.flush();
                  }
                  Logger.getGlobal().log(Level.INFO,"Update Downloaded");

                  VersionAndCurrentFile versionAndCurrentFile = fileUtil.getVesionAndCurrentFile();
                  versionAndCurrentFile.setCurrentFile("mysqlbackup_"+server_version+".sql");
                  versionAndCurrentFile.setVersion(server_version);

                  String path = getClass().getResource("/config/"+versionAndCurrentFile.getCurrentFile()).getPath();
                  if(mySQLProcess.restore(path)) {
                      fileUtil.saveVersionAndCurrentFile(versionAndCurrentFile);
                      Logger.getGlobal().log(Level.INFO,"Database Updated");
                  }

              }
              catch (IOException io)
              {
                 io.printStackTrace();
              }

          }
        };

        try {
            TimeConverter timeConverter = new TimeConverter();
            Map<String,Parameter> parameterMap = fileUtil.populateProperty();
            long timeToRepeater = timeConverter.getScheduleTime(parameterMap.get("WAIT_TIME").getValue());
            scheduledExecutorService.scheduleAtFixedRate(runnable,0,timeToRepeater,TimeUnit.MILLISECONDS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
