package com.technoride.server.databaseupgrade.mode;

import java.io.Serializable;

public class Mode implements Serializable {
    private String WORKING_MODE="setup";


    public String getWORKING_MODE() {
        return WORKING_MODE;
    }

    public void setWORKING_MODE(String WORKING_MODE) {
        this.WORKING_MODE = WORKING_MODE;
    }
}
