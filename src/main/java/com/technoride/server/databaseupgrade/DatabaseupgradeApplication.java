package com.technoride.server.databaseupgrade;

import com.technoride.server.databaseupgrade.dto.Parameter;
import com.technoride.server.databaseupgrade.loader.Loader;
import com.technoride.server.databaseupgrade.mode.Mode;
import com.technoride.server.databaseupgrade.mode.ModeInfo;
import com.technoride.server.databaseupgrade.setting.Description;
import com.technoride.server.databaseupgrade.setting.ThreadSetting;
import com.technoride.server.databaseupgrade.utils.EncodeDecodeMode;
import com.technoride.server.databaseupgrade.utils.FileUtil;
import com.technoride.server.databaseupgrade.utils.MySQLProcess;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.Map;

/**
 * @author Atul Saurabh
 * @see javafx.application.Application
 * @see org.springframework.boot.ApplicationRunner
 * @serial 1.0
 * @since 1.0
 * @version 1.0
 * <h1>Story Line</h1>
 *  <p>
 *      The code name of this project will be <u><b>ADYATAN</b></u>.
 *      There are many versions of the databases available with client.
 *      whenever some requirement comes, we update the corresponding
 *      database. By this way many different version of database were
 *      created. And day by day it become hard to maintain the uniformity
 *      across the clients.That is where this project comes in picture.
 *
 *  </p>
 *
 */



@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DatabaseupgradeApplication extends Application implements ApplicationRunner
{

    @Autowired
    private Loader loader;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private MySQLProcess mySQLProcess;
    @Autowired
    private EncodeDecodeMode encodeDecodeMode;

    private static Stage primeStage;

    @Override
    public void start(Stage primaryStage)
    {
        primeStage=primaryStage;
        String [] args = getParameters().getRaw().toArray(new String[getParameters().getRaw().size()]);
        SpringApplication.run(DatabaseupgradeApplication.class, args);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Map<String,Parameter> parameterMap = fileUtil.populateProperty();
        if (parameterMap.isEmpty())
        {
            Parameter waitparameter = new Parameter();
            waitparameter.setPropertyName("WAIT_TIME");
            waitparameter.setPropertyValue(ThreadSetting.ONE_DAY);
            waitparameter.setPropertyType("CS");
            waitparameter.setDescription(Description.THREAD_DESCRIPTION);
            parameterMap.put(waitparameter.getPropertyName(),waitparameter);

            Parameter restIpParameter = new Parameter();
            restIpParameter.setPropertyName("REST_SERVER_IP_ADDRESS");
            restIpParameter.setPropertyType("TS");
            restIpParameter.setPropertyValue("localhost");
            restIpParameter.setDescription(Description.REST_SERVER);
            parameterMap.put(restIpParameter.getPropertyName(),restIpParameter);

            Parameter restPortProperties = new Parameter();
            restPortProperties.setPropertyName("REST_SERVER_PORT");
            restPortProperties.setPropertyValue("8080");
            restPortProperties.setPropertyType("TI");
            restPortProperties.setDescription(Description.SERVER_PORT);
            parameterMap.put(restPortProperties.getPropertyName(),restPortProperties);

            fileUtil.saveParameter(parameterMap);
        }
        if(args.containsOption("config"))
        {
            openPrimaryWindow();
        }
        else
        {
            Mode mode = encodeDecodeMode.decodeMode();
            if(mode.getWORKING_MODE().equals(ModeInfo.SETUP.getMode()))
            {
               openPrimaryWindow();
            }
            else
            {
                // Run the command mode

                Platform.runLater(()->{
                    mySQLProcess.restore();
                });

            }
        }


    }

    private void openPrimaryWindow()
    {
        primeStage.setScene(new Scene(loader.load("mysqlsetup.fxml")));
        primeStage.setResizable(false);
        primeStage.setTitle("Setup MySql Home");
        primeStage.show();
    }
}
