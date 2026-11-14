package com.healthcheck;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DBConnection {

    private static String username;

    private static String password;

    @Value("${spring.datasource.username}")
    public void setUsername(String username) {
        DBConnection.username = username;
    }

    @Value("${spring.datasource.password}")
    public void setPassword(String password) {
        DBConnection.password = password;
    }

    public static Connection getConnection()
            throws Exception {

        String url =
                "jdbc:sqlserver://gal02059:1433;"
                + "databaseName=SQL_DETAILS;"
                + "encrypt=true;"
                + "trustServerCertificate=true;";

        return DriverManager.getConnection(
                url,
                username,
                password);
    }

    public static Connection getConnection(
            String serverName,
            int portNumber)
            throws Exception {

        String url =
                "jdbc:sqlserver://"
                + serverName
                + ":"
                + portNumber
                + ";databaseName=master;"
                + "encrypt=true;"
                + "trustServerCertificate=true;";

        return DriverManager.getConnection(
                url,
                username,
                password);
    }
}