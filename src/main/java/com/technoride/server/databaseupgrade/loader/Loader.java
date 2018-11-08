package com.technoride.server.databaseupgrade.loader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

public class Loader
{
    @Autowired
    private ApplicationContext context;


    public Parent load(String guiname)
    {
        FXMLLoader loader=new FXMLLoader();
        loader.setControllerFactory(context::getBean);
        loader.setLocation(getClass().getResource("/gui/"+guiname));
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public NodeAndController loadGUI(String guiname)
    {
        FXMLLoader loader=new FXMLLoader();
        loader.setControllerFactory(context::getBean);
        loader.setLocation(getClass().getResource("/gui/"+guiname));
        try {
            Parent parent=loader.load();
            Object controller = loader.getController();
            NodeAndController nodeAndController = new NodeAndController();
            nodeAndController.setController(controller);
            nodeAndController.setParent(parent);
            return nodeAndController;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


}
