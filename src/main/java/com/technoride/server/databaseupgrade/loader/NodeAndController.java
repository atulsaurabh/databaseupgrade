package com.technoride.server.databaseupgrade.loader;

import javafx.scene.Parent;

public class NodeAndController {
    private Parent parent;
    private Object controller;

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }
}
