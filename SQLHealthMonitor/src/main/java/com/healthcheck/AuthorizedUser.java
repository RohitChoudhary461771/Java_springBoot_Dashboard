package com.healthcheck;

import java.util.Date;

public class AuthorizedUser {

    private Integer userId;
    private String userName;
    private String displayName;
    private String roleName;
    private Boolean isActive;
    private Date createdDate;
    private String createdBy;

    // Default Constructor
    public AuthorizedUser() {
    }

    // Parameterized Constructor
    public AuthorizedUser(Integer userId, String userName,
                          String displayName, String roleName,
                          Boolean isActive, Date createdDate,
                          String createdBy) {
        this.userId = userId;
        this.userName = userName;
        this.displayName = displayName;
        this.roleName = roleName;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "AuthorizedUser{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", roleName='" + roleName + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}