package com.healthcheck;

import java.sql.Timestamp;

public class ServerHealthStatus {

    private int serverId;

    private String serverName;
    
    private String instanceName;

    private String site;

    private String environment;

    private String connectionStatus;

    private String healthStatus;

    private String lastIssue;

    private Timestamp lastRefreshTime;
    
    private String lastRefreshTimeDisplay;

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
    	this.serverName = serverName;
    }
    
    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
    
    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    
    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
    
    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }
    
    public String getLastIssue() {
        return lastIssue;
    }

    public void setLastIssue(String lastIssue) {
        this.lastIssue = lastIssue;
    }
    
    public Timestamp getLastRefreshTime() {
        return lastRefreshTime;
    }

    public void setLastRefreshTime(Timestamp lastRefreshTime) {
        this.lastRefreshTime = lastRefreshTime;
    }

    
    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
    // getters/setters
    
    

    public String getLastRefreshTimeDisplay() {
        return lastRefreshTimeDisplay;
    }

    public void setLastRefreshTimeDisplay(String lastRefreshTimeDisplay) {
        this.lastRefreshTimeDisplay = lastRefreshTimeDisplay;
    }
}
