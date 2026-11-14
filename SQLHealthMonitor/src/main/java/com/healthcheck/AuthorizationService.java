package com.healthcheck;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthorizationService {

    public AuthorizedUser getAuthorizedUser(String userName) {

        String sql = """
                SELECT UserID,
                       UserName,
                       DisplayName,
                       RoleName,
                       IsActive,
                       CreatedDate,
                       CreatedBy
                FROM AuthorizedUsers
                WHERE UserName = ?
                AND IsActive = 1
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userName);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    AuthorizedUser user = new AuthorizedUser();

                    user.setUserId(rs.getInt("UserID"));
                    user.setUserName(rs.getString("UserName"));
                    user.setDisplayName(rs.getString("DisplayName"));
                    user.setRoleName(rs.getString("RoleName"));
                    user.setIsActive(rs.getBoolean("IsActive"));
                    user.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    user.setCreatedBy(rs.getString("CreatedBy"));

                    return user;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean isAuthorized(String userName) {
        return getAuthorizedUser(userName) != null;
    }
}