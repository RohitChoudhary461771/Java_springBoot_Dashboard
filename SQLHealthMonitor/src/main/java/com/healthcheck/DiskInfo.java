package com.healthcheck;

public class DiskInfo {

    private String drive;
    private double totalGB;
    private double freeGB;
    private double freePercent;
    private String diskStatusColor;

    public String getDrive() {
        return drive;
    }

    public void setDrive(String drive) {
        this.drive = drive;
    }

    public double getTotalGB() {
        return totalGB;
    }

    public void setTotalGB(double TotalGB) {
        this.totalGB = TotalGB;
    }

    public double getFreeGB() {
        return freeGB;
    }

    public void setFreeGB(double FreeGB) {
        this.freeGB = FreeGB;
    }

    public double getFreePercent() {
        return freePercent;
    }

    public void setFreePercent(double freePercent) {
        this.freePercent = freePercent;
    }
    
    
    public String getDiskStatusColor() {
        return diskStatusColor;
    }

    public void setDiskStatusColor(String diskStatusColor) {
        this.diskStatusColor = diskStatusColor;
    }
}