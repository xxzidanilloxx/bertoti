package com.fatec.padroes.singleton;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            Connection connection = databaseConnection.getConnection();

            if (connection != null && !connection.isClosed()) {
                System.out.println("Sucesso!");
            } else {
                System.out.println("Erro.");
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
