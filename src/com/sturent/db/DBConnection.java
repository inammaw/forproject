
package com.sturent.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://mysql-178f6340-sturent.h.aivencloud.com:17980/defaultdb?sslMode=REQUIRED";

    private static final String USER = "avnadmin";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}




