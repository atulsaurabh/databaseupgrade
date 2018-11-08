package com.technoride.server.databaseupgrade.mode;

public enum ModeInfo {
    SETUP("setup"),
    WORKER("worker");

    private String mode;
    ModeInfo(String mode)
    {
        this.mode=mode;
    }

    public String getMode()
    {
        return this.mode;
    }
}
