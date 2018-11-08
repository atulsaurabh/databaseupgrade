package com.technoride.server.databaseupgrade;

import com.technoride.server.databaseupgrade.loader.Loader;
import com.technoride.server.databaseupgrade.mode.Mode;
import com.technoride.server.databaseupgrade.mode.ModeInfo;
import com.technoride.server.databaseupgrade.utils.EncodeDecodeMode;
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
