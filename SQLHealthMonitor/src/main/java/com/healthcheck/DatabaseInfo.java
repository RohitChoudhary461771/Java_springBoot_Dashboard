package com.healthcheck;

public class DatabaseInfo {

    private String name;
    private String status;
    private String lastBackupDate;
    private String backupStatusColor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getLastBackupDate() {
        return lastBackupDate;
    }
    
    public void setLastBackupDate(String lastBackupDate) {
        this.lastBackupDate = lastBackupDate;
    }
    
    public String getBackupStatusColor() {
        return backupStatusColor;
    }
    
    public void setBackupStatusColor(String backupStatusColor) {
        this.backupStatusColor = backupStatusColor;
    }
    
}