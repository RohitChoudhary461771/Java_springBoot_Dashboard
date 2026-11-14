package com.healthcheck;

public class ServerResourceInfo {

    private double totalServerRAMGB;
    private double sqlMaxMemoryGB;
    private double sqlMemoryUsedGB;
    private int sqlCpuUsage;
    private double serverMemoryUtilization;
    private int totalCpuUsage;

    public double getTotalServerRAMGB() {
        return totalServerRAMGB;
    }

    public void setTotalServerRAMGB(double totalServerRAMGB) {
        this.totalServerRAMGB = totalServerRAMGB;
    }

    public double getSqlMaxMemoryGB() {
        return sqlMaxMemoryGB;
    }

    public void setSqlMaxMemoryGB(double sqlMaxMemoryGB) {
        this.sqlMaxMemoryGB = sqlMaxMemoryGB;
    }

    public double getSqlMemoryUsedGB() {
        return sqlMemoryUsedGB;
    }

    public void setSqlMemoryUsedGB(double sqlMemoryUsedGB) {
        this.sqlMemoryUsedGB = sqlMemoryUsedGB;
    }

    public int getSqlCpuUsage() {
        return sqlCpuUsage;
    }

    public void setSqlCpuUsage(int sqlCpuUsage) {
        this.sqlCpuUsage = sqlCpuUsage;
    }
    
    public double getServerMemoryUtilization() {
        return serverMemoryUtilization;
    }

    public int getTotalCpuUsage() {
        return totalCpuUsage;
    }
    
    public void setServerMemoryUtilization(
            double serverMemoryUtilization) {

        this.serverMemoryUtilization =
                serverMemoryUtilization;
    }

    public void setTotalCpuUsage(
            int totalCpuUsage) {

        this.totalCpuUsage =
                totalCpuUsage;
    }
}