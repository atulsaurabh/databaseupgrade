package com.technoride.server.databaseupgrade.dto;

public class VersionAndCurrentFile
{
    private long version;
    private String currentFile;


    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }
}
