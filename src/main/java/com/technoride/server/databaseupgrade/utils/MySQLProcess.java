package com.technoride.server.databaseupgrade.utils;

import com.technoride.server.databaseupgrade.controller.ConsoleController;
import com.technoride.server.databaseupgrade.controller.ThreadInfo;
import com.technoride.server.databaseupgrade.loader.Loader;
import com.technoride.server.databaseupgrade.loader.NodeAndController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.omg.SendingContext.RunTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MySQLProcess
{

    @Autowired
    private Loader loader;

    private String[] getMySqlProperties(){
        try {
            Scanner scanner=new Scanner(
                    new FileInputStream(
                            new File(getClass().getResource("/config/mysql.cfg").getPath())));
            String[] str= new String[3];
            int i=0;
            while(scanner.hasNextLine()) {
                str[i]=scanner.nextLine();
                 i++;
            }
            return str;

        }
        catch (FileNotFoundException fnf)
        {
            fnf.printStackTrace();
        }
        return null;
    }


private Map<String,String> getParameters()
{
    Map<String,String> parameters = new HashMap<>();
    try {
        FileInputStream fileInputStream = new FileInputStream(
                new File(getClass().getResource("/config/mysqldump.config").getPath()));
        Scanner scanner = new Scanner(fileInputStream);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String [] values = line.split(":");
            parameters.put(values[0],values[1]);
        }
    }
    catch (FileNotFoundException fnf)
    {
      fnf.printStackTrace();
    }

    return parameters;
}

public boolean restore(String fileName)
{
    /*Stage stage=new Stage();
    NodeAndController nodeAndController = loader.loadGUI("interactiveconsole.fxml");
    Scene scene = new Scene(nodeAndController.getParent());
    ConsoleController consoleController = (ConsoleController)nodeAndController.getController();
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();*/

    String [] properties = getMySqlProperties();
    /*long lineNumbers=0;
    try {
        lineNumbers = findFileSize(fileName);
        //consoleController.setLinenumber(lineNumbers);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    try {
        Thread.sleep(ThreadInfo.MINIMAL_THREAD_SLEEP_TIME);
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
*/
    String home="\""+properties[0]+"\\bin\\mysql.exe\"";
    fileName=fileName.substring(1);
    try {
        String [] commands = new String[]{
                home,
                "--user="+properties[1],
                "--password="+properties[2],
                "--verbose",
                "--execute=source "+fileName
        };
        Process process=Runtime.getRuntime().exec(commands);
        InputStream inputStream=process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        long count=0;
        while ((line=bufferedReader.readLine()) != null)
        {
            System.out.print("#");
            count++;
            try {
                Thread.sleep(ThreadInfo.MINIMAL_THREAD_SLEEP_TIME);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            if (count == 100)
            {
                System.out.println();
                count=0;
            }
        }

        InputStream errorInputStream = process.getErrorStream();
        BufferedReader errorbufferedreader = new BufferedReader(new InputStreamReader(errorInputStream));

        while ((line = errorbufferedreader.readLine()) != null)
        {
            if(!line.startsWith("Warning"))
                Logger.getGlobal().log(Level.SEVERE,line);
            try {
                Thread.sleep(ThreadInfo.MODERATE_THREAD_SLEEP_TIME);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
       // consoleController.writeOutputToConsole(bufferedReader);
       // BufferedReader errorBuffferedReader=new BufferedReader(new InputStreamReader(process.getErrorStream()));
       // consoleController.writeErrorToConsole(errorBuffferedReader);

        return true;
    }
    catch (IOException io)
    {
        io.printStackTrace();
    }
    /*catch (InterruptedException in)
    {
        in.printStackTrace();
    }*/

    return false;
}


   private long findFileSize(String filepath) throws ExecutionException,InterruptedException
   {
       ExecutorService lineNumberService = Executors.newSingleThreadExecutor();
       Callable<Long> lineNumberCallBack = ()->{
           Path  path=Paths.get(filepath);
           return Files.lines(path).filter(s -> {
             return !(s.trim().equals("") || s.trim().startsWith("--"));
           }).count();
       };

       Future<Long> lineNumberFinderThread = lineNumberService.submit(lineNumberCallBack);
       return lineNumberFinderThread.get();
   }
}
