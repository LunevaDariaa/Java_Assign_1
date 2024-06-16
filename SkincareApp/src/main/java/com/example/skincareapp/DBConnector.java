package com.example.skincareapp;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DBConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/skincare?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "4507";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
