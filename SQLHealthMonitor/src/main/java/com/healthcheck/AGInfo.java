package com.healthcheck;

public class AGInfo {

    private String agName;
    private String role;
    private String syncStatus;
    private String replicaServer;
    
    public String getReplicaServer() {
        return replicaServer;
    }

    public void setReplicaServer(String replicaServer) {
        this.replicaServer = replicaServer;
    }

    public String getAgName() {
        return agName;
    }

    public void setAgName(String agName) {
        this.agName = agName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }
}