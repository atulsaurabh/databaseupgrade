package com.technoride.server.databaseupgrade.controller;

import com.technoride.server.databaseupgrade.mode.Mode;
import com.technoride.server.databaseupgrade.mode.ModeInfo;
import com.technoride.server.databaseupgrade.utils.EncodeDecodeMode;
import com.technoride.server.databaseupgrade.utils.MySQLProcess;
import com.technoride.server.databaseupgrade.utils.Strings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;


@Controller
public class MySqlSetupController {

    @Autowired
    private EncodeDecodeMode encodeDecodeMode;
    @FXML
    private TextField homepath;

    @FXML
    private TextField mysqlUserTextField;

    @FXML
    private PasswordField mySqlPasswordField;


    @Autowired
    private MySQLProcess mySQLProcess;

    @FXML
    void browseHomePath(ActionEvent event) {
        homepath.setText("");
        String architecture = System.getProperty("os.arch");
        System.out.println(architecture);
        String initialDirectory="";
        if (architecture.contains("64"))
        {
            initialDirectory="c:\\Program Files";
        }
        else
            initialDirectory="c:\\Program Files (x86)";
        DirectoryChooser chooser=new DirectoryChooser();
        chooser.setInitialDirectory(new File(initialDirectory));
        chooser.setTitle("Select MySql Home");
        File mySQLHomeDirectory=chooser.showDialog(null);
        if(isValidMySqlHome(mySQLHomeDirectory))
        {
            try {
                homepath.setText(mySQLHomeDirectory.getCanonicalPath());
            }
            catch (IOException io)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Path Not Exists");
            }

        }
        else
        {
              showMySqlInvalidHomeSelectedMessage();
        }
    }


    private void showMySqlInvalidHomeSelectedMessage()
    {
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Home not set");
        alert.setContentText("Invalid MySQL home directory is selected");
        alert.setHeaderText("Invalid Home");
        alert.showAndWait();
    }

    private boolean isValidMySqlHome(File mySQLHomeDirectory) {
        if (mySQLHomeDirectory == null)
            return false;
        File[] allchild=mySQLHomeDirectory.listFiles();
        return Arrays.asList(allchild).stream().anyMatch(file -> {
            if (file.getName().equals("bin"))
            {
                return Arrays.asList(file.listFiles()).stream().anyMatch(file1 -> {
                    return file1.getName().equals("mysql.exe");
                });
            }
            else
                return false;
        });
    }

    @FXML
    void resetHomePath(ActionEvent event) {
      homepath.setText("");
    }

    @FXML
    void setHomePath(ActionEvent event)
    {
       String mysqlHome=homepath.getText();
       File mySqlHomeFile=new File(mysqlHome);
       if (!isValidMySqlHome(mySqlHomeFile))
       {
            showMySqlInvalidHomeSelectedMessage();
       }
       else
       {
           try {
               String configFile = this.getClass().getResource("/config/dvm.cfg").toURI().getPath();
               File file=new File(configFile);
               if (!file.exists())
                   file.createNewFile();
               FileOutputStream fileOutputStream=new FileOutputStream(new File(configFile));
               PrintWriter writer=new PrintWriter(new BufferedOutputStream(fileOutputStream),true);
               writer.println(mysqlHome);
               writer.flush();
               //second line

               writer.println(mysqlUserTextField.getText());
               writer.flush();
               // third line

               writer.println(mySqlPasswordField.getText());
               writer.flush();
               Mode mode=new Mode();
               mode.setWORKING_MODE(ModeInfo.WORKER.getMode());
               encodeDecodeMode.encodeMode(mode);
               Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
               alert.setHeaderText("Configuration Done..");
               alert.setContentText("MySQL home is configured successfully");
               alert.show();

               mySQLProcess.restore();

           }
           catch (IOException io)
           {
               Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Unable To Configure MySQL Home");
           }
           catch (URISyntaxException syn)
           {
               Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Unable To Configure MySQL Home");
           }
       }

    }



}
