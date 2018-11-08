package com.technoride.server.databaseupgrade.controller;



import javafx.application.Platform;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;


@Controller
public class ConsoleController {

    @FXML
    private ProgressBar taskProgress;
    private long linenumber;
    private Service<Void> outputWriterThread;
    private Service<Void> errorWriterThread;


    @FXML
    private Label progressLabel;

    @FXML
    private Button stopButton;

    @FXML
    private Button restartButton;

    @FXML
    private Button closeButton;




    @FXML
    public void initialize()
    {
        progressLabel.setStyle("-fx-text-fill: green;-fx-font-size: 10px");
        taskProgress.setStyle("-fx-accent:ORANGE");
        restartButton.setDisable(true);
        restartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                restartButton.setDisable(true);
                progressLabel.textProperty().bind(outputWriterThread.messageProperty());
                taskProgress.progressProperty().bind(outputWriterThread.progressProperty());
                outputWriterThread.restart();
                stopButton.setDisable(false);
            }
        });
    }

    public void writeOutputToConsole(final BufferedReader bufferedReader)
    {
         outputWriterThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        long currentLineNumber=0;
                        String line = "";
                        while ((line = bufferedReader.readLine()) != null)
                        {
                            if(line.trim().startsWith("-") || line.trim().equals(""))
                                continue;
                            updateMessage(line);
                            currentLineNumber++;
                            updateProgress((currentLineNumber*100)/linenumber,100);
                            try {
                                Thread.sleep(ThreadInfo.MINIMAL_THREAD_SLEEP_TIME);
                            }
                            catch (Exception ex)
                            {

                            }
                        }
                        updateMessage("DONE");
                        updateProgress(100,100);
                        return null;
                    }
                };
            }

             @Override
             public boolean cancel() {

                    progressLabel.textProperty().unbind();
                    taskProgress.progressProperty().unbind();
                    //bufferedReader.close();
                    Platform.runLater(()->{
                        Alert alert=new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Progress cancelled");
                        alert.setHeaderText("Cancelling..");
                        alert.setTitle("Operation");
                        alert.showAndWait();
                    });
                    return true;
             }
         };
        progressLabel.textProperty().bind(outputWriterThread.messageProperty());
        taskProgress.progressProperty().bind(outputWriterThread.progressProperty());
        outputWriterThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Platform.runLater(()->{
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Updation Completed Successfully");
                    alert.setHeaderText("Success");
                    alert.setTitle("Updation");
                    alert.showAndWait();
                });
            }
        });
        outputWriterThread.start();
    }

    public void writeErrorToConsole(final BufferedReader bufferedReader)
    {
        errorWriterThread= new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        String line = "";
                        while (true)
                        {
                            if (bufferedReader.ready())
                            {
                              outputWriterThread.cancel();
                              break;
                            }
                            else
                            updateMessage(line);
                        }
                     return null;
                    }
                };
            }
        };
        errorWriterThread.restart();
    }



    public void setLinenumber(long linenumber) {
        this.linenumber = linenumber;
    }

    @FXML
    public void stopOutputThread(ActionEvent event)
    {
        //if(outputWriterThread.getState()==Worker.State.RUNNING)
        outputWriterThread.cancel();
        restartButton.setDisable(false);
        stopButton.setDisable(true);
    }


    public void closeUpdate(ActionEvent actionEvent)
    {
        System.exit(0);
    }
}
