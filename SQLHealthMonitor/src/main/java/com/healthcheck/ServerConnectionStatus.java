package com.healthcheck;

public class ServerConnectionStatus {

    private String serverName;
    private String instanceName;
    private String status;
    private String healthStatus;
    private String healthColor;
    private String lastIssue;
    
    
    private int serverId;
    private String environment;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
    
    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    
    public String getDisplayName() {

        if(instanceName != null &&
           !instanceName.trim().isEmpty()) {

            return serverName + "\\" + instanceName;
        }

        return serverName;
    }
    
    
    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getHealthColor() {
        return healthColor;
    }

    public void setHealthColor(String healthColor) {
        this.healthColor = healthColor;
    }
    
    
    public String getLastIssue() {
        return lastIssue;
    }

    public void setLastIssue(String lastIssue) {
        this.lastIssue = lastIssue;
    }
    
}