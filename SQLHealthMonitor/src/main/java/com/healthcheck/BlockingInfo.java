package com.healthcheck;

public class BlockingInfo {

    private int blockedSpid;

    private int blockingSpid;

    private String databaseName;

    private String programName;

    private int waitSeconds;

    private String commandText;

    private String waitColor;

    public int getBlockedSpid() {
        return blockedSpid;
    }

    public void setBlockedSpid(int blockedSpid) {
        this.blockedSpid = blockedSpid;
    }

    public int getBlockingSpid() {
        return blockingSpid;
    }

    public void setBlockingSpid(int blockingSpid) {
        this.blockingSpid = blockingSpid;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public int getWaitSeconds() {
        return waitSeconds;
    }

    public void setWaitSeconds(int waitSeconds) {
        this.waitSeconds = waitSeconds;
    }

    public String getCommandText() {
        return commandText;
    }

    public void setCommandText(String commandText) {
        this.commandText = commandText;
    }

    public String getWaitColor() {
        return waitColor;
    }

    public void setWaitColor(String waitColor) {
        this.waitColor = waitColor;
    }
}