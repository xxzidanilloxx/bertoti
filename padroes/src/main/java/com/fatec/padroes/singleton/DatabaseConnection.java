package com.fatec.padroes.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum DatabaseConnection {
    INSTANCE;

    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/teste";
    private static final String USER = "root";
    private static final String PASSWORD = "12345678";

    DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro.", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
